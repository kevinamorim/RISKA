package feup.lpoo.riska.scenes.loading;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.scenes.BaseScene;
import feup.lpoo.riska.utilities.Utils;

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
	public void onBackKeyPressed() { }
	
	@Override
	public void onMenuKeyPressed() { }

	@Override
	public void disposeScene()
	{
		splash.detachSelf();
		splash.dispose();
		detachSelf();
		dispose();
	}
	
	@Override
	public Utils.CONTEXT getSceneType()
	{
		return Utils.CONTEXT.SPLASH;
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
		splash = new Sprite(0,0, 
				resources.splashLogo,
				vbom);
		
		splash.setPosition(camera.getCenterX(), camera.getCenterY());
		
		attachChild(splash);
	}
}
