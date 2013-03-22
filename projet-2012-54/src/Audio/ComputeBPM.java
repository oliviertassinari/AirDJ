package Audio;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import Modele.ModelePlay;

public class ComputeBPM implements Runnable
{
	private Thread computeBPM;
	private Player player;
	private ModelePlay modelePlay;

	/**
	 * length (s) of music which will be analysed
	 */
	private int length=5;
	
	/**
	 * offset size
	 */
	private int sizeBis = 256;

	/**
	 * size of array before FFT
	 */
	private int size1 = sizeBis*4*40*length;

	/**
	 * 
	 */
	private int Nfft = 64;
	
	
	/**
	 * size of array after FFT
	 */
	private int size2 = 4*40*length;
	
	public ComputeBPM(Player player, ModelePlay modelePlay)
	{
		this.player = player;
		this.modelePlay = modelePlay;
		
		computeBPM = new Thread(this, "computeBPM");
		computeBPM.start();
	}

	
	/**
	 * @param fileName of the file we want the samples
	 * @param offset position (s)
	 * @return double array of the samples
	 */
	public double[] getSample(File file, int offset)
	{
		byte[] bytes = new byte[4*size1];
		double[] output = new double[size1];
        AudioFormat af = null;
		
		try
		{
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			af = ais.getFormat();
			ais.skip(offset*44100*4);
			ais.read(bytes, 0, bytes.length);
			ais.close();
		}
		catch(UnsupportedAudioFileException e)
		{e.printStackTrace();}
		catch(IOException e)
		{e.printStackTrace();}
		
		if(af.isBigEndian())
		{
			for (int i = 0; i<size1-4; i++) 
			{
				output[i] = (((bytes[4*i + 0] << 8) | (bytes[4*i + 1] & 0xFF)) + ((bytes[4*i + 2] << 8) | (bytes[4*i + 3] & 0xFF)))/2;
			}
		}
		else
		{
			for (int i = 0; i<size1-4; i++) 
			{
				output[i] = (((bytes[4*i + 0] & 0xFF) | (bytes[4*i + 1] << 8)) + ((bytes[4*i + 2] & 0xFF) | (bytes[4*i + 3] << 8)))/2;
			}
		}
		
		return output;
	}
	
	
	private double getFormatedBPM(double value)
	{
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2); 

		Number n = null;
		NumberFormat formater3 = new DecimalFormat(".##");
		String str = formater3.format(value);
		try
		{
			n = formater3.parse(str);
		}
		catch(ParseException pe)
		{
			System.out.println("parse : " + pe);
		}
 
