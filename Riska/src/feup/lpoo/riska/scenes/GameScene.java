package feup.lpoo.riska.scenes;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
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
import feup.lpoo.riska.io.LoadGame;
import feup.lpoo.riska.io.SaveGame;
import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.logic.GameLogic.GAME_STATE;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.SceneManager.SceneType;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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

	// ======================================================
	// SINGLETONS
	// ======================================================
	MainActivity activity;
	GameLogic logic;
	SceneManager sceneManager;
	CameraManager cameraManager;
	ResourceCache resources;
	
	BattleScene battleScene;
	
	// ======================================================
	// FIELDS
	// ======================================================
	GameHUD hud;
	DetailScene detailScene;
	
	protected Point touchPoint;
	
	private ScrollDetector scrollDetector;
	
	public Region selectedRegion;
	public Region targetedRegion;
	
	private boolean touchLocked;
	

	
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
		this.targetedRegion = null;
		
		activity = MainActivity.getSharedInstance();
		
		logic = new GameLogic();
		sceneManager = SceneManager.getSharedInstance();	
		cameraManager = CameraManager.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		detailScene = new DetailScene();

		lastTouchTime = 0;	
		
		LoadGame load = new LoadGame(activity, logic);
		
		if(load.checkLoadGame()) {
			
			activity.runOnUiThread(new Runnable() {
			     @Override
			     public void run() {


			         AlertDialog.Builder alert = new AlertDialog.Builder(activity);
			         alert.setTitle("");
			         alert.setMessage("Load previous game?");
			         alert.setPositiveButton("Yes", new OnClickListener() {
			                 @Override
			                 public void onClick(DialogInterface arg0, int arg1) {
			                	 loadGame();
			                 }
			         });
			         
			         alert.setNegativeButton("No", null);

			         alert.show();
			     }
			    });
			
		}
		
		createDisplay();
		

		
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		
		super.onManagedUpdate(pSecondsElapsed);
		
		switch(logic.getState()) {
		case PAUSED:
			logic.setState(GAME_STATE.DEPLOYMENT);
			break;
			
		case DEPLOYMENT:
			logic.updateDeployment();
			break;
			
		case PLAY:
			//logic.updateGame();
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
		
		if(touchLocked) {
			return false;
		}
		
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
		
		if(touchLocked) {
			return;
		}
		
		switch(logic.getState()) {
		
		case DEPLOYMENT:
			onDeploymentHandler(pRegion);
			break;
			
		case PLAY:
			onPlayHandler(pRegion);
			break;
			
		default:
			break;
			
		}
	}

	public void targetRegion(Region pRegion) {
		if(selectedRegion != null) {
			
			if(pRegion.isNeighbourOf(selectedRegion)) {
				
				if(targetedRegion != null) {
					targetedRegion.unfocus();
					targetedRegion = null;
					
					hud.hideAttackButton();
				}
				
				targetedRegion = pRegion;
				
				pRegion.focus();

				detailScene.setAttributes(selectedRegion, targetedRegion);
				
				if(!touchLocked) {
					hud.showAttackButton();	
					setInfoTabToProceedToAttack();
				}
					
				Log.d("Regions", "Targeted: " + pRegion.getName());
			}
		}
	}
	
	private void untargetRegion(Region pRegion) {
		targetedRegion = null;
		
		pRegion.unfocus();
		
		detailScene.setAttributes(selectedRegion, null);
		hud.hideAttackButton();
		
		setInfoTabToChooseEnemyRegion();
		
		for(int i = 0; i < logic.getMap().getRegions().size(); i++) {
			showRegionButton(logic.getMap().getRegions().get(i));
		}
	}

	public void selectRegion(Region pRegion) {
		
		if(selectedRegion != null) {
			selectedRegion.unfocus();
			selectedRegion = null;
			
			if(targetedRegion != null) {
				targetedRegion.unfocus();
				targetedRegion = null;
			}
			
			hud.hideAttackButton();
			hud.hideDetailButton();
		}
		
		selectedRegion = pRegion;
		
		detailScene.setAttributes(selectedRegion, null);
		
		if(!touchLocked) {
			hud.showDetailButton();
			setInfoTabToChooseEnemyRegion();
		}

		pRegion.focus();

		for(int i = 0; i < logic.getMap().getRegions().size(); i++) {

			Region r = logic.getMap().getRegions().get(i);

			if( (!pRegion.getNeighbours().contains(r) && !r.equals(selectedRegion))
					|| (r.getOwner().equals(logic.getPlayers().get(0)) && !r.equals(selectedRegion))
					) {

				hideRegionButton(logic.getMap().getRegions().get(i));

			}
		}

		Log.d("Regions", "Selected: " + pRegion.getName());
	}
	
	private void unselectRegion(Region pRegion) {	

		if(targetedRegion != null) {
			targetedRegion.unfocus();
			targetedRegion = null;
			
			hud.hideAttackButton();
		}
		
		detailScene.setAttributes(null, null);
		hud.hideDetailButton();
		
		selectedRegion = null;

		pRegion.unfocus();
		
		for(int i = 0; i < logic.getMap().getRegions().size(); i++) {
			showRegionButton(logic.getMap().getRegions().get(i));
		}
		
		setInfoTabToChooseOwnRegion();
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

	// ======================================================
	// ======================================================
	
	public void onAttack() {
		
		logic.attack(selectedRegion, targetedRegion);
		
	}
	
	public void showBattleScene(boolean result) {
		
		hud.hideAttackButton();
		hud.showDetailButton();
		hud.changeDetailButton();
		
		Region tmpSelectedRegion = new Region(-1, selectedRegion.getName(), selectedRegion.getStratCenter(), "");
		tmpSelectedRegion.setOwner(selectedRegion.getOwner());
		Region tmpTargetedRegion = new Region(-1, targetedRegion.getName(), targetedRegion.getStratCenter(), "");
		tmpTargetedRegion.setOwner(targetedRegion.getOwner());
		
		battleScene = new BattleScene(tmpSelectedRegion, tmpTargetedRegion, result);
		
		doubleTapAllowed = false;
		scrollDetector.setEnabled(false);
		
		hud.hideInfoTab();
		
		attachChild(battleScene);
		
	}
	
	public void hideBattleScene() {
		
		detachChild(battleScene);
	
		hud.showInfoTab();
		hud.hideDetailButton();
		hud.hideAttackButton();
		
		battleScene = null;
//		selectedRegion = null;
//		targetedRegion = null;
		unselectRegion(selectedRegion);
		
		setInfoTabToChooseOwnRegion();
		
		doubleTapAllowed = true;
		scrollDetector.setEnabled(true);
		
		logic.turnDone = true;
		
		logic.updateGame();
			
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
	
	public BattleScene getBattleScene() {
		return battleScene;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	public Region getSelectedRegion() {
		return selectedRegion;
	}

	public Region getTargetedRegion() {
		return targetedRegion;
	}
	
	
	// ======================================================
	// onRegionTouch Handlers
	// ======================================================
	private void onDeploymentHandler(Region pRegion) {
		
		if(logic.getCurrentPlayer().hasSoldiersLeftToDeploy()) {
			
			if(logic.getCurrentPlayer().ownsRegion(pRegion)) {
				int deployed = logic.getCurrentPlayer().deploySoldiers(SOLDIER_INC);
				pRegion.addSoldiers(deployed);
				
				hud.setInfoTabText(logic.getCurrentPlayer().getSoldiersToDeploy() + " left to deploy");
			}
			
		}
		
	}
	
	private void onPlayHandler(Region pRegion) {
		
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
		
		
	}
	
	
	// ======================================================
	// INFO TAB
	// ======================================================
	public void setInfoTabToChooseOwnRegion() {
		hud.setInfoTabText("Tap a region to select.");
	}
	
	public void setInfoTabToChooseEnemyRegion() {
		hud.setInfoTabText("Tap an enemy neighbour.");
	}
	
	public void setInfoTabToProceedToAttack() {
		hud.setInfoTabText("Attack! Attack!");
	}
	
	public void setInfoTabToWait() {
		hud.setInfoTabText("Wait...");
	}
	
	public void simulateCPU(float pDelay, final Region pRegion1, final Region pRegion2) {
		
		lockUserInput();
		hud.lockHUD();
		setInfoTabToWait();
		
		DelayModifier selectRegionMod = new DelayModifier(pDelay) {
			
			@Override
			protected void onModifierFinished(IEntity pItem) {
				pRegion1.focus();
				
			}
			
		};
		
		DelayModifier targetRegionMod = new DelayModifier(pDelay * 2) {
			
			@Override
			protected void onModifierFinished(IEntity pItem) {
				pRegion2.focus();
			}
			
		};
		
		DelayModifier attackMod = new DelayModifier(pDelay * 3) {
			
			@Override
			protected void onModifierFinished(IEntity pItem) {
				
				selectRegion(pRegion1);
				targetRegion(pRegion2);
				
				unlockUserInput();
				hud.unlockHUD();
				logic.attack(pRegion1, pRegion2);
				
				// untargetRegion(pRegion1);
				// unselectRegion(pRegion1);
			}
			
		};
		
		
		registerEntityModifier(selectRegionMod);
		registerEntityModifier(targetRegionMod);
		registerEntityModifier(attackMod);

		
	}
	
	public void lockUserInput() {
		
		doubleTapAllowed = false;
		scrollDetector.setEnabled(false);
		touchLocked = true;
		
	}
	
	public void unlockUserInput() {
		
		doubleTapAllowed = true;
		scrollDetector.setEnabled(true);
		touchLocked = false;
		
	}

	public void saveGame() {
		
		if(logic.getState() != GAME_STATE.PAUSED && logic.getState() != GAME_STATE.DEPLOYMENT) {
			new SaveGame(activity, logic);
		}
		
	}
	
	public void loadGame() {
		
		LoadGame load = new LoadGame(activity, logic);
		
		load.load();
		
	}

	// ======================================================
	// REGION BUTTONS
	// ======================================================
	private void hideRegionButton(Region pRegion) {
		
		if(pRegion.getButton().isVisible()) {
			
			pRegion.getButton().setVisible(false);
			
			if(getTouchAreas().contains(pRegion.getButton())) {
				unregisterTouchArea(pRegion.getButton());
			}
			
		}	
	}
	
	private void showRegionButton(Region pRegion) {
		
		if(!pRegion.getButton().isVisible()) {
			
			pRegion.getButton().setVisible(true);
			
			if(!getTouchAreas().contains(pRegion.getButton())) {
				registerTouchArea(pRegion.getButton());
			}
		
		}
	}
	
	
}


