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
	
	private int bass = 0;
	private int mid = 0;
	
	/** 
	 * As Thread start he calls this method
	 */
	public void run()
	{
		//Filtre RIF, bande passante 200Hz -> 20 000Hz -> reduit les Bass
		double[] aLowerBass = new double[49];
		aLowerBass[0] =	-0.008787861;
		aLowerBass[1] =	-0.0099598095;
		aLowerBass[2] =	-0.011140668;
		aLowerBass[3] =	-0.01232451;
		aLowerBass[4] =	-0.01350546;
		aLowerBass[5] =	-0.014677407;
		aLowerBass[6] =	-0.015834318;
		aLowerBass[7] =	-0.016970146;
		aLowerBass[8] =	-0.018078908;
		aLowerBass[9] =	-0.019154703;
		aLowerBass[10] =	-0.020191766;
		aLowerBass[11] =	-0.02118451;
		aLowerBass[12] =	-0.022127545;
		aLowerBass[13] =	-0.023015667;
		aLowerBass[14] =	-0.023844099;
		aLowerBass[15] =	-0.024608238;
		aLowerBass[16] =	-0.025303854;
		aLowerBass[17] =	-0.02592709;
		aLowerBass[18] =	-0.026474472;
		aLowerBass[19] =	-0.026942857;
		aLowerBass[20] =	-0.027329797;
		aLowerBass[21] =	-0.027633052;
		aLowerBass[22] =	-0.027850812;
		aLowerBass[23] =	-0.027981985;
		aLowerBass[24] =	0.88091856;
		aLowerBass[25] =	-0.027981985;
		aLowerBass[26] =	-0.027850812;
		aLowerBass[27] =	-0.027633052;
		aLowerBass[28] =	-0.027329797;
		aLowerBass[29] =	-0.026942857;
		aLowerBass[30] =	-0.026474472;
		aLowerBass[31] =	-0.02592709;
		aLowerBass[32] =	-0.025303854;
		aLowerBass[33] =	-0.024608238;
		aLowerBass[34] =	-0.023844099;
		aLowerBass[35] =	-0.023015667;
		aLowerBass[36] =	-0.022127545;
		aLowerBass[37] =	-0.02118451;
		aLowerBass[38] =	-0.020191766;
		aLowerBass[39] =	-0.019154703;
		aLowerBass[40] =	-0.018078908;
		aLowerBass[41] =	-0.016970146;
		aLowerBass[42] =	-0.015834318;
		aLowerBass[43] =	-0.014677407;
		aLowerBass[44] =	-0.01350546;
		aLowerBass[45] =	-0.01232451;
		aLowerBass[46] =	-0.011140668;
		aLowerBass[47] =	-0.0099598095;
		aLowerBass[48] =	-0.008787861;

		double[] aLower = new double[49];
		
		//Filtre RIF, bande passante 20Hz -> 200Hz
		double[] aHigerBass = new double[49];
		aHigerBass[0] =	-0.01078301;
		aHigerBass[1] =	-0.012320813;
		aHigerBass[2] =	-0.013392618;
		aHigerBass[3] =	-0.013906638;
		aHigerBass[4] =	-0.013785316;
		aHigerBass[5] =	-0.012968764;
		aHigerBass[6] =	-0.0114176655;
		aHigerBass[7] =	-0.0091155125;
		aHigerBass[8] =	-0.006070085;
		aHigerBass[9] =	-0.002314086;
		aHigerBass[10] =	0.002095092;
		aHigerBass[11] =	0.007076491;
		aHigerBass[12] =	0.012527576;
		aHigerBass[13] =	0.018327016;
		aHigerBass[14] =	0.024338216;
		aHigerBass[15] =	0.03041341;
		aHigerBass[16] =	0.036398225;
		aHigerBass[17] =	0.0421366;
		aHigerBass[18] =	0.04747575;
		aHigerBass[19] =	0.05227117;
		aHigerBass[20] =	0.05639134;
		aHigerBass[21] =	0.059722103;
		aHigerBass[22] =	0.062170453;
		aHigerBass[23] =	0.06366763;
		aHigerBass[24] =	0.06417143;
		aHigerBass[25] =	0.06366763;
		aHigerBass[26] =	0.062170453;
		aHigerBass[27] =	0.059722103;
		aHigerBass[28] =	0.05639134;
		aHigerBass[29] =	0.05227117;
		aHigerBass[30] =	0.04747575;
		aHigerBass[31] =	0.0421366;
		aHigerBass[32] =	0.036398225;
		aHigerBass[33] =	0.03041341;
		aHigerBass[34] =	0.024338216;
		aHigerBass[35] =	0.018327016;
		aHigerBass[36] =	0.012527576;
		aHigerBass[37] =	0.007076491;
		aHigerBass[38] =	0.002095092;
		aHigerBass[39] =	-0.002314086;
		aHigerBass[40] =	-0.006070085;
		aHigerBass[41] =	-0.0091155125;
		aHigerBass[42] =	-0.0114176655;
		aHigerBass[43] =	-0.012968764;
		aHigerBass[44] =	-0.013785316;
		aHigerBass[45] =	-0.013906638;
		aHigerBass[46] =	-0.013392618;
		aHigerBass[47] =	-0.012320813;
		aHigerBass[48] =	-0.01078301;

		try
		{	
			byte bytesPrev[] = null;
			byte bytes[] = new byte[50*frameSize];

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

					if(bytesPrev != null)
					{
						byte[] bytesBoth = new byte[bytes.length*2];
						System.arraycopy(bytesPrev, 0, bytesBoth, 0, bytes.length);
						System.arraycopy(bytes, 0, bytesBoth, bytes.length, bytes.length);

						bytesPrev = new byte[bytes.length];
						System.arraycopy(bytes, 0, bytesPrev, 0, bytes.length);

						for(int i = 0; i < 50; i++)
						{
							double valueLeft = 0;
							double valueRight = 0;

							if(bass < 0)
							{
								valueLeft += ((double)(50+bass)/50)*byteToShort(bytesBoth[bytes.length + 4 * i + 0], bytesBoth[bytes.length + 4 * i + 1]);
								valueRight += ((double)(50+bass)/50)*byteToShort(bytesBoth[bytes.length + 4 * i + 2], bytesBoth[bytes.length + 4 * i + 3]);

								for(int j = 1; j < 49; j++)
								{
									valueLeft += ((-(double)bass)/50)*aLowerBass[j]*byteToShort(bytesBoth[bytes.length + 4 * (i-j) + 0], bytesBoth[bytes.length + 4 * (i-j) + 1]);
									valueRight += ((-(double)bass)/50)*aLowerBass[j]*byteToShort(bytesBoth[bytes.length + 4 * (i-j) + 2], bytesBoth[bytes.length + 4 * (i-j) + 3]);
								}
							}
							else if(bass > 0)
							{
								valueLeft += ((double)(bass-50)/50)*byteToShort(bytesBoth[bytes.length + 4 * i + 0], bytesBoth[bytes.length + 4 * i + 1]);
								valueRight += ((double)(bass-50)/50)*byteToShort(bytesBoth[bytes.length + 4 * i + 2], bytesBoth[bytes.length + 4 * i + 3]);

								valueLeft += 0.5*(((double)bass)/50)*byteToShort(bytesBoth[bytes.length + 4 * i + 0], bytesBoth[bytes.length + 4 * i + 1]);
								valueRight += 0.5*(((double)bass)/50)*byteToShort(bytesBoth[bytes.length + 4 * i + 2], bytesBoth[bytes.length + 4 * i + 3]);

								for(int j = 1; j < 49; j++)
								{
									valueLeft += 0.5*(((double)bass)/50)*aHigerBass[j]*byteToShort(bytesBoth[bytes.length + 4 * (i-j) + 0], bytesBoth[bytes.length + 4 * (i-j) + 1]);
									valueRight += 0.5*(((double)bass)/50)*aHigerBass[j]*byteToShort(bytesBoth[bytes.length + 4 * (i-j) + 2], bytesBoth[bytes.length + 4 * (i-j) + 3]);
								}
								
								System.out.println((((double)bass)/50));
							}
							else
							{
								valueLeft += byteToShort(bytesBoth[bytes.length + 4 * i + 0], bytesBoth[bytes.length + 4 * i + 1]);
								valueRight += byteToShort(bytesBoth[bytes.length + 4 * i + 2], bytesBoth[bytes.length + 4 * i + 3]);
							}

							byte[] resLeft = shortToByte((short)valueLeft);
							byte[] resRight = shortToByte((short)valueRight);

							bytes[4*i] = resLeft[0];
							bytes[4*i+1] = resLeft[1];
							bytes[4*i+2] = resRight[0];
							bytes[4*i+3] = resRight[1];
						}
					}
					else
					{
						bytesPrev = bytes;
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