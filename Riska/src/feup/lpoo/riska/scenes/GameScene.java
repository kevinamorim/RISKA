package feup.lpoo.riska.scenes;

import java.util.ArrayList;
import java.util.Arrays;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;

import feup.lpoo.riska.HUD.GameHUD;
import feup.lpoo.riska.HUD.GameHUD.BUTTON;
import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.io.LoadGame;
import feup.lpoo.riska.io.SaveGame;
import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.logic.GameLogic.GAME_STATE;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.scenes.SceneManager.SceneType;
import feup.lpoo.riska.utilities.Utilities;
import feup.lpoo.riska.R;
import android.graphics.Point;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;

public class GameScene extends BaseScene implements IOnSceneTouchListener, IScrollDetectorListener {

	// ======================================================
	// CONSTANTS
	// ======================================================
	private final int MIN_SCROLLING_DIST = 30; /* Bigger the number, slower the scrolling. */
	private final long MIN_TOUCH_INTERVAL = 70;
	private final long MAX_TOUCH_INTERVAL = 400;
	
	private final int ANIM_DURATION = 250;
	
	private final int MIN_SOLDIERS_PER_REGION = 1;
	
	private final long REGION_BUTTON_MIN_TOUCH_INTERVAL = 30;
	private final int MAX_REGION_CHARS = 10;
	
	private final int PLAYER_NUM = 0;

	// ======================================================
	// SINGLETONS
	// ======================================================
	GameLogic logic;
	CameraManager cameraManager;	
	BattleScene battleScene;
	
	// ======================================================
	// FIELDS
	// ======================================================
	GameHUD hud;
	DetailScene detailScene;
	
	private Point touchPoint;
	
	private Map map;
	
	private ScrollDetector scrollDetector;
	
	private ArrayList<Pair<ButtonSprite,Text> > regionButtons;
	
	private boolean doubleTapAllowed = true;
	private long lastTouchTime;
	private long lastTouchTimeInRegion;

	
	@Override
	public void createScene() {
		logic = new GameLogic(this);
		detailScene = new DetailScene();
		battleScene = new BattleScene(null, null, false);
		
		map = resources.map;
		
		cameraManager = CameraManager.getSharedInstance();
		
		lastTouchTime = 0;
		lastTouchTimeInRegion = 0;
		
		createDisplay();
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getSharedInstance().loadMainMenuScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.GAME;
	}

	@Override
	public void disposeScene() {
		camera.setHUD(null);
	}
	
	// ======================================================
	// CREATE DISPLAY
	// ======================================================
	
	private void createDisplay() {
		
		createBackground();
		
		createMap();
		
		createHUD();
		
		createScrollDetector();
	
		createRegionButtons();
		
		detailScene.setVisible(false);
		attachChild(detailScene);
		
		battleScene.setVisible(false);
		attachChild(battleScene);

		setTouchAreaBindingOnActionDownEnabled(true);
		setOnSceneTouchListener(this);	
	}
	
	private void createMap() {
		
		Sprite mapSprite = new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT,
				resources.mapRegion, activity.getVertexBufferObjectManager());
		
