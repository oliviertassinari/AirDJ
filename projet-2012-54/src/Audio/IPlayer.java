package Audio;

public interface IPlayer
{
	/**
	 * 
	 */
	public void setPause();
	
	/**
	 * 
	 */
	public void setPlay();
	
	/** Regle le volume
	 * @param volume de 0 a 100
	 */
	public void setVolume(float volume);
	
	/** NON FONCTIONELLE
	 * @deprecated
	 * @param vitesse
	 */
	public void setVitesse(int vitesse);
	
	/** Retourne la position
	 * 
	 * @return en seconde
	 */
	public float getPosition();
	
	/** Avance a la position souhaite
	 * 
	 * @param position en seconde
	 */
	public void setPosition(float position);
	
	/**
	 * 
	 * @return volume du sample en cours
	 */
	public float getCurrentVolume();
	
	/**
	 * 
	 * @return duree totale du morceau en seconde
	 */
	public float getLength();
}
