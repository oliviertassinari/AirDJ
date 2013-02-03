package Audio;

public interface IPlayer
{
	/** 
	 * Met le morceau en pause s'il est en lecture
	 */
	public void setPause();

	/** 
	 * Met le morceau en lecture s'il est en pause
	 */
	public void setPlay();

	/**
	 * Regle le volume
	 * @param volume de 0 a 100
	 */
	public void setVolume(float volume);

	/** NON FONCTIONELLE
	 * @deprecated
	 * @param vitesse
	 */
	public void setVitesse(int vitesse);

	/** 
	 * Retourne la position dans la piste audio
	 * @return en seconde
	 */
	public float getPosition();

	/** 
	 * Avance a la position souhaite
	 * @param position en seconde
	 */
	public void setPosition(float position);

	/**
	 * Regle la position de lecture
	 * @return volume du sample en cours
	 */
	public int[] getCurrentVolume();

	/**
	 * Donne la longueur du morceux en seconde
	 * @return duree totale du morceau en seconde
	 */
	public float getLength();

	/**
	 * reinitialise le morceau
	 */
	public boolean reset();
}
