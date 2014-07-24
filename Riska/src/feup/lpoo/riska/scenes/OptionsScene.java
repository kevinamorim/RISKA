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
import feup.lpoo.riska.io.SharedPreferencesManager;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.scenes.SceneManager.SceneType;

public class OptionsScene extends BaseScene implements IOnMenuItemClickListener {
	
	// ==================================================
	// FIELDS
	// ==================================================
	private MenuScene optionsMenuChildScene;
	private final int MENU_RETURN = 0;
	private final int MENU_SFX = 1;
	private final int MENU_MUSIC = 2;
	
	@Override
	public void createScene() {
		createDisplay();
		createOptionsMenuChildScene();
		//SharedPreferencesManager.LoadBoolean("musicOn");
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getSharedInstance().setScene(SceneType.MAIN_MENU);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.OPTIONS;
	}

	@Override
	public void disposeScene() {
		optionsMenuChildScene.detachSelf();
		optionsMenuChildScene.dispose();
		detachSelf();
		dispose();
	}
	
	private void createOptionsMenuChildScene() {
		
		optionsMenuChildScene = new MenuScene(camera);
		
		
		TiledTextureRegion button = resources.returnBtnRegion;
		
		final AnimatedButtonSpriteMenuItem button_return = new AnimatedButtonSpriteMenuItem(MENU_RETURN, (float) 0.3*button.getWidth(), 
				(float) 0.3*button.getHeight(), button, 
				activity.getVertexBufferObjectManager());
		
		button = resources.sliderBtnRegion;
		
		AnimatedButtonSpriteMenuItem button_slider_sfx = new AnimatedButtonSpriteMenuItem(MENU_SFX, 
				(float) 0.3*button.getWidth(),
				(float) 0.3*button.getHeight(), button, 
				activity.getVertexBufferObjectManager());
		
		AnimatedButtonSpriteMenuItem button_slider_music = new AnimatedButtonSpriteMenuItem(MENU_MUSIC, 
				(float) 0.3*button.getWidth(),
				(float) 0.3*button.getHeight(), button, 
				activity.getVertexBufferObjectManager());
		
		
		
		final Text music_text = new Text(0, 0, resources.mainMenuFont, "MUSIC", activity.getVertexBufferObjectManager());
		music_text.setPosition(MainActivity.CAMERA_WIDTH/2 - (float) 0.75*music_text.getWidth(), 
				(float) 0.5*MainActivity.CAMERA_HEIGHT);
		music_text.setColor(Color.BLACK);
		
		final Text sfx_text = new Text(0, 0, resources.mainMenuFont, "SFX", activity.getVertexBufferObjectManager());
		sfx_text.setPosition(MainActivity.CAMERA_WIDTH/2 - (float) 0.75*sfx_text.getWidth(),
				(float) 0.75*MainActivity.CAMERA_HEIGHT);
		sfx_text.setColor(Color.BLACK);
	
		
		button_return.setPosition(button_return.getWidth()/2, button_return.getHeight()/2);
		
		button_slider_sfx.setPosition(MainActivity.CAMERA_WIDTH/2 + button_slider_sfx.getWidth()/2, 
				(float) 0.75*MainActivity.CAMERA_HEIGHT);
		button_slider_music.setPosition(MainActivity.CAMERA_WIDTH/2 + button_slider_music.getWidth()/2, 
				(float) 0.5*MainActivity.CAMERA_HEIGHT);
		
		
		
		optionsMenuChildScene.addMenuItem(button_return);
		optionsMenuChildScene.addMenuItem(button_slider_sfx);
		optionsMenuChildScene.addMenuItem(button_slider_music);
		
		optionsMenuChildScene.attachChild(music_text);
		optionsMenuChildScene.attachChild(sfx_text);
		
		optionsMenuChildScene.setOnMenuItemClickListener(this);	
		optionsMenuChildScene.setBackgroundEnabled(false);
		
		setChildScene(optionsMenuChildScene);
		
	}

	private void createDisplay() {
		SpriteBackground background = new SpriteBackground(new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
				resources.menuBackgroundRegion, activity.getVertexBufferObjectManager()));
		setBackground(background);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		
		switch(pMenuItem.getID()) {
		case MENU_RETURN:
			SharedPreferencesManager.SaveBoolean("musicOn", true); /* TODO: implement music conductor */
			SceneManager.getSharedInstance().setScene(SceneType.MAIN_MENU);
			break;
		case MENU_SFX:
			break;
		case MENU_MUSIC:
			break;
		default:
				break;
		}
		return false;
	}

}
