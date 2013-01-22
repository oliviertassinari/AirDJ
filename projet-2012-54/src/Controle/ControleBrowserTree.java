package Controle;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Timer;
import java.util.TimerTask;

import Modele.ModeleBrowserTree;
import Vue.VueBrowserTable;
import Vue.VueBrowserTree;

public class ControleBrowserTree implements MouseListener, MouseMotionListener, MouseWheelListener
{
	private VueBrowserTree vueBrowserTree;
	private VueBrowserTable vueBrowserTable;
	private int blockOver = 0;
	private int blockSelected = 0;
	private int offset = 0;
	private Timer timer = new Timer();

	public ControleBrowserTree(Controle controle)
	{
		vueBrowserTree = controle.getVue().getVueBrowser().getVueBrowserTree();
		vueBrowserTable = controle.getVue().getVueBrowser().getVueBrowserTable();
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
		if(blockOver == 11) //ScrollBar Up
		{
			vueBrowserTree.setScroll(vueBrowserTree.getScroll() - 16);

			timer = new Timer();
			timer.schedule(new TimerTask(){
				public void run()
				{
					vueBrowserTree.setScroll(vueBrowserTree.getScroll() - 16);
				}
			}, 500, 100);
		}
		else if(blockOver == 12) //ScrollBar Down
		{
			vueBrowserTree.setScroll(vueBrowserTree.getScroll() + 16);

			timer = new Timer();
			timer.schedule(new TimerTask(){
				public void run()
				{
					vueBrowserTree.setScroll(vueBrowserTree.getScroll() + 16);
				}
			}, 500, 100);
		}
		else if(blockOver == 13) //ScrollBar
		{
			int scroll = vueBrowserTree.getScroll();
			int length = vueBrowserTree.getLength();

			blockSelected = 1;
			offset = 123*(80+scroll)/(length*16) + 20 - e.getY();
		}
		else if(blockOver == 14) //ScrollBar All
		{
			blockSelected = 1;
			mouseDragged(e);
			vueBrowserTree.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		else //Tree
		{
			int x = e.getX();
			int y = e.getY();
			int scroll = vueBrowserTree.getScroll();
			int length = vueBrowserTree.getLength();
			int line = (int)((scroll+y)/16)+1;

			if(line <= length)
			{
				ModeleBrowserTree node = vueBrowserTree.getNode(line);

				System.out.println(node.getName());

				if(!node.isLeaf())
				{
					if(node.isExpanded())
					{
						node.setExpanded(false);
						vueBrowserTree.setLength(vueBrowserTree.getLength()-node.removeChildren());
					}
					else
					{
						node.setExpanded(true);

						if(node.getChildCount() == 0)
						{
							vueBrowserTree.buildNode(node);
						}
					}

					vueBrowserTree.paintRoot();
				}

				vueBrowserTable.paintNode(node);
			}
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		timer.cancel();
		blockSelected = 0;
		offset = 0;
		mouseMoved(e);
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
		mouseMoved(new MouseEvent(vueBrowserTree, 0, 0, 0, -1, -1, 0, false));
	}

	public void mouseMoved(MouseEvent e)
	{
		int length = vueBrowserTree.getLength();

		if(blockSelected == 0 && length*16 > 161)
		{
			int x = e.getX();
			int y = e.getY();
			int scroll = vueBrowserTree.getScroll();

			if(333 <= x && x <= 347)
			{
				if(y <= 15) //ScrollBar Up
				{
					if(blockOver != 11)
					{
						int[] state = {2, 1};
						blockOver = 11;
						vueBrowserTree.setScrollBarState(state);
						vueBrowserTree.setCursor(new Cursor(Cursor.HAND_CURSOR));
					}
				}
				else if(145 <= y) //ScrollBar Down
				{
					if(blockOver != 12)
					{
						int[] state = {1, 2};
						blockOver = 12;
						vueBrowserTree.setScrollBarState(state);
						vueBrowserTree.setCursor(new Cursor(Cursor.HAND_CURSOR));
					}
				}
				else if(20 + (int)(123*scroll/(16*length)) <= y && y <= 20 + (int)(123*(scroll+160)/(16*length)))
				{
					if(blockOver != 13) //ScrollBar
					{
						int[] state = {1, 1};
						blockOver = 13;
						vueBrowserTree.setScrollBarState(state);
						vueBrowserTree.setCursor(new Cursor(Cursor.HAND_CURSOR));
					}
				}
				else if(20 <= y && y <= 140)
				{
					if(blockOver != 14) //ScrollBar All
					{
						int[] state = {1, 1};
						blockOver = 14;
						vueBrowserTree.setScrollBarState(state);
						vueBrowserTree.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}
				else
				{
					if(blockOver != 1)
					{
						int[] state = {1, 1};
						blockOver = 1;
						vueBrowserTree.setScrollBarState(state);
						vueBrowserTree.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}
			}
			else if(blockOver != 0)
			{
				if(blockOver == 1 || blockOver == 11 || blockOver == 12 || blockOver == 13 || blockOver == 14)
				{
					int[] state = {0, 0};
					vueBrowserTree.setScrollBarState(state);
				}
	
				blockOver = 0;
				vueBrowserTree.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	public void mouseDragged(MouseEvent e)
	{
		if(blockSelected == 1) //ScrollBar
		{
			int y = e.getY();
			int length = vueBrowserTree.getLength();

			vueBrowserTree.setScroll((int)(((y+offset-20)*length*16)/123-80));
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e)
	{
		vueBrowserTree.setScroll(vueBrowserTree.getScroll() + 32*e.getWheelRotation());
	}
}
