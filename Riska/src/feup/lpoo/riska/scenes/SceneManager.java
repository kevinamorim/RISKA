package feup.lpoo.riska.scenes;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.resources.ResourceCache;

public class SceneManager {
	
	// ==================================================
	// SCENES
	// ==================================================
	private BaseScene splashScene;
	private BaseScene loadingScene;
	private BaseScene mainMenuScene;
	
	private Scene startGameScene, optionsScene, 
		loadMapScene, gameScene; /* Create more scene if needed, like gameScene. */
	
	// ==================================================
	// FIELDS
	// ==================================================
	public static SceneManager instance = new SceneManager();
	private BaseScene currentScene;
	private SceneType currentSceneType = SceneType.SPLASH;
	private Engine engine = ResourceCache.getSharedInstance().engine;
	
	public enum SceneType {
		SPLASH, 
		LOADING,
		MAIN_MENU,
		STARTGAME,
		OPTIONS,
		LOAD_MAP,
		GAME,
		NEWGAME,
		LOADGAME,
		GAME_OVER,
	};


	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		
		ResourceCache.getSharedInstance().loadSplashSceneResources();
		splashScene = new SplashScene();
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
		
	}
	
	public void disposeSplashScene() {
		ResourceCache.getSharedInstance().unloadSplashSceneResources();
		splashScene.dispose();
		splashScene = null;
	}
	
	public void createMainMenuScene() {
		ResourceCache.getSharedInstance().loadMainMenuResources();
		mainMenuScene = new MainMenuScene();
		loadingScene = new LoadingScene();
		setScene(mainMenuScene);
		disposeSplashScene();
	}
	
	
	
	public void createGameScenes() {
		
		mainMenuScene = new MainMenuScene();
		//startGameScene = new StartGameScene();
		//optionsScene = new OptionsScene();
		//loadMapScene = new LoadMapScene();
		//gameScene = new GameScene();
	}
	

	
	public void setCurrentScene(SceneType scene) {
		
		currentSceneType = scene;
		
		switch(scene) {
		case SPLASH: 
			break;
		case MAIN_MENU:
			
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
			currentSceneType = SceneType.GAME;
			break;
		case LOADGAME:
			if(gameScene == null)
			{
				gameScene = new GameScene();
			}
			((GameScene)gameScene).hud.setVisible(true);
			((GameScene)gameScene).loadGame();
			engine.setScene(gameScene);
			currentSceneType = SceneType.GAME;
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
	
	
	// ==================================================
	// GETTERS & SETTERS
	// ==================================================
	public SceneType getCurrentSceneType() {
		return currentSceneType;
	}
	
	public BaseScene getCurrentScene() {
		return currentScene;
	}
	
	public void setScene(BaseScene scene) {
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}
	
	public static SceneManager getSharedInstance() {
		return instance;
	}
	
}
