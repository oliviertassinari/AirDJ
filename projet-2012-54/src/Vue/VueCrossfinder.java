
package Vue;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import Modele.ModeleCrossfinder;

public class VueCrossfinder extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 507864433965915931L;
	/**
	 * Vue contenant le panel
	 */
	private Vue vue;
	/**
	 * image d'arrière plan
	 */
	private BufferedImage imageBackground;
	/**
	 * image du curseur de volume
	 */
	private Image imageVolumeCursor;
	/**
	 * image du curseur crossfinder
	 */
	private Image imageCrossfinderCursor;
	/**
	 * image d'arrière plan de l'affichage du volume
	 */
	private Image imageDisplayVolumeOver;
	
	private Image imageEQ1;
	private Image imageEQ2;
	private Image imageEQ3;

	/**
	* constructeur
	* @param La vue contenante
	*/
	public VueCrossfinder(Vue vue)
	{
		this.vue = vue;

		imageVolumeCursor = new ImageIcon(getClass().getResource("/image/volumeCursor.png")).getImage();
		imageCrossfinderCursor = new ImageIcon(getClass().getResource("/image/crossfinderCursor.png")).getImage();
		imageDisplayVolumeOver = new ImageIcon(getClass().getResource("/image/displayVolumeOver.png")).getImage();

		imageEQ1 = new ImageIcon(getClass().getResource("/image/vueEQ1.png")).getImage();
		imageEQ2 = new ImageIcon(getClass().getResource("/image/vueEQ2.png")).getImage();
		imageEQ3 = new ImageIcon(getClass().getResource("/image/vueEQ3.png")).getImage();

		imageBackground = getImageBackground();
	}

	/**
	 * 
	 */
	public BufferedImage getImageBackground()
	{
		BufferedImage image = new BufferedImage(250, 300, BufferedImage.TRANSLUCENT);
		Graphics g = image.getGraphics();
		g.setColor(new Color(0x181613));
		g.fillRect(0, 0, 250, 300);

		Graphics2D g2d = (Graphics2D)g;

		// Shadow
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		AlphaComposite ac = null;
		g2d.setColor(Color.BLACK);
		int sw = 250 - 2 * 7;
		int sh = 300 - 2 * 7;
		for(int i = 0; i < 7; ++i)
		{
			ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f / (i + 1));
			g2d.setComposite(ac);
			g2d.fillRoundRect(7 - i, 7 - i, sw + 2 * i, sh + 2 * i, i * 2, i * 2);
		}
		ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
		g2d.setComposite(ac);

		// Border
		g.setColor(new Color(0x42413d));
		g.drawLine(0, 0, 0, 292);
		g.drawLine(244, 0, 244, 292);
		g.drawLine(0, 292, 244, 292);
		g.drawLine(1, 0, 244, 0);

		// Background
		GradientPaint gp1 = new GradientPaint(0, 0, new Color(0x383839), 0, 300, new Color(0x171718));
		g2d.setPaint(gp1);
		g2d.fillRect(1, 1, 243, 291);

		GradientPaint gp2 = new GradientPaint(0, 0, new Color(0x272726), 0, 220, new Color(0x0a0909));
		g2d.setPaint(gp2);
		g2d.fillRect(85, 1, 80, 219);

		GradientPaint gp3 = new GradientPaint(0, 0, new Color(0x242423), 0, 220, new Color(0x090808));
		g2d.setPaint(gp3);
		g2d.fillRect(85, 1, 1, 219);
		g2d.fillRect(165, 1, 1, 219);

		g.setColor(new Color(0x090808));
		g.drawLine(85, 220, 165, 220);

		// Texte
		g.setFont(new Font("sansserif", Font.PLAIN, 8));
		g.setColor(new Color(0xaaaaaa));
		g.drawString("VOLUME", 88, 63);
		g.drawString("VOLUME", 128, 63);

		g.setFont(new Font("sansserif", Font.PLAIN, 10));
		g.drawString("Mid", 18, 97);
		g.drawString("Bass", 15, 156);
	
		g.drawString("Mid", 213, 97);
		g.drawString("Bass", 210, 156);

		// Images
		Image imageSetVolumeGrid = new ImageIcon(getClass().getResource("/image/volumeGrid.png")).getImage();
		g.drawImage(imageSetVolumeGrid, 95, 70, null);
		g.drawImage(imageSetVolumeGrid, 135, 70, null);

		Image imageCrossfinderGrid = new ImageIcon(getClass().getResource("/image/crossfinderGrid.png")).getImage();
		g.drawImage(imageCrossfinderGrid, 45, 230, null);

		Image imageVolumeGrid = new ImageIcon(getClass().getResource("/image/displayVolumeGrid.png")).getImage();
		g.drawImage(imageVolumeGrid, 50, 50, null);
		g.drawImage(imageVolumeGrid, 173, 50, null);

		g.dispose();

		return image;
	}

	/**
	 * paintComponent
	 */
	protected void paintComponent(Graphics g)
	{
		ModeleCrossfinder modeleCrossfinder = vue.getModele().getModeleCrossfinder();

		int crossfinder = modeleCrossfinder.getCrossfinder();
		int volumeP1 = modeleCrossfinder.getVolumeP1();
		int volumeP2 = modeleCrossfinder.getVolumeP2();
		int[] displayVolumeP1 = modeleCrossfinder.getDisplayVolumeP1();
		int[] displayVolumeP2 = modeleCrossfinder.getDisplayVolumeP2();
		int[] equalizerP1 = modeleCrossfinder.getEqualizerP1();
		int[] equalizerP2 = modeleCrossfinder.getEqualizerP2();

		g.drawImage(imageBackground, 0, 0, this);

		// volumeCursor
		g.drawImage(imageVolumeCursor, 94, (int)(148 - volumeP1 * 0.78), null);
		g.drawImage(imageVolumeCursor, 134, (int)(148 - volumeP2 * 0.78), null);

		g.drawImage(imageCrossfinderCursor, (int)(116 + crossfinder * 0.5), 238, null);

		// displayVolume
		g.setColor(new Color(0x28acff));
		g.fillRect(54, 53 + 120 - (int)(displayVolumeP1[0] * 1.2 / 100 * modeleCrossfinder.getVolumeP1() * modeleCrossfinder.getCoefVolumeP1()), 9, (int)(displayVolumeP1[0] * 1.2 / 100 * modeleCrossfinder.getCoefVolumeP1() * modeleCrossfinder.getVolumeP1()));
		g.fillRect(64, 53 + 120 - (int)(displayVolumeP1[1] * 1.2 / 100 * modeleCrossfinder.getVolumeP1() * modeleCrossfinder.getCoefVolumeP1()), 9, (int)(displayVolumeP1[1] * 1.2 / 100 * modeleCrossfinder.getCoefVolumeP1() * modeleCrossfinder.getVolumeP1()));

		g.setColor(new Color(0xff171a));
		g.fillRect(177, 53 + 120 - (int)(displayVolumeP2[0] * 1.2 / 100 * modeleCrossfinder.getVolumeP2() * modeleCrossfinder.getCoefVolumeP2()), 9, (int)(displayVolumeP2[0] * 1.2 / 100 * modeleCrossfinder.getCoefVolumeP2() * modeleCrossfinder.getVolumeP2()));
		g.fillRect(187, 53 + 120 - (int)(displayVolumeP2[1] * 1.2 / 100 * modeleCrossfinder.getVolumeP2() * modeleCrossfinder.getCoefVolumeP2()), 9, (int)(displayVolumeP2[1] * 1.2 / 100 * modeleCrossfinder.getCoefVolumeP2() * modeleCrossfinder.getVolumeP2()));

		g.drawImage(imageDisplayVolumeOver, 54, 53, null);
		g.drawImage(imageDisplayVolumeOver, 177, 53, null);

		// Bouton Equalizer
		g.drawImage(getImageEQ(equalizerP1[2], equalizerP1[0], 0), 7, 50, null);
		g.drawImage(getImageEQ(equalizerP1[3], equalizerP1[1], 0), 7, 110, null);

		g.drawImage(getImageEQ(equalizerP2[2], equalizerP2[0], 1), 203, 50, null);
		g.drawImage(getImageEQ(equalizerP2[3], equalizerP2[1], 1), 203, 110, null);
	}

	/**
	 * 
	 * @param state 0 : normal / 1 : hover / 2 : press
	 * @param value entre -50 et 50
	 * @param color 0 : blue / 1 : red
	 * @return
	 */
	public BufferedImage getImageEQ(int state, int value, int color)
	{
		BufferedImage imageEQ = new BufferedImage(38, 38, BufferedImage.TRANSLUCENT);
		Graphics g = imageEQ.getGraphics();
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(new Color(0x2F2F30));
		g.fillArc(0, 2, 37, 37, 0, 380);

		if(color == 0)
		{
			g.setColor(new Color(0x28ACFF));
		}
		else
		{
			g.setColor(new Color(0xFF171A));
		}

		g.fillArc(0, 2, 37, 37, 90, -(value*140)/50);

		g.drawImage(imageEQ2, 0, 0, null);

		if(state == 0 || state == 1)
		{
			g.drawImage(imageEQ1, 7, 9, 7+24, 9+24, state*24, 0, 24 + state*24, 24, null);
		}
		else if(state == 2)
		{
			if(color == 0)
			{
				g.drawImage(imageEQ1, 7, 9, 7+24, 9+24, 2*24, 0, 24 + 2*24, 24, null);
			}
			else
			{
				g.drawImage(imageEQ1, 7, 9, 7+24, 9+24, 3*24, 0, 24 + 3*24, 24, null);
			}
		}

		g2d.rotate(Math.toRadians((value*140)/50), 19, 21);

		g.drawImage(imageEQ3, 7, 9, 7+24, 9+24, 0, 0, 24, 24, null);

		return imageEQ;
	}
}
