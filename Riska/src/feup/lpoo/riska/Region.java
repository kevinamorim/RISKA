package feup.lpoo.riska;

import org.andengine.util.adt.color.Color;

public class Region extends Element {

	protected Color color;
	
	public Region(float x, float y) {
		super(x, y);
		this.color = null;
	}
	
	public Region(float x, float y, String TAG) {
		super(x, y, TAG);
		this.color = null;
	}
	
	public Region(float x, float y, Color c, String TAG) {
		super(x, y, TAG);
		this.color = c;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
