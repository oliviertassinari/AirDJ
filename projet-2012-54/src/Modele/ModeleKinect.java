package Modele;

public class ModeleKinect {
	/**
	 * Partie du Modele controlée par le package kinectControle.
	 */
	private Modele modele;
	String messageDroite = "commande";
	String messageGauche = "commande";

	public ModeleKinect(Modele modele) {
		this.modele = modele;
	}

	/**
	 * 
	 * @param messageDroite
	 */
	public void setMessageDroite(String messageDroite) {
		this.messageDroite = messageDroite;
		modele.getVue().getVueKinect().repaint();

	}

	/**
	 * 
	 * @param messageGauche
	 */
	public void setMessageGauche(String messageGauche) {
		this.messageGauche = messageGauche;
		modele.getVue().getVueKinect().repaint();

	}

	/**
	 * 
	 * @return String
	 */
	public String getMessageDroite() {
		return messageDroite;
	}

	/**
	 * 
	 * @return String
	 */
	public String getMessageGauche() {
		return messageGauche;
	}
}
