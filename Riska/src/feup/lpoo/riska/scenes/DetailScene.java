package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;

import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.resources.ResourceCache;

public class DetailScene extends Scene {

	// ======================================================
	// SINGLETONS
	// ======================================================
	MainActivity activity;
	//SceneManager sceneManager;
	//CameraManager cameraManager;
	ResourceCache resources;

	// ======================================================
	// FIELDS
	// ======================================================
	private Sprite background;

	private Region playerRegion, enemyRegion;

	public DetailScene() {
		
		activity = MainActivity.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		
		this.playerRegion = null;
		this.enemyRegion = null;

		createDisplay();
	}
	
	private void createDisplay() {
		background = new Sprite(MainActivity.CAMERA_WIDTH / 2,
				MainActivity.CAMERA_HEIGHT / 2,
				MainActivity.CAMERA_WIDTH,
				MainActivity.CAMERA_HEIGHT,
				resources.getMenuBackgroundTexture(),
				activity.getVertexBufferObjectManager());
		
		background.setScale(0.9f);
		
		setBackgroundEnabled(false);
		attachChild(background);
		
		setVisible(false);
	}
	
	public void setAttributes(Region playerRegion, Region enemyRegion) {
		this.playerRegion = playerRegion;
		this.enemyRegion = enemyRegion;
	}
}
