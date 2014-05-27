package feup.lpoo.riska;

import feup.lpoo.riska.region.color.RegionColor;
import feup.lpoo.riska.region.color.TransparencyMask;
import android.graphics.Point;

public class Region extends Element {

	protected RegionColor color;
	protected Point stratCenter;
	
	protected int width;
	protected int height;
	
	protected TransparencyMask mask;
	
	@Override
	public String toString() {
		return "Region: '" + this.name + "' at (" + (int)this.x + "," + (int)this.y + ") sized (" + this.width + "," + this.height + ")";
	}
	
	public Region(float x, float y, int width, int height, String name) {
		super(x, y, name);
		this.width = width;
		this.height = height;
		this.color = null;
		this.mask = null;
	}
	
	public Region(float x, float y, int width, int height, RegionColor c, String name) {
		super(x, y, name);
		this.width = width;
		this.height = height;
		this.color = c;
		this.mask = null;
	}

	public RegionColor getColor() {
		return color;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
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

	public TransparencyMask getMask() {
		return mask;
	}

	public void setMask(TransparencyMask mask) {
		this.mask = mask;
	}

}
