
package Controle;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Timer;
import java.util.TimerTask;

import Modele.Modele;
import Vue.VueBrowserTable;

public class ControleBrowserTable implements MouseListener, MouseMotionListener, MouseWheelListener
{
	private VueBrowserTable vueBrowserTable;
	private Modele modele;
	private int blockOver = 0;
	private int blockSelected = 0;
	private int offset = 0;
	private Timer timer = new Timer();

	/**
	 * constructeur
	 * @param ControleBrowserTable
	 */
	public ControleBrowserTable(Controle controle)
	{
		vueBrowserTable = controle.getVue().getVueBrowser().getVueBrowserTable();
		modele = controle.getModele();
	}

	/**
	 * controle de l'évènement mouseClicked
	 * @param MouseEvent
	 */
	public void mouseClicked(MouseEvent e)
	{
	}

	/**
	 * controle de l'évènement mousePressed
	 * @param MouseEvent
	 */
	public void mousePressed(MouseEvent e)
	{
		if(blockOver == 11) // ScrollBar Up
		{
			vueBrowserTable.setScroll(vueBrowserTable.getScroll() - 25);

			timer = new Timer();
			timer.schedule(new TimerTask()
			{
				public void run()
				{
					vueBrowserTable.setScroll(vueBrowserTable.getScroll() - 25);
				}
			}, 500, 100);
		}
		else if(blockOver == 12) // ScrollBar Down
		{
			vueBrowserTable.setScroll(vueBrowserTable.getScroll() + 25);

			timer = new Timer();
			timer.schedule(new TimerTask()
			{
				public void run()
				{
					vueBrowserTable.setScroll(vueBrowserTable.getScroll() + 25);
				}
			}, 500, 100);
		}
		else if(blockOver == 13) // ScrollBar
		{
			int scroll = vueBrowserTable.getScroll();
			int length = vueBrowserTable.getLength();

			blockSelected = 1;
			offset = 123 * (80 + scroll) / (length * 16) + 20 - e.getY();
		}
		else if(blockOver == 14) // ScrollBar All
		{
			blockSelected = 1;
			mouseDragged(e);
			vueBrowserTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		else
		// Table
		{
			int x = e.getX();
			int y = e.getY();
			int scroll = vueBrowserTable.getScroll();
			int length = vueBrowserTable.getLength();
			int line = (int)((scroll + y) / 25);

			if(line < length)
			{
				if(4 <= x && x <= 23)
				{
					modele.getModelePlayP1().setFilePath(vueBrowserTable.getFilePath(line));
				}
				else if(26 <= x && x <= 45)
				{
					modele.getModelePlayP2().setFilePath(vueBrowserTable.getFilePath(line));
				}
			}
		}
	}

	/**
	 * controle de l'évènement mouseReleased
	 * @param MouseEvent
	 */
	public void mouseReleased(MouseEvent e)
	{
		timer.cancel();
		blockSelected = 0;
		offset = 0;
		mouseMoved(e);
	}

	/**
	 * controle de l'évènement mouseEntered
	 * @param MouseEvent
	 */
	public void mouseEntered(MouseEvent e)
	{
	}

	/**
	 * controle de l'évènement mouseExited
	 * @param MouseEvent
	 */
	public void mouseExited(MouseEvent e)
	{
		mouseMoved(new MouseEvent(vueBrowserTable, 0, 0, 0, -1, -1, 0, false));
	}

	/**
	 * controle de l'évènement mouseMoved
	 * @param MouseEvent
	 */
	public void mouseMoved(MouseEvent e)
	{
		int length = vueBrowserTable.getLength();

		if(blockSelected == 0)
		{
			int x = e.getX();
			int y = e.getY();
			int scroll = vueBrowserTable.getScroll();

			if(628 <= x && x <= 644 && length * 25 > 161)
			{
				if(y <= 15) // ScrollBar Up
				{
					if(blockOver != 11)
					{
						int[] state = {2, 1};
						blockOver = 11;
						vueBrowserTable.setScrollBarState(state);
						vueBrowserTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
					}
				}
				else if(145 <= y) // ScrollBar Down
				{
					if(blockOver != 12)
					{
						int[] state = {1, 2};
						blockOver = 12;
						vueBrowserTable.setScrollBarState(state);
						vueBrowserTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
					}
				}
				else if(20 + (int)(123 * scroll / (25 * length)) <= y && y <= 20 + (int)(123 * (scroll + 160) / (25 * length)))
				{
					if(blockOver != 13) // ScrollBar
					{
						int[] state = {1, 1};
						blockOver = 13;
						vueBrowserTable.setScrollBarState(state);
						vueBrowserTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
					}
				}
				else if(20 <= y && y <= 140)
				{
					if(blockOver != 14) // ScrollBar All
					{
						int[] state = {1, 1};
						blockOver = 14;
						vueBrowserTable.setScrollBarState(state);
						vueBrowserTable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}
				else
				{
					if(blockOver != 1)
					{
						int[] state = {1, 1};
						blockOver = 1;
						vueBrowserTable.setScrollBarState(state);
						vueBrowserTable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}
			}
			else if(blockOver != 0)
			{
				if(blockOver == 1 || blockOver == 11 || blockOver == 12 || blockOver == 13 || blockOver == 14)
				{
					int[] state = {0, 0};
					vueBrowserTable.setScrollBarState(state);
				}

				blockOver = 0;
				vueBrowserTable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			else
			{
				int line = (int)((scroll + y) / 25);

				if(line < length)
				{
					if(4 <= x && x <= 23)
					{
						int[] state = {line, 0};
						vueBrowserTable.setTableOver(state);
						vueBrowserTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
					}
					else if(26 <= x && x <= 45)
					{
						int[] state = {line, 1};
						vueBrowserTable.setTableOver(state);
						vueBrowserTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
					}
					else
					{
						int[] state = {-1, -1};
						vueBrowserTable.setTableOver(state);
						vueBrowserTable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}
				else
				{
					int[] state = {-1, -1};
					vueBrowserTable.setTableOver(state);
					vueBrowserTable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		}
	}

	/**
	 * controle de l'évènement mouseDragged
	 * @param MouseEvent
	 */
	public void mouseDragged(MouseEvent e)
	{
		if(blockSelected == 1) // ScrollBar
		{
			int y = e.getY();
			int length = vueBrowserTable.getLength();

			vueBrowserTable.setScroll((int)(((y + offset - 20) * length * 25) / 123 - 80));
		}
	}

	/**
	 * controle de l'évènement mouseWheelMoved
	 * @param MouseEvent
	 */
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		vueBrowserTable.setScroll(vueBrowserTable.getScroll() + 50 * e.getWheelRotation());
	}
}
