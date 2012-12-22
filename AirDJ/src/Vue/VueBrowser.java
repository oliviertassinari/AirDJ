package Vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import Modele.ModeleFileTree;

public class VueBrowser extends JPanel
{
	public VueBrowser()
	{
		setBackground(Color.GREEN);
		setPreferredSize(new Dimension(1000, 200));

		ModeleFileTree fileSystemDataModel = new ModeleFileTree();

		JTree tree = new JTree(fileSystemDataModel);

		JScrollPane treePlane = new JScrollPane(tree);
		treePlane.setPreferredSize(new Dimension(400, 170));

		add(treePlane);
	}
}
