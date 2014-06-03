package feup.lpoo.riska.logic;

public class Element extends Object {
	
	protected String name;
	protected int x;
	protected int y;
	
	// TODO private float depth;
	
	protected Element(int x, int y, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
