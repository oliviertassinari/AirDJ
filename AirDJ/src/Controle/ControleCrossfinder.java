package Controle;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import Modele.ModeleCrossfinder;
import Vue.VueCrossfinder;

public class ControleCrossfinder implements MouseListener, MouseMotionListener
{
	private VueCrossfinder vueCrossfinder ;
	private ModeleCrossfinder modeleCrossfinder;
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

		if(94 <= x && x <= 116 && (int)(148-volumeP1*0.78) <= y && y <= (int)(148-volumeP1*0.78) + 5)
		{
			blockSelected = 1; //volumeP1
			offset = (int)(y - (148-volumeP1*0.78));
			vueCrossfinder.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR ));
		}
		else if(134 <= x && x <= 156 && (int)(148-volumeP2*0.78) <= y && y <= (int)(148-volumeP2*0.78) + 5)
		{
			blockSelected = 2; //volumeP2
			offset = (int)(y - (148-volumeP2*0.78));
			vueCrossfinder.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
		}
		else if(238 <= y && y <= 272 && (int)(116+crossfinder*0.5) <= x && x <= (int)(116+crossfinder*0.5) + 14)
		{
			blockSelected = 3; //crossfinder
			offset = (int)((116+crossfinder*0.5) - x);
			vueCrossfinder.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
		}
		else
		{
			blockSelected = 0;
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		vueCrossfinder.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mouseMoved(MouseEvent e)
	{
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
}
