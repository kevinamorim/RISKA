package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.elements.Player;
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
	
	Region region1, region2;
	boolean won;

	public BattleScene(Region region1, Region region2, boolean won) {
		
		activity = MainActivity.getSharedInstance();
		sceneManager = SceneManager.getSharedInstance();	
		cameraManager = CameraManager.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		
		this.region1 = region1;
		this.region2 = region2;
		this.won = won;
		
		createDisplay();
		
	}

	private void createDisplay() {
		
		float centerX = MainActivity.CAMERA_WIDTH / 2;
		
		background = new Sprite(centerX,
				MainActivity.CAMERA_HEIGHT / 2,
				MainActivity.CAMERA_WIDTH,
				MainActivity.CAMERA_HEIGHT,
				resources.getMenuBackgroundTexture(),
				activity.getVertexBufferObjectManager());
		
		background.setScale(0.9f);
		
		setBackgroundEnabled(false);
		attachChild(background);
	
		int playerNum = (region1.getOwner().isCPU())? 1 : 0;
		displayRegionInfo(region1, won, playerNum);
		playerNum = (region2.getOwner().isCPU())? 1 : 0;
		displayRegionInfo(region2, !won, playerNum);
		
	}
	
	/* Assuming only two players for now. PlayerNum = 0 -> Left side, PlayerNum = 1 -> Right side */
	private void displayRegionInfo(Region pRegion, boolean regionWon, int playerNum) {
		
		String typePlayer = ((playerNum == 0)? "(PLAYER)" : "(CPU)");
		String regionName = pRegion.getName();
		String resultStr = (regionWon)? "WON" : "LOSE";
		
		float x, y = 0.9f * MainActivity.CAMERA_HEIGHT;
		if(playerNum == 0) {
			x = MainActivity.CAMERA_WIDTH / 4;
		} else {
			x = MainActivity.CAMERA_WIDTH * 0.75f;
		}
		
		Text typePlayerText = new Text(x - this.getWidth()/2, y,  resources.getGameFont(),
				typePlayer, 10,  activity.getVertexBufferObjectManager());
		typePlayerText.setScale(0.5f);
		
		y = 0.7f * MainActivity.CAMERA_HEIGHT;
		
		Text regionNameText = new Text(x - this.getWidth()/2, y,  resources.getGameFont(),
				wrapText(resources.getGameFont(), regionName, this.getWidth()/2), 1000,  activity.getVertexBufferObjectManager());
		
		y = 0.3f * MainActivity.CAMERA_HEIGHT;
		
		Text resultText =  new Text(x - this.getWidth()/2, y,  resources.getGameFont(),
				resultStr, 4,  activity.getVertexBufferObjectManager());
		
		Color textColor;
		if(regionWon) {
			textColor = Color.GREEN;
		} else {
			textColor = Color.RED;

		}
		
		typePlayerText.setColor(textColor);
		regionNameText.setColor(textColor);
		resultText.setColor(textColor);	
		
		attachChild(typePlayerText);
		attachChild(regionNameText);
		attachChild(resultText);
		
		
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
	
	
}
