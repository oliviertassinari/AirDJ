package Audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Filler implements Runnable {

	/**
	 * 
	 */
	private Thread filler;
	
	/**
	 * Player dont on rempli le tableau de volume
	 */
	private Player player;
	
	/**
	 * 
	 */
	public void run() 
	{
		this.fillVolumeArray();
	}
	
	/**
	 * Rempli le tableau de volume du player donnee en parametre
	 * @param player
	 */
	public Filler (Player player) 
	{
		this.player = player;
		filler = new Thread(this, "filler");
		filler.start();
	
	}
	
	/** 
	 * Rempli le tableau ou chaque case correspond au volume d une tranche de 0.05 seconde
	 */
	public void fillVolumeArray() 
	{
		try
		{
			int frameSize = player.getFrameSize();
			float frameRate= player.getFrameRate();
			File file = player.getFile();
			
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			
			int bytesDemiDixiemeSeconde = (int) (frameSize * frameRate / 20);
			byte bytes[] = new byte[bytesDemiDixiemeSeconde];
			int pos = 0;
			int i = 0;
			int bytesRead = 0;

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
					if (tmp1 < 10) tmp1 = 0;
					tmp2 = (5*frameSize*tmp1 + tmp2*i )/(5*frameSize + i);
					tmp1 = 0;
					i += 5*frameSize;
				}
				if (pos == 0) player.setVolumeArrayI(tmp1, pos);
				if (pos != 0) player.setVolumeArrayI((int) (0.5 * player.getVolumeArrayI(pos-1) + 2 * tmp2), pos);
				pos ++;
				i = 0;
				tmp2 = 0;
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
	}
	
	
}
