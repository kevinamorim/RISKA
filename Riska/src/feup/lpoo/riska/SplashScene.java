package feup.lpoo.riska;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.util.modifier.IModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;
import android.opengl.GLES20;

public class SplashScene extends Scene {
	
	MainActivity activity;
	
	Text title, description;
	
	Sprite logoSprite;
	
	public SplashScene() {
		
		setBackground(new Background(Color.BLACK));
		activity = MainActivity.getSharedInstance();
	
		logoSprite = new Sprite(activity.CAMERA_WIDTH/2, activity.CAMERA_HEIGHT/2, 
				activity.logoTextureRegion, activity.getVertexBufferObjectManager());
		
		logoSprite.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		logoSprite.registerEntityModifier(new AlphaModifier(6f, 0f, 1f));
		
		attachChild(logoSprite);
		
		loadResources();
	}

	private void loadResources() {
		DelayModifier dMod = new DelayModifier(2, 
				new IEntityModifierListener() {
		
					@Override 
					public void onModifierStarted(IModifier<IEntity> arg0, IEntity arg1) {
						// FUCK
					}
		
					@Override
					public void onModifierFinished(IModifier<IEntity> arg0, IEntity arg1) {
						activity.setCurrentScene(new MainMenuScene());
					}

				});
		
		
		registerEntityModifier(dMod);

		
	}
	
}
