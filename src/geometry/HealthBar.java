package geometry;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.Serializable;

public class HealthBar implements Serializable {
	
	public float x, y;
	public float width;
	public int clientIndex;
	public boolean alive;
	public float originalWidth;

	
	public HealthBar(float x, float y, float width, int clientIndex)
	{
		
		this.x = x;
		this.y = y;
		this.width = width;
		originalWidth = width;
		this.clientIndex = clientIndex;
		alive = true;
	}
	
	public void draw()
	{
		if (clientIndex == 0)
			glColor3f(0,1,0);
		else
			glColor3f(1,1,0);
		
		glBegin(GL_QUADS);
			glVertex2f(x,y);
			glVertex2f(x + width, y);
			glVertex2f(x + width, y - 20);
			glVertex2f(x, y - 20);
		glEnd();
	}
	
	public void updateWidth()
	{
		if (width > 0)
			width -= 10;
		else
			alive = false;
	}
	
	public void resetBar()
	{
		width = originalWidth;
		alive = true;
	}
}
