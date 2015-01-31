package feup.lpoo.riska.elements;

public class Element extends Object {
	
	public String name;
	protected float x, y;

	protected Element(int x, int y, String pName)
	{
		this((float)x, (float)y, pName);
	}
	
	protected Element(float x, float y, String pName)
	{
		this.x = x;
		this.y = y;
		name = pName;
	}
	
	@Override
	public String toString()
	{
		return (name.length() > 0 ? name : "");
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
}
