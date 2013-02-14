
package Vue;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Audio.IPlayer;

public class VueSpectre
{
	private int couleur;
	
	public VueSpectre(int couleur)
	{
		this.couleur = couleur;
	}

	public BufferedImage get(int current, int total, IPlayer player)
	{
		if(player != null)
		{
			int[][] volumeArray = player.getVolumeArray();

			float listdim = volumeArray[0].length;
			float m = listdim / 366;
			int max = 0;
			int pixelLength = (int)(listdim / m);

			for(int i = 0; i < pixelLength; i++)
			{
				int S = 0;

				for(int j = 0; j < m; j++)
				{
					S += (volumeArray[0][(int)(m * i + j)] + volumeArray[1][(int)(m * i + j)]) / 2;
				}

				if(max < S)
				{
					max = S;
				}
			}

			BufferedImage imageSpectre = new BufferedImage(pixelLength+2, 29, BufferedImage.TRANSLUCENT);
			Graphics g = imageSpectre.getGraphics();

			g.setColor(new Color(0x1b1b1b));
			g.fillRect(0, 0, pixelLength+2, 29);

			g.setColor(new Color(0x58595a));
			g.drawRect(0, 0, pixelLength+1, 28);

			if(current != 0)
			{
				if(couleur == 0)
				{
					g.setColor(new Color(0, 127, 172));
				}
				else
				{
					g.setColor(new Color(255, 24, 24));
				}
			}
			else
			{
				g.setColor(new Color(0x939598));
			}

			for(int i = 0; i < pixelLength; i++)
			{
				int S = 0;

				for(int j = 0; j < m; j++)
				{
					S += (volumeArray[0][(int)(m * i + j)] + volumeArray[1][(int)(m * i + j)]) / 2;
				}

				if((int)(pixelLength * current / total) == i)
				{
					g.setColor(new Color(0x939598));
				}

				g.drawLine(i+1, 14, i+1, 14 - S / (max / 13 + 1));
				g.drawLine(i+1, 14, i+1, 14 + S / (max / 13 + 1));
			}

			return imageSpectre;
		}
		else
		{
			BufferedImage imageSpectre = new BufferedImage(368, 29, BufferedImage.TRANSLUCENT);
			Graphics g = imageSpectre.getGraphics();

			g.setColor(new Color(0x1b1b1b));
			g.fillRect(0, 0, 368, 29);

			g.setColor(new Color(0x58595a));
			g.drawRect(0, 0, 367, 28);

			return imageSpectre;
		}
	}
}
