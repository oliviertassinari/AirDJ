package Audio;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Player implements Runnable, IPlayer
{
	/*attributs dedies aux caracteristiques du morceau joue*/
	private File file; //fichier joue
	private int vitesse; //vitesse de lecture par rapport a� la vitesse initiale
	private int currentVolume; //volume du sample joue actuellement

	/*attributs caches*/
	Thread runner; //thread dedie a la lecture du morceau via le buffer
	private int status; //0 tant que le morceau n'a pas ete play puis 1 ou 2 si le morceau est en pause ou pas
	private AudioInputStream audioInputStream;
	private SourceDataLine line;
	private AudioFormat audioFormat;
	private FloatControl gainControl; //controleur pour le volume
	private int byteFromBeginning; //nbr de bytes debut le debut

	public void run() //methode appelee par le thread lorsqu'il start
	{
		this.lecture();
	}

	public Player(String fileName) //initialisation du player
	{
		this.status = 0;
		this.file = new File(fileName);
		this.vitesse = 1;
		this.byteFromBeginning = 0;
		runner = new Thread(this, "player");

		try {
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

		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void setPlay()
	{
		if(status == 0)
		{
			runner.start();
		}
		else
		{
			runner.resume();
		}
		status = 1;
		line.start();
	}

	public void setPause()
	{
		if(status != 0)
		{
			this.status = 2;
			line.stop();
			runner.suspend();
		}
	}

	public void setVolume(float volume) //volume de 0 a 100
	{
		if (volume >= 100) volume = 100;
		if (volume <= 0) volume = 0;
		float max = (float) Math.pow(10.0,gainControl.getMaximum()/20);
		float temp1 = max*volume/100;
		float temp2 = (float) (20*Math.log10(temp1));
		gainControl.setValue(temp2);
	}

	public void setVitesse(int vitesse)
	{
		this.vitesse = vitesse;
	}

	public float getPosition() //elapsed time en seconde
	{
		return byteFromBeginning/(audioFormat.getFrameRate() * audioFormat.getFrameSize());
	}

	public void setPosition(float position) //lance le morceau a partir d'un temps donnee en seconde
	{
		try{
			if(status == 1){ this.setPause(); status = 1; }
			audioInputStream.close();
			audioInputStream = AudioSystem.getAudioInputStream(file);
			long n = (long) (position * audioFormat.getFrameRate() * audioFormat.getFrameSize());
			byteFromBeginning = (int)n;
			audioInputStream.skip(n);
			if(status == 1) this.setPlay();		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}	
	}

	public float getCurrentVolume()
	{
		return this.currentVolume;
	}

	public void lecture() //initialisation du stream audio
	{
		try {
			//Lecture
	 		int frameSize = audioFormat.getFrameSize();
			byte bytes1[] = new byte[5*frameSize*10];
			byte bytes2[] = new byte[5*frameSize];
			int bytesRead=0;
			int temp1 = 0;

			while(((bytesRead = audioInputStream.read(bytes1, 0, 5*frameSize*vitesse)) != -1))
			{
				byteFromBeginning += 5*frameSize*vitesse;

				for(int i =0; i<5;i++)
				{
					for(int j=0; j<frameSize;j++)
					{
						byte temp2 = bytes1[i*frameSize*vitesse+j];
						Byte temp3 = new Byte(temp2);
						temp1 =  ((i*frameSize+j)*temp1 + Math.abs(temp3.intValue()))/(i*frameSize+j+1);
						bytes2[frameSize*i+j] = temp2;
					}
				}

				line.write(bytes2, 0, bytes2.length);
				currentVolume = temp1;
				temp1 = 0;
			}

			line.close(); //Fermeture de la ligne
			
		} catch (IOException e) {
				e.printStackTrace();
		} 	
	}

	public float getLength() //longeur du morcheau en seconde
	{
	    return file.length()/(audioFormat.getFrameSize()*audioFormat.getFrameRate());
	}
}
