package Vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class VueBrowser extends JPanel
{
	private VueBrowserTree vueBrowserTree;
	private VueBrowserTable vueBrowserTable;

	public VueBrowser()
	{
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setPreferredSize(new Dimension(1000, 170));
		setBorder(BorderFactory.createMatteBorder(0, 0, 7, 5, new Color(0x181613)));

		vueBrowserTree = new VueBrowserTree();
		vueBrowserTable = new VueBrowserTable();

		add(vueBrowserTree);
		add(vueBrowserTable);
	}

	public VueBrowserTree getVueBrowserTree()
	{
		return vueBrowserTree;
	}
	
	public VueBrowserTable getVueBrowserTable()
	{
		return vueBrowserTable;
	}
}
