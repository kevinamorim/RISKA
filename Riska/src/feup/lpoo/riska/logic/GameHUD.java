package feup.lpoo.riska.logic;

import org.andengine.engine.camera.hud.HUD;

public class GameHUD extends HUD {
	
	MainActivity activity;
	SceneManager instance;
	CameraManager cameraManager;

	public GameHUD() {
		
		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();
		cameraManager = CameraManager.getSharedInstance();
		
		
	}
}
