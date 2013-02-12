package Controle;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import Modele.ModelePlay;
import Vue.VuePlay;

/**
 * Partie du controleur associée à ModelePlay
 */
public class ControlePlay implements MouseListener, MouseMotionListener, MouseWheelListener
{
	private VuePlay vuePlay;
	private ModelePlay modelePlay;
	int blockOver = 0;
	int blockSelected = 0;
	int offset = 0;

	/**
	 * constructeur
	 * @param vuePlay
	 * @param modelePlay
	 */
	public ControlePlay(VuePlay vuePlay, ModelePlay modelePlay)
	{
		this.vuePlay = vuePlay;
		this.modelePlay = modelePlay;
	}

	public void mouseClicked(MouseEvent e)
	{
	}
	

	/**
	 * gestion de l'appui de souris
	 * @param e
	 */
	public void mousePressed(MouseEvent e)
	{
		if(blockOver == 1) //pitch
		{
			if(e.getClickCount() > 1)
			{
				modelePlay.setPitch(0);
			}
			else
			{
				blockSelected = 1;
				offset = (int)(181+(0.57*modelePlay.getPitch())) - e.getY();
			}
		}
		else if(blockOver == 11) //pitch All
		{
			blockSelected = 1;
			offset = -3;
			mouseDragged(e);
			vuePlay.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		else if(blockOver == 2) //Pause
		{
			modelePlay.setButtonPause("press");
			modelePlay.setPause();
		}
		else if(blockOver == 3) //Play
		{
			modelePlay.setButtonPlay("press");
			modelePlay.setPlay();
		}
		else if(blockOver == 4) //player
		{
			blockSelected = 4;
			offset = 0;
			mouseDragged(e);
		}
	}

	/**
	 * gestion de l'évènement relever la souris
	 * @param e
	 */
	public void mouseReleased(MouseEvent e)
	{
		if(blockOver == 2)
		{
			modelePlay.setButtonPause("release");
		}
		else if(blockOver == 3)
		{
			modelePlay.setButtonPlay("release");
		}

		blockSelected = 0;
		offset = 0;
		mouseMoved(e);
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	/**
	 * gestion d'un mouvement
	 * @param e
	 */
	public void mouseMoved(MouseEvent e)
	{
		if(blockSelected == 0)
		{
			int x = e.getX();
			int y = e.getY();
			int pitch = modelePlay.getPitch();
	
			if(303 <= x && x <= 326 && 181+(0.57*pitch) <= y && y <= 189+(0.57*pitch)) //Pitch
			{
				if(blockOver != 1)
				{
					blockOver = 1;
					vuePlay.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
			else if(303 <= x && x <= 326 && 124 <= y && y <= 246) //Pitch All
			{
				if(blockOver != 11)
				{
					blockOver = 11;
					vuePlay.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
			else if(124 <= x && x <= 159 && 174 <= y && y <= 196) //Pause
			{
				if(blockOver != 2)
				{
					blockOver = 2; 
					modelePlay.setButtonPause("over");
					modelePlay.setButtonPlay("out");
					vuePlay.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
			else if(162 <= x && x <= 200 && 174 <= y && y <= 196) //Play
			{
				if(blockOver != 3)
				{
					blockOver = 3; 
					modelePlay.setButtonPause("out");
					modelePlay.setButtonPlay("over");
					vuePlay.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
			else if(19 <= x && x <= 350 && 90 <= y && y <= 99 && modelePlay.getTotal() != 0) //Player
			{
				if(blockOver != 4)
				{
					blockOver = 4;
					vuePlay.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
			else if(blockOver != 0)
			{
				if(blockOver == 2)
				{
					modelePlay.setButtonPause("out");
				}
				else if(blockOver == 3)
				{
					modelePlay.setButtonPlay("out");
				}
	
				blockOver = 0;
				vuePlay.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	/**
	 * gestion d'un glissé
	 * @param e
	 */
	public void mouseDragged(MouseEvent e)
	{
		if(blockSelected == 1) //Pitch
		{
			int y = e.getY();
			modelePlay.setPitch((int)((y+offset-181)/0.57));
		}
		else if(blockSelected == 4) //Player
		{
			int x = e.getX();
			int total = modelePlay.getTotal();
			modelePlay.setCurrent((int)(total*(x+1-19)/332));
		}
	}

	/**
	 * gestion d'une rotation de la molette
	 */
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		if(blockOver == 1 || blockOver == 11) //Pitch ou Pitch All
		{
			modelePlay.setPitch(modelePlay.getPitch()+5*e.getWheelRotation());
		}
	}
}
