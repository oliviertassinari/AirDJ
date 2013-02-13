
package Modele;

import java.util.ArrayList;

public class ModeleBrowserTree
{
	/**
	 * adresse du fichier 
	 */
	private String path;
	/**
	 * nom du fichier
	 */
	private String name;
	/**
	 * ensemble des fils du dossier dans l'arbre des dossiers/fichiers de l'ordinateur
	 */
	private ArrayList<ModeleBrowserTree> children;
	/**
	 * nombre de fils
	 */
	private int childCount;
	/**
	 * arbre parent
	 */
	private ModeleBrowserTree parent;
	private int childIndex;
	/** 
	 * si le dossier est développé ou non
	 */
	private boolean expanded;
	/**
	 * si le fichier/dossier est une feuille ou non
	 */
	private boolean leaf;

	/**
	 * constructeur
	 * @param String adresse
	 * @param String nom 
	 * @param boolean dossier développé ou non
	 * @param boolean fichier/dossier feuille ou non
	 */
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

	/**
	 * ajouter un arbre fils
	 * @param ModeleBrowserTree
	 */
	public void addChild(ModeleBrowserTree node)
	{
		node.parent = this;
		node.childIndex = childCount;
		children.add(node);

		childCount += 1;
	}

	/**
	 * développer
	 * @param boolean
	 * 
	 */
	public void setExpanded(boolean expanded)
	{
		this.expanded = expanded;
	}

	/**
	 * obtenir l'adresse
	 * @return String
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * obtenir le nom
	 * @return String
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Obtenir l'arbre fils
	 * @param int indice
	 * @return ModeleBrowserTree
	 */
	public ModeleBrowserTree getChild(int index)
	{
		return children.get(index);
	}

	/**
	 * obtenir le nombre de fils
	 * @return int
	 */
	public int getChildCount()
	{
		return childCount;
	}

	/**
	 * @return ModeleBrowserTree
	 * @param int lineGoal
	 * @param int lineCurrent
	 */
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
				return parent.getChild(childIndex + 1).getNode(lineGoal, lineCurrent + 1);
			}
			else
			{
				return parent.parent.getChild(parent.childIndex + 1).getNode(lineGoal, lineCurrent + 1);
			}
		}
	}

	/**
	 * savoir si le fichier est développé ou non
	 * @return boolean
	 */
	public boolean isExpanded()
	{
		return expanded;
	}

	/**
	 * savoir si c'est un feuille ou non
	 * @return boolean
	 */
	public boolean isLeaf()
	{
		return leaf;
	}

	/**
	 * @return int
	 */
	public int removeChildren()
	{
		int count = getLeafCount();

		children.clear();
		childCount = 0;

		return count;
	}

	/**
	 * @return int le nombre de feuilles
	 */
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
