package feup.lpoo.riska.logic;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.BaseScene;
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
		/*OPTIONS,*/
		LOAD_MAP,
		GAME,
		NEWGAME,
		LOADGAME,
		GAME_OVER,
		DETAIL,
		PRE_BATTLE,
		BATTLE
	};

	// ==================================================
	// FIELDS
	// ==================================================
	private BaseScene splashScene;
	private BaseScene loadingScene;
	private BaseScene mainMenuScene;
	private BaseScene gameScene;
	//private BaseScene optionsScene;	
	
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
	
//	public void createOptionsScene() {
//		optionsScene = new OptionsScene();
//		setScene(optionsScene);
//	}
	
	// ==================================================
	// LOAD SCENES
	// ==================================================
	public void loadMainMenuScene(final Engine mEngine)
	{
		setScene(loadingScene);
		gameScene.disposeScene();
		ResourceCache.getSharedInstance().unloadGameSceneResources();
		
		mEngine.registerUpdateHandler(new TimerHandler(MIN_LOAD_SECONDS, new ITimerCallback()
		{

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
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
	}
	
	public void setScene(SCENE_TYPE sceneType) {
		switch(sceneType) {
		case SPLASH:
			setScene(splashScene);
			break;
		case MAIN_MENU:
			setScene(mainMenuScene);
			break;
//		case OPTIONS:
//			setScene(optionsScene);
//			break;
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
