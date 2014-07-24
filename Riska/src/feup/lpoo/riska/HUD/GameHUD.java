package feup.lpoo.riska.HUD;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.gameInterface.AnimatedTextButtonSpriteMenuItem;
import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.DetailScene;
import feup.lpoo.riska.scenes.GameScene;
import feup.lpoo.riska.scenes.SceneManager;
import android.view.MotionEvent;

public class GameHUD extends HUD implements Displayable {

	// ======================================================
	// CONSTANTS
	// ======================================================
	private static final long MIN_TOUCH_INTERVAL = 30;
	private enum B {ATTACK, DETAILS, AUTO_DEPLOY};

	// ======================================================
	// SINGLETONS
	// ======================================================
	ResourceCache resources;

	// ======================================================
	// FIELDS
	// ======================================================
	private long lastTimeTouched;
	private ButtonSprite attackButton;
	private ButtonSprite detailsButton;
	private ButtonSprite autoDeployButton;	
	private Sprite infoTab;
	private Text infoTabText;


	public GameHUD() {

		lastTimeTouched = 0;

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

		GameScene gameScene = SceneManager.getSharedInstance().getGameScene();
		DetailScene details = gameScene.getDetailScene();

		changeDetailButton();

		if(details.isVisible()) {
			gameScene.hideDetailPanel();
			details.setVisible(false);
		}
		else {

			if(gameScene.getBattleScene() != null && gameScene.getBattleScene().isVisible()) {
				gameScene.hideBattleScene();
			} else {				
				details.setAttributes(gameScene.getSelectedRegion(), gameScene.getTargetedRegion());
				details.updateDisplay();
				gameScene.showDetailPanel();
				gameScene.getCameraManager().zoomOut();
				details.setVisible(true);
			}

		}
	}
	
	private void pressed(B current)
	{
		switch(current) {
		case ATTACK:
			pressedAttackButton();
			break;
		case DETAILS:
			// Do something
			break;
		case AUTO_DEPLOY:
			pressedAutoDeployButton();
			break;
		default:
			// Do nothing
			break;
		}
	}
	
	private void touched(B current)
	{
		switch(current) {
		case ATTACK:
			touchedAttackButton();
			break;
		case DETAILS:
			touchedDetailsButton();
			break;
		case AUTO_DEPLOY:
			touchedAutoDeployButton();
			break;
		default:
			// Do nothing
			break;
		}
	}

	private void released(B current)
	{
		switch(current) {
		case ATTACK:
			releasedAttackButton();
			break;
		case DETAILS:
			// Do something
			break;
		case AUTO_DEPLOY:
			releasedAutoDeployButton();
			break;
		default:
			// Do nothing
			break;
		}
	}
	
	private void pressedAutoDeployButton() {
		autoDeployButton.setCurrentTileIndex(1);
	}
	
	private void touchedAutoDeployButton() {
		SceneManager.getSharedInstance().getGameScene().onAutoDeploy();
	}
	
	private void releasedAutoDeployButton() {
		autoDeployButton.setCurrentTileIndex(0);
	}

	/**
	 * Handles the release event for the attack button.
	 */
	protected void releasedAttackButton() {
		attackButton.setCurrentTileIndex(0);
	}

	/**
	 * Handles the touch event for the attack button.
	 */
	protected void touchedAttackButton() {
		long now = System.currentTimeMillis();

		if((now - lastTimeTouched) > MIN_TOUCH_INTERVAL) {
			attackButton.setCurrentTileIndex(0);
			SceneManager.getSharedInstance().getGameScene().onAttack();
			SceneManager.getSharedInstance().getGameScene().getCameraManager().zoomOut();
		}

		lastTimeTouched = now;
	}

	/**
	 * Handles the pressed event for the attack button.
	 */
	protected void pressedAttackButton() {
		attackButton.setCurrentTileIndex(1);
	}
	
	/**
	 * Displays the attack button.
	 */
	public void showAttackButton() {

		if(!attackButton.hasParent()) {
			attachChild(attackButton);
			registerTouchArea(attackButton);
		}
	}

	/**
	 * Hides the attack button.
	 */
	public void hideAttackButton() {

		if(attackButton.hasParent()) {
			detachChild(attackButton);
			unregisterTouchArea(attackButton);
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

	/**
	 * Displays the details button.
	 */
	public void showDetailButton() {

		if(!detailsButton.hasParent()) {
			attachChild(detailsButton);
			registerTouchArea(detailsButton);
		}

	}

	/**
	 * Hides the details button.
	 */
	public void hideDetailButton() {

		if(detailsButton.hasParent()) {
			detachChild(detailsButton);
			unregisterTouchArea(detailsButton);
		}
	}
	public void showAutoDeployButton() {

		if(!autoDeployButton.hasParent()) {
			attachChild(autoDeployButton);
			registerTouchArea(autoDeployButton);
		}

	}
	public void hideAutoDeployButton() {

		if(autoDeployButton.hasParent()) {
			detachChild(autoDeployButton);
			unregisterTouchArea(autoDeployButton);
		}
	}

	public void changeDetailButton() {
		if(detailsButton.getCurrentTileIndex() == 0) {
			detailsButton.setCurrentTileIndex(1);
		} else {
			detailsButton.setCurrentTileIndex(0);
		}
	}

	/** 
	 * Locks HUD so the user can't use it. 
	 */
	public void lockHUD() {

		detailsButton.setEnabled(false);
		attackButton.setEnabled(false);

	}

	/** 
	 * Unlocks HUD so the user can use it.
	 */
	public void unlockHUD() {

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
					pressed(B.ATTACK);
					break;
				case MotionEvent.ACTION_UP:
					touched(B.ATTACK);
					break;
				case MotionEvent.ACTION_OUTSIDE:
					released(B.ATTACK);
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
					pressed(B.DETAILS);
					break;
				case MotionEvent.ACTION_UP:
					touched(B.DETAILS);
					break;
				case MotionEvent.ACTION_OUTSIDE:
					released(B.DETAILS);
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
					pressed(B.AUTO_DEPLOY);
					break;
				case MotionEvent.ACTION_UP:
					touched(B.AUTO_DEPLOY);
					break;
				case MotionEvent.ACTION_OUTSIDE:
					released(B.AUTO_DEPLOY);
					break;
				default:
					break;
				}
				return true;
			}
		};
		
		autoDeployButton.setScale(0.3f);
		autoDeployButton.setPosition(0.66f * resources.camera.getWidth(), 0.18f * resources.camera.getHeight());
		
	}
}
