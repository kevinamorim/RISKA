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

public class SplashScene extends Scene {
	
	MainActivity activity;
	SceneManager instance;
	ResourceCache resources;
	
	public SplashScene() {
		
		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		
		setBackground(new Background(Color.BLACK));

		Sprite logoSprite;
		
		logoSprite = new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
				resources.getLogo(), activity.getVertexBufferObjectManager());
		
		//logoSprite.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		//logoSprite.registerEntityModifier(new AlphaModifier(6f, 0f, 1f));
		
		Text company = new Text(0, 0, resources.mSplashFont, "HellHounds", activity.getVertexBufferObjectManager());
		company.setPosition(MainActivity.CAMERA_WIDTH/2, 
				MainActivity.CAMERA_HEIGHT * 0.05f );
		company.setAlpha(0.3f);
		
		attachChild(logoSprite);
		attachChild(company);
		
		loadResources();
		
	}

	private void loadResources() {
		
		DelayModifier delayModifier = new DelayModifier(2) {
			
			@Override
			protected void onModifierFinished(IEntity pItem) {
				instance.setCurrentScene(SceneType.MENU);
			}
			
		};
		
		
		registerEntityModifier(delayModifier);

		
	}
	
}
