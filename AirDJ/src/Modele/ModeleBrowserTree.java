package Modele;

import java.util.ArrayList;

public class ModeleBrowserTree
{
	private String path;
	private String name;
	private int length;
	private ArrayList<ModeleBrowserTree> children;
	private int childCount;

	public ModeleBrowserTree(String path, String name, int length)
	{
		this.path = path;
		this.name = name;
		this.length = length;
		children = new ArrayList<ModeleBrowserTree>();
		childCount = 0;
	}

	public void addChild(ModeleBrowserTree node)
	{
		children.add(node);
		childCount += 1;
	}

	public String getPath()
	{
		return path;
	}

	public String getName()
	{
		return name;
	}

	public int getLength()
	{
		return length;
	}

	public ArrayList<ModeleBrowserTree> getChildren()
	{
		return children;
	}

	public int getChildCount()
	{
		return childCount;
	}
}
