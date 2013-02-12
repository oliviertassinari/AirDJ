package Modele;

public class ModeleKinect
{
	private Modele modele;
	String messageDroite="commande";
	String messageGauche="commande";

	public ModeleKinect(Modele modele)
	{
		this.modele = modele;
	}
	public void setMessageDroite(String messageDroite){
		this.messageDroite=messageDroite;
		modele.getVue().getVueKinect().repaint();
		
	}
	
	public void setMessageGauche(String messageGauche){
		this.messageGauche=messageGauche;
		modele.getVue().getVueKinect().repaint();
	
	}
	
	public String getMessageDroite(){
		return messageDroite;
	}
	public String getMessageGauche(){
		return messageGauche;
	}
}
