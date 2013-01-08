package Modele;

import Vue.VuePlay;

public class ModelePlay
{
	private Modele modele;
	private VuePlay vuePlay;
	private String filePath = "";
	private int pitch = 0;
	private String title = "Drag a song on this desk to load it";
	private String artist = "";
	private double bpm = 0;
	private int total = 0;
	private int current = 0;
	private int play = 0;
	private int pause = 0;

	public ModelePlay(Modele modele)
	{
		this.modele = modele;
	}

	public void setVuePlay(VuePlay vuePlay)
	{
		this.vuePlay = vuePlay;
	}

	public void setFilePath(String value)
	{
		filePath = value;

		title = "Mozart: Allegreto In B Flat For St...";
		artist = "Academy Of St. Martin In The ...";
		bpm = 108.83;
		total = 2621;
		current = 0;

		vuePlay.repaint();
	}

	public void setPlay()
	{
	}
	
	public void setPlay(int value)
	{
		play = value;
		vuePlay.repaint();
	}

	public void setPause()
	{
	}

	public void setPause(int value)
	{
		pause = value;
		vuePlay.repaint();
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
		vuePlay.repaint();
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
		vuePlay.repaint();
	}

	public int getPitch()
	{
		return pitch;
	}

	public String getTitle()
	{
		return title;
	}

	public String getArtist()
	{
		return artist;
	}

	public double getBpm()
	{
		return bpm;
	}

	public int getTotal()
	{
		return total;
	}

	public int getCurrent()
	{
		return current;
	}

	public int getPlay()
	{
		return play;
	}

	public int getPause()
	{
		return pause;
	}
}
