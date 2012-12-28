package Vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import Modele.ModeleBrowserTree;

public class VueBrowserTree extends JPanel
{
	private File[] listRouts;
	private Image imageTree;
	private Image imageScrollBar;
	private BufferedImage imageBackground;
	private int x = 0;
	private int y = 0;
	private int length = 0;
	private int scroll = 0;
	private int[] scrollBarState = {0, 0};

	public VueBrowserTree()
	{
		listRouts = File.listRoots();
		imageTree = new ImageIcon("image/tree.png").getImage();
		imageScrollBar = new ImageIcon("image/scrollBar.png").getImage();

		setBackground(Color.BLUE);
		setPreferredSize(new Dimension(350, 163));
		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x4E4C4B)));

		//Background
		ModeleBrowserTree root = new ModeleBrowserTree("root", "Ordinateur", 0);
		length = 1;
		buildNode(root);

		if(length*16 > 161)
		{
			imageBackground = new BufferedImage(334, length*16, BufferedImage.TRANSLUCENT);
		}
		else
		{
			imageBackground = new BufferedImage(334, 161, BufferedImage.TRANSLUCENT);
		}

        Graphics gi = imageBackground.getGraphics();
		gi.setColor(Color.BLACK);
		gi.fillRect(0, 0, imageBackground.getWidth(), imageBackground.getHeight());

	    gi.setFont(new Font("sansserif", Font.BOLD, 11));
		gi.setColor(Color.WHITE);

        paintNode(root, gi);

        gi.dispose();
	}

	protected void paintComponent(Graphics g)
	{
		g.drawImage(imageBackground, 1, 1, 333, 162, 0, scroll, 334, 161 + scroll, this);


		//ScrollBar
		g.setColor(Color.BLACK);
		g.fillRect(333, 1, 16, 161);

		if(length*16 > 161)
		{
			if(scrollBarState[0] == 0) //normal
			{
				g.setColor(new Color(0x333333));
				g.drawImage(imageScrollBar, 336, 4, 346, 16, 0, 0, 10, 12, this);
			}
			else if(scrollBarState[0] == 1) //over all
			{
				g.setColor(new Color(0x444444));
				g.drawImage(imageScrollBar, 336, 4, 346, 16, 10, 0, 20, 12, this);
			}
			else if(scrollBarState[0] == 2) //over
			{
				g.setColor(new Color(0x444444));
				g.drawImage(imageScrollBar, 336, 4, 346, 16, 20, 0, 30, 12, this);
			}

			g.fillRect(340, 20, 2, 123);

			if(scrollBarState[1] == 0) //normal
			{
				g.setColor(new Color(0x9D9D9D));
				g.drawImage(imageScrollBar, 336, 147, 346, 159, 0, 12, 10, 24, this);
			}
			else if(scrollBarState[1] == 1) //over all
			{
				g.setColor(new Color(0xD0D0D0));
				g.drawImage(imageScrollBar, 336, 147, 346, 159, 10, 12, 20, 24, this);
			}
			else if(scrollBarState[1] == 2) //over
			{
				g.setColor(new Color(0xD0D0D0));
				g.drawImage(imageScrollBar, 336, 147, 346, 159, 20, 12, 30, 24, this);
			}

			g.fillRoundRect(339, 20 + (int)(123*scroll/(16*length)), 4, (int)(123*160/(16*length)), 0, 0);
		}
	}

	public void paintNode(ModeleBrowserTree node, Graphics g)
	{
		g.drawImage(imageTree, x, y, 16 + x, 12 + y, 0, 0, 16, 12, null);
		g.drawString(node.getName(), x+19, y+11);

		x += 13;
		int childCount = node.getChildCount();

		for(int i = 0; i < childCount; i++)
		{
			y += 16;
			paintNode(node.getChildren().get(i), g);
			x -= 13;
		}
	}

	public void buildNode(ModeleBrowserTree node)
	{
		if(node.getLength() < 1)
		{
			int childCount = getChildCount(node);

			for(int i = 0; i < childCount; i++)
			{
				length += 1;
				ModeleBrowserTree child = getChild(node, i);
				node.addChild(child);
				buildNode(child);
			}
		}
	}

	public int getChildCount(ModeleBrowserTree node)
	{
		if(node.getPath() == "root")
		{
			return listRouts.length;
		}
		else
		{
			File parentFile = new File(node.getPath());

			if(parentFile.isDirectory())
			{
				String[] directoryMembers = parentFile.list();
				try{
					return directoryMembers.length;
				}
				catch(Exception e)
				{
					return 0;
				}
			}
			else
			{
				return 0;
			}
		}
	}

	public ModeleBrowserTree getChild(ModeleBrowserTree node, int index)
	{
		if(node.getPath() == "root")
		{
			return new ModeleBrowserTree(listRouts[index].getPath(), listRouts[index].getPath(), 1);
		}
		else
		{
			File[] listFiles = new File(node.getPath()).listFiles();
			File file = listFiles[index];
			return new ModeleBrowserTree(file.getPath(), file.getName(), node.getLength() + 1);
		}
	}

	public boolean isLeaf(ModeleBrowserTree node)
	{
		if(node.getPath() == "root")
		{
			return false;
		}
		else
		{
			return new File(node.getPath()).isFile();
		}
	}

	public int getLength()
	{
		return length;
	}

	public int getScroll()
	{
		return scroll;
	}

	public void setScroll(int value)
	{
		if(value > length*16 - 161)
		{
			value = length*16 - 161;
		}
		if(value < 0)
		{
			value = 0;
		}

		scroll = value;
		repaint();
	}

	public int[] getScrollBarState()
	{
		return scrollBarState;
	}

	public void setScrollBarState(int[] value)
	{
		scrollBarState = value;
		repaint();
	}
}
