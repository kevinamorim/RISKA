package feup.lpoo.riska.elements;

import java.util.ArrayList;

import org.andengine.entity.sprite.Sprite;

import android.util.Log;

public class Map {
	
	protected Sprite background;
	protected ArrayList<Region> regions;
	
	public Map(ArrayList<Region> regions) {
		this.regions = regions;
	}

	public ArrayList<Region> getRegions() {
		return regions;
	}

	public void setRegions(ArrayList<Region> regions) {
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
	
	public int getNumberOfRegions() {
		return regions.size();
	}
}
