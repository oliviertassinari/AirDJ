package KinectControle;

import java.util.EventObject;

public class KinectEvent extends EventObject
{
	private static final long serialVersionUID = 1L;
	private KinectSource source;
	private String message;
	private String param1;

	public KinectEvent(KinectSource source, String message, String param1)
	{
		super(source);
		this.source =source;
		this.message = message;
		this.param1 = param1;
	}
	
	public KinectSource getSource()
	{
		return source;
	}
	
	public String getMessage()
	{
		return message;
	}

	public String getParam1()
	{
		return param1;
	}
}
