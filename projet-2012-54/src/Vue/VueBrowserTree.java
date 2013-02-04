package Vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import Modele.ModeleBrowserTree;

/**
 * arbre de recherche de chansons dans l'ordinateur
 */
public class VueBrowserTree extends JPanel
{
	/**
	 * 
	 */
	private Image imageTree;
	/**
	 * image de la barre de d efilement
	 */
	private Image imageScrollBar;
	/**
	 * arriere plan
	 */
	private BufferedImage imageBackground;
	/**
	 * position horiziontale dans l'arbre
	 */
	private int x;
	/**
	 * position verticale dans l'arbre
	 */
	private int y;
	private int length = 0;
	private int scroll = 0;
	private int[] scrollBarState = {0, 0};
	private ModeleBrowserTree root;

	/**
	 * constructeur du panel
	 */
	public VueBrowserTree()
	{
		imageTree = new ImageIcon("image/tree.png").getImage();
		imageScrollBar = new ImageIcon("image/scrollBar.png").getImage();

		setBackground(Color.BLUE);
		setPreferredSize(new Dimension(350, 163));
		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x4E4C4B)));

		//Background
		length = 1;
		root = new ModeleBrowserTree("root", "Ordinateur", true, false);
		buildNode(root);

		paintRoot();
	}
	
	/**
	 * paintComponent
	 */
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

			int longeur = (int)(123*160/(16*length));
			
			if(longeur < 5)
			{
				longeur = 5;
			}
			
			g.fillRoundRect(339, 20 + (int)(123*scroll/(16*length)), 4, longeur, 0, 0);
		}
	}

	/**
	 * 
	 */
	public void paintRoot()
	{
        x = 0;
        y = 0;

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
		repaint();
	}

	/**
	 * 
	 */
	public void paintNode(ModeleBrowserTree node, Graphics g)
	{
		if(!node.isExpanded())
		{
			g.drawImage(imageTree, x + 13, y, x + 29, 12 + y, 0, 0, 16, 12, null);
		}
		else
		{
			g.drawImage(imageTree, x + 13, y, x + 29, 12 + y, 0, 12, 16, 24, null);
		}

		if(!node.isLeaf())
		{
			if(!node.isExpanded())
			{
				g.drawImage(imageTree, x + 2, y + 2, x + 11, y + 11, 16, 0, 25, 9, null);
			}
			else
			{
				g.drawImage(imageTree, x + 2, y + 2, x + 11, y + 11, 16, 9, 25, 18, null);
			}
		}

		g.drawString(node.getName(), x+32, y+11);

		x += 13;

		int childCount = node.getChildCount();

		for(int i = 0; i < childCount; i++)
		{
			y += 16;
			paintNode(node.getChild(i), g);
			x -= 13;
		}
	}
	/**
	 * @param ModeleBrowserTree
	 */
	public void buildNode(ModeleBrowserTree node)
	{
		int childCount = getChildCount(node);

		for(int i = 0; i < childCount; i++)
		{
			length += 1;
			node.addChild(getChild(node, i));
		}
	}

	/**
	 * @param ModeleBrowserTree
	 */
	public int getChildCount(ModeleBrowserTree node)
	{
		if(node.getPath() == "root")
		{
			return File.listRoots().length;
		}
		else
		{
			File parentFile = new File(node.getPath());

			if(parentFile.isDirectory())
			{
				File[] listFiles = parentFile.listFiles(new FileFilter(){
				    public boolean accept(File file)
				    {
				        return file.isDirectory() && !file.isHidden();
				    }
				});

				return listFiles.length;
			}
			else
			{
				return 0;
			}
		}
	}

	/**
	 * mméthode renvoyant le/les fils
	 * @param ModeleBrowserTree
	 * @param int
	 */
	public ModeleBrowserTree getChild(ModeleBrowserTree node, int index)
	{
		if(node.getPath() == "root")
		{
			String path = File.listRoots()[index].getPath();			
			return new ModeleBrowserTree(path, path, false, isLeaf(path));
		}
		else
		{
			File[] listFiles = new File(node.getPath()).listFiles(new FileFilter(){
			    public boolean accept(File file)
			    {
			        return file.isDirectory() && !file.isHidden();
			    }
			});

			File file = listFiles[index];

			return new ModeleBrowserTree(file.getPath(), file.getName(), false, isLeaf(file.getPath()));
		}
	}

	/**
	 * méthode spécifiant si on a atteint une feuille ou non
	 * @param String emplacement du dossier/fichier dans lequel on se trouve
	 */
	public boolean isLeaf(String path)
	{
		return new File(path).isFile();
	}

	
	/**
	 * @param int
	 */
	public ModeleBrowserTree getNode(int lineGoal)
	{
		return root.getNode(lineGoal, 1);
	}

	/**
	 * 
	 */
	public int getLength()
	{
		return length;
	}

	/**
	 * @param int 
	 */
	public void setLength(int value)
	{
		if(scroll > value*16 - 161)
		{
			scroll = value*16 - 161;
		}
		if(scroll < 0)
		{
			scroll = 0;
		}

		length = value;
	}

	/**
	 * 
	 */
	public int getScroll()
	{
		return scroll;
	}

	/**
	 * @param int
	 */
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

	/**
	 * 
	 */
	public int[] getScrollBarState()
	{
		return scrollBarState;
	}

	/**
	 * @param int[]
	 */
	public void setScrollBarState(int[] value)
	{
		scrollBarState = value;
		repaint();
	}
}
