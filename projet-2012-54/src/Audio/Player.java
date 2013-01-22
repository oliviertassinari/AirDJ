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

	/*attributs caches*/
	Thread runner; //thread dedie a la lecture du morceau via le buffer
	private int status; //0 pause - 1 play
	private AudioInputStream audioInputStream;
	private SourceDataLine line;
	private AudioFormat audioFormat;
	private FloatControl gainControl; //controleur pour le volume
	private int byteFromBeginning; //nbr de bytes debut le debut

	public void run() //methode appelee par le thread lorsqu'il start
	{
		try
		{
			//Lecture
	 		int frameSize = audioFormat.getFrameSize();
	 		float frameRate = audioFormat.getFrameRate();
			byte bytes[] = new byte[5*frameSize];
			int bytesRead=0;
			int bytesDixiemeSeconde = (int) (frameSize * frameRate / 10);
			
			int tmp1 = 0;
			int tmp2 = 0;
			int tmp3 = 0;
			int tmp4 = 0;
			
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
					
					tmp3 = byteFromBeginning % bytesDixiemeSeconde;
					if (tmp3 == 0)
					{
						currentVolume = (int) (currentVolume*0.5 + 0.5*3*tmp2);
						tmp2 = 0;
					}
					byteFromBeginning += 5*frameSize;
					for(int i =0; i<bytes.length;i++)
					{
							Byte temp0 = new Byte(bytes[i]);
							tmp1 += Math.abs(temp0.intValue())/(bytes.length);
					}
	
					line.write(bytes, 0, bytes.length);
					tmp2 = (tmp2*tmp3 +  tmp1*5*frameSize)/(tmp3+5*frameSize);
					tmp1 = 0;
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

			System.out.println(info.toString());

			//Recuperation et initialisation du volume
			gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
			this.setVolume(50);

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

	public void setVitesse(int vitesse) //vitesse entiere positive: a modifier
	{
		this.vitesse = vitesse;
	}

	public float getPosition() //retourne la position en seconde
	{
		return byteFromBeginning/(audioFormat.getFrameRate() * audioFormat.getFrameSize());
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

			long n = (long)(position * audioFormat.getFrameRate() * audioFormat.getFrameSize());
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
		return currentVolume;
	}

	public float getLength() //duree totale du morceau
	{
	    return file.length()/(audioFormat.getFrameSize()*audioFormat.getFrameRate());
	}
}
