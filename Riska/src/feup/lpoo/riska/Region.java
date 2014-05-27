package feup.lpoo.riska;

import feup.lpoo.riska.region.color.RegionColor;
import android.graphics.Point;

public class Region extends Element {
	
	protected String name;

	protected RegionColor color;
	protected Point stratCenter;
	
	protected int width;
	protected int height;
	
	@Override
	public String toString() {
		return "Region: '" + this.name + "' at (" + (int)this.x + "," + (int)this.y + ") sized (" + this.width + "," + this.height + ")";
	}
	
	public Region(float x, float y, int width, int height, String name) {
		super(x, y, name);
		this.width = width;
		this.height = height;
		this.color = null;
	}
	
	public Region(float x, float y, int width, int height, RegionColor c, String name) {
		super(x, y, name);
		this.width = width;
		this.height = height;
		this.color = c;
	}

	public RegionColor getColor() {
		return color;
	}

	public void setRegionColor(RegionColor color) {
		this.color = color;
	}
	
	public void setStratCenter(int x, int y) {
		this.stratCenter.set(x, y);
	}
	
	public Point getStratCenter() {
		return this.stratCenter;
	}

}
