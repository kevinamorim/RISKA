package feup.lpoo.riska.elements;

import java.util.ArrayList;

import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.utilities.Utils;

public class Player extends Object {

	// ======================================================
	// FIELDS
	// ======================================================
	public boolean isCpu;
	public Color priColor, secColor;
	public String name;
	
	private ArrayList<Region> regionsPool;	

	public int soldiersPool = 0;
	public int moves = 0;

	// ======================================================
	// =====================================================
	public Player(boolean isCPU, Color[] colors, String name)
	{
		this(isCPU, colors[0], colors[1], name);
	}

	public Player(boolean isCPU, Color primaryColor, Color secondaryColor, String pName)
	{
		isCpu = isCPU;
		
		if(isCpu)
		{
			name = "CPU";
		}
		else
		{
			name = pName;
		}

		regionsPool = new ArrayList<Region>();

		priColor = primaryColor;
		secColor = secondaryColor;
	}

	// ======================================================
	// =====================================================
	public void addRegion(Region region)
	{
		regionsPool.add(region);
	}

	public void removeRegion(Region region)
	{
		regionsPool.remove(region);
	}

	public ArrayList<Region> getRegions()
	{
		return regionsPool;
	}

	public boolean ownsRegion(Region region)
	{
		return regionsPool.contains(region);
	}

	public void setSoldiersPool(int value)
	{
		soldiersPool = value;
	}

	public void subtractFromSoldiersPool(int value)
	{
		soldiersPool -= value;
	}

	public boolean hasSoldiersInPool()
	{
		return (soldiersPool > 0);
	}
	
	public Region pickRegionForAttack()
	{	
//		ArrayList<Region> allowedRegions = new ArrayList<Region>();
//		
//		for(Region region : regionsPool)
//		{
//			if(region.canAttack() && region.hasEnemyNeighbor()) {
//				allowedRegions.add(region);
//			}
//		}
//		
//		if(allowedRegions.size() > 0)
//		{
//			return allowedRegions.get(Utils.randomInt(0, regionsPool.size() - 1));
//		}
//		
//		pickRegionForAttack();
		
		return null;
	}
	
	public Region pickRegionForMove()
	{	
//		ArrayList<Region> allowedRegions = new ArrayList<Region>();
//		
//		for(Region item : regionsPool)
//		{
//			if(item.canAttack() && item.hasAlliedNeighbour())
//			{
//				allowedRegions.add(item);
//			}
//		}
//		
//		if(allowedRegions.size() > 0)
//		{
//			return allowedRegions.get(Utils.randomInt(0, regionsPool.size() - 1));
//		}
//		
//		pickRegionForMove();
		
		return null;
	}
	
	public Region pickNeighbourAlliedRegion(Region pRegion)
	{
		ArrayList<Region> neighbours = pRegion.getNeighbours();
		ArrayList<Region> allowed = new ArrayList<Region>();
		
		for(Region item : neighbours) {
			if(!item.ownerIs(this)) {
				allowed.add(item);
			}
		}
		
		return allowed.get(Utils.randomInt(0, allowed.size() - 1));
	}
	
	public Region pickNeighbourEnemyRegion(Region pRegion)
	{
		ArrayList<Region> neighbours = pRegion.getNeighbours();
		ArrayList<Region> allowed = new ArrayList<Region>();

		for(Region item : neighbours) {
			if(item.ownerIs(this)) {
				allowed.add(item);
			}
		}
		
		return allowed.get(Utils.randomInt(0, allowed.size() - 1));
	}
	
	public boolean hasPossibleMoves()
	{	
//		for(Region item : regionsPool)
//		{
//			if(item.hasEnemyNeighbor() && item.canAttack())
//			{
//				return true;
//			}
//		}
//
		return false;
	}	

	@Override
	public String toString()
	{
		return "Player '" + name + "' (cpu=" + isCpu + ")";
	}
}
