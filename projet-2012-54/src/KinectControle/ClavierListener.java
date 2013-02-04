package KinectControle;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ClavierListener implements KeyListener {

	KinectSource source;
	public ClavierListener(KinectSource source){
		this.source=source;
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if((arg0.getKeyChar())=='a'){
			source.fireEvent("play");
		}
		
		if((arg0.getKeyChar())=='p'){
			source.fireEvent("pause");
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
