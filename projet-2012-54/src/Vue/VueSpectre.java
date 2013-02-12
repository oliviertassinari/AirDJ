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

		imageSpectre = new BufferedImage(332, 30, BufferedImage.TRANSLUCENT);
		Graphics g = imageSpectre.getGraphics();

        int listdim = volumeArray.length; 
        int m = listdim / 332 + 1;
        int max = 0;
 
        for(int k = 0; k < listdim; k++)
        {
        	if(max < volumeArray[0][k])
        	{
        		max = volumeArray[0][k];
        	}
        }

        for(int i = 0; i < listdim/m; i++)
        {
        	g.setColor(Color.DARK_GRAY);
        	int S = 0;

        	for(int j = 0; j < m; j++)
        	{
        		S = volumeArray[0][m*i + j] + S;
        	}

        	g.fillRect(i, 15 - (S/m)/(max/20 + 1),
        			1, (S/m)/(max/20 + 1));
        	g.setColor(Color.gray);
        	g.fillRect(i, 15, 1, (S/m)/(max/10 + 1));
        }  

        return imageSpectre;
	}
}
