package feup.lpoo.riska.region.color;

import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.Region;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;

public class TransparencyMask {

	protected boolean[][] mask;
	protected Bitmap src;
	protected Region region;
	protected RegionColor regionColor;
	protected RegionColor special;
	
	public TransparencyMask(Bitmap source, Region region, RegionColor special) {
		mask = new boolean[source.getHeight()][source.getWidth()];
		
		this.src = source;
		this.region = region;
		this.regionColor = region.getColor();
		this.special = special;
		
		createMask();
	}
	
	public void createMask() {
		boolean TRANSPARENT = false;
		boolean OPAQUE = true;
		
		RegionColor comp = new RegionColor();
		
		for(int x = 0; x < src.getWidth(); x++) {
			for(int y = 0; y < src.getHeight(); y++) {
				comp.set(src.getPixel(x, y));

				if(comp.equals(special)) {
					region.setStratCenter(x, y);
					mask[y][x] = OPAQUE;
				}
				else if(comp.equals(regionColor)) {
					mask[y][x] = OPAQUE;
				}
				else {
					mask[y][x] = TRANSPARENT;
				}
			}
		}
	}
	
	public boolean isOpaque(int x, int y) {
		Point pt = convertToMaskPosition(x,y);
		return (mask[pt.y][pt.x]);
	}

	public boolean isOpaque(Point point) {
		Point pt = convertToMaskPosition(point.x,point.y);
		return (mask[pt.y][pt.x]);
	}
	
	private Point convertToMaskPosition(int x, int y) {
		Point p = new Point();
		p.set(region.getX() + region.getWidth() - x, region.getY() + region.getHeight() - y);
		return p;
	}
	
	public void print() {
		String msg = "";
		Log.d("regions", "mask of " + region.toString());
		Log.d("regions", "size: " + mask[0].length + "x" + mask.length);
		
		for(int y = 0; y < mask.length; y++) {
			for(int x = 0; x < mask[0].length; x++) {
				msg += "" + ((mask[y][x] == true) ? 1 : 0);
			}
			Log.d("regions", msg);
			msg = "";
		}
		
	}
}
