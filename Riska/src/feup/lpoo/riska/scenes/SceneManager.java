package feup.lpoo.riska.scenes;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;

import feup.lpoo.riska.logic.MainActivity;

public class SceneManager {
	
	private SceneType currentScene;
	private Engine engine;
	
	private boolean firstTime;
	
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
		GAME_OVER,
	};
	
	public SceneManager(MainActivity activity, Engine engine, Camera camera) {
		this.engine = engine;
		instance = this;
		
		firstTime = true;
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
		//gameScene = new GameScene();
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
			
			if(gameScene != null) {
				
				if(((GameScene) gameScene).hud.isVisible()) {
					((GameScene) gameScene).hud.setVisible(false);
				} 

			}

			engine.setScene(mainMenuScene);
			break;
		case OPTIONS:
			engine.setScene(optionsScene);
			break;
		case LOAD_MAP:
			engine.setScene(loadMapScene);
			break;
		case GAME:
			
			if(firstTime) {
				gameScene = new GameScene();
				firstTime = false;
			}
			
			if(!((GameScene) gameScene).hud.isVisible()) {
				((GameScene) gameScene).hud.setVisible(true);
			} 
			
			((GameScene)gameScene).showInitialHUD();
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
	
	public GameScene getGameScene() {
		return ((GameScene) gameScene);
	}
	
}
