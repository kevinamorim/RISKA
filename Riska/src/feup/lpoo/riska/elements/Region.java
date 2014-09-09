package feup.lpoo.riska.elements;

import java.util.ArrayList;

import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.logic.GameInfo;
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

	
	// ======================================================
	// ======================================================
	public Region(final int id, String name, Point pos, String continent)
	{
		
		super(pos.x, pos.y, name);
		
		ID = id;
		owner = null;
		focused = false;
		garrisonArmy = 0;
		neighbourRegion = new ArrayList<Region>();
	}

	public int numberOfSoldiers()
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

	public void deploy(int number)
	{
		addSoldiers(number);
	}
	
	public boolean hasOwner(Player player)
	{
		return (owner.equals(player));
	}
	
	public boolean canAttack()
	{
		return (garrisonArmy > GameInfo.minGarrison);
	}

	public boolean hasEnemyNeighbor()
	{
		for(Region neighbour : neighbourRegion)
		{
			if(!neighbour.owner().equals(owner))
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
}
