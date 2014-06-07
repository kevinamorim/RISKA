package feup.lpoo.riska.elements;

import java.util.ArrayList;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.scenes.CameraManager;
import feup.lpoo.riska.scenes.GameScene;
import feup.lpoo.riska.scenes.SceneManager;
import android.graphics.Point;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;

public class Region {
	
	// ======================================================
	// CONSTANTS
	// ======================================================
	private static final long MIN_TOUCH_INTERVAL = 30;
	
	// ======================================================
	// SINGLETONS
	// ======================================================
	private MainActivity activity;
	private SceneManager instance;
	private CameraManager cameraManager;

	// ======================================================
	// FIELDS
	// ======================================================
	private final int id;
	protected Point stratCenter;

	protected String name;
	protected String continent;

	protected boolean selected;

	protected boolean owned;
	
	protected ButtonSprite button;
	protected ButtonSprite hudButton;
	private Text hudButtonText;
	
	private long lastTimeTouched;
	
	private Sprite flag;
	
	private ArrayList<Region> neighbours;
	
	public Region(final int id, String name, Point stratCenter, String continent) {
		
		this.id = id;
		
		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();
		cameraManager = CameraManager.getSharedInstance();
		
		neighbours = new ArrayList<Region>();

		lastTimeTouched = System.currentTimeMillis();
		
		this.name = name;
		this.stratCenter = stratCenter;
		this.continent = continent;
		this.flag = new Sprite(0, 0, 240, 150, instance.getRegionFlag(), activity.getVertexBufferObjectManager());
		
		button = new ButtonSprite(stratCenter.x, stratCenter.y, instance.getRegionButtonTextureRegion(), 
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
		
		Text buttonText = new Text(0, 0, instance.getFont() , "10",
				activity.getVertexBufferObjectManager());
		buttonText.setScale((float) 0.5);
		buttonText.setPosition(button.getWidth()/2, button.getHeight()/2);
		button.attachChild(buttonText);

		hudButton = new ButtonSprite(MainActivity.CAMERA_WIDTH/4, MainActivity.CAMERA_HEIGHT/5, instance.getStartButtonTextureRegion(),
				activity.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, 
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
				case MotionEvent.ACTION_DOWN:
					pressedConfirmationButton();
					break;
				case MotionEvent.ACTION_UP:
					releasedConfirmationButton();
					break;
				case MotionEvent.ACTION_OUTSIDE:
					releasedConfirmationButton();
					break;
				default:
					break;
				}
				return true;
			}
		};
		hudButtonText = new Text(hudButton.getWidth()/2, hudButton.getHeight()/2, 
				instance.getFont(), "DEFAULT", activity.getVertexBufferObjectManager());
		hudButton.attachChild(hudButtonText);
		
	}
	
	public void updateHudButtonText(Player player) {
		if(player.ownsRegion(this)) {
			hudButtonText.setText("CHOOSE");
		} else {
			hudButtonText.setText("ATTACK!");
		}
	}
	
	public void pressedConfirmationButton() {
		
		hudButton.setCurrentTileIndex(1);
		
	}
	
	public void releasedConfirmationButton() {
		
		long now = System.currentTimeMillis();
		
		if((now - lastTimeTouched) > MIN_TOUCH_INTERVAL) {
			
			hudButton.setCurrentTileIndex(0);
			((GameScene) instance.getGameScene()).onRegionConfirmed(this);
			
		}
		
		lastTimeTouched = System.currentTimeMillis();
		
	}

	public void pressedRegionButton() {	

			button.setCurrentTileIndex(1);	

	}
	
	public void releasedRegionButton() {
		
		long now = System.currentTimeMillis();
		
		if((now - lastTimeTouched) > MIN_TOUCH_INTERVAL) {
			
			button.setCurrentTileIndex(0);
			selected = !selected;
			
			Log.d("Region", "Released: " + selected);
			
			if(selected) {
				
				cameraManager.focusOnRegion(this);
				
				((GameScene) instance.getGameScene()).onRegionSelected();
				
			} else {
				
				cameraManager.zoomOut();
				cameraManager.panToCenter();
				((GameScene) instance.getGameScene()).onRegionUnselected(this);
			}
			
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
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return the owned
	 */
	public boolean isOwned() {
		return owned;
	}

	/**
	 * @param owned the owned to set
	 */
	public void setOwned(boolean owned) {
		this.owned = owned;
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
	
	public ButtonSprite getHudButton() {
		return hudButton;
	}

}
