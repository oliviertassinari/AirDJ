package Vue;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Vue extends JFrame
{
	private static final long serialVersionUID = 3589596133770161700L;
	private VueBrowser vueBrowser;
	private VueCrossfinder vueCrossfinder;
	private VueKinect vueKinect;
	private VuePlay vuePlayP1;
	private VuePlay vuePlayP2;

	public Vue()
	{
		vueBrowser = new VueBrowser();
		vueCrossfinder = new VueCrossfinder();
		vueKinect = new VueKinect();
		vuePlayP1 = new VuePlay();
		vuePlayP2 = new VuePlay();

		setTitle("AirDJ");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHgap(5);
		borderLayout.setVgap(5);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0x262221));
		panel.setLayout(borderLayout);
		getContentPane().add(panel);
		
		panel.add(vueBrowser, BorderLayout.NORTH);
		panel.add(vueCrossfinder, BorderLayout.CENTER);
		panel.add(vueKinect, BorderLayout.SOUTH);
		panel.add(vuePlayP1, BorderLayout.WEST);
		panel.add(vuePlayP2, BorderLayout.EAST);
	}

	public VueBrowser getVueBrowser()
	{
		return vueBrowser;
	}

	public VueCrossfinder getVueCrossfinder()
	{
		return vueCrossfinder;
	}

	public VueKinect getVueKinect()
	{
		return vueKinect;
	}

	public VuePlay getVuePlayP1()
	{
		return vuePlayP1;
	}

	public VuePlay getVuePlayP2()
	{
		return vuePlayP2;
	}
}
