package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.util.adt.color.Color;

import android.util.Log;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.generator.BattleGenerator;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.resources.ResourceCache;

public class BattleScene extends Scene {

	// ======================================================
	// CONSTANTS
	// ======================================================

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
	BattleGenerator battleGenerator;
	Sprite background;
	
	Region attacker, defender;
	boolean won;

	public BattleScene(Region attacker, Region defender, boolean won) {
		
		activity = MainActivity.getSharedInstance();
		sceneManager = SceneManager.getSharedInstance();	
		cameraManager = CameraManager.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		
		this.attacker = attacker;
		this.defender = defender;
		this.won = won;
		
		createDisplay();
		
	}

	private void createDisplay() {
		
		background = new Sprite(
				MainActivity.CAMERA_WIDTH / 2,
				MainActivity.CAMERA_HEIGHT / 2,
				MainActivity.CAMERA_WIDTH,
				MainActivity.CAMERA_HEIGHT,
				resources.getWindowTexture(),
				activity.getVertexBufferObjectManager());
		
		background.setScale(0.9f);
		background.setAlpha(1f);
		attachChild(background);
		
		setBackgroundEnabled(false);
		
		Text vsText = new Text(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
				resources.getGameFont(), "VS", activity.getVertexBufferObjectManager());
		
		vsText.setColor(Color.BLACK);
		vsText.setScale(2.0f);
		
		attachChild(vsText);
	
		int playerNum = (attacker.getOwner().isCPU())? 1 : 0;
		displayRegionInfo(attacker, won, playerNum);
		
		playerNum = (defender.getOwner().isCPU())? 1 : 0;
		displayRegionInfo(defender, !won, playerNum);
	}
	
	/* Assuming only two players for now. PlayerNum = 0 -> Left side, PlayerNum = 1 -> Right side */
	private void displayRegionInfo(Region pRegion, boolean regionWon, int playerNum) {
		
		Color textColor = Color.BLACK;
		
		String typePlayer = ((playerNum == 0)? "- PLAYER -" : "- CPU -");
		String regionName = pRegion.getName();
		String resultStr = (regionWon)? "WON" : "LOSE";
		
		float halfWidth = getWidth()/2f;
		
		float x, y = 0.80f * MainActivity.CAMERA_HEIGHT;
		
		if(playerNum == 0) {
			x = MainActivity.CAMERA_WIDTH * 0.25f;
		} else {
			x = MainActivity.CAMERA_WIDTH * 0.75f;
		}
		
		Text typePlayerText = new Text(x - this.getWidth()/2, y,  resources.getGameFont(),
				typePlayer, 10,  activity.getVertexBufferObjectManager());
		
		typePlayerText.setScale(0.70f);
		
		y = 0.60f * MainActivity.CAMERA_HEIGHT;
		
		Text regionNameText = new Text(x - this.getWidth()/2, y,  resources.getGameFont(),
				wrapText(resources.getGameFont(), regionName, halfWidth), 1000,  activity.getVertexBufferObjectManager());
		
		y = 0.25f * MainActivity.CAMERA_HEIGHT;
		
		Text resultText =  new Text(x - this.getWidth()/2, y,  resources.getGameFont(),
				resultStr, 4,  activity.getVertexBufferObjectManager());
		
		typePlayerText.setColor(textColor);
		regionNameText.setColor(textColor);
		
		ButtonSprite result;
		result = new ButtonSprite(0,0,
				resources.getRegionButtonTexture(),
				activity.getVertexBufferObjectManager());
		
		if(regionWon)
		{
			result.setColor(Color.GREEN);
		} else
		{
			result.setColor(Color.BLACK);
		}
		
		result.setScale(0.7f);
		result.setPosition(x, y);
		attachChild(result);
		
		resultText.setColor(textColor);	
		
		attachChild(typePlayerText);
		attachChild(regionNameText);
		//attachChild(resultText);
	}
	
	private String wrapText(Font pFont, String pString, float maxWidth) {

		Text pText = new Text(0, 0, pFont, pString, 1000, activity.getVertexBufferObjectManager());
		
		Log.d("Riska","Text width: " + pText.getWidth() + " of " + maxWidth);
		
		if(pText.getWidth() < maxWidth) {
			return pString;
		}

		// Split the entire string into separated words.
		String[] words = pText.getText().toString().split(" ");

		String wrappedText = ""; /* Final string. */
		String line = ""; /* Temp variable */

		for(String word : words) {

			pText.setText(line + word);
			
			Log.d("Riska","Text width: " + pText.getWidth() + " of " + maxWidth);
			
			if(pText.getWidth() > maxWidth) {			
				wrappedText += line + "\n\n";
				line = "";
			}

			line += word + " ";

		}

		wrappedText += line;

		return wrappedText;

	}
}
