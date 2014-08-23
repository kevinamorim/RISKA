package feup.lpoo.riska.scenes;

import java.util.ArrayList;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
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
import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.logic.GameLogic.GAME_STATE;
import feup.lpoo.riska.logic.SceneManager.SCENE_TYPE;
import feup.lpoo.riska.utilities.Utils;
import android.graphics.Point;
import android.view.MotionEvent;

public class GameScene extends BaseScene implements IOnSceneTouchListener, IScrollDetectorListener {

	// ======================================================
	// CONSTANTS
	// ======================================================
	private final int MIN_SCROLLING_DIST = 30; /* Bigger the number, slower the scrolling. */
	private final long MIN_TOUCH_INTERVAL = 70;
	private final long MAX_TOUCH_INTERVAL = 400;
	private final float CPU_DELAY = 1.0f;
	private final long REGION_BUTTON_MIN_TOUCH_INTERVAL = 30;
	private final int MAX_REGION_CHARS = 10;

	// ======================================================
	// FIELDS
	// ======================================================
	private GameLogic logic;
	private GameHUD hud;
	private BattleScene battleScene;
	private PreBattleScene preBattleScene;
	private PreMoveScene preMoveScene;
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
		initVars();
		createDisplay();
		createChildScenes();
		createScrollDetector();

		setTouchAreaBindingOnActionDownEnabled(true);
		setOnSceneTouchListener(this);	
		
		draw();
	}

	@Override
	public void onBackKeyPressed()
	{
		camera.zoomOut();
		sceneManager.loadMainMenuScene(engine);
	}

	@Override
	public SCENE_TYPE getSceneType()
	{
		return SCENE_TYPE.GAME;
	}

	@Override
	public void disposeScene()
	{
		camera.setHUD(null);
		detachSelf();
		dispose();
	}

	// ======================================================
	// CREATE DISPLAY
	// ======================================================

	private void createDisplay() {
		createMap();
		createHUD();
		createRegionButtons();
	}

	private void createMap()
	{
		Sprite mapSprite = new Sprite(
				camera.getCenterX(),
				camera.getCenterY(),
				camera.getWidth() + 2,
				camera.getHeight() + 2,
				resources.mapRegion, vbom);

		attachChild(mapSprite);	
	}

	private void createHUD()
	{
		hud = new GameHUD(this);
		hud.show(SPRITE.INFO_TAB);	
		camera.setHUD(hud);
	}

	private void createRegionButtons()
	{
		Text buttonText;
		ButtonSprite regionButton;

		for(Region region : map.getRegions()) {

			int x = (int)((region.getStratCenter().x * camera.getWidth()) / 100);
			int y = (int)((region.getStratCenter().y * camera.getHeight()) / 100);

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
			regionButton.setSize(0.1f * camera.getHeight(), 0.1f * camera.getHeight());

			regionButton.setColor(region.getPrimaryColor());

			buttonText.setText("" + logic.MIN_SOLDIERS_PER_REGION);
			buttonText.setPosition(Utils.getCenterX(regionButton), Utils.getCenterY(regionButton));
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
		battleScene = new BattleScene();
		preBattleScene = new PreBattleScene();
		preMoveScene = new PreMoveScene();

		battleScene.setVisible(false);
		attachChild(battleScene);

		preBattleScene.setVisible(false);
		attachChild(preBattleScene);

		preMoveScene.setVisible(false);
		attachChild(preMoveScene);
	}

	// ======================================================
	// ======================================================

	private void draw()
	{		
		if(anyChildSceneIsVisible())
		{
			return;
		}

		if(logic.selectedRegion == null)
		{
			showAllRegions();
		} else {
			if(logic.getState() == GAME_STATE.ATTACK) {
				showOnlyNeighbourRegions(logic.selectedRegion);
			} else if(logic.getState() == GAME_STATE.MOVE) {
				showOnlyPlayerNeighbourRegions(logic.selectedRegion);
			}
		}

		drawRegionButtons();

		hud.draw(logic);
		
		drawButtons();
		
	}
	
	private void drawButtons() {
		
		/* Reset */
		hideAllButtons(); 
		
		if(!logic.getCurrentPlayer().isCPU) {
			switch(logic.getState()) {
			case SETUP:
				hud.show(BUTTON.AUTO_DEPLOY);
				break;
			case ATTACK:
				if(logic.selectedRegion != null && logic.targetedRegion != null) {
					hud.show(BUTTON.ATTACK);
				}
				break;
			case MOVE:
				if(logic.selectedRegion != null && logic.targetedRegion != null) {
					hud.show(BUTTON.MOVE);
				}
				break;
			case DEPLOYMENT:
				hud.show(BUTTON.AUTO_DEPLOY);
				break;
			case ENDTURN:
				hud.show(BUTTON.NEXT_TURN);
				break;
			default:
				break;
			}
		}
		
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
				logic.updateCPU();
				unlockUserInput();
				unlockHUD();
			}

		};

		registerEntityModifier(selectRegionDelay);
		registerEntityModifier(targetRegionDelay);
		registerEntityModifier(attackDelay);
	}

	// ======================================================
	// SCENE TOUCH TODO: Create a class for the ScrollDetector, that would clean a lot of code.
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
			logic.onRegionTouched(map.getRegionById(regionID));
			updateRegionButton(regionID);
			draw();
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

	public void showOnlyPlayerNeighbourRegions(Region pRegion) {
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
					if(comp.getOwner() != logic.getCurrentPlayer())
					{
						regionButtons.get(i).setVisible(false);
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
	public void showBattleScene(Region pRegion1, Region pRegion2, BattleGenerator battleGenerator)
	{
		if(battleScene != null)
		{
			showChildScene();
			hud.show(BUTTON.DETAILS);
			battleScene.update(pRegion1, pRegion2, battleGenerator);
			battleScene.setVisible(true);
		}
	}

	private void hideBattleScene()
	{
		if(battleScene != null)
		{
			hideChildScene();
			battleScene.setVisible(false);
			logic.resumeGame();
		}
	}

	private void showPreBattleScene(int attackingSoldiers, int defendingSoldiers)
	{
		if(preBattleScene != null)
		{
			showChildScene();
			hud.show(BUTTON.ATTACK);
			hud.show(BUTTON.ARROW_LEFT);
			hud.show(BUTTON.ARROW_RIGHT);
			preBattleScene.update(attackingSoldiers, defendingSoldiers);
			preBattleScene.setVisible(true);
		}

	}

	private void showPreMoveScene(int maxSoldiers) {
		if(preMoveScene != null) {
			showChildScene();
			hud.show(BUTTON.MOVE);
			hud.show(BUTTON.ARROW_LEFT);
			hud.show(BUTTON.ARROW_RIGHT);
			preMoveScene.update(maxSoldiers);
			preMoveScene.setVisible(true);
		}
	}

	private void hidePreBattleScene()
	{
		if(preBattleScene != null)
		{
			hideChildScene();
			preBattleScene.setVisible(false);
		}
	}

	private void hidePreMoveScene() {
		if(preMoveScene != null) {
			hideChildScene();
			preMoveScene.setVisible(false);
		}
	}

	private boolean anyChildSceneIsVisible()
	{

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

	private void hideAllChildScenes() {

		if(battleScene.isVisible())
		{
			hideBattleScene();
		}

		if(preBattleScene.isVisible())
		{
			hidePreBattleScene();
		}

		if(preMoveScene.isVisible()) {
			hidePreMoveScene();
		}

	}

	private void hideAllButtons() {
		if(hud.get(BUTTON.ATTACK).isVisible()) {
			hud.hide(BUTTON.ATTACK);
		}
		if(hud.get(BUTTON.DETAILS).isVisible()) {
			hud.hide(BUTTON.DETAILS);
		}
		if(hud.get(BUTTON.AUTO_DEPLOY).isVisible()) {
			hud.hide(BUTTON.AUTO_DEPLOY);
		}
		if(hud.get(BUTTON.MOVE).isVisible()) {
			hud.hide(BUTTON.MOVE);
		}
		if(hud.get(BUTTON.ARROW_LEFT).isVisible()) {
			hud.hide(BUTTON.ARROW_LEFT);
		}
		if(hud.get(BUTTON.ARROW_RIGHT).isVisible()) {
			hud.hide(BUTTON.ARROW_RIGHT);
		}
	}

	/* Common operations when showing a child scene. */
	private void showChildScene() {
		camera.zoomOut();
		hideAllButtons();
		hud.hide(SPRITE.INFO_TAB);
		hud.setDetailButtonToExit();
		lockUserInput();
	}

	/* Common operations when hiding a child scene. */
	private void hideChildScene() {
		hideAllButtons();
		hud.show(SPRITE.INFO_TAB);	
		hud.setDetailButtonToQuestion();	
		unlockUserInput();
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
		if(battleScene.isVisible())
		{
			hideBattleScene();
		}
		else
		{
			return;
		}
		
		buttonTouched();
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
			logic.update();
		}
		else
		{
			showPreBattleScene(logic.attackingSoldiers, logic.defendingSoldiers); // TODO alter this, maybe, no?
		}
	}

	public void onAutoDeployButtonTouched()
	{
		logic.getCurrentPlayer().deployAllSoldiers();
		buttonTouched();
	}

	public void onLeftArrowTouched()
	{
		if(preBattleScene != null && preBattleScene.isVisible())
		{
			preBattleScene.decreaseSoldiers();
		} else if(preMoveScene != null && preMoveScene.isVisible()) {
			preMoveScene.decreaseSoldiers();
		}
	}

	public void onRightArrowTouched()
	{
		if(preBattleScene != null && preBattleScene.isVisible())
		{
			preBattleScene.increaseSoldiers();
		} else if(preMoveScene != null && preMoveScene.isVisible()) {
			preMoveScene.increaseSoldiers();
		}
	}

	public void onMoveButtonTouched() {
		if(!preMoveScene.isVisible()) {
			showPreMoveScene(logic.selectedRegion.getNumberOfSoldiers() - logic.MIN_SOLDIERS_PER_REGION);
		} else {
			hidePreMoveScene();
			logic.movingSoldiers = preMoveScene.getAttackingSoldiers();
			buttonTouched();
		}
	}

	public void onNextTurnButtonTouched() {
		buttonTouched();
	}
	
	private void initVars() {
		logic = new GameLogic(this);

		map = resources.maps.get(0);

		regionButtons = new ArrayList<ButtonSprite>();
		regionButtonsText = new ArrayList<Text>();

		lastTouchTime = 0;
		lastTouchTimeInRegion = 0;
		doubleTapAllowed = true;
	}
	
	private void buttonTouched() {
		logic.update();
		draw();
	}

	@Override
	public void onSceneShow() {
		// TODO Auto-generated method stub
		
	}
}


