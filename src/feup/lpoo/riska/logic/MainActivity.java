package feup.lpoo.riska.logic;

import java.io.IOException;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.KeyEvent;
import feup.lpoo.riska.gameInterface.CameraManager;
import feup.lpoo.riska.io.IOManager;
import feup.lpoo.riska.resources.ResourceManager;
import feup.lpoo.riska.utilities.Utils;

public class MainActivity extends BaseGameActivity {	

	// ======================================================
	// CONSTANTS
	// ======================================================
	private final static int CAMERA_WIDTH = 960;
	private final static int CAMERA_HEIGHT = 540;
	private final static float MAX_VELOCITY = 700f;

	public final static float RES_RATIO = 16f/9f;
	public final static float RES_RATIO_INVERTED = 1f/RES_RATIO;

	// ======================================================
	// FIELDS
	// ======================================================
	public CameraManager mCamera;
	public static MainActivity instance;

	// ======================================================
	// ======================================================
	@Override
	public EngineOptions onCreateEngineOptions()
	{
		instance = this;

		mCamera = new CameraManager(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, MAX_VELOCITY, MAX_VELOCITY, 3.0f);

		mCamera.setBounds(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		mCamera.setBoundsEnabled(true);
		
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
				new RatioResolutionPolicy(RES_RATIO), mCamera);

		engineOptions.getAudioOptions().setNeedsMusic(true);
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
	{
		ResourceManager.prepareManager(mEngine, this, mCamera, getVertexBufferObjectManager());
		ResourceManager.instance.createResources(Utils.CONTEXT.SPLASH);
        ResourceManager.instance.createResources(Utils.CONTEXT.LOADING);
        ResourceManager.instance.createResources(Utils.CONTEXT.MENU);
        ResourceManager.instance.createResources(Utils.CONTEXT.GAME);

		IOManager.loadGameOptions();

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException
	{
		SceneManager.instance.loadSplashScene(pOnCreateSceneCallback);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
	{
		float seconds = 2.0f;

		mEngine.registerUpdateHandler(new TimerHandler(seconds, new ITimerCallback()
		{
			@Override
			public void onTimePassed(TimerHandler pTimerHandler)
			{
				mEngine.unregisterUpdateHandler(pTimerHandler);
				SceneManager.instance.loadScene(Utils.CONTEXT.MENU);
            }
		}));

		pOnPopulateSceneCallback.onPopulateSceneFinished();	
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                SceneManager.instance.getCurrentScene().onBackKeyPressed();
                break;

            case KeyEvent.KEYCODE_MENU:
                SceneManager.instance.getCurrentScene().onMenuKeyPressed();
                break;

            default:
                break;
        }

		return false;
	}

}
