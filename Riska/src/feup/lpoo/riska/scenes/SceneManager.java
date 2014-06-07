package feup.lpoo.riska.scenes;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import feup.lpoo.riska.logic.MainActivity;

public class SceneManager {
	
	private SceneType currentScene;
	private MainActivity activity;
	private Engine engine;
	
	private Scene splashScene, mainMenuScene, optionsScene, 
		loadMapScene, gameScene; /* Create more scene if needed, like gameScene. */
	
	public static SceneManager instance;
	
	/* Every time a new scene is created, a new entry is also added. */
	public enum SceneType {
		SPLASH, 
		MENU,
		OPTIONS,
		LOAD_MAP,
		GAME,
		GAME_OVER
	};
	
	public SceneManager(MainActivity activity, Engine engine, Camera camera) {
		this.activity = activity;
		this.engine = engine;
		instance = this;
	}
	
	public static SceneManager getSharedInstance() {
		return instance;
	}

	public Scene createSplashScene() {
		
		splashScene = new SplashScene();
		return splashScene;
		
	}
	
	public void createGameScenes() {
		
		mainMenuScene = new MainMenuScene();
		optionsScene = new OptionsScene();
		//loadMapScene = new LoadMapScene();
		gameScene = new GameScene();
	}
	
	public SceneType getCurrentScene() {
		return currentScene;
	}
	
	public void setCurrentScene(SceneType scene) {
		
		currentScene = scene;
		
		switch(scene) {
		case SPLASH: 
			break;
		case MENU:
			engine.setScene(mainMenuScene);
			break;
		case OPTIONS:
			engine.setScene(optionsScene);
			break;
		case LOAD_MAP:
			engine.setScene(loadMapScene);
			break;
		case GAME:
			engine.setScene(gameScene);
			break;
		default:
				break;
		}
		
	}
	
	public void cleanGame() {
		/*
		 * Do stuff before exiting the game.
		 * p.e. save game state.
		 */
	}
	
	public Scene getGameScene() {
		return gameScene;
	}
}
