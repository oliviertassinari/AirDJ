
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Controle.Controle;
import KinectControle.ClavierListener;
import KinectControle.KinectListener;
import KinectControle.KinectSource;
import Modele.Modele;
import Vue.Vue;

public class Main
{
	public static void main(String[] args)
	{
		Modele modele = new Modele();

		Vue vue = new Vue(modele);
		modele.setVue(vue);
		modele.getModeleCrossfinder().setTimer();

		Controle controle = new Controle(vue, modele);

		modele.getModelePlayP1().setFilePath("data/Aerodynamic.wav");
		
		//test controleKinect
		KinectListener listener = new KinectListener();
		final KinectSource kinect = new KinectSource(vue, modele);
		kinect.addKinectListener(listener);
		vue.addKeyListener(new KeyListener() {
		
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if((arg0.getKeyChar())=='a'){
					kinect.fireEvent("play");
				}
				
				if((arg0.getKeyChar())=='p'){
					kinect.fireEvent("pause");
				}
				}

			public void keyReleased(KeyEvent arg0) {
			}

			
			public void keyTyped(KeyEvent arg0) {
			}
            
        });
		
	}
}