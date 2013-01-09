package Audio;


public interface IPlayer {

	public void setPause();
	public void setPlay();
	public void setVolume(float volume);
	public void setVitesse(int vitesse);
	public float getPosition();
	public void setPosition(float position);
	public float getCurrentVolume ();
	
	
}