		return n.doubleValue();
	}

	public void run()
	{
		Filler filler = player.getFiller();

		double[] data = getSample(player.getFile(), 10);
		
		double tmp1 = BPM(data);

		double tmp2 = 60/(tmp1*256/player.getFrameRate());
		if (tmp2 >= 180) tmp2/=2;
		if (tmp2 < 90) tmp2*=2;

		tmp2 = getFormatedBPM(tmp2);

		System.out.println(tmp2);

		player.computeBPMEnd();
		modelePlay.computeBPMEnd(tmp2);
	}
	
	/**
	 * 
	 * @param sequence we want to analyse
	 * @return period in 256*sample
	 */
	public int BPM(double[] sequence) 
	{
		double[] outputB = new double[size2];
		double[] output = new double[size2];
		double[][] outputBis = new double[Nfft/2][size2];
		double[][] outputTer = new double[Nfft/2][size2];
		double[] tmp = new double[2];
		
		/** Creation de la reponse impulsionelle du filtre passe bas **/
		int orderLowPass = 34;
		double[] ri1 = new double[orderLowPass+1];
		for (int n = 0; n <= orderLowPass; n++) ri1[orderLowPass-n] = Math.pow(Math.sin(Math.PI*n/(2*orderLowPass+1)), 2);
		
		/** Creation de la reponse impulsionelle du differentiateur **/
		double[] ri2 = {-0.017725858944917, 0.070914138578986, -0.110519250417914, -0.021350533927998, 0.677879052570720,
				0, -0.677879052570720, 0.021350533927998, 0.110519250417914, -0.070914138578986, 0.017725858944917};
		
		
		/** On commence le travaille sur chaque bande de frequence **/
		for (int f = 0; f < Nfft/2; f++)
		{
			/************ FFT + Module² ************/
			//double a = System.currentTimeMillis();
			double cst = 2*Math.PI/4/sizeBis*44100/Nfft*f;
			for (int m = 0; m < size2-4; m++)
			{
				for (int n = 0; n < sizeBis*4; n++)
				{	
					tmp[0] += sequence[n+m*sizeBis]*Math.cos((float)(n*cst));
					tmp[1] += sequence[n+m*sizeBis]*xsin((float)(n*cst));
				}
				outputBis[f][m] = tmp[0]*tmp[0] + tmp[1]*tmp[1];
				tmp[0] = 0;
				tmp[1] = 0;
			}
			//System.out.println(System.currentTimeMillis()-a);
			/************ LowPassFilter ************/
			for (int m = 0; m < size2; m++)
			{
				for (int i = 0; i <= Math.min(orderLowPass,m); i++) outputTer[f][m] += ri1[i]*outputBis[f][m-i];
			}
			
			/************ Differentiator ************/
			for (int m = 0; m < size2; m++)
			{
				outputBis[f][m]=0;
				for (int i = 0; i <= Math.min(10,m); i++) outputBis[f][m] += ri2[i]*outputTer[f][m-i];
			}
			
			/************ Positive ************/
			for (int m = 0; m < size2; m++)
			{
				outputBis[f][m] = Math.max(outputBis[f][m], 0);
			}
			
			/************ LowPassFilter ************/
			for (int m = 0; m < size2; m++)
			{
				outputTer[f][m]=0;
				for (int i = 0; i <= Math.min(orderLowPass,m); i++) outputTer[f][m] += ri1[i]*outputBis[f][m-i];
			}
		}
		
		/** On additionne le resultat de chaque bande **/
		for (int m = 0; m < size2; m++)
		{
			for (int f = 0; f < Nfft/2; f++)
			{
				outputB[m] += (1-f/Nfft)*outputTer[f][m];
			}
		}
		
		/************ LowPassFilter ************/
		for (int m = 0; m < size2; m++)
		{
			for (int i = 0; i <= Math.min(orderLowPass,m); i++) output[m] += ri1[i]*outputB[m-i];
		}
		
		/************ Auto correlation ************/
		int min = 50;
		int max =200;
		int length = output.length;
		double[] tmpB = new double[max];
		for (int j = 0; j < length; j++) tmpB[0] += output[j]*output[j];
		
		int indiceMax = 0;
		double Max = 0; 
		
		for (int i = min; i <max; i++)
		{
			for (int j = 0; j < length-i; j++) tmpB[i] += output[j]*output[j+i];
			if (tmpB[i]/tmpB[0] > Max)
			{
				Max = tmpB[i]/tmpB[0];
				indiceMax = i;
			}
		}
		return indiceMax;
	}

	public double xsin(double x)
	{
		double x1 = x % (2 * 3.14);

		if(x1 > 3.1415)
		{
			x1 -= 2*3.14;
 		}

		double x2 = x1 * x1;
		double res = x1*(1+x2*0.166666*(-1+x2*0.05*(1-0.003689*x2)));

		//System.out.println(Math.sin((float)(x1))-res2);
/*
		if(Math.abs(Math.sin((float)(x1))-res2) > 1)
		{
			System.out.println(res3);
		}
		*/
		return res;
	}

	public double xcos(double x)
	{
		double x2 = x * x;
		
		return ((((.00015148419 * x2
	             - .00467376557) * x2
	            + .07968967928) * x2
	           - .64596371106) * x2
	          + 1.57079631847) * x;
	}

	public void interrupt()
	{
		computeBPM.interrupt();
	}

}
