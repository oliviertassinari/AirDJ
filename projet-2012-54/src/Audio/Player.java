package Audio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.sound.sampled.*;

import org.tritonus.share.sampled.TAudioFormat;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import Modele.ModelePlay;

public class Player implements Runnable, IPlayer
{
	/**
	 * 
	 */
	private Filler filler;
	
	private ComputeBPM computeBPM;
	
	/**
	 * File played
	*/
	private File file;
	
	/** 
	 * Volume of the current 0.05s played (0-left  1-right)
	 */
	private int[] currentVolume = new int[2];
	
	/** 
	 * Array filled with the volume of every 0.05s bracket (0-left  1-right)
	 */
	private int[][] volumeArray;

	/** 
	 * 
	 */
	private float frameRate;
	
	/** 
	 * 
	 */
	private int frameSize;

	/** 
	 * Thread ruling play/pause settings
	 */
	Thread runner;
	
	/** 
	 * 0-pause  1-play
	 */
	private int status;
	
	/**
	 * 
	 */
	private AudioInputStream audioInputStream;
	
	/**
	 * 
	 */
	private SourceDataLine line;

	/**
	 * 
	 */
	private AudioFormat audioFormat;
	
	/** 
	 * Control the gain of the music played
	 */
	private FloatControl gainControl;
	
	/** 
	 * Number of bytes elapsed since the beginning of the music
	 */
	private int byteFromBeginning;

	private int bass = 0;
	private int mid = 0;
	private double vitesse = 1;

	/**
	 * Filtre RIF, coupe bande : 200Hz -> 20 000Hz : Réduit les Bass
	 */
	private double[] aLowerBass = {
			-0.008787861,
			-0.0099598095,
			-0.011140668,
			-0.01232451,
			-0.01350546,
			-0.014677407,
			-0.015834318,
			-0.016970146,
			-0.018078908,
			-0.019154703,
			-0.020191766,
			-0.02118451,
			-0.022127545,
			-0.023015667,
			-0.023844099,
			-0.024608238,
			-0.025303854,
			-0.02592709,
			-0.026474472,
			-0.026942857,
			-0.027329797,
			-0.027633052,
			-0.027850812,
			-0.027981985,
			0.88091856,
			-0.027981985,
			-0.027850812,
			-0.027633052,
			-0.027329797,
			-0.026942857,
			-0.026474472,
			-0.02592709,
			-0.025303854,
			-0.024608238,
			-0.023844099,
			-0.023015667,
			-0.022127545,
			-0.02118451,
			-0.020191766,
			-0.019154703,
			-0.018078908,
			-0.016970146,
			-0.015834318,
			-0.014677407,
			-0.01350546,
			-0.01232451,
			-0.011140668,
			-0.0099598095,
			-0.008787861
	};

	/**
	 * Filtre RIF, coupe bande : 200Hz -> 2 000Hz : Réduit Mid
	 */
	private double[] aLowerMid = {
			-3.53469083669411E-003,
			-2.04417403552522E-003,
			7.18204584999788E-004,
			4.60624810386546E-003,
			9.35489283788638E-003,
			1.45936768599084E-002,
			1.98698234934287E-002,
			2.46794719123869E-002,
			2.85048530392627E-002,
			3.08546366989605E-002,
			3.13043178066969E-002,
			2.95334005589486E-002,
			2.53562951020205E-002,
			1.87442539225236E-002,
			9.83631643083466E-003,
			-1.06194856652329E-003,
			-1.34921758849029E-002,
			-2.68687081513636E-002,
			-4.05142277562520E-002,
			-5.37030498843225E-002,
			-6.57089710363407E-002,
			-7.58541669662924E-002,
			-8.35555068164655E-002,
			-8.83648091612411E-002,
			9.10000000000000E-001,
			-8.83648091612411E-002,
			-8.35555068164655E-002,
			-7.58541669662924E-002,
			-6.57089710363407E-002,
			-5.37030498843225E-002,
			-4.05142277562520E-002,
			-2.68687081513636E-002,
			-1.34921758849029E-002,
			-1.06194856652329E-003,
			9.83631643083466E-003,
			1.87442539225236E-002,
			2.53562951020205E-002,
			2.95334005589486E-002,
			3.13043178066969E-002,
			3.08546366989605E-002,
			2.85048530392627E-002,
			2.46794719123869E-002,
			1.98698234934287E-002,
			1.45936768599084E-002,
			9.35489283788638E-003,
			4.60624810386546E-003,
			7.18204584999788E-004,
			-2.04417403552522E-003,
			-3.53469083669411E-003
	};

