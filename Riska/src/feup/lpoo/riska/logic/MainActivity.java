package feup.lpoo.riska.logic;

import java.io.IOException;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.Font;
import org.andengine.ui.activity.BaseGameActivity;

import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.SceneManager;
import feup.lpoo.riska.scenes.SceneManager.SceneType;

public class MainActivity extends BaseGameActivity {	
	
	// ======================================================
	// CONSTANTS
	// ======================================================
	public final static int CAMERA_WIDTH = 800;
	public final static int CAMERA_HEIGHT = 480;
	private final float MAX_VELOCITY = 700f;
	
	public final static float RES_RATIO = 16f/10f;
	
	public Font mFont;
	public SmoothCamera  mCamera;
	
	public Scene mCurrentScene;
	public static MainActivity instance;
	
	protected SceneManager sceneManager;
	protected ResourceCache resources;

	@Override
	public EngineOptions onCreateEngineOptions() {
		
		instance = this;
		
		mCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, MAX_VELOCITY, MAX_VELOCITY, 3.0f);

		mCamera.setBounds(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		mCamera.setBoundsEnabled(true);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
				new RatioResolutionPolicy(RES_RATIO), mCamera);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		
		sceneManager = new SceneManager(this, mEngine, mCamera);
		
		resources = new ResourceCache(this, mEngine, mCamera);	
		resources.loadSplashSceneResources();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
		
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		
		pOnCreateSceneCallback.onCreateSceneFinished(sceneManager.createSplashScene());
		
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
		
		mEngine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
			
			public void onTimePassed(final TimerHandler pTimerHandler) {
				
				mEngine.unregisterUpdateHandler(pTimerHandler);
				
				resources.loadMainMenuResources();
				resources.loadGameSceneResources();
				resources.loadMusic();
				
				sceneManager.createGameScenes();
				sceneManager.setCurrentScene(SceneType.MENU);
				
			}
		}));
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
		
	}
	
	/**
	 * Handles what happens when the back button is pressed.
	 */
	@Override
	public void onBackPressed() {
		this.finish();
	}

	public static MainActivity getSharedInstance() {
		 return instance;
	}
	
}
