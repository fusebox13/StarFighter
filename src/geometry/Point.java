package geometry;

import java.util.Random;

public class Point {
	public float x, y, z;
	public float red, green, blue;
	
	public Point(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		randomizeColors();
	}
	
	public void randomizeColors()
	{
		Random randomGenerator = new Random();
		red = randomGenerator.nextFloat();
		blue = randomGenerator.nextFloat();
		green = randomGenerator.nextFloat();
	}
	
}