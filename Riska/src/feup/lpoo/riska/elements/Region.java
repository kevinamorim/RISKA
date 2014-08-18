package feup.lpoo.riska.elements;

import java.util.ArrayList;
import org.andengine.util.adt.color.Color;

import android.graphics.Point;

public class Region extends Element {

	private static final int SOLDIER_ATT = 10;
	private static final int SOLDIER_DEF = 10;
	private static final int MIN_SOLDIERS_FOR_AN_ATTACK = 2;

	// ======================================================
	// FIELDS
	// ======================================================
	public final int ID;
	private boolean focused;

	private Player owner;
	private Color priColor, secColor;
	
	private ArrayList<Unit> soldiers;
	private ArrayList<Region> neighbours;

	// ======================================================
	// ======================================================
	
	/**
	 * Constructor for a region.
	 * 
	 * @param id : ID of this region
	 * @param name : Name of the region
	 * @param stratCenter : strategic center for this region
	 * @param continent : continent this region belongs to in the map
	 */
	public Region(final int id, String name, Point stratCenter, String continent) {
		
		super(stratCenter.x, stratCenter.y, name);
		
		this.ID = id;
		this.owner = null;
		this.focused = false;
		this.soldiers = new ArrayList<Unit>();
		this.neighbours = new ArrayList<Region>();
	}

	/**
	 * @return The strategic center of the region
	 */
	public Point getStratCenter() {
		return this.position;
	}

	/**
	 * @param stratCenter : the new strategic center to set
	 */
	public void setStratCenter(Point stratCenter) {
		this.position = stratCenter;
	}

	/**
	 * @return Name of the region
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Number of soldiers stationed in this region
	 */
	public int getNumberOfSoldiers()
	{
		return soldiers.size();
	}
	
	/**
	 * Adds a number of soldiers to a region.
	 * 
	 * @param value : number of soldiers to add
	 */
	public void addSoldiers(int value)
	{
		for(int i = 0; i < value; i++)
		{
			soldiers.add(new Unit(SOLDIER_ATT, SOLDIER_DEF));
		}
	}
	
	public void setSoldiers(int value)
	{
		soldiers.clear();
		for(int i = 0; i < value; i++) 
		{
			soldiers.add(new Unit(SOLDIER_ATT, SOLDIER_DEF));
		}
	}
	
	/**
	 * Adds a region to the set of neighbours.
	 * 
	 * @param region : region to add
	 */
	public void addNeighbour(Region region)
	{
		neighbours.add(region);
	}

	/**
	 * @return The set of neighbour regions
	 */
	public ArrayList<Region> getNeighbours()
	{
		return neighbours;
	}

	/**
	 * Changes the ownership for this region.
	 * 
	 * @param player : new owner
	 */
	public void setOwner(Player newOwner)
	{
		if(owner != null)
		{
			owner.removeRegion(this);
		}
		
		owner = newOwner;
		owner.addRegion(this);
		
		secColor = owner.getPrimaryColor();
		priColor = owner.getScondaryColor();
	}
	
	/**
	 * @return The set of soldiers stationed in this region
	 */
	public ArrayList<Unit> getSoldiers()
	{
		return soldiers;
	}

	/**
	 * @return The player that is currently owner of this region
	 */
	public Player getOwner()
	{
		return this.owner;
	}

	/**
	 * Checks if the region is neighbour of a given region.
	 * 
	 * @param pRegion : region to check
	 * @return True if is neighbour of pRegion
	 */
	public boolean isNeighbourOf(Region pRegion)
	{
		return neighbours.contains(pRegion);
	}
	
	/**
	 * Sets the primary and secondary color for the region.
	 * 
	 * @param priColor : primary color
	 * @param secColor : secondary color
	 */
	public void setColors(Color priColor, Color secColor)
	{
		this.priColor = priColor;
		this.secColor = secColor;
	}

	/**
	 * Switches the primary color with the secondary one.
	 */
	private void switchColors(boolean toFocus)
	{
		if(toFocus)
		{
			priColor = owner.getScondaryColor();
			secColor = owner.getPrimaryColor();
		}
		else
		{
			priColor = owner.getPrimaryColor();
			secColor = owner.getScondaryColor();
		}
	}
	
	/**
	 * Clears (erases) the set of soldiers in a region.
	 */
	public void removeSoldiers()
	{
		soldiers.clear();
	}

//	public boolean hasEnemyNeighbor()
//	{
//		
//	}
	
	public void focus()
	{	
		focused = true;
		
		switchColors(true);
	}
	
	public void unfocus()
	{
		focused = false;
		
		switchColors(false);
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
	
	public boolean canAttack() {
		return soldiers.size() >= MIN_SOLDIERS_FOR_AN_ATTACK;
	}
	
	public boolean hasEnemyNeighbor()
	{
		for(Region neighbour : neighbours)
		{
			if(!neighbour.getOwner().equals(owner))
			{
				return true;
			}
		}

		return false;
	}
}
