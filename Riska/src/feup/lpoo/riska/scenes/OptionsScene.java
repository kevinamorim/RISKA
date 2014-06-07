package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.graphics.Color;
import feup.lpoo.riska.gameInterface.AnimatedButtonSpriteMenuItem;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.SceneManager.SceneType;

public class OptionsScene extends MenuScene implements IOnMenuItemClickListener {
	
	MainActivity activity; 
	SceneManager sceneManager;
	ResourceCache resources;
	
	final int RETURN = 0;
	final int SFX = 1;
	final int MUSIC = 2;
	
	public OptionsScene() {		
		super(MainActivity.getSharedInstance().mCamera);
		
		activity = MainActivity.getSharedInstance();
		sceneManager = SceneManager.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		
		createDisplay();	
	}

	private void createDisplay() {
		SpriteBackground background = new SpriteBackground(new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
				resources.getMenuBackgroundTexture(), activity.getVertexBufferObjectManager()));
		
		TiledTextureRegion button = resources.getReturnButtonTexture();
		
		final AnimatedButtonSpriteMenuItem button_return = new AnimatedButtonSpriteMenuItem(RETURN, (float) 0.3*button.getWidth(), 
				(float) 0.3*button.getHeight(), button, 
				activity.getVertexBufferObjectManager());
		
		button = resources.getSliderButtonTexture();
		
		final AnimatedButtonSpriteMenuItem button_slider_sfx = new AnimatedButtonSpriteMenuItem(SFX, (float) 0.3*button.getWidth(),
				(float) 0.3*button.getHeight(), button, activity.getVertexBufferObjectManager());
		
		final AnimatedButtonSpriteMenuItem button_slider_music = new AnimatedButtonSpriteMenuItem(MUSIC, (float) 0.3*button.getWidth(),
				(float) 0.3*button.getHeight(), button, activity.getVertexBufferObjectManager());
		
		
		
		final Text music_text = new Text(0, 0, resources.getFont(), "MUSIC", activity.getVertexBufferObjectManager());
		music_text.setPosition(MainActivity.CAMERA_WIDTH/2 - (float) 0.75*music_text.getWidth(), 
				(float) 0.5*MainActivity.CAMERA_HEIGHT);
		music_text.setColor(Color.BLACK);
		
		final Text sfx_text = new Text(0, 0, resources.getFont(), "SFX", activity.getVertexBufferObjectManager());
		sfx_text.setPosition(MainActivity.CAMERA_WIDTH/2 - (float) 0.75*sfx_text.getWidth(),
				(float) 0.75*MainActivity.CAMERA_HEIGHT);
		sfx_text.setColor(Color.BLACK);
	
		
		button_return.setPosition(button_return.getWidth()/2, button_return.getHeight()/2);
		
		button_slider_sfx.setPosition(MainActivity.CAMERA_WIDTH/2 + button_slider_sfx.getWidth()/2, 
				(float) 0.75*MainActivity.CAMERA_HEIGHT);
		button_slider_music.setPosition(MainActivity.CAMERA_WIDTH/2 + button_slider_music.getWidth()/2, 
				(float) 0.5*MainActivity.CAMERA_HEIGHT);
		
		setBackground(background);
		
		addMenuItem(button_return);
		addMenuItem(button_slider_sfx);
		addMenuItem(button_slider_music);
		
		attachChild(music_text);
		attachChild(sfx_text);
		
		setOnMenuItemClickListener(this);	
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		
		switch(pMenuItem.getID()) {
		case RETURN:
			// TODO: Save configurations.
			sceneManager.setCurrentScene(SceneType.MENU);
			break;
		case SFX:
			break;
		case MUSIC:
			break;
		default:
				break;
		}
		return false;
	}

}