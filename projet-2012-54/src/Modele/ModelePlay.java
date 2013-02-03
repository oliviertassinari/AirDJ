package Modele;

import java.io.File;
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
	private int buttonPlay = 0;
	private int buttonPause = 0;
	private IPlayer player = null;
	private int cote; // 0 gauche - 1 droite
	private int startFrom = 0;

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
		if(player != null && state == 1)
		{
			player.setPause();
		}

		filePath = value;

		File file = new File(filePath);

		player = new Player(value);
		if(cote == 0)
		{	
			player.setVolume((int)(modele.getModeleCrossfinder().getVolumeP1()*modele.getModeleCrossfinder().getCoefVolumeP1()));
		}
		else
		{
			player.setVolume((int)(modele.getModeleCrossfinder().getVolumeP2()*modele.getModeleCrossfinder().getCoefVolumeP2()));
		}

		title = file.getName();
		artist = file.getName();

		bpm = 108;
		total = Math.round(player.getLength()*10);

		current = 0;
		state = 0;

		buttonPlay = 0;
		buttonPause = 2;

		startFrom = 0;

		vuePlay.repaint();
	}

	public void setPlay()
	{
		if(player != null)
		{
			if(state == 0)
			{
				state = 1;
				player.setPlay();
			}	
			else
			{
				player.setPosition(startFrom/10);
			}
		}
	}

	public void setPause()
	{
		if(player != null && state == 1)
		{
			state = 0;
			player.setPause();
		}
	}

	public void setButtonPlay(String state)
	{
		if(state == "over")
		{
			if(this.state == 0 || player == null)
			{
				buttonPlay = 1;
			}
		}
		else if(state == "press")
		{
			buttonPlay = 3;
			buttonPause = 0;
		}
		else if(state == "release")
		{
			buttonPlay = 2;
		}
		else if(state == "out")
		{
			if(this.state == 0  || player == null)
			{
				buttonPlay = 0;
			}
		}

		vuePlay.repaint();
	}

	public void setButtonPause(String state)
	{
		if(state == "over")
		{
			if(this.state == 1  || player == null)
			{
				buttonPause = 1;
			}
		}
		else if(state == "press")
		{
			buttonPlay = 0;
			buttonPause = 3;
		}
		else if(state == "release")
		{
			buttonPause = 2;
		}
		else if(state == "out")
		{
			if(this.state == 1 || player == null)
			{
				buttonPause = 0;
			}
		}

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

		if(player != null)
		{
			player.setVitesse(pitch);
		}

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
		startFrom = value;
		player.setPosition((float)(current)/10);

		vuePlay.repaint();
	}

	public void updateCurrent()
	{
		if(player != null)
		{
			int currentNew = Math.round(player.getPosition()*10);

			if(currentNew == total)
			{
				reset();
			}

			if(currentNew != current)
			{
				current = currentNew;
				vuePlay.repaint();
			}
		}
	}

	public void setVolume(int volume)
	{
		if(player != null)
		{
			player.setVolume(volume);
		}
	}

	public int[] getDisplayVolume()
	{
		if(player != null)
		{
			int[] displayVolume = player.getCurrentVolume();
			return displayVolume;
		}
		else
		{
			int[] displayVolume = {0, 0};
			return displayVolume;
		}
	}
	
	public void reset()
	{
		if(player.reset())
		{
			state = 0;
			buttonPlay = 0;
			buttonPause = 2;
			vuePlay.repaint();
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

	public int getButtonPlay()
	{
		return buttonPlay;
	}

	public int getButtonPause()
	{
		return buttonPause;
	}
}
