package feup.lpoo.riska;

import org.andengine.util.adt.color.Color;

import android.graphics.Bitmap;

public class TransparencyMask {

	protected boolean[][] mask;
	protected Bitmap src;
	protected Region region;
	protected Color special;
	
	public TransparencyMask(Bitmap source, Region toSearch, Color s) {
		mask = new boolean[source.getHeight()][source.getWidth()];
		
		src = source;
		region = toSearch;
		special = s;
	}
	
	public void init() {
//		boolean TRANSPARENT = false;
//		boolean OPAQUE = true;
//		
//		int CENTER = special.getABGRPackedInt();
//		int REGION_COLOR = region.getColor().getARGBPackedInt();
//		
//		for(int y = 0; y < src.getHeight(); y++) {
//			for(int x = 0; x < src.getWidth(); x++) {
//				int pixel = src.getPixel(x, y);
//
//				if(pixel == CENTER) {
//					region.setStratCenter(x, y);
//					mask[y][x] = OPAQUE;
//					continue;
//				}
//				if(src.getPixel(x, y) == REGION_COLOR) {
//					mask[y][x] = OPAQUE;
//					continue;
//				}
//				
//				mask[y][x] = TRANSPARENT;
//			}
//		}
	}
	
	public void printMask() {
		for(int y = 0; y < src.getHeight(); y++) {
			for(int x = 0; x < src.getWidth(); x++) {
				System.out.println(mask[y][x] + " ");
			}
			System.out.println();
		}
	}
}
