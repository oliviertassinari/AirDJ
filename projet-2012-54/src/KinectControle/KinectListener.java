
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
		/*if(modeleKinect.getMessageDroite() == "crossfinder")
		{
			modeleKinect.setMessageDroite(null);
			modeleKinect.setMessageGauche(null);
		}*/
		// commande play/pause
		if(event.getMessage() == "play")
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

		if(event.getMessage() == "volume")
		{
			ModeleCrossfinder crossfinder = event.getSource().getModele().getModeleCrossfinder();
			if(event.getCote() == "left")
			{
				crossfinder.setVolumeP1(crossfinder.getVolumeP1() + event.getValeur());
				modeleKinect.setMessageGauche("volume");
				
				modeleKinect.setTimerGauche();
			}
			else if(event.getCote() == "right")
			{
				crossfinder.setVolumeP2(crossfinder.getVolumeP2() + event.getValeur());
				modeleKinect.setMessageDroite("volume");
			
				modeleKinect.setTimerDroite();
			}
		}

		if(event.getMessage() == "crossfinder")
		{
			ModeleCrossfinder crossfinder = event.getSource().getModele().getModeleCrossfinder();
			if(event.getCote() == "left")
			{
				crossfinder.setCrossfinder(crossfinder.getCrossfinder() + event.getValeur());
				modeleKinect.setMessageGauche("crossfinder");
		
				modeleKinect.setTimerGauche();
			}
			else if(event.getCote() == "right")
			{
				crossfinder.setCrossfinder(crossfinder.getCrossfinder() + event.getValeur());
				modeleKinect.setMessageDroite("crossfinder");
				
				modeleKinect.setTimerDroite();
			}
		}
		/*if(event.getMessage() == "crossfinder")
		{
			ModeleCrossfinder crossfinder = event.getSource().getModele().getModeleCrossfinder();
			crossfinder.setCrossfinder(crossfinder.getCrossfinder() + event.getValeur());
			modeleKinect.setMessageDroite("crossfinder");
			modeleKinect.setMessageGauche("crossfinder");
		}*/

	}
}
