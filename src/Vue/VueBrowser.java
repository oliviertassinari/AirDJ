
package Vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class VueBrowser extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2434229119897581826L;
	private Vue vue;
	private VueBrowserTree vueBrowserTree;
	private VueBrowserTable vueBrowserTable;

	/**
	 * constructeur vueBrowser (en haut de la fenetre, pour choix pistes)
	 * @param vue
	 */
	public VueBrowser(Vue vue)
	{
		this.vue = vue;

		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setPreferredSize(new Dimension(1000, 170));
		setBorder(BorderFactory.createMatteBorder(0, 0, 7, 5, new Color(0x181613)));

		vueBrowserTree = new VueBrowserTree();
		vueBrowserTable = new VueBrowserTable();

		add(vueBrowserTree);
		add(vueBrowserTable);
	}

	/**
	 * getter vueBrowserTree (arbre de recherche)
	 * @return vueBrowserTree
	 */
	public VueBrowserTree getVueBrowserTree()
	{
		return vueBrowserTree;
	}

	/**
	 * getter vueBrowserTable (affichage fichiers wav)
	 * @return VueBrowserTable
	 */
	public VueBrowserTable getVueBrowserTable()
	{
		return vueBrowserTable;
	}
}
