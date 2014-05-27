package feup.lpoo.riska;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;

import feup.lpoo.riska.SceneManager.SceneType;

public class OptionsScene extends MenuScene implements IOnMenuItemClickListener {
	
	MainActivity activity; 
	SceneManager instance;
	
	final int RETURN = 0;
	
	public OptionsScene() {		
		super(MainActivity.getSharedInstance().mCamera);
		
		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();
		
		SpriteBackground background = new SpriteBackground(new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
				instance.mMenuBackgroundTextureRegion, activity.getVertexBufferObjectManager()));
		
		final AnimatedButtonSpriteMenuItem button_return = new AnimatedButtonSpriteMenuItem(RETURN, (float) 0.3*instance.mReturnButtonTiledTextureRegion.getWidth(), 
				(float) 0.3*instance.mReturnButtonTiledTextureRegion.getHeight(), instance.mReturnButtonTiledTextureRegion, 
				activity.getVertexBufferObjectManager());
		
		button_return.setPosition(button_return.getWidth()/2, button_return.getHeight()/2);
		
		setBackground(background);
		addMenuItem(button_return);
		
		setOnMenuItemClickListener(this);	
		
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		
		switch(pMenuItem.getID()) {
		case RETURN:
			// TODO: Save configurations.
			instance.setCurrentScene(SceneType.MENU);
			break;
		default:
				break;
		}
		return false;
	}

}
