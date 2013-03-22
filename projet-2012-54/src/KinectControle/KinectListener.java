
package KinectControle;

import Modele.ModeleCrossfinder;
import Modele.ModeleKinect;
import Modele.ModelePlay;

/**
 * Classe Listener des objets KinectEvent
 */
public class KinectListener implements ListenerInterface
{
	/**
	 * Méthode définissant la réaction à un KinectEvent. La réaction dépend de
	 * la commande envoyée par la partie kinect. Il y a alors mise à jour du
	 * Modele
	 */
	public void ListenToKinect(KinectEvent event)
	{
		ModeleKinect modeleKinect = event.getSource().getModele().getModeleKinect();

		if(event.getMessage() == "playpause")
		{
			ModelePlay modelePlay;

			if(event.getCote() == "left")
			{
				modelePlay = event.getSource().getModele().getModelePlayP1();
				modeleKinect.setTimerGauche();
			}
			else
			{
				modelePlay = event.getSource().getModele().getModelePlayP2();
				modeleKinect.setTimerDroite();
			}

			if(modelePlay.getState() == 0 && modelePlay.getPlayer() != null)
			{
				modelePlay.setPlay();
				modelePlay.setButtonPlay("release");
				modelePlay.setButtonPause("out");

				if(event.getCote() == "left")
				{
					modeleKinect.setMessageGauche("play");
					modeleKinect.setTimerGauche();
				}
				else if(event.getCote() == "right")
				{
					modeleKinect.setMessageDroite("play");
					modeleKinect.setTimerDroite();
		
				}
			}
			else if(modelePlay.getState() == 1)
			{
				modelePlay.setPause();
				modelePlay.setButtonPlay("out");
				modelePlay.setButtonPause("release");

				if(event.getCote() == "left")
				{
					modeleKinect.setMessageGauche("pause");
				
					modeleKinect.setTimerGauche();
				}
				else if(event.getCote() == "right")
				{
					modeleKinect.setMessageDroite("pause");
					
					modeleKinect.setTimerDroite();
				}
			}
		}
		else if(event.getMessage() == "volume")
		{
			ModeleCrossfinder crossfinder = event.getSource().getModele().getModeleCrossfinder();

			if(event.getCote() == "left")
			{
				crossfinder.setVolumeP1(crossfinder.getVolumeP1() + event.getValeur());
				
				if(event.getValeur() > 0)
				{
					modeleKinect.setMessageGauche("volumeUp");
				}
				else
				{
					modeleKinect.setMessageGauche("volumeDown");
				}
				
				modeleKinect.setTimerGauche();
			}
			else if(event.getCote() == "right")
			{
				crossfinder.setVolumeP2(crossfinder.getVolumeP2() + event.getValeur());

				if(event.getValeur() > 0)
				{
					modeleKinect.setMessageDroite("volumeUp");
				}
				else
				{
					modeleKinect.setMessageDroite("volumeDown");
				}

				modeleKinect.setTimerDroite();
			}
		}
		else if(event.getMessage() == "crossfinder")
		{
			ModeleCrossfinder crossfinder = event.getSource().getModele().getModeleCrossfinder();
	
			if(event.getCote() == "left")
			{
				crossfinder.setCrossfinder(crossfinder.getCrossfinder() + event.getValeur());

				if(event.getValeur() < 0)
				{
					modeleKinect.setMessageGauche("crossfinderLeft");
				}
				else
				{
					modeleKinect.setMessageGauche("crossfinderRight");
				}

				modeleKinect.setTimerGauche();
			}
			else if(event.getCote() == "right")
			{
				crossfinder.setCrossfinder(crossfinder.getCrossfinder() + event.getValeur());

				if(event.getValeur() < 0)
				{
					modeleKinect.setMessageDroite("crossfinderLeft");
				}
				else
				{
					modeleKinect.setMessageDroite("crossfinderRight");
				}

				modeleKinect.setTimerDroite();
			}
		}

	}
}
