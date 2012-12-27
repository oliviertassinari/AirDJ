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
	private int x = 0;
	private int y = 0;
	private int length = 0;
	private int scroll = 0;

	public VueBrowserTree()
	{
		listRouts = File.listRoots();
		imageTree = new ImageIcon("image/tree.png").getImage();

		setBackground(Color.BLUE);
		setPreferredSize(new Dimension(350, 163));
		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x4E4C4B)));
	}

	protected void paintComponent(Graphics g)
	{
		ModeleBrowserTree root = new ModeleBrowserTree("root", "Ordinateur", 0);
		buildNode(root);

		BufferedImage imageBackground = new BufferedImage(350, length*16, BufferedImage.TRANSLUCENT);
        Graphics gi = imageBackground.getGraphics();

		gi.setColor(Color.BLACK);
		gi.fillRect(0, 0, 350, length*16);

	    gi.setFont(new Font("sansserif", Font.BOLD, 11));
		gi.setColor(Color.WHITE);

        paintNode(root, gi);

        gi.dispose();

        g.drawImage(imageBackground, 1, 1, 349, 162, 0, 0, 348, 161, this);
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
		if(node.getLength() < 2)
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
}
