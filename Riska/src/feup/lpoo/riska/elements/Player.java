package feup.lpoo.riska.elements;

import java.util.ArrayList;
import org.andengine.util.adt.color.Color;

/**
 * Class that represents a player.
 */
public class Player extends Object {
	
	// ======================================================
	// CONSTANTS
	// ======================================================
	
	// ======================================================
	// FIELDS
	// ======================================================
	private boolean isCPU;
	private ArrayList<Region> regions;
	private int soldiersToDeploy;
	private Color priColor, secColor;
	
	/**
	 * Constructor for Player.
	 * <p>
	 * Note: the player's secondary color should be distinguishable from the primary color.
	 * 
	 * @param isCPU : true if player is not human
	 * @param primaryColor : player's primary color
	 * @param secondaryColor : player's secondary color
	 */
	public Player(boolean isCPU, Color primaryColor, Color secondaryColor) {
		
		this.isCPU = isCPU;
		
		this.regions = new ArrayList<Region>();
		
		this.priColor = primaryColor;
		this.secColor = secondaryColor;
		
	}
	
	/**
	 * Adds a region to the set of regions owned by the player.
	 * 
	 * @param region : Region to be added
	 */
	public void addRegion(Region region) {
		regions.add(region);
	}
	
	/**
	 * Removes a region from the set of regions owned by the player.
	 * 
	 * @param region : Region to be removed
	 */
	public void removeRegion(Region region) {
		regions.remove(region);
	}
	
	/**
	 * @return Set of regions owned by the player
	 */
	public ArrayList<Region> getRegions() {
		return regions;
	}
	
	/**
	 * Checks if the player owns the given region.
	 * 
	 * @param region : Region to check
	 * @return true if player owns the region
	 */
	public boolean ownsRegion(Region region) {
		return regions.contains(region);
	}
	
	/**
	 * @return True if the player is not human
	 */
	public boolean isCPU() {
		return isCPU;
	}

	/**
	 * @return Number of soldiers left to deploy
	 */
	public int getSoldiersToDeploy() {
		return soldiersToDeploy;
	}

	/**
	 * Sets the number of soldiers the player can deploy
	 * 
	 * @param soldiersToDeploy : number of soldiers to deploy
	 */
	public void setSoldiersToDeploy(int soldiersToDeploy) {
		this.soldiersToDeploy = soldiersToDeploy;
	}

	/**
	 * Deploys a number of soldiers, decreasing the number of soldiers left to be deployed.
	 *  
	 * @param number : soldiers to deploy
	 * @return Actual number of deployed soldiers
	 */
	public int deploySoldiers(int number) {
		int deployed = number;

		soldiersToDeploy -= number;

		if(soldiersToDeploy < 0) {
			deployed = number + soldiersToDeploy;
			
		}

		return deployed;
	}

	/**
	 * @return True if the player has still soldiers left to deploy
	 */
	public boolean hasSoldiersLeftToDeploy() {
		return (soldiersToDeploy > 0);
	}
	
	/**
	 * @return The player's primary color
	 */
	public Color getPrimaryColor() {
		return this.priColor;
	}
	
	/**
	 * @return The player's secondary color
	 */
	public Color getScondaryColor() {
		return this.secColor;
	}

	/**
	 * Called only with non-human players
	 * TODO : heavy deployment
	 */
	public void deploy() {
		
		int i = 0;
		while(soldiersToDeploy > 0) {
			regions.get(i).addSoldiers(1);
			i = (i + 1) % regions.size();
			soldiersToDeploy--;
		}
		
	}

}
