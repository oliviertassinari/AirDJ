package Vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class VueKinect extends JPanel
/**
 *Panel devant afficher ce qui est filmé
 *prend en attribut la vue qui le contient
 */
{ 
	private Vue vue;

     /**
	 * constructeur qui initialise le panel
	 * @param Vue
	 */
	public VueKinect(Vue vue)
	
	{
		this.vue = vue;

		setBackground(Color.RED);
		setPreferredSize(new Dimension(1000, 200));
	}

	/**
	 * 
	 */
	protected void paintComponent(Graphics g)
	{
		
	}
}
