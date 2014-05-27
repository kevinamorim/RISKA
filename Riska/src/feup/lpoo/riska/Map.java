package feup.lpoo.riska;

import java.util.ArrayList;

import org.andengine.entity.sprite.Sprite;

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
}
