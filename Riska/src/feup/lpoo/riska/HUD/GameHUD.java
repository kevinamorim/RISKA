package feup.lpoo.riska.HUD;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.HUD.GameHUD.BUTTON;
import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.DetailScene;
import feup.lpoo.riska.scenes.GameScene;
import feup.lpoo.riska.scenes.SceneManager;
import feup.lpoo.riska.utilities.Utilities;
import android.view.MotionEvent;

public class GameHUD extends HUD implements Displayable {

	// ======================================================
	// CONSTANTS
	// ======================================================
	private static final long MIN_TOUCH_INTERVAL = 30;
	public enum BUTTON
	{
		ATTACK, 
		DETAILS,
		AUTO_DEPLOY
	};

	// ======================================================
	// SINGLETONS
	// ======================================================
	ResourceCache resources;

	// ======================================================
	// FIELDS
	// ======================================================
	private long lastTouchTime;
	private ButtonSprite attackButton;
	private ButtonSprite detailsButton;
	private ButtonSprite autoDeployButton;	
	private Sprite infoTab;
	private Text infoTabText;
	
	private GameScene gameScene;

	public GameHUD(GameScene scene) {
		
		gameScene = scene;

		lastTouchTime = 0;

		resources = ResourceCache.getSharedInstance();

		createDisplay();
	}

	public void createDisplay() {	

		createAttackButton();
		createInfoTab();
		createDetailsButton();
		createAutoDeployButton();

	}

	/**
	 * Handles the touch event for the details button.
	 */
	protected void touchedDetailsButton() {

	}
	
	private void pressed(BUTTON current)
	{
		switch(current)
		{
		
		case ATTACK:
			attackButton.setCurrentTileIndex(1);
			break;
			
		case DETAILS:
			// Do something
			break;
			
		case AUTO_DEPLOY:
			autoDeployButton.setCurrentTileIndex(1);
			break;
			
		default:
			// Do nothing
			break;
		}
	}
	
	private void touched(BUTTON current)
	{
		switch(current)
		{
		
		case ATTACK:
			long now = System.currentTimeMillis();

			if(Utilities.isValidTouch(now, lastTouchTime, MIN_TOUCH_INTERVAL))
			{
				attackButton.setCurrentTileIndex(0);
				gameScene.onAttack();
			}

			lastTouchTime = now;
			break;
			
		case DETAILS:
			gameScene.touchedDetailsButton();
			break;
			
		case AUTO_DEPLOY:
			gameScene.onAutoDeploy();
			break;
			
		default:
			// Do nothing
			break;
		}
	}

	private void released(BUTTON current)
	{
		switch(current) {
		
		case ATTACK:
			attackButton.setCurrentTileIndex(0);
			break;
			
		case DETAILS:
			// Do something
			break;
			
		case AUTO_DEPLOY:
			autoDeployButton.setCurrentTileIndex(0);
			break;
			
		default:
			// Do nothing
			break;
		}
	}
	
	/**
	 * Displays the game info tab.
	 */
	public void showInfoTab() {
		if(!infoTab.hasParent()) {
			attachChild(infoTab);
		}	
	}

	/**
	 * Hides the game info tab.
	 */
	public void hideInfoTab() {
		if(infoTab.hasParent()) {
			detachChild(infoTab);
		}
	}

	/**
	 * Changes the text of the info tab.
	 * 
	 * @param info : new info to set
	 */
	public void setInfoTabText(String info) {
		infoTabText.setText(info);
	}

	public void changeDetailButton()
	{
		if(detailsButton.getCurrentTileIndex() == 0)
		{
			detailsButton.setCurrentTileIndex(1);
		}
		else
		{
			detailsButton.setCurrentTileIndex(0);
		}
	}

	/** 
	 * Locks HUD so the user can't use it. 
	 */
	public void lock() {

		detailsButton.setEnabled(false);
		attackButton.setEnabled(false);

	}

	/** 
	 * Unlocks HUD so the user can use it.
	 */
	public void unlock() {

		detailsButton.setEnabled(true);
		attackButton.setEnabled(true);

	}
	
