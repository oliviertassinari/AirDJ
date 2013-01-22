package Audio;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Player implements Runnable, IPlayer
{
	/*attributs dedies aux caracteristiques du morceau joue*/
	private File file; //fichier joue
	private int vitesse; //vitesse de lecture par rapport a la vitesse initiale
	private int currentVolume; //volume du sample joue actuellement
	private int[] volumeArray;
	private float frameRate;
	private int frameSize;

	/*attributs caches*/
	Thread runner; //thread dedie a la lecture du morceau via le buffer
	private int status; //0 pause - 1 play
	private AudioInputStream audioInputStream;
	private SourceDataLine line;
	private AudioFormat audioFormat;
	private FloatControl gainControl; //controleur pour le volume
	private int byteFromBeginning; //nbre de bytes debut le debut


	public void run() //methode appelee par le thread lorsqu'il start
	{
		try
		{
			//Lecture
			byte bytes[] = new byte[5*frameSize];
			int bytesRead=0;
			
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
					currentVolume = volumeArray[pos];
					line.write(bytes, 0, bytes.length);
				}
	
				line.drain(); //On attend que le buffer soit vide
				line.stop();
				line.close(); //Fermeture de la ligne
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	public Player(String fileName) //initialisation du player
	{
		this.status = 0;
		this.file = new File(fileName);
		this.vitesse = 1;
		this.byteFromBeginning = 0;
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
	 		
			//Recuperation et initialisation du volume
			gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
			this.setVolume(50);
			
			this.fillVolumeArray();
			runner.start();
		}
		catch(UnsupportedAudioFileException e){
			e.printStackTrace();	
		}
		catch(IOException e){
			e.printStackTrace();
		}
		catch(LineUnavailableException e){
			e.printStackTrace();
		}
	}

	public synchronized void setPlay()
	{
		status = 1;
		notify();
	}

	public void setPause()
	{
		status = 0;
	}

	public void setVolume(float volume) //volume de 0 a 100
	{
		float max = (float)Math.pow(10.0, gainControl.getMaximum()/20);
		float temp1 = max*volume/100;
		float temp2 = (float)(20*Math.log10(temp1));

		gainControl.setValue(temp2);
	}

	public void setVitesse(int vitesse) //non fonctionnelle pour l'instant
	{
		this.vitesse = vitesse;
	}

	public float getPosition() //retourne la position en seconde
	{
		return byteFromBeginning/(frameRate * frameSize);
	}

	public void setPosition(float position) //avance a la position souhaite en seconde
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
		catch(IOException e){
			e.printStackTrace();
		}
		catch(UnsupportedAudioFileException e){
			e.printStackTrace();
		}	
	}

	public float getCurrentVolume() //volume du sample en cours
	{
		if (status == 0) {return 0;}
		else {return currentVolume;}
	}

	public float getLength() //duree totale du morceau
	{
	    return file.length()/(frameSize*frameRate);
	}
	
	public void fillVolumeArray() 
	{
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			
			int bytesDemiDixiemeSeconde = (int) (frameSize * frameRate / 20);
			byte bytes[] = new byte[bytesDemiDixiemeSeconde];
			int pos = 0;
			int i = 0;
			volumeArray = new int[((int) (file.length()/(frameSize*frameRate)*20))+1];
			int bytesRead=0;

			int tmp1 = 0;
			int tmp2 = 0;

			while(((bytesRead = ais.read(bytes, 0, bytes.length)) != -1))
				{
					while(i<bytes.length)
					{
						for(int j=0;j<5*frameSize;j++)
						{
							Byte temp0 = new Byte(bytes[i+j]);
							tmp1 += Math.abs(temp0.intValue())/(5*frameSize);
						}
						tmp2 = (5*frameSize*tmp1 + tmp2*i )/(5*frameSize + i);
						tmp1 = 0;
						i += 5*frameSize;
					}
					if (pos == 0) volumeArray[pos] = tmp1;
					if (pos != 0) volumeArray[pos]=(int) (0.4 * volumeArray[pos-1] + 1.5 * tmp2);
					pos ++;
					i = 0;
					tmp2 = 0;
				}
		}catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

