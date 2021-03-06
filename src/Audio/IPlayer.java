package Audio;

import Modele.ModelePlay;

public interface IPlayer
{
	/** 
	 * Pause the music if it is currently played
	 */
	public void setPause();

	/** 
	 * Play the music if it is currently paused
	 */
	public void setPlay();

	/**
	 * 
	 * @param volume (0 to 100)
	 */
	public void setVolume(float volume);

	/** 
	 * @param vitesse
	 */
	public void setVitesse(double vitesse);

	/** 
	 * 
	 * @return current position (s)
	 */
	public float getPosition();

	/** 
	 * Set the current position to
	 * @param position (s)
	 */
	public void setPosition(float position);

	public void setMid(int value);
	
	public void setBass(int value);

	/**
	 * 
	 * @return volume of the current 0.05s played (0-left  1-right)
	 */
	public int[] getCurrentVolume();

	/**
	 * 
	 * @return total length of the file
	 */
	public float getLength();

	public void computeBPM(ModelePlay modelePlay);

	public int[][] getVolumeArray();

	/**
	 * Reset song
	 */
	public boolean reset();

	public String getTitle();

	public String getArtist();

	/**
	 * Stop thread
	 */
	public void stop();
}
