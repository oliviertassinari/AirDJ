
package Kinect;

import java.awt.AWTException;
import java.awt.Robot;

import KinectControle.KinectSource;

/**
 * Implémentation de la reconnaissance de mouvements
 */
public class reconnaissanceMvt
{
	private MainPosition mainPosition;
	private String side;
	private int ct1;
	private int ct2;
	private int ct3;
	private long timeOrigin;
	private KinectSource kinectSource;
	private Robot robot;

	public reconnaissanceMvt(MainPosition mainPosition, String side, KinectSource kinectSource)
	{
		this.mainPosition = mainPosition;
		this.side = side;
		ct1 = 0;
		ct2 = 0;
		ct3 = 0;
		timeOrigin = System.currentTimeMillis();
		this.kinectSource = kinectSource;

		try
		{
			robot = new Robot();
		}
		catch(AWTException e1)
		{
			e1.printStackTrace();
		}
	}

	public void compute(long timeLastGrab)
	{
		if(side == "right")
		{
			System.out.println(mainPosition.get(0)[7]);

			if(mainPosition.get(0)[4] == 1)
			{
				robot.mouseMove((int)mainPosition.getFiltre(0)[3]*2, (int)mainPosition.getFiltre(0)[4]*2);
			}
		}

		long[] positionCurrent = mainPosition.getFiltre(0);

		for(int i = 0; i < 19; i++)
		{
			long[] position = mainPosition.getFiltre(i);

			if(mainPosition.get(i)[0] > timeOrigin)
			{
				if(Math.abs(position[0] - positionCurrent[0]) < 40 && Math.abs(position[1] - positionCurrent[1]) < 40) // stable en x, y
				{
					ct1 += 1;
				}
				else
				{
					ct1 = 0;
				}

				if(Math.abs(position[0] - positionCurrent[0]) < 40 && Math.abs(position[2] - positionCurrent[2]) < 80) // stable en x, z
				{
					ct2 += 1;
				}
				else
				{
					ct2 = 0;
				}

				if(Math.abs(position[1] - positionCurrent[1]) < 40 && Math.abs(position[2] - positionCurrent[2]) < 80) // stable en y, z
				{
					ct3 += 1;
				}
				else
				{
					ct3 = 0;
				}

				if(ct1 > 20)
				{
					float dz = 0;

					for(int j = 0; j <= i; j++)
					{
						dz += mainPosition.getDerivee(j)[2]; // z
					}

					if(dz < -2 && position[2] - positionCurrent[2] > 80)
					{
						timeOrigin = timeLastGrab + 500;
						ct1 = 0;
						ct2 = 0;
						ct3 = 0;
						System.out.println(side + " pause " + dz + " " + (position[2] - positionCurrent[2]));
						kinectSource.fireEvent("play", side, 0);
					}
				}

				if(ct2 > 20)
				{
					float dy = 0;

					for(int j = 0; j <= i; j++)
					{
						dy += mainPosition.getDerivee(j)[1]; // y
					}

					if(Math.abs(dy) > 3)
					{
						timeOrigin = timeLastGrab + 500;
						ct1 = 0;
						ct2 = 0;
						ct3 = 0;
						System.out.println(side + " volume " + dy);
						kinectSource.fireEvent("volume", side, (int)(-dy * 10));
					}
				}

				if(ct3 > 20)
				{
					float dx = 0;

					for(int j = 0; j <= i; j++)
					{
						dx += mainPosition.getDerivee(j)[0]; // x
					}

					if((dx > 3 && side == "right") || (dx < -3 && side == "left"))
					{
						timeOrigin = timeLastGrab + 500;
						ct1 = 0;
						ct2 = 0;
						ct3 = 0;
						System.out.println(side + " crossfinder" + dx);
						kinectSource.fireEvent("crossfinder", side, (int)(dx * 20));
					}
				}
			}
			else
			{
				break;
			}
		}
	}
}
