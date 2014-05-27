package feup.lpoo.riska.region.color;

import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.Region;
import android.graphics.Bitmap;
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
	}
	
	public void init() {
		boolean TRANSPARENT = false;
		boolean OPAQUE = true;
		
		RegionColor comp = new RegionColor();
		
		for(int x = 0; x < src.getHeight(); x++) {
			for(int y = 0; y < src.getWidth(); y++) {
				comp.set(src.getPixel(x, y));

				if(comp == special) {
					region.setStratCenter(x, y);
					mask[x][y] = OPAQUE;
					continue;
				}
				if(comp == regionColor) {
					mask[x][y] = OPAQUE;
					continue;
				}
				
				mask[x][y] = TRANSPARENT;
			}
		}
	}
	
	public void print() {
		String msg = "";
		Log.d("regions", "mask of " + region.toString());
		Log.d("regions", "size: " + mask[0].length + "x" + mask.length);
		
		for(int x = 0; x < mask.length; x++) {
			for(int y = 0; y < mask[0].length; y++) {
				msg += " " + (mask[x][y] == true ? 1 : 0);
			}
			Log.d("regions", msg);
			msg = "";
		}
		
	}
}
