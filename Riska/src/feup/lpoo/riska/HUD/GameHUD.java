package feup.lpoo.riska.HUD;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;

import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.CameraManager;
import feup.lpoo.riska.scenes.SceneManager;
import android.graphics.Point;
import android.util.Log;

public class GameHUD extends HUD {

	// ======================================================
	// CONSTANTS
	// ======================================================
	private final Point FLAG_POS = new Point((int)(MainActivity.CAMERA_WIDTH/4), (int)(MainActivity.CAMERA_HEIGHT/2));
	private final int PANEL_CENTER_X = MainActivity.CAMERA_WIDTH/4;

	MainActivity activity;
	SceneManager sceneManager;
	CameraManager cameraManager;
	ResourceCache resources;

	private Sprite panel;
	private Text countryName;
	private Sprite countryFlag;

	public GameHUD() {

		activity = MainActivity.getSharedInstance();
		sceneManager = SceneManager.getSharedInstance();
		cameraManager = CameraManager.getSharedInstance();
		resources = ResourceCache.getSharedInstance();

		panel = new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT,
				resources.getHUDPanelTexture(), activity.getVertexBufferObjectManager());

		countryName = new Text(0, 0, resources.getGameFont(), "COUNTRY", 1000, activity.getVertexBufferObjectManager());

		countryName.setPosition(PANEL_CENTER_X, 
				MainActivity.CAMERA_HEIGHT - countryName.getHeight());

		panel.attachChild(countryName);

		attachChild(panel);	

	}

	/* TODO: Move this to the updateHUD method. */
	public void updateButtonText(boolean owned) {
		if(owned) {
			//attackButton.setText("CHOOSE");
		} else {
			//attackButton.setText("ATTACK!");
		}
	}

	public void updateHUD(Region pRegion) {
			
		countryName.setText(wrapText(resources.getGameFont(), pRegion.getName(), panel.getWidth()/2));
		countryName.setPosition(PANEL_CENTER_X, 
				MainActivity.CAMERA_HEIGHT - countryName.getHeight());

		if(countryFlag != null) {
			panel.detachChild(countryFlag);
		} 

		countryFlag = pRegion.getFlag(FLAG_POS.x, FLAG_POS.y);
		panel.attachChild(countryName);
		panel.attachChild(countryFlag);
		panel.attachChild(pRegion.getHudButton());
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
		panel.detachChildren();
		setVisible(false);
	}

	public void show() {
		setVisible(true);
	}


}
