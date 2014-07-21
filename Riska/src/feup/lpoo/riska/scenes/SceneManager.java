package feup.lpoo.riska.scenes;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;

import feup.lpoo.riska.logic.MainActivity;

public class SceneManager {
	
	private SceneType currentScene;
	private Engine engine;
	
	private Scene splashScene, mainMenuScene, startGameScene, optionsScene, 
		loadMapScene, gameScene; /* Create more scene if needed, like gameScene. */
	
	public static SceneManager instance;
	
	/* Every time a new scene is created, a new entry is also added. */
	public enum SceneType {
		SPLASH, 
		MENU,
		STARTGAME,
		OPTIONS,
		LOAD_MAP,
		GAME,
		NEWGAME,
		LOADGAME,
		GAME_OVER,
	};
	
	public SceneManager(MainActivity activity, Engine engine, Camera camera) {
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
		startGameScene = new StartGameScene();
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
		case STARTGAME:
			engine.setScene(startGameScene);
			break;
		case OPTIONS:
			engine.setScene(optionsScene);
			break;
		case LOAD_MAP:
			engine.setScene(loadMapScene);
			break;
		case NEWGAME:
			gameScene = new GameScene();
			((GameScene)gameScene).hud.setVisible(true);
			((GameScene)gameScene).showInitialHUD();
			engine.setScene(gameScene);
			currentScene = SceneType.GAME;
			break;
		case LOADGAME:
			if(gameScene == null)
			{
				gameScene = new GameScene();
			}
			((GameScene)gameScene).hud.setVisible(true);
			((GameScene)gameScene).loadGame();
			engine.setScene(gameScene);
			currentScene = SceneType.GAME;
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
