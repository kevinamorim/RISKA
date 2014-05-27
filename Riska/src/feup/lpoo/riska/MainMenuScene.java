package feup.lpoo.riska;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.util.adt.color.Color;

import android.graphics.Bitmap;
import android.util.Log;
import feup.lpoo.riska.region.color.RegionColor;
import feup.lpoo.riska.region.creator.RegionCreator;

public class MainMenuScene extends MenuScene implements IOnMenuItemClickListener {
	
	MainActivity activity; 
	SceneManager instance;
	
	RegionCreator creator;
	
	final int MENU_START = 0;
	
	Sprite backgroundSprite;
	
	public MainMenuScene() {
		
		super(MainActivity.getSharedInstance().mCamera);
		
		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();	
		
		evaluateRegions(); /* NEW */
		
		SpriteBackground background = new SpriteBackground(new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
				instance.mMenuBackgroundTextureRegion, activity.getVertexBufferObjectManager()));

		IMenuItem startButton = new TextMenuItem(MENU_START, instance.mFont, 
				activity.getString(R.string.start_button), 
				activity.getVertexBufferObjectManager());
		
		startButton.setPosition(mCamera.getWidth()/2, mCamera.getHeight()/2);

		setOnMenuItemClickListener(this);
		
		setBackground(background);
		//addMenuItem(startButton);
	
	}
	
	/*
	 * NEW REGION CREATOR
	 */
	private void evaluateRegions() {

		creator = new RegionCreator();
		
		Log.d("regions", "Will now create the regions.");
		
		creator = RegionCreator.getSharedInstance();
		creator.CreateAllRegions(instance.mapRegions, new RegionColor(Color.BLACK));
		
		Log.d("regions", "Has created the regions.");
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