	/**
	 * Filtre RIF, bande augmenté : 20Hz -> 200Hz
	 */
	private double[] aHigherBass = {
			6.44172522792564E-002,
			1.20780368416833E-002,
			1.31207601433078E-002,
			1.40972431875273E-002,
			1.51758532775757E-002,
			1.63908107273220E-002,
			1.78527152799349E-002,
			1.98774379371414E-002,
			1.90375336112458E-002,
			2.07114812986848E-002,
			2.16030666092759E-002,
			2.26262307235240E-002,
			2.35847944354608E-002,
			2.45389999743536E-002,
			2.53864719476830E-002,
			2.56123660426905E-002,
			2.67224232932284E-002,
			2.71847418701315E-002,
			2.78706868829391E-002,
			2.82737493257007E-002,
			2.86915058618749E-002,
			2.89772732113979E-002,
			2.90706062866833E-002,
			2.94402431594945E-002,
			8.29317381079748E-001,
			2.94402431594945E-002,
			2.90706062866833E-002,
			2.89772732113979E-002,
			2.86915058618749E-002,
			2.82737493257007E-002,
			2.78706868829391E-002,
			2.71847418701315E-002,
			2.67224232932284E-002,
			2.56123660426905E-002,
			2.53864719476830E-002,
			2.45389999743536E-002,
			2.35847944354608E-002,
			2.26262307235240E-002,
			2.16030666092759E-002,
			2.07114812986848E-002,
			1.90375336112458E-002,
			1.98774379371414E-002,
			1.78527152799349E-002,
			1.63908107273220E-002,
			1.51758532775757E-002,
			1.40972431875273E-002,
			1.31207601433078E-002,
			1.20780368416833E-002,
			6.44172522792564E-002
	};

	/**
	 * Filtre RIF, bande augmenté : 200Hz -> 2 000Hz
	 */
	private double[] aHigherMid = {
			-2.33289875956047E-001,
			1.83297406985040E-002,
			1.37596718682256E-002,
			7.80913753031319E-003,
			6.58716237420219E-004,
			-6.90825672601756E-003,
			-1.41151416040762E-002,
			-2.07840809558732E-002,
			-2.06970388340627E-002,
			-3.12449796544360E-002,
			-2.95248383636961E-002,
			-2.59483194565680E-002,
			-2.00833261214061E-002,
			-1.21437744558801E-002,
			-2.34881751710141E-003,
			9.68225672324198E-003,
			2.07643714196601E-002,
			3.67270007963361E-002,
			4.89402621378549E-002,
			6.16010189900944E-002,
			7.33692399884099E-002,
			8.34794543269053E-002,
			9.11781236876308E-002,
			9.51793226010627E-002,
			8.98278380751246E-001,
			9.51793226010627E-002,
			9.11781236876308E-002,
			8.34794543269053E-002,
			7.33692399884099E-002,
			6.16010189900944E-002,
			4.89402621378549E-002,
			3.67270007963361E-002,
			2.07643714196601E-002,
			9.68225672324198E-003,
			-2.34881751710141E-003,
			-1.21437744558801E-002,
			-2.00833261214061E-002,
			-2.59483194565680E-002,
			-2.95248383636961E-002,
			-3.12449796544360E-002,
			-2.06970388340627E-002,
			-2.07840809558732E-002,
			-1.41151416040762E-002,
			-6.90825672601756E-003,
			6.58716237420219E-004,
			7.80913753031319E-003,
			1.37596718682256E-002,
			1.83297406985040E-002,
			-2.33289875956047E-001
	};

