package KinectControle;

import Modele.ModelePlay;

public class KinectListener implements ListenerInterface
{
	@Override
	public void ListenToKinect(KinectEvent event)
	{
		if(event.getMessage() == "play")
		{
			ModelePlay modelePlay;
			
			if(event.getParam1() == "left")
			{
				modelePlay = event.getSource().getModele().getModelePlayP1();
			}
			else
			{
				modelePlay = event.getSource().getModele().getModelePlayP2();
			}

			if(event.getSource().getModele().getModelePlayP1().getState() == 0)
			{
				modelePlay.setPlay();
				modelePlay.setButtonPlay("release");
				modelePlay.setButtonPause("out");
			}
			else if(event.getSource().getModele().getModelePlayP1().getState() == 1)
			{
				modelePlay.setPause();
				modelePlay.setButtonPlay("out");
				modelePlay.setButtonPause("release");
			}		
		}
	}
}
