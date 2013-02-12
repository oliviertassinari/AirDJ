package KinectControle;

import Modele.ModeleCrossfinder;
import Modele.ModeleKinect;
import Modele.ModelePlay;

public class KinectListener implements ListenerInterface
{
	/**
	 * 
	 */
	@Override
	public void ListenToKinect(KinectEvent event)
	{		
		ModeleKinect modeleKinect = event.getSource().getModele().getModeleKinect();
		if(modeleKinect.getMessageDroite()=="crossfinder") {
			modeleKinect.setMessageDroite(null);
			modeleKinect.setMessageGauche(null);
		}
		// commande play/pause
		if(event.getMessage() == "play")
		{
			ModelePlay modelePlay;
			
			if(event.getCote() == "left")
			{
				modelePlay = event.getSource().getModele().getModelePlayP1();
			}
			else 
			{
				modelePlay = event.getSource().getModele().getModelePlayP2();
			}
			
			if(modelePlay.getState() == 0)
			{
				modelePlay.setPlay();
				modelePlay.setButtonPlay("release");
				modelePlay.setButtonPause("out");
				
				if ( event.getCote()=="left"){
					modeleKinect.setMessageGauche("play");
				}
				else if ( event.getCote()=="right"){
					modeleKinect.setMessageDroite("play");
				}
			}
			else if(modelePlay.getState() == 1)
			{
				modelePlay.setPause();
				modelePlay.setButtonPlay("out");
				modelePlay.setButtonPause("release");
				
				if ( event.getCote()=="left"){
					modeleKinect.setMessageGauche("pause");
				}
				else if ( event.getCote()=="right"){
					modeleKinect.setMessageDroite("pause");
				}
			}		
		}
		
		if(event.getMessage() == "volume"){
			
			ModeleCrossfinder crossfinder = event.getSource().getModele().getModeleCrossfinder();
			if(event.getCote() == "left"){
				crossfinder.setVolumeP1(crossfinder.getVolumeP1()+event.getValeur());
				modeleKinect.setMessageGauche("volume");
			}
			else if(event.getCote() == "right"){
				crossfinder.setVolumeP2(crossfinder.getVolumeP2()+event.getValeur());
				modeleKinect.setMessageDroite("volume");
			}		
		}
		if(event.getMessage() == "crossfinder"){
			ModeleCrossfinder crossfinder = event.getSource().getModele().getModeleCrossfinder();
			crossfinder.setCrossfinder(crossfinder.getCrossfinder()+event.getValeur());
			modeleKinect.setMessageDroite("crossfinder");
			modeleKinect.setMessageGauche("crossfinder");
		}
			
		
	}
}
