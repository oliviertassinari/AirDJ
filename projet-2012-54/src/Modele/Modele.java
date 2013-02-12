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

	/** 
	 * constructeur
	 */
	public Modele()
	{
		modeleBrowser = new ModeleBrowser(this);
		modeleCrossfinder = new ModeleCrossfinder(this);
		modelePlayP1 = new ModelePlay(this, 0);
		modelePlayP2 = new ModelePlay(this, 1);
		modeleKinect = new ModeleKinect(this);
	}

	/**
	 * définir la vue référente
	 * @param Vue
	 */
	public void setVue(Vue vue)
	{
		this.vue = vue;
		modelePlayP1.setVuePlay(vue.getVuePlayP1());
		modelePlayP2.setVuePlay(vue.getVuePlayP2());
	}

	/**
	 * accéder à la vue référente
	 * @return Vue
	 */
	public Vue getVue()
	{
		return vue;
	}

	/**
	 * accéder au ModeleBrowser
	 * @return ModeleBrowserTree
	 */
	public ModeleBrowser getModeleBrowser()
	{
		return modeleBrowser;
	}

	/**
	 * accéder au ModeleCrossFinder
	 * @return ModeleCrossFinder
	 */
	public ModeleCrossfinder getModeleCrossfinder()
	{
		return modeleCrossfinder;
	}

	/**
	 * accéder au ModelePlay de la piste 1
	 * @return ModelePlay
	 */
	public ModelePlay getModelePlayP1()
	{
		return modelePlayP1;
	}

	/**
	 * accéder au ModelePlay de la piste 2
	 * @return ModelePlay
	 */
	public ModelePlay getModelePlayP2()
	{
		return modelePlayP2;
	}

	/**
	 * accéder au ModeleKinect
	 * @return ModeleKinect
	 */
	public ModeleKinect getModeleKinect()
	{
		return modeleKinect;
	}
}
