package geometry;

import static org.lwjgl.opengl.GL11.*;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector3f;

public class Quad implements Serializable{
	
	public Vector3f[] points = new Vector3f[4];
	
	public Vector3f leftVertex;
	public Vector3f rightVertex;
	public Vector3f leftVertexFar;
	public Vector3f rightVertexFar;
	public Vector3f midLane;
	public float red = 1, green, blue;
	public double angle;
	public float depth;
	public int laneIndex;
	
	public Quad(Vector3f origin, float width, float depth, double angle, int laneIndex)
	{
		this.angle = angle;
		this.depth = depth;
		this.laneIndex = laneIndex;
		createLaneCoordinates(origin, width, depth, angle);
	}
	
	public void setColor(float red, float green, float blue)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
		
	}
	
	public void draw()
	{
		for (int i = 0; i < points.length; i++)
		{
			glColor3f(red,green,blue);
			
			glBegin(GL_LINES);
				glVertex3f(points[i].x, points[i].y, points[i].z);
				glVertex3f(points[(i+1) % 4].x, points[(i+1) % 4].y, points[(i+1) % 4].z);
			glEnd();
		}
	}
	
	public void createLaneCoordinates(Vector3f origin, float width, float depth, double angle)
	{
		
		float adjacent = (float)Math.cos(Math.toRadians(angle)) / width;
		
		float opposite = (float)Math.sin(Math.toRadians(angle)) / width;
		
		adjacent = adjacent * 0.1f;
		opposite = opposite * 0.1f;
		
		float midX = adjacent / 2.0f;
		float midY = opposite / 2.0f;
		
		
		
		
		Vector3f[] LaneCoordinates = new Vector3f[4];
		
		LaneCoordinates[0] = new Vector3f(origin.x, origin.y, origin.z);
		leftVertex = LaneCoordinates[0];
		
		LaneCoordinates[1] = new Vector3f(origin.x + adjacent, origin.y + opposite, origin.z);
		rightVertex = LaneCoordinates[1];
		
		LaneCoordinates[2] = new Vector3f(origin.x + adjacent , origin.y + opposite, origin.z - depth);
		rightVertexFar = LaneCoordinates[2];
		
		LaneCoordinates[3] = new Vector3f(origin.x, origin.y, origin.z - depth);
		leftVertexFar = LaneCoordinates[3];
		
		midLane = new Vector3f(origin.x + midX, origin.y + midY, origin.z);
		
		points = LaneCoordinates;
	}
	
	

}
