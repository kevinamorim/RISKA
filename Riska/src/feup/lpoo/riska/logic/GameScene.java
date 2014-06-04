package feup.lpoo.riska.logic;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
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

	public GameScene() {

		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();
		
		cameraManager = instance.cameraManager;

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
		scrollDetector.onTouchEvent(pSceneTouchEvent);
		return true;
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
