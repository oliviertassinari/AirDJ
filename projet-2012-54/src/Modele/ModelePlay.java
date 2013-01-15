package Modele;

import java.util.Timer;
import java.util.TimerTask;

import Vue.VuePlay;
import Audio.IPlayer;
import Audio.Player;

public class ModelePlay
{
	private Modele modele;
	private VuePlay vuePlay;
	private String filePath = "";
	private int pitch = 0;
	private String title = "Drag a song on this desk to load it";
	private String artist = "";

	private int bpm = 0;
	private int total = 0;
	private int current = 0;
	private int state = 0; //0 pause - 1 play
	private int play = 0;
	private int pause = 0;
	private IPlayer player;
	private int cote; // 0 gauche - 1 droite

	public ModelePlay(Modele modele, int cote)
	{
		this.modele = modele;
		this.cote = cote;
	}

	public void setVuePlay(VuePlay vuePlay)
	{
		this.vuePlay = vuePlay;
	}

	public void setFilePath(String value)
	{
		filePath = value;

		player = new Player(value);

		title = value;
		artist = value;

		bpm = 108;
		total = 2621;

		current = 0;

		vuePlay.repaint();
	}

	public void setPlay()
	{
		player.setPlay();
	}

	public void setPlay(int value)
	{
		play = value;
		vuePlay.repaint();
		
		Timer timer1 = new Timer();
		timer1.schedule(new TimerTask()
		{
			public void run()
			{
				current = Math.round((player.getPosition()/100000));
				vuePlay.repaint();
			}
		}, 100, 100);

		Timer timer2 = new Timer();
		timer2.schedule(new TimerTask()
		{
			public void run()
			{
				
				modele.getVue().getVueCrossfinder().repaint();
			}
		}, 100, 100);
	}

	public void setPause()
	{
		player.setPause();
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

		player.setVitesse(pitch);

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
		player.setPosition(current*100000);

		vuePlay.repaint();
	}

	public void setVolume(int volume)
	{
		if(filePath != "")
		{
			player.setVolume(volume);
		}
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
