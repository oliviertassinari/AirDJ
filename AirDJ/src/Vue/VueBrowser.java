package Vue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import Modele.ModeleFileTree;

public class VueBrowser extends JPanel
{
	public VueBrowser()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		}
		catch(Exception e){}

		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setBackground(Color.GREEN);
		//setPreferredSize(new Dimension(1000, 200));

		JTree tree = new JTree(new ModeleFileTree());
		tree.setBackground(Color.BLACK);
	    DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        renderer.setBackgroundNonSelectionColor(Color.BLACK);
        renderer.setTextNonSelectionColor(Color.WHITE); 
        renderer.setBackgroundSelectionColor(new Color(0x678a30));        
        renderer.setTextSelectionColor(Color.WHITE);
        renderer.setBorderSelectionColor(new Color(0x678a30));

		JScrollPane treePlane = new JScrollPane(tree);
		treePlane.setPreferredSize(new Dimension(400, 170));
		treePlane.setBorder(BorderFactory.createEmptyBorder());

		add(treePlane);
	}
}
