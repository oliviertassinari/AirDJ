package Vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


public class VueBrowserTable extends JPanel
{
	public VueBrowserTable()
	{
		setBackground(Color.RED);
		setPreferredSize(new Dimension(645, 163));
		setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, new Color(0x4E4C4B)));
	}

	protected void paintComponent(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 1, 644, 162);
	}
}
