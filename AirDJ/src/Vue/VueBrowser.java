package Vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class VueBrowser extends JPanel
{
	public VueBrowser()
	{
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setPreferredSize(new Dimension(1000, 170));
		setBorder(BorderFactory.createMatteBorder(0, 0, 7, 5, new Color(0x181613)));

		add(new VueBrowserTree());
		add(new VueBrowserTable());
	}
}
