package feup.lpoo.riska.scenes;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.HUD.GameHUD;
import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Player;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.generator.BattleGenerator;
import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.logic.GameLogic.GAME_STATE;
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
	
	private static final int ANIM = 250;
	
	private static final int SOLDIER_INC = 1;
	
	private static final int FIRST = 0;
	
	// ======================================================
	// SINGLETONS
	// ======================================================
	MainActivity activity;
	GameLogic logic;
	SceneManager sceneManager;
	CameraManager cameraManager;
	ResourceCache resources;
	BattleGenerator battleGenerator;
	
	// ======================================================
	// FIELDS
	// ======================================================
	GameHUD hud;
	DetailScene detailScene;
	
	protected Point touchPoint;
	
	private ScrollDetector scrollDetector;
	
	private Region focusedRegion;
	
	protected Region selectedRegion;
	protected Region targetedRegion;
	
	// ======================================================
	// DOUBLE TAP
	// ======================================================
	private boolean doubleTapAllowed = true;
	private long lastTouchTime;

	// ====================================================== //
	// ====================================================== //
	// ====================================================== //
	public GameScene() {	
		
		this.selectedRegion = null;
		this.focusedRegion = null;
		this.targetedRegion = null;
		
		activity = MainActivity.getSharedInstance();
		logic = new GameLogic();
		sceneManager = SceneManager.getSharedInstance();	
		cameraManager = CameraManager.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		
		battleGenerator = new BattleGenerator();
		detailScene = new DetailScene();

		lastTouchTime = 0;	
		
		createDisplay();
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		
		switch(logic.getState()) {
		case PAUSED:
			logic.setState(GAME_STATE.DEPLOYMENT);
			break;
			
		case DEPLOYMENT:
			logic.updateDeployment();
			break;
			
		case PLAY:
			break;
			
		default:
			break;
		}
	}

	private void createDisplay() {
		
		Sprite mapSprite = new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT,
				resources.getMapTexture(), activity.getVertexBufferObjectManager());
		
		AnimatedSprite background = new AnimatedSprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
				resources.getSeaTexture(), activity.getVertexBufferObjectManager());
		background.setScale(2f);
		long duration[] = { ANIM, ANIM, ANIM, ANIM, ANIM, ANIM, ANIM, ANIM, };
		background.animate(duration, 0, 7, true);
		
		attachChild(background);
		
		attachChild(mapSprite);
		
		hud = new GameHUD();
		activity.mCamera.setHUD(hud);
		
		hud.setInfoTabText(logic.getCurrentPlayer().getSoldiersToDeploy() + " left to deploy");
		
		createScrollDetector();
		
		setRegionButtons();

		setTouchAreaBindingOnActionDownEnabled(true);
		setOnSceneTouchListener(this);
		
		/*
		 * Details Scene
		 */
		detailScene = new DetailScene();
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
	
	// =================================================================================
	//
	// =================================================================================
	public void onRegionTouched(Region pRegion) {
		
		switch(logic.getState()) {
		case DEPLOYMENT:
			
			if(logic.getCurrentPlayer().hasSoldiersLeftToDeploy()) {
				
				if(logic.getCurrentPlayer().ownsRegion(pRegion)) {
					int deployed = logic.getCurrentPlayer().deploySoldiers(SOLDIER_INC);
					pRegion.addSoldiers(deployed);
					
					hud.setInfoTabText(logic.getCurrentPlayer().getSoldiersToDeploy() + " left to deploy");
				}
				
			}

			break;
		case PLAY:
			if(!pRegion.isFocused()) {
				
				if(logic.getCurrentPlayer().ownsRegion(pRegion)) {
					selectRegion(pRegion);
				}
				else {
					targetRegion(pRegion);
				}
	
			}
			else {

				if(logic.getCurrentPlayer().ownsRegion(pRegion)) {
					unselectRegion(pRegion);
				}
				else {
					untargetRegion(pRegion);
				}
				
			}	
			
			break;
			
		default:
			break;
		}
	}

	private void targetRegion(Region pRegion) {
		if(selectedRegion != null) {
			
			if(pRegion.isNeighbourOf(selectedRegion)) {
				
				if(targetedRegion != null) {
					targetedRegion.changeFocus(false);
					targetedRegion = null;
					
					hud.hideAttackButton();
				}
				
				targetedRegion = pRegion;
				
				pRegion.changeFocus(true);

				detailScene.setAttributes(selectedRegion, targetedRegion);
				hud.showAttackButton();	
				
				Log.d("Regions", "Targeted: " + pRegion.getName());
			}
		}
	}
	
	private void untargetRegion(Region pRegion) {
		targetedRegion = null;
		
		pRegion.changeFocus(false);
		
		detailScene.setAttributes(selectedRegion, null);
		hud.hideAttackButton();
	}

	private void selectRegion(Region pRegion) {
		
		if(selectedRegion != null) {
			selectedRegion.changeFocus(false);
			selectedRegion = null;
			
			if(targetedRegion != null) {
				targetedRegion.changeFocus(false);
				targetedRegion = null;
			}
			
			hud.hideAttackButton();
			hud.hideDetailButton();
		}
		
		selectedRegion = pRegion;
		
		detailScene.setAttributes(selectedRegion, null);
		hud.showDetailButton();
		
		pRegion.changeFocus(true);
		
		Log.d("Regions", "Selected: " + pRegion.getName());
	}
	
	private void unselectRegion(Region pRegion) {	

		if(targetedRegion != null) {
			targetedRegion.changeFocus(false);
			targetedRegion = null;
			
			hud.hideAttackButton();
		}
		
		detailScene.setAttributes(null, null);
		hud.hideDetailButton();
		
		selectedRegion = null;

		pRegion.changeFocus(false);
	}

	public void onRegionConfirmed() {
		
		if(logic.getCurrentPlayer().ownsRegion(focusedRegion)) {

			if(focusedRegion == selectedRegion) {
				//focusedRegion.setColor(Color.GREEN);
				selectedRegion = null;
			}
			else {
				if(selectedRegion != null) {
					//selectedRegion.setColor(Color.GREEN);
				}
				
				selectedRegion = focusedRegion;
				//selectedRegion.setColor(Color.BLUE);
				
				Log.d("Regions","Confirmed");
				Log.d("Regions","  > " + selectedRegion.getName());
			}

			//unfocusRegion(focusedRegion);
		} else {		
			//targetedRegion = focusedRegion;
			//onAttackRegion(selectedRegion, targetedRegion);
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

	public void onAttack() {
		// TODO Auto-generated method stub
	}

	public void showInitialHUD() {
		hud.showInfoTab();
	}
	
	public void showDetailPanel() {
		doubleTapAllowed = false;
		scrollDetector.setEnabled(false);
		
		if(targetedRegion != null) {
			hud.hideAttackButton();
		}
		hud.hideInfoTab();
		
		attachChild(detailScene);
	}
	
	public void hideDetailPanel() {
		doubleTapAllowed = true;
		scrollDetector.setEnabled(true);
		
		if(targetedRegion != null) {
			hud.showAttackButton();
		}
		hud.showInfoTab();
		
		detachChild(detailScene);
	}

	public DetailScene getDetailScene() {
		return detailScene;
	}

	// ======================================================
	// HUD
	// ======================================================
	public void setInfoTabText(String pText) {
		hud.setInfoTabText(pText);
	}
}
