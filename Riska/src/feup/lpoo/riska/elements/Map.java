package feup.lpoo.riska.elements;

import java.util.ArrayList;

/**
 * A map is a set of regions.
 * <p>
 * Each map has its regions.
 *
 */
public class Map {
	
	//protected Sprite background; TODO implement
	protected ArrayList<Region> regions;
	
	/**
	 * Constructor for the Map class.
	 * <p>
	 * Sets the regions of the map.
	 * 
	 * @param regions
	 */
	public Map(ArrayList<Region> regions) {
		this.regions = regions;
	}

	/**
	 * Returns the set of regions that compose the map.
	 */
	public ArrayList<Region> getRegions() {
		return regions;
	}
	
	/**
	 * Returns the region with the given id.
	 * <p>
	 * Returns null if not found.
	 * 
	 * @param id : ID of the region
	 */
	public Region getRegionById(int id) {
		for(Region region : regions) {
			if(region.getId() == id) return region;
		}
		return null;
	}
	
	/**
	 * @return Returns the number of regions in this map.
	 */
	public int getNumberOfRegions() {
		return regions.size();
	}
}
