package feup.lpoo.riska.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.gameInterface.RiskaSprite;
import feup.lpoo.riska.gameInterface.RiskaTextButtonSprite;
import feup.lpoo.riska.hud.GameHUD;
import feup.lpoo.riska.logic.BattleGenerator;
import feup.lpoo.riska.logic.GameInfo;
import feup.lpoo.riska.logic.GameLogic;
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

	// ======================================================
	// FIELDS
	// ======================================================
	private GameLogic logic;
	private GameHUD hud;

	private AttackScene attackScene;
	private SummonScene summonScene;
	private DeployScene deployScene;

//	private ButtonSprite[] regions;
	private Sprite mapSprite;

	private RiskaSprite[] focusSprite;

	private RiskaTextButtonSprite[] regionButton;

	private float regionButtonSize;
	private float regionButtonSizeSelected;
	private float regionButtonSizeTargeted;
	
	private ScrollDetector scrollDetector;
	private Map map;
	private boolean doubleTapAllowed;
	private long lastTouchTime;

	@Override
	public void createScene()
	{
		initVars();
		createDisplay();
		createScrollDetector();

		setTouchAreaBindingOnActionDownEnabled(true);
		setOnSceneTouchListener(this);	

		draw();
	}

	@Override
	public void onBackKeyPressed()
	{
		//debugRegions();
		camera.zoomOutImmediate();
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
	private void createDisplay()
	{
		createBackground();
		createHUD();
		createMapSprite();
		createRegions();
		createChildScenes();
		createAnimatedSprites();
	}

	private void createBackground()
	{
		setBackground(new Background(Color.BLACK));
		
//		SpriteBackground background = new SpriteBackground(new Sprite(
//				camera.getCenterX(),
//				camera.getCenterY(),
//				camera.getWidth() + 2,
//				camera.getHeight() + 2,
//				resources.background, vbom));
//
//		setBackground(background);
	}

	private void createHUD()
	{
		hud = new GameHUD(this, logic);

		camera.setHUD(hud);
	}

	private void createMapSprite()
	{
		float pX = camera.getCenterX();
		float pY = camera.getCenterY();
		//		float pWidth = hud.mapWidth();
		//		float pHeight = Utils.getRatioInverted() * pWidth;

		float pWidth = camera.getWidth();
		float pHeight = camera.getHeight();

		mapSprite = new Sprite(pX, pY, pWidth, pHeight, resources.map, vbom);

		attachChild(mapSprite);	
	}

	private void createRegions()
	{
		regionButtonSize = 0.07f * mapSprite.getHeight();
		regionButtonSizeSelected = 1.5f * regionButtonSize;
		regionButtonSizeTargeted = 1.2f * regionButtonSize;

		//regions = new ButtonSprite[map.getNumberOfRegions()];
		regionButton = new RiskaTextButtonSprite[map.getNumberOfRegions()];

		for(int i = 0; i < map.getNumberOfRegions(); i++)
		{
			Region region = map.getRegionByIndex(i);

			float pX = 0.01f * region.getStratCenter().x * mapSprite.getWidth();
			float pY = 0.01f * region.getStratCenter().y * mapSprite.getHeight();

			regionButton[i] = new RiskaTextButtonSprite(resources.buttonRegion, vbom, "", resources.mGameNumbersFont, Utils.maxNumericChars)
			{
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
				{
					switch(ev.getMotionEvent().getActionMasked()) 
					{

					case MotionEvent.ACTION_DOWN:
						onRegionPressed(this, getTag());
						break;

					case MotionEvent.ACTION_UP:
						if(Utils.contains(this, pX, pY))
						{
							onRegionTouched(this, getTag());
						}
						else
						{
							onRegionReleased(this, getTag());
						}
						break;

					default:
						break;
					}

					return true;
				}
			};

			regionButton[i].setTag(region.ID);
			regionButton[i].setPosition(pX, pY);
			regionButton[i].setSize(regionButtonSize, regionButtonSize);

			regionButton[i].setColor(region.getPrimaryColor());

			regionButton[i].setTextBoundingFactor(0.9f);
			regionButton[i].setTextColor(region.getSecundaryColor());
			regionButton[i].setText("" + region.getGarrison());

//			regions[i] = new ButtonSprite(0f, 0f, resources.regions[i], vbom);
//
//			float width = mapSprite.getWidth() * resources.regions[i].getWidth() / resources.map.getWidth();
//			float height = mapSprite.getHeight() * resources.regions[i].getHeight() / resources.map.getHeight();
//
//			regions[i].setSize(width, height);
//
//			pX = 0.01f * region.getX() * mapSprite.getWidth();
//			pY = 0.01f * region.getY() * mapSprite.getHeight();
//
//			regions[i].setPosition(pX, pY);
		}

//		for(int i = 0; i < map.getNumberOfRegions(); i++)
//		{
//			regions[i].setColor(map.getRegionByIndex(i).owner().priColor);
//			regions[i].setAlpha(0.8f);
//
//			mapSprite.attachChild(regions[i]);
//		}

		for(int i = 0; i < map.getNumberOfRegions(); i++)
		{
			mapSprite.attachChild(regionButton[i]);
		}

		registerTouchAreaForAllRegions();
	}

	private void createChildScenes()
	{
		attackScene = new AttackScene(logic);
		summonScene = new SummonScene(logic);
		deployScene = new DeployScene(logic);

		attackScene.setVisible(false);
		//attackScene.setAlpha(0f);
		attachChild(attackScene);

		summonScene.setVisible(false);
		//summonScene.setAlpha(0f);
		attachChild(summonScene);

		deployScene.setVisible(false);
		//deployScene.setAlpha(0f);
		attachChild(deployScene);
	}

	private void createAnimatedSprites()
	{
		focusSprite = new RiskaSprite[map.getNumberOfRegions()];

		for(int i = 0; i < focusSprite.length; i++)
		{
			focusSprite[i] = new RiskaSprite(resources.selection, vbom);
			
			focusSprite[i].setPosition(regionButton[i]);
			
			focusSprite[i].fadeOut(0f);

			attachChild(focusSprite[i]);
		}
	}

	// ======================================================
	// ======================================================

	public void draw()
	{		
		if(anyChildSceneIsVisible())
		{
			return;
		}

		if(logic.selectedRegion == null)
		{
			showAllRegions();
		}
		else
		{
			showOnlyNeighbourRegions(logic.selectedRegion);
		}

		drawRegionButtons();

		//hud.draw();

		drawButtons();

	}

	private void drawButtons() {

		/* Reset */
		hideAllButtons(); 

		if(!logic.getCurrentPlayer().isCpu)
		{
			switch(logic.getState()) {
			case SETUP:
				//hud.show(BUTTON.AUTO_DEPLOY);
				break;
			case ATTACK:
				if(logic.selectedRegion != null && logic.targetedRegion != null) {
					//hud.show(BUTTON.ATTACK);
				}
				break;
			case MOVE:
				if(logic.selectedRegion != null && logic.targetedRegion != null) {
					//hud.show(BUTTON.BUTTON);
				}
				break;
			case DEPLOY:
				//hud.show(BUTTON.AUTO_DEPLOY);
				break;
			case ENDTURN:
				//hud.show(BUTTON.NEXT_TURN);
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
				logic.Select(pRegion1);
				draw();
			}

		};

		DelayModifier targetRegionDelay = new DelayModifier(CPU_DELAY * 2)
		{
			@Override
			protected void onModifierFinished(IEntity pItem)
			{
				logic.Target(pRegion2);
				draw();
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
				draw();
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
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
	{		
		switch(pSceneTouchEvent.getMotionEvent().getActionMasked())
		{
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

	private void createScrollDetector()
	{
		scrollDetector = new SurfaceScrollDetector(this);
		scrollDetector.setTriggerScrollMinimumDistance(MIN_SCROLLING_DIST);
		scrollDetector.setEnabled(true);
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pX, float pY)
	{		
		//unregisterTouchAreaForAllRegions();
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID, float pX, float pY)
	{
		Point p = new Point((int)(activity.mCamera.getCenterX() - pX), (int)(activity.mCamera.getCenterY() + pY));
		camera.jumpTo(p);	
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID, float pX, float pY)
	{		
		Point p = new Point((int)(activity.mCamera.getCenterX() - pX), (int)(activity.mCamera.getCenterY() + pY));
		camera.jumpTo(p);

		//registerTouchAreaForAllRegions();
	}

	private void lockUserInput() {

		doubleTapAllowed = false;
		scrollDetector.setEnabled(false);

		//unregisterTouchAreaForAllRegions();
	}

	private void unlockUserInput() {

		doubleTapAllowed = true;
		scrollDetector.setEnabled(true);

		//registerTouchAreaForAllRegions();
	}

	// ======================================================
	// REGION BUTTONS
	// ======================================================
	private void drawRegionButtons() {

		for(int i = 0; i < regionButton.length; i++)
		{
			RiskaTextButtonSprite button = regionButton[i];

			if(button.isVisible())
			{
				Region region = map.getRegionByIndex(i);
				button.setText("" + region.getGarrison());

				if(region.getPrimaryColor() != button.getColor())
				{
					button.setColor(region.getPrimaryColor());
					button.setTextColor(region.getSecundaryColor());
				}
			}
		}

	}

	private void onRegionPressed(ButtonSprite btn, int regionID)
	{
		//btn.setCurrentTileIndex(1);
	}

	private void onRegionTouched(ButtonSprite btn, int regionID)
	{
		onRegionReleased(btn, regionID);

		logic.onRegionTouched(map.getRegionById(regionID));
	}

	public void Untarget(int rID)
	{
		Region region = map.getRegionById(rID);	
		region.setFocus(false);
		
		UpdateRegion(rID);
		
		float fadeDuration = 0.5f;

		regionButton[rID].setCurrentTileIndex(0);
		regionButton[rID].setSize(regionButtonSize, regionButtonSize);
//		focusSprite[rID].fadeOutAndStopRotation(fadeDuration);
	}

	public void Unselect(int rID)
	{
		Region region = map.getRegionById(rID);	
		region.setFocus(false);
		
		UpdateRegion(rID);
		
		float fadeDuration = 0.5f;

		regionButton[rID].setCurrentTileIndex(0);
		regionButton[rID].setSize(regionButtonSize, regionButtonSize);
//		focusSprite[rID].fadeOutAndStopRotation(fadeDuration);
	}

	public void Select(int rID)
	{
		Region region = map.getRegionById(rID);	
		region.setFocus(true);
		
		UpdateRegion(rID);
		
		float fadeDuration = 0.5f;
		float rotationSpeed = 3f;

		regionButton[rID].setCurrentTileIndex(1);
		regionButton[rID].setSize(regionButtonSizeSelected, regionButtonSizeSelected);
//		focusSprite[rID].setSize(1.8f * regionButton[rID].getWidth(), 1.8f * regionButton[rID].getHeight());
//		focusSprite[rID].setColor(Utils.OtherColors.WHITE);
//		focusSprite[rID].fadeIn(fadeDuration);
//		focusSprite[rID].rotate(rotationSpeed);
	}

	public void Target(int rID)
	{
		Region region = map.getRegionById(rID);	
		region.setFocus(true);
		
		UpdateRegion(rID);
		
		float fadeDuration = 0.5f;
		float rotationSpeed = 3f;

		regionButton[rID].setCurrentTileIndex(1);
		regionButton[rID].setSize(regionButtonSizeTargeted, regionButtonSizeTargeted);
//		focusSprite[rID].setSize(1.8f * regionButton[rID].getWidth(), 1.8f * regionButton[rID].getHeight());
//		focusSprite[rID].setColor(Utils.OtherColors.BLACK);
//		focusSprite[rID].fadeIn(fadeDuration);
//		focusSprite[rID].rotate(rotationSpeed);
	}

	private void onRegionReleased(ButtonSprite btn, int regionID)
	{
		//btn.setCurrentTileIndex(0);
	}

	public void UpdateRegion(int rID)
	{
		Region reg = map.getRegionById(rID);
		regionButton[rID].setColor(reg.priColor);
		regionButton[rID].setTextColor(reg.secColor);
		regionButton[rID].setText("" + reg.getGarrison());
	}

	public void showOnlyNeighbourRegions(Region pRegion) {

		for(int i = 0; i < map.getRegions().size(); i++) {

			Region comp = map.getRegions().get(i);

			if (comp != pRegion)
			{
				if(!comp.isNeighbourOf(pRegion))
				{
					regionButton[i].setVisible(false);
					//regionButtons.get(i).first.setEnabled(false);
				}
				//				else
				//				{
				//					if(comp.owner() == logic.getCurrentPlayer())
				//					{
				//						regionButton[i].setVisible(false);
				//						//regionButtons.get(i).first.setEnabled(false);
				//					}
				//				}
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
					regionButton[i].setVisible(false);
					//regionButtons.get(i).first.setEnabled(false);
				}
				else
				{
					if(comp.owner() != logic.getCurrentPlayer())
					{
						regionButton[i].setVisible(false);
					}
				}
			}
		}	
	}

	private void showAllRegions()
	{
		for(int i = 0; i < regionButton.length; i++)
		{
			ButtonSprite btn = regionButton[i];

			if(!btn.isVisible())
			{
				btn.setVisible(true);
			}
		}
	}

	private void registerTouchAreaForAllRegions()
	{
		for(ButtonSprite region : regionButton)
		{
			if(!getTouchAreas().contains(region))
			{
				registerTouchArea(region);
			}
		}
	}

	private void unregisterTouchAreaForAllRegions()
	{
		for(ButtonSprite region : regionButton)
		{
			if(getTouchAreas().contains(region))
			{
				unregisterTouchArea(region);
			}
		}
	}

	// ======================================================
	// CHILD SCENES
	// ======================================================
	public void showBattleScene(Region pRegion1, Region pRegion2, BattleGenerator battleGenerator)
	{
		if(attackScene != null)
		{
			showChildScene();
			//hud.show(BUTTON.DETAILS);
			attackScene.update(pRegion1, pRegion2);
			attackScene.setVisible(true);
		}
	}

	private void hideBattleScene()
	{
		if(attackScene != null)
		{
			hideChildScene();
			attackScene.setVisible(false);
			logic.resumeGame();
		}
	}

	private void showPreBattleScene(int attackingSoldiers, int defendingSoldiers)
	{
		if(summonScene != null)
		{
			showChildScene();
			//hud.show(BUTTON.ATTACK);
			//hud.show(BUTTON.ARROW_LEFT);
			//hud.show(BUTTON.ARROW_RIGHT);
			summonScene.update(attackingSoldiers, defendingSoldiers);
			summonScene.setVisible(true);
		}

	}

	private void showPreMoveScene(int maxSoldiers) {
		if(deployScene != null) {
			showChildScene();
			//hud.show(BUTTON.BUTTON);
			//hud.show(BUTTON.ARROW_LEFT);
			//hud.show(BUTTON.ARROW_RIGHT);
			deployScene.update(maxSoldiers);
			deployScene.setVisible(true);
		}
	}

	private void hidePreBattleScene()
	{
		if(summonScene != null)
		{
			hideChildScene();
			summonScene.setVisible(false);
		}
	}

	private void hidePreMoveScene() {
		if(deployScene != null) {
			hideChildScene();
			deployScene.setVisible(false);
		}
	}

	private boolean anyChildSceneIsVisible()
	{

		if(attackScene.isVisible())
		{
			return true;
		}

		if(summonScene.isVisible())
		{
			return true;
		}

		return false;
	}

	private void hideAllChildScenes() {

		if(attackScene.isVisible())
		{
			hideBattleScene();
		}

		if(summonScene.isVisible())
		{
			hidePreBattleScene();
		}

		if(deployScene.isVisible()) {
			hidePreMoveScene();
		}

	}

	private void hideAllButtons()
	{
		//		if(hud.get(BUTTON.ATTACK).isVisible()) {
		//			hud.hide(BUTTON.ATTACK);
		//		}
		//		if(hud.get(BUTTON.DETAILS).isVisible()) {
		//			hud.hide(BUTTON.DETAILS);
		//		}
		//		if(hud.get(BUTTON.AUTO_DEPLOY).isVisible()) {
		//			hud.hide(BUTTON.AUTO_DEPLOY);
		//		}
		//		if(hud.get(BUTTON.MOVE).isVisible()) {
		//			hud.hide(BUTTON.MOVE);
		//		}
		//		if(hud.get(BUTTON.ARROW_LEFT).isVisible()) {
		//			hud.hide(BUTTON.ARROW_LEFT);
		//		}
		//		if(hud.get(BUTTON.ARROW_RIGHT).isVisible()) {
		//			hud.hide(BUTTON.ARROW_RIGHT);
		//		}
	}

	/* Common operations when showing a child scene. */
	private void showChildScene()
	{
		camera.zoomOut();
		hideAllButtons();
		//hud.hide(SPRITE.INFO_TAB);
		//hud.setDetailButtonToExit();
		lockUserInput();
	}

	/* Common operations when hiding a child scene. */
	private void hideChildScene()
	{
		hideAllButtons();
		//hud.show(SPRITE.INFO_TAB);	
		//hud.setDetailButtonToQuestion();	
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
		if(attackScene.isVisible())
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
		//hud.hide(BUTTON.AUTO_DEPLOY);
	}

	public void onAttackButtonTouched()
	{

		if(summonScene.isVisible())
		{
			hidePreBattleScene();
			logic.attackingSoldiers = summonScene.getAttackingSoldiers();
			logic.UpdatePlayer();
		}
		else
		{
			showPreBattleScene(logic.attackingSoldiers, logic.defendingSoldiers); // TODO alter this, maybe, no?
		}
	}

	public void onAutoDeployButtonTouched()
	{
		logic.deployAllSoldiers(logic.getCurrentPlayer());
	}

	public void onLeftArrowTouched()
	{
		if(summonScene != null && summonScene.isVisible())
		{
			summonScene.decreaseSoldiers();
		} else if(deployScene != null && deployScene.isVisible()) {
			deployScene.decreaseSoldiers();
		}
	}

	public void onRightArrowTouched()
	{
		if(summonScene != null && summonScene.isVisible())
		{
			summonScene.increaseSoldiers();
		} else if(deployScene != null && deployScene.isVisible()) {
			deployScene.increaseSoldiers();
		}
	}

	public void onMoveButtonTouched()
	{
		if(!deployScene.isVisible())
		{
			showPreMoveScene(logic.selectedRegion.getGarrison() - GameInfo.minGarrison);
		}
		else
		{
			hidePreMoveScene();
			logic.movingSoldiers = deployScene.getAttackingSoldiers();
			buttonTouched();
		}
	}

	public void onNextTurnButtonTouched() {
		buttonTouched();
	}

	private void initVars()
	{
		logic = new GameLogic(this, hud);

		map = GameInfo.currentMap;

		lastTouchTime = 0;
		doubleTapAllowed = true;
	}

	private void buttonTouched()
	{


	}


	// ======================================================
	// ======================================================
	public void onDeploy()
	{
		lockHUD();
		deployScene.show();
		// TODO Auto-generated method stub
	}

	public void onAttack()
	{
		lockHUD();
		attackScene.show();
		// TODO Auto-generated method stub

	}

	public void onSummon()
	{
		lockHUD();
		summonScene.show();
		// TODO Auto-generated method stub

	}

	@Override
	public void onMenuKeyPressed() {
		// TODO Auto-generated method stub

	}


	public void updateHUD()
	{
		hud.updateInfo();
	}

	// ======================================================
	// ======================================================

}


