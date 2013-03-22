import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Controle.Controle;

import Kinect.Kinect;
import KinectControle.KinectListener;
import KinectControle.KinectSource;
import Modele.Modele;
import Vue.Vue;

public class Main2
{
	public static void main(String[] args)
	{
		Modele modele = new Modele();

		Vue vue = new Vue(modele);
		modele.setVue(vue);
		modele.getModeleCrossfinder().setTimer();

		Controle controle = new Controle(vue, modele);

		final KinectSource kinectSource = new KinectSource(vue, modele);

		//modele.getModelePlayP1().setFilePath("C:/Users/Arthur-MEYER/git/musique/acul.wav");
		//modele.getModelePlayP2().setFilePath("C:/Users/Arthur-MEYER/git/musique/2.wav");	
		
		// test controleKinect
		KinectListener listener = new KinectListener();
		kinectSource.addKinectListener(listener);
		vue.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent arg0)
			{
				// TODO Auto-generated method stub

				if((arg0.getKeyChar()) == 'p')
				{
					kinectSource.fireEvent("play", "right", 0);
				}
				if((arg0.getKeyChar()) == 'm')
				{
					kinectSource.fireEvent("volumeUp", "right", 10);
				}
				if((arg0.getKeyChar()) == 'l')
				{
					kinectSource.fireEvent("volumeDown", "right", -10);
				}

				if((arg0.getKeyChar()) == 'b')
				{
					kinectSource.fireEvent("crossfinderRight", "right", 10);
				}
				if((arg0.getKeyChar()) == 'v')
				{
					kinectSource.fireEvent("crossfinderLeft", "left", -10);
				}

				if((arg0.getKeyChar()) == 'z')
				{
					kinectSource.fireEvent("play", "left", 0);
				}
				if((arg0.getKeyChar()) == 's')
				{
					kinectSource.fireEvent("volumeUp", "left", 10);
				}
				if((arg0.getKeyChar()) == 'q')
				{
					kinectSource.fireEvent("volumeDown", "left", -10);
				}
			}

			public void keyReleased(KeyEvent arg0)
			{
			}

			public void keyTyped(KeyEvent arg0)
			{
			}
		});

		new Kinect(kinectSource, vue);
	}
}