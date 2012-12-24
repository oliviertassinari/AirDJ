package Controle;

import Modele.Modele;
import Vue.Vue;
import Vue.VueCrossfinder;

public class Controle
{
	private Vue vue;
	private Modele modele;

	public Controle(Vue vue, Modele modele)
	{
		this.vue = vue;
		this.modele = modele;
		
		ControleCrossfinder controleCrossfinder = new ControleCrossfinder(this);
		VueCrossfinder vueCrossfinder = vue.getVueCrossfinder();
		vueCrossfinder.addMouseListener(controleCrossfinder);
		vueCrossfinder.addMouseMotionListener(controleCrossfinder);
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
