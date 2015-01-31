package feup.lpoo.riska.logic;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;
import org.andengine.util.debug.Debug;

import feup.lpoo.riska.interfaces.Debuggable;
import feup.lpoo.riska.resources.ResourceManager;
import feup.lpoo.riska.scenes.BaseScene;
import feup.lpoo.riska.scenes.game.GameScene;
import feup.lpoo.riska.scenes.loading.LoadingScene;
import feup.lpoo.riska.scenes.loading.SplashScene;
import feup.lpoo.riska.scenes.menu.BaseMenuScene;
import feup.lpoo.riska.utilities.Utils;

public class SceneManager implements Debuggable {

	// ==================================================
	// CONSTANTS
	// ==================================================
	private final float MIN_LOAD_SECONDS = 1f;

	// ==================================================
	// FIELDS
	// ==================================================
    private Engine engine = MainActivity.instance.getEngine();

    private Utils.CONTEXT currentScene = Utils.CONTEXT.SPLASH;

	private BaseScene splashScene;
	private BaseScene loadingScene;
	private BaseScene mainMenuScene;
	private BaseScene gameScene;

    // ==================================================
    // SINGLETON
    // ==================================================
    public static SceneManager instance = new SceneManager();

    private SceneManager() {
        this.splashScene = new SplashScene();
        this.loadingScene = new LoadingScene();
        this.mainMenuScene = new BaseMenuScene();
        this.gameScene = new GameScene();
    }

	// ==================================================
	// CREATE SCENES
	// ==================================================
	public void loadSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		ResourceManager.instance.loadResources(Utils.CONTEXT.SPLASH);
        ResourceManager.instance.loadResources(Utils.CONTEXT.LOADING);

        splashScene = new SplashScene();
        loadingScene = new LoadingScene();
        currentScene = Utils.CONTEXT.SPLASH;

		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}

    public void loadScene(final Utils.CONTEXT context) {

        Utils.CONTEXT current = currentScene;

        setScene(Utils.CONTEXT.LOADING);

        unloadScene(current);

        engine.registerUpdateHandler(new TimerHandler(MIN_LOAD_SECONDS, new ITimerCallback()
        {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler)
            {
                engine.unregisterUpdateHandler(pTimerHandler);
                ResourceManager.instance.loadResources(context);
                getScene(context).createScene();
                setScene(Utils.CONTEXT.GAME);
            }
        }));
    }

	// ==================================================
	// GETTERS & SETTERS
	// ==================================================
	public void setScene(Utils.CONTEXT context) {

		BaseScene scene = getScene(context);

        if(scene != null) {
            engine.setScene(scene);
            currentScene = context;
        }
        else {
            print("setScene() : context '" + context + "' returned no valid scene");
        }
	}

    public BaseScene getScene(Utils.CONTEXT context) {

        switch(context)
        {
            case SPLASH:
                return splashScene;
            case LOADING:
                return loadingScene;
            case MENU:
                return mainMenuScene;
            case GAME:
                return gameScene;

            default:
                return null;
        }
    }

    public void unloadScene(Utils.CONTEXT context) {

        BaseScene scene = getScene(context);

        if(scene != null) {
            ResourceManager.instance.unloadResources(context);
            scene.disposeScene();
        }
    }

    public BaseScene getCurrentScene() {
        return getScene(currentScene);
    }

    @Override
    public void print(String debugInfo) {
        Debug.d("Scenes", "[Class=SceneManager]");
        Debug.d("Scenes", "    " + debugInfo);
    }

}
