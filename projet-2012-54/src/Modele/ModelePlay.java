
package Modele;

import java.io.File;

import Vue.VuePlay;
import Audio.IPlayer;
import Audio.Player;

/**
 * Partie du modele associée à VuePlay, contenant des attributs sur l'état de la
 * lecture des pistes, les pistes jouées.
 * 
 * 
 */
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
	private int state = 0; // 0 pause - 1 play
	private int buttonPlay = 0;
	private int buttonPause = 0;
	private IPlayer player = null;
	private int cote; // 0 gauche - 1 droite
	private int startFrom = 0;
	private int buttonBPM = 0;

	/**
	 * 
	 * @param modele
	 * @param cote
	 */
	public ModelePlay(Modele modele, int cote)
	{
		this.modele = modele;
		this.cote = cote;
	}

	/**
	 * 
	 * @param vuePlay
	 */
	public void setVuePlay(VuePlay vuePlay)
	{
		this.vuePlay = vuePlay;
	}

	/**
	 * 
	 * @param value
	 */
	public void setFilePath(String value)
	{
		if(player != null && state == 1)
		{
			player.stop();
		}

		filePath = value;

		File file = new File(filePath);

		player = new Player(value);
		if(cote == 0)
		{
			player.setVolume((int)(modele.getModeleCrossfinder().getVolumeP1() * modele.getModeleCrossfinder().getCoefVolumeP1()));
		}
		else
		{
			player.setVolume((int)(modele.getModeleCrossfinder().getVolumeP2() * modele.getModeleCrossfinder().getCoefVolumeP2()));
		}

		title = file.getName();
		artist = file.getName();

		total = Math.round(player.getLength() * 10);

		bpm = 0;

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
				player.setPosition(startFrom / 10);
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

	/**
	 * 
	 * @param state
	 */
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
		{ if(this.state == 1 ){
			buttonPlay = 2;
		}
		}
		else if(state == "out")
		{
			if(this.state == 0 || player == null)
			{
				buttonPlay = 0;
			}
		}

		vuePlay.repaint();
	}

	/**
	 * 
	 * @param state
	 */
	public void setButtonPause(String state)
	{
		if(state == "over")
		{
			if(this.state == 1 || player == null)
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
			 if(this.state == 0 ){
				buttonPause = 2;
			}
		
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
	
	public void setButtonBPM(String state)
	{
		if(state == "over")
		{
			buttonBPM = 1;
		}
		else
		{
			buttonBPM = 0;
		}
		
		vuePlay.repaint();
	}

	/**
	 * 
	 * @param value
	 */
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

	/**
	 * 
	 * @param value
	 */
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
		player.setPosition((float)(current) / 10);

		vuePlay.repaint();
	}

	public void updateCurrent()
	{
		if(player != null)
		{
			int currentNew = Math.round(player.getPosition() * 10);

			if(currentNew == total)
			{
				reset();
			}

			if(currentNew != current || current == 0)
			{
				current = currentNew;
				vuePlay.repaint();
			}
		}
	}

	/**
	 * 
	 * @param volume
	 */
	public void setVolume(int volume)
	{
		if(player != null)
		{
			player.setVolume(volume);
		}
	}

	/**
	 * 
	 * @return int[]
	 */
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
			current = 0;
			state = 0;
			buttonPlay = 0;
			buttonPause = 2;
			vuePlay.repaint();
		}
	}

	/**
	 * 
	 * @return int
	 */
	public int getPitch()
	{
		return pitch;
	}

	/**
	 * 
	 * @return String
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * 
	 * @return String
	 */
	public String getArtist()
	{
		return artist;
	}

	/**
	 * 
	 * @return double
	 */
	public double getBpm()
	{
		return bpm;
	}

	public void computeBPM()
	{
		if(player != null)
		{
			bpm = -1;
	
			player.computeBPM(this);
		}
	}
	
	public void computeBPMEnd(double bpm)
	{		
		this.bpm = bpm;
	}

	/**
	 * 
	 * @return int
	 */
	public int getTotal()
	{
		return total;
	}

	/**
	 * 
	 * @return int
	 */
	public int getCurrent()
	{
		return current;
	}

	/**
	 * 
	 * @return int
	 */
	public int getButtonPlay()
	{
		return buttonPlay;
	}

	/**
	 * 
	 * @return int
	 */
	public int getButtonPause()
	{
		return buttonPause;
	}

	/**
	 * 
	 * @return int
	 */
	public int getState()
	{
		return state;
	}

	/**
	 * 
	 * @return IPlayer
	 */
	public IPlayer getPlayer()
	{
		return player;
	}

	public double getBPM()
	{
		return bpm;
	}

	/**
	 * retourne l'etat du bouton BPM
	 * @return etat button BPM
	 */
	public int getButtonBPM()
	{
		return buttonBPM;
	}
}
