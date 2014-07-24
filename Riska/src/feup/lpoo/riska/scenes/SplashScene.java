package feup.lpoo.riska.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.SceneManager.SceneType;

public class SplashScene extends BaseScene {
	
	// ==================================================
	// FIELDS
	// ==================================================
	private Sprite splash;
	
	@Override
	public void createScene() {
		
		setBackground(new Background(Color.BLACK));

		Sprite logoSprite;
		
		splash = new Sprite(resources.camera.getWidth()/2, resources.camera.getHeight()/2, 
				resources.splashRegion, resources.vbom);
		
		attachChild(splash);
		
	}

	@Override
	public void onBackKeyPressed() {
		return;
	}

	@Override
	public void disposeScene() {
		splash.detachSelf();
		splash.dispose();
		detachSelf();
		dispose();
	}
	
	@Override
	public SceneType getSceneType() {
		return SceneType.SPLASH;
	}
	
}
