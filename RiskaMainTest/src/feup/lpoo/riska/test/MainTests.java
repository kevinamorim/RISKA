package feup.lpoo.riska.test;

import org.andengine.entity.scene.Scene;

import junit.framework.Assert;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;
import feup.lpoo.riska.MainActivity;
import feup.lpoo.riska.SplashScene;

public class MainTests extends ActivityUnitTestCase<MainActivity> {
	
	private final float LOGO_WIDTH = 370;
	
	private MainActivity activity;
	
	public MainTests() {
		super(MainActivity.class);
	}
	
	/*
	 * Initialize activity.
	 */
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent(getInstrumentation().getTargetContext(), 
				MainActivity.class);
		
		startActivity(intent, null, null);
		activity = getActivity();
		activity.onCreateEngineOptions();
	}
	
//	public void testCurrentScene() {
//		Scene scene = new SplashScene();
//		
//		activity.setCurrentScene(scene);
//		Assert.assertTrue(activity.mCurrentScene.equals(scene));
//	}
	
	public void testGraphicsLoad() {
		activity.loadGraphics();
		Assert.assertTrue(activity.logoTexture.getWidth() == LOGO_WIDTH);	
	}
	

}
