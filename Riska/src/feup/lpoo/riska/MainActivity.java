package feup.lpoo.riska;
import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.SceneManager.SceneType;
import android.graphics.Typeface;

public class MainActivity extends BaseGameActivity {	
	
	final static int CAMERA_WIDTH = 854;
	final static int CAMERA_HEIGHT = 480;
	
	public Font mFont;
	public Camera mCamera;
	
	public Scene mCurrentScene;
	public static MainActivity instance;
	
	protected SceneManager sceneManager;

	@Override
	public EngineOptions onCreateEngineOptions() {
		
		instance = this;
		
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
	}
	
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		
		sceneManager = new SceneManager(this, mEngine, mCamera);
		sceneManager.loadSplashSceneResources();
		
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
				
				sceneManager.loadMainMenuResources();
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
		// TODO
	}

	public static MainActivity getSharedInstance() {
		 return instance;
	}
	
}
