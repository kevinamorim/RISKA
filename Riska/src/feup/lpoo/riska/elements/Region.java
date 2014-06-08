package feup.lpoo.riska.elements;

import java.util.ArrayList;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.CameraManager;
import feup.lpoo.riska.scenes.GameScene;
import feup.lpoo.riska.scenes.SceneManager;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

public class Region {

	// ======================================================
	// CONSTANTS
	// ======================================================
	private static final long MIN_TOUCH_INTERVAL = 30;
	private static final int MAX_CHARS = 10;

	// ======================================================
	// SINGLETONS
	// ======================================================
	private MainActivity activity;
	private SceneManager sceneManager;
	private ResourceCache resources;

	// ======================================================
	// FIELDS
	// ======================================================
	private final int id;
	protected Point stratCenter;
	protected int soldiers;

	protected String name;
	protected String continent;

	protected boolean focused;
	
	private long lastTimeTouched;
	
	protected ButtonSprite button;	
	Text buttonText;
	private Sprite flag;
	
	private ArrayList<Region> neighbours;
	private Player owner;
	
	public Region(final int id, String name, Point stratCenter, String continent) {
		
		this.id = id;
		
		activity = MainActivity.getSharedInstance();
		sceneManager = SceneManager.getSharedInstance();
		CameraManager.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		
		neighbours = new ArrayList<Region>();
		
		lastTimeTouched = System.currentTimeMillis();
		
		this.name = name;
		this.stratCenter = stratCenter;
		this.continent = continent;
		this.soldiers = 0;
		this.owner = null;
		this.focused = false;
		
		this.flag = new Sprite(0, 0, 240, 150, resources.getRegionFlags(), activity.getVertexBufferObjectManager());
		
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
		
	}
	
	public boolean playerIsOwner(Player player) {
		return (owner == player);
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
	 * @return the stratCenter
	 */
	public Point getStratCenter() {
		return stratCenter;
	}

	/**
	 * @param stratCenter the stratCenter to set
	 */
	public void setStratCenter(Point stratCenter) {
		this.stratCenter = stratCenter;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the selected
	 */
	public boolean isFocused() {
		return focused;
	}

	/**
	 * @param value the selected value to set
	 */
	public void setFocused(boolean value) {
		this.focused = value;
	}
	
	public int getNumberOfSoldiers() {
		return soldiers;
	}

	public void setNumberOfSoldiers(int soldiers) {
		this.soldiers = soldiers;
	}
	
	public void addSoldiers(int value) {
		soldiers += value;
	}
	
	public Sprite getFlag(float pX, float pY) {
		flag.setPosition(pX, pY);
		return flag;
	}
	
	public void addNeighbour(Region region) {
		neighbours.add(region);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the neighbours
	 */
	public ArrayList<Region> getNeighbours() {
		return neighbours;
	}
	
	public ButtonSprite getButton() {
		return button;
	}

	public void setColor(Color color) {
		this.button.setColor(color);
	}
	
	public void setOwner(Player player) {
		this.owner = player;
	}
	
	public Player getOwner() {
		return this.owner;
	}

	public boolean isNeighbourOf(Region focusedRegion) {
		return neighbours.contains(focusedRegion);
	}

	public void updateSoldiers() {
		
		button.detachChildren();
		buttonText.setText("" + soldiers);
		button.attachChild(buttonText);
	}
}
