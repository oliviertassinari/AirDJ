import Audio.Audio;
import Controle.Controle;
import Modele.Modele;
import Vue.Vue;

public class Main
{
	public static void main(String[] args)
	{
		Vue vue = new Vue();
		Audio audio = new Audio();
		Modele modele = new Modele(vue);
		Controle controle = new Controle(vue, modele);
	}
}