package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

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
	Text vsText;
	Text typePlayer1, typePlayer2;
	Text regionName1, regionName2;
	Sprite result1, result2;

	boolean attackerWon;

	public BattleScene() {
		
		activity = MainActivity.getSharedInstance();
		sceneManager = SceneManager.getSharedInstance();	
		cameraManager = CameraManager.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		
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
		
		typePlayer1 = new Text(0,0,resources.mGameFont,"",10,resources.vbom);
		typePlayer2 = new Text(0,0,resources.mGameFont,"",10,resources.vbom);
		
		regionName1 = new Text(0,0,resources.mGameFont,"",1000,resources.vbom);
		regionName2 = new Text(0,0,resources.mGameFont,"",1000,resources.vbom);
		
		result1 = new Sprite(0,0,resources.regionBtnRegion, resources.vbom);
		result2 = new Sprite(0,0,resources.regionBtnRegion, resources.vbom);
		
		attachChild(result1);
		attachChild(result2);
		attachChild(typePlayer1);
		attachChild(typePlayer2);
		attachChild(regionName1);
		attachChild(regionName2);
		attachChild(vsText);
	}
	
	public void setAttributes(Region pRegion1, Region pRegion2, boolean won)
	{
		
		Boolean Player1 = pRegion1.getOwner().isCPU();
		Boolean Player2 = pRegion2.getOwner().isCPU();
		
		typePlayer1.setText(Player1 ? "" : "- PLAYER -");
		typePlayer2.setText(Player2 ? "" : "- PlAYER -");
		regionName1.setText(pRegion1.getName());
		regionName2.setText(pRegion2.getName());
		
		if(won)
		{
			result1.setColor(Color.GREEN);
			result2.setColor(Color.BLACK);
		}
		else
		{
			result1.setColor(Color.BLACK);
			result2.setColor(Color.GREEN);
		}
	}
}
