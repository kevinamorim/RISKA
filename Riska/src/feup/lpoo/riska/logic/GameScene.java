package feup.lpoo.riska.logic;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.IBackground;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.util.adt.color.Color;

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
	
	private ScrollDetector scrollDetector;
	
	// ======================================================
	// DOUBLE TAP
	// ======================================================
	private boolean doubleTapAllowed = true;
	private long lastTouchTime;

	public GameScene() {
		
		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();
		
		cameraManager = instance.cameraManager;

		lastTouchTime = 0;

		Sprite map = new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT,
				instance.mapTextureRegion, activity.getVertexBufferObjectManager());
		
		AnimatedSprite background = new AnimatedSprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
				instance.mSeaTiledTextureRegion, 
				activity.getVertexBufferObjectManager());
		background.setScale(2f);
		long duration[] = { 1000, 1000, 1000, 1000, 1000, 1000 };
		background.animate(duration, 0, 5, true);
		attachChild(background);
		
		attachChild(map);
		
		hud = new GameHUD();
		activity.mCamera.setHUD(hud);
		
		hud.hide();
		
		createScrollDetector();
		
		setRegionButtons();

		setTouchAreaBindingOnActionDownEnabled(true);
		setOnSceneTouchListener(this);

	}

	private void setRegionButtons() {

		for(int i = 0; i < instance.NUMBER_OF_REGIONS; i++) {

			int x = (instance.map.getRegions()[i].getStratCenter().x * MainActivity.CAMERA_WIDTH)/100;
			int y = (instance.map.getRegions()[i].getStratCenter().y * MainActivity.CAMERA_HEIGHT)/100;
			instance.map.getRegions()[i].button.setPosition(x, y);
			instance.map.getRegions()[i].button.setScale((float) 0.5);
			attachChild(instance.map.getRegions()[i].button);
			registerTouchArea(instance.map.getRegions()[i].button);

		}

	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		
		switch(pSceneTouchEvent.getMotionEvent().getActionMasked()) {
		case TouchEvent.ACTION_UP:
			
			if( ((System.currentTimeMillis() - lastTouchTime) >  MIN_TOUCH_INTERVAL) &&
					((System.currentTimeMillis() - lastTouchTime) < MAX_TOUCH_INTERVAL) && doubleTapAllowed ) {
				
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
		
		for(Region region : instance.map.getRegions()) {
			if(!region.isSelected()) {
				unregisterTouchArea(region.button);
				detachChild(region.button);
			} else {
				hud.updateHUD(region);
			}
		}
		
		doubleTapAllowed = false;
		scrollDetector.setEnabled(false);
		hud.show();
	}
	
	public void onRegionUnselected(Region pRegion) {
		
		unregisterTouchArea(pRegion.button);
		detachChild(pRegion.button);
		
		for(Region region : instance.map.getRegions()) {
		
			registerTouchArea(region.button);
			attachChild(region.button);
			
		}
		
		doubleTapAllowed = true;
		scrollDetector.setEnabled(true);
		hud.hide();

		
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
		
		for(Region region : instance.map.getRegions()) {
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
		
		for(Region region : instance.map.getRegions()) {
			registerTouchArea(region.button);
		}
		
	}



}
