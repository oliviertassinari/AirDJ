
package KinectControle;

import java.util.EventObject;

public class KinectEvent extends EventObject
{
	private static final long serialVersionUID = 1L;
	private KinectSource source;
	private String message;
	private String cote;
	private int valeur;

	/**
	 * constructeur : construction d'evenements qui seront appeles par la
	 * kinect. Tout les parametres ne seront pas tjs utilises
	 * 
	 * @param source
	 *            //source des evenements qui doivent etre declanchés lsq un
	 *            geste est detecté par la kinect et appele fireEvent
	 * @param message
	 *            //play, volume, crossfinder
	 * @param cote
	 *            // piste a ou b
	 * @param valeur
	 *            // utilisé pour le volume et crossfinder
	 */
	public KinectEvent(KinectSource source, String message, String cote, int valeur)
	{
		super(source);
		this.source = source;
		this.message = message;
		this.cote = cote;
		this.valeur = valeur;
	}

	/**
	 * getter source
	 * 
	 * @return source
	 */
	public KinectSource getSource()
	{
		return source;
	}

	/**
	 * getter message
	 * 
	 * @return message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * getter coté
	 * 
	 * @return
	 */
	public String getCote()
	{
		return cote;
	}

	/**
	 * getteur valeur
	 * 
	 * @return valeur
	 */
	public int getValeur()
	{
		return valeur;
	}
}
