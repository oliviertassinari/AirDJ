
package Modele;

import java.util.Timer;
import java.util.TimerTask;

public class ModeleCrossfinder
{

	private Modele modele;
	/**
	 * position du cross finder
	 */
	private int crossfinder = 0;
	/**
	 * volume piste 1
	 */
	private int volumeP1 = 100;
	/**
	 * volume piste 2
	 * 
	 */
	private int volumeP2 = 100;
	private int[] displayVolumeP1 = {0, 0};
	private int[] displayVolumeP2 = {0, 0};

	/**
	 * index 0 : Mid
	 * index 1 : Bass
	 * index 2 : Mid button
	 * index 3 : Bass button
	 */
	private int[] equalizerP1 = {0, 0, 0, 0};
	private int[] equalizerP2 = {0, 0, 0, 0};

	/**
	 * constructeur
	 * @param Modele
	 */
	public ModeleCrossfinder(Modele modele)
	{
		this.modele = modele;
	}

	/**
	 * timer pour mette à jour le volume
	 */
	public void setTimer()
	{
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
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
		}, 0, 100);
	}

	/**
	 * @param int la valeur qu'on veut appliquer au cross finder
	 */
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
			modele.getModelePlayP2().setVolume((int)(volumeP2 * getCoefVolumeP2()));
		}
		else
		{
			modele.getModelePlayP1().setVolume((int)(volumeP1 * getCoefVolumeP1()));
		}

		modele.getVue().getVueCrossfinder().repaint();
	}

	/**
	 * @param int valeur qu'on veut appliquer au volume de la piste 1
	 */
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
		modele.getModelePlayP1().setVolume((int)(volumeP1 * getCoefVolumeP1()));
		modele.getVue().getVueCrossfinder().repaint();
	}

	/**
	 * @param int valeur qu'on veut appliquer au volume de la piste 2
	 */
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
		modele.getModelePlayP2().setVolume((int)(volumeP2 * getCoefVolumeP2()));
		modele.getVue().getVueCrossfinder().repaint();
	}

	public void setEqualizerP1(int index, int value)
	{
		if((index == 0 || index == 1) && value > 50)
		{
			value = 50;
		}
		else if((index == 0 || index == 1) && value < -50)
		{
			value = -50;
		}
		
		if(index == 0) //Mid
		{
			modele.getModelePlayP1().setMid(value);
		}
		else if(index == 1) //Bass
		{
			modele.getModelePlayP1().setBass(value);
		}

		equalizerP1[index] = value;

		modele.getVue().getVueCrossfinder().repaint();
	}

	public void setEqualizerP2(int index, int value)
	{
		if((index == 0 || index == 1) && value > 50)
		{
			value = 50;
		}
		else if((index == 0 || index == 1) && value < -50)
		{
			value = -50;
		}

		if(index == 0) //Mid
		{
			modele.getModelePlayP2().setMid(value);
		}
		else if(index == 1) //Bass
		{
			modele.getModelePlayP2().setBass(value);
		}

		equalizerP2[index] = value;

		modele.getVue().getVueCrossfinder().repaint();
	}

	/**
	 * @return float le coefficient à appliquer au volume de la piste 1 en fonction de la valeur du crossfinder
	 */
	public float getCoefVolumeP1()
	{
		if(crossfinder < 0)
		{
			return 1;
		}
		else
		{
			return (float)(100 - crossfinder) / 100;
		}
	}

	/**
	 * @return float le coefficient à appliquer au volume de la piste 2 en fonction de la valeur du crossfinder
	 */
	public float getCoefVolumeP2()
	{
		if(crossfinder > 0)
		{
			return 1;
		}
		else
		{
			return (float)(100 + crossfinder) / 100;
		}
	}

	/**
	 * @return int la valeur du crossfinder
	 */
	public int getCrossfinder()
	{
		return crossfinder;
	}

	/**
	 * @return int la valeur du volume de la piste 1
	 */
	public int getVolumeP1()
	{
		return volumeP1;
	}

	/**
	 * @return int la valeur du volume de la piste 2
	 */
	public int getVolumeP2()
	{
		return volumeP2;
	}

	/**
	 * @return
	 */
	public int[] getDisplayVolumeP1()
	{
		return displayVolumeP1;
	}

	/**
	 * @return
	 */
	public int[] getDisplayVolumeP2()
	{
		return displayVolumeP2;
	}

	public int[] getEqualizerP1()
	{
		return equalizerP1;
	}

	public int[] getEqualizerP2()
	{
		return equalizerP2;
	}
}