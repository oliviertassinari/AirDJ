import Audio.Audio;
import Controle.Controle;
import Modele.Modele;
import Vue.Vue;

public class Main
{
	public static void main(String[] args)
	{
		Modele modele = new Modele();

		Vue vue = new Vue(modele);
		modele.setVue(vue);

		Audio audio = new Audio();

		Controle controle = new Controle(vue, modele);

		modele.getModelePlayP1().setFilePath("lol");
	}
}