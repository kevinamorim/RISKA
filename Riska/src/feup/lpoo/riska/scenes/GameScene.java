package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import feup.lpoo.riska.HUD.GameHUD;
import feup.lpoo.riska.elements.Player;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.resources.ResourceCache;
import android.graphics.Point;
import android.util.Log;

public class GameScene extends Scene implements IOnSceneTouchListener, IScrollDetectorListener {
	
	// ======================================================
	// CONSTANTS
	// ======================================================
	private final int MIN_SCROLLING_DIST = 30; /* Bigger the number, slower the scrolling. */
	private static final long MIN_TOUCH_INTERVAL = 70;
	private static final long MAX_TOUCH_INTERVAL = 400;
	
	private static final int ANIM = 100;
	
	// ======================================================
	// SINGLETONS
	// ======================================================
	MainActivity activity;
	SceneManager sceneManager;
	CameraManager cameraManager;
	ResourceCache resources;
	
	// ======================================================
	// FIELDS
	// ======================================================
	GameHUD hud;
	
	protected Point touchPoint;
	
	private ScrollDetector scrollDetector;
	
	private Player player;
	
	// ======================================================
	// DOUBLE TAP
	// ======================================================
	private boolean doubleTapAllowed = true;
	private long lastTouchTime;

	public GameScene() {
		
		activity = MainActivity.getSharedInstance();
		sceneManager = SceneManager.getSharedInstance();	
		cameraManager = CameraManager.getSharedInstance();
		resources = ResourceCache.getSharedInstance();

		lastTouchTime = 0;

		Sprite map = new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT,
				resources.getMapTexture(), activity.getVertexBufferObjectManager());
		
		AnimatedSprite background = new AnimatedSprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
				resources.getSeaTexture(), activity.getVertexBufferObjectManager());
		background.setScale(2f);
		long duration[] = { ANIM, ANIM, ANIM, ANIM, ANIM, ANIM, ANIM, ANIM, };
		background.animate(duration, 0, 7, true);
		
		attachChild(background);
		
		attachChild(map);
		
		hud = new GameHUD();
		activity.mCamera.setHUD(hud);
		
		hud.hide();
		
		createScrollDetector();
		
		setRegionButtons();

		setTouchAreaBindingOnActionDownEnabled(true);
		setOnSceneTouchListener(this);
		
		createPlayer();

	}

	private void createPlayer() {
		player = new Player(0);
		
		boolean toPlayer = true; /* Is next region to be given to the player. */
		for(Region region : resources.getMap().getRegions()) {
			if(toPlayer) {
				player.addRegion(region);
			}
			toPlayer = !toPlayer;
			region.updateHudButtonText(player);
		}
		
	}

	private void setRegionButtons() {

		for(Region region : resources.getMap().getRegions()) {

			int x = (region.getStratCenter().x * MainActivity.CAMERA_WIDTH)/100;
			int y = (region.getStratCenter().y * MainActivity.CAMERA_HEIGHT)/100;
			
			region.getButton().setPosition(x, y);
			region.getButton().setScale((float) 0.5);
			
			attachChild(region.getButton());
			registerTouchArea(region.getButton());
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
		
		for(Region region : resources.getMap().getRegions()) {
			if(!region.isSelected()) {
				unregisterTouchArea(region.getButton());
				detachChild(region.getButton());
			} else {
				hud.updateButtonText(player.ownsRegion(region));
				hud.updateHUD(region);
			}
		}
		
		doubleTapAllowed = false;
		scrollDetector.setEnabled(false);
		hud.show();
	}
	
	public void onRegionUnselected(Region pRegion) {
		
		unregisterTouchArea(pRegion.getHudButton());
		detachChild(pRegion.getHudButton());
		
		for(Region region : resources.getMap().getRegions()) {
		
			if(region != pRegion) {
				registerTouchArea(region.getButton());
				attachChild(region.getButton());
			}	
		}
		
		doubleTapAllowed = true;
		scrollDetector.setEnabled(true);
		hud.hide();
		
		Log.d("Regions","Region unselected.");
	}
	
	public void onRegionConfirmed(Region pRegion) {
		
		if(player.ownsRegion(pRegion)) {
			player.setRegionSelected(pRegion);
			player.setRegionToAttack(null);
		} else {
			if(player.getRegionSelected() != null) {
				player.setRegionToAttack(pRegion);
			} 
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
		
		for(Region region : resources.getMap().getRegions()) {
			unregisterTouchArea(region.getButton());
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
		
		for(Region region : resources.getMap().getRegions()) {
			registerTouchArea(region.getButton());
		}
		
	}



}
