package Audio;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Player implements Runnable, IPlayer
{
	/** Attributs dedies aux caracteristiques du morceau joue
	 * 
	 */
	private File file;
	private int vitesse;
	private int currentVolume; //volume du sample joue actuellement
	private int[] volumeArray;
	private float frameRate;
	private int frameSize;

	
	
	/** Attributs caches
	 * 
	 */
	Thread runner;
	private int status; //0 pause - 1 play
	private AudioInputStream audioInputStream;
	private SourceDataLine line;
	private AudioFormat audioFormat;
	private FloatControl gainControl;
	private int byteFromBeginning;

	
	
	/** Methode appelee par le thread lorsque celui commence, qui gere la lecture de la piste audio
	 * 
	 */
	public void run()
	{
		try
		{
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
	
				line.drain();
				line.stop();
				line.close();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	
	
	/** Constructeur qui initialise le player avec les differents parametres standards
	 *
	 * @param fileName Chemin du fichier qui sera joue
	 */
	public Player(String fileName)
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

	
	
	/** Methode permettant de mettre le morceau en lecture s'il est en pause
	 * 
	 */
	public synchronized void setPlay()
	{
		status = 1;
		notify();
	}

	
	
	/** Methode permettant de mettre le morceau en pause s'il est en lecture
	 * 
	 */
	public void setPause()
	{
		status = 0;
	}

	
	
	/** Methode qui regle le volume
	 * @param volume Valeur de 0 a 100
	 */
	public void setVolume(float volume) 
	{
		float max = (float)Math.pow(10.0, gainControl.getMaximum()/20);
		float temp1 = max*volume/100;
		float temp2 = (float)(20*Math.log10(temp1));

		gainControl.setValue(temp2);
	}

	
	
	/** Methode non fonctionelle
	 * @deprecated NON FONCTIONELLE
	 */
	public void setVitesse(int vitesse)
	{
		this.vitesse = vitesse;
	}

	
	
	/** Retourne la position dans la piste audio
	 * @return Position en seconde
	 */
	public float getPosition()
	{
		return byteFromBeginning/(frameRate * frameSize);
	}

	
	
	/** Met la piste audio a un certain temps donne
	 * @param position Position souhaitee en seconde
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
		catch(IOException e){
			e.printStackTrace();
		}
		catch(UnsupportedAudioFileException e){
			e.printStackTrace();
		}	
	}

	
	
	/** Retourne le volume du sample jouee par tranche de 0.05 seconde
	 * @return 
	 */
	public float getCurrentVolume()
	{
		if (status == 0) {return 0;}
		else {return currentVolume;}
	}

	
	
	/** Retourne la duree totale du morceau en seconde
	 * @return Duree totale du morceau en seconde
	 */
	public float getLength()
	{
	    return file.length()/(frameSize*frameRate);
	}
	
	
	
	/** Rempli le tableau ou chaque case correspond au volume d'une tranche de 0.05 seconde
	 * 
	 */
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
					if (pos != 0) volumeArray[pos]=(int) (0.2 * volumeArray[pos-1] + 2 * tmp2);
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

