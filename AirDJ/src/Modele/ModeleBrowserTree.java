package Modele;

import java.util.ArrayList;

public class ModeleBrowserTree
{
	private String path;
	private String name;
	private ArrayList<ModeleBrowserTree> children;
	private int childCount;
	private ModeleBrowserTree parent;
	private int childIndex;
	private boolean expanded;
	private boolean leaf;

	public ModeleBrowserTree(String path, String name, boolean expanded, boolean leaf)
	{
		this.path = path;
		this.name = name;
		children = new ArrayList<ModeleBrowserTree>();
		childCount = 0;
		parent = null;
		childIndex = 0;
		this.expanded = expanded;
		this.leaf = leaf;
	}

	public void addChild(ModeleBrowserTree node)
	{
		node.parent = this;
		node.childIndex = childCount;
		children.add(node);

		childCount += 1;
	}

	public void setExpanded(boolean expanded)
	{
		this.expanded = expanded;
	}

	public String getPath()
	{
		return path;
	}

	public String getName()
	{
		return name;
	}

	public ModeleBrowserTree getChild(int index)
	{
		return children.get(index);
	}

	public int getChildCount()
	{
		return childCount;
	}

	public ModeleBrowserTree getNode(int lineGoal, int lineCurrent)
	{
		if(lineGoal == lineCurrent)
		{
			return this;
		}
		else
		{
			if(childCount != 0)
			{
				return getChild(0).getNode(lineGoal, lineCurrent + 1);
			}
			else if(childIndex + 1 < parent.childCount)
			{
				return parent.getChild(childIndex+1).getNode(lineGoal, lineCurrent + 1);
			}
			else
			{
				return parent.parent.getChild(parent.childIndex+1).getNode(lineGoal, lineCurrent + 1);
			}
		}
	}

	public boolean isExpanded()
	{
		return expanded;
	}

	public boolean isLeaf()
	{
		return leaf;
	}

	public int removeChildren()
	{
		int count = getLeafCount();

		children.clear();
		childCount = 0;

		return count;
	}
	
	public int getLeafCount()
	{
		int count = childCount;

		for(int i = 0; i < childCount; i++)
		{
			count += getChild(i).getLeafCount();
		}

		return count;
	}
}
