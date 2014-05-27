package feup.lpoo.riska.region.creator;

import java.util.ArrayList;

import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.Region;
import feup.lpoo.riska.region.color.RegionColor;
import feup.lpoo.riska.region.color.TransparencyMask;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.util.Pair;

public class RegionCreator {
	
	public static RegionCreator instance;
	
	protected Bitmap src;
	protected ArrayList<RegionColor> colors;
	
	protected ArrayList<Region> regions;
	
	protected ArrayList<Pair<Point, Point> > regionLimits;
	
	public RegionCreator() {
		instance = this;
		
		colors = new ArrayList<RegionColor>();
		
		regions = new ArrayList<Region>();
		
		regionLimits = new ArrayList<Pair<Point, Point> >();
	}
	
	public void CreateAllRegions(Bitmap src, RegionColor special) {
		this.src = src;
		
		addColorsToArray(special);
		
		Log.d("regions", "Number of detected colors (non transparent): " + colors.size());
		Log.d("regions", "Computing regions limits... (may take a while)");
		
		evaluateRegionsPosition(special);
		
		Log.d("regions", "Number of Computed Regions (total): " + regionLimits.size() + " (" + colors.size() + ")");
		Log.d("regions", "Creating regions...");
		
		addRegionsToArray();
		
		Log.d("regions", "Number of Created Regions (total): " + regions.size() + " (" + colors.size() + ")");
		Log.d("regions", "Creating transparency maps...");
		
		addMasksToRegions(special);
		
		Log.d("regions", "All created. Returning...");
	}

	private void addMasksToRegions(RegionColor special) {
		TransparencyMask mask;
		
		for(Region region: regions) {
			Bitmap regionSubMap = Bitmap.createBitmap(src, (int)region.getX(), (int)region.getY(), region.getWidth(), region.getHeight());
			mask = new TransparencyMask(regionSubMap, region, special);
			region.setMask(mask);
			//region.getMask().print();
		}
		
	}

	private void evaluateRegionsPosition(RegionColor special) {
		RegionColor col = new RegionColor();
		
		int done = 0;
		
		for(RegionColor c: colors) {
			int Xmin = Integer.MAX_VALUE, Ymin = Integer.MAX_VALUE, Xmax = 0, Ymax = 0;
//			boolean foundTop = false;
			
			for(int y = 0; y < src.getHeight() ; y++) {
//				boolean foundBottom = false;
				
				for(int x = 0; x < src.getWidth() ; x++) {
					col.set(src.getPixel(x, y));
					
					if(col.equals(c)) {
//						if(!foundTop) {
//							foundTop = true;
//						}
//						
//						foundBottom = true;
						
						if(x < Xmin) {
							Xmin = x;
						}
						if(y < Ymin) {
							Ymin = y;
						}
						if(x > Xmax) {
							Xmax = x;
						}
						if(y > Ymax) {
							Ymax = y;
						}
					}
				}
				
//				// Optimization
//				if(foundTop && !foundBottom) {
//					break;
//				}
			}
			
			regionLimits.add(new Pair<Point, Point>(new Point(Xmin, Ymin), new Point(Xmax, Ymax)));
			Log.d("regions","Region " + done + " done");
			done++;
			
			//Log.d("regions", "added: " + c.toString() + " at (" + Xmin + "," + Ymin + ") to (" + Xmax + "," + Ymax + ")");
		}
	}

	private void addRegionsToArray() {
		for(int i = 0; i < colors.size(); i++) {
			int x = regionLimits.get(i).first.x;
			int y = regionLimits.get(i).first.y;
			int width = regionLimits.get(i).second.x - x;
			int height = regionLimits.get(i).second.y - y;
			
			regions.add(new Region(x, y, width, height, colors.get(i), "Region " + i));
			Log.d("regions", regions.get(i).toString());
		}
	}

	private void addColorsToArray(RegionColor special) {
		Log.d("regions", "width: " + src.getWidth());
		Log.d("regions", "height: " + src.getHeight());
		
		RegionColor comp = new RegionColor();
		RegionColor col;
		//int count = 0;
		//int pixels = src.getWidth() * src.getHeight();
		
		for(int y = 0; y < src.getHeight() ; y++) {
			for(int x = 0; x < src.getWidth() ; x++) {
				comp.set(src.getPixel(x, y));
				
				if(comp.isOpaque()) {				
					if(!colors.contains(comp)) {
						col = new RegionColor(comp);
						//Log.d("regions", "new color: " + col.toString());
						colors.add(col);
					}
				}
			}
		}
		
		//Log.d("regions", "[" + count + "] off [" + pixels + "] pixels done");
		
	}
	
	public static RegionCreator getSharedInstance() {
		return instance;
	}

	public ArrayList<Region> getRegions() {
		return regions;
	}

}
