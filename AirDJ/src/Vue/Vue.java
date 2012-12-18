package Vue;

import javax.swing.JFrame;

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
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
