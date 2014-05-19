package feup.lpoo.riska;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.util.adt.color.Color;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;

public class MainMenuScene extends MenuScene implements IOnMenuItemClickListener {
	
	MainActivity activity; 
	final int MENU_START = 0;
	
	public MainMenuScene() {
		super(MainActivity.getSharedInstance().mCamera);
		activity = MainActivity.getSharedInstance();
		
		setBackground(new Background(Color.WHITE));
		
		IMenuItem startButton = new TextMenuItem(MENU_START, activity.mFont, 
				activity.getString(R.string.start_button), 
				activity.getVertexBufferObjectManager());
		
		startButton.setPosition(mCamera.getWidth()/2 - startButton.getWidth()/2,
				mCamera.getHeight()/2 - startButton.getHeight()/2);
		
		addMenuItem(startButton);
		
		setOnMenuItemClickListener(this);
		
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
