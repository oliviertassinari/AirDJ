package KinectControle;

import java.util.ArrayList;
import java.util.Iterator;

import Modele.Modele;
import Vue.Vue;

public class KinectSource {
	Vue vue;
	Modele modele;


	
	// code qu'il faudra intégrer dans une classe kinect
	
	public KinectSource(Vue vue, Modele modele) {
		// TODO Auto-generated constructor stub
		this.vue=vue;
		this.modele=modele;
	}

	private ArrayList<ListenerInterface> listeners = new ArrayList<ListenerInterface>();
	public synchronized void addKinectListener(ListenerInterface listener){
		listeners.add(listener);
		}
	
	public synchronized void removeKinectListener(ListenerInterface listener){
		listeners.remove(listener);
		}
	
	public synchronized void fireEvent(String message){
		KinectEvent event = new KinectEvent(this, message );
		Iterator<ListenerInterface> i = listeners.iterator();
	    while(i.hasNext()){
	    	((ListenerInterface) i.next()).ListenToKinect(event);	  
	    }
	    }
	
	public Vue getVue(){
		return vue;
	}

	public Modele getModele(){
		return modele;
	}
}
