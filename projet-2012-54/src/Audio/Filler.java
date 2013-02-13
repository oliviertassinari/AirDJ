package Audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Filler implements Runnable
{

	/**
	 * 
	 */
	private Thread filler;

	/**
	 * Player we will the arrayVolume
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
	public Filler(Player player)
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
			float frameRate = player.getFrameRate();
			File file = player.getFile();

			AudioInputStream ais = AudioSystem.getAudioInputStream(file);

			int bytesDemiDixiemeSeconde = (int)(frameSize * frameRate / 20);
			byte bytes[] = new byte[bytesDemiDixiemeSeconde];
			int pos = 0;
			int i = 0;
			int ind[] = new int[2];
			int tmp[][] = new int[2][2];

			while(((ais.read(bytes, 0, bytes.length)) != -1))
			{
				while(i < bytes.length)
				{
					for(int j = 0; j < 5 * frameSize; j++)
					{
						Byte temp0 = new Byte(bytes[i + j]);
						tmp[ind[1]][0] += Math.abs(temp0.intValue()) / (5 * frameSize / 2);
						ind[0]++;
						if(ind[0] == 2)
							ind[1] = 1;
						if(ind[0] == 4)
						{
							ind[0] = 0;
							ind[1] = 0;
						}
					}
					if(tmp[0][0] < 10)
						tmp[0][0] = 0;
					if(tmp[1][0] < 10)
						tmp[1][0] = 0;
					tmp[0][1] = (5 * frameSize * tmp[0][0] + tmp[0][1] * i) / (5 * frameSize + i);
					tmp[1][1] = (5 * frameSize * tmp[1][0] + tmp[1][1] * i) / (5 * frameSize + i);
					tmp[0][0] = 0;
					tmp[1][0] = 0;
					i += 5 * frameSize;
				}
				if(tmp[0][1] > tmp[1][1])
					tmp[0][1] += 1.5 * (tmp[0][1] - tmp[1][1]);
				if(tmp[1][1] > tmp[0][1])
					tmp[1][1] += 1.5 * (tmp[1][1] - tmp[0][1]);
				if(pos == 0)
				{
					player.setVolumeArray(tmp[0][0], pos, 0);
					player.setVolumeArray(tmp[1][0], pos, 1);
				}
				if(pos != 0)
				{
					player.setVolumeArray((int)(0.5 * player.getVolumeArray(pos - 1, 0) + 1.5 * tmp[0][1]), pos, 0);
					player.setVolumeArray((int)(0.5 * player.getVolumeArray(pos - 1, 1) + 1.5 * tmp[1][1]), pos, 1);
				}
				pos++;
				i = 0;
				tmp[0][1] = 0;
				tmp[1][1] = 0;
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
