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
		Player cpu = createPlayer(true);
		
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
		
	}

	private Player createPlayer(boolean isCPU) {
		return new Player(isCPU);
	}
	
	private void handOutRegions() {
		
		int i = 0;
		
		for(Region region : map.getRegions()) {
			
			players.get(i).addRegion(region);
			region.setNumberOfSoldiers(1);
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
	
	public void onRegionSelected(Region pRegion) {
		
		String hudButtonText;
		boolean enabled = true;
		
		focusedRegion = pRegion;
	
		if(focusedRegion.playerIsOwner(currentPlayer)) {

			if(focusedRegion == selectedRegion) {
				hudButtonText = "UNSELECT";
			}
			else {
				hudButtonText = "SELECT";
			}
		}
		else {
			hudButtonText = "ATTACK";

			if(selectedRegion != null && selectedRegion.isNeighbourOf(focusedRegion)) {
				enabled = false;
			}
		}

		hud.update(focusedRegion.getName(), hudButtonText, enabled);
		
		isolateRegion(pRegion);
		
		doubleTapAllowed = false;
		scrollDetector.setEnabled(false);
		hud.show();
	}
	
	public void onRegionUnselected(Region pRegion) {
		
		focusedRegion = null;
		
		deisolateRegion(pRegion);
		
		doubleTapAllowed = true;
		scrollDetector.setEnabled(true);
		hud.hide();
		
		Log.d("Regions","Region unselected.");
	}
	
	public void onRegionConfirmed() {	
		
		if(focusedRegion.playerIsOwner(currentPlayer)) {
			
			focusedRegion.setSelected(focusedRegion.isSelected() ? false : true);
			selectedRegion = (focusedRegion.isSelected() ? focusedRegion : null);
			
		} else {
			
			targetedRegion = focusedRegion;
			//onAttackRegion(selectedRegion, targetedRegion);
			
		}
		
		Log.d("Regions","Confirmed");
		Log.d("Regions","  > " + selectedRegion.getName());
	}
	
	
	private void onAttackRegion(Region attacker, Region defensor) {
		
		battleGenerator.createBattleRegions(attacker, defensor);
		sceneManager.setCurrentScene(SceneType.BATTLE);
	}

	private void isolateRegion(Region pRegion) {
		cameraManager.focusOnRegion(pRegion);
		for(Region region : resources.getMap().getRegions()) {
			
			if(region != pRegion) {
				unregisterTouchArea(region.getButton());
				detachChild(region.getButton());
			}
		}
	}
	
	private void deisolateRegion(Region pRegion) {
		cameraManager.zoomOut();
		cameraManager.panToCenter();
		
		for(Region region : resources.getMap().getRegions()) {
		
			if(region != pRegion) {
				registerTouchArea(region.getButton());
				attachChild(region.getButton());
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