		attachChild(mapSprite);
		
	}
	
	private void createBackground() {
		
		int ANIMATION_TILES = 8;
		
		AnimatedSprite background = new AnimatedSprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
				resources.seaRegion, activity.getVertexBufferObjectManager());
		
		background.setScale(2f);
		
		long duration[] = new long[ANIMATION_TILES];
		Arrays.fill(duration, ANIM_DURATION);
		
		background.animate(duration, 0, (ANIMATION_TILES - 1), true);
		
		attachChild(background);
		
	}
	
	private void createHUD() {
		
		hud = new GameHUD(this);
		activity.mCamera.setHUD(hud);
		
		hud.setInfoTabText(logic.getCurrentPlayer().getSoldiersToDeploy() 
				+ activity.getResources().getString(R.string.leftToDeploy));
		
		hud.show(BUTTON.AUTO_DEPLOY);
	}
	
	// ======================================================
	// ======================================================
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		
		super.onManagedUpdate(pSecondsElapsed);
		
		switch(logic.getState())
		{
		case PAUSED:
			logic.setState(GAME_STATE.DEPLOYMENT);
			break;
			
		case DEPLOYMENT:
			deploymentUpdate();
			logic.updateDeployment();
			break;
			
		case PLAY:
			gameUpdate();
			logic.updateGame();
			break;
			
		default:
			break;
		}
		
	}
	
	private void gameUpdate()
	{
		
		//hud.hide(BUTTON.AUTO_DEPLOY);
		
		if(detailScene.isVisible())
		{
			doubleTapAllowed = false;
			scrollDetector.setEnabled(false);
			hud.hide(BUTTON.ATTACK);
			hud.hideInfoTab();
			battleScene.setVisible(false);
			return;
		}
		
		if(battleScene.isVisible())
		{
			doubleTapAllowed = false;
			scrollDetector.setEnabled(false);
			hud.hide(BUTTON.ATTACK);
			hud.hideInfoTab();
			detailScene.setVisible(false);
			return;
		}
		
		doubleTapAllowed = true;
		scrollDetector.setEnabled(true);
		hud.show(BUTTON.DETAILS);
		hud.showInfoTab();
	}

	private void deploymentUpdate()
	{	
		doubleTapAllowed = false;
		scrollDetector.setEnabled(false);
		
		if(logic.getCurrentPlayerByIndex() == PLAYER_NUM)
		{
			hud.setInfoTabText(Utilities.getString(logic.getCurrentPlayer().getSoldiersToDeploy() + R.string.game_info_deployable));
		}
		else
		{
			hud.setInfoTabText(Utilities.getString(R.string.game_info_wait_for_CPU));
			hud.hide(BUTTON.AUTO_DEPLOY);
		}
		
		hud.hide(BUTTON.ATTACK);
		hud.hide(BUTTON.DETAILS);
	}

	private void createRegionButtons() {
		
		Text buttonText;
		ButtonSprite regionButton;

		for(Region region : resources.map.getRegions()) {

			int x = (region.getStratCenter().x * MainActivity.CAMERA_WIDTH)/100;
			int y = (region.getStratCenter().y * MainActivity.CAMERA_HEIGHT)/100;
			
			buttonText = new Text(0, 0, resources.mGameFont, "" + MIN_SOLDIERS_PER_REGION, MAX_REGION_CHARS, vbom);
			
			regionButton = new ButtonSprite(x, y, resources.regionBtnRegion, vbom) {

				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY) {

					switch(ev.getAction()) {
					case MotionEvent.ACTION_DOWN:
						pressedRegionButton(this, getTag());
						break;
					case MotionEvent.ACTION_UP:
						releasedRegionButton(this, getTag());
						break;
					case MotionEvent.ACTION_OUTSIDE:
						releasedRegionButton(this, getTag());
						break;
					}

					return true;
				}
			};
			
			regionButton.setTag(region.ID);
			regionButton.setPosition(x, y);
			regionButton.setScale((float) 0.5);
			
			buttonText.setScale((float) 1.4);
			buttonText.setPosition(regionButton.getWidth()/2, regionButton.getHeight()/2);
			regionButton.attachChild(buttonText);
			
			regionButtons.add(new Pair<ButtonSprite, Text>(regionButton, buttonText));
			
			attachChild(regionButton);
			registerTouchArea(regionButton);
		}
	}
	
	void pressedRegionButton(ButtonSprite btn, int regionID)
	{
		btn.setCurrentTileIndex(1);
	}
	
	void releasedRegionButton(ButtonSprite btn, int regionID)
	{
		long now = System.currentTimeMillis();

		if((now - lastTouchTimeInRegion) > MIN_TOUCH_INTERVAL) {

			btn.setCurrentTileIndex(0);

			logic.onRegionTouched(resources.map.getRegionById(regionID));
		}

		lastTouchTimeInRegion = System.currentTimeMillis();
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

	// ======================================================
	// SCROLL DETECTOR
	// ======================================================
	
	private void createScrollDetector() {
		
		scrollDetector = new SurfaceScrollDetector(this);
		scrollDetector.setTriggerScrollMinimumDistance(MIN_SCROLLING_DIST);
		scrollDetector.setEnabled(true);
		
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pX, float pY) {
		
		Log.d("ScrollDetector", "Scrolling started");
		
		unregisterTouchAreaForAllRegions();
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID, float pX, float pY) {
		
		Log.d("ScrollDetector", "Scrolling");
		
		Point p = new Point((int)(activity.mCamera.getCenterX() - pX), (int)(activity.mCamera.getCenterY() + pY));
		cameraManager.jumpTo(p);	
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID, float pX, float pY) {
		
		Log.d("ScrollDetector", "Scrolling finished.");
		
		Point p = new Point((int)(activity.mCamera.getCenterX() - pX), (int)(activity.mCamera.getCenterY() + pY));
		cameraManager.jumpTo(p);
		
		registerTouchAreaForAllRegions();
	}

	// ======================================================
	// ======================================================
	
	public void onAttack() {
		
		logic.attack(selectedRegion, targetedRegion);
		
	}
	
	public void onAutoDeploy()
	{
		logic.autoDeployment(logic.getPlayers().get(0));
	}
	
	public void createBattleScene(boolean result) {
		
		hud.hideAttackButton();
		hud.showDetailButton();
		hud.changeDetailButton();
		hud.hideInfoTab();
//		
//		Region tmpSelectedRegion = new Region(-1, selectedRegion.getName(), selectedRegion.getStratCenter(), "");
//		tmpSelectedRegion.setOwner(selectedRegion.getOwner());
//		
//		Region tmpTargetedRegion = new Region(-1, targetedRegion.getName(), targetedRegion.getStratCenter(), "");
//		tmpTargetedRegion.setOwner(targetedRegion.getOwner());
		
		battleScene = new BattleScene(logic.selectedRegion, logic.targetedRegion, result);
		
		doubleTapAllowed = false;
		scrollDetector.setEnabled(false);

		attachChild(battleScene);		
	}
	
	public void hideBattleScene() {
		
		detachChild(battleScene);
	
		hud.showInfoTab();
		hud.hideDetailButton();
		hud.hideAttackButton();
		
		battleScene = null;
		unselectRegion();
		
		setInfoTabToChooseOwnRegion();
		
		doubleTapAllowed = true;
		scrollDetector.setEnabled(true);
		
		logic.turnDone = true;
		
		logic.updateGame();
			
	}

	public void showInitialHUD() {
		hud.showInfoTab();
	}
	
	public void showDetails()
	{	
		detailScene.setVisible(true);
		
		//attachChild(detailScene);
	}
	
	public void hideDetails()
	{
		detailScene.setVisible(false);
		
		//detachChild(detailScene);
	}

	// ======================================================
	// HUD
	// ======================================================
	public void setInfoTabText(String pText) {
		hud.setInfoTabText(pText);
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
	
	// ======================================================
	// ======================================================
	
	public void lockUserInput() {
		
		doubleTapAllowed = false;
		scrollDetector.setEnabled(false);
		
		unregisterTouchAreaForAllRegions();
	}
	
	public void unlockUserInput() {
		
		doubleTapAllowed = true;
		scrollDetector.setEnabled(true);
		
		registerTouchAreaForAllRegions();
	}

	public void saveGame()
	{
		
		if(logic.getState() != GAME_STATE.PAUSED && logic.getState() != GAME_STATE.DEPLOYMENT) {
			
			Log.d("Riska", "Will save the game.");
			
			new SaveGame(activity, logic);
		}
		
	}
	
	public void loadGame() {
		
		LoadGame load = new LoadGame(activity);
		load.setLogic(this.logic);
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

	private void showOnlyNeighbourRegions(Region pRegion) {
		
		for(int i = 0; i < logic.getMap().getRegions().size(); i++) {

			Region r = logic.getMap().getRegions().get(i);

			if( (!pRegion.getNeighbours().contains(r) && !r.equals(selectedRegion))
					|| (r.getOwner().equals(logic.getPlayers().get(0)) && !r.equals(selectedRegion))
					) {

				hideRegionButton(logic.getMap().getRegions().get(i));

			}
		}
		
	}
	
	private void showAllRegions()
	{
		
		for(int i = 0; i < logic.getMap().getRegions().size(); i++) {
			showRegionButton(logic.getMap().getRegions().get(i));
		}
		
	}
	// ======================================================
	// ======================================================
	
	// ======================================================
	// GETTERS & SETTERS
	// ======================================================
	public CameraManager getCameraManager() {
		return cameraManager;
	}

	// ======================================================
	// ======================================================

	private void registerTouchAreaForAllRegions()
	{
		for(Pair<ButtonSprite, Text> regionButton : regionButtons)
		{
			if(!getTouchAreas().contains(regionButton.first))
			{
				registerTouchArea(regionButton.first);
			}
		}
	}
	
	private void unregisterTouchAreaForAllRegions()
	{
		for(Pair<ButtonSprite, Text> regionButton : regionButtons)
		{
			if(getTouchAreas().contains(regionButton.first))
			{
				unregisterTouchArea(regionButton.first);
			}
		}
	}

	
	public void lockHUD() {
		hud.lock();
	}

	public void unlockHUD() {
		hud.unlock();
	}

	
	public void touchedDetailsButton()
	{
		hud.changeDetailButton();

		if(detailScene.isVisible())
		{
			hideDetails();
			detailScene.setVisible(false);
		}
		else
		{
			if(battleScene.isVisible())
			{
				hideBattleScene();
			}
			else
			{				
				detailScene.setAttributes(logic.selectedRegion, logic.targetedRegion);
				detailScene.setVisible(true);
				
				showDetails();
				getCameraManager().zoomOut();
			}
		}
	}
}


