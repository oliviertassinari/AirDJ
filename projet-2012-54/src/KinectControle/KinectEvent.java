package KinectControle;

import java.util.EventObject;

public class KinectEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	KinectSource source;
	String message;
	
	public KinectEvent(KinectSource source, String message) {
		super(source);
		this.source =source;
		this.message = message;
		
		}
	
	public KinectSource getSource(){
		return source;
	}
	
	public String getMessage(){
		return message;
	}


	

}
