package feup.lpoo.riska.logic;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

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

	// ======================================================
	// FIELDS
	// ======================================================
	protected Point stratCenter;

	protected String name;
	protected String continent;

	protected boolean selected;

	protected boolean owned;
	
	protected ButtonSprite button;
	
	private long lastTimeTouched;
	
	public Region(String name, Point stratCenter, String continent) {
		
		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();

		lastTimeTouched = System.currentTimeMillis();
		
		this.name = name;
		this.stratCenter = stratCenter;
		this.continent = continent;
		
		button = new ButtonSprite(stratCenter.x, stratCenter.y, instance.regionButtonTiledTextureRegion, 
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
		
		Text buttonText = new Text(0, 0, instance.mFont, "10",
				activity.getVertexBufferObjectManager());
		buttonText.setScale((float) 0.5);
		buttonText.setPosition(button.getWidth()/2, button.getHeight()/2);
		
		button.attachChild(buttonText);
		
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
				
				instance.cameraManager.focusOnRegion(this);
				
				((GameScene) instance.getGameScene()).onRegionSelected();
				
			} else {
				
				instance.cameraManager.zoomOut();
				instance.cameraManager.panToCenter();
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



}
