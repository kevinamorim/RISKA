package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.logic.SceneManager.SCENE_TYPE;

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
	public SCENE_TYPE getSceneType()
	{
		return SCENE_TYPE.SPLASH;
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
				resources.splashRegion,
				vbom);
		
		splash.setPosition(camera.getCenterX(), camera.getCenterY());
		
		attachChild(splash);
	}

	@Override
	public void onSceneShow() {
		// TODO Auto-generated method stub
		
	}
	
}
