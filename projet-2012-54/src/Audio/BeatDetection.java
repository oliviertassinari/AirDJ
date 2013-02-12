/*package Audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class BeatDetection implements Runnable{

	/**
	 * 
	 *
	private Thread detector;
	
	/**
	 * 
	 *
	private Player player;
	
	/**
	 * 
	 *
	public void run() 
	{
		this.beatDetection();
	}
	
	/**
	 * CONSTRUCTOR
	 * @param player
	 *
	public BeatDetection(Player player) 
	{
		this.player = player;
		//detector = new Thread(this, "detector");
		//detector.start();
		this.beatDetection();
	
	}
	
	/** 
	 * 
	 *
	public void beatDetection() 
	{
		try
		{
			int frameSize = player.getFrameSize();
			float frameRate= player.getFrameRate();
			File file = player.getFile();
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			
			int bytesUneSeconde = (int) (frameSize * frameRate);
			int bytesUnCentieme = (int) (frameSize * frameRate / 100);
			
			byte bytes[] = new byte[(int) file.length()];
			double bytesBis[] = new double[(int) file.length()];
			while(((ais.read(bytes, 0, bytes.length)) != -1));
				
			for (int i =0; i<bytes.length; i++) 
				{
				Byte temp = new Byte(bytes[i]);
				bytesBis[i] = temp.doubleValue();
				}
			
			for (int i = 1; i<bytes.length; i++) 
			{
			bytesBis[i] = bytesBis[i] + 0*bytesBis[i-1];
			}
			
			int[] beat = new int[(int) (file.length()/bytesUnCentieme)+1];
			float C = 1.4f;
			
			int pos[] = new int[3];

			int tmp[] = new int[4];
			tmp[0] = 0;
			tmp[1] = 0;
			tmp[2] = 0;
			tmp[3] = 0;
			
			for (pos[0]=0; pos[0]<bytes.length-bytesUnCentieme; pos[0]+=bytesUnCentieme)
			{
				for (pos[1]=0; pos[1]<bytesUnCentieme; pos[1]++) 
				{
					tmp[0] += Math.pow(Math.abs(bytesBis[pos[0]+pos[1]]), 2);
				}
				beat[pos[2]] = tmp[0];
				
				if (pos[2]>20)
				{
					for(pos[1] = 0; pos[1]<20; pos[1]++) tmp[1] += 0.05*beat[pos[2]-pos[1]];
					if(tmp[0]>C*tmp[1]) 
					{
						player.setBeatArray(1, pos[2]);				
					}
					else
					{
						player.setBeatArray(0, pos[2]);				
					}
					tmp[0] = 0;
					tmp[1] = 0;
				}		
				
				pos[2]++;
			}
			
			for (int i=0; i<=30*100; i++)
			{
				tmp[0] = player.getBeatArray(i);
				if (tmp[0] == 1 && player.getBeatArray(i+1) == 0) 
				{
					//if (tmp[3]>20)
					//{
						if (tmp[1]>3*tmp[2]/4)
						{
							tmp[2] = (tmp[3]*tmp[2]+tmp[1])/(tmp[3]+1);
							tmp[1] = 0;
							tmp[3]++;
						}
						else
						{
							tmp[1]++; 
						}
					//}
					//else
					//{
					//	tmp[2] = (tmp[3]*tmp[2]+tmp[1])/(tmp[3]+1);
					//	tmp[1] = 0;
					//	tmp[3]++;
					//}
				}
				else
				{
					tmp[1]++;
				}
			}
			player.setBPM(tmp[2]);

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
*/