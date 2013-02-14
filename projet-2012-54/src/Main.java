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

		// modele.getModelePlayP1().setFilePath("/stud/users/promo15/ameyer/git/musique/Aerodynamic.wav");

		final KinectSource kinectSource = new KinectSource(vue, modele);

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
					kinectSource.fireEvent("volume", "right", 10);
				}
				if((arg0.getKeyChar()) == 'l')
				{
					kinectSource.fireEvent("volume", "right", -10);
				}

				if((arg0.getKeyChar()) == 'b')
				{
					kinectSource.fireEvent("crossfinder", "right", 10);
				}
				if((arg0.getKeyChar()) == 'v')
				{
					kinectSource.fireEvent("crossfinder", "left", -10);
				}

				if((arg0.getKeyChar()) == 'z')
				{
					kinectSource.fireEvent("play", "left", 0);
				}
				if((arg0.getKeyChar()) == 's')
				{
					kinectSource.fireEvent("volume", "left", 10);
				}
				if((arg0.getKeyChar()) == 'q')
				{
					kinectSource.fireEvent("volume", "left", -10);
				}
			}

			public void keyReleased(KeyEvent arg0)
			{
			}

			public void keyTyped(KeyEvent arg0)
			{
			}
		});
	}
}
