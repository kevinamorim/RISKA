package feup.lpoo.riska;
import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.graphics.Typeface;


public class MainActivity extends SimpleBaseGameActivity {
	
	static final int CAMERA_WIDTH = 800;
	static final int CAMERA_HEIGHT = 480;
	
	public Font mFont;
	public Camera mCamera;
	
	public Scene mCurrentScene;
	public static MainActivity instance;

	@Override
	public EngineOptions onCreateEngineOptions() {
		
		instance = this;
		
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
	}

	@Override
	protected void onCreateResources() throws IOException {
		mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 
				256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		mFont.load();
	}

	@Override
	protected Scene onCreateScene() {
		mCurrentScene = new SplashScene();
		return mCurrentScene;
	}
	
	public static MainActivity getSharedInstance() {
		 return instance;
	}
	
	public void setCurrentScene(Scene scene) {
		mCurrentScene = scene;
		getEngine().setScene(mCurrentScene);
	}

}
