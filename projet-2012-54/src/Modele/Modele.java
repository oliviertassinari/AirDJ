package Modele;

import Audio.Player;
import Vue.Vue;

public class Modele
{
	Vue vue;
	ModeleBrowser modeleBrowser;
	ModeleCrossfinder modeleCrossfinder;
	ModelePlay modelePlayP1;
	ModelePlay modelePlayP2;
	ModeleKinect modeleKinect;

	public Modele()
	{
		modeleBrowser = new ModeleBrowser(this);
		modeleCrossfinder = new ModeleCrossfinder(this);
		modelePlayP1 = new ModelePlay(this, 0);
		modelePlayP2 = new ModelePlay(this, 1);
		modeleKinect = new ModeleKinect(this);
	}

	public void setVue(Vue vue)
	{
		this.vue = vue;
		modelePlayP1.setVuePlay(vue.getVuePlayP1());
		modelePlayP2.setVuePlay(vue.getVuePlayP2());
	}

	public Vue getVue()
	{
		return vue;
	}

	public ModeleBrowser getModeleBrowser()
	{
		return modeleBrowser;
	}

	public ModeleCrossfinder getModeleCrossfinder()
	{
		return modeleCrossfinder;
	}

	public ModelePlay getModelePlayP1()
	{
		return modelePlayP1;
	}

	public ModelePlay getModelePlayP2()
	{
		return modelePlayP2;
	}

	public ModeleKinect getModeleKinect()
	{
		return modeleKinect;
	}
}
