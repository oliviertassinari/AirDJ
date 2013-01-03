package Controle;

import Modele.Modele;
import Vue.Vue;
import Vue.VueBrowserTree;
import Vue.VueCrossfinder;
import Vue.VuePlay;

public class Controle
{
	private Vue vue;
	private Modele modele;

	public Controle(Vue vue, Modele modele)
	{
		this.vue = vue;
		this.modele = modele;

		ControleBrowserTree controleBrowserTree = new ControleBrowserTree(this);
		VueBrowserTree vueBrowserTree = vue.getVueBrowser().getVueBrowserTree();
		vueBrowserTree.addMouseListener(controleBrowserTree);
		vueBrowserTree.addMouseMotionListener(controleBrowserTree);
		vueBrowserTree.addMouseWheelListener(controleBrowserTree);

		ControleCrossfinder controleCrossfinder = new ControleCrossfinder(this);
		VueCrossfinder vueCrossfinder = vue.getVueCrossfinder();
		vueCrossfinder.addMouseListener(controleCrossfinder);
		vueCrossfinder.addMouseMotionListener(controleCrossfinder);
		vueCrossfinder.addMouseWheelListener(controleCrossfinder);

		VuePlay vuePlayP1 = vue.getVuePlayP1();
		ControlePlay controlePlayP1 = new ControlePlay(vuePlayP1, modele.getModelePlayP1());
		vuePlayP1.addMouseListener(controlePlayP1);
		vuePlayP1.addMouseMotionListener(controlePlayP1);
		vuePlayP1.addMouseWheelListener(controlePlayP1);

		VuePlay vuePlayP2 = vue.getVuePlayP2();
		ControlePlay controlePlayP2 = new ControlePlay(vuePlayP2, modele.getModelePlayP2());
		vuePlayP2.addMouseListener(controlePlayP2);
		vuePlayP2.addMouseMotionListener(controlePlayP2);
		vuePlayP2.addMouseWheelListener(controlePlayP2);
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
