package Audio;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Player implements Runnable, IPlayer
{
	/**
	 * Fichier joue
	*/
	private File file;
	
	/** 
	 * NON FONCTIONELLE
	 * @deprecated
	 */
	private int vitesse;
	
	/** 
	 * Volume de la tranche de 0.05 sec joue
	 */
	private int[] currentVolume = new int[2];
	
	/** 
	 * Tableau contenant tous les volumes des tranches de 0.05 sec
	 */
	private int[] volumeArray;
	
	/** 
	 * Taux dechantillonage
	 */
	private float frameRate;
	
	/** 
	 * Taille d une frame
	 */
	private int frameSize;

	/** 
	 * Thread gerant pause / play
	 */
	Thread runner;
	
	/** 
	 * 0 pause - 1 play
	 */
	private int status;
	
	private AudioInputStream audioInputStream;
	
	private SourceDataLine line;
	
	private AudioFormat audioFormat;
	
	/** 
	 * Controleur du gain
	 */
	private FloatControl gainControl;
	
	/** 
	 * Nombres de bytes depuis le debut
	 */
	private int byteFromBeginning;
	
	/** 
	 * Methode appelee par le thread lorsque celui commence, qui gere la lecture de la piste audio
	 */
	public void run()
	{
		try
		{	
			byte bytes[] = new byte[5*frameSize];
			int bytesRead = 0;
			
			synchronized(this)
			{
				while(((bytesRead = audioInputStream.read(bytes, 0, 5*frameSize)) != -1))
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
					currentVolume[0] = volumeArray[pos];
					currentVolume[1] = volumeArray[pos];

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

	/** 
	 * Constructeur qui initialise le player avec les differents parametres standards
	 * @param fileName Chemin du fichier qui sera joue
	 */
	public Player(String fileName)
	{
		status = 0;
		file = new File(fileName);
		vitesse = 1;
		byteFromBeginning = 0;
		runner = new Thread(this, "player");
		try
		{
			//Creation du flux audio, recuperation de la bonne line et demarrage de celle ci
			audioInputStream = AudioSystem.getAudioInputStream(file);
			audioFormat = audioInputStream.getFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(audioFormat);
			
			frameSize = audioFormat.getFrameSize();
	 		frameRate = audioFormat.getFrameRate();
	 		volumeArray = new int[((int) (file.length()/(frameSize*frameRate)*20))+1];
			Filler filler = new Filler(this);
			
			//Recuperation et initialisation du volume
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
	 * Methode permettant de mettre le morceau en lecture s'il est en pause
	 */
	public synchronized void setPlay()
	{
		status = 1;
		notify();
	}

	/** 
	 * Methode permettant de mettre le morceau en pause s'il est en lecture
	 */
	public void setPause()
	{
		status = 0;
	}

	/**
	 * Methode qui regle le volume
	 * @param volume de 0 a 100
	 */
	public void setVolume(float volume) 
	{
		float max = (float)Math.pow(10.0, gainControl.getMaximum()/20);
		float temp1 = max*volume/100;
		float temp2 = (float)(20*Math.log10(temp1));

		gainControl.setValue(temp2);
	}

	/** 
	 * NON FONCTIONELLE
	 * @deprecated
	 * @param vitesse
	 */
	public void setVitesse(int vitesse)
	{
		this.vitesse = vitesse;
	}

	/** 
	 * Retourne la position dans la piste audio
	 * @return en seconde
	 */
	public float getPosition()
	{
		return byteFromBeginning/(frameRate * frameSize);
	}

	/** 
	 * Avance a la position souhaite
	 * @param position en seconde
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
	 * Donne la position actuel en seconde
	 * @return volume du sample en cours
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
	 * Donne la longeur du morceau en seconde
	 * @return duree totale du morceau en seconde
	 */
	public float getLength()
	{
	    return file.length()/(frameSize*frameRate);
	}

	/**
	 * Renitialise le morceau
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
	 * @return fichier joue
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
	 * 
	 * @param volume
	 * @param i case du tableau
	 */
	public void setVolumeArrayI(int volume, int i)
	{
		this.volumeArray[i] = volume;
	}
	
	/**
	 * 
	 * @param i case du tableau	
	 * @return valeur de la case en question
	 */
	public int getVolumeArrayI(int i)
	{
		return this.volumeArray[i];
	}

}