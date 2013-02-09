package KinectControle;

import java.util.ArrayList;
import java.util.Iterator;

import Modele.Modele;
import Vue.Vue;

public class KinectSource
{
	private Vue vue;
	private Modele modele;
	private ArrayList<ListenerInterface> listeners = new ArrayList<ListenerInterface>();

	public KinectSource(Vue vue, Modele modele)
	{
		this.vue = vue;
		this.modele = modele;
	}

	public synchronized void addKinectListener(ListenerInterface listener)
	{
		listeners.add(listener);
	}
	
	public synchronized void removeKinectListener(ListenerInterface listener)
	{
		listeners.remove(listener);
	}
	
	public synchronized void fireEvent(String message, String cote, int valeur)
	{
		KinectEvent event = new KinectEvent(this, message, cote, valeur);
		Iterator<ListenerInterface> i = listeners.iterator();

	    while(i.hasNext())
	    {
	    	((ListenerInterface) i.next()).ListenToKinect(event);	
	    }
	}
	
	public Vue getVue()
	{
		return vue;
	}

	public Modele getModele()
	{
		return modele;
	}
}
