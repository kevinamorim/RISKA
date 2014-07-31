package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
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

	private final Color COLOR_LOSE = Color.RED;
	private final Color COLOR_WIN = Color.GREEN;
	
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
		resources = ResourceCache.getSharedInstance();
		cameraManager = resources.camera;
		
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
		
		result1.setPosition(0.25f * MainActivity.CAMERA_WIDTH, 0.3f * MainActivity.CAMERA_HEIGHT);
		result2.setPosition(0.75f * MainActivity.CAMERA_WIDTH, 0.3f * MainActivity.CAMERA_HEIGHT);
		
		attachChild(background);
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
		
		float x1, x2;
		float y1, y2;
		
		Player Player1 = pRegion1.getOwner();
		Player Player2 = pRegion2.getOwner();
		
		x1 = 0.25f * MainActivity.CAMERA_WIDTH;
		x2 = 0.75f * MainActivity.CAMERA_WIDTH;
		
		if(Player1.isCPU() && !Player2.isCPU())
		{
			x1 = 0.75f * MainActivity.CAMERA_WIDTH;
			x2 = 0.25f * MainActivity.CAMERA_WIDTH;
			
			won = !won;
		}
		
		y1 = 0.8f * MainActivity.CAMERA_HEIGHT;
		y2 = 0.8f * MainActivity.CAMERA_HEIGHT;
		
		typePlayer1.setText(Player1.getName());
		typePlayer1.setPosition(x1, y1);
		typePlayer2.setText(Player2.getName());
		typePlayer2.setPosition(x2, y2);
		
		y1 = 0.5f * MainActivity.CAMERA_HEIGHT;
		y2 = 0.5f * MainActivity.CAMERA_HEIGHT;
		
		regionName1.setText(pRegion1.getName());
		regionName1.setPosition(x1, y1);
		regionName2.setText(pRegion2.getName());
		regionName2.setPosition(x2, y2);
		
		if(won)
		{
			result1.setColor(COLOR_WIN);
			result2.setColor(COLOR_LOSE);
		}
		else
		{
			result1.setColor(COLOR_LOSE);
			result2.setColor(Color.GREEN);
		}
	}

}
