
package Kinect;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.awt.GridLayout;
import java.nio.ByteBuffer;
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
	private MainPosition mainPositionLeft;
	private MainPosition mainPositionRight;
	private IplImage imageTraitement;
	private IplImage imageGrab;
	private IplImage imageGrab2;
	private IplImage imageDislay2;
	private IplImage imageThreshold;
	private long timeLastGrab;
	private long[] timeList = new long[4];
	private reconnaissanceMvt reconnaissanceMvtLeft;
	private reconnaissanceMvt reconnaissanceMvtRight;
	private Vue vue;

	public Kinect(KinectSource kinectSource, Vue vue)
	{
		this.vue = vue;

		mainPositionLeft = new MainPosition();
		mainPositionRight = new MainPosition();

		reconnaissanceMvtLeft = new reconnaissanceMvt(this, mainPositionLeft, "left", kinectSource);
		reconnaissanceMvtRight = new reconnaissanceMvt(this, mainPositionRight, "right", kinectSource);

		runner = new Thread(this, "kinect");
		runner.start();
	}

	public void run()
	{
		try
		{
			/*
			timeLastGrab = System.currentTimeMillis();
			for(int i = 0; i < 200; i++)
			{
				
			}
			System.out.println((System.currentTimeMillis()-timeLastGrab)/200f+"ms");
			*/
			
			
			// OpenCVFrameGrabber grabber = new OpenCVFrameGrabber("video/depth_pact54_test1.mkv");
			// OpenCVFrameGrabber grabber = new OpenCVFrameGrabber("video/depth_pact54_test2.mpg");
			// OpenCVFrameGrabber grabber = new OpenCVFrameGrabber("video/depth_pact42_test1.mkv");
			// OpenCVFrameGrabber grabber = new OpenCVFrameGrabber("video/depth_pact54_test2.mkv");
			// OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
			KinectGrabber grabber = new KinectGrabber();

			grabber.start();

			imageGrab = grabber.grab();
			int width = imageGrab.width();
			int height = imageGrab.height();
			CvPoint minPoint = new CvPoint();
			CvPoint maxPoint = new CvPoint();
			double[] minVal = new double[1];
			double[] maxVal = new double[1];

			imageGrab2 = IplImage.create(width, height, IPL_DEPTH_8U, 1);
			imageDislay2 = IplImage.create(width, height, IPL_DEPTH_8U, 3);
			imageThreshold = IplImage.create(width, height, IPL_DEPTH_8U, 1);

			// creation window used to display the video, the object in JavaCv can use the material acceleration
			JFrame Fenetre = new JFrame();
			Fenetre.setLayout(new GridLayout(1, 2));
			Fenetre.setTitle("module JavaCV");
			Fenetre.setSize(width * 2 + 40, height + 60);
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

			CvSeq points;
			CvSeq contour;

			while((imageGrab = grabber.grab()) != null)
			{
				timeLastGrab = System.currentTimeMillis();

				grabber.scale(imageGrab, imageGrab2); // 1.5ms

				cvCvtColor(imageGrab2, imageDislay2, CV_GRAY2RGB); // 0.35ms
				//OpenCV.cvCvtColor(imageGrab2, imageDislay2, CV_GRAY2RGB); // 1.65ms

				grabber.correct(imageGrab, imageGrab); // 0.63ms

				imageTraitement = imageGrab.clone();

				cvMinMaxLoc(imageGrab, minVal, maxVal, minPoint, maxPoint, null); // 0.31ms
				//OpenCV.cvMinMaxLoc(imageGrab, minVal, maxVal, minPoint, maxPoint, null); // 0.94ms

				cvCircle(imageDislay2, minPoint, 3, CvScalar.YELLOW, -1, 8, 0);

				cvSmooth(imageTraitement, imageTraitement, CV_GAUSSIAN, 7, 7, 0, 0); // 2.4ms

				int nbrIteration = 0;
				int firstFound = 0;
				boolean isFound = false;

				while(!isFound && nbrIteration < 6) // 5 iterations max
				{
					nbrIteration++;

					int nbrFound = 0;

					//cvThreshold(imageTraitement, imageThreshold, minVal[0] + 5 * nbrIteration, 255, CV_THRESH_BINARY_INV);
					OpenCV.cvThreshold(imageTraitement, imageThreshold, minVal[0] + 100*nbrIteration, 255, CV_THRESH_BINARY_INV); // 1.0ms

					contour = new CvSeq();
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

				if(!isFound)
				{
					//cvThreshold(imageTraitement, imageThreshold, minVal[0] + 5 * firstFound, 255, CV_THRESH_BINARY_INV);
					OpenCV.cvThreshold(imageTraitement, imageThreshold, minVal[0] + 100*firstFound, 255, CV_THRESH_BINARY_INV);
				}

				ArrayList<CvPoint> centerList = new ArrayList<CvPoint>();
				ArrayList<int[]> fingersList = new ArrayList<int[]>();
				contour = new CvSeq();
				cvFindContours(imageThreshold.clone(), storage, contour, Loader.sizeof(CvContour.class), CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);

				while(contour != null && !contour.isNull())
				{
					if(contour.elem_size() > 0)
					{
						double aire = cvContourArea(contour, CV_WHOLE_SEQ, 0);

						if(aire > 1000 && aire < 15000)
						{
							points = cvApproxPoly(contour, Loader.sizeof(CvContour.class), storage, CV_POLY_APPROX_DP, cvContourPerimeter(contour) * 0.005, 0);
							cvDrawContours(imageDislay2, points, CvScalar.GREEN, CvScalar.GREEN, -1, 1, CV_AA);

							CvPoint centre = getContourCenter3(points, storage);
							cvCircle(imageDislay2, centre, 3, CvScalar.RED, -1, 8, 0);

							fingersList.add(getFingers(points, storage, imageDislay2));

							centerList.add(centre);
						}
					}
					contour = contour.h_next();
				}

				getPositionHand(centerList, fingersList);
				reconnaissanceMvtLeft.compute(timeLastGrab);
				reconnaissanceMvtRight.compute(timeLastGrab);

				// Affichage position filtrée
				cvCircle(imageDislay2, new CvPoint((int)mainPositionLeft.getFiltre(0)[0], (int)mainPositionLeft.getFiltre(0)[1]), 3, CvScalar.BLACK, -1, 8, 0);
				cvCircle(imageDislay2, new CvPoint((int)mainPositionRight.getFiltre(0)[0], (int)mainPositionRight.getFiltre(0)[1]), 3, CvScalar.BLACK, -1, 8, 0);

				CvFont font = new CvFont(CV_FONT_HERSHEY_COMPLEX, 0.6, 1);

				if(mainPositionLeft.get(0)[0] == timeLastGrab)
				{
					cvPutText(imageDislay2, "Gauche", cvPoint((int)mainPositionLeft.getFiltre(0)[0] - 20, (int)mainPositionLeft.getFiltre(0)[1] - 10), font, CvScalar.BLUE);
				}
				if(mainPositionRight.get(0)[0] == timeLastGrab)
				{
					cvPutText(imageDislay2, "Droite", cvPoint((int)mainPositionRight.getFiltre(0)[0] - 20, (int)mainPositionRight.getFiltre(0)[1] - 10), font, CvScalar.RED);
				}

				IplImage imageLeft = IplImage.create(310, 200, IPL_DEPTH_8U, 3);
				cvGetRectSubPix(imageDislay2, imageLeft, getCvPointHand(mainPositionLeft));

				IplImage imageRight = IplImage.create(310, 200, IPL_DEPTH_8U, 3);
				cvGetRectSubPix(imageDislay2, imageRight, getCvPointHand(mainPositionRight));

				CvFont fontFPS = new CvFont(CV_FONT_HERSHEY_COMPLEX, 1, 2);
				cvPutText(imageDislay2, "FPS : " + Integer.toString(getFPS()), cvPoint(10, 465), fontFPS, CvScalar.RED);

				IplImage resizeDisplay = IplImage.create(width / 2, height / 2, IPL_DEPTH_8U, 3);
				cvResize(imageDislay2, resizeDisplay);

				vue.getVueKinect().setKinectImage(resizeDisplay.getBufferedImage(), imageLeft.getBufferedImage(), imageRight.getBufferedImage());

				fenetreFrame1.showImage(imageThreshold);
				fenetreFrame2.showImage(imageDislay2);

				cvClearMemStorage(storage);

				long timeEnd = 33 - (int)(System.currentTimeMillis() - timeLastGrab);

				if(timeEnd > 0)
				{
					Thread.sleep(timeEnd);
				}

				// System.out.println(-timeEnd+33+" ms");
			}

			grabber.stop();
			fenetreFrame1.dispose();
			fenetreFrame2.dispose();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		catch(java.lang.Exception e)
		{
			e.printStackTrace();
		}
	}

	public int[] getFingers(CvSeq contour, CvMemStorage storage, IplImage imageDislay2)
	{
		int[] fingers = {0, 639, 479};

		//CvSeq convex = cvConvexHull2(contour, storage, CV_COUNTER_CLOCKWISE, 1);
		//cvDrawContours(imageDislay2, convex, CvScalar.RED, CvScalar.RED, -1, 1, CV_AA);

		CvSeq hull = cvConvexHull2(contour, storage, CV_COUNTER_CLOCKWISE, 0);
		CvSeq defect = cvConvexityDefects(contour, hull, storage);

		ArrayList<CvPoint> fingersList = new ArrayList<CvPoint>();
		ArrayList<CvPoint> fingersList2 = new ArrayList<CvPoint>();

		int nbr = 0;

		while(defect != null)
		{
			for(int i = 0; i < defect.total(); i++)
			{
				CvConvexityDefect convexityDefect = new CvConvexityDefect(cvGetSeqElem(defect, i));

				if(convexityDefect.depth() > 10)
				{
					if(getAngle(convexityDefect.end(), convexityDefect.depth_point(), convexityDefect.start()) < 100)
					{
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
				if(getLenght(fingersList.get(i), fingersList.get(i + 1)) < 10)
				{
					i++;
				}
			}

			nbr++;

			cvCircle(imageDislay2, fingersList.get(i), 5, CvScalar.MAGENTA, -1, 8, 0);

			fingersList2.add(fingersList.get(i));
		}

		if(nbr == 0)
		{
/*			contour = cvApproxPoly(contour, Loader.sizeof(CvContour.class), storage, CV_POLY_APPROX_DP, cvContourPerimeter(contour) * 0.02, 0);
			cvDrawContours(imageDislay2, contour, CvScalar.RED, CvScalar.RED, -1, 1, CV_AA);

			for(int i = 0; i < contour.total(); i++)
			{
				CvPoint point = new CvPoint(cvGetSeqElem(contour, i));
				CvPoint prev;
				CvPoint next;

				if(i == contour.total() - 1)
				{
					next = new CvPoint(cvGetSeqElem(contour, 0));
				}
				else
				{
					next = new CvPoint(cvGetSeqElem(contour, i+1));
				}

				if(i == 0)
				{
					prev = new CvPoint(cvGetSeqElem(contour, contour.total() - 1));
				}
				else
				{
					prev = new CvPoint(cvGetSeqElem(contour, i-1));
				}

				if(getAngle(prev, point, next) < 40)
				{
					cvCircle(imageDislay2, point, 5, CvScalar.CYAN, -1, 8, 0);
				}
			}

			hull = cvConvexHull2(contour, storage, CV_COUNTER_CLOCKWISE, 0);*/
			defect = cvConvexityDefects(contour, hull, storage);

			while(defect != null)
			{
				for(int i = 0; i < defect.total(); i++)
				{
					CvConvexityDefect convexityDefect = new CvConvexityDefect(cvGetSeqElem(defect, i));

					if(convexityDefect.depth() > 10)
					{
						if(getAngle(convexityDefect.end(), convexityDefect.depth_point(), convexityDefect.start()) < 150)
						{
							/*CvFont font = new CvFont(CV_FONT_HERSHEY_COMPLEX, 0.5, 1);
							cvPutText(imageDislay2, Integer.toString(i), convexityDefect.start(), font, CvScalar.MAGENTA);
							cvCircle(imageDislay2, convexityDefect.start(), 3, CvScalar.MAGENTA, -1, 8, 0);
							cvCircle(imageDislay2, convexityDefect.depth_point(), 3, CvScalar.WHITE, -1, 8, 0);
							cvCircle(imageDislay2, convexityDefect.end(), 3, CvScalar.CYAN, -1, 8, 0);
*/
							if(convexityDefect.start().y() < fingers[2])
							{
								fingers[1] = convexityDefect.start().x();
								fingers[2] = convexityDefect.start().y();
							}
	
							if(convexityDefect.end().y() < fingers[2])
							{
								fingers[1] = convexityDefect.end().x();
								fingers[2] = convexityDefect.end().y();
							}
						}
					}
				}

				defect = defect.h_next();
			}

			if(fingers[1] != 639 && fingers[2] != 479)
			{
				fingers[0] = 1;
				cvCircle(imageDislay2, new CvPoint(fingers[1], fingers[2]), 5, CvScalar.CYAN, -1, 8, 0);
			}
		}
		else
		{
			fingers[0] = nbr;
		}

		return fingers;
	}

	public void getPositionHand(ArrayList<CvPoint> centerList, ArrayList<int[]> fingersList)
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
						addToClotherHand(centerList, fingersList, centreLeft, mainPositionLeft);
					}
					else // On a perdu la gauche
					{
						addToClotherHand(centerList, fingersList, centreRight, mainPositionRight);
					}
				}
				else if(centerList.size() >= 2) // On réinitialise les positions
				{
					mainPositionLeft.reset();
					mainPositionRight.reset();

					CvPoint centre1 = centerList.get(0);
					CvPoint centre2 = centerList.get(1);

					if(centre2.x() > centre1.x()) // center1 : left
					{
						mainPositionLeft.add(timeLastGrab, centre1, getValue(imageGrab, centre1), fingersList.get(0), getValue(imageGrab, fingersList.get(0)));
						mainPositionRight.add(timeLastGrab, centre2, getValue(imageGrab, centre2), fingersList.get(1), getValue(imageGrab, fingersList.get(1)));
					}
					else
					{
						mainPositionLeft.add(timeLastGrab, centre2, getValue(imageGrab, centre2), fingersList.get(1), getValue(imageGrab, fingersList.get(1)));
						mainPositionRight.add(timeLastGrab, centre1, getValue(imageGrab, centre1), fingersList.get(0), getValue(imageGrab, fingersList.get(0)));
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
					int i = minLengthListToLeft[0];
					mainPositionLeft.add(timeLastGrab, centerList.get(i), getValue(imageGrab, centerList.get(i)), fingersList.get(i), getValue(imageGrab, fingersList.get(i)));
					centerList.remove(i);
					fingersList.remove(i);
				}
				else
				{
					choose = 2;
					int i = minLengthListToRight[0];
					mainPositionRight.add(timeLastGrab, centerList.get(i), getValue(imageGrab, centerList.get(i)), fingersList.get(i), getValue(imageGrab, fingersList.get(i)));
					centerList.remove(i);
					fingersList.remove(i);
				}

				if(centerList.size() == 1)
				{
					if(choose == 1)
					{
						mainPositionRight.add(timeLastGrab, centerList.get(0), getValue(imageGrab, centerList.get(0)), fingersList.get(0), getValue(imageGrab, fingersList.get(0)));
					}
					else
					{
						mainPositionLeft.add(timeLastGrab, centerList.get(0), getValue(imageGrab, centerList.get(0)), fingersList.get(0), getValue(imageGrab, fingersList.get(0)));
					}
				}
				else if(centerList.size() > 1)
				{
					if(choose == 1)
					{
						addToClotherHand(centerList, fingersList, centreRight, mainPositionRight);
					}
					else
					{
						addToClotherHand(centerList, fingersList, centreLeft, mainPositionLeft);
					}
				}
			}
		}
	}

	public void addToClotherHand(ArrayList<CvPoint> centerList, ArrayList<int[]> fingersList, long[] center, MainPosition mainPosition)
	{
		double[] lengthList = new double[centerList.size()];

		for(int i = 0; i < centerList.size(); i++)
		{
			lengthList[i] = getLenght(center[1], center[2], centerList.get(i).x(), centerList.get(i).y());
		}

		int[] minLengthList = getMinList(lengthList);
		int i = minLengthList[0];

		mainPosition.add(timeLastGrab, centerList.get(i), getValue(imageGrab, centerList.get(i)), fingersList.get(i), getValue(imageGrab, fingersList.get(i)));
	}

	public int getFPS()
	{
		for(int i = 3; i > 0; i--)
		{
			timeList[i] = timeList[i - 1];
		}

		timeList[0] = System.currentTimeMillis();

		return (int)(3000 / (float)((timeList[0] - timeList[3])));
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

			centre.x(centre.x() + point.x());
			centre.y(centre.y() + point.y());
		}

		centre.x(centre.x() / contour.total());
		centre.y(centre.y() / contour.total());

		return centre;
	}

	// Moment
	public CvPoint getContourCenter3(CvSeq contour, CvMemStorage storage)
	{
		CvMoments moments = new CvMoments();

		cvMoments(contour, moments, 0);

		return new CvPoint((int)(moments.m10() / moments.m00()), (int)(moments.m01() / moments.m00()));
	}

	public int getValue(IplImage src, CvPoint point)
	{
		ByteBuffer srcBuffer = src.getByteBuffer();
		int pixelIndex = 2 * point.x() + 2 * 640 * point.y();
		int value = OpenCV.getUnsignedByte(srcBuffer, pixelIndex+1) * 256 + OpenCV.getUnsignedByte(srcBuffer, pixelIndex);

		return value;
	}

	public int getValue(IplImage src, int[] fingersList)
	{
		ByteBuffer srcBuffer = src.getByteBuffer();
		int pixelIndex = 2 * fingersList[1] + 2 * 640 * fingersList[2];
		int value = OpenCV.getUnsignedByte(srcBuffer, pixelIndex+1) * 256 + OpenCV.getUnsignedByte(srcBuffer, pixelIndex);

		return value;
	}

	public double getLenght(long x1, long y1, int x2, int y2)
	{
		return Math.sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
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

		// Correction de l'angle pour le restituer entre 0 et 2PI
		while(angle < 0.0 || angle > 2 * Math.PI)
		{
			if(angle < 0.0)
				angle += 2 * Math.PI;
			else if(angle > 2 * Math.PI)
				angle -= 2 * Math.PI;
		}

		return (int)(Math.round(Math.toDegrees(angle)));
	}

	public CvPoint2D32f getCvPointHand(MainPosition mainPosition)
	{
		int x = (int)mainPosition.getFiltre(0)[0];
		int y = (int)mainPosition.getFiltre(0)[1];

		if(x < 155)
		{
			x = 155;
		}
		else if(x > 485)
		{
			x = 485;
		}

		if(y < 100)
		{
			y = 100;
		}
		else if(y > 380)
		{
			y = 380;
		}

		return new CvPoint2D32f(x, y);
	}
}
