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
import feup.lpoo.riska.HUD.GameHUD.SPRITE;
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
import android.view.MotionEvent;

public class GameScene extends BaseScene implements IOnSceneTouchListener, IScrollDetectorListener {

	// ======================================================
	// CONSTANTS
	// ======================================================
	private final int MIN_SCROLLING_DIST = 30; /* Bigger the number, slower the scrolling. */
	private final long MIN_TOUCH_INTERVAL = 70;
	private final long MAX_TOUCH_INTERVAL = 400;
	
	private final int ANIM_DURATION = 200;
	
	private final float CPU_DELAY = 1.0f;
	
	private final int MIN_SOLDIERS_PER_REGION = 1;
	
	private final long REGION_BUTTON_MIN_TOUCH_INTERVAL = 30;
	private final int MAX_REGION_CHARS = 10;
	
	private final int PLAYER_NUM = 0;

	// ======================================================
	// SINGLETONS
	// ======================================================
	GameLogic logic;
	BattleScene battleScene;
	
	// ======================================================
	// FIELDS
	// ======================================================
	GameHUD hud;
	DetailScene detailScene;
	
	private Map map;
	
	private ScrollDetector scrollDetector;
	
	private ArrayList<ButtonSprite> regionButtons;
	private ArrayList<Text> regionButtonsText;
	
	private boolean doubleTapAllowed = true;
	private long lastTouchTime;
	private long lastTouchTimeInRegion;

	
	@Override
	public void createScene() {
		logic = new GameLogic(this);
		
		map = resources.map;
		
		regionButtons = new ArrayList<ButtonSprite>();
		regionButtonsText = new ArrayList<Text>();
		
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
		detachChildren();
		detachSelf();
		dispose();
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
		
		createChildScenes();

		setTouchAreaBindingOnActionDownEnabled(true);
		setOnSceneTouchListener(this);	
	}
	
	private void createMap() {
		
		Sprite mapSprite = new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT,
				resources.mapRegion, vbom);
		
