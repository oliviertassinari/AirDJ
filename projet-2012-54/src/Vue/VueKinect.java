
package Vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panel devant afficher ce qui est filmé prend en attribut : - la vue qui le
 * contient - une BufferedImage qui est mise à jour périodiquement par la
 * partie kinect pour afficher la video - 4 images pour décrire les commandes
 * détectées par la partie kinect
 */
public class VueKinect extends JPanel
{
	private Vue vue;
	private BufferedImage kinectImage;
	private BufferedImage kinectImageLeft;
	private BufferedImage kinectImageRight;
	private Image play;
	private Image pause;
	private Image volume;
	private Image crossfinder;

	/**
	 * constructeur qui initialise le panel
	 * 
	 * @param Vue
	 */
	public VueKinect(Vue vue)
	{
		this.vue = vue;
		setPreferredSize(new Dimension(1000, 240));

		play = new ImageIcon(getClass().getResource("/image/playvuekinect.png")).getImage();
		pause = new ImageIcon(getClass().getResource("/image/pausevuekinect.png")).getImage();
		volume = new ImageIcon(getClass().getResource("/image/volumevuekinect.png")).getImage();
		crossfinder = new ImageIcon(getClass().getResource("/image/crossfindervuekinect.png")).getImage();
	}

	protected void paintComponent(Graphics g)
	{
		String messageDroite = vue.getModele().getModeleKinect().getMessageDroite();
		String messageGauche = vue.getModele().getModeleKinect().getMessageGauche();
		g.setColor(new Color(0x181613));
		g.fillRect(0, 0, 1000, 240);

		g.drawImage(kinectImage, 0, 0, null);

		g.setFont(new Font("sansserif", Font.BOLD, 17));

		g.setColor(new Color(0x4e4c4b));
		g.drawRect(335, 35, 311, 201);
		g.drawImage(kinectImageLeft, 336, 36, null);
		g.setColor(Color.WHITE);
		g.drawString("Main gauche", 450, 25);

		g.setColor(new Color(0x4e4c4b));
		g.drawRect(665, 35, 311, 201);
		g.drawImage(kinectImageRight, 666, 36, null);
		g.setColor(Color.WHITE);
		g.drawString("Main droite", 780, 25);

		g.setFont(new Font("sansserif", Font.BOLD, 15));

		if(messageDroite == "play")
		{
			g.drawImage(play, 730, 55, null);
		}
		if(messageDroite == "pause")
		{
			g.drawImage(pause, 730, 55, null);
		}
		if(messageDroite == "volume")
		{
			g.drawImage(volume, 740, 55, null);
		}
		if(messageDroite == "crossfinder")
		{
			g.drawImage(crossfinder, 745, 55, null);
		}
		else if(messageDroite == null)
		{
			g.drawImage(null, 740, 55, new Color(0x4e4c4b), null);
		}

		if(messageGauche == "play")
		{
			g.drawImage(play, 405, 55, null);
		}
		if(messageGauche == "pause")
		{
			g.drawImage(pause, 405, 55, null);
		}
		if(messageGauche == "volume")
		{
			g.drawImage(volume, 415, 55, null);
		}
		if(messageGauche == "crossfinder")
		{
			g.drawImage(crossfinder, 420, 55, null);
		}
		else if(messageGauche == null)
		{
			g.drawImage(null, 420, 55, new Color(0x4e4c4b), null);
		}
	}

	/**
	 * setters de la BufferedImage qui sera appelé par l'objet Kinect
	 * 
	 * @param kinectImage
	 */
	public void setKinectImage(BufferedImage kinectImage, BufferedImage kinectImageLeft, BufferedImage kinectImageRight)
	{
		this.kinectImage = kinectImage;
		this.kinectImageLeft = kinectImageLeft;
		this.kinectImageRight = kinectImageRight;

		repaint();
	}
}
