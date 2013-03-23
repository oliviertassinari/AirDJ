
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
	/**
	 * VueCrossFinder contrôlée
	 */
	private VueCrossfinder vueCrossfinder;
	/**
	 * ModeleCrossFinder contrôlé
	 */
	private ModeleCrossfinder modeleCrossfinder;
	/**
	 * attribut désignant
	 */
	int blockOver = 0;
	int blockSelected = 0;
	int offset = 0;
	int offset2 = 0;

	/**
	 * @param Controle
	 */
	public ControleCrossfinder(Controle controle)
	{
		vueCrossfinder = controle.getVue().getVueCrossfinder();
		modeleCrossfinder = controle.getModele().getModeleCrossfinder();
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	/**
	 * Contrôle de l'évènement appui sur la souris
	 * 
	 * @param MouseEvent
	 */

	public void mousePressed(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		int crossfinder = modeleCrossfinder.getCrossfinder();
		int volumeP1 = modeleCrossfinder.getVolumeP1();
		int volumeP2 = modeleCrossfinder.getVolumeP2();

		if(blockOver == 1) // volumeP1
		{
			blockSelected = 1;
			offset = (int)(y - (148 - volumeP1 * 0.78));
		}
		else if(blockOver == 11) // volumeP1 All
		{
			blockSelected = 1;
			offset = 2;
			mouseDragged(e);
			vueCrossfinder.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		else if(blockOver == 2) // volumeP2
		{
			blockSelected = 2;
			offset = (int)(y - (148 - volumeP2 * 0.78));
		}
		else if(blockOver == 21) // volmeP2 All
		{
			blockSelected = 2;
			offset = 2;
			mouseDragged(e);
			vueCrossfinder.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		else if(blockOver == 3) // crossfinder
		{
			if(e.getClickCount() > 1)
			{
				modeleCrossfinder.setCrossfinder(0);
			}
			else
			{
				blockSelected = 3;
				offset = (int)((116 + crossfinder * 0.5) - x);
			}
		}
		else if(blockOver == 31) // crossfinder All
		{
			blockSelected = 3;
			offset = -7;
			mouseDragged(e);
			vueCrossfinder.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		else if(blockOver == 411) // Equalizer P1 Mid
		{
			if(e.getClickCount() > 1)
			{
				modeleCrossfinder.setEqualizerP1(0, 0);
			}
			else
			{
				offset = e.getX();
				offset2 = e.getY();
				blockSelected = 411;
				modeleCrossfinder.setEqualizerP1(2, 2);
			}
		}
		else if(blockOver == 412) // Equalizer P1 Bass
		{
			if(e.getClickCount() > 1)
			{
				modeleCrossfinder.setEqualizerP1(1, 0);
			}
			else
			{
				offset = e.getX();
				offset2 = e.getY();
				blockSelected = 412;
				modeleCrossfinder.setEqualizerP1(3, 2);
			}
		}
		else if(blockOver == 421) // Equalizer P2 Mid
		{
			if(e.getClickCount() > 1)
			{
				modeleCrossfinder.setEqualizerP2(0, 0);
			}
			else
			{
				offset = e.getX();
				offset2 = e.getY();
				blockSelected = 421;
				modeleCrossfinder.setEqualizerP2(2, 2);
			}
		}
		else if(blockOver == 422) // Equalizer P2 Bass
		{
			if(e.getClickCount() > 1)
			{
				modeleCrossfinder.setEqualizerP2(1, 0);
			}
			else
			{
				offset = e.getX();
				offset2 = e.getY();
				blockSelected = 422;
				modeleCrossfinder.setEqualizerP2(3, 2);
			}
		}
	}

	/**
	 * Contrôle de l'évènement mouseReleased
	 * 
	 * @param MouseEvent
	 */
	public void mouseReleased(MouseEvent e)
	{
		if(blockSelected == 411) // Equalizer P1 Mid
		{
			modeleCrossfinder.setEqualizerP1(2, 1);
		}
		else if(blockSelected == 412) // Equalizer P1 Bass
		{
			modeleCrossfinder.setEqualizerP1(3, 1);
		}
		else if(blockSelected == 421) // Equalizer P2 Mid
		{
			modeleCrossfinder.setEqualizerP2(2, 1);
		}
		else if(blockSelected == 422) // Equalizer P2 Bass
		{
			modeleCrossfinder.setEqualizerP2(3, 1);
		}

		blockSelected = 0;
		offset = 0;
		offset2 = 0;
		mouseMoved(e);
	}

	/**
	 * controle de l'évènement mouseEntered
	 * 
	 * @param MouseEvent
	 */
	public void mouseEntered(MouseEvent e)
	{
	}

	/**
	 * controle de l'évènement mouseExited
	 * 
	 * @param MouseEvent
	 */
	public void mouseExited(MouseEvent e)
	{
	}

	/**
	 * controle de l'évènement mouseMoved
	 * 
	 * @param MouseEvent
	 */
	public void mouseMoved(MouseEvent e)
	{
		if(blockSelected == 0)
		{
			int x = e.getX();
			int y = e.getY();
			int crossfinder = modeleCrossfinder.getCrossfinder();
			int volumeP1 = modeleCrossfinder.getVolumeP1();
			int volumeP2 = modeleCrossfinder.getVolumeP2();

			if(94 <= x && x <= 116 && (int)(148 - volumeP1 * 0.78) <= y && y <= (int)(153 - volumeP1 * 0.78))
			{
				if(blockOver != 1) // volumeP1
				{
					blockOver = 1;
					vueCrossfinder.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
			else if(94 <= x && x <= 116 && 70 <= y && y <= 153)
			{
				if(blockOver != 11) // volumeP1 All
				{
					blockOver = 11;
					vueCrossfinder.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
			else if(134 <= x && x <= 156 && (int)(148 - volumeP2 * 0.78) <= y && y <= (int)(153 - volumeP2 * 0.78))
			{
				if(blockOver != 2) // volumeP2
				{
					blockOver = 2;
					vueCrossfinder.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
			else if(134 <= x && x <= 156 && 70 <= y && y <= 153)
			{
				if(blockOver != 21) // volumeP2 All
				{
					blockOver = 21;
					vueCrossfinder.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
			else if(238 <= y && y <= 272 && (int)(116 + crossfinder * 0.5) <= x && x <= (int)(130 + crossfinder * 0.5))
			{
				if(blockOver != 3) // crossfinder
				{
					blockOver = 3;
					vueCrossfinder.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
			else if(238 <= y && y <= 272 && 66 <= x && x <= 180)
			{
				if(blockOver != 31) // crossfinder All
				{
					blockOver = 31;
					vueCrossfinder.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
			else if(7 <= x && x <= 45 && 50 <= y && y <= 88)
			{
				if(blockOver != 411) // Equializer P1 Mid
				{
					blockOver = 411;
					vueCrossfinder.setCursor(new Cursor(Cursor.HAND_CURSOR));
					modeleCrossfinder.setEqualizerP1(2, 1);
				}
			}
			else if(7 <= x && x <= 45 && 110 <= y && y <= 148)
			{
				if(blockOver != 412) // Equializer P1 Bass
				{
					blockOver = 412;
					vueCrossfinder.setCursor(new Cursor(Cursor.HAND_CURSOR));
					modeleCrossfinder.setEqualizerP1(3, 1);
				}
			}
			else if(203 <= x && x <= 241 && 50 <= y && y <= 88)
			{
				if(blockOver != 421) // Equializer P2 Mid
				{
					blockOver = 421;
					vueCrossfinder.setCursor(new Cursor(Cursor.HAND_CURSOR));
					modeleCrossfinder.setEqualizerP2(2, 1);
				}
			}
			else if(203 <= x && x <= 241 && 110 <= y && y <= 148)
			{
				if(blockOver != 422) // Equializer P2 Bass
				{
					blockOver = 422;
					vueCrossfinder.setCursor(new Cursor(Cursor.HAND_CURSOR));
					modeleCrossfinder.setEqualizerP2(3, 1);
				}
			}
			else if(blockOver != 0)
			{
				if(blockOver == 411)
				{
					modeleCrossfinder.setEqualizerP1(2, 0);
				}
				else if(blockOver == 412)
				{
					modeleCrossfinder.setEqualizerP1(3, 0);
				}
				else if(blockOver == 421)
				{
					modeleCrossfinder.setEqualizerP2(2, 0);
				}
				else if(blockOver == 422)
				{
					modeleCrossfinder.setEqualizerP2(3, 0);
				}

				blockOver = 0;
				vueCrossfinder.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	/**
	 * controle de l'évènement mouseDragged
	 * 
	 * @param MouseEvent
	 */
	public void mouseDragged(MouseEvent e)
	{
		if(blockSelected == 1)
		{
			int y = e.getY();
			modeleCrossfinder.setVolumeP1((int)((148 - y + offset) / 0.78));
		}
		else if(blockSelected == 2)
		{
			int y = e.getY();
			modeleCrossfinder.setVolumeP2((int)((148 - y + offset) / 0.78));
		}
		else if(blockSelected == 3) // crossfinder
		{
			int x = e.getX();

			int crossfinder = modeleCrossfinder.getCrossfinder();
			int value = 2 * (x + offset - 116);

			if(-20 < value && value < -15 && value > crossfinder)
			{
				modeleCrossfinder.setCrossfinder(0);
			}
			else
			{
				modeleCrossfinder.setCrossfinder(2 * (x + offset - 116));
			}
		}
		else if(blockSelected == 411)
		{
			modeleCrossfinder.setEqualizerP1(0, modeleCrossfinder.getEqualizerP1()[0] + (e.getX() - offset) + (offset2 - e.getY()));

			offset = e.getX();
			offset2 = e.getY();
		}
		else if(blockSelected == 412)
		{
			modeleCrossfinder.setEqualizerP1(1, modeleCrossfinder.getEqualizerP1()[1] + (e.getX() - offset) + (offset2 - e.getY()));

			offset = e.getX();
			offset2 = e.getY();
		}
		else if(blockSelected == 421)
		{
			modeleCrossfinder.setEqualizerP2(0, modeleCrossfinder.getEqualizerP2()[0] + (e.getX() - offset) + (offset2 - e.getY()));

			offset = e.getX();
			offset2 = e.getY();
		}
		else if(blockSelected == 422)
		{
			modeleCrossfinder.setEqualizerP2(1, modeleCrossfinder.getEqualizerP2()[1] + (e.getX() - offset) + (offset2 - e.getY()));

			offset = e.getX();
			offset2 = e.getY();
		}
	}

	/**
	 * controle de l'évènement mouseWheelMoved
	 * 
	 * @param MouseEvent
	 */
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		if(blockOver == 1 || blockOver == 11)
		{
			modeleCrossfinder.setVolumeP1(modeleCrossfinder.getVolumeP1() - 7 * e.getWheelRotation());
		}
		else if(blockOver == 2 || blockOver == 21)
		{
			modeleCrossfinder.setVolumeP2(modeleCrossfinder.getVolumeP2() - 7 * e.getWheelRotation());
		}
		else if(blockOver == 3 || blockOver == 31)
		{
			modeleCrossfinder.setCrossfinder(modeleCrossfinder.getCrossfinder() - 8 * e.getWheelRotation());
		}
		else if(blockOver == 411)
		{
			modeleCrossfinder.setEqualizerP1(0, modeleCrossfinder.getEqualizerP1()[0] - 5*e.getWheelRotation());
		}
		else if(blockOver == 412)
		{
			modeleCrossfinder.setEqualizerP1(1, modeleCrossfinder.getEqualizerP1()[1] - 5*e.getWheelRotation());
		}
		else if(blockOver == 421)
		{
			modeleCrossfinder.setEqualizerP2(0, modeleCrossfinder.getEqualizerP2()[0] - 5*e.getWheelRotation());
		}
		else if(blockOver == 422)
		{
			modeleCrossfinder.setEqualizerP2(1, modeleCrossfinder.getEqualizerP2()[1] - 5*e.getWheelRotation());
		}
	}
}
