package feup.lpoo.riska;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.sprite.Sprite;
<<<<<<< HEAD
=======
import org.andengine.input.touch.TouchEvent;

import android.util.Log;
>>>>>>> 4542bda479f6a00084e8c3c3e0c2f312ebca28a4

public class MainMenuScene extends MenuScene implements IOnMenuItemClickListener {
	
	MainActivity activity; 
	SceneManager instance;
	
	final int MENU_START = 0;
	
<<<<<<< HEAD
	Sprite backgroundSprite;
=======
	Sprite testSprite;
>>>>>>> 4542bda479f6a00084e8c3c3e0c2f312ebca28a4
	
	public MainMenuScene() {
		
		super(MainActivity.getSharedInstance().mCamera);
		
		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();	
		
		SpriteBackground background = new SpriteBackground(new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
				instance.mMenuBackgroundTextureRegion, activity.getVertexBufferObjectManager()));

		IMenuItem startButton = new TextMenuItem(MENU_START, instance.mFont, 
				activity.getString(R.string.start_button), 
				activity.getVertexBufferObjectManager());
		
<<<<<<< HEAD
		startButton.setPosition(mCamera.getWidth()/2, mCamera.getHeight()/2);

		setOnMenuItemClickListener(this);
=======
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
>>>>>>> 4542bda479f6a00084e8c3c3e0c2f312ebca28a4
		
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
