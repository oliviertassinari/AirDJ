package Modele;

import Vue.VuePlay;

public class ModelePlay
{
	private Modele modele;
	private VuePlay vuePlay;
	private String filePath = "";
	private int pitch = 0;
	private String title = "";
	private String artist = "";
	private double bpm = 0;
	private int total = 0;
	private int current = 0;

	public ModelePlay(Modele modele, VuePlay vuePlay)
	{
		this.modele = modele;
		this.vuePlay = vuePlay;
	}

	public void setFilePath(String value)
	{
		filePath = value;

		title = "Mozart: Allegreto In B Flat For St...";
		artist = "Academy Of St. Martin In The ...";
		bpm = 108.83;
		total = 2621;

		vuePlay.setInfo(title, artist, bpm, total);
		setCurrent(0);
	}

	public void setPlay()
	{
	}

	public void setPause()
	{
	}

	public void setPitch(int value)
	{
		if(value > 100)
		{
			value = 100;
		}
		else if(value < -100)
		{
			value = -100;
		}

		pitch = value;
		vuePlay.setPitch(value);
	}

	public void setCurrent(int value)
	{
		if(value < 0)
		{
			value = 0;
		}
		else if(value > total)
		{
			value = total;
		}

		current = value;
		vuePlay.setCurrent(value);
	}

	public int getPitch()
	{
		return pitch;
	}
	
	public int getTotal()
	{
		return total;
	}
}
