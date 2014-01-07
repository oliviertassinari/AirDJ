
package Controle;

import Modele.Modele;
import Vue.Vue;
import Vue.VueBrowserTable;
import Vue.VueBrowserTree;
import Vue.VueCrossfinder;
import Vue.VuePlay;

public class Controle
{
	private Vue vue;
	private Modele modele;

	/**
	 * constructeur
	 * @param Vue
	 * @param Modele
	 */
	public Controle(Vue vue, Modele modele)
	{
		this.vue = vue;
		this.modele = modele;

		ControleBrowserTree controleBrowserTree = new ControleBrowserTree(this);
		VueBrowserTree vueBrowserTree = vue.getVueBrowser().getVueBrowserTree();
		vueBrowserTree.addMouseListener(controleBrowserTree);
		vueBrowserTree.addMouseMotionListener(controleBrowserTree);
		vueBrowserTree.addMouseWheelListener(controleBrowserTree);

		ControleBrowserTable controleBrowserTable = new ControleBrowserTable(this);
		VueBrowserTable vueBrowserTable = vue.getVueBrowser().getVueBrowserTable();
		vueBrowserTable.addMouseListener(controleBrowserTable);
		vueBrowserTable.addMouseMotionListener(controleBrowserTable);
		vueBrowserTable.addMouseWheelListener(controleBrowserTable);

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

	/**
	 * accéder à la vue
	 * @return Vue
	 */
	public Vue getVue()
	{
		return vue;
	}

	/**
	 * accéder au modele
	 * @return Modele
	 */
	public Modele getModele()
	{
		return modele;
	}
}