	/** 
	 * As Thread start he calls this method
	 */
	public void run()
	{
		try
		{	
			byte bytes[] = new byte[50*frameSize];
			byte bytesPrev1[] = null;
			byte bytesPrev2[] = null;

			synchronized(this)
			{
				while(((audioInputStream.read(bytes, 0, 50*frameSize)) != -1))
				{
					while(status != 1)
					{
						if(line.isRunning())
						{
							line.stop();
						}

						try
						{
							wait();
						}
						catch(InterruptedException e){
							e.printStackTrace();
						}
					}

					if(!line.isRunning())
					{
						line.start();
					}

					byteFromBeginning += 50*frameSize;
					int pos = (int) ((byteFromBeginning*20)/(frameSize*frameRate));
					if (pos > 7) pos -= 7;
					currentVolume[0] = volumeArray[0][pos];
					currentVolume[1] = volumeArray[1][pos];

					byte[][] res1 = getFiltredValue(bytes, bytesPrev1, bass, aLowerBass, aHigherBass);

					bytes = res1[0];
					bytesPrev1 = res1[1];
					
					byte[][] res2 = getFiltredValue(bytes, bytesPrev2, mid, aLowerMid, aHigherMid);

					bytes = res2[0];
					bytesPrev2 = res2[1];

					// Vitesse de lecture
					byte bytes2[] = new byte[(int)Math.round(50/vitesse)*frameSize];

					for(int i = 0; i < bytes2.length/frameSize; i++)
					{
						int index = (int)Math.floor(49*i/(bytes2.length/frameSize-1));

						double valueLeft = byteToShort(bytes[4 * index + 0], bytes[4 * index + 1]);
						double valueRight = byteToShort(bytes[4 * index + 2], bytes[4 * index + 3]);

						byte[] resLeft = shortToByte((short)valueLeft);
						byte[] resRight = shortToByte((short)valueRight);

						bytes2[4*i] = resLeft[0];
						bytes2[4*i+1] = resLeft[1];
						bytes2[4*i+2] = resRight[0];
						bytes2[4*i+3] = resRight[1];
					}

					line.write(bytes2, 0, bytes2.length);
				}

				line.drain();
				line.stop();
				line.close();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Implémentation d'un filtre RIF
	 * @param bytes valeurs non filtrées
	 * @param bytesPrev valeurs non filtrées précédentes
	 * @param value coefficient du filtre
	 * @param aLower coefficients du filtre coupe bande
	 * @param aHigher coefficients du filtre augmentation bande
	 * @return valeurs filtrées
	 */
	public byte[][] getFiltredValue(byte[] bytes, byte[] bytesPrev, int value, double[] aLower, double[] aHigher)
	{
		if(bytesPrev != null)
		{
			byte[] bytesBoth = new byte[bytes.length*2];
			System.arraycopy(bytesPrev, 0, bytesBoth, 0, bytes.length);
			System.arraycopy(bytes, 0, bytesBoth, bytes.length, bytes.length);

			System.arraycopy(bytes, 0, bytesPrev, 0, bytes.length);

			for(int i = 0; i < bytes.length/frameSize; i++)
			{
				if(value < 0)
				{
					double valueLeft = ((double)(50+value)/50)*byteToShort(bytesBoth[bytes.length + 4 * i + 0], bytesBoth[bytes.length + 4 * i + 1]);
					double valueRight = ((double)(50+value)/50)*byteToShort(bytesBoth[bytes.length + 4 * i + 2], bytesBoth[bytes.length + 4 * i + 3]);

					for(int j = 0; j < 49; j++)
					{
						valueLeft += ((-(double)value)/50)*aLower[j]*byteToShort(bytesBoth[bytes.length + 4 * (i-j) + 0], bytesBoth[bytes.length + 4 * (i-j) + 1]);
						valueRight += ((-(double)value)/50)*aLower[j]*byteToShort(bytesBoth[bytes.length + 4 * (i-j) + 2], bytesBoth[bytes.length + 4 * (i-j) + 3]);
					}

					byte[] resLeft = shortToByte((short)valueLeft);
					byte[] resRight = shortToByte((short)valueRight);

					bytes[4*i] = resLeft[0];
					bytes[4*i+1] = resLeft[1];
					bytes[4*i+2] = resRight[0];
					bytes[4*i+3] = resRight[1];
				}
				else if(value > 0)
				{
					double valueLeft = ((double)(value-50)/50)*byteToShort(bytesBoth[bytes.length + 4 * i + 0], bytesBoth[bytes.length + 4 * i + 1]);
					double valueRight = ((double)(value-50)/50)*byteToShort(bytesBoth[bytes.length + 4 * i + 2], bytesBoth[bytes.length + 4 * i + 3]);

					for(int j = 0; j < 49; j++)
					{
						valueLeft += (((double)value)/50)*aHigher[j]*byteToShort(bytesBoth[bytes.length + 4 * (i-j) + 0], bytesBoth[bytes.length + 4 * (i-j) + 1]);
						valueRight += (((double)value)/50)*aHigher[j]*byteToShort(bytesBoth[bytes.length + 4 * (i-j) + 2], bytesBoth[bytes.length + 4 * (i-j) + 3]);
					}

					byte[] resLeft = shortToByte((short)valueLeft);
					byte[] resRight = shortToByte((short)valueRight);

					bytes[4*i] = resLeft[0];
					bytes[4*i+1] = resLeft[1];
					bytes[4*i+2] = resRight[0];
					bytes[4*i+3] = resRight[1];
				}
			}
		}
		else
		{
			bytesPrev = new byte[bytes.length];
			System.arraycopy(bytes, 0, bytesPrev, 0, bytes.length);
		}

		byte[][] res = {bytes, bytesPrev};

		return res;
	}

	public short byteToShort(byte byte1, byte byte2)
	{
		if(audioFormat.isBigEndian())
		{
			return (short)((byte2 & 0xFF) | (byte1 << 8));
		}
		else
		{
			return (short)((byte1 & 0xFF) | (byte2 << 8));
		}
	}

	public byte[] shortToByte(short value)
	{
		byte[] bytes = new byte[2];

		if(audioFormat.isBigEndian())
		{
			bytes[1] = (byte)(value & 0xff);
			bytes[0] = (byte)((value >> 8) & 0xff);
		}
		else
		{
			bytes[0] = (byte)(value & 0xff);
			bytes[1] = (byte)((value >> 8) & 0xff);
		}

		return bytes;
	}

	public void setMid(int value)
	{
		mid = value;
	}

	public void setBass(int value)
	{
		bass = value;
	}

	/** 
	 * CONSTRUCTOR
	 * @param fileName Path name of the music who will be play
	 */
	public Player(String fileName)
	{
		/*try
		{
			/*URL url = new URL("http://ec-media.soundcloud.com/K6hjhqw6FEw6.128.mp3?ff61182e3c2ecefa438cd02102d0e385713f0c1faf3b0339595665fe0e00e810cdd694f30d569d6876f50c8cf5a9c2760f3d3ffd4b9dfcd54b4730efa6a58473027fcbfe28&AWSAccessKeyId=AKIAJ4IAZE5EOI7PA7VQ&Expires=1364153418&Signature=zrsobEfjmAY9Z42dGNBDbWbfLpA%3D");
            InputStream inputStream = url.openStream();
            AdvancedPlayer pl = new AdvancedPlayer(inputStream);
            pl.play();
            			
			file = new File("E:/Musique/2012/Daughter - Medicine (Sound Remedy Remix).mp3");
			FileInputStream fileInputStream = new FileInputStream(file);
			AdvancedPlayer pl = new AdvancedPlayer(fileInputStream);
            pl.play();
		}
		catch(IOException e1)
		{
			e1.printStackTrace();
		}*/
		
		/*AudioInputStream din = null;
		try {
			File file = new File("E:/Musique/2012/Daughter - Medicine (Sound Remedy Remix).mp3");
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
					false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
			SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
			if(line != null) {
				line.open(decodedFormat);
				byte[] data = new byte[4096];
				// Start
				line.start();
				
				int nBytesRead;
				while ((nBytesRead = din.read(data, 0, data.length)) != -1) {	
					line.write(data, 0, nBytesRead);
				}
				// Stop
				line.drain();
				line.stop();
				line.close();
				din.close();
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(din != null) {
				try { din.close(); } catch(IOException e) { }
			}
		}
		*/

		file = new File(fileName);
		byteFromBeginning = 0;
		runner = new Thread(this, "player");
		try
		{
			//Initializing music player
			audioInputStream = AudioSystem.getAudioInputStream(file);
			audioFormat = audioInputStream.getFormat();

			audioFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					audioFormat.getSampleRate(), 16, audioFormat.getChannels(),
					audioFormat.getChannels() * 2, audioFormat.getSampleRate(),
					false);
			audioInputStream = AudioSystem.getAudioInputStream(audioFormat, audioInputStream);

			DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(audioFormat);
			frameSize = audioFormat.getFrameSize();
	 		frameRate = audioFormat.getFrameRate();
	 		volumeArray = new int[2][((int) (file.length()/(frameSize*frameRate)*20))+1];
	 		
	 		//Filling volumeArray
			filler = new Filler(this);

			//Initializing gainControl
			gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
			setVolume(50);
			runner.start();
		}
		catch(UnsupportedAudioFileException e)
		{
			e.printStackTrace();	
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(LineUnavailableException e)
		{
			e.printStackTrace();
		}
	}

	/** 
	 * Play the music if it is currently paused
	 */
	public synchronized void setPlay()
	{
		status = 1;
		notify();
	}

	/** 
	 * Pause the music if it is currently played
	 */
	public void setPause()
	{
		status = 0;
	}

	/**
	 * 
	 * @param volume (0 to 100)
	 */
	public void setVolume(float volume) 
	{
		float max = (float)Math.pow(10.0, gainControl.getMaximum()/20);
		float temp1 = max*volume/100;
		float temp2 = (float)(20*Math.log10(temp1));

		gainControl.setValue(temp2);
	}

	/** 
	 * @param vitesse
	 */
	public void setVitesse(double vitesse)
	{
		if(vitesse > 2)
		{
			vitesse = 2;
		}
		else if(vitesse <= 0)
		{
			vitesse = 0.1;
		}

		this.vitesse = vitesse;
	}

	/** 
	 * 
	 * @return current position (s)
	 */
	public float getPosition()
	{
		return byteFromBeginning/(frameRate * frameSize);
	}

	/** 
	 * Set the current position to
	 * @param position (s)
	 */
	public void setPosition(float position)
	{
		try
		{
			boolean isPlayed = false;

			if(status == 1)
			{
				line.flush();
				status = 0;
				isPlayed = true;
			}
			else
			{
				line.start();
				line.flush();
				line.stop();
			}

			long n = (long)(position * frameRate * frameSize);
			byteFromBeginning = (int)n;

			AudioInputStream audioInputStream2 = audioInputStream;
			audioInputStream = AudioSystem.getAudioInputStream(file);
			audioInputStream.skip(n);

			audioInputStream2.close();

			if(isPlayed)
			{
				setPlay();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		}	
	}

	/**
	 * 
	 * @return volume of the current 0.05s played (0-left  1-right)
	 */
	public int[] getCurrentVolume()
	{
		int[] currentVolume = {0, 0};

		if(status != 0)
		{
			currentVolume[0] = (int)this.currentVolume[0];
			currentVolume[1] = (int)this.currentVolume[1];
		}

		return currentVolume;
	}

	/**
	 * 
	 * @return total length of the file
	 */
	public float getLength()
	{
	    return file.length()/(frameSize*frameRate);
	}

	/**
	 * Reset song
	 */
	public boolean reset()
	{
		if(!runner.isAlive())
		{
			status = 0;
			byteFromBeginning = 0;

			try
			{
				audioInputStream = AudioSystem.getAudioInputStream(file);
				line.open(audioFormat);
				runner = new Thread(this, "player");
				runner.start();
				return true;
			}
			catch(UnsupportedAudioFileException e)
			{
				e.printStackTrace();
				return false;
			}
			catch (LineUnavailableException e)
			{
				e.printStackTrace();
				return false;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	/**
	 * 
	 * @return file played
	 */
	public File getFile() 
	{
		return file;
	}

	/**
	 * 
	 * @return frame rate
	 */
	public float getFrameRate() 
	{
		return frameRate;
	}

	/**
	 * 
	 * @return frame size
	 */
	public int getFrameSize()
	{
		return frameSize;
	}
	
	/**
	 * @param volume
	 * @param j (0-left  1-right)
	 * @param i array position
	 */
	public void setVolumeArray(int volume, int i, int j)
	{
		volumeArray[j][i] = volume;
	}

	/**
	 * 
	 * @return array position
	 */
	public int[][] getVolumeArray()
	{
		return volumeArray;
	}

	/**
	 * @param j (0-left  1-right)
	 * @param i array position
	 */
	public int getVolumeArray(int i, int j)
	{
		return volumeArray[j][i];
	}

	public void computeBPM(ModelePlay modelePlay)
	{
		if(computeBPM == null)
		{
			computeBPM = new ComputeBPM(this, modelePlay, byteFromBeginning);
		}
	}
	
	public void computeBPMEnd()
	{
		computeBPM = null;
	}

	public AudioFormat getAudioFormat()
	{
		return audioFormat;
	}

	public String getTitle()
	{
		String title = "";

		try
		{
			AudioFileFormat baseFileFormat = AudioSystem.getAudioFileFormat(file);

			if(baseFileFormat instanceof TAudioFileFormat)
			{
			    Map properties = ((TAudioFileFormat)baseFileFormat).properties();

			    title = (String) properties.get("title");
			}
		}
		catch(UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		if(title == "")
		{
			title = file.getName();
			title = title.substring(0, title.lastIndexOf("."));
		}

		return title;
	}

	public String getArtist()
	{
		String artist = "";

		try
		{
			AudioFileFormat baseFileFormat = AudioSystem.getAudioFileFormat(file);

			if(baseFileFormat instanceof TAudioFileFormat)
			{
			    Map properties = ((TAudioFileFormat)baseFileFormat).properties();

			    artist = (String) properties.get("author");
			}
		}
		catch(UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		if(artist == "")
		{
			artist = file.getName();
			artist = artist.substring(0, artist.lastIndexOf("."));
		}

		return artist;
	}

	public void stop()
	{
		setPause();

		filler.stop();

		if(computeBPM != null)
		{
			computeBPM.stop();
		}

		runner.stop();
	}
}