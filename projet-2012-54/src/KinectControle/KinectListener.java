package KinectControle;

public class KinectListener implements ListenerInterface {

	
	@Override
	public void ListenToKinect(KinectEvent event) {
		// TODO Auto-generated method stub
		if (event.getMessage()=="play"){
			if(event.getSource().getModele().getModelePlayP1().getState()==0){
					//(event.getSource().getVue().getVuePlayP1()).setPause(0);
			//(event.getSource().getVue().getVuePlayP1()).setPlay(2);
			(event.getSource().getModele().getModelePlayP1()).setButtonPlay("press");
			(event.getSource().getModele().getModelePlayP1()).setButtonPause("out");
			(event.getSource().getModele().getModelePlayP1()).setPlay();
			}
			
			if(event.getSource().getModele().getModelePlayP1().getState()==1){
				//(event.getSource().getVue().getVuePlayP1()).setPause(0);
		//(event.getSource().getVue().getVuePlayP1()).setPlay(2);
		(event.getSource().getModele().getModelePlayP1()).setButtonPlay("out");
		(event.getSource().getModele().getModelePlayP1()).setButtonPause("press");
		(event.getSource().getModele().getModelePlayP1()).setPause();
		}
					
		}
			(event.getSource().getVue().getVuePlayP1()).repaint();
		
	}
	

}
