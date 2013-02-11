package Audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class BeatDetection implements Runnable{

	/**
	 * 
	 */
	private Thread detector;
	
	/**
	 * 
	 */
	private Player player;
	
	/**
	 * 
	 */
	public void run() 
	{
		this.beatDetection();
	}
	
	/**
	 * CONSTRUCTOR
	 * @param player
	 */
	public BeatDetection(Player player) 
	{
		this.player = player;
		detector = new Thread(this, "detector");
		detector.start();
	
	}
	
	/** 
	 * 
	 */
	public void beatDetection() 
	{
		try
		{
			int frameSize = player.getFrameSize();
			float frameRate= player.getFrameRate();
			File file = player.getFile();
			
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			
			int bytesUneSeconde = (int) (frameSize * frameRate);
			int bytesCinqCentieme = (int) (frameSize * frameRate / 20);
			byte bytes[] = new byte[bytesCinqCentieme];
			int[] beat = new int[(int) (file.length()/bytesCinqCentieme)+1];
			int C = 1;
			
			int pos[] = new int[2];
			pos[0] = 0;
			pos[1] = 0;
			
			int tmp[] = new int[2];
			tmp[0] = 0;
			tmp[1] = 0;
			
			while(((ais.read(bytes, 0, bytesCinqCentieme)) != -1))
			{
				System.out.println("pos " + pos[0]);
				pos[1] = 0;
				while(pos[1]<bytesCinqCentieme)
				{
					Byte temp = new Byte(bytes[pos[1]]);
					tmp[0] += Math.pow(Math.abs(temp.intValue()), 2);
					pos[1]++;
				}
				beat[pos[0]] = tmp[0];
				System.out.println("e " + tmp[0]);
				tmp[0] = 0;
				
				if (pos[0]>20)
				{
					pos[1] = 0;
					while(pos[1]<20)
					{
						tmp[1] += 0.05*beat[pos[0]-pos[1]];
						pos[1]++;
					}
					System.out.println("E " + tmp[1]);

					if(tmp[0]>C*tmp[1]) 
					{
						player.setBeatArray(1, pos[0]);				
						System.out.println("beat " + 1);
					}
					else
					{
						player.setBeatArray(0, pos[0]);				
						System.out.println("beat " + 0);
					}
					tmp[1] = 0;
				}
				pos[0]++;
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
