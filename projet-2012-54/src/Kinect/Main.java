package Kinect;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Controle.Controle;

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

		modele.getModelePlayP1().setFilePath("data/Any Colour You Like.wav");
		
		//test controleKinect
		KinectListener listener = new KinectListener();
		final KinectSource kinectSource = new KinectSource(vue, modele);
		kinectSource.addKinectListener(listener);
		vue.addKeyListener(new KeyListener() {
		
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
							
				if((arg0.getKeyChar())=='p'){
					kinectSource.fireEvent("play");
				}
				}

			public void keyReleased(KeyEvent arg0) {
			}

			
			public void keyTyped(KeyEvent arg0) {
			}
            
        });
		
		new Kinect(kinectSource);
	}
}