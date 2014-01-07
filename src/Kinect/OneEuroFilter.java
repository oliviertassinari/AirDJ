
package Kinect;

/**
 * Implementation du filtre d'un euro.
 */
public class OneEuroFilter
{
	private double freq;
	private double minFreqCut;
	private double beta;
	private double dFreqCut;
	private double lastTime;
	private LowPassFilter x;
	private LowPassFilter dx;

	/**
	 * Initialise le filtre.
	 * @param freq fr�quence d'acquisition des donn�es
	 * @param minFreqCut fr�quence de coupure minimale
	 * @param beta facteur de contribution de la d�riv�e
	 * @param dFreqCut fr�quence de coupure minimale de la d�riv�e
	 */
	public OneEuroFilter(double freq, double minFreqCut, double beta, double dFreqCut)
	{
		this.freq = freq;
		this.minFreqCut = minFreqCut;
		this.beta = beta;
		this.dFreqCut = dFreqCut;

		x = new LowPassFilter();
		dx = new LowPassFilter();
	}

	/**
	 * Retourne la valeur alpha pour le filtre passe bas.
	 * @param freqCut fr�quence de coupure d�sir�e
	 * @return la valeur alpha pour le filtre passe bas
	 */
	public double getAlpha(double freqCut)
	{
		double sampleRate = 1.0 / freq;
		double tau = 1 / (2 * Math.PI * freqCut);
		return 1 / (1 + tau / sampleRate);
	}

	/**
	 * Retourne la valeur filtr�e.
	 * @param value valeur a filtrer
	 * @param v
	 * @param dv
	 * @return valeur filtr�e
	 */
	public double filter(double value)
	{
		if(lastTime != 0)
		{
			freq = 1000 / (System.currentTimeMillis() - lastTime);
		}

		lastTime = System.currentTimeMillis();

		double dvalue = x.isInitialized() ? (value - x.lastRawValue()) * freq : 0.0;
		double edvalue = dx.filter(dvalue, getAlpha(dFreqCut));

		return x.filter(value, getAlpha(minFreqCut + beta * Math.abs(edvalue)));
	}
}