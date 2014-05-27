package feup.lpoo.riska;

public class Element extends Object {
	
	protected String name;
	protected float x;
	protected float y;
	
	// TODO private float depth;
	
	protected Element(float x, float y, String name) {
		this.x = x;
		this.y = y;
		this.name = null;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}

}
