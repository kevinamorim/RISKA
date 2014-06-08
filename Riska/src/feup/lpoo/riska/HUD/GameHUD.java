package feup.lpoo.riska.HUD;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.elements.Player;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.CameraManager;
import feup.lpoo.riska.scenes.DetailScene;
import feup.lpoo.riska.scenes.SceneManager;
import feup.lpoo.riska.scenes.SceneManager.SceneType;
import android.view.MotionEvent;

public class GameHUD extends HUD {

	// ======================================================
	// CONSTANTS
	// ======================================================
	private static final long MIN_TOUCH_INTERVAL = 30;

	//private final Point FLAG_POS = new Point((int)(MainActivity.CAMERA_WIDTH/4), (int)(MainActivity.CAMERA_HEIGHT/2));
	private final int PANEL_CENTER_X = MainActivity.CAMERA_WIDTH/4;

	// ======================================================
	// SINGLETONS
	// ======================================================
	MainActivity activity;
	SceneManager sceneManager;
	CameraManager cameraManager;
	ResourceCache resources;

	// ======================================================
	// FIELDS
	// ======================================================		
	private ButtonSprite attackButton;
	
	private Sprite infoTab;
	private Text infoTabText;
	
	private ButtonSprite detailsButton;

	private long lastTimeTouched;

	public GameHUD() {
		
		lastTimeTouched = 0;

		activity = MainActivity.getSharedInstance();
		sceneManager = SceneManager.getSharedInstance();
		cameraManager = CameraManager.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		
		createDisplay();
	}
	
	private void createDisplay() {
		
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
		
		attackButton.setScale(0.5f);
		attackButton.setPosition(
				attackButton.getScaleX() * attackButton.getWidth() / 2,
				attackButton.getScaleY() * attackButton.getHeight() / 2);
		

		// =================================
		//  NEW INFO TAB
		// =================================
		infoTab = new Sprite(MainActivity.CAMERA_WIDTH / 2,
				MainActivity.CAMERA_HEIGHT - resources.getInfoTabTexture().getHeight() / 2,
				resources.getInfoTabTexture(),
				activity.getVertexBufferObjectManager());
		
		infoTab.setScaleX(2.0f);
		
		infoTabText = new Text(infoTab.getWidth() / 2, infoTab.getHeight() / 2,
				resources.getGameFont(), "NO INFO", 1000, activity.getVertexBufferObjectManager());
		
		infoTabText.setScale(0.6f);
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
	
	protected void touchedDetailsButton() {
		
		DetailScene details = sceneManager.getGameScene().getDetailScene();
	
		if(details.isVisible()) {
			detailsButton.setCurrentTileIndex(0);
			sceneManager.getGameScene().hideDetailPanel();
			details.setVisible(false);
		}
		else {
		
			if(sceneManager.getGameScene().getBattleScene() != null &&
					sceneManager.getGameScene().getBattleScene().isVisible()) {
				detailsButton.setCurrentTileIndex(0);
				sceneManager.getGameScene().hideBattleScene();
			} else {
				detailsButton.setCurrentTileIndex(1);
				
				details.setAttributes(sceneManager.getGameScene().getSelectedRegion(), 
						sceneManager.getGameScene().getTargetedRegion());
				details.updateDisplay();
				sceneManager.getGameScene().showDetailPanel();
				sceneManager.getGameScene().getCameraManager().setZoomFactor(1.0f);
				details.setVisible(true);
			}

		}
	}

	protected void releasedAttackButton() {
		attackButton.setCurrentTileIndex(0);
	}

	protected void touchedAttackButton() {
		long now = System.currentTimeMillis();

		if((now - lastTimeTouched) > MIN_TOUCH_INTERVAL) {
			attackButton.setCurrentTileIndex(0);
			sceneManager.getGameScene().onAttack();
		}

		lastTimeTouched = System.currentTimeMillis();
	}

	protected void pressedAttackButton() {
		attackButton.setCurrentTileIndex(1);
		detailsButton.setCurrentTileIndex(1);
	}

	public void hide() {
		setVisible(false);
	}

	public void show() {
		setVisible(true);
	}

	public void showAttackButton() {
		attachChild(attackButton);
		registerTouchArea(attackButton);
	}
	
	public void hideAttackButton() {
		detachChild(attackButton);
		unregisterTouchArea(attackButton);
	}
	
	public void showInfoTab() {
		attachChild(infoTab);
	}
	
	public void hideInfoTab() {
		detachChild(infoTab);
	}

	public void setInfoTabText(String info) {
		infoTab.detachChild(infoTabText);
		infoTabText.setText(info);
		infoTab.attachChild(infoTabText);
	}

	public void showDetailButton() {
		attachChild(detailsButton);
		registerTouchArea(detailsButton);
	}
	
	public void hideDetailButton() {
		detachChild(detailsButton);
		unregisterTouchArea(detailsButton);
	}


}
