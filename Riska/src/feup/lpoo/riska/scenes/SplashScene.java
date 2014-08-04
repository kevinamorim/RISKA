package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.logic.SceneManager.SceneType;

public class SplashScene extends BaseScene implements Displayable {
	
	// ==================================================
	// FIELDS
	// ==================================================
	private Sprite splash;
	
	
	// ==================================================
	// ==================================================
	@Override
	public void createScene()
	{
		createDisplay();
	}

	@Override
	public void onBackKeyPressed()
	{
		return;
	}

	@Override
	public void disposeScene()
	{
		splash.detachSelf();
		splash.dispose();
		detachSelf();
		dispose();
	}
	
	@Override
	public SceneType getSceneType()
	{
		return SceneType.SPLASH;
	}

	// ==================================================
	// ==================================================
	@Override
	public void createDisplay()
	{
		createBackground();
		createSplashLogo();
	}

	private void createBackground()
	{
		setBackground(new Background(Color.BLACK));
	}
	
	private void createSplashLogo()
	{
		splash = new Sprite(
				0.5f * camera.getWidth(),
				0.5f * camera.getHeight(), 
				resources.splashRegion,
				vbom);
		
		attachChild(splash);
	}
	
}
