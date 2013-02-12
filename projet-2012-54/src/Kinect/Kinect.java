package Kinect;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import KinectControle.KinectSource;
import Vue.Vue;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.CvBox2D;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvFont;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc.CvConvexityDefect;

public class Kinect implements Runnable
{
	private Thread runner;
	private MainPosition mainPositionLeft = new MainPosition(); 
	private MainPosition mainPositionRight = new MainPosition();
	private IplImage imageTraitement;
	private long timeLastGrab;
	private long[] timeList = new long[4];
    private reconnaissanceMvt reconnaissanceMvtLeft;
    private reconnaissanceMvt reconnaissanceMvtRight;
    private Vue vue;

	public Kinect(KinectSource kinectSource, Vue vue)
    {
		reconnaissanceMvtLeft = new reconnaissanceMvt(mainPositionLeft, "left", kinectSource);
		reconnaissanceMvtRight = new reconnaissanceMvt(mainPositionRight, "right", kinectSource);

		runner = new Thread(this, "kinect");
		runner.start();
		this.vue = vue;
    }

	public void run()
	{
		try
		{
	    	//OpenCVFrameGrabber grabber = new OpenCVFrameGrabber("video/depth_pact54_test1.mkv");
	    	//OpenCVFrameGrabber grabber = new OpenCVFrameGrabber("video/depth_pact54_test2.mpg");
	    	//OpenCVFrameGrabber grabber = new OpenCVFrameGrabber("video/depth_pact42_test1.mkv");
	    	//OpenCVFrameGrabber grabber = new OpenCVFrameGrabber("video/depth_pact54_test2.mkv");
			//OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
	        KinectGrabber grabber = new KinectGrabber();

			grabber.start();

			IplImage imageGrab = grabber.grab();
			BufferedImage bufferedImageGrab = imageGrab.getBufferedImage();
			int width  = imageGrab.width();
			int height = imageGrab.height();
	    	CvPoint minPoint = new CvPoint();
	    	CvPoint maxPoint = new CvPoint();
	    	double[] minVal = new double[1];
	    	double[] maxVal = new double[1];
	    	
	    	vue.getVueKinect().setKinectImage(bufferedImageGrab);

	    	// creation window used to display the video, the object in JavaCv can use the material acceleration
	    	JFrame Fenetre = new JFrame();
			Fenetre.setLayout(new GridLayout(1, 2));
			Fenetre.setTitle("module JavaCV");
			Fenetre.setSize(width*2+40, height+60);
			Fenetre.setLocationRelativeTo(null);
			Fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			CanvasFrame fenetreFrame1 = new CanvasFrame("AVI Playback Demo");
			fenetreFrame1.setVisible(false);
			Fenetre.getContentPane().add(fenetreFrame1.getCanvas());

			CanvasFrame fenetreFrame2 = new CanvasFrame("AVI Playback Demo");
			fenetreFrame2.setVisible(false);
			Fenetre.getContentPane().add(fenetreFrame2.getCanvas());

			Fenetre.setVisible(true);

			CvMemStorage storage = CvMemStorage.create();

			while((imageGrab = grabber.grab()) != null)
			{
				timeLastGrab = System.currentTimeMillis();
/*
				imageTraitement = IplImage.create(width, height, IPL_DEPTH_8U, 1);
				cvCvtColor(imageGrab, imageTraitement, CV_RGB2GRAY);

				IplImage imageDislay2 = imageGrab.clone();
*/
				
				IplImage imageDislay2 = IplImage.create(width, height, IPL_DEPTH_8U, 3);
				cvCvtColor(imageGrab, imageDislay2, CV_GRAY2RGB);
				//OpenCV.cvCvtColor(imageGrab, imageDislay2, CV_GRAY2RGB);

				imageTraitement = imageGrab.clone();

				cvMinMaxLoc(imageTraitement, minVal, maxVal, minPoint, maxPoint, null);
				//OpenCV.cvMinMaxLoc(imageGrab, minVal, maxVal, minPoint, maxPoint, null);
	        	cvCircle(imageDislay2, minPoint, 3, CvScalar.YELLOW, -1, 8, 0);

	        	cvSmooth(imageTraitement, imageTraitement, CV_GAUSSIAN, 7, 7, 0, 0);

	        	IplImage imageThreshold = imageTraitement.clone();

	        	int nbrIteration = 0;
	        	int firstFound = 0;
	        	boolean isFound = false;
	        	ArrayList<CvPoint> centerList = new ArrayList<CvPoint>();

	        	while(!isFound && nbrIteration < 6) //5 iterations max
	        	{
	        		nbrIteration++;

	        		int nbrFound = 0;

		        	cvThreshold(imageTraitement, imageThreshold, minVal[0] + 5*nbrIteration, 255, CV_THRESH_BINARY_INV);
		        	//OpenCV.cvThreshold(imageTraitement, imageThreshold, minVal[0] + 5*nbrIteration, 255, CV_THRESH_BINARY_INV);

		        	CvSeq contour = new CvSeq();
		         	cvFindContours(imageThreshold.clone(), storage, contour, Loader.sizeof(CvContour.class), CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);

		            while(contour != null && !contour.isNull())
		            {
		            	if(contour.elem_size() > 0)
		                {
		            		double aire = cvContourArea(contour, CV_WHOLE_SEQ, 0);

		                	if(aire > 1000 && aire < 10000)
		                	{
		                		nbrFound += 1;
		                		
		                		if(nbrFound == 1 && firstFound == 0)
		                		{
		                			firstFound = nbrIteration;
		                		}
		                		if(nbrFound == 2)
		                		{
		                			isFound = true;
		                		}
		                 	}
		                }
		                contour = contour.h_next();
		            }
	        	}

	        	if(isFound)
	        	{
	        		cvThreshold(imageTraitement, imageThreshold, minVal[0] + 5*nbrIteration, 255, CV_THRESH_BINARY_INV);
	        		//OpenCV.cvThreshold(imageTraitement, imageThreshold, minVal[0] + 5*nbrIteration, 255, CV_THRESH_BINARY_INV);
	        	}
	        	else
	        	{
	        		cvThreshold(imageTraitement, imageThreshold, minVal[0] + 5*nbrIteration, 255, CV_THRESH_BINARY_INV);
	        		//OpenCV.cvThreshold(imageTraitement, imageThreshold, minVal[0] + 5*firstFound, 255, CV_THRESH_BINARY_INV);
	        	}

	        	CvSeq contour = new CvSeq();
	         	cvFindContours(imageThreshold.clone(), storage, contour, Loader.sizeof(CvContour.class), CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);

	            while(contour != null && !contour.isNull())
	            {
	            	if(contour.elem_size() > 0)
	                {
	            		double aire = cvContourArea(contour, CV_WHOLE_SEQ, 0);

	                	if(aire > 1000 && aire < 10000)
	                	{
	                		//cvDrawContours(imageDislay2, contour, CvScalar.BLUE, CvScalar.BLUE, -1, 1, CV_AA);

	                	    CvSeq points = cvApproxPoly(contour, Loader.sizeof(CvContour.class), storage, CV_POLY_APPROX_DP, cvContourPerimeter(contour)*0.002, 0);
	                		//cvDrawContours(imageDislay2, points, CvScalar.GREEN, CvScalar.GREEN, -1, 1, CV_AA);

	                		CvPoint centre = getContourCenter3(points, storage);
	                		cvCircle(imageDislay2, centre, 3, CvScalar.RED, -1, 8, 0);

	                		getFingers(contour, storage, imageDislay2);

	                		centerList.add(centre);
	                 	}
	                }
	                contour = contour.h_next();
	        	}

	        	getPositionHand(centerList);
	        	reconnaissanceMvtLeft.compute(timeLastGrab);
	        	reconnaissanceMvtRight.compute(timeLastGrab);

	    		cvCircle(imageDislay2, new CvPoint((int)mainPositionLeft.getFiltre(0)[0], (int)mainPositionLeft.getFiltre(0)[1]), 3, CvScalar.BLACK, -1, 8, 0);
	    		cvCircle(imageDislay2, new CvPoint((int)mainPositionRight.getFiltre(0)[0], (int)mainPositionRight.getFiltre(0)[1]), 3, CvScalar.BLACK, -1, 8, 0);

				CvFont font = new CvFont(CV_FONT_HERSHEY_COMPLEX, 0.6, 1); 

				if(mainPositionLeft.get(0)[0] == timeLastGrab)
				{
					cvPutText(imageDislay2, "Gauche", cvPoint((int)mainPositionLeft.getFiltre(0)[0]-20, (int)mainPositionLeft.getFiltre(0)[1]-10), font, CvScalar.BLUE);
				}
				if(mainPositionRight.get(0)[0] == timeLastGrab)
				{
					cvPutText(imageDislay2, "Droite", cvPoint((int)mainPositionRight.getFiltre(0)[0]-20, (int)mainPositionRight.getFiltre(0)[1]-10), font, CvScalar.RED);
				}

	        	CvFont fontFPS = new CvFont(CV_FONT_HERSHEY_COMPLEX, 0.6, 1); 
				cvPutText(imageDislay2, "FPS : "+Integer.toString(getFPS()), cvPoint(10, 460), fontFPS, CvScalar.BLACK);

				//IplImage resizeDisplay = IplImage.create(width/2, height/2, IPL_DEPTH_8U, 3);
				//cvResize(imageDislay2, resizeDisplay);

				fenetreFrame1.showImage(imageThreshold);
				fenetreFrame2.showImage(imageDislay2);

				cvClearMemStorage(storage);

				long timeEnd = 33-(int)(System.currentTimeMillis()-timeLastGrab);

				if(timeEnd > 0)
				{
					Thread.sleep(timeEnd);
				}

				//System.out.println(-timeEnd+33+" ms");
		    }

			grabber.stop();
			fenetreFrame1.dispose();
			fenetreFrame2.dispose();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
    }

	public void getFingers(CvSeq contour, CvMemStorage storage, IplImage imageDislay2)
	{
		CvSeq points = cvApproxPoly(contour, Loader.sizeof(CvContour.class), storage, CV_POLY_APPROX_DP, cvContourPerimeter(contour)*0.005, 0);
		cvDrawContours(imageDislay2, points, CvScalar.GREEN, CvScalar.GREEN, -1, 1, CV_AA);

		CvSeq convex = cvConvexHull2(points, storage, CV_COUNTER_CLOCKWISE, 1);
		cvDrawContours(imageDislay2, convex, CvScalar.RED, CvScalar.RED, -1, 1, CV_AA);

		CvSeq hull = cvConvexHull2(points, storage, CV_COUNTER_CLOCKWISE, 0);
		CvSeq defect = cvConvexityDefects(points, hull, storage);

		ArrayList<CvPoint> fingersList = new ArrayList<CvPoint>();
		ArrayList<CvPoint> fingersList2 = new ArrayList<CvPoint>();

		//System.out.println(getAngle(new CvConvexityDefect(cvGetSeqElem(defect, 0)).end(), new CvConvexityDefect(cvGetSeqElem(defect, 0)).depth_point(), new CvConvexityDefect(cvGetSeqElem(defect, 0)).start()));

		while(defect != null)
		{
    		for(int i = 0; i < defect.total(); i++)
    		{
				CvConvexityDefect convexityDefect = new CvConvexityDefect(cvGetSeqElem(defect, i));

				if(convexityDefect.depth() > 10)
				{

					if(getAngle(convexityDefect.end(), convexityDefect.depth_point(), convexityDefect.start()) < 100)
					{
/*						CvFont font = new CvFont(CV_FONT_HERSHEY_COMPLEX, 0.5, 1); 
						cvPutText(imageDislay2, Integer.toString(i), convexityDefect.start(), font, CvScalar.MAGENTA);

						cvCircle(imageDislay2, convexityDefect.start(), 3, CvScalar.MAGENTA, -1, 8, 0);
						cvCircle(imageDislay2, convexityDefect.depth_point(), 3, CvScalar.WHITE, -1, 8, 0);
						cvCircle(imageDislay2, convexityDefect.end(), 3, CvScalar.CYAN, -1, 8, 0);
*/
						fingersList.add(convexityDefect.start());
						fingersList.add(convexityDefect.end());
					}
				}
    		}

		    defect = defect.h_next();
		}

		for(int i = 0; i < fingersList.size(); i++)
		{
			if(i < fingersList.size() - 1)
			{
				if(getLenght(fingersList.get(i), fingersList.get(i+1)) < 10)
				{
					i++;
				}
			}

			cvCircle(imageDislay2, fingersList.get(i), 5, CvScalar.MAGENTA, -1, 8, 0);

			fingersList2.add(fingersList.get(i));
		}
	}
	
    public void getPositionHand(ArrayList<CvPoint> centerList)
    {
		if(centerList.size() > 0)
		{
			long[] centreLeft = mainPositionLeft.get(0);
			long[] centreRight = mainPositionRight.get(0);

			if(centreLeft[0] != centreRight[0]) // Si on a perdu une main
			{
    			if(centerList.size() == 1)
    			{
    				if(centreLeft[0] > centreRight[0]) // On a perdu la droite
					{
    					addToClotherHand(centerList, centreLeft, mainPositionLeft);
					}
					else // On a perdu la gauche
					{
						addToClotherHand(centerList, centreRight, mainPositionRight);
					}	
       			}
				else if(centerList.size() >= 2) //On r�initialise les positions
				{
					mainPositionLeft.reset();
					mainPositionRight.reset();

					CvPoint centre1 = centerList.get(0);
	    			CvPoint centre2 = centerList.get(1);

	    			if(centre2.x() > centre1.x()) // center1 : left
	    			{
	    				mainPositionLeft.add(timeLastGrab, centre1, getDepth(centre1), 0);
	    				mainPositionRight.add(timeLastGrab, centre2, getDepth(centre2), 0);
	    			}
	    			else
	    			{
	    				mainPositionLeft.add(timeLastGrab, centre2, getDepth(centre2), 0);
	    				mainPositionRight.add(timeLastGrab, centre1, getDepth(centre1), 0);
	    			}
				}
			}
			else
			{
				int choose = 0;
				double[] lengthListToLeft = new double[centerList.size()];
				double[] lengthListToRight = new double[centerList.size()];
	
				for(int i = 0; i < centerList.size(); i++)
				{
					lengthListToLeft[i] = getLenght(centreLeft[1], centreLeft[2], centerList.get(i).x(), centerList.get(i).y());
					lengthListToRight[i] = getLenght(centreRight[1], centreRight[2], centerList.get(i).x(), centerList.get(i).y());
				}

				int[] minLengthListToLeft = getMinList(lengthListToLeft);
				int[] minLengthListToRight = getMinList(lengthListToRight);

				if(minLengthListToLeft[1] < minLengthListToRight[1])
				{
					choose = 1;
					mainPositionLeft.add(timeLastGrab, centerList.get(minLengthListToLeft[0]), getDepth(centerList.get(minLengthListToLeft[0])), 0);
					centerList.remove(minLengthListToLeft[0]);
				}
				else
				{
					choose = 2;
					mainPositionRight.add(timeLastGrab, centerList.get(minLengthListToRight[0]), getDepth(centerList.get(minLengthListToRight[0])), 0);
					centerList.remove(minLengthListToRight[0]);
				}

				if(centerList.size() == 1)
				{
					if(choose == 1)
					{
						mainPositionRight.add(timeLastGrab, centerList.get(0), getDepth(centerList.get(0)), 0);
					}
					else
					{
						mainPositionLeft.add(timeLastGrab, centerList.get(0), getDepth(centerList.get(0)), 0);
					}
				}
				else if(centerList.size() > 1)
				{
					if(choose == 1)
					{
						addToClotherHand(centerList, centreRight, mainPositionRight);
					}
					else
					{
						addToClotherHand(centerList, centreLeft, mainPositionLeft);
					}		
				}
			}
		}
    }

    public void addToClotherHand(ArrayList<CvPoint> centerList, long[] center, MainPosition mainPosition)
    {
    	double[] lengthList = new double[centerList.size()];
		
		for(int i = 0; i < centerList.size(); i++)
		{
			lengthList[i] = getLenght(center[1], center[2], centerList.get(i).x(), centerList.get(i).y());
		}

		int[] minLengthList = getMinList(lengthList);

		mainPosition.add(timeLastGrab, centerList.get(minLengthList[0]), getDepth(centerList.get(minLengthList[0])), 0);
    }

    public int getFPS()
    {
    	for(int i = 3; i > 0; i--)
    	{
    		timeList[i] = timeList[i-1];
    	}

    	timeList[0] = System.currentTimeMillis();

    	return (int)(3000/(float)((timeList[0] - timeList[3])));
    }

    // Rectangle englobant
    public CvPoint getContourCenter(CvSeq contour, CvMemStorage storage)
    {
    	CvBox2D box = cvMinAreaRect2(contour, storage);

    	return new CvPoint((int)box.center().x(), (int)box.center().y());
    }
    
    // Barycentre
    public CvPoint getContourCenter2(CvSeq contour, CvMemStorage storage)
    {
    	CvPoint centre = new CvPoint();

    	for(int i = 0; i < contour.total(); i++)
		{
			CvPoint point = new CvPoint(cvGetSeqElem(contour, i));

			centre.x(centre.x()+point.x());
			centre.y(centre.y()+point.y());
		}

    	centre.x(centre.x()/contour.total());
    	centre.y(centre.y()/contour.total());

    	return centre;
    }
    
    // Moment
    public CvPoint getContourCenter3(CvSeq contour, CvMemStorage storage)
    {
    	CvMoments moments = new CvMoments();
    	
    	cvMoments(contour, moments, 0);

    	return new CvPoint((int)(moments.m10()/moments.m00()), (int)(moments.m01()/moments.m00()));
    }

    public int getDepth(CvPoint point)
    {
    	return OpenCV.getUnsignedByte(imageTraitement.getByteBuffer(), point.x() + imageTraitement.width()*point.y());
    }

    public double getLenght(long x1, long y1, int x2, int y2)
    {
    	return Math.sqrt(((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)));
    }

    public double getLenght(CvPoint point1, CvPoint point2)
    {
    	return getLenght(point1.x(), point1.y(), point2.x(), point2.y());
    }

    public int[] getMinList(double[] list)
    {
    	double min = list[0];
    	int index = 0;

    	for(int i = 1; i < list.length; i++)
    	{
    		if(list[i] < min)
    		{
    			min = list[i];
    			index = i;
    		}
    	}

    	int[] r = {index, (int)min};

    	return r;
    }

	public int getAngle(CvPoint p1, CvPoint p2, CvPoint p3)
	{
		double alpha = Math.atan2(p1.y() - p2.y(), p1.x() - p2.x());
		double beta = Math.atan2(p3.y() - p2.y(), p3.x() - p2.x());
		double angle = (beta - alpha);

		//Correction de l'angle pour le restituer entre 0 et 2PI
		while (angle < 0.0 || angle > 2 * Math.PI)
		{
			if (angle < 0.0) angle += 2 * Math.PI;
			else if (angle > 2 * Math.PI) angle -= 2 * Math.PI;
		}

		return (int)(Math.round(Math.toDegrees(angle)));
	}
}
