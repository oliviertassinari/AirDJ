
package Vue;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Audio.IPlayer;

public class VueSpectre
{
	private BufferedImage imageSpectre;
	private IPlayer player;

	public VueSpectre(IPlayer player)
	{
		this.player = player;
	}

	public BufferedImage get()
	{
		int[][] volumeArray = player.getVolumeArray();

		float listdim = volumeArray[0].length;
		float m = listdim / 368;
		int max = 0;
		int pixelLength = (int)(listdim / m);

		for(int i = 0; i < pixelLength; i++)
		{
			int S = 0;

			for(int j = 0; j < m; j++)
			{
				S = (volumeArray[0][(int)(m * i + j)] + volumeArray[1][(int)(m * i + j)]) / 2 + S;
			}

			if(max < S)
			{
				max = S;
			}
		}

		imageSpectre = new BufferedImage(pixelLength, 29, BufferedImage.TRANSLUCENT);
		Graphics g = imageSpectre.getGraphics();

		g.setColor(Color.black);
		g.fillRect(0, 0, pixelLength, 29);

		g.setColor(Color.gray);
		g.drawRect(0, 0, pixelLength - 1, 28);

		for(int i = 0; i < pixelLength; i++)
		{
			int S = 0;

			for(int j = 0; j < m; j++)
			{
				S = (volumeArray[0][(int)(m * i + j)] + volumeArray[1][(int)(m * i + j)]) / 2 + S;
			}

			g.setColor(Color.gray);
			g.drawLine(i, 14, i, 14 - S / (max / 13 + 1));
			g.drawLine(i, 14, i, 14 + S / (max / 13 + 1));
		}

		return imageSpectre;
	}
}
