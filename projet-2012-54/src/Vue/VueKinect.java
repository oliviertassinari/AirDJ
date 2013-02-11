package Vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class VueKinect extends JPanel
/**
 *Panel devant afficher ce qui est filmé
 *prend en attribut la vue qui le contient
 */
{ 
	private Vue vue;
	private BufferedImage kinectImage;
	
	
	
	

     /**
	 * constructeur qui initialise le panel
	 * @param Vue
	 */
	public VueKinect(Vue vue)
	
	{
		this.vue = vue;

		setBackground(Color.black);
		setPreferredSize(new Dimension(1000, 240));
	}

	/**
	 * 
	 */
	protected void paintComponent(Graphics g)
	{
		
		String messageDroite=vue.getModele().getModeleKinect().getMessageDroite();
		String messageGauche=vue.getModele().getModeleKinect().getMessageGauche();
		g.setColor(Color.black);
		g.fillRect(0, 0, 1000, 240);
		g.drawImage(kinectImage,320,240, null);
		
		
		g.setFont(new Font("sansserif", Font.BOLD, 17));
		g.setColor(Color.WHITE);
		g.drawRect(335, 5, 320, 230);
		g.drawRect(665, 5, 320, 230);
		g.drawString("Main gauche", 450, 25);
		g.drawString("Main droite", 780, 25);
		
		g.setFont(new Font("sansserif", Font.BOLD, 15));
		g.drawString(messageGauche, 450, 100);
		g.drawString(messageDroite, 780, 100);
		
		
	}

}
