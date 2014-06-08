package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

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
		
		String resultStr;
		if(won) {
			resultStr = "WIN";
		} else {
			resultStr = "LOSE";
		}
		
		Text resultText = new Text(centerX - this.getWidth()/2, 
				MainActivity.CAMERA_HEIGHT / 3,  resources.getGameFont(),
				resultStr, 4,  activity.getVertexBufferObjectManager());
		
		attachChild(resultText);
		
	}
	
	
}
