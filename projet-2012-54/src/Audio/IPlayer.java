package Audio;

public interface IPlayer
{
	public void setPause();
	public void setPlay();
	public void setVolume(float volume); //volume de 0 a 100
	public void setVitesse(int vitesse); //non fonctionelle
	public float getPosition(); //retourne la position en seconde
	public void setPosition(float position); //avance a la position souhaite en seconde
	public float getCurrentVolume(); //volume du sample en cours:en cours de modification
	public float getLength(); //duree totale du morceau
}
