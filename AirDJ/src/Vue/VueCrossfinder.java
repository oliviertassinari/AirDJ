package Vue;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class VueCrossfinder extends JPanel
{
	VueCrossfinderOver vueCrossfinderOver;

	public VueCrossfinder()
	{
		vueCrossfinderOver = new VueCrossfinderOver();
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		add(vueCrossfinderOver);
	}

	protected void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		GradientPaint gp1 = new GradientPaint(0, 0, new Color(0x383839), 0, 300, new Color(0x171718));
		g2d.setPaint(gp1);
		g2d.fillRect(0, 0, 250, 300);

		GradientPaint gp2 = new GradientPaint(0, 0, new Color(0x272726), 0, 220, new Color(0x0a0909));
		g2d.setPaint(gp2);
		g2d.fillRect(85, 0, 80, 220);

		GradientPaint gp3 = new GradientPaint(0, 0, new Color(0x242423), 0, 220, new Color(0x090808));
		g2d.setPaint(gp3);
		g2d.fillRect(85, 0, 1, 220);
		g2d.fillRect(165, 0, 1, 220);

		g.setColor(new Color(0x090808));
		g.drawLine(85, 220, 165, 220);

		Image imageSetVolumeGrid = new ImageIcon("image/volumeGrid.png").getImage();
		g.drawImage(imageSetVolumeGrid, 95, 70, null);
		g.drawImage(imageSetVolumeGrid, 135, 70, null);

		Image imageCrossfinderGrid = new ImageIcon("image/crossfinderGrid.png").getImage();
		g.drawImage(imageCrossfinderGrid, 45, 230, null);

		Image imageVolumeGrid = new ImageIcon("image/displayVolumeGrid.png").getImage();
		g.drawImage(imageVolumeGrid, 30, 50, null);
		g.drawImage(imageVolumeGrid, 193, 50, null);
	}

	public void displayVolumeP1(int left, int right)
	{
		vueCrossfinderOver.displayVolumeP1(left, right);
	}

	public void displayVolumeP2(int left, int right)
	{
		vueCrossfinderOver.displayVolumeP1(left, right);
	}

	public void setVolumeP1(int value)
	{
		vueCrossfinderOver.setVolumeP1(value);
	}

	public void setVolumeP2(int value)
	{
		vueCrossfinderOver.setVolumeP2(value);
	}

	public void setCrossfinder(int value)
	{
		vueCrossfinderOver.setCrossfinder(value);
	}
}
