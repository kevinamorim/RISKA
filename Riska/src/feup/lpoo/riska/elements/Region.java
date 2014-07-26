package feup.lpoo.riska.elements;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.SceneManager;
import android.graphics.Point;

/**
 * Represents any region.
 * 
 * @author Lu�s
 *
 */
public class Region extends Element {

	// ======================================================
	// CONSTANTS
	// ======================================================
	protected static final long MIN_TOUCH_INTERVAL = 30;
	protected static final int MAX_CHARS = 10;
	protected static final int SOLDIER_ATT = 10;
	protected static final int SOLDIER_DEF = 10;
	private static final int MIN_SOLDIERS_FOR_AN_ATTACK = 2;

	// ======================================================
	// SINGLETONS
	// ======================================================
	protected MainActivity activity;
	protected SceneManager sceneManager;
	protected ResourceCache resources;

	// ======================================================
	// FIELDS
	// ======================================================
	public final int ID;
	protected boolean focused;

	protected Player owner;
	protected String continent;
	
	protected Color priColor, secColor;
	
	protected ArrayList<Unit> soldiers;
	protected ArrayList<Region> neighbours;
	
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
		
		activity = MainActivity.getSharedInstance();
		sceneManager = SceneManager.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		
		this.ID = id;
		this.continent = continent;
		this.owner = null;
		this.focused = false;
		this.soldiers = new ArrayList<Unit>();
		this.neighbours = new ArrayList<Region>();
	}
	
	/* ======================================================================
	 * ======================================================================    
	 *                          GETTERS & SETTERS 
	 * ======================================================================    
	 * ======================================================================    
	 */

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
	 * @return True if this region is focused/selected
	 */
	public boolean isFocused() {
		return focused;
	}

	/**
	 * Changes the focus of a region.
	 * 
	 * @param value : new value of the focus
	 */
	public void setFocused(boolean value) {
		this.focused = value;
	}
	
	/**
	 * @return Number of soldiers stationed in this region
	 */
	public int getNumberOfSoldiers() {
		return soldiers.size();
	}
	
	/**
	 * Adds a number of soldiers to a region.
	 * 
	 * @param value : number of soldiers to add
	 */
	public void addSoldiers(int value) {
		for(int i = 0; i < value; i++)
		{
			soldiers.add(new Unit(SOLDIER_ATT, SOLDIER_DEF));
		}
	}
	
	public void setSoldiers(int value) {
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
	public void addNeighbour(Region region) {
		neighbours.add(region);
	}

	/**
	 * @return The set of neighbour regions
	 */
	public ArrayList<Region> getNeighbours() {
		return neighbours;
	}

	/**
	 * Changes the ownership for this region.
	 * 
	 * @param player : new owner
	 */
	public void setOwner(Player player) {
		this.owner = player;
	}
	
	/**
	 * @return The set of soldiers stationed in this region
	 */
	public ArrayList<Unit> getSoldiers() {
		return soldiers;
	}

	/**
	 * @return The player that is currently owner of this region
	 */
	public Player getOwner() {
		return this.owner;
	}

	/**
	 * Checks if the region is neighbour of a given region.
	 * 
	 * @param pRegion : region to check
	 * @return True if is neighbour of pRegion
	 */
	public boolean isNeighbourOf(Region pRegion) {
		return neighbours.contains(pRegion);
	}
	
	/**
	 * Sets the primary and secondary color for the region.
	 * 
	 * @param priColor : primary color
	 * @param secColor : secondary color
	 */
	public void setColors(Color priColor, Color secColor) {
		this.priColor = priColor;
		this.secColor = secColor;
	}

	/**
	 * Switches the primary color with the secondary one.
	 */
	private void switchColors(boolean toFocus) {
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
	public void clearSoldiers() {
		soldiers.clear();
	}
	
	/**
	 * Changes the ownership for this region.
	 * Unlike setOwner(), this method updates every information regarding the new owner.
	 * 
	 * @param newOwner : new owner
	 */
	public void changeOwner(Player newOwner) {
		
		if(owner != null)
		{
			owner.removeRegion(this);
		}
		
		owner = newOwner;
		owner.addRegion(this);
		
		secColor = owner.getPrimaryColor();
		priColor = owner.getScondaryColor();
	}
	
	public boolean canAttack() {
		return (getNumberOfSoldiers() > MIN_SOLDIERS_FOR_AN_ATTACK);
	}
	
	public boolean hasEnemyNeighbor() {
		for(Region region : neighbours) {
			if(!region.getOwner().equals(owner)) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Region> getEnemyNeighbours() {
		ArrayList<Region> result = new ArrayList<Region>();
		
		for(Region region : neighbours) {
			if(!region.getOwner().equals(owner)) {
				result.add(region);
			}
		}
		
		return result;
	}
	
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
	
	public Region selectTargetRegion()
	{
		
		if(hasEnemyNeighbor())
		{
			ArrayList<Region> neighbours = getEnemyNeighbours();
			Random r = new Random();
			int i = r.nextInt(neighbours.size());
			return neighbours.get(i);
		}
		
		return null;
		
	}
	
	public Color getPrimaryColor()
	{
		return priColor;
	}
	
	public Color getSecundaryColor()
	{
		return secColor;
	}

}
