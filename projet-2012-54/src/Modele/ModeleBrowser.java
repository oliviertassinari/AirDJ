package Modele;

public class ModeleBrowser
{
	private Modele modele;

	public ModeleBrowser(Modele modele)
	{
		this.modele = modele;
	}
	
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
}
