package feup.lpoo.riska.elements;

import java.util.ArrayList;
import org.andengine.util.adt.color.Color;

public class Player extends Object {
	
	// ======================================================
	// CONSTANTS
	// ======================================================
	
	// ======================================================
	// FIELDS
	// ======================================================
	public boolean isCPU;	
	
	private ArrayList<Region> regions;	
	
	public int soldiersToDeploy;

	private Color priColor, secColor;
	
	private String playerName;
	
	public Player(boolean isCPU, Color primaryColor, Color secondaryColor, String name)
	{
		
		this.isCPU = isCPU;
		if(isCPU)
		{
			playerName = "CPU";
		}
		else
		{
			playerName = name;
		}
		
		this.regions = new ArrayList<Region>();
		
		this.priColor = primaryColor;
		this.secColor = secondaryColor;

	}
	
	public void addRegion(Region region)
	{
		regions.add(region);
	}

	public void removeRegion(Region region)
	{
		regions.remove(region);
	}

	public ArrayList<Region> getRegions() {
		return regions;
	}
	
	public boolean ownsRegion(Region region) {
		return regions.contains(region);
	}

	public void setSoldiersToDeploy(int soldiersToDeploy) {
		this.soldiersToDeploy = soldiersToDeploy;
	}

	public int deploySoldiers(int number)
	{
		int deployed = number;

		soldiersToDeploy -= number;

		if(soldiersToDeploy < 0) {
			deployed = number + soldiersToDeploy;
			
		}

		return deployed;
	}

	public boolean hasSoldiersLeftToDeploy()
	{
		return (soldiersToDeploy > 0);
	}

	public Color getPrimaryColor()
	{
		return this.priColor;
	}

	public Color getScondaryColor()
	{
		return this.secColor;
	}

	/**
	 * Called only with non-human players
	 * TODO : heavy deployment
	 */
	public void deployAllSoldiers() {
		
		int i = 0;
		while(soldiersToDeploy > 0) {
			regions.get(i).addSoldiers(1);
			i = (i + 1) % regions.size();
			soldiersToDeploy--;
		}
		
	}
	
	public String getName()
	{
		return playerName;
	}
}