	private void createAttackButton() {
		
		attackButton = new ButtonSprite(0, 0,
				resources.attackBtnRegion,
				resources.vbom) 
		{

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, 
					float pTouchAreaLocalX, float pTouchAreaLocalY)
			{
				switch(pSceneTouchEvent.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					pressed(BUTTON.ATTACK);
					break;
				case MotionEvent.ACTION_UP:
					touched(BUTTON.ATTACK);
					break;
				case MotionEvent.ACTION_OUTSIDE:
					released(BUTTON.ATTACK);
					break;
				default:
					break;
				}
				return true;
			}
		};
		
		attackButton.setScaleX(.5f);
		attackButton.setScaleY(.6f);
		attackButton.setPosition(
				(resources.camera.getWidth() - (attackButton.getScaleX() * attackButton.getWidth() / 2f) + 4),
				(resources.camera.getHeight()/2f));
		
	}
	
	private void createInfoTab() {
		
		infoTab = new Sprite(resources.camera.getWidth()/ 2,
				resources.camera.getHeight() - resources.infoTabRegion.getHeight() / 2,
				resources.infoTabRegion,
				resources.vbom);

		infoTab.setScaleX(2.4f);

		infoTabText = new Text(infoTab.getWidth() / 2, infoTab.getHeight() / 2,
				resources.mInfoTabFont, "NO INFO", 1000, resources.vbom);

		infoTabText.setColor(Color.BLACK);

		infoTab.attachChild(infoTabText);
		
	}
	
	private void createDetailsButton() {
		
		detailsButton = new ButtonSprite(
				resources.detailsBtnRegion.getWidth() / 2,
				resources.camera.getHeight() / 2,
				resources.detailsBtnRegion,
				resources.vbom) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, 
					float pTouchAreaLocalX, float pTouchAreaLocalY)
			{
				switch(pSceneTouchEvent.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					pressed(BUTTON.DETAILS);
					break;
				case MotionEvent.ACTION_UP:
					touched(BUTTON.DETAILS);
					break;
				case MotionEvent.ACTION_OUTSIDE:
					released(BUTTON.DETAILS);
					break;
				default:
					break;
				}
				return true;
			}
		};
		
		detailsButton.setScale(0.5f);
		detailsButton.setPosition(detailsButton.getScaleX() * detailsButton.getWidth() / 2, detailsButton.getY());
	}
	
	private void createAutoDeployButton() {
		
		autoDeployButton = new ButtonSprite(
				0f,
				0f,
				resources.autoDeployBtnRegion,
				resources.vbom) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, 
					float pTouchAreaLocalX, float pTouchAreaLocalY)
			{
				switch(pSceneTouchEvent.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					pressed(BUTTON.AUTO_DEPLOY);
					break;
				case MotionEvent.ACTION_UP:
					touched(BUTTON.AUTO_DEPLOY);
					break;
				case MotionEvent.ACTION_OUTSIDE:
					released(BUTTON.AUTO_DEPLOY);
					break;
				default:
					break;
				}
				return true;
			}
		};
		
		autoDeployButton.setScaleX(.5f);
		autoDeployButton.setScaleY(.6f);
		autoDeployButton.setPosition(
				(resources.camera.getWidth() - (autoDeployButton.getScaleX() * autoDeployButton.getWidth() / 2f) + 4),
				(resources.camera.getHeight()/2f));
		
	}

	public void hide(BUTTON toHide)
	{
		ButtonSprite btn = getButton(toHide);
		
		if(btn == null)
		{
			return;
		}
		
		if(btn.hasParent())
		{
			detachChild(btn);
			unregisterTouchArea(btn);
		}
	}
	
	public void show(BUTTON toShow)
	{
		ButtonSprite btn = getButton(toShow);
		
		if(btn == null)
		{
			return;
		}
		
		if(!btn.hasParent()) {
			attachChild(btn);
			registerTouchArea(btn);
		}
	}
	
	public ButtonSprite getButton(BUTTON btn)
	{
		switch(btn)
		{
		case ATTACK:
			return attackButton;
		case DETAILS:
			return detailsButton;
		case AUTO_DEPLOY:
			return autoDeployButton;
		default:
			return null;
		}
	}
}
