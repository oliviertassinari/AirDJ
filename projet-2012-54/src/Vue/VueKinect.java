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
 * Panel devant afficher ce qui est filmé 
 * prend en attribut : 
 * - la vue qui le contient 
 * - une BufferedImage qui est mise à jour périodiquement par la partie kinect pour afficher la video 
 * - 4 images pour décrire les commandes détectées par la partie kinect
 */
public class VueKinect extends JPanel {
	private Vue vue;
	private BufferedImage kinectImage;
	private Image play;
	private Image pause;
	private Image volume;
	private Image crossfinder;

	/**
	 * constructeur qui initialise le panel
	 * 
	 * @param Vue
	 */
	public VueKinect(Vue vue) {
		this.vue = vue;

		setBackground(Color.black);
		setPreferredSize(new Dimension(1000, 240));
	}

	protected void paintComponent(Graphics g) {
		g.drawImage(kinectImage, 10, 10, null);

		play = new ImageIcon("image/playvuekinect.png").getImage();
		pause = new ImageIcon("image/pausevuekinect.png").getImage();
		volume = new ImageIcon("image/volumevuekinect.png").getImage();
		crossfinder = new ImageIcon("image/crossfindervuekinect.png")
				.getImage();

		String messageDroite = vue.getModele().getModeleKinect()
				.getMessageDroite();
		String messageGauche = vue.getModele().getModeleKinect()
				.getMessageGauche();
		g.setColor(Color.black);
		g.fillRect(0, 0, 1000, 240);
		g.drawImage(kinectImage, 320, 240, null);

		g.setFont(new Font("sansserif", Font.BOLD, 17));
		g.setColor(Color.WHITE);
		g.drawRect(335, 5, 320, 230);
		g.drawRect(665, 5, 320, 230);
		g.drawString("Main gauche", 450, 25);
		g.drawString("Main droite", 780, 25);

		g.setFont(new Font("sansserif", Font.BOLD, 15));
		if (messageDroite == "play") {
			g.drawImage(play, 730, 55, null);
		}
		if (messageDroite == "pause") {
			g.drawImage(pause, 730, 55, null);
		}
		if (messageDroite == "volume") {
			g.drawImage(volume, 740, 55, null);
		} else if (messageDroite == "crossfinder") {
			g.drawImage(crossfinder, 745, 55, null);
		}

		if (messageGauche == "play") {
			g.drawImage(play, 405, 55, null);
		}
		if (messageGauche == "pause") {
			g.drawImage(pause, 405, 55, null);
		}
		if (messageGauche == "volume") {
			g.drawImage(volume, 415, 55, null);
		} else if (messageGauche == "crossfinder") {
			g.drawImage(crossfinder, 420, 55, null);
		}

	}

	/**
	 * setters de la BufferedImage qui sera appelé par l'objet Kinect
	 * @param kinectImage
	 */
	public void setKinectImage(BufferedImage kinectImage) {
		this.kinectImage = kinectImage;
		repaint();
	}
}
