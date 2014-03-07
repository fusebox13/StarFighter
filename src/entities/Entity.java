package entities;

public interface Entity {
	
	public void draw();
	public void update(int delta);
	public void setLocation(float x, float y, float z);
	public void setX(float x);
	public void setY(float y);
	public void setZ(float z);
	public void setLength(float length);
	public void setWidth(float width);
	public void setHeight(float height);
	public float getX();
	public float getY();
	public float getZ();
	public float getLength();
	public float getWidth();
	public float getHeight();
	public float getVolume();
	public boolean intersects(Entity other);

}
