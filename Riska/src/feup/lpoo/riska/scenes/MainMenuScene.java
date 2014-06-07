package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import feup.lpoo.riska.gameInterface.AnimatedButtonSpriteMenuItem;
import feup.lpoo.riska.gameInterface.AnimatedTextButtonSpriteMenuItem;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.music.Conductor;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.SceneManager.SceneType;

public class MainMenuScene extends MenuScene implements IOnMenuItemClickListener {
	
	MainActivity activity; 
	SceneManager sceneManager;
	ResourceCache resources;
	Conductor conductor;
		
	final int MENU_START = 0;
	final int MENU_OPTIONS = 1;
	
	Sprite backgroundSprite;
	
	public MainMenuScene() {
		
		super(MainActivity.getSharedInstance().mCamera);
		
		activity = MainActivity.getSharedInstance();
		sceneManager = SceneManager.getSharedInstance();	
		resources = ResourceCache.getSharedInstance();
		conductor = Conductor.getSharedInstance();
		
		conductor.playBackgroundMusic();
		
		createDisplay();
	}
	

	private void createDisplay() {
		SpriteBackground background = new SpriteBackground(new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
				resources.getMenuBackgroundTexture(), activity.getVertexBufferObjectManager()));
		
		TiledTextureRegion button = resources.getStartButtonTexture();

		final AnimatedTextButtonSpriteMenuItem button_start = new AnimatedTextButtonSpriteMenuItem(MENU_START, button.getWidth(), 
				button.getHeight(), button, activity.getVertexBufferObjectManager(), "START", resources.getFont());
		
		button = resources.getOptionsButtonTexture();
		
		final AnimatedButtonSpriteMenuItem button_options = new AnimatedButtonSpriteMenuItem(MENU_OPTIONS, (float)(0.3*button.getWidth()),
				(float)(0.3*button.getHeight()), button, 
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
			sceneManager.setCurrentScene(SceneType.GAME);
			break;
		case MENU_OPTIONS:
			sceneManager.setCurrentScene(SceneType.OPTIONS);
			break;
		default:
			break;
		}
		return false;
	}
	
	

}
