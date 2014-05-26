package feup.lpoo.riska;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.sprite.Sprite;

public class MainMenuScene extends MenuScene implements IOnMenuItemClickListener {
	
	MainActivity activity; 
	SceneManager instance;
	
	final int MENU_START = 0;
	
	Sprite backgroundSprite;
	
	public MainMenuScene() {
		
		super(MainActivity.getSharedInstance().mCamera);
		
		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();	
		
		SpriteBackground background = new SpriteBackground(new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
				instance.mMenuBackgroundTextureRegion, activity.getVertexBufferObjectManager()));

		IMenuItem startButton = new TextMenuItem(MENU_START, instance.mFont, 
				activity.getString(R.string.start_button), 
				activity.getVertexBufferObjectManager());
		
		startButton.setPosition(mCamera.getWidth()/2, mCamera.getHeight()/2);

		setOnMenuItemClickListener(this);
		
		setBackground(background);
		addMenuItem(startButton);
	
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch(pMenuItem.getID()) {
		case MENU_START:
			break;
		default:
				break;
		}
		return false;
	}
	
	

}
