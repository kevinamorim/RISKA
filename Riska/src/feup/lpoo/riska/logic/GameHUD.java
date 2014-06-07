package feup.lpoo.riska.logic;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;

import android.graphics.Point;
import android.util.Log;

public class GameHUD extends HUD {
	
	// ======================================================
	// CONSTANTS
	// ======================================================
	private final Point FLAG_POS = new Point((int)(MainActivity.CAMERA_WIDTH/4), (int)(MainActivity.CAMERA_HEIGHT/2));
	private final int PANEL_CENTER_X = MainActivity.CAMERA_WIDTH/4;
	private final int ATTACK_ID = 1;
	
	MainActivity activity;
	SceneManager instance;
	CameraManager cameraManager;
	
	private MenuScene menuScene;
	
	private Sprite panel;
	private Text countryName;
	private Sprite countryFlag;
	
	private AnimatedTextButtonSpriteMenuItem attackButton;
	
	public GameHUD() {
		
		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();
		cameraManager = CameraManager.getSharedInstance();
		
		panel = new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT,
				instance.mLeftPanelTextureRegion, activity.getVertexBufferObjectManager());
		
		countryName = new Text(0, 0, instance.mGameFont, "COUNTRY", 1000, activity.getVertexBufferObjectManager());
		
		countryName.setPosition(PANEL_CENTER_X, 
				MainActivity.CAMERA_HEIGHT - countryName.getHeight());
		
		panel.attachChild(countryName);
		
		menuScene = new MenuScene(activity.mCamera);
		
		attackButton = new AnimatedTextButtonSpriteMenuItem(ATTACK_ID, instance.mStartButtonTiledTextureRegion.getWidth(), 
				instance.mStartButtonTiledTextureRegion.getHeight(), instance.mStartButtonTiledTextureRegion, 
				activity.getVertexBufferObjectManager(), "ATTACK!", instance.mFont);
		
		attackButton.setPosition(PANEL_CENTER_X, MainActivity.CAMERA_HEIGHT/5);
		menuScene.addMenuItem(attackButton);
		menuScene.setBackgroundEnabled(false);
		
		setChildScene(menuScene);
		
		attachChild(panel);	
		
		attackButton.setText("LOL");
		
	}
	
	public void updateHUD(Region pRegion) {
		countryName.setText(wrapText(instance.mGameFont, pRegion.getName(), panel.getWidth()/2));
		countryName.setPosition(PANEL_CENTER_X, 
				MainActivity.CAMERA_HEIGHT - countryName.getHeight());
		
		if(countryFlag != null) {
			panel.detachChild(countryFlag);
		} 
		
		countryFlag = pRegion.getFlag(FLAG_POS.x, FLAG_POS.y);
		panel.attachChild(countryFlag);
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
		menuScene.unregisterTouchArea(attackButton);
	}
	
	public void show() {
		setVisible(true);
		menuScene.registerTouchArea(attackButton);
	}
}
