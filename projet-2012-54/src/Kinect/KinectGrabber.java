
package Kinect;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_16U;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;

import java.nio.ByteBuffer;

import org.OpenNI.Context;
import org.OpenNI.DepthGenerator;
import org.OpenNI.DepthMap;
import org.OpenNI.GeneralException;
import org.OpenNI.License;
import org.OpenNI.MapOutputMode;
import org.OpenNI.StatusException;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

/**
 * Wrapper entre OpenNI et JavaCV.
 * Il permet de r�cup�rer l'image de profondeur de la kinect.
*/
public class KinectGrabber
{
	private Context context;
	private DepthGenerator depthGenerator;
	private int scaleI = 9;
	private float scaleA;
	private float scaleB;
	private boolean scaleIs = false;
	private IplImage imageDepth;
	private ByteBuffer depthByteBuffer;

	/**
	 * Initialise le grabber.
	 */
	public KinectGrabber()
	{
		try
		{
			context = new Context();
			context.addLicense(new License("PrimeSense", "0KOIk2JeIBYClPWVnMoRKn5cdY4="));
			context.setGlobalMirror(true);

			depthGenerator = DepthGenerator.create(context);
			depthGenerator.setMapOutputMode(new MapOutputMode(640, 480, 30));

			imageDepth = IplImage.create(640, 480, IPL_DEPTH_16U, 1);
			depthByteBuffer = imageDepth.getByteBuffer();
		}
		catch(GeneralException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * D�marre le grabber.
	 */
	public void start()
	{
		try
		{
			context.startGeneratingAll();
		}
		catch(StatusException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * R�cup�re la derni�re image fourni par la kinect.
	 * @return une image
	 */
	public IplImage grab()
	{
		try
		{
			context.waitAnyUpdateAll();

			DepthMap depthM = depthGenerator.getDepthMap();
			depthM.copyToBuffer(depthByteBuffer, 640 * 480 * 2);

			return imageDepth;
		}
		catch(GeneralException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Augmente le contraste de l image 16bit fournie par la kinect et la convertie en 8bit.
	 * @param src image source
	 * @return image trait�e
	 */
	public void scale(IplImage src, IplImage dst)
	{
		ByteBuffer srcBuffer = src.getByteBuffer();
		ByteBuffer dstBuffer = dst.getByteBuffer();

		/*
		 * pour avoir tout le champ de vision int c1 = 65535; int c2 = -8000;
		 */

		int c1 = 65535;
		int c2 = -12000;

		// Calcule de max et min
		if(scaleI > 10)
		{
			int max = 0;
			int min = 65535;

			for(int x = 0; x < 640; x++)
			{
				for(int y = 0; y < 480; y++)
				{
					int srcPixelIndex = 2 * x + 2 * 640 * y;
					int value = (srcBuffer.get(srcPixelIndex + 1) & 0xff) * 256 + (srcBuffer.get(srcPixelIndex) & 0xff);

					if(value < min)
					{
						min = value;
					}
					else if(value > max)
					{
						max = value;
					}
				}
			}

			// Filtre passe bas
			if(scaleIs)
			{
				if(max != min)
				{
					scaleA = (float)((c1 - c2) / (max - min) * 0.3 + scaleA * 0.7);
					scaleB = (float)((c1 - scaleA * max) * 0.3 + scaleB * 0.7);
				}
			}
			else
			{
				if(max != min)
				{
					scaleA = (c1 - c2) / (max - min);
					scaleB = c1 - scaleA * max;
					scaleIs = false;
				}
			}

			scaleI = 0;
		}
		else
		{
			scaleI++;
		}

		// Renormalisation
		// equivalent de cvNormalize(src, dst, -8000, 65535, CV_MINMAX, null);
		// et conversion en 8Bit
		// equivalent de cvConvertScale(src, dst, 1/256.0, 0);
		for(int x = 0; x < 640; x++)
		{
			for(int y = 0; y < 480; y++)
			{
				int srcPixelIndex = 2 * x + 2 * 640 * y;
				float value = (srcBuffer.get(srcPixelIndex + 1) & 0xff) * 256 + (srcBuffer.get(srcPixelIndex) & 0xff);

				value = (float)(scaleA * value + scaleB);
				value = (float)(value / 256.0);

				if(value <= 0)
				{
					value = 255; // White
				}
				else if(value > 255)
				{
					value = 255;
				}

				dstBuffer.put(srcPixelIndex / 2, (byte)(value));
			}
		}
	}

	public void correct(IplImage src, IplImage dst)
	{
		ByteBuffer srcBuffer = src.getByteBuffer();
		ByteBuffer dstBuffer = dst.getByteBuffer();

		for(int x = 0; x < 640; x++)
		{
			for(int y = 0; y < 480; y++)
			{
				int pixelIndex = 2 * x + 2 * 640 * y;
				int value = OpenCV.getUnsignedByte(srcBuffer, pixelIndex+1) * 256 + OpenCV.getUnsignedByte(srcBuffer, pixelIndex);

				if(value == 0)
				{
					dstBuffer.put(pixelIndex, (byte)(255)); // White
					dstBuffer.put(pixelIndex+1, (byte)(255)); // White
				}
			}
		}
	}

	/**
	 * Arr�te le grabber.
	 */
	public void stop()
	{
		try
		{
			context.stopGeneratingAll();
		}
		catch(StatusException e)
		{
		}

		context.release();
	}
}
