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

        int listdim = volumeArray[0].length; 
        int m = listdim / 350 + 1;
        int max = 0;
 
        for(int i = 0; i < listdim/m; i++)
        {
        	int S = 0;

        	for(int j = 0; j < m; j++)
        	{
        		S = (volumeArray[0][m*i + j] + volumeArray[1][m*i + j])/2 + S;
        	}

        	if(max < S)
        	{
        		max = S;
        	}
        }
      
		imageSpectre = new BufferedImage(listdim/m, 39, BufferedImage.TRANSLUCENT);
		Graphics g = imageSpectre.getGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, listdim/m, 40);
		
		g.setColor(Color.gray);
		g.drawRect(0, 0, listdim/m-1, 38);
		//g.drawLine(0, 20, listdim/m-1, 20);

        for(int i = 0; i < listdim/m; i++)
        {
        	int S = 0;

        	for(int j = 0; j < m; j++)
        	{
        		S = (volumeArray[0][m*i + j] + volumeArray[1][m*i + j])/2 + S;
        	}

        	g.setColor(Color.gray);
        	g.drawLine(i, 19, i, 19 - S/(max/18+1));
        	g.drawLine(i, 19, i, 19 + S/(max/18+1));
		}

		return imageSpectre;
	}
}
