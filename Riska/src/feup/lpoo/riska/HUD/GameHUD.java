package feup.lpoo.riska.HUD;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

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

	// ======================================================
	// SINGLETONS
	// ======================================================
	MainActivity activity;
	SceneManager sceneManager;
	ResourceCache resources;

	// ======================================================
	// FIELDS
	// ======================================================
	private long lastTimeTouched;
	
	private ButtonSprite attackButton;
	private ButtonSprite detailsButton;
	
	private Sprite infoTab;
	private Text infoTabText;
	
	/**
	 * Constructor for the game HUD.
	 * <p>
	 * Created the HUD display.
	 */
	public GameHUD() {
		
		lastTimeTouched = 0;

		activity = MainActivity.getSharedInstance();
		sceneManager = SceneManager.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		
		createDisplay();
	}
	
	/**
	 * Creates the display for the given scene.
	 */
	public void createDisplay() {
		
		// =================================
		//  NEW ATTACK BUTTON
		// =================================
		attackButton = new ButtonSprite(0, 0,
				resources.getAttackButtonTexture(),
				activity.getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, 
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
				case MotionEvent.ACTION_DOWN:
					pressedAttackButton();
					break;
				case MotionEvent.ACTION_UP:
					touchedAttackButton();
					break;
				case MotionEvent.ACTION_OUTSIDE:
					releasedAttackButton();
					break;
				default:
					break;
				}
				return true;
			}
		};
		
		attackButton.setScaleX(0.3f);
		attackButton.setScaleY(0.2f);
		attackButton.setPosition((MainActivity.CAMERA_WIDTH/2),
				attackButton.getScaleY() * attackButton.getHeight() / 2);
		

		// =================================
		//  NEW INFO TAB
		// =================================
		infoTab = new Sprite(MainActivity.CAMERA_WIDTH / 2,
				MainActivity.CAMERA_HEIGHT - resources.getInfoTabTexture().getHeight() / 2,
				resources.getInfoTabTexture(),
				activity.getVertexBufferObjectManager());
		
		infoTab.setScaleX(2.4f);
		
		infoTabText = new Text(infoTab.getWidth() / 2, infoTab.getHeight() / 2,
				resources.mInfoTabFont, "NO INFO", 1000, activity.getVertexBufferObjectManager());
		
		infoTabText.setColor(Color.BLACK);

		infoTab.attachChild(infoTabText);

		// =================================
		//  NEW DETAILS BUTTON
		// =================================
		detailsButton = new ButtonSprite(
				resources.getDetailsButtonTexture().getWidth() / 2,
				MainActivity.CAMERA_HEIGHT / 2,
				resources.getDetailsButtonTexture(),
				activity.getVertexBufferObjectManager()) {
			
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, 
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_UP:
					touchedDetailsButton();
					break;
				case MotionEvent.ACTION_OUTSIDE:
					break;
				default:
					break;
				}
				return true;
			}
		};
		detailsButton.setScale(0.5f);
		detailsButton.setPosition(detailsButton.getScaleX() * detailsButton.getWidth() / 2, detailsButton.getY());
		
		/*
		 * ==================================
		 */
		
	}
	
	/**
	 * Handles the touch event for the details button.
	 */
	protected void touchedDetailsButton() {
		
		GameScene gameScene = sceneManager.getGameScene();
		DetailScene details = gameScene.getDetailScene();
	
		if(details.isVisible()) {
			detailsButton.setCurrentTileIndex(0);
			gameScene.hideDetailPanel();
			details.setVisible(false);
		}
		else {
		
			if(gameScene.getBattleScene() != null && gameScene.getBattleScene().isVisible()) {
				detailsButton.setCurrentTileIndex(0);
				gameScene.hideBattleScene();
			} else {
				detailsButton.setCurrentTileIndex(1);
				
				details.setAttributes(gameScene.getSelectedRegion(), gameScene.getTargetedRegion());
				details.updateDisplay();
				gameScene.showDetailPanel();
				gameScene.getCameraManager().zoomOut();
				details.setVisible(true);
			}

		}
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
			sceneManager.getGameScene().onAttack();
		}

		lastTimeTouched = System.currentTimeMillis();
	}

	/**
	 * Handles the pressed event for the attack button.
	 */
	protected void pressedAttackButton() {
		attackButton.setCurrentTileIndex(1);
		detailsButton.setCurrentTileIndex(1);
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

	
	public void changeDetailButton() {
		if(detailsButton.getCurrentTileIndex() == 0) {
			detailsButton.setCurrentTileIndex(1);
		} else {
			detailsButton.setCurrentTileIndex(0);
		}
	}
}
