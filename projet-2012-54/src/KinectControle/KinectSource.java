package KinectControle;

import java.util.ArrayList;
import java.util.Iterator;

import Modele.Modele;
import Vue.Vue;

public class KinectSource {
	/**
	 * classe source des KinectEvent
	 */

	private Vue vue;
	private Modele modele;
	private ArrayList<ListenerInterface> listeners = new ArrayList<ListenerInterface>();

	/**
	 * constructeur kinectSource(pour que le listener sache d'ou vient la
	 * commande)
	 * 
	 * @param vue
	 * @param modele
	 */
	public KinectSource(Vue vue, Modele modele) {
		this.vue = vue;
		this.modele = modele;
	}

	/**
	 * pour ajouter un listener
	 * 
	 * @param listener
	 */
	public synchronized void addKinectListener(ListenerInterface listener) {
		listeners.add(listener);
	}

	/**
	 * pour supprimer un listener
	 * 
	 * @param listener
	 */
	public synchronized void removeKinectListener(ListenerInterface listener) {
		listeners.remove(listener);
	}

	/**
	 * pour faire comme s'il y avait eu un évènement. Méthode appelée par la
	 * kinect.
	 * 
	 * @param message
	 * @param cote
	 * @param valeur
	 */
	public synchronized void fireEvent(String message, String cote, int valeur) {
		KinectEvent event = new KinectEvent(this, message, cote, valeur);
		Iterator<ListenerInterface> i = listeners.iterator();

		while (i.hasNext()) {
			((ListenerInterface) i.next()).ListenToKinect(event);
		}
	}

	/**
	 * getter vue
	 * 
	 * @return Vue
	 */
	public Vue getVue() {
		return vue;
	}

	/**
	 * getter modele
	 * 
	 * @return Modele
	 */
	public Modele getModele() {
		return modele;
	}
}
