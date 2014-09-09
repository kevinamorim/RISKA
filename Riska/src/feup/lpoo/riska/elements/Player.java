package feup.lpoo.riska.elements;

import java.util.ArrayList;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.utilities.Utils;

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

	// ======================================================
	// CONSTRUCTORS
	// =====================================================
	public Player(boolean isCPU, Color[] colors, String name)
	{
		this(isCPU, colors[0], colors[1], name);
	}

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

	// ======================================================
	// LOGIC
	// =====================================================
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

	public void deploy(int number, Region pRegion)
	{
		int deployed = number;

		soldiersToDeploy -= number;

		if(soldiersToDeploy < 0)
		{
			deployed = number + soldiersToDeploy;
		}

		pRegion.deploy(deployed);
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
	
	public Region pickRegionForAttack() {
		
		ArrayList<Region> allowedRegions = new ArrayList<Region>();
		
		for(Region item : regions) {
			if(item.canAttack() && item.hasEnemyNeighbor()) {
				allowedRegions.add(item);
			}
		}
		
		if(allowedRegions.size() > 0) {
			return allowedRegions.get(Utils.randomInt(0, regions.size() - 1));
		}
		
		pickRegionForAttack();
		
		return null;

	}
	
	public Region pickRegionForMove() {
		
		ArrayList<Region> allowedRegions = new ArrayList<Region>();
		
		for(Region item : regions) {
			if(item.canAttack() && item.hasAlliedNeighbour()) {
				allowedRegions.add(item);
			}
		}
		
		if(allowedRegions.size() > 0) {
			return allowedRegions.get(Utils.randomInt(0, regions.size() - 1));
		}
		
		pickRegionForMove();
		
		return null;
	}
	
	public Region pickNeighbourAlliedRegion(Region pRegion) {
		ArrayList<Region> neighbours = pRegion.getNeighbours();
		ArrayList<Region> allowed = new ArrayList<Region>();
		
		for(Region item : neighbours) {
			if(!item.hasOwner(this)) {
				allowed.add(item);
			}
		}
		
		return allowed.get(Utils.randomInt(0, allowed.size() - 1));
	}
	
	public Region pickNeighbourEnemyRegion(Region pRegion) {
		ArrayList<Region> neighbours = pRegion.getNeighbours();
		ArrayList<Region> allowed = new ArrayList<Region>();

		for(Region item : neighbours) {
			if(item.hasOwner(this)) {
				allowed.add(item);
			}
		}
		
		return allowed.get(Utils.randomInt(0, allowed.size() - 1));
	}
	
	public boolean hasPossibleMoves() {
		
		for(Region item : regions)
		{
			if(item.hasEnemyNeighbor() && item.canAttack())
			{
				return true;
			}
		}

		return false;
	}
	
}
