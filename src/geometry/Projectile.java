package geometry;

import static org.lwjgl.opengl.GL11.*;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector3f;

import enumerations.Player;

public class Projectile implements Serializable{
	
	
	public float x,y,z;
	public float depth;
	public int clientIndex;
	public int laneIndex;
	
	public Projectile(Quad lane, float depth, int clientIndex) {
		
		Vector3f mid = lane.midLane;
		x = mid.x;
		y= mid.y;
		this.depth = depth;
		this.clientIndex = clientIndex;
		this.laneIndex = lane.laneIndex;
		
		if (clientIndex == 0)
		{
			z = mid.z -.2f;
		}
		else if (clientIndex == 1)
		{
			z = lane.leftVertexFar.z + .2f;
		}
		else
			throw new RuntimeException("Failed to create projectile, invalid player");
		
	}
	
	synchronized public void update(float speed)
	{
		if (clientIndex == 0)
			z-=speed;
		else if (clientIndex == 1)
		{
			z+=speed;
		}
	}
	
	public void draw()
	{
		if (clientIndex == 0)
		{
			glColor3f(0,1,0);
			glBegin(GL_LINES);
				glVertex3f(x, y, z);
				glVertex3f(x, y, z - depth);
			glEnd();
		}
		else if (clientIndex == 1)
		{
			glColor3f(1,1,0);
			glBegin(GL_LINES);
				glVertex3f(x,y, z);
				glVertex3f(x,y, z + depth);
			glEnd();
		}
	}
	
	

}
