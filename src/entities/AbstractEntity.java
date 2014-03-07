package entities;

public abstract class AbstractEntity implements Entity {
	
	private float x, y, z;
	private float length, width, height;

	
	
	@Override
	public void draw() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(int delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLocation(float x, float y, float z) {
		
		this.x = x;
		this.y = y;
		this.z = z;

	}

	@Override
	public void setX(float x) {
		this.x = x;

	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public void setZ(float z) {
		this.z = z;
	}

	@Override
	public void setLength(float length) {
		this.length = length;

	}

	@Override
	public void setWidth(float width) {
		this.width = width;

	}

	@Override
	public void setHeight(float height) {
		this.height = height;

	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getZ() {
		return z;
	}

	@Override
	public float getLength() {
		return length;
	}

	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public float getVolume() {
		return length * width * height;
	}

	@Override
	public boolean intersects(Entity other) {
		// TODO Auto-generated method stub
		return false;
	}

}
