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
import feup.lpoo.riska.utilities.Utilities;

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
	Text vsText;
	Text typePlayer1, typePlayer2;
	
	Region attacker, defender;
	boolean attackerWon;

	public BattleScene(Region attacker, Region defender, boolean won) {
		
		activity = MainActivity.getSharedInstance();
		sceneManager = SceneManager.getSharedInstance();	
		cameraManager = CameraManager.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		
		this.attacker = attacker;
		this.defender = defender;
		this.attackerWon = won;
		
		setBackgroundEnabled(false);
		
		createDisplay();
		
	}

	private void createDisplay()
	{
		background = new Sprite(
				MainActivity.CAMERA_WIDTH / 2,
				MainActivity.CAMERA_HEIGHT / 2,
				MainActivity.CAMERA_WIDTH,
				MainActivity.CAMERA_HEIGHT,
				resources.windowRegion,
				activity.getVertexBufferObjectManager());
		
		background.setScale(0.9f);
		background.setAlpha(1f);
		attachChild(background);
		
		vsText = new Text(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
				resources.mGameFont, "VS", activity.getVertexBufferObjectManager());
		
		vsText.setColor(Color.BLACK);
		vsText.setScale(2.0f);
		
		attachChild(vsText);
	}
	
	public void setAttributes(Region attacker, Region defender, boolean attackerWon)
	{
		this.attacker = attacker;
		this.defender = defender;
		this.attackerWon = attackerWon;
	}
	
	private void displayRegionInfo(Region pRegion, boolean regionWon, int playerNum) {
		
		Color textColor = Color.BLACK;
		
		String typePlayer = ((playerNum == 0)? "- PLAYER -" : "- CPU -");
		String regionName = pRegion.getName();
		
		float halfWidth = getWidth()/2f;
		
		float x, y = 0.80f * MainActivity.CAMERA_HEIGHT;
		
		if(playerNum == 0)
		{
			x = MainActivity.CAMERA_WIDTH * 0.25f;
		} else {
			x = MainActivity.CAMERA_WIDTH * 0.75f;
		}
		
		typePlayerText = new Text(x - this.getWidth()/2, y,  resources.mGameFont, typePlayer, 10,  resources.vbom);
		
		typePlayerText.setScale(0.70f);
		
		y = 0.60f * MainActivity.CAMERA_HEIGHT;
		
		Text regionNameText = new Text(x - this.getWidth()/2, y,  resources.mGameFont,
				Utilities.wrapText(resources.mGameFont, regionName, halfWidth), 1000,  resources.vbom);
		
		y = 0.25f * MainActivity.CAMERA_HEIGHT;
		
		typePlayerText.setColor(textColor);
		regionNameText.setColor(textColor);
		
		ButtonSprite result;
		result = new ButtonSprite(0,0, resources.regionBtnRegion, resources.vbom);
		
		if(regionWon)
		{
			result.setColor(Color.GREEN);
		}
		else
		{
			result.setColor(Color.BLACK);
		}
		
		result.setScale(0.7f);
		result.setPosition(x, y);
		attachChild(result);
		
		attachChild(typePlayerText);
		attachChild(regionNameText);
	}
}
