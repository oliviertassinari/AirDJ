package Audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
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
			AudioFormat af = player.getAudioFormat();
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);

			int bytesDemiDixiemeSeconde = (int)(frameSize * frameRate / 20);
			byte bytes[] = new byte[bytesDemiDixiemeSeconde];
			int pos = 0;
			int i = 0;
			double max = 5000000;
			double tmp[] = new double[2];

			while(((ais.read(bytes, 0, bytes.length)) != -1))
			{
				while(i < bytes.length)
				{
					if(af.isBigEndian())
					{
							tmp[0] += 4*(((bytes[i + 0] << 8) | (bytes[i + 1] & 0xFF))*((bytes[i + 0] << 8) | (bytes[i + 1] & 0xFF)))/bytes.length ;
							tmp[1] += 4*(((bytes[i + 2] << 8) | (bytes[i + 3] & 0xFF))*((bytes[i + 2] << 8) | (bytes[i + 3] & 0xFF)))/bytes.length;
					}
					else
					{
							tmp[0] += 4*(((bytes[i + 0] & 0xFF) | (bytes[i + 1] << 8))*((bytes[i + 0] & 0xFF) | (bytes[i + 1] << 8)))/bytes.length;
							tmp[1] += 4*(((bytes[i + 2] & 0xFF) | (bytes[i + 3] << 8))*((bytes[i + 2] & 0xFF) | (bytes[i + 3] << 8)))/bytes.length;
					}
					i += 4;
				}
				if(max<tmp[0]) max = tmp[0];
				if(max<tmp[1]) max = tmp[1];
				tmp[0]=tmp[0]/max*100;
				tmp[1]=tmp[1]/max*100;
				if(pos == 0)
				{
					player.setVolumeArray((int) tmp[0], pos, 0);
					player.setVolumeArray((int) tmp[1], pos, 1);
				}
				if(pos != 0)
				{
					player.setVolumeArray((int)(0.2 * player.getVolumeArray(pos - 1, 0) + 0.8 * tmp[0]), pos, 0);
					player.setVolumeArray((int)(0.2 * player.getVolumeArray(pos - 1, 1) + 0.8 * tmp[1]), pos, 1);
				}
				pos++;
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

	public void interrupt()
	{
		filler.interrupt();
	}
}