		attachChild(mapSprite);	
	}
	
	private void createBackground() {
		
		int ANIMATION_TILES = 4;
		
		AnimatedSprite background = new AnimatedSprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
				resources.seaRegion, vbom);
		
		background.setSize(MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT);
		
		long duration[] = new long[ANIMATION_TILES];
		Arrays.fill(duration, ANIM_DURATION);
		
		background.animate(duration, 0, (ANIMATION_TILES - 1), true);
		
		attachChild(background);
		
	}
	
	private void createHUD() {
		
		hud = new GameHUD(this);
		activity.mCamera.setHUD(hud);
	
		hud.show(SPRITE.INFO_TAB);
		hud.show(BUTTON.AUTO_DEPLOY);
	}
	
	private void createChildScenes() {
		
		detailScene = new DetailScene();
		battleScene = new BattleScene();
		
		detailScene.setVisible(false);
		attachChild(detailScene);
		
		battleScene.setVisible(false);
		attachChild(battleScene);
		
	}
	
	// ======================================================
	// ======================================================
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		
		super.onManagedUpdate(pSecondsElapsed);
		
		switch(logic.getState())
		{
		case PAUSED:
			break;
		case SETUP:
			deploymentUpdate();
			logic.setup();
			break;		
		case DEPLOYMENT:
			deploymentUpdate();
			logic.deploy();
			break;	
		case ATTACK:
			break;
		case MOVE:
			break;
		case PLAY:
			gameUpdate();
			if(!logic.getCurrentPlayer().isCPU()) logic.updateGame();
			break;	
		case CPU:
			break;
		default:
			break;
		}
		
	}
	
	private void gameUpdate()
	{	
		
		if(detailScene.isVisible())
		{
			doubleTapAllowed = false;
			scrollDetector.setEnabled(false);
			hud.show(BUTTON.DETAILS);
			hud.hide(BUTTON.ATTACK);
			hud.hide(SPRITE.INFO_TAB);
			battleScene.setVisible(false);
			return;
		}

		if(battleScene.isVisible())
		{
			doubleTapAllowed = false;
			scrollDetector.setEnabled(false);
			hud.show(BUTTON.DETAILS);
			hud.hide(BUTTON.ATTACK);
			hud.hide(SPRITE.INFO_TAB);
			detailScene.setVisible(false);
			return;
		}
		
		doubleTapAllowed = true;
		scrollDetector.setEnabled(true);
		hud.setDetailButtonToQuestion();
		hud.show(BUTTON.DETAILS);
		hud.show(SPRITE.INFO_TAB);
		
		
		for(int i = 0; i < regionButtons.size(); i++)
		{
			ButtonSprite btn = regionButtons.get(i);
			Text btnText = regionButtonsText.get(i);
			
			if(btn.isVisible())
			{
				Region region = map.getRegionById(i);
				btnText.setText("" + region.getNumberOfSoldiers());
				
				if(region.getPrimaryColor() != btn.getColor())
				{
					btn.setColor(region.getPrimaryColor());
					btnText.setColor(region.getSecundaryColor());
				}
			}
		}
		
		if(logic.selectedRegion != null)
		{
			hud.show(BUTTON.DETAILS);
			if(logic.targetedRegion != null)
			{
				detailScene.update(logic.selectedRegion, logic.targetedRegion);
				
				if(logic.getCurrentPlayerIndex() == 0)
				{
					hud.show(BUTTON.ATTACK);
					hud.setInfoTabText(Utilities.getString(R.string.game_info_attack));
				}
				else
				{
					hud.hide(BUTTON.ATTACK);
					hud.hide(BUTTON.DETAILS);
					hud.setInfoTabText(Utilities.getString(R.string.game_info_wait_for_CPU));
				}
			}
			else
			{
				detailScene.update(logic.selectedRegion, null);
				hud.hide(BUTTON.ATTACK);
				
				if(logic.getCurrentPlayerIndex() == 0)
				{
					hud.setInfoTabText(Utilities.getString(R.string.game_info_tap_enemy_region));
				}
				else
				{
					hud.hide(BUTTON.ATTACK);
					hud.hide(BUTTON.DETAILS);
					hud.setInfoTabText(Utilities.getString(R.string.game_info_wait_for_CPU));
				}
			}
		}
		else
		{
			detailScene.update(null, null);
			hud.hide(BUTTON.DETAILS);
			hud.hide(BUTTON.ATTACK);

			showAllRegions();

			if(logic.getCurrentPlayerIndex() == 0)
			{
				hud.setInfoTabText(Utilities.getString(R.string.game_info_tap_allied_region));
			}
			else
			{
				hud.setInfoTabText(Utilities.getString(R.string.game_info_wait_for_CPU));
			}
		}
	}

	private void deploymentUpdate()
	{	
		doubleTapAllowed = false;
		scrollDetector.setEnabled(false);
		
		if(logic.getCurrentPlayerIndex() == PLAYER_NUM)
		{
			hud.setInfoTabText(logic.getCurrentPlayer().getSoldiersToDeploy() + Utilities.getString(R.string.game_info_left_to_deploy));
		}
		else
		{
			hud.setInfoTabText(Utilities.getString(R.string.game_info_wait_for_CPU));
			hud.hide(BUTTON.AUTO_DEPLOY);
		}
		
		hud.hide(BUTTON.ATTACK);
		hud.hide(BUTTON.DETAILS);
	}
	
	// ======================================================
	// REGION BUTTONS
	// ======================================================
	
	private void createRegionButtons() {
		
		Text buttonText;
		ButtonSprite regionButton;

		for(Region region : resources.map.getRegions()) {

			int x = (region.getStratCenter().x * MainActivity.CAMERA_WIDTH)/100;
			int y = (region.getStratCenter().y * MainActivity.CAMERA_HEIGHT)/100;
			
			buttonText = new Text(0, 0, resources.mGameFont, "", MAX_REGION_CHARS, vbom);
			
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
			
			regionButton.setColor(region.getPrimaryColor());
			
			buttonText.setText("" + MIN_SOLDIERS_PER_REGION);
			buttonText.setScale((float) 1.4);
			buttonText.setPosition(regionButton.getWidth()/2, regionButton.getHeight()/2);
			buttonText.setColor(region.getSecundaryColor());
			
			regionButton.attachChild(buttonText);
			
			regionButtons.add(regionButton);
			regionButtonsText.add(buttonText);
			
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
		btn.setCurrentTileIndex(0);
		
		long now = System.currentTimeMillis();

		if((now - lastTouchTimeInRegion) > REGION_BUTTON_MIN_TOUCH_INTERVAL) {

			logic.onRegionTouched(resources.map.getRegionById(regionID));
			updateRegionButton(regionID);
		}

		lastTouchTimeInRegion = System.currentTimeMillis();
	}

	void updateRegionButton(int i) {
		Region reg = map.getRegionById(i);
		regionButtonsText.get(i).setText("" + reg.getNumberOfSoldiers());
	}
	
	// ======================================================
	// ======================================================
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
			
		switch(pSceneTouchEvent.getMotionEvent().getActionMasked()) {
		case TouchEvent.ACTION_UP:
			
			if( ((System.currentTimeMillis() - lastTouchTime) >  MIN_TOUCH_INTERVAL) &&
					((System.currentTimeMillis() - lastTouchTime) < MAX_TOUCH_INTERVAL) && doubleTapAllowed ) {
				
				/* Double tap */
				camera.setAutomaticZoom(new Point((int) pSceneTouchEvent.getX(), 
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
		camera.jumpTo(p);	
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID, float pX, float pY) {
		
		Log.d("ScrollDetector", "Scrolling finished.");
		
		Point p = new Point((int)(activity.mCamera.getCenterX() - pX), (int)(activity.mCamera.getCenterY() + pY));
		camera.jumpTo(p);
		
		registerTouchAreaForAllRegions();
	}

	// ======================================================
	// ======================================================
	
	public void onAttack()
	{
		camera.zoomOut();
		logic.attack();
	}
	
	public void onAutoDeploy()
	{
		logic.autoDeployment(logic.getPlayers().get(0));
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
	public void showOnlyNeighbourRegions(Region pRegion) {
		
		for(int i = 0; i < map.getRegions().size(); i++) {

			Region comp = map.getRegions().get(i);
			
			if (comp != pRegion)
			{
				if(!comp.isNeighbourOf(pRegion))
				{
					regionButtons.get(i).setVisible(false);
					//regionButtons.get(i).first.setEnabled(false);
				}
				else
				{
					if(comp.getOwner() == logic.getCurrentPlayer())
					{
						regionButtons.get(i).setVisible(false);
						//regionButtons.get(i).first.setEnabled(false);
					}
				}
			}
		}		
	}
	
	public void showAllRegions()
	{
		for(int i = 0; i < regionButtons.size(); i++)
		{
			ButtonSprite btn = regionButtons.get(i);
			
			if(!btn.isVisible())
			{
				btn.setVisible(true);
			}
		}
	}
	
	// ======================================================
	// ======================================================
	
	// ======================================================
	// CHILD SCENES
	// ======================================================
	public void showDetailScene() {
		if(detailScene != null) {
			hud.setDetailButtonToExit();
			hud.show(BUTTON.DETAILS);
			hud.hide(BUTTON.ATTACK);
			hud.hide(SPRITE.INFO_TAB);
			unregisterTouchAreaForAllRegions();
			detailScene.setVisible(true);
		}
	}
	
	public void hideDetailScene() {
		if(detailScene != null) {
			hud.setDetailButtonToQuestion();
			hud.hide(BUTTON.DETAILS);
			hud.show(SPRITE.INFO_TAB);
			registerTouchAreaForAllRegions();
			detailScene.setVisible(false);
		}
	}
	
	public void showBattleScene(Region pRegion1, Region pRegion2, boolean result) {
		if(battleScene != null) {
			hud.setDetailButtonToExit();
			hud.show(BUTTON.DETAILS);
			hud.hide(BUTTON.ATTACK);
			hud.hide(SPRITE.INFO_TAB);
			unregisterTouchAreaForAllRegions();
			battleScene.setAttributes(pRegion1, pRegion2, result);
			battleScene.setVisible(true);
		}
	}
	
	public void hideBattleScene() {
		if(battleScene != null) {
			hud.setDetailButtonToQuestion();
			hud.hide(BUTTON.DETAILS);
			hud.show(SPRITE.INFO_TAB);
			registerTouchAreaForAllRegions();
			battleScene.setVisible(false);
		}
	}

	private void registerTouchAreaForAllRegions()
	{
		for(ButtonSprite regionButton : regionButtons)
		{
			if(!getTouchAreas().contains(regionButton))
			{
				registerTouchArea(regionButton);
			}
		}
	}
	
	private void unregisterTouchAreaForAllRegions()
	{
		for(ButtonSprite regionButton : regionButtons)
		{
			if(getTouchAreas().contains(regionButton))
			{
				unregisterTouchArea(regionButton);
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

		if(detailScene.isVisible())
		{
			detailScene.setVisible(false);
			hud.setDetailButtonToQuestion();
			unlockUserInput();
			hud.show(BUTTON.DETAILS);
			hud.show(SPRITE.INFO_TAB);
			logic.resumeGame();
		}
		else
		{
			if(battleScene.isVisible())
			{
				battleScene.setVisible(false);
				hud.setDetailButtonToQuestion();
				unlockUserInput();
				hud.show(BUTTON.DETAILS);
				hud.show(SPRITE.INFO_TAB);
				logic.resumeGame();
			}
			else
			{
				hud.setDetailButtonToExit();
				lockUserInput();
				logic.pauseGame();
				hud.show(BUTTON.DETAILS);
				hud.hide(BUTTON.ATTACK);
				hud.hide(SPRITE.INFO_TAB);
				detailScene.update(logic.selectedRegion, logic.targetedRegion);
				detailScene.setVisible(true);
				camera.zoomOut();
			}
		}
	}

	public void createDelay(float time)
	{	
		DelayModifier delay = new DelayModifier(time) {

			@Override
			protected void onModifierFinished(IEntity pItem) {
			}

		};
		
		registerEntityModifier(delay);
	}

	public void showCpuMove(final Region pRegion1, final Region pRegion2)
	{
		lockUserInput();
		lockHUD();
		
		DelayModifier selectRegionDelay = new DelayModifier(CPU_DELAY)
		{
			@Override
			protected void onModifierFinished(IEntity pItem)
			{
				logic.selectRegion(pRegion1);
			}

		};
		
		DelayModifier targetRegionDelay = new DelayModifier(CPU_DELAY * 2)
		{
			@Override
			protected void onModifierFinished(IEntity pItem)
			{
				logic.targetRegion(pRegion2);
			}

		};
		
		DelayModifier attackDelay = new DelayModifier(CPU_DELAY * 3)
		{
			@Override
			protected void onModifierFinished(IEntity pItem)
			{
				unlockUserInput();
				unlockHUD();
				logic.attack();
				logic.updateGame();
			}

		};
		
		registerEntityModifier(selectRegionDelay);
		registerEntityModifier(targetRegionDelay);
		registerEntityModifier(attackDelay);
	}

	public void setInitialHUD() {
		hud.hide(BUTTON.AUTO_DEPLOY);
	}
}


