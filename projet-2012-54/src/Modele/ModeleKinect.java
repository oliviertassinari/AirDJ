
package Modele;

import java.util.Timer;
import java.util.TimerTask;

public class ModeleKinect
{
	/**
	 * Partie du Modele controlée par le package kinectControle.
	 */
	private Modele modele;
	String messageDroite = "commande";
	String messageGauche = "commande";

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
		}, 500, 10);
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
		}, 500, 10);
		
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
