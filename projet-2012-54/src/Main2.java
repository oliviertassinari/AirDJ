
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

		KinectListener listener = new KinectListener();
		final KinectSource kinectSource = new KinectSource(vue, modele);
		kinectSource.addKinectListener(listener);

		new Kinect(kinectSource, vue);
	}
}