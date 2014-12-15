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
	private Engine engine = ResourceCache.instance.engine;
	
	private SCENE_TYPE currentSceneType = SCENE_TYPE.SPLASH;


	// ==================================================
	// CREATE SCENES
	// ==================================================
	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback)
	{
		splashScene = new SplashScene();
		ResourceCache.instance.loadSplashSceneResources();
		ResourceCache.instance.loadLoadingSceneResources();
		loadingScene = new LoadingScene();
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}
	
	public void disposeSplashScene()
	{
		ResourceCache.instance.unloadSplashSceneResources();
		splashScene.dispose();
		splashScene = null;
	}
	
	public void createMainMenuScene()
	{
		ResourceCache.instance.loadMainMenuResources();
		mainMenuScene = new MainMenuScene();
		setScene(mainMenuScene);
		disposeSplashScene();
	}
	
	public void createGameScene()
	{
		setScene(loadingScene);
		ResourceCache.instance.unloadMainMenuResources();
		mainMenuScene.disposeScene();
		mainMenuScene = null;
		engine.registerUpdateHandler(new TimerHandler(MIN_LOAD_SECONDS, new ITimerCallback()
		{
			@Override
			public void onTimePassed(TimerHandler pTimerHandler)
			{
				engine.unregisterUpdateHandler(pTimerHandler);
				ResourceCache.instance.loadGameSceneResources();
				gameScene = new GameScene();
				setScene(gameScene);
			}
		}));
	}
	
	public void createGameOverScene()
	{
		setScene(loadingScene);
		ResourceCache.instance.unloadGameSceneResources();
		gameScene.dispose();
		gameScene = null;
		engine.registerUpdateHandler(new TimerHandler(MIN_LOAD_SECONDS, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				engine.unregisterUpdateHandler(pTimerHandler);
				ResourceCache.instance.loadGameOverSceneResources();
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
			ResourceCache.instance.unloadGameSceneResources();
			break;
			
		case GAMEOVER:
			gameOverScene.disposeScene();
			ResourceCache.instance.unloadGameOverSceneResources();
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
				ResourceCache.instance.loadMainMenuResources();
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
	
	public void setScene(SCENE_TYPE sceneType)
	{
		switch(sceneType)
		{
		
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
	
	public GameScene getGameScene()
	{
		return ((GameScene) gameScene);
	}

}
