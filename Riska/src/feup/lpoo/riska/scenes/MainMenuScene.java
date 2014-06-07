package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.sprite.Sprite;

import feup.lpoo.riska.gameInterface.AnimatedButtonSpriteMenuItem;
import feup.lpoo.riska.gameInterface.AnimatedTextButtonSpriteMenuItem;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.scenes.SceneManager.SceneType;

public class MainMenuScene extends MenuScene implements IOnMenuItemClickListener {
	
	MainActivity activity; 
	SceneManager instance;
		
	final int MENU_START = 0;
	final int MENU_OPTIONS = 1;
	
	Sprite backgroundSprite;
	
	public MainMenuScene() {
		
		super(MainActivity.getSharedInstance().mCamera);
		
		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();	
		
		SpriteBackground background = new SpriteBackground(new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
				instance.mMenuBackgroundTextureRegion, activity.getVertexBufferObjectManager()));

		final AnimatedTextButtonSpriteMenuItem button_start = new AnimatedTextButtonSpriteMenuItem(MENU_START, instance.mStartButtonTiledTextureRegion.getWidth(), 
				instance.mStartButtonTiledTextureRegion.getHeight(), instance.mStartButtonTiledTextureRegion, 
				activity.getVertexBufferObjectManager(), "START", instance.mFont);
		
		final AnimatedButtonSpriteMenuItem button_options = new AnimatedButtonSpriteMenuItem(MENU_OPTIONS, (float)(0.3*instance.mOptionsButtonTiledTextureRegion.getWidth()),
				(float)(0.3*instance.mOptionsButtonTiledTextureRegion.getHeight()), instance.mOptionsButtonTiledTextureRegion, 
				activity.getVertexBufferObjectManager());
		
		final int centerX = MainActivity.CAMERA_WIDTH/2, centerY = MainActivity.CAMERA_HEIGHT/2;
		
		button_start.setPosition(centerX, centerY);
		
		button_options.setPosition(button_options.getWidth()/2, button_options.getHeight()/2);
		
		setBackground(background);
		addMenuItem(button_start);
		addMenuItem(button_options);
		
		setOnMenuItemClickListener(this);
	
	}
	

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch(pMenuItem.getID()) {
		case MENU_START:
			instance.setCurrentScene(SceneType.GAME);
			break;
		case MENU_OPTIONS:
			instance.setCurrentScene(SceneType.OPTIONS);
			break;
		default:
			break;
		}
		return false;
	}
	
	

}
