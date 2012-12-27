package Controle;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import Modele.ModelePlay;
import Vue.VuePlay;

public class ControlePlay implements MouseListener, MouseMotionListener
{
	private VuePlay vuePlay;
	private ModelePlay modelePlay;
	int blockOver = 0;
	int blockSelected = 0;
	int offset = 0;

	public ControlePlay(VuePlay vuePlay, ModelePlay modelePlay)
	{
		this.vuePlay = vuePlay;
		this.modelePlay = modelePlay;
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		int pitch = modelePlay.getPitch();

		if(blockOver == 1) //pitch
		{
			blockSelected = 1;
			offset = (int)(181+(0.57*pitch)) - y;
		}
		else if(blockOver == 4) //player
		{
			blockSelected = 4;
			offset = 0;
			mouseDragged(e);
		}
		else if(303 <= x && x <= 326 && 124 <= y && y <= 246) //pitch
		{
			blockSelected = 1;
			offset = -3;
			mouseDragged(e);
			vuePlay.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}

	public void mouseReleased(MouseEvent e)
	{
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

	public void mouseMoved(MouseEvent e)
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
		else if(124 <= x && x <= 159 && 174 <= y && y <= 196) //Pause
		{
			if(blockOver != 2)
			{
				blockOver = 2; 
				vuePlay.setPause(1);
				vuePlay.setPlay(0);
				vuePlay.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		}
		else if(162 <= x && x <= 200 && 174 <= y && y <= 196) //Play
		{
			if(blockOver != 3)
			{
				blockOver = 3; 
				vuePlay.setPause(0);
				vuePlay.setPlay(1);
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
				vuePlay.setPause(0);
			}
			else if(blockOver == 3)
			{
				vuePlay.setPlay(0);
			}

			blockOver = 0;
			vuePlay.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public void mouseDragged(MouseEvent e)
	{
		if(blockSelected == 1)
		{
			int y = e.getY();
			modelePlay.setPitch((int)((y+offset-181)/0.57));
		}
		else if(blockSelected == 4)
		{
			int x = e.getX();
			int total = modelePlay.getTotal();
			modelePlay.setCurrent((int)(total*(x+1-19)/332));
		}
	}
}
