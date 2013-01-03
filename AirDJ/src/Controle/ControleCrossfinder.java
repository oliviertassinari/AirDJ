package Controle;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import Modele.ModeleCrossfinder;
import Vue.VueCrossfinder;

public class ControleCrossfinder implements MouseListener, MouseMotionListener, MouseWheelListener
{
	private VueCrossfinder vueCrossfinder ;
	private ModeleCrossfinder modeleCrossfinder;
	int blockOver = 0;
	int blockSelected = 0;
	int offset = 0;

	public ControleCrossfinder(Controle controle)
	{
		vueCrossfinder = controle.getVue().getVueCrossfinder();
		modeleCrossfinder = controle.getModele().getModeleCrossfinder();
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		int crossfinder = modeleCrossfinder.getCrossfinder();
		int volumeP1 = modeleCrossfinder.getVolumeP1();
		int volumeP2 = modeleCrossfinder.getVolumeP2();

		if(blockOver == 1) //volumeP1
		{
			blockSelected = 1;
			offset = (int)(y - (148-volumeP1*0.78));
		}
		else if(blockOver == 11) //volumeP1 All
		{
			blockSelected = 1;
			offset = 2;
			mouseDragged(e);
			vueCrossfinder.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		else if(blockOver == 2) //volumeP2
		{
			blockSelected = 2;
			offset = (int)(y - (148-volumeP2*0.78));
		}
		else if(blockOver == 21) //volmeP2 All
		{
			blockSelected = 2;
			offset = 2;
			mouseDragged(e);
			vueCrossfinder.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		else if(blockOver == 3) //crossfinder
		{
			blockSelected = 3;
			offset = (int)((116+crossfinder*0.5) - x);
		}
		else if(blockOver == 31) //crossfinder All
		{
			blockSelected = 3;
			offset = -7;
			mouseDragged(e);
			vueCrossfinder.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
		if(blockSelected == 0)
		{
			int x = e.getX();
			int y = e.getY();
			int crossfinder = modeleCrossfinder.getCrossfinder();
			int volumeP1 = modeleCrossfinder.getVolumeP1();
			int volumeP2 = modeleCrossfinder.getVolumeP2();
	
			if(94 <= x && x <= 116 && (int)(148-volumeP1*0.78) <= y && y <= (int)(153-volumeP1*0.78))
			{
				if(blockOver != 1) //volumeP1
				{
					blockOver = 1;
					vueCrossfinder.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
			else if(94 <= x && x <= 116 && 70 <= y && y <= 153)
			{
				if(blockOver != 11) //volumeP1 All
				{
					blockOver = 11;
					vueCrossfinder.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
			else if(134 <= x && x <= 156 && (int)(148-volumeP2*0.78) <= y && y <= (int)(153-volumeP2*0.78))
			{
				if(blockOver != 2) //volumeP2
				{
					blockOver = 2;
					vueCrossfinder.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
			else if(134 <= x && x <= 156 && 70 <= y && y <= 153)
			{
				if(blockOver != 21) //volumeP2 All
				{
					blockOver = 21;
					vueCrossfinder.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
			else if(238 <= y && y <= 272 && (int)(116+crossfinder*0.5) <= x && x <= (int)(130+crossfinder*0.5))
			{
				if(blockOver != 3) //crossfinder
				{
					blockOver = 3;
					vueCrossfinder.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
			else if(238 <= y && y <= 272 && 66 <= x && x <= 180)
			{
				if(blockOver != 31) //crossfinder All
				{
					blockOver = 31;
					vueCrossfinder.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
			else if(blockOver != 0)
			{
				blockOver = 0;
				vueCrossfinder.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	public void mouseDragged(MouseEvent e)
	{
		if(blockSelected == 1)
		{
			int y = e.getY();
			modeleCrossfinder.setVolumeP1((int)((148-y+offset)/0.78));
		}
		else if(blockSelected == 2)
		{
			int y = e.getY();
			modeleCrossfinder.setVolumeP2((int)((148-y+offset)/0.78));
		}
		else if(blockSelected == 3)
		{
			int x = e.getX();
			modeleCrossfinder.setCrossfinder(2*(x+offset-116));
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e)
	{
		if(blockOver == 1 || blockOver == 11)
		{
			modeleCrossfinder.setVolumeP1(modeleCrossfinder.getVolumeP1()-7*e.getWheelRotation());
		}
		else if(blockOver == 2 || blockOver == 21)
		{
			modeleCrossfinder.setVolumeP2(modeleCrossfinder.getVolumeP2()-7*e.getWheelRotation());
		}
		else if(blockOver == 3 || blockOver == 31)
		{
			modeleCrossfinder.setCrossfinder(modeleCrossfinder.getCrossfinder()-8*e.getWheelRotation());
		}
	}
}
