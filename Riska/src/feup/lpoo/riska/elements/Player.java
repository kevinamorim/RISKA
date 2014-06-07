package feup.lpoo.riska.elements;

import java.util.ArrayList;

public class Player {
	
	// ======================================================
	// CONSTANTS
	// ======================================================
	
	// ======================================================
	// FIELDS
	// ======================================================
	private boolean isCPU;
	
	private ArrayList<Region> regions;
	private Region regionSelected;
	private Region regionToAttack;
	
	
	public Player(boolean isCPU) {
		
		this.isCPU = isCPU;
		
		regions = new ArrayList<Region>();
		regionSelected = null;
		regionToAttack = null;
		
	}
	
	public void addRegion(Region region) {
		regions.add(region);
	}
	
	public void removeRegion(Region region) {
		regions.remove(region);
	}
	
	public ArrayList<Region> getRegions() {
		return regions;
	}
	
	public void setRegionSelected(Region region) {
		regionSelected = region;
	}
	
	public void setRegionToAttack(Region region) {
		regionToAttack = region;
	}
	
	public Region getRegionSelected() {
		return regionSelected;
	}
	
	public Region getRegionToAttack() {
		return regionToAttack;
	}
	
	public boolean ownsRegion(Region pRegion) {
		for(Region region : regions) {
			if(region.equals(pRegion)) {
				return true;
			}
		}	
		return false;
	}
	
	public boolean isCPU() {
		return isCPU;
	}
	
	public boolean isOwnerOf(Region region) {
		return regions.contains(region);
	}

}
