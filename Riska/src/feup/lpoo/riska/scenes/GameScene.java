package feup.lpoo.riska.scenes;

import java.util.ArrayList;

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
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.SceneManager.SceneType;
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
	
	private static final int INITIAL_SOLDIERS = 1;
	private static final int SOLDIER_INC = 1;
	
	private enum Fase {
		DEPLOYMENT,
		PLAY
	}
	
	// ======================================================
	// SINGLETONS
	// ======================================================
	MainActivity activity;
	SceneManager sceneManager;
	CameraManager cameraManager;
	ResourceCache resources;
	BattleGenerator battleGenerator;
	
	// ======================================================
	// FIELDS
	// ======================================================
	GameHUD hud;
	
	protected Point touchPoint;
	
	private ScrollDetector scrollDetector;
	
	private ArrayList<Player> players;
	private Player currentPlayer;
	
	private Region selectedRegion;
	private Region focusedRegion;
	private Region targetedRegion;
	
	private Fase fase;
	
	private Map map;
	
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
		sceneManager = SceneManager.getSharedInstance();	
		cameraManager = CameraManager.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		
		battleGenerator = new BattleGenerator();

		lastTouchTime = 0;	
		
		createDisplay();
		
		createGameElements();
		
		fase = Fase.DEPLOYMENT;
		
		/*
		 * Enter game loop
		 */
		gameLoop();
	}

	private void createGameElements() {
		/*
		 * Gets the map
		 */
		map = resources.getMap();
		
		/*
		 * Creates Players
		 */
		players = new ArrayList<Player>();
		
		Player player = createPlayer(false);
		player.setSoldiersToDeploy(10);
		
		Player cpu = createPlayer(true);
		cpu.setSoldiersToDeploy(10);
		
		players.add(player);
		players.add(cpu);
		
		currentPlayer = player;
		
		/*
		 * Distributes the regions amongst the players
		 */
		handOutRegions();
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
		hud.hide();
		
		createScrollDetector();
		
		setRegionButtons();

		setTouchAreaBindingOnActionDownEnabled(true);
		setOnSceneTouchListener(this);	
	}

	private void gameLoop() {
		// TODO : game loop - VERY IMPORTANT !! (no shit, sherlock)
	}

	private Player createPlayer(boolean isCPU) {
		return new Player(isCPU);
	}
	
	private void handOutRegions() {
		
		int i = 0;
		
		for(Region region : map.getRegions()) {
			
			players.get(i).addRegion(region);
			
			region.setNumberOfSoldiers(INITIAL_SOLDIERS);
			region.updateSoldiers();
			
			region.setOwner(players.get(i));
			
			if(players.get(i).isCPU()) {
				region.setColor(Color.RED);
			}
			else {
				region.setColor(Color.GREEN);
			}
			
			i++;
			i = i % players.size(); 
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
	
	public void onRegionTouched(Region pRegion) {
		
		switch(fase) {
		case DEPLOYMENT:
			if(currentPlayer.ownsRegion(pRegion) && currentPlayer.getSoldiersToDeploy() > 0) {
				int deployed = currentPlayer.deploySoldiers(SOLDIER_INC);
				pRegion.addSoldiers(deployed);
				pRegion.updateSoldiers();
			}
			else {
				// TODO : next player's fase of deployment
			}
			break;
		case PLAY:
			if(!pRegion.isFocused()) {
				
				focusRegion(pRegion);
				
				Log.d("Regions","Region focused.");
				Log.d("Regions","  > " + (focusedRegion != null ? focusedRegion.getName() : "null"));
			}
			else {
				Log.d("Regions","Region unselected.");
				Log.d("Regions","  > " + (focusedRegion != null ? focusedRegion.getName() : "null"));
				
				unfocusRegion(pRegion);
			}	
			break;
		}
	}
	
	public void onRegionConfirmed() {
		
		if(currentPlayer.ownsRegion(focusedRegion)) {

			if(focusedRegion == selectedRegion) {
				focusedRegion.setColor(Color.GREEN);
				selectedRegion = null;
			}
			else {
				if(selectedRegion != null) {
					selectedRegion.setColor(Color.GREEN);
				}
				
				selectedRegion = focusedRegion;
				selectedRegion.setColor(Color.BLUE);
				
				Log.d("Regions","Confirmed");
				Log.d("Regions","  > " + selectedRegion.getName());
			}

			unfocusRegion(focusedRegion);
		} else {		
			targetedRegion = focusedRegion;
			//onAttackRegion(selectedRegion, targetedRegion);
		}
	}
	
	
	private void onAttackRegion(Region attacker, Region defensor) {
		
		battleGenerator.createBattleRegions(attacker, defensor);
		sceneManager.setCurrentScene(SceneType.BATTLE);
	}

	private void focusRegion(Region pRegion) {
		
		pRegion.setFocused(true);
		
		cameraManager.focusOnRegion(pRegion);
		
		for(Region region : resources.getMap().getRegions()) {
			
			if(region != pRegion) {
				unregisterTouchArea(region.getButton());
				detachChild(region.getButton());
			}
		}
		
		focusedRegion = pRegion;
		
		hud.updateCountry(focusedRegion);
		hud.updateButton(focusedRegion, selectedRegion, currentPlayer);
		hud.show();
		
		doubleTapAllowed = false;
		scrollDetector.setEnabled(false);	
	}
	
	private void unfocusRegion(Region pRegion) {
		pRegion.setFocused(false);
		
		cameraManager.zoomOut();
		cameraManager.panToCenter();
		
		for(Region region : resources.getMap().getRegions()) {
		
			if(region != pRegion) {
				registerTouchArea(region.getButton());
				attachChild(region.getButton());
			}	
		}
		
		focusedRegion = null;	

		hud.hide();

		doubleTapAllowed = true;
		scrollDetector.setEnabled(true);
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
