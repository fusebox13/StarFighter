package geometry;

import static org.lwjgl.opengl.GL11.*;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector3f;

import enumerations.Player;

public class Ship implements Serializable {
	
	public int laneIndex;
	public Vector3f leftVertex;
	public Vector3f rightVertex;
	public Vector3f leftVertexFar;
	public Vector3f rightVertexFar;
	public Vector3f midLane;
	public int clientIndex;
	
	public Ship(Quad lane, int clientIndex)
	{
		laneIndex = 0;
		this.leftVertex = lane.leftVertex;
		this.rightVertex = lane.rightVertex;
		this.leftVertexFar = lane.leftVertexFar;
		this.rightVertexFar = lane.rightVertexFar;
		this.midLane = lane.midLane;
		this.clientIndex = clientIndex;
		this.laneIndex = lane.laneIndex;
		
	}
	
	public void update(Quad lane)
	{
		this.leftVertex = lane.leftVertex;
		this.rightVertex = lane.rightVertex;
		this.leftVertexFar = lane.leftVertexFar;
		this.rightVertexFar = lane.rightVertexFar;
		this.midLane = lane.midLane;
		this.laneIndex = lane.laneIndex;
	}
	
	public void draw()
	{
		
		
		if(clientIndex == 0)
		{
			glColor3f(0,1,0);
			glBegin(GL_TRIANGLES);
				glVertex3f(leftVertex.x, leftVertex.y, leftVertex.z);
				glVertex3f(rightVertex.x, rightVertex.y, rightVertex.z);
				glVertex3f(midLane.x, midLane.y, midLane.z - 0.2f);
			glEnd();
		}
		else if(clientIndex == 1)
		{
			glColor3f(1,1,0);
			glBegin(GL_TRIANGLES);
				glVertex3f(leftVertexFar.x, leftVertexFar.y, leftVertexFar.z);
				glVertex3f(rightVertexFar.x, rightVertexFar.y, rightVertexFar.z);
				glVertex3f(midLane.x, midLane.y, leftVertexFar.z + 0.2f);
			glEnd();
		}
		else
		{
			throw new RuntimeException("Invalid player, error drawing ships");
		}
		
	}

}
