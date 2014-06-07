package feup.lpoo.riska.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.scenes.SceneManager.SceneType;
import android.opengl.GLES20;

public class SplashScene extends Scene {
	
	MainActivity activity;
	SceneManager instance;
	
	public SplashScene() {
		
		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();
		
		setBackground(new Background(Color.BLACK));

		Sprite logoSprite;
		
		logoSprite = new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
				instance.logoTextureRegion, activity.getVertexBufferObjectManager());
		
		//logoSprite.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		//logoSprite.registerEntityModifier(new AlphaModifier(6f, 0f, 1f));
		
		attachChild(logoSprite);
		
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
