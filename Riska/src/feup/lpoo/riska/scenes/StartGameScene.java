package feup.lpoo.riska.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import feup.lpoo.riska.gameInterface.AnimatedButtonSpriteMenuItem;
import feup.lpoo.riska.gameInterface.AnimatedTextButtonSpriteMenuItem;
import feup.lpoo.riska.io.LoadGame;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.music.Conductor;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.SceneManager.SceneType;

public class StartGameScene extends MenuScene implements IOnMenuItemClickListener {

	MainActivity activity; 
	SceneManager sceneManager;
	ResourceCache resources;

	final int MENU_NEW_GAME = 0;
	final int MENU_LOAD_GAME = 1;

	Sprite backgroundSprite;

	public StartGameScene() 
	{

		super(MainActivity.getSharedInstance().mCamera);		

		activity = MainActivity.getSharedInstance();
		sceneManager = SceneManager.getSharedInstance();
		resources = ResourceCache.getSharedInstance();	

		createDisplay();

	}


	public void createDisplay()
	{

		SpriteBackground background = new SpriteBackground(new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
				resources.getMenuBackgroundTexture(), activity.getVertexBufferObjectManager()));

		TiledTextureRegion button = resources.getStartButtonTexture();

		final AnimatedTextButtonSpriteMenuItem newGameButton = new AnimatedTextButtonSpriteMenuItem(MENU_NEW_GAME, button.getWidth(), 
				button.getHeight(), button, activity.getVertexBufferObjectManager(), "NEW", resources.getFont());

		final AnimatedTextButtonSpriteMenuItem loadGameButton = new AnimatedTextButtonSpriteMenuItem(MENU_NEW_GAME, button.getWidth(), 
				button.getHeight(), button, activity.getVertexBufferObjectManager(), "LOAD", resources.getFont());

		float centerX = MainActivity.CAMERA_WIDTH/2;

		newGameButton.setPosition(centerX, MainActivity.CAMERA_HEIGHT * 0.6f);
		loadGameButton.setPosition(centerX, MainActivity.CAMERA_HEIGHT * 0.3f);

		setBackground(background);
		addMenuItem(newGameButton);
		addMenuItem(loadGameButton);

		setOnMenuItemClickListener(this);

	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch(pMenuItem.getID()) {
		case MENU_NEW_GAME:
			sceneManager.setCurrentScene(SceneType.NEWGAME);
			break;
		case MENU_LOAD_GAME:
			LoadGame load = new LoadGame(activity);
			if(load.checkLoadGame())
				sceneManager.setCurrentScene(SceneType.LOADGAME);
			else
				sceneManager.setCurrentScene(SceneType.NEWGAME);
			break;
		default:
			break;
		}

		return false;
	}
}
