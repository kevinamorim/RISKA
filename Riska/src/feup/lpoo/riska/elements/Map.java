package feup.lpoo.riska.elements;

import org.andengine.entity.sprite.Sprite;

import android.util.Log;

public class Map {
	
	protected Sprite background;
	protected Region[] regions;
	
	public Map(Region[] regions) {
		this.regions = regions;
	}

	public Region[] getRegions() {
		return regions;
	}

	public void setRegions(Region[] regions) {
		this.regions = regions;
	}
	
	public Region getRegionById(int id) {
		for(Region region : regions) {
			if(region.getId() == id) return region;
		}
		return null;
	}
	
	
	public void printNeighbours() {
		for(Region region : regions) {
			for(Region neighbour : region.getNeighbours()) {
				Log.d("Region", "Region: " + region.getName() + " -> Neighbour: " + neighbour.getName());
			}
		}
	}
}
