package feup.lpoo.riska.logic;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;

import android.graphics.Point;
import android.util.Log;

public class GameScene extends Scene implements IOnSceneTouchListener, IScrollDetectorListener {
	
	// ======================================================
	// CONSTANTS
	// ======================================================
	private final int MIN_SCROLLING_DIST = 30; /* Bigger the number, slower the scrolling. */
	private static final long MIN_TOUCH_INTERVAL = 70;
	private static final long MAX_TOUCH_INTERVAL = 400;
	
	// ======================================================
	// SINGLETONS
	// ======================================================
	MainActivity activity;
	SceneManager instance;
	
	// ======================================================
	// FIELDS
	// ======================================================
	GameHUD hud;

	CameraManager cameraManager;

	protected Point touchPoint;
	protected Sprite background;

	protected Map map; /* Not used for now */
	
	private ScrollDetector scrollDetector;
	private Scene leftPanelScene;
	
	// ======================================================
	// DOUBLE TAP
	// ======================================================
	private long lastTouchTime;

	public GameScene() {

		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();
		
		cameraManager = instance.cameraManager;
		
		leftPanelScene = new Scene();
		
		Sprite panel = new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT,
				instance.mLeftPanelTextureRegion, activity.getVertexBufferObjectManager());
		
		leftPanelScene.setBackgroundEnabled(false);
		
		leftPanelScene.attachChild(panel);
		
		//this.setChildScene(leftPanelScene);

		lastTouchTime = 0;

		Sprite map = new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT,
				instance.mapTextureRegion, activity.getVertexBufferObjectManager());

		attachChild(map);
		
		hud = new GameHUD();
		activity.mCamera.setHUD(hud);
		
		createScrollDetector();
		
		setOnSceneTouchListener(this);
		
		setRegionButtons();

		setTouchAreaBindingOnActionDownEnabled(true);
		setOnSceneTouchListener(this);

	}

	private void setRegionButtons() {

		for(int i = 0; i < instance.NUMBER_OF_REGIONS; i++) {

			int x = (instance.regions[i].getStratCenter().x * MainActivity.CAMERA_WIDTH)/100;
			int y = (instance.regions[i].getStratCenter().y * MainActivity.CAMERA_HEIGHT)/100;
			instance.regions[i].button.setPosition(x, y);
			instance.regions[i].button.setScale((float) 0.5);
			attachChild(instance.regions[i].button);
			registerTouchArea(instance.regions[i].button);

		}

	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		
		switch(pSceneTouchEvent.getMotionEvent().getActionMasked()) {
		case TouchEvent.ACTION_UP:
			
			if( ((System.currentTimeMillis() - lastTouchTime) >  MIN_TOUCH_INTERVAL) &&
					((System.currentTimeMillis() - lastTouchTime) < MAX_TOUCH_INTERVAL) ) {
				
				/* Double tap */
				cameraManager.setAutomaticZoom(new Point((int) pSceneTouchEvent.getX(), 
						(int) pSceneTouchEvent.getY()));
				
			}
			
			if((System.currentTimeMillis() - lastTouchTime) >  MIN_TOUCH_INTERVAL)
			{
				lastTouchTime = System.currentTimeMillis();
			}

			
			break;
			
		}
		
		scrollDetector.onTouchEvent(pSceneTouchEvent);
		
		return true;
	}
	
	public void onRegionSelected() {
		
		for(Region region : instance.regions) {
			if(!region.isSelected()) {
				unregisterTouchArea(region.button);
				detachChild(region.button);
			}
		}
	}
	
	public void onRegionUnselected(Region pRegion) {
		
		unregisterTouchArea(pRegion.button);
		detachChild(pRegion.button);
		
		for(Region region : instance.regions) {
		
			registerTouchArea(region.button);
			attachChild(region.button);
			
		}
		
	}
	
	// ======================================================
	// SCROLL DETECTOR
	// ======================================================
	private void createScrollDetector() {
		
		scrollDetector = new SurfaceScrollDetector(this);
		scrollDetector.setTriggerScrollMinimumDistance(MIN_SCROLLING_DIST);
		scrollDetector.setEnabled(true);
		
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		
		Log.d("ScrollDetector", "Scrolling started");
		
		for(Region region : instance.regions) {
			unregisterTouchArea(region.button);
		}
		
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		
		Log.d("ScrollDetector", "Scrolling");
		
		Point p = new Point((int)(activity.mCamera.getCenterX() - pDistanceX),
				
				(int)(activity.mCamera.getCenterY() + pDistanceY));
		cameraManager.jumpTo(p);
		
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		
		Log.d("ScrollDetector", "Scrolling finished.");
		
		Point p = new Point((int)(activity.mCamera.getCenterX() - pDistanceX),
				
				(int)(activity.mCamera.getCenterY() + pDistanceY));
		cameraManager.jumpTo(p);
		
		for(Region region : instance.regions) {
			registerTouchArea(region.button);
		}
		
	}



}
