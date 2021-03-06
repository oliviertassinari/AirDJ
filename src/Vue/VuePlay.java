
package Vue;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import Modele.ModelePlay;

/**
 * Panel affichant l'état de lecture pour chaque piste
 * 
 */
public class VuePlay extends JPanel
{
	private static final long serialVersionUID = 328034180530039435L;
	private Vue vue;
	private ModelePlay modelePlay;
	private VueSpectre vueSpectre;

	private BufferedImage imageBackground;
	private Image imagePitchCursor;
	private Image imageBPM;
	private Image imageButton;

	/**
	 * constructeur
	 * 
	 * @param couleur 0 = blue, 1 = red
	 * @param vue
	 * @param modelePlay
	 */

	public VuePlay(int couleur, Vue vue, ModelePlay modelePlay)
	{
		this.vue = vue;
		this.modelePlay = modelePlay;
		setPreferredSize(new Dimension((1000 - 250) / 2, 300));

		imagePitchCursor = new ImageIcon(getClass().getResource("/image/pitchCursor.png")).getImage();
		imageBPM = new ImageIcon(getClass().getResource("/image/bpm.png")).getImage();

		if(couleur == 0)
		{
			imageButton = new ImageIcon(getClass().getResource("/image/buttonBlue.png")).getImage();
		}
		else
		{
			imageButton = new ImageIcon(getClass().getResource("/image/buttonRed.png")).getImage();
		}

		vueSpectre = new VueSpectre(couleur);

		imageBackground = getImageBackground();
	}

	public BufferedImage getImageBackground()
	{
		BufferedImage image = new BufferedImage(375, 300, BufferedImage.TRANSLUCENT);
		Graphics g = image.getGraphics();
		g.setColor(new Color(0x181613));
		g.fillRect(0, 0, 375, 300);

		Graphics2D g2d = (Graphics2D)g;

		// Shadow
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		AlphaComposite ac = null;
		g2d.setColor(Color.BLACK);
		int sw = 375 - 2 * 7;
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
		g.drawLine(369, 0, 369, 292);
		g.drawLine(0, 292, 369, 292);
		g.drawLine(0, 0, 369, 0);

		// Background
		GradientPaint gp1 = new GradientPaint(0, 0, new Color(0x383839), 0, 300, new Color(0x171718));
		g2d.setPaint(gp1);
		g2d.fillRect(1, 1, 368, 291);

		// Texte
		g.setFont(new Font("sansserif", Font.BOLD, 12));
		g.setColor(new Color(0x908F8D));
		g.drawString("ELAPSED", 8, 60);
		g.drawString("REMAIN", 140, 60);
		g.drawString("PITCH", 8, 72);
	
		g.setFont(new Font("sansserif", Font.PLAIN, 8));
		g.setColor(new Color(0xaaaaaa));
		g.drawString("PITCH", 303, 267);

		// Images
		Image imagePitchGrid = new ImageIcon(getClass().getResource("/image/pitchGrid.png")).getImage();
		g.drawImage(imagePitchGrid, 300, 120, null);

		Image imagePlayButton = new ImageIcon(getClass().getResource("/image/button.png")).getImage();
		g.drawImage(imagePlayButton, 120, 170, null);
		
		g.dispose();

		return image;
	}

	protected void paintComponent(Graphics g)
	{
		int pitch = modelePlay.getPitch();
		String title = modelePlay.getTitle();
		String artist = modelePlay.getArtist();
		double bpm = modelePlay.getBpm();
		int total = modelePlay.getTotal();
		int current = modelePlay.getCurrent();
		int buttonPlay = modelePlay.getButtonPlay();
		int buttonPause = modelePlay.getButtonPause();
		int buttonBPM = modelePlay.getButtonBPM();

		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.drawImage(imageBackground, 0, 0, this);

		g.drawImage(imagePitchCursor, 303, (int)(181 + (0.57 * pitch)), null);

		// Texte
		g.setFont(new Font("sansserif", Font.BOLD, 17));
		g.setColor(Color.WHITE);

		if(artist.length() > 30)
		{
			artist = artist.substring(0, 29) + "...";
		}
		g.drawString(artist, 8, 23);

		if(bpm == -1)
		{
			g.setFont(new Font("sansserif", Font.BOLD, 18));
			g.drawString("......", 290, 30);
		}
		else if(bpm != 0)
		{
			g.setFont(new Font("sansserif", Font.BOLD, 18));
			g.drawString(String.valueOf(getFormatBPM(bpm * (1 + pitch/100f))), 270, 30);
		}

		// Button BPM
		g.drawImage(imageBPM, 330, 17, 330 + 31, 17 + 15, 0, 15 * buttonBPM, 31, 15 * (1 + buttonBPM), null);

		g.setFont(new Font("sansserif", Font.BOLD, 15));
		g.setColor(new Color(0xD1D3BA));
		if(title.length() > 30)
		{
			title = title.substring(0, 29) + "...";
		}
		g.drawString(title, 8, 40);

		g.setFont(new Font("sansserif", Font.BOLD, 12));
		g.setColor(new Color(0x908F8D));
		g.drawString(String.valueOf(pitch), 48, 72);

		if(total != 0)
		{
			g.drawString(getFormatMMSS(current), 68, 60);
			g.drawString(getFormatMMSS(total - current), 190, 60);
		}

		BufferedImage imageSpectre = vueSpectre.get(current, total, modelePlay.getPlayer());
		g.drawImage(imageSpectre, 1, 80, this);

		// Button
		if(buttonPause == 1) // over
		{
			g.drawImage(imageButton, 124, 174, 124 + 36, 174 + 23, 0, 0, 36, 23, null);
		}
		else if(buttonPause == 2) // on
		{
			g.drawImage(imageButton, 124, 174, 124 + 36, 174 + 23, 0, 23, 36, 46, null);
		}
		else if(buttonPause == 3) // press
		{
			g.drawImage(imageButton, 124, 174, 124 + 36, 174 + 23, 0, 46, 36, 69, null);
		}

		if(buttonPlay == 1) // over
		{
			g.drawImage(imageButton, 162, 174, 162 + 39, 174 + 23, 36, 0, 75, 23, null);
		}
		else if(buttonPlay == 2) // on
		{
			g.drawImage(imageButton, 162, 174, 162 + 39, 174 + 23, 36, 23, 75, 46, null);
		}
		else if(buttonPlay == 3) // press
		{
			g.drawImage(imageButton, 162, 174, 162 + 39, 174 + 23, 36, 46, 75, 69, null);
		}
	}

	/**
	 * 
	 * @param value
	 * @return String
	 */
	public String getFormatMMSS(int value)
	{
		int ms = value % 10;
		int secondes = (value - ms) / 10;
		int seconde = secondes % 60;
		int minute = (secondes - seconde) / 60;

		String secondeSting = String.valueOf(seconde);
		String minuteSting = String.valueOf(minute);

		if(seconde < 10)
		{
			secondeSting = "0" + String.valueOf(seconde);
		}

		if(minute < 10)
		{
			minuteSting = "0" + String.valueOf(minute);
		}

		return minuteSting + ":" + secondeSting + "." + ms;
	}

	private double getFormatBPM(double value)
	{
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);

		Number n = null;
		NumberFormat formater3 = new DecimalFormat(".##");
		String str = formater3.format(value);
		try
		{
			n = formater3.parse(str);
		}
		catch(ParseException pe)
		{
			System.out.println("parse : " + pe);
		}

		return n.doubleValue();
	}
}