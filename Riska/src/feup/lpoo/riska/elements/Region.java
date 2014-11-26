package feup.lpoo.riska.elements;

import java.util.ArrayList;

import org.andengine.util.adt.color.Color;

import android.graphics.Point;

public class Region extends Element {

	// ======================================================
	// FIELDS
	// ======================================================
	public final int ID;
	
	public boolean focused;
	
	private int garrisonArmy;
	private Player owner;	
	private ArrayList<Region> neighbourRegion;	
	private Point stratCenter;
	
	// ======================================================
	// ======================================================
	public Region(final int id, String name, float pX, float pY, int sX, int sY, String continent)
	{
		super(pX, pY, name);
		
		stratCenter = new Point(sX, sY);
		
		ID = id;
		owner = null;
		focused = false;
		garrisonArmy = 0;
		neighbourRegion = new ArrayList<Region>();
	}
	
	public void addNeighbour(Region region)
	{
		neighbourRegion.add(region);
	}

	public ArrayList<Region> getNeighbours()
	{
		return neighbourRegion;
	}

	public void setOwner(Player newOwner)
	{
		owner = newOwner;
	}

	public Player owner()
	{
		return owner;
	}

	public boolean isNeighbourOf(Region pRegion)
	{
		return neighbourRegion.contains(pRegion);
	}
	
	public Color getPriColor()
	{
		if(focused)
			return owner.secColor;
		else
			return owner.priColor;
	}
	
	public Color getSecColor()
	{
		if(focused)
			return owner.priColor;
		else
			return owner.secColor;
	}
	
	public boolean ownerIs(Player player)
	{
		return (owner.equals(player));
	}

	public boolean hasEnemyNeighbor()
	{
		for(Region neighbour : neighbourRegion)
		{
			if(!neighbour.ownerIs(owner))
			{
				return true;
			}
		}

		return false;
	}
	
	public boolean hasAlliedNeighbour()
	{
		for(Region neighbour : neighbourRegion)
		{
			if(neighbour.owner().equals(owner))
			{
				return true;
			}
		}

		return false;
	}

	public Point getStratCenter()
	{
		return stratCenter;
	}

	public void setSoldiers(int value)
	{
		this.garrisonArmy = value;
	}

	public int getSoldiers()
	{
		return garrisonArmy;
	}

	public void addSoldiers(int value)
	{
		garrisonArmy += value;
	}
	
}
