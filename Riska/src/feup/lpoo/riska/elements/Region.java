package feup.lpoo.riska.elements;

import java.util.ArrayList;

import org.andengine.util.adt.color.Color;

import android.graphics.Point;

public class Region extends Element {

	// ======================================================
	// FIELDS
	// ======================================================
	public final int ID;
	public Color priColor, secColor;
	
	private boolean focused;
	private Player owner;
	private int garrisonArmy;
	
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

	public int getGarrison()
	{
		return garrisonArmy;
	}
	
	public void addSoldiers(int value)
	{
		garrisonArmy += value;
	}
	
	public void setSoldiers(int value)
	{
		garrisonArmy = value;
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
		if(owner != null)
		{
			owner.removeRegion(this);
		}
		
		owner = newOwner;
		owner.addRegion(this);
		
		secColor = owner.priColor;
		priColor = owner.secColor;
	}

	public Player owner()
	{
		return owner;
	}

	public boolean isNeighbourOf(Region pRegion)
	{
		return neighbourRegion.contains(pRegion);
	}

	public void setColors(Color priColor, Color secColor)
	{
		this.priColor = priColor;
		this.secColor = secColor;
	}

	private void switchColors(boolean toFocus)
	{
		if(toFocus)
		{
			priColor = owner.secColor;
			secColor = owner.priColor;
		}
		else
		{
			priColor = owner.priColor;
			secColor = owner.secColor;
		}
	}

	public void clearArmy()
	{
		garrisonArmy = 0;
	}
	
	public void setFocus(boolean value)
	{
		if(value)
		{
			focused = true;
			switchColors(true);
		}
		else
		{
			focused = false;
			switchColors(false);
		}
	}

	public boolean isFocused()
	{
		return focused;
	}
	
	public Color getPrimaryColor()
	{
		return priColor;
	}
	
	public Color getSecundaryColor()
	{
		return secColor;
	}

	public void add(int number)
	{
		addSoldiers(number);
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
	
}
