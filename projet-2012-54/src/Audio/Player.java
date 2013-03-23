package Audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

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
	
	/** 
	 * As Thread start he calls this method
	 */
	public void run()
	{
		try
		{	
			byte bytesPrev[] = null;
			byte bytes[] = new byte[5*frameSize];

			synchronized(this)
			{
				while(((audioInputStream.read(bytes, 0, 5*frameSize)) != -1))
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

					byteFromBeginning += 5*frameSize;
					int pos = (int) ((byteFromBeginning*20)/(frameSize*frameRate));
					if (pos > 7) pos -= 7;
					currentVolume[0] = volumeArray[0][pos];
					currentVolume[1] = volumeArray[1][pos];

					if(bytesPrev != null)
					{
						byte[] bytesBoth = new byte[bytes.length + bytesPrev.length];
						System.arraycopy(bytesPrev, 0, bytesBoth, 0, bytes.length);
						System.arraycopy(bytes, 0, bytesBoth, bytes.length, bytesPrev.length);

						bytesPrev = bytes;

						//double[] a = { 0.12944685, 0.23299822, 0.27510983, 0.23299822, 0.12944685 };
						double[] a = { 1, 0, 0, 0, 0 };

						for(int i = 0; i < 5; i++)
						{
							double valueLeft = 0;
							double valueRight = 0;

							for(int j = 0; j < 5; j++)
							{
								valueLeft += a[j]*byteToShort(bytesBoth[bytes.length + 4 * (i-j) + 0], bytesBoth[bytes.length + 4 * (i-j) + 1]);
								valueRight += a[j]*byteToShort(bytesBoth[bytes.length + 4 * (i-j) + 2], bytesBoth[bytes.length + 4 * (i-j) + 1]);
							}

							byte[] resLeft = shortToByte((short)valueLeft);
							byte[] resRight = shortToByte((short)valueRight);

							bytes[4*i] = resLeft[0];
							bytes[4*i+1] = resLeft[1];
							bytes[4*i+2] = resRight[0];
							bytes[4*i+3] = resRight[1];
						}
					}					

					line.write(bytes, 0, bytes.length);
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

	public short byteToShort(byte byte1, byte byte2)
	{
		if(audioFormat.isBigEndian())
		{
			return (short)((byte1 & 0xFF) | (byte2 << 8));
		}
		else
		{
			return (short)((byte2 & 0xFF) | (byte1 << 8));
		}
	}

	public byte[] shortToByte(short value)
	{
		byte[] bytes = new byte[2];

		if(audioFormat.isBigEndian())
		{
			bytes[0] = (byte)(value & 0xff);
			bytes[1] = (byte)((value >> 8) & 0xff);
		}
		else
		{
			bytes[1] = (byte)(value & 0xff);
			bytes[0] = (byte)((value >> 8) & 0xff);
		}

		return bytes;
	}

	/** 
	 * CONSTRUCTOR
	 * @param fileName Path name of the music who will be play
	 */
	public Player(String fileName)
	{
		status = 0;
		file = new File(fileName);
		byteFromBeginning = 0;
		runner = new Thread(this, "player");
		try
		{
			//Initializing music player
			audioInputStream = AudioSystem.getAudioInputStream(file);
			audioFormat = audioInputStream.getFormat();
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
	 * USELESS
	 * @deprecated
	 * @param vitesse
	 */
	public void setVitesse(int vitesse)
	{
		
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