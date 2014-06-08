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
	
	private static final int INITIAL_SOLDIERS_IN_REGION = 1;
	private static final int SOLDIER_INC = 1;
	
	private static final int FIRST = 0;
	
	private enum Fase {
		NONE,
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
	DetailScene detailScene;
	
	private float BONUS_FACTOR = 0.1f;
	
	protected Point touchPoint;
	
	private ScrollDetector scrollDetector;
	
	private ArrayList<Player> players;
	private Player currentPlayer;
	
	private Region focusedRegion;
	
	protected Region selectedRegion;
	protected Region targetedRegion;
	
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
		detailScene = new DetailScene();

		lastTouchTime = 0;	
		
		createGameElements();
		
		fase = Fase.NONE;
		
		createDisplay();
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		
		switch(fase) {
		case NONE:
			fase = Fase.DEPLOYMENT;
			break;
			
		case DEPLOYMENT:
			if(!currentPlayer.hasSoldiersLeftToDeploy()) {
				if(chooseNextPlayer() == FIRST) {
					fase = Fase.PLAY;
				}
				
				if(currentPlayer != getHumanPlayer()) {
					hud.setInfoTabText("Wait for deployment");
					
					if(currentPlayer.isCPU()) {
						currentPlayer.deploy(); // TODO
					}
				}
			}
			break;
			
		case PLAY:
			break;
			
		default:
			break;
		}
	}

	private void createGameElements() {
		/*
		 * Gets the map
		 */
		map = resources.getMap();
		int MAX_SOLDIERS_TO_DEPLOY = (int) (map.getNumberOfRegions() * BONUS_FACTOR);
		
		/*
		 * Creates Players
		 */
		players = new ArrayList<Player>();
		
		Player player = createPlayer(false, new Color(0f, 0.6f, 0f), new Color(1f, 1f, 0f));
		player.setSoldiersToDeploy(MAX_SOLDIERS_TO_DEPLOY);
		
		Player cpu = createPlayer(true, new Color(1f, 0f, 0f), new Color(1f, 1f, 0f));
		cpu.setSoldiersToDeploy(MAX_SOLDIERS_TO_DEPLOY);
		
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
		
		hud.setInfoTabText(currentPlayer.getSoldiersToDeploy() + " left to deploy");
		
		createScrollDetector();
		
		setRegionButtons();

		setTouchAreaBindingOnActionDownEnabled(true);
		setOnSceneTouchListener(this);
		
		/*
		 * Details Scene
		 */
		detailScene = new DetailScene();
	}

	private Player createPlayer(boolean isCPU, Color priColor, Color secColor) {
		return new Player(isCPU, priColor, secColor);
	}
	
	private void handOutRegions() {
		
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		
		Random random = new Random();
		
		while(indexes.size() < map.getNumberOfRegions()) {
			int index = random.nextInt(map.getNumberOfRegions());
			if(!indexes.contains(index)) {
				indexes.add(index);
			}
		}
		
		int i = 0;
		
		for(Integer index : indexes) {
			
			Region region = map.getRegions().get(index);
			Player player = players.get(i);
			
			player.addRegion(region);
			
			region.setNumberOfSoldiers(INITIAL_SOLDIERS_IN_REGION);
			region.updateSoldiers();
			
			region.setOwner(player);
			region.setColors(player.getPrimaryColor(), player.getScondaryColor());
			
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
	
	// =================================================================================
	//
	// =================================================================================
	public void onRegionTouched(Region pRegion) {
		
		switch(fase) {
		case DEPLOYMENT:
			
			if(currentPlayer.hasSoldiersLeftToDeploy()) {
				
				if(currentPlayer.ownsRegion(pRegion)) {
					int deployed = currentPlayer.deploySoldiers(SOLDIER_INC);
					pRegion.addSoldiers(deployed);
					
					hud.setInfoTabText(currentPlayer.getSoldiersToDeploy() + " left to deploy");
				}
				
			}

			break;
		case PLAY:
			if(!pRegion.isFocused()) {
				
				if(currentPlayer.ownsRegion(pRegion)) {
					selectRegion(pRegion);
				}
				else {
					targetRegion(pRegion);
				}
					
				//focusRegion(pRegion);
			}
			else {

				if(currentPlayer.ownsRegion(pRegion)) {
					unselectRegion(pRegion);
				}
				else {
					untargetRegion(pRegion);
				}
				
				//unfocusRegion(pRegion);
			}	
			break;
		default:
			break;
		}
	}
	
	private int chooseNextPlayer() {
		
		for(int i = 0; i < players.size(); i++) {
			if(currentPlayer == getPlayer(i)) {
				i++;
				i = i % players.size();
				currentPlayer = getPlayer(i);
				
				return i;
			}
		}
		
		return -1;
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
		
		if(currentPlayer.ownsRegion(focusedRegion)) {

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
	
	/*
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
	*/
	
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
	
	private Player getPlayer(int index) {
		return players.get(index);
	}

	private Player getHumanPlayer() {
		return players.get(0);
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

}
