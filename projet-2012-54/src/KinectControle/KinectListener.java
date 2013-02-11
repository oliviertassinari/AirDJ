package KinectControle;

import Modele.ModeleCrossfinder;
import Modele.ModelePlay;

public class KinectListener implements ListenerInterface
{
	/**
	 * 
	 */
	@Override
	public void ListenToKinect(KinectEvent event)
	{		
		
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
			}
			else if(modelePlay.getState() == 1)
			{
				modelePlay.setPause();
				modelePlay.setButtonPlay("out");
				modelePlay.setButtonPause("release");
			}		
		}
		
		if(event.getMessage() == "volume"){
			
			ModeleCrossfinder crossfinder = event.getSource().getModele().getModeleCrossfinder();
			if(event.getCote() == "left"){
				crossfinder.setVolumeP1(crossfinder.getVolumeP1()+event.getValeur());
			}
			else if(event.getCote() == "right"){
				crossfinder.setVolumeP2(crossfinder.getVolumeP2()+event.getValeur());
			}		
		}
		if(event.getMessage() == "crossfinder"){
			ModeleCrossfinder crossfinder = event.getSource().getModele().getModeleCrossfinder();
			crossfinder.setCrossfinder(crossfinder.getCrossfinder()+event.getValeur());
		}
			
		
	}
}
