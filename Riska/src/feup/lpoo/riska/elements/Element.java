package feup.lpoo.riska.elements;

import android.graphics.Point;

public class Element extends Object {
	
	public String name;
	protected Point position;

	protected Element(int x, int y, String pName)
	{
		position = new Point(x, y);
		name = pName;
	}
	
	@Override
	public String toString()
	{
		return (name.length() > 0 ? name : "");
	}
	
	public Point getPosition()
	{
		return position;
	}
	
	public int getX()
	{
		return position.x;
	}
	
	public int getY()
	{
		return position.y;
	}
}
