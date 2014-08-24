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
import feup.lpoo.riska.resources.ResourceCache;

public class MainActivity extends BaseGameActivity {	

	// ======================================================
	// CONSTANTS
	// ======================================================
	private final static int CAMERA_WIDTH = 800;
	private final static int CAMERA_HEIGHT = 500;
	private final float MAX_VELOCITY = 700f;

	public final static float RES_RATIO = 16f/10f;

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
		ResourceCache.prepareManager(mEngine, this, mCamera, getVertexBufferObjectManager());	
		ResourceCache.getSharedInstance().loadSplashScene();
		ResourceCache.getSharedInstance().createMainMenuResources();
		ResourceCache.getSharedInstance().createGameResources();

		IOManager.loadGameOptions();

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException
	{
		SceneManager.getSharedInstance().createSplashScene(pOnCreateSceneCallback);
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
				SceneManager.getSharedInstance().createMainMenuScene();
			}
		}));

		pOnPopulateSceneCallback.onPopulateSceneFinished();	
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			SceneManager.getSharedInstance().getCurrentScene().onBackKeyPressed();
		}
		
		return false;
	}

	public static MainActivity getSharedInstance() { return instance; }

}
