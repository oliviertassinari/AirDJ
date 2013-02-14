
package Modele;

import java.util.Timer;
import java.util.TimerTask;

public class ModeleKinect
{
	/**
	 * Partie du Modele controlée par le package kinectControle.
	 */
	private Modele modele;
	private String messageDroite = "commande";
	private String messageGauche = "commande";

	public ModeleKinect(Modele modele)
	{
		this.modele = modele;
	}
	
	public void setTimerDroite()
	{
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{
			public void run()
			{
				setMessageDroite(null);
				cancel();
			}
		}, 2000);
	}
	
	public void setTimerGauche()
	{
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{
			public void run()
			{
				setMessageGauche(null);
				cancel();
			}
		}, 2000);
	}

	/**
	 * 
	 * @param messageDroite
	 */
	public void setMessageDroite(String messageDroite)
	{
		this.messageDroite = messageDroite;
		modele.getVue().getVueKinect().repaint();
	}

	/**
	 * 
	 * @param messageGauche
	 */
	public void setMessageGauche(String messageGauche)
	{
		this.messageGauche = messageGauche;
		modele.getVue().getVueKinect().repaint();
	}

	/**
	 * 
	 * @return String
	 */
	public String getMessageDroite()
	{
		return messageDroite;
	}

	/**
	 * 
	 * @return String
	 */
	public String getMessageGauche()
	{
		return messageGauche;
	}
}
