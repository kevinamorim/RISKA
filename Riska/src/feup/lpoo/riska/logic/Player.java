package feup.lpoo.riska.logic;

import java.util.ArrayList;

public class Player {
	
	// ======================================================
	// CONSTANTS
	// ======================================================
	private enum PlayerType {
		PLAYER,
		CPU
	};
	
	// ======================================================
	// FIELDS
	// ======================================================
	private int playerType;
	private ArrayList<Region> regions;
	private Region regionSelected;
	private Region regionToAttack;
	
	
	public Player(int pPlayerType) {
		
		playerType = pPlayerType;
		
		regions = new ArrayList<Region>();
		
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

}
