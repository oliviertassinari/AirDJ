
package Modele;

import java.util.Timer;
import java.util.TimerTask;

public class ModeleKinect
{
	/**
	 * Partie du Modele controlée par le package kinectControle.
	 */
	private Modele modele;
	private String messageDroite = "";
	private String messageGauche = "";
	private Timer timerLeft;
	private Timer timerRight;

	public ModeleKinect(Modele modele)
	{
		this.modele = modele;
	}

	/**
	 * 
	 * @param messageDroite
	 */
	public void setMessageDroite(String messageDroite)
	{
		this.messageDroite = messageDroite;

		modele.getVue().getVueKinect().repaint();

		if(messageDroite != "")
		{
			if(timerRight != null)
			{
				timerRight.cancel();
			}
			timerRight = new Timer();
			timerRight.schedule(new TimerTask()
			{
				public void run()
				{
					setMessageDroite("");
					cancel();
				}
			}, 2000);
		}
	}

	/**
	 * 
	 * @param messageGauche
	 */
	public void setMessageGauche(String messageGauche)
	{
		this.messageGauche = messageGauche;

		modele.getVue().getVueKinect().repaint();
		
		if(messageGauche == "")
		{
			if(timerLeft != null)
			{
				timerLeft.cancel();
			}
			
			timerLeft = new Timer();
			timerLeft.schedule(new TimerTask()
			{
				public void run()
				{
					setMessageGauche("");
					cancel();
				}
			}, 2000);			
		}
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
