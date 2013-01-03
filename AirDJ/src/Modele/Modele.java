package Modele;

import Vue.Vue;

public class Modele
{
	Vue vue;
	ModeleBrowser modeleBrowser;
	ModeleCrossfinder modeleCrossfinder;
	ModelePlay modelePlayP1;
	ModelePlay modelePlayP2;
	ModeleKinect modeleKinect;

	public Modele(Vue vue)
	{
		this.vue = vue;

		modeleBrowser = new ModeleBrowser(this);
		modeleCrossfinder = new ModeleCrossfinder(this);
		modelePlayP1 = new ModelePlay(this, vue.getVuePlayP1());
		modelePlayP2 = new ModelePlay(this, vue.getVuePlayP2());
		modeleKinect = new ModeleKinect(this);
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
