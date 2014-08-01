package feup.lpoo.riska.elements;

import android.graphics.Point;

public class Element extends Object {
	
	public String name;
	protected Point position;

	protected Element(int x, int y, String name) {
		this.position = new Point(x, y);
		this.name = name;
	}
	
	@Override
	public String toString() {
		return (name.length() > 0 ? name : "");
	}
	
	public Point getPosition() {
		return position;
	}
}
