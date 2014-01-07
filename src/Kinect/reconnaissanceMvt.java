
package Kinect;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import KinectControle.KinectSource;

/**
 * Implémentation de la reconnaissance de mouvements
 */
public class reconnaissanceMvt
{
	private Kinect kinect;
	private MainPosition mainPosition;
	private String side;
	private int ct1;
	private int ct2;
	private int ct3;
	private long timeOrigin;
	private KinectSource kinectSource;
	private Robot robot;

	public reconnaissanceMvt(Kinect kinect, MainPosition mainPosition, String side, KinectSource kinectSource)
	{
		this.kinect = kinect;
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
		// Mouse move
		if(side == "right")
		{
			float statut = 0;

			for(int i = 0; i < 19; i++)
			{
				if(mainPosition.get(i)[4] == 1)
				{
					statut += 1;
				}
			}

			if(statut/19 > 0.5)
			{
				robot.mouseMove((int)mainPosition.getFiltre(0)[3]*2, (int)mainPosition.getFiltre(0)[4]*2);
				//System.out.println(statut/19);
			}
		}

		if(mainPosition.get(0)[4] == 1 && mainPosition.get(1)[4] == 0)
		{
			int t1 = 0;
			long fingerActuelX = mainPosition.get(0)[5];
			long fingerActuelY = mainPosition.get(0)[6];

			for(int i = 0; i < 30; i++)
			{
				if(mainPosition.get(i)[4] == 0) //fermé
				{
					t1 += 1;
				}

				if(t1 > 2)
				{
					double length = kinect.getLenght(fingerActuelX, fingerActuelY, (int)mainPosition.get(i)[5], (int)mainPosition.get(i)[6]);

					if(2 < length && length < 12)
					{
						robot.mousePress(InputEvent.BUTTON1_MASK);
						robot.mouseRelease(InputEvent.BUTTON1_MASK);
						System.out.println("click length:"+length);
						break;
					}
				}
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

				if(ct1 > 20) // Suffisamment stable en x, y
				{
					float dz = 0;
					float statut = 0;

					for(int j = 0; j <= i; j++)
					{
						dz += mainPosition.getDerivee(j)[2]; // z

						if(mainPosition.get(j)[4] > 1)
						{
							statut += 1;
						}
					}

					if(dz < -2 && position[2] - positionCurrent[2] > 80 && statut/(i+1) > 0.7)
					{
						timeOrigin = timeLastGrab + 500;
						ct1 = 0;
						ct2 = 0;
						ct3 = 0;
						System.out.println(side + " pause dz:" + dz + " dx:" + (position[2] - positionCurrent[2]) + " statut:"+statut/(i+1));
						kinectSource.fireEvent("playpause", side, 0);
					}
				}

				if(ct2 > 20) // Suffisamment stable en x, z
				{
					float dy = 0;
					float statut = 0;

					for(int j = 0; j <= i; j++)
					{
						dy += mainPosition.getDerivee(j)[1]; // y

						if(mainPosition.get(j)[4] > 1)
						{
							statut += 1;
						}
					}

					if(Math.abs(dy) > 3 && statut/(i+1) > 0.7)
					{
						timeOrigin = timeLastGrab + 500;
						ct1 = 0;
						ct2 = 0;
						ct3 = 0;
						System.out.println(side + " volume dy:" + dy + " statut:"+statut/(i+1));
						kinectSource.fireEvent("volume", side, (int)(-dy * 10));
					}
				}

				if(ct3 > 20) // Suffisamment stable en y, z
				{
					float dx = 0;
					float statut = 0;

					for(int j = 0; j <= i; j++)
					{
						dx += mainPosition.getDerivee(j)[0]; // x

						if(mainPosition.get(j)[4] > 1)
						{
							statut += 1;
						}
					}

					if(((dx > 3 && side == "right") || (dx < -3 && side == "left")) && statut/(i+1) > 0.6)
					{
						timeOrigin = timeLastGrab + 500;
						ct1 = 0;
						ct2 = 0;
						ct3 = 0;
						System.out.println(side + " crossfinder dx:" + dx + " statut:"+statut/(i+1));
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
