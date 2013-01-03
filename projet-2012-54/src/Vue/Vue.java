package Vue;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
		setTitle("AirDJ");
		setSize(1000 + 8 + 9, 700 + 4 + 14);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon("image/icon.png").getImage());


		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createMatteBorder(7, 7, 7, 2, new Color(0x181613)));
		getContentPane().add(panel);	

		vueBrowser = new VueBrowser();
		vueCrossfinder = new VueCrossfinder();
		vueKinect = new VueKinect();
		vuePlayP1 = new VuePlay(0); //blue
		vuePlayP2 = new VuePlay(1);	//red

		panel.add(vueBrowser, BorderLayout.NORTH);
		panel.add(vueCrossfinder, BorderLayout.CENTER);
		panel.add(vueKinect, BorderLayout.SOUTH);
		panel.add(vuePlayP1, BorderLayout.WEST);
		panel.add(vuePlayP2, BorderLayout.EAST);

		setVisible(true);
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