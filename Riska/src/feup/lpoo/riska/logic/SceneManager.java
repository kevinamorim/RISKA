package feup.lpoo.riska.logic;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.BaseScene;
import feup.lpoo.riska.scenes.GameOverScene;
import feup.lpoo.riska.scenes.GameScene;
import feup.lpoo.riska.scenes.LoadingScene;
import feup.lpoo.riska.scenes.MainMenuScene;
import feup.lpoo.riska.scenes.SplashScene;

public class SceneManager {
	
	// ==================================================
	// CONSTANTS
	// ==================================================
	private final float MIN_LOAD_SECONDS = 1f;
	
	
	public enum SCENE_TYPE {
		SPLASH, 
		LOADING,
		MAIN_MENU,
		LOAD_MAP,
		GAME,
		NEWGAME,
		LOADGAME,
		PRE_BATTLE,
		BATTLE,
		PRE_MOVE,
		GAMEOVER
	};

	// ==================================================
	// FIELDS
	// ==================================================
	private BaseScene splashScene;
	private BaseScene loadingScene;
	private BaseScene mainMenuScene;
	private BaseScene gameScene;
	private BaseScene gameOverScene;
	
	public static SceneManager instance = new SceneManager();
	private BaseScene currentScene;
	private Engine engine = ResourceCache.getSharedInstance().engine;
	
	private SCENE_TYPE currentSceneType = SCENE_TYPE.SPLASH;


	// ==================================================
	// CREATE SCENES
	// ==================================================
	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback)
	{
		
		ResourceCache.getSharedInstance().loadSplashSceneResources();
		splashScene = new SplashScene();
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
		
	}
	
	public void disposeSplashScene()
	{
		ResourceCache.getSharedInstance().unloadSplashSceneResources();
		splashScene.dispose();
		splashScene = null;
	}
	
	public void createMainMenuScene()
	{
		ResourceCache.getSharedInstance().loadMainMenuResources();
		mainMenuScene = new MainMenuScene();
		loadingScene = new LoadingScene();
		setScene(mainMenuScene);
		disposeSplashScene();
	}
	
	public void createGameScene()
	{
		setScene(loadingScene);
		ResourceCache.getSharedInstance().unloadMainMenuResources();
		mainMenuScene.dispose();
		mainMenuScene = null;
		engine.registerUpdateHandler(new TimerHandler(MIN_LOAD_SECONDS, new ITimerCallback()
		{
			@Override
			public void onTimePassed(TimerHandler pTimerHandler)
			{
				engine.unregisterUpdateHandler(pTimerHandler);
				ResourceCache.getSharedInstance().loadGameSceneResources();
				gameScene = new GameScene();
				setScene(gameScene);
			}
		}));
	}
	
	public void createGameOverScene() {
		setScene(loadingScene);
		ResourceCache.getSharedInstance().unloadGameSceneResources();
		gameScene.dispose();
		gameScene = null;
		engine.registerUpdateHandler(new TimerHandler(MIN_LOAD_SECONDS, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				engine.unregisterUpdateHandler(pTimerHandler);
				ResourceCache.getSharedInstance().loadGameOverSceneResources();
				gameOverScene = new GameOverScene();
				setScene(gameOverScene);
			}
			
		}));
	}
	
	// ==================================================
	// LOAD SCENES
	// ==================================================
	public void loadMainMenuScene(final Engine mEngine)
	{
		
		SCENE_TYPE type = currentSceneType;
		
		setScene(loadingScene);

		switch(type) {
		case GAME:
			gameScene.disposeScene();
			ResourceCache.getSharedInstance().unloadGameSceneResources();
			break;
		case GAMEOVER:
			gameOverScene.disposeScene();
			ResourceCache.getSharedInstance().unloadGameOverSceneResources();
			break;
		default:
			/* Not an handled scene. */
			return;
		}

		mEngine.registerUpdateHandler(new TimerHandler(MIN_LOAD_SECONDS, new ITimerCallback()
		{

			@Override
			public void onTimePassed(TimerHandler pTimerHandler)
			{
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceCache.getSharedInstance().loadMainMenuResources();
				mainMenuScene = new MainMenuScene();
				setScene(mainMenuScene);
			}
		
		}));
	}
	
	// ==================================================
	// GETTERS & SETTERS
	// ==================================================
	public SCENE_TYPE getCurrentSceneType()
	{
		return currentSceneType;
	}
	
	public BaseScene getCurrentScene()
	{
		return currentScene;
	}
	
	public void setScene(BaseScene scene)
	{
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
		scene.onSceneShow();
	}
	
	public void setScene(SCENE_TYPE sceneType) {
		switch(sceneType) {
		case SPLASH:
			setScene(splashScene);
			break;
		case MAIN_MENU:
			setScene(mainMenuScene);
			break;
		case GAMEOVER:
			setScene(gameOverScene);
			break;
		case GAME:
			setScene(gameScene);
			break;
		case LOADING:
			setScene(loadingScene);
			break;
		default:
				break;
		}
	}
	
	public static SceneManager getSharedInstance() {
		return instance;
	}
	
	public GameScene getGameScene() {
		return ((GameScene) gameScene);
	}

}
