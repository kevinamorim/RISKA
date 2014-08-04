package feup.lpoo.riska.scenes;

import java.util.ArrayList;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
//import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;

import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.gameInterface.GameHUD;
import feup.lpoo.riska.gameInterface.GameHUD.BUTTON;
import feup.lpoo.riska.gameInterface.GameHUD.SPRITE;
import feup.lpoo.riska.generator.BattleGenerator;
import feup.lpoo.riska.io.LoadGame;
import feup.lpoo.riska.io.SaveGame;
import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.logic.SceneManager;
import feup.lpoo.riska.logic.GameLogic.GAME_STATE;
import feup.lpoo.riska.logic.SceneManager.SceneType;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.utilities.Utils;
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
	//private final int ANIM_DURATION = 200;
	private final float CPU_DELAY = 1.0f;
	//private final int MIN_SOLDIERS_PER_REGION = 1;	
	private final long REGION_BUTTON_MIN_TOUCH_INTERVAL = 30;
	private final int MAX_REGION_CHARS = 10;	
	private final int PLAYER_NUM = 0;

	// ======================================================
	// FIELDS
	// ======================================================
	private GameLogic logic;
	private GameHUD hud;
	private DetailScene detailScene;
	private BattleScene battleScene;
	private PreBattleScene preBattleScene;
	
	private Sprite mapSprite;

	private ScrollDetector scrollDetector;
	private Map map;		
	private ArrayList<ButtonSprite> regionButtons;
	private ArrayList<Text> regionButtonsText;
	private boolean doubleTapAllowed;
	private long lastTouchTime;
	private long lastTouchTimeInRegion;


	@Override
	public void createScene()
	{
		logic = new GameLogic(this);

		map = resources.map;

		regionButtons = new ArrayList<ButtonSprite>();
		regionButtonsText = new ArrayList<Text>();

		lastTouchTime = 0;
		lastTouchTimeInRegion = 0;
		doubleTapAllowed = true;

		createDisplay();
	}

	@Override
	public void onBackKeyPressed()
	{
		if(getEntityModifierCount() == 0) // NO CPU DELAYS OCCURING
		{
			SceneManager.getSharedInstance().loadMainMenuScene(engine);
		}	
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.GAME;
	}

	@Override
	public void disposeScene() {
		camera.setHUD(null);
		//detachChildren();
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

	private void createMap()
	{
		mapSprite = new Sprite(
				camera.getCenterX(),
				camera.getCenterY(),
				camera.getWidth(),
				camera.getHeight(),
				resources.mapRegion, vbom);

		attachChild(mapSprite);	
	}

	private void createBackground()
	{
		//		int ANIMATION_TILES = 4;
		//		
		//		AnimatedSprite background = new AnimatedSprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
		//				resources.seaRegion, vbom);
		//		
		//		background.setSize(MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT);
		//		
		//		long duration[] = new long[ANIMATION_TILES];
		//		Arrays.fill(duration, ANIM_DURATION);
		//		
		//		background.animate(duration, 0, (ANIMATION_TILES - 1), true);
		//		
		//		attachChild(background);
	}

	private void createHUD()
	{
		hud = new GameHUD(this);
		
		hud.show(SPRITE.INFO_TAB);
		//hud.show(BUTTON.AUTO_DEPLOY);
		
		camera.setHUD(hud);
	}

	private void createRegionButtons()
	{
		Text buttonText;
		ButtonSprite regionButton;

		for(Region region : resources.map.getRegions()) {

			int x = (region.getStratCenter().x * MainActivity.CAMERA_WIDTH)/100;
			int y = (region.getStratCenter().y * MainActivity.CAMERA_HEIGHT)/100;

			buttonText = new Text(0, 0, resources.mGameFont, "", MAX_REGION_CHARS, vbom);

			regionButton = new ButtonSprite(x, y, resources.regionBtnRegion, vbom) {

				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
				{
					switch(ev.getAction()) 
					{
					case MotionEvent.ACTION_DOWN:
						onRegionButtonPressed(this, getTag());
						break;
					case MotionEvent.ACTION_UP:
						onRegionButtonReleased(this, getTag());
						break;
					case MotionEvent.ACTION_OUTSIDE:
						onRegionButtonReleased(this, getTag());
						break;
					}

					return true;
				}
			};

			regionButton.setTag(region.ID);
			regionButton.setPosition(x, y);
			regionButton.setScale((float) 0.8);

			regionButton.setColor(region.getPrimaryColor());

			buttonText.setText("" + logic.MIN_SOLDIERS_PER_REGION);
			buttonText.setScale((float) 1.1);
			buttonText.setPosition(regionButton.getWidth()/2, regionButton.getHeight()/2);
			buttonText.setColor(region.getSecundaryColor());

			regionButton.attachChild(buttonText);

			regionButtons.add(regionButton);
			regionButtonsText.add(buttonText);

			attachChild(regionButton);
			registerTouchArea(regionButton);
		}
	}

	private void createChildScenes()
	{
		detailScene = new DetailScene();
		battleScene = new BattleScene();
		preBattleScene = new PreBattleScene();

		detailScene.setVisible(false);
		attachChild(detailScene);

		battleScene.setVisible(false);
		attachChild(battleScene);

		preBattleScene.setVisible(false);
		attachChild(preBattleScene);
	}

	// ======================================================
	// ======================================================

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {

		super.onManagedUpdate(pSecondsElapsed);

		switch(logic.getState())
		{
		case PAUSED:
			draw();
			break;
		case SETUP:
			deploymentUpdate();
			logic.setup();
			break;		
		case DEPLOYMENT:
			//Log.d("user", "deployment");
			deploymentUpdate();
			logic.deploy();
			break;	
		case ATTACK:
			break;
		case MOVE:
			break;
		case PLAY:
			draw();
			logic.update();
			break;	
		case CPU:
			break;
		default:
			break;
		}

	}

	private void draw()
	{		
		if(anyChildSceneIsVisible())
		{
			return;
		}
		
		if(logic.selectedRegion == null)
		{
			showAllRegions();
		}

		detailScene.update(logic.selectedRegion, logic.targetedRegion);

		drawRegionButtons();
		
		hud.draw(logic);
		hud.setInfoTabText(logic);
	}

	private void deploymentUpdate()
	{	
		if(detailScene.isVisible())
		{
			hideDetailScene();
		}
		
		if(battleScene.isVisible())
		{
			hideBattleScene();
		}
		
		if(preBattleScene.isVisible())
		{
			hidePreBattleScene();
		}

		if(logic.getCurrentPlayerIndex() == PLAYER_NUM)
		{
			hud.setInfoTabText(logic.getCurrentPlayer().soldiersToDeploy + Utils.getString(R.string.game_info_left_to_deploy));
		}
		else
		{
			hud.setInfoTabText(Utils.getString(R.string.game_info_wait_for_CPU));
			hud.hide(BUTTON.AUTO_DEPLOY);
		}

		hud.hide(BUTTON.ATTACK);
		hud.hide(BUTTON.DETAILS);
		hud.show(BUTTON.AUTO_DEPLOY);
		drawRegionButtons();
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
				logic.attack();
				unlockUserInput();
				unlockHUD();
			}

		};

		registerEntityModifier(selectRegionDelay);
		registerEntityModifier(targetRegionDelay);
		registerEntityModifier(attackDelay);
	}
	
	// ======================================================
	// SCENE TOUCH
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

	private void createScrollDetector() {

		scrollDetector = new SurfaceScrollDetector(this);
		scrollDetector.setTriggerScrollMinimumDistance(MIN_SCROLLING_DIST);
		scrollDetector.setEnabled(true);

	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pX, float pY) {		
		unregisterTouchAreaForAllRegions();
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID, float pX, float pY) {
		Point p = new Point((int)(activity.mCamera.getCenterX() - pX), (int)(activity.mCamera.getCenterY() + pY));
		camera.jumpTo(p);	
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID, float pX, float pY) {		
		Point p = new Point((int)(activity.mCamera.getCenterX() - pX), (int)(activity.mCamera.getCenterY() + pY));
		camera.jumpTo(p);

		registerTouchAreaForAllRegions();
	}

	private void lockUserInput() {

		doubleTapAllowed = false;
		scrollDetector.setEnabled(false);

		unregisterTouchAreaForAllRegions();
	}

	private void unlockUserInput() {

		doubleTapAllowed = true;
		scrollDetector.setEnabled(true);

		registerTouchAreaForAllRegions();
	}

	// ======================================================
	// REGION BUTTONS
	// ======================================================
	private void drawRegionButtons() {

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

	}

	private void onRegionButtonPressed(ButtonSprite btn, int regionID)
	{
		btn.setCurrentTileIndex(1);
	}

	private void onRegionButtonReleased(ButtonSprite btn, int regionID)
	{
		btn.setCurrentTileIndex(0);

		long now = System.currentTimeMillis();

		if((now - lastTouchTimeInRegion) > REGION_BUTTON_MIN_TOUCH_INTERVAL)
		{
			logic.onRegionTouched(resources.map.getRegionById(regionID));
			updateRegionButton(regionID);
		}

		lastTouchTimeInRegion = System.currentTimeMillis();
	}

	private void updateRegionButton(int i)
	{
		Region reg = map.getRegionById(i);
		regionButtonsText.get(i).setText("" + reg.getNumberOfSoldiers());
	}

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

	private void showAllRegions()
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

	// ======================================================
	// CHILD SCENES
	// ======================================================
	private void showDetailScene()
	{
		if(detailScene != null)
		{
			camera.zoomOut();
			hud.setDetailButtonToExit();
			hud.show(BUTTON.DETAILS);
			hud.hide(BUTTON.ATTACK);
			hud.hide(SPRITE.INFO_TAB);
			detailScene.setVisible(true);
			lockUserInput();
			logic.pauseGame();
		}
	}

	private void hideDetailScene()
	{
		if(detailScene != null)
		{
			hud.setDetailButtonToQuestion();
			hud.hide(BUTTON.DETAILS);
			hud.show(SPRITE.INFO_TAB);		
			detailScene.setVisible(false);
			unlockUserInput();
			logic.resumeGame();
		}
	}

	public void showBattleScene(Region pRegion1, Region pRegion2, BattleGenerator battleGenerator)
	{
		if(battleScene != null)
		{
			camera.zoomOut();
			hud.setDetailButtonToExit();
			hud.show(BUTTON.DETAILS);
			hud.hide(BUTTON.ATTACK);
			hud.hide(SPRITE.INFO_TAB);
			battleScene.update(pRegion1, pRegion2, battleGenerator);
			battleScene.setVisible(true);
			lockUserInput();
		}
	}

	private void hideBattleScene()
	{
		if(battleScene != null)
		{
			hud.setDetailButtonToQuestion();
			hud.hide(BUTTON.DETAILS);
			hud.show(SPRITE.INFO_TAB);
			battleScene.setVisible(false);
			unlockUserInput();
			logic.resumeGame();
		}
	}

	private void showPreBattleScene(int attackingSoldiers, int defendingSoldiers)
	{
		if(preBattleScene != null)
		{
			camera.zoomOut();
			//hud.setDetailButtonToExit();
			//hud.hide(BUTTON.ATTACK);
			hud.hide(BUTTON.DETAILS);
			hud.hide(SPRITE.INFO_TAB);
			hud.show(BUTTON.ARROW_LEFT);
			hud.show(BUTTON.ARROW_RIGHT);
			preBattleScene.update(attackingSoldiers, defendingSoldiers);
			preBattleScene.setVisible(true);
			lockUserInput();
		}

	}

	private void hidePreBattleScene()
	{
		if(preBattleScene != null)
		{
			hud.setDetailButtonToQuestion();
			hud.hide(BUTTON.DETAILS);
			hud.show(SPRITE.INFO_TAB);
			hud.hide(BUTTON.ARROW_LEFT);
			hud.hide(BUTTON.ARROW_RIGHT);
			preBattleScene.setVisible(false);
			unlockUserInput();
		}
	}

	private boolean anyChildSceneIsVisible()
	{
		if(detailScene.isVisible())
		{
			return true;
		}

		if(battleScene.isVisible())
		{
			return true;
		}

		if(preBattleScene.isVisible())
		{
			return true;
		}

		return false;
	}

	// ======================================================
	// HUD
	// ======================================================
	private void lockHUD() {
		hud.Lock();
	}

	private void unlockHUD() {
		hud.Unlock();
	}

	public void onDetailButtonTouched()
	{	
		if(detailScene.isVisible()) 
		{
			hideDetailScene();
		}
		else if(battleScene.isVisible())
		{
			hideBattleScene();
		}
		else if(!detailScene.isVisible())
		{
			showDetailScene();
		}
		else
		{
			return;
		}
	}

	public void setInitialHUD() 
	{
		hud.hide(BUTTON.AUTO_DEPLOY);
	}

	public void onAttackButtonTouched()
	{
		if(preBattleScene.isVisible())
		{
			hidePreBattleScene();
			logic.attackingSoldiers = preBattleScene.getAttackingSoldiers();
			logic.attack();
		}
		else
		{
			logic.pauseGame();
			showPreBattleScene(logic.attackingSoldiers, logic.defendingSoldiers); // TODO alter this, maybe, no?
		}
	}

	public void onAutoDeployButtonTouched()
	{
		logic.getCurrentPlayer().deployAllSoldiers();
	}

	public void onLeftArrowTouched()
	{
		if(preBattleScene != null)
		{
			preBattleScene.decreaseSoldiers();
		}
	}

	public void onRightArrowTouched()
	{
		if(preBattleScene != null)
		{
			preBattleScene.increaseSoldiers();
		}
	}
}


