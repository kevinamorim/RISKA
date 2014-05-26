package feup.lpoo.riska;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.util.adt.color.Color;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;

public class MainMenuScene extends MenuScene implements IOnMenuItemClickListener {
	
	MainActivity activity; 
	final int MENU_START = 0;
	
	Sprite testSprite;
	
	public MainMenuScene() {
		super(MainActivity.getSharedInstance().mCamera);
		activity = MainActivity.getSharedInstance();
		
		setBackground(new Background(Color.WHITE));
		
		IMenuItem startButton = new TextMenuItem(MENU_START, activity.mFont, 
				activity.getString(R.string.start_button), 
				activity.getVertexBufferObjectManager());
		
		startButton.setPosition(mCamera.getWidth()/2 - startButton.getWidth()/2,
				mCamera.getHeight()/2 - startButton.getHeight()/2);
		
		//addMenuItem(startButton);
		
		//this.setOnMenuItemClickListener(this);
		
		/* TESTING */
		
		testSprite = new Sprite(activity.CAMERA_WIDTH/2, activity.CAMERA_HEIGHT/2, 
				activity.logoTextureRegion, activity.getVertexBufferObjectManager()) {

					@Override
					public boolean onAreaTouched(TouchEvent ev, float x, float y) {
						
						Log.d("Touched: ", "touched lol");
						
						return true;
					}
			
		};
		
		this.setOnMenuItemClickListener(this);
		this.registerTouchArea(testSprite);
		this.setTouchAreaBindingOnActionDownEnabled(true);
		this.attachChild(testSprite);
		
		/* END TESTING */
		
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
