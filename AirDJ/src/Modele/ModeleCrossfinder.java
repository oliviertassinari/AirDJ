package Modele;

public class ModeleCrossfinder
{
	private Modele modele;
	private int crossfinder = 0;
	private int volumeP1 = 100;
	private int volumeP2 = 100;

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
		modele.getVue().getVueCrossfinder().setCrossfinder(value);
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
		modele.getVue().getVueCrossfinder().setVolumeP1(value);
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
		modele.getVue().getVueCrossfinder().setVolumeP2(value);
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
}