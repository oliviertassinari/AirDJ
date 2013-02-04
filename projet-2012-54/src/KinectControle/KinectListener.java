package KinectControle;

public class KinectListener implements ListenerInterface {

	
	@Override
	public void ListenToKinect(KinectEvent event) {
		// TODO Auto-generated method stub
		if (event.getMessage()=="play"){
			//(event.getSource().getVue().getVuePlayP1()).setPause(0);
			//(event.getSource().getVue().getVuePlayP1()).setPlay(2);
			(event.getSource().getModele().getModelePlayP1()).setPlay();
			
		}
		if (event.getMessage()=="pause"){
			//(event.getSource().getModele().getModelePlayP1()).setPause(2);
			//(event.getSource().getModele().getModelePlayP1()).setPlay(0);
			(event.getSource().getModele().getModelePlayP1()).setPause();
		}
		(event.getSource().getVue().getVuePlayP1()).repaint();
		
	}
	

}
