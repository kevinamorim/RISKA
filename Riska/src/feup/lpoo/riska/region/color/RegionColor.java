package feup.lpoo.riska.region.color;

import org.andengine.util.adt.color.Color;

public class RegionColor extends Object {
	
	@Override
	public String toString() {
		return "[" + R + "," + G + "," + B + "," + A + "]";
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof RegionColor) {
			return((((RegionColor)o).R == this.R)
					&& (((RegionColor)o).G == this.G)
					&& (((RegionColor)o).B == this.B)
					&& (((RegionColor)o).A == this.A));
		}
		
		if(o instanceof Color) {
			return(((int)((Color)o).getRed() == this.R)
					&& ((int)((Color)o).getGreen() == this.R)
					&& ((int)((Color)o).getBlue() == this.R)
					&& ((int)((Color)o).getAlpha() == this.R));
		}
		
		return super.equals(o);
	}

	int R, G, B, A;
	
	public RegionColor() {
		R = 0;
		G = 0;
		B = 0;
		A = 0;
	}
	
	public RegionColor(Color col) {
		R = (int) col.getRed();
		G = (int) col.getGreen();
		B = (int) col.getBlue();
		A = (int) col.getAlpha();
	}
	
	public RegionColor(RegionColor col) {
		R = col.R;
		G = col.G;
		B = col.B;
		A = col.A;
	}
	
	public RegionColor(int red, int green, int blue, int alpha) {
		R = red;
		G = green;
		B = blue;
		A = alpha;
	}
	
	public RegionColor(int pixel) {
		convertPixelToColor(pixel);
	}
	
	private void convertPixelToColor(int pix) {
		
		R = ((pix << 8) >>> 24);
		G = ((pix << 16) >>> 24);
		B = ((pix << 24) >>> 24);
		A = (pix >>> 24);
	}
	
	public boolean isTransparent() {
		return (A < 255);
	}
	
	public boolean isOpaque() {
		return (A == 255);
	}
	
	public int getRed() {
		return R;
	}
	
	public int getGreen() {
		return G;
	}
	
	public int getBlue() {
		return B;
	}
	
	public int getAlpha() {
		return A;
	}
	
	public void setRed(int red) {
		R = red;
	}
	
	public void setGreen(int green) {
		G = green;
	}
	
	public void setBlue(int blue) {
		B = blue;
	}
	
	public void setAlpha(int alpha) {
		A = alpha;
	}
	
	public void set(int red, int green, int blue, int alpha) {
		R = red;
		G = green;
		B = blue;
		A = alpha;
	}
	
	public void set(int pixel) {
		convertPixelToColor(pixel);
	}
}
