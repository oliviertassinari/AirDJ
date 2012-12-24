package Vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class VueCrossfinderOver extends JPanel
{
	private int crossfinder = 0;
	private int volumeP1 = 100;
	private int volumeP2 = 100;
	private int[] displayVolumeP1 = {0, 0};
	private int[] displayVolumeP2 = {0, 0};

	public VueCrossfinderOver()
	{
		setPreferredSize(new Dimension(250, 300));
		setOpaque(false);
	}

	protected void paintComponent(Graphics g)
	{
		Image imageVolumeCursor = new ImageIcon("image/volumeCursor.png").getImage();
		g.drawImage(imageVolumeCursor, 94, (int)(148-volumeP1*0.78), null);
		g.drawImage(imageVolumeCursor, 134, (int)(148-volumeP2*0.78), null);

		Image imageCrossfinderCursor = new ImageIcon("image/crossfinderCursor.png").getImage();
		g.drawImage(imageCrossfinderCursor, (int)(116+crossfinder*0.5), 238, null);

		g.setColor(new Color(0x28acff));
		g.fillRect(34, 53+120-(int)(displayVolumeP1[0]*1.2), 9, (int)(displayVolumeP1[0]*1.2));
		g.fillRect(44, 53+120-(int)(displayVolumeP1[1]*1.2), 9, (int)(displayVolumeP1[1]*1.2));

		g.setColor(new Color(0xff171a));
		g.fillRect(197, 53+120-(int)(displayVolumeP2[0]*1.2), 9, (int)(displayVolumeP2[0]*1.2));
		g.fillRect(207, 53+120-(int)(displayVolumeP2[1]*1.2), 9, (int)(displayVolumeP2[1]*1.2));

		Image imageDisplayVolumeOver = new ImageIcon("image/displayVolumeOver.png").getImage();
		g.drawImage(imageDisplayVolumeOver, 34, 53, null);
		g.drawImage(imageDisplayVolumeOver, 197, 53, null);
	}

	public void displayVolumeP1(int left, int right)
	{
		displayVolumeP1[0] = left;
		displayVolumeP1[1] = right;
		repaint();
	}

	public void displayVolumeP2(int left, int right)
	{
		displayVolumeP2[0] = left;
		displayVolumeP2[1] = right;
		repaint();
	}

	public void setVolumeP1(int value)
	{
		volumeP1 = value;
		repaint();
	}

	public void setVolumeP2(int value)
	{
		volumeP2 = value;
		repaint();
	}

	public void setCrossfinder(int value)
	{
		crossfinder = value;
		repaint();
	}
}
