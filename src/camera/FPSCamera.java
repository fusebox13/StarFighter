package camera;

import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import org.lwjgl.util.vector.Vector3f;

public class FPSCamera
{
	public Vector3f position = null;
	private float yaw = 0.0f;
	private float pitch = 0.0f;
	
	public FPSCamera(float x, float y, float z)
	{
		position = new Vector3f(x,y,z);
	}
	
	
	public void yaw(float amount)
	{
		yaw += amount;
	}
	
	public void pitch(float amount)
	{
		pitch -= amount;
	}
	
	public void forward(float distance)
	{
		position.x -= distance * (float)Math.sin(Math.toRadians(yaw));
		position.y += distance * (float) Math.tan(Math.toRadians(pitch));
		position.z += distance * (float)Math.cos(Math.toRadians(yaw));
	}
	
	public void backwards(float distance)
	{
		position.x += distance * (float)Math.sin(Math.toRadians(yaw));
		position.y -= distance * (float) Math.tan(Math.toRadians(pitch));
		position.z -= distance * (float)Math.cos(Math.toRadians(yaw));
	}
	
	public void strafeLeft(float distance)
	{
		position.x -= distance * (float)Math.sin(Math.toRadians(yaw-90));
		position.z += distance * (float)Math.cos(Math.toRadians(yaw-90));
	}
	
	public void strafeRight(float distance)
	{
		position.x -= distance * (float)Math.sin(Math.toRadians(yaw+90));
		position.z += distance * (float)Math.cos(Math.toRadians(yaw+90));
	}
	
	public void down(float distance)
	{
		position.y += distance * 0.002;
	}
	
	public void up(float distance)
	{
		position.y -= distance * 0.002;
	}
	
	public void updateCameraPosition()
	{
		glRotatef(pitch, 1.0f, 0f, 0f);
		glRotatef(yaw, 0f, 1.0f, 0f);
		glTranslatef(position.x, position.y, position.z);
	}
	
	
}