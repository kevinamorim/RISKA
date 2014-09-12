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
import feup.lpoo.riska.logic.BattleGenerator;
import feup.lpoo.riska.logic.GameInfo;
import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.logic.GameLogic.GAME_STATE;
import feup.lpoo.riska.logic.SceneManager.SCENE_TYPE;
import feup.lpoo.riska.resources.ResourceCache;
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
	private final int MAX_REGION_CHARS = 10;

	// ======================================================
	// FIELDS
	// ======================================================
	private GameLogic logic;
	private GameHUD hud;

	private AttackScene attackScene;
	private SummonScene summonScene;
	private DeployScene deployScene;

	private ScrollDetector scrollDetector;
	private Map map;		
	private ArrayList<ButtonSprite> regionButtons;
	private ArrayList<Text> regionButtonsText;
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
	private void createDisplay()
	{
		createBackground();
		createHUD();
		createRegions();
		createChildScenes();
	}

	private void createBackground()
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
		hud = new GameHUD(this, logic);
		//hud.show(SPRITE.INFO_TAB);	
		camera.setHUD(hud);
	}

	private void createRegions()
	{
		Text buttonText;
		ButtonSprite regionButton;

		for(Region region : map.getRegions()) {

			int x = (int)((region.getX() * camera.getWidth()) / 100);
			int y = (int)((region.getY() * camera.getHeight()) / 100);

			buttonText = new Text(0, 0, resources.mGameFont, "", MAX_REGION_CHARS, vbom);

			regionButton = new ButtonSprite(x, y, resources.regionButtonRegion, vbom) {

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

			regionButton.setTag(region.ID);
			regionButton.setPosition(x, y);
			regionButton.setSize(0.1f * camera.getHeight(), 0.1f * camera.getHeight());

			regionButton.setColor(region.getPrimaryColor());

			buttonText.setText("" + region.getGarrison());
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

		if(!logic.getCurrentPlayer().isCpu) {
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
				logic.selectRegion(pRegion1);
				draw();
			}

		};

		DelayModifier targetRegionDelay = new DelayModifier(CPU_DELAY * 2)
		{
			@Override
			protected void onModifierFinished(IEntity pItem)
			{
				logic.targetRegion(pRegion2);
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
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pX, float pY)
	{		
		unregisterTouchAreaForAllRegions();
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
				btnText.setText("" + region.getGarrison());

				if(region.getPrimaryColor() != btn.getColor())
				{
					btn.setColor(region.getPrimaryColor());
					btnText.setColor(region.getSecundaryColor());
				}
			}
		}

	}

	private void onRegionPressed(ButtonSprite btn, int regionID)
	{
		btn.setCurrentTileIndex(1);
	}

	private void onRegionTouched(ButtonSprite btn, int regionID)
	{
		onRegionReleased(btn, regionID);

		logic.onRegionTouched(map.getRegionById(regionID));
		updateRegion(regionID);
		draw();
	}

	private void onRegionReleased(ButtonSprite btn, int regionID)
	{
		btn.setCurrentTileIndex(0);
	}

	private void updateRegion(int i)
	{
		Region reg = map.getRegionById(i);
		regionButtonsText.get(i).setText("" + reg.getGarrison());
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
					if(comp.owner() == logic.getCurrentPlayer())
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
					if(comp.owner() != logic.getCurrentPlayer())
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
	private void hideChildScene() {
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
		logic = new GameLogic(this);

		map = ResourceCache.getSharedInstance().maps.get(GameInfo.currentMapIndex);

		regionButtons = new ArrayList<ButtonSprite>();
		regionButtonsText = new ArrayList<Text>();

		lastTouchTime = 0;
		doubleTapAllowed = true;
	}

	private void buttonTouched()
	{
		logic.update();
		draw();
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

	// ======================================================
	// ======================================================

}


