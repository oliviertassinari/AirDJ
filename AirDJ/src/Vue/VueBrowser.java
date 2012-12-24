package Vue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import Modele.ModeleBrowserTable;
import Modele.ModeleFileTree;

public class VueBrowser extends JPanel
{
	public VueBrowser()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//SwingUtilities.updateComponentTreeUI(this);
		}
		catch(Exception e){}

		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setBackground(Color.GREEN);
		setPreferredSize(new Dimension(1000, 170));

		JTree tree = new JTree(new ModeleFileTree());
		tree.setBackground(Color.BLACK);
	    DefaultTreeCellRenderer treeRenderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
	    treeRenderer.setBackgroundNonSelectionColor(Color.BLACK);
        treeRenderer.setTextNonSelectionColor(Color.WHITE); 
        treeRenderer.setBackgroundSelectionColor(new Color(0x678a30));        
        treeRenderer.setTextSelectionColor(Color.WHITE);
        treeRenderer.setBorderSelectionColor(new Color(0x678a30));

		JScrollPane treeScrollPlane = new JScrollPane(tree);
		treeScrollPlane.setPreferredSize(new Dimension(400, 170));
		treeScrollPlane.setBorder(BorderFactory.createEmptyBorder());

		add(treeScrollPlane);

		JTable table = new JTable(new ModeleBrowserTable())
    	{
    		public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
    		{
    			Component c = super.prepareRenderer(renderer, row, column);
    			JComponent jc = (JComponent)c;

    			c.setForeground(Color.WHITE);

    			if(isRowSelected(row))
    			{
    				c.setBackground(new Color(0x678a30));
    			}
    			else
    			{
    				if(row % 2 == 1)
    				{
    					c.setBackground(new Color(0x171717));
    				}
    				else
    				{
    					c.setBackground(Color.BLACK);
    				}
    			}

    			return c;
    		}
    	};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoCreateRowSorter(true);

		JScrollPane tableScrollPlane = new JScrollPane(table);
		tableScrollPlane.setPreferredSize(new Dimension(400, 170));
		tableScrollPlane.setBorder(BorderFactory.createEmptyBorder());

		add(tableScrollPlane);
	}
}
