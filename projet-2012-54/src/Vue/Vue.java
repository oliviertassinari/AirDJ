package Vue;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Modele.Modele;

public class Vue extends JFrame
{
	private static final long serialVersionUID = 3589596133770161700L;
	private VueBrowser vueBrowser;
	private VueCrossfinder vueCrossfinder;
	private VueKinect vueKinect;
	private VuePlay vuePlayP1;
	private VuePlay vuePlayP2;
	private Modele modele;

	/**
	 * constructeur vue (toute la fenetre)
	 * @param modele
	 */
	public Vue(Modele modele)
	{
		this.modele = modele;

		setTitle("AirDJ");
		setSize(1000 +8 + 17, 740+4  + 14);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon("image/icon.png").getImage());


		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createMatteBorder(7, 7, 7, 2, new Color(0x181613)));
		getContentPane().add(panel);	

		vueBrowser = new VueBrowser(this);
		vueCrossfinder = new VueCrossfinder(this);
		vueKinect = new VueKinect(this);
		vuePlayP1 = new VuePlay(0, this, modele.getModelePlayP1()); //blue
		vuePlayP2 = new VuePlay(1, this, modele.getModelePlayP2()); //red

		panel.add(vueBrowser, BorderLayout.NORTH);
		panel.add(vueCrossfinder, BorderLayout.CENTER);
		panel.add(vueKinect, BorderLayout.SOUTH);
		panel.add(vuePlayP1, BorderLayout.WEST);
		panel.add(vuePlayP2, BorderLayout.EAST);

		setVisible(true);
	}

	/**
	 * getter vueBrowser (tout le haut : arbre de recherche et fichiers wav)
	 * @return vueBrowser
	 */
	public VueBrowser getVueBrowser()
	{
		return vueBrowser;
	}

	/**
	 * getter vueCrossfinder (au milieu : pour passer d'une piste à l'autre)
	 * @return vueCrossfinder
	 */
	public VueCrossfinder getVueCrossfinder()
	{
		return vueCrossfinder;
	}

	/**
	 * getter vueKinect (video en bas de l'ecran)
	 * @return
	 */
	public VueKinect getVueKinect()
	{
		return vueKinect;
	}

	/**
	 * getter vuePlayP1 (toute piste 1 sauf volume)
	 * @return vuePlayP1
	 */
	public VuePlay getVuePlayP1()
	{
		return vuePlayP1;
	}

	/**
	 * getter vuePlayP2 (toute piste 2 sauf volume)
	 * @return vuePlayP2
	 */
	public VuePlay getVuePlayP2()
	{
		return vuePlayP2;
	}

	/**
	 * getter modele ??
	 * @return modele
	 */
	public Modele getModele()
	{
		return modele;
	}
}