package Vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class VueKinect extends JPanel
{
	public VueKinect()
	{
		setBackground(Color.RED);
		setPreferredSize(new Dimension(1000, 200));
	}
	
	protected void paintComponent(Graphics g)
	{
		
	}
}
