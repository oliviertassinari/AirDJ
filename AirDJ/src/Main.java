import Audio.Audio;
import Modele.Modele;
import Vue.Vue;

public class Main
{
	public static void main(String[] args)
	{
		Vue vue = new Vue();
		Audio audio = new Audio();
		Modele modele = new Modele(vue);
	}
}