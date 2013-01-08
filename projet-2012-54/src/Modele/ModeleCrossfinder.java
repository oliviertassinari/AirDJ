package Modele;

public class ModeleCrossfinder
{
	private Modele modele;
	private int crossfinder = 0;
	private int volumeP1 = 100;
	private int volumeP2 = 100;
	private int[] displayVolumeP1 = {0, 0};
	private int[] displayVolumeP2 = {0, 0};

	public ModeleCrossfinder(Modele modele)
	{
		this.modele = modele;
	}

	public void setCrossfinder(int value)
	{
		if(value > 100)
		{
			value = 100;
		}
		else if(value < -100)
		{
			value = -100;
		}

		crossfinder = value;
		modele.getVue().getVueCrossfinder().repaint();
	}

	public void setVolumeP1(int value)
	{
		if(value > 100)
		{
			value = 100;
		}
		else if(value < 0)
		{
			value = 0;
		}

		volumeP1 = value;
		modele.getVue().getVueCrossfinder().repaint();
	}

	public void setVolumeP2(int value)
	{
		if(value > 100)
		{
			value = 100;
		}
		else if(value < 0)
		{
			value = 0;
		}

		volumeP2 = value;
		modele.getVue().getVueCrossfinder().repaint();
	}

	public int getCrossfinder()
	{
		return crossfinder;
	}

	public int getVolumeP1()
	{
		return volumeP1;
	}

	public int getVolumeP2()
	{
		return volumeP2;
	}

	public int[] getDisplayVolumeP1()
	{
		return displayVolumeP1;
	}

	public int[] getDisplayVolumeP2()
	{
		return displayVolumeP2;
	}
}