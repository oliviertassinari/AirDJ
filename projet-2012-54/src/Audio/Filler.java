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
	 * Player we will fill the arrayVolume
	 */
	private Player player;
	
	/**
	 * As Thread start he calls this method
	 */
	public void run() 
	{
		this.fillVolumeArray();
	}
	
	/**
	 * CONSTRUCTOR
	 * @param player
	 */
	public Filler (Player player) 
	{
		this.player = player;
		filler = new Thread(this, "filler");
		filler.start();
	
	}
	
	/** 
	 * Fill the arrayVolume of the player provided
	 */
	public void fillVolumeArray() 
	{
		try
		{
			int frameSize = player.getFrameSize();
			float frameRate= player.getFrameRate();
			File file = player.getFile();
			
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			
			int bytesCinqCentieme = (int) (frameSize * frameRate / 20);
			byte bytes[] = new byte[bytesCinqCentieme];
			int pos = 0;
			int i = 0;
			int ind[] = new int[2];
			int tmp[] = new int[2];
			
			while(((ais.read(bytes, 0, bytes.length)) != -1))
			{
				while(i<bytes.length)
				{
					Byte temp = new Byte(bytes[i]);
					tmp[ind[1]] += Math.abs(temp.intValue());
					ind[0] ++;
					if (ind[0] == 2) ind[1] = 1;
					if (ind[0] == 4) {ind[0]=0; ind[1] = 0;}
					i++;
				}
				tmp[0] = 2*tmp[0]/bytes.length;		
				tmp[1] = 2*tmp[1]/bytes.length;
				if (pos == 0) 
				{
					player.setVolumeArray(tmp[0], pos, 0); 
					player.setVolumeArray(tmp[1], pos, 1);
				}
				if (pos != 0) 
				{
					player.setVolumeArray((int) (0.4 * player.getVolumeArray(pos-1, 0) + 0.7 * tmp[0]), pos, 0);
					player.setVolumeArray((int) (0.4 * player.getVolumeArray(pos-1, 1) + 0.7 * tmp[1]), pos, 1);
				}
				pos ++;
				i = 0;
				tmp[0] = 0;
				tmp[1] = 0;
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
