package Modele;

import java.io.File;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class ModeleFileTree implements TreeModel
{
	private File[] listRouts;
	private ModeleFile root;

	public ModeleFileTree()
	{
		listRouts = File.listRoots();
		root = new ModeleFile("root", "Ordinateur");
	}

	public Object getRoot()
	{
		return root;
	}

	public Object getChild(Object parent, int index)
	{
		ModeleFile parentModelFile = (ModeleFile) parent;

		if(parentModelFile.getPath() == "root")
		{
			return new ModeleFile(listRouts[index].getPath());
		}
		else
		{
			File[] listFiles = new File(parentModelFile.getPath()).listFiles();
			return new ModeleFile(listFiles[index].getPath(), listFiles[index].getName());
		}
	}

	public int getChildCount(Object parent)
	{
		ModeleFile parentModelFile = (ModeleFile) parent;

		if(parentModelFile.getPath() == "root")
		{
			return listRouts.length;
		}
		else
		{
			File parentFile = new File(parentModelFile.getPath());

			if(parentFile.isDirectory())
			{
				String[] directoryMembers = parentFile.list();
				return directoryMembers.length;
			}
			else
			{
				return 0;
			}
		}
	}

	public int getIndexOfChild(Object parent, Object child)
	{
		ModeleFile parentModelFile = (ModeleFile) parent;
		ModeleFile childModelFile = (ModeleFile) child;
		File parentFile = new File(parentModelFile.getPath());
		File childFile = new File(childModelFile.getPath());

		if(parentModelFile.getPath() == "root")
		{
			return 2;
		}
		else
		{
			String[] directoryMemberNames = parentFile.list();
			int result = -1;

			for(int i = 0; i < directoryMemberNames.length; ++i)
			{
				if(childFile.getName().equals(directoryMemberNames[i]))
				{
					result = i;
					break;
				}
			}

			return result;
		}
	}

	public boolean isLeaf(Object node)
	{
		ModeleFile nodeModelFile = (ModeleFile) node;

		if(nodeModelFile.getPath() == "root")
		{
			return false;
		}
		else
		{
			return new File(nodeModelFile.getPath()).isFile();
		}
	}

	public void addTreeModelListener(TreeModelListener l)
	{
	}

	public void removeTreeModelListener(TreeModelListener l)
	{
	}

	public void valueForPathChanged(TreePath path, Object newValue)
	{
	}
}