package Modele;

public class ModeleFile
{
	private String path;
	private String name;

	public ModeleFile(String path)
	{
		this.path = path;
		this.name = path;
	}

	public ModeleFile(String path, String name)
	{
		this.path = path;
		this.name = name;
	}

	public String toString()
	{
		return name;
	}
	
	public String getPath()
	{
		return path;
	}
}
