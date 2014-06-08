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
	
	private int soldiersToDeploy;
	
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
	
	public boolean ownsRegion(Region region) {
		return regions.contains(region);
	}
	
	public boolean isCPU() {
		return isCPU;
	}

	public int getSoldiersToDeploy() {
		return soldiersToDeploy;
	}

	public void setSoldiersToDeploy(int soldiersToDeploy) {
		this.soldiersToDeploy = soldiersToDeploy;
	}

	public int deploySoldiers(int number) {
		
		int deployed = number;
		
		soldiersToDeploy -= number;
		
		if(soldiersToDeploy < 0) {
			deployed = number + soldiersToDeploy;
		}
		
		return deployed;
	}

	public boolean hasSoldiersLeftToDeploy() {
		return (soldiersToDeploy > 0);
	}

}
