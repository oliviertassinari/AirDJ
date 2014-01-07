
package Vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class VueBrowserTable extends JPanel
{
	private static final long serialVersionUID = -3655883389805563009L;

	/** 
	 * image de la barre de défilement
	 */
	private Image imageScrollBar;
	/** 
	 * image de
	 */
	private Image imageTable;
	/**
	 * arrière plan
	 */
	private BufferedImage imageBackground;
	private int length = 0;
	private int scroll = 0;
	private int[] scrollBarState = {0, 0};
	private int[] tableOver = {-1, -1};
	private ArrayList<String> pathList;

	/**
	 * constructeur
	 */
	public VueBrowserTable()
	{
		imageScrollBar = new ImageIcon(getClass().getResource("/image/scrollBar.png")).getImage();
		imageTable = new ImageIcon(getClass().getResource("/image/table.png")).getImage();

		setBackground(Color.RED);
		setPreferredSize(new Dimension(645, 163));
		setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, new Color(0x4E4C4B)));

		paintNode("");
	}

	/**
	 * méthode paintComponent
	 */
	protected void paintComponent(Graphics g)
	{
		g.drawImage(imageBackground, 0, 1, 628, 162, 0, scroll, 628, 161 + scroll, this);

		if(tableOver[0] != -1)
		{
			if(tableOver[1] == 0)
			{
				g.drawImage(imageTable, 4, tableOver[0] * 25 + 3 - scroll, 24, tableOver[0] * 25 + 24 - scroll, 0, 21, 20, 42, this);
			}
			else
			{
				g.drawImage(imageTable, 26, tableOver[0] * 25 + 3 - scroll, 46, tableOver[0] * 25 + 24 - scroll, 20, 21, 40, 42, this);
			}
		}

		// ScrollBar
		g.setColor(Color.BLACK);
		g.fillRect(628, 1, 16, 161);

		if(length * 25 > 161)
		{
			if(scrollBarState[0] == 0) // normal
			{
				g.setColor(new Color(0x333333));
				g.drawImage(imageScrollBar, 631, 4, 641, 16, 0, 0, 10, 12, this);
			}
			else if(scrollBarState[0] == 1) // over all
			{
				g.setColor(new Color(0x444444));
				g.drawImage(imageScrollBar, 631, 4, 641, 16, 10, 0, 20, 12, this);
			}
			else if(scrollBarState[0] == 2) // over
			{
				g.setColor(new Color(0x444444));
				g.drawImage(imageScrollBar, 631, 4, 641, 16, 20, 0, 30, 12, this);
			}

			g.fillRect(635, 20, 2, 123);

			if(scrollBarState[1] == 0) // normal
			{
				g.setColor(new Color(0x9D9D9D));
				g.drawImage(imageScrollBar, 631, 147, 641, 159, 0, 12, 10, 24, this);
			}
			else if(scrollBarState[1] == 1) // over all
			{
				g.setColor(new Color(0xD0D0D0));
				g.drawImage(imageScrollBar, 631, 147, 641, 159, 10, 12, 20, 24, this);
			}
			else if(scrollBarState[1] == 2) // over
			{
				g.setColor(new Color(0xD0D0D0));
				g.drawImage(imageScrollBar, 631, 147, 641, 159, 20, 12, 30, 24, this);
			}

			int longeur = (int)(123 * 160 / (25 * length));

			if(longeur < 5)
			{
				longeur = 5;
			}

			g.fillRoundRect(634, 20 + (int)(123 * scroll / (25 * length)), 4, longeur, 0, 0);
		}
	}

	/**
	 * 
	 */
	public void paintNode(String path)
	{
		pathList = new ArrayList<String>();
		File node = new File(path);

		if(node.isDirectory())
		{
			File[] listFiles = node.listFiles();

			for(int i = 0; i < listFiles.length; i++)
			{
				if(listFiles[i].isFile() && getExtension(listFiles[i].getName()).equals(".wav") || getExtension(listFiles[i].getName()).equals(".mp3"))
				{
					pathList.add(listFiles[i].getPath());
				}
			}
		}

		int x = 0;

		length = pathList.size();

		int color = 0; // 0 = #1C1C1D - 1 = #101010

		if(length * 25 > 161)
		{
			imageBackground = new BufferedImage(647, length * 25, BufferedImage.TRANSLUCENT);
		}
		else
		{
			imageBackground = new BufferedImage(647, 161, BufferedImage.TRANSLUCENT);
		}

		Graphics gi = imageBackground.getGraphics();

		gi.setColor(Color.BLACK);
		gi.fillRect(0, 0, 628, 161);
		gi.setFont(new Font("sansserif", Font.BOLD, 11));

		for(String filePath : pathList)
		{
			if(color == 0)
			{
				color = 1;
				gi.setColor(new Color(0x1C1C1D));
			}
			else
			{
				color = 0;
				gi.setColor(new Color(0x101010));
			}

			gi.fillRect(0, x, 628, 25);

			gi.setColor(Color.WHITE);
			gi.drawString(new File(filePath).getName(), 60, x + 16);

			gi.drawImage(imageTable, 4, x + 2, 24, x + 23, 0, 0, 20, 21, this);
			gi.drawImage(imageTable, 26, x + 2, 46, x + 23, 20, 0, 40, 21, this);

			x += 25;
		}

		gi.dispose();
		repaint();
	}

	/**
	 * getter extension des fichiers ??
	 * @param filePath
	 * @return " "
	 */
	public String getExtension(String filePath)
	{
		if(filePath.lastIndexOf(".") != -1)
		{
			return filePath.substring(filePath.lastIndexOf(".")).toLowerCase();
		}
		else
		{
			return "";
		}
	}

	/**
	 * getter length (nombre de lignes)
	 * @return length
	 */
	public int getLength()
	{
		return length;
	}

	/**
	 * setter length (nombre de lignes)
	 * @param value
	 */
	public void setLength(int value)
	{
		length = value;
	}

	/**
	 * getter scroll (barre de défilement ?)
	 * @return scroll
	 */
	public int getScroll()
	{
		return scroll;
	}

	/**
	 * setter scroll (barre de défilement ?)
	 * @param value
	 */
	public void setScroll(int value)
	{
		if(value > length * 25 - 161)
		{
			value = length * 25 - 161;
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
	 * @return scrollBarState
	 */
	public int[] getScrollBarState()
	{
		return scrollBarState;
	}

	/**
	 * setter scrollBarState
	 * @param value
	 */
	public void setScrollBarState(int[] value)
	{
		scrollBarState = value;
		repaint();
	}

	/**
	 * setter tableOver
	 * @param value
	 */
	public void setTableOver(int[] value)
	{
		tableOver = value;
		repaint();
	}

	/**
	 * getter filePath
	 * @param index
	 * @return
	 */
	public String getFilePath(int index)
	{
		return pathList.get(index);
	}
}
