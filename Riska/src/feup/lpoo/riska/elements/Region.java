package feup.lpoo.riska.elements;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.SceneManager;
import android.graphics.Point;
import android.view.MotionEvent;

/**
 * Represents any region.
 * 
 * @author Luís
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
	private static final int MIN_SOLDIERS_TO_ATTACK = 1;

	// ======================================================
	// SINGLETONS
	// ======================================================
	protected MainActivity activity;
	protected SceneManager sceneManager;
	protected ResourceCache resources;

	// ======================================================
	// FIELDS
	// ======================================================
	protected final int id;
	
	protected long lastTimeTouched;
	protected ButtonSprite button;
	protected Text buttonText;
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
		
		this.id = id;
		this.continent = continent;
		this.owner = null;
		this.focused = false;
		this.soldiers = new ArrayList<Unit>();
		this.neighbours = new ArrayList<Region>();
		
		button = new ButtonSprite(stratCenter.x, stratCenter.y, resources.getRegionButtonTexture(), 
				activity.getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, 
					float pTouchAreaLocalX, float pTouchAreaLocalY) {

					switch(pSceneTouchEvent.getAction()) {
					case MotionEvent.ACTION_DOWN:
						pressedRegionButton();
						break;
					case MotionEvent.ACTION_UP:
						releasedRegionButton();
						break;
					case MotionEvent.ACTION_OUTSIDE:
						releasedRegionButton();
						break;
					}

				return true;
			}
		};	
		buttonText = new Text(0, 0, resources.getGameFont() , "" + soldiers, MAX_CHARS, activity.getVertexBufferObjectManager());	
		buttonText.setScale((float) 1.4);
		buttonText.setPosition(button.getWidth()/2, button.getHeight()/2);
		button.attachChild(buttonText);
		
		
		lastTimeTouched = 0;
	}

	public void pressedRegionButton() {	
			button.setCurrentTileIndex(1);	
	}
	
	public void releasedRegionButton() {
		
		long now = System.currentTimeMillis();
		
		if((now - lastTimeTouched) > MIN_TOUCH_INTERVAL) {
			
			button.setCurrentTileIndex(0);

			sceneManager.getGameScene().onRegionTouched(this);
		}

		lastTimeTouched = System.currentTimeMillis();
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
		for(int i = 0; i < value; i++) {
			soldiers.add(new Unit(SOLDIER_ATT, SOLDIER_DEF));
		}
		updateSoldiers();
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
	 * @return The ID of this region
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return The set of neighbour regions
	 */
	public ArrayList<Region> getNeighbours() {
		return neighbours;
	}
	
	/**
	 * @return The button associated with this region 
	 */
	public ButtonSprite getButton() {
		return button;
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
	 * Updates the number of soldiers shown in the region's button.
	 */
	public void updateSoldiers() {	
		buttonText.setText("" + getNumberOfSoldiers());
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
		
		updateButtonColors();
	}

	/**
	 * Switches the primary color with the secondary one.
	 */
	public void switchColors() {
		Color temp = new Color(priColor);
		
		this.priColor = secColor;
		this.secColor = temp;
		
		updateButtonColors();
	}
	
	/**
	 * Updates the button and text colors with the region's colors
	 */
	private void updateButtonColors() {
		this.button.setColor(priColor);
		this.buttonText.setColor(secColor);
	}

	/**
	 * Changes a region's focus.
	 *  
	 * @param value : new value for the region focus
	 */
	public void changeFocus(boolean value) {
		this.focused = value;
		this.switchColors();
	}
	
	/**
	 * Clears (erases) the set of soldiers in a region.
	 */
	public void clearSoldiers() {
		soldiers.clear();
		updateSoldiers();
	}
	
	/**
	 * Changes the ownership for this region.
	 * Unlike setOwner(), this method updates every information regarding the new owner.
	 * 
	 * @param newOwner : new owner
	 */
	public void changeOwner(Player newOwner) {
		
		if(owner != null) {
			owner.removeRegion(this);
		}
		
		owner = newOwner;
		owner.addRegion(this);

		soldiers.clear();
		
		priColor = newOwner.getPrimaryColor();
		secColor = newOwner.getScondaryColor();

		updateButtonColors();
		
		unfocus();
		
		updateSoldiers();
	}
	
	public boolean canAttack() {
		return getNumberOfSoldiers() > 1;
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
	
	public void focus() {
		
		focused = true;
		this.button.setColor(secColor);
		this.buttonText.setColor(priColor);
		
	}
	
	public void unfocus() {
		
		focused = false;
		this.button.setColor(priColor);
		this.buttonText.setColor(secColor);
		
	}
	
	public Region selectTargetRegion() {
		
		if(hasEnemyNeighbor()) {
			ArrayList<Region> neighbours = getEnemyNeighbours();
			Random r = new Random();
			int i = r.nextInt(neighbours.size());
			return neighbours.get(i);
		}
		
		return null;
		
	}
}
