package Modele;

import java.util.Timer;
import java.util.TimerTask;

import Audio.Player;

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

		Timer timer1 = new Timer();
		timer1.schedule(new TimerTask()
		{
			public void run()
			{
				ModelePlay modelePlayP1 = ModeleCrossfinder.this.modele.getModelePlayP1();
				ModelePlay modelePlayP2 = ModeleCrossfinder.this.modele.getModelePlayP2();

				modelePlayP1.updateCurrent();
				modelePlayP2.updateCurrent();

				displayVolumeP1 = modelePlayP1.getDisplayVolume();
				displayVolumeP2 = modelePlayP2.getDisplayVolume();

				ModeleCrossfinder.this.modele.getVue().getVueCrossfinder().repaint();
			}
		}, 1000, 100);
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

		if(crossfinder < 0)
		{
			modele.getModelePlayP2().setVolume((int)(volumeP2*getCoefVolumeP2()));
		}
		else
		{
			modele.getModelePlayP1().setVolume((int)(volumeP1*getCoefVolumeP1()));
		}

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
		modele.getModelePlayP1().setVolume((int)(volumeP1*getCoefVolumeP1()));
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
		modele.getModelePlayP2().setVolume((int)(volumeP2*getCoefVolumeP2()));
		modele.getVue().getVueCrossfinder().repaint();
	}

	public float getCoefVolumeP1()
	{
		if(crossfinder < 0)
		{
			return 1;
		}
		else
		{
			return (float)(100-crossfinder)/100;
		}
	}
	
	public float getCoefVolumeP2()
	{
		if(crossfinder > 0)
		{
			return 1;
		}
		else
		{
			return (float)(100+crossfinder)/100;
		}
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