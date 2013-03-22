
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
	private Image play;
	private Image pause;
	private Image volumeUp;
	private Image volumeDown;
	private Image crossfinderLeft;
	private Image crossfinderRight;

	/**
	 * constructeur qui initialise le panel
	 * 
	 * @param Vue
	 */
	public VueKinect(Vue vue)
	{
		this.vue = vue;
		setPreferredSize(new Dimension(1000, 240));

		play = new ImageIcon(getClass().getResource("/image/kinectPlay.png")).getImage();
		pause = new ImageIcon(getClass().getResource("/image/kinectPause.png")).getImage();
		volumeUp = new ImageIcon(getClass().getResource("/image/kinectVolumeUp.png")).getImage();
		volumeDown = new ImageIcon(getClass().getResource("/image/kinectVolumeDown.png")).getImage();
		crossfinderLeft = new ImageIcon(getClass().getResource("/image/kinectCrossfinderLeft.png")).getImage();
		crossfinderRight = new ImageIcon(getClass().getResource("/image/kinectCrossfinderRight.png")).getImage();
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
		g.setColor(Color.BLACK);
		g.fillRect(336, 36, 310, 200);
		g.setColor(Color.WHITE);
		g.drawString("Main gauche", 450, 25);

		g.setColor(new Color(0x4e4c4b));
		g.drawRect(665, 35, 311, 201);
		g.setColor(Color.BLACK);
		g.fillRect(666, 36, 310, 200);
		g.setColor(Color.WHITE);
		g.drawString("Main droite", 780, 25);

		g.setFont(new Font("sansserif", Font.BOLD, 15));
		
		if(messageGauche == "play")
		{
			g.drawImage(play, 336, 36, null);
		}
		else if(messageGauche == "pause")
		{
			g.drawImage(pause, 336, 36, null);
		}
		else if(messageGauche == "volumeUp")
		{
			g.drawImage(volumeUp, 336, 36, null);
		}
		else if(messageGauche == "volumeDown")
		{
			g.drawImage(volumeDown, 336, 36, null);
		}
		else if(messageGauche == "crossfinderRight")
		{
			g.drawImage(crossfinderRight, 336, 36, null);
		}
		else if(messageGauche == "crossfinderLeft")
		{
			g.drawImage(crossfinderLeft, 336, 36, null);
		}

		if(messageDroite == "play")
		{
			g.drawImage(play, 666, 36, null);
		}
		else if(messageDroite == "pause")
		{
			g.drawImage(pause, 666, 36, null);
		}
		else if(messageDroite == "volumeUp")
		{
			g.drawImage(volumeUp, 666, 36, null);
		}
		else if(messageDroite == "volumeDown")
		{
			g.drawImage(volumeDown, 666, 36, null);
		}
		else if(messageDroite == "crossfinderRight")
		{
			g.drawImage(crossfinderRight, 666, 36, null);
		}
		else if(messageDroite == "crossfinderLeft")
		{
			g.drawImage(crossfinderLeft, 666, 36, null);
		}
	}

	/**
	 * setters de la BufferedImage qui sera appelé par l'objet Kinect
	 * 
	 * @param kinectImage
	 */
	public void setKinectImage(BufferedImage kinectImage)
	{
		this.kinectImage = kinectImage;

		repaint();
	}
}
