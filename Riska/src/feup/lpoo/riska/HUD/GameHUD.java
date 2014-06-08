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
import feup.lpoo.riska.scenes.SceneManager;
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
	private Sprite panel;
	private Text countryName;
	private ButtonSprite hudButton;
	private Text hudButtonText;
	
	
	private ButtonSprite attackButton;
	private Text attackText;
	
	private Sprite infoTab;
	private Text infoTabText;

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
		panel = new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT,
				resources.getHUDPanelTexture(), activity.getVertexBufferObjectManager());

		countryName = new Text(0, 0, resources.getGameFont(), "COUNTRY", 1000, activity.getVertexBufferObjectManager());

		countryName.setPosition(PANEL_CENTER_X, 
				MainActivity.CAMERA_HEIGHT - countryName.getHeight());

		hudButton = new ButtonSprite(MainActivity.CAMERA_WIDTH/4, MainActivity.CAMERA_HEIGHT/5, resources.getStartButtonTexture(),
				activity.getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, 
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
				case MotionEvent.ACTION_DOWN:
					pressedConfirmationButton();
					break;
				case MotionEvent.ACTION_UP:
					releasedConfirmationButton();
					break;
				case MotionEvent.ACTION_OUTSIDE:
					releasedConfirmationButton();
					break;
				default:
					break;
				}
				return true;
			}
		};
		
		hudButtonText = new Text(hudButton.getWidth() / 2, hudButton.getHeight() / 2, 
				resources.getFont(), "DEFAULT", activity.getVertexBufferObjectManager());
		hudButton.attachChild(hudButtonText);
		
		/* =================================
		 *  NEW ATTACK BUTTON
		 * ================================= */
		attackButton = new ButtonSprite(MainActivity.CAMERA_WIDTH / 2,
				resources.getAttackButtonTexture().getHeight() / 2,
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
		
		attackText = new Text(0, 0, resources.getGameFont(), "ATTACK", 50, activity.getVertexBufferObjectManager());
		attackText.setColor(Color.BLACK);
		
		attackButton.attachChild(attackText);
		
		
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
		
		/*
		 * ==================================
		 */
		
		panel.attachChild(countryName);

		//attachChild(panel);
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
	}

	public void pressedConfirmationButton() {
		
		hudButton.setCurrentTileIndex(1);	
	}
	
	public void releasedConfirmationButton() {
		
		long now = System.currentTimeMillis();
		
		if((now - lastTimeTouched) > MIN_TOUCH_INTERVAL) {
			
			hudButton.setCurrentTileIndex(0);
			sceneManager.getGameScene().onRegionConfirmed();	
		}
		
		lastTimeTouched = System.currentTimeMillis();
		
	}

	private String wrapText(Font pFont, String pString, float maxWidth) {

		Text pText = new Text(0, 0, pFont, pString, 1000, activity.getVertexBufferObjectManager());

		if(pText.getWidth() < maxWidth) {
			return pString;
		}

		// Split the entire string into separated words.
		String[] words = pText.getText().toString().split(" ");

		String wrappedText = ""; /* Final string. */
		String line = ""; /* Temp variable */

		for(String word : words) {

			pText.setText(line + word);
			if(pText.getWidth() > maxWidth) {			
				wrappedText += line + "\n\n";
				line = "";

			}

			line += word + " ";

		}

		wrappedText += line;

		return wrappedText;

	}

	public void hide() {
		setVisible(false);
	}

	public void show() {
		setVisible(true);
	}

	public ButtonSprite getHudButton() {
		return this.hudButton;
	}

/*	public void update(String regionName, String hudText, boolean enabled) {

		hudButton.setEnabled(enabled);
		hudButtonText.setText(hudText);

		countryName.setText(wrapText(resources.getGameFont(), regionName, panel.getWidth()/2));
		countryName.setPosition(PANEL_CENTER_X, MainActivity.CAMERA_HEIGHT - countryName.getHeight());

		//		if(countryFlag != null) { 
		//			panel.detachChild(countryFlag);
		//		} 
		//
		//		countryFlag = hudButtonText2.getFlag(FLAG_POS.x, FLAG_POS.y);

		//panel.attachChild(countryFlag);
		panel.attachChild(countryName);	
		panel.attachChild(hudButton);

		if(hudButton.isEnabled()) {
			registerTouchArea(hudButton);
		}
	}*/

	public void updateCountry(Region focusedRegion) {
		countryName.setText(wrapText(resources.getGameFont(), focusedRegion.getName(), panel.getWidth()/2));
		countryName.setPosition(PANEL_CENTER_X, MainActivity.CAMERA_HEIGHT - countryName.getHeight());
		panel.attachChild(countryName);	
	}

	public void updateButton(Region focusedRegion, Region selectedRegion, Player player) {
		
		panel.detachChild(hudButton);
		hudButton.setEnabled(false);
		
		if(player.ownsRegion(focusedRegion)) {
			hudButtonText.setText(focusedRegion == selectedRegion ? "RESET" : "CHOOSE");
			hudButton.setEnabled(true);
		}
		else {
			if(selectedRegion != null) {
				if(focusedRegion.isNeighbourOf(selectedRegion)) {
					hudButtonText.setText("ATTACK");		
					hudButton.setEnabled(true);
				}
			}
		}
		
		if(hudButton.isEnabled()) {
			panel.attachChild(hudButton);
			registerTouchArea(hudButton);
		}
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


}
