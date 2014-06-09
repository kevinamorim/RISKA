package feup.lpoo.riska.elements;

import android.graphics.Point;

/**
 * References any element in the game.
 * <p>
 * An element is any entity that might have a position and a name.
 *
 */
public class Element extends Object {
	
	protected String name;
	protected Point position;
	
	/**
	 * Constructor for Element.
	 * 
	 * @param x : x coordinate for the element
	 * @param y : y coordinate for the element
	 * @param name : name of the element
	 */
	protected Element(int x, int y, String name) {
		this.position = new Point(x, y);
		this.name = name;
	}
	
	@Override
	public String toString() {
		return (name.length() > 0 ? name : "");
	}

	/**
	 * @return name of the Element
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return The element's position
	 */
	public Point getPosition() {
		return position;
	}
}
