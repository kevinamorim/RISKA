package feup.lpoo.riska;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;

import android.graphics.Point;

public class GameScene extends MenuScene implements IOnMenuItemClickListener {
	
	MainActivity activity;
	SceneManager instance;
	
	protected Point touchPoint;
	protected Sprite background;
	
	private int regionButtonID[];
	private AnimatedTextButtonSpriteMenuItem regionButtons[];
	
	protected Map map; /* Not used for now */
	
	public GameScene() {
		
		super(MainActivity.getSharedInstance().mCamera);
		
		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();
		
		regionButtonID = new int[instance.NUMBER_OF_REGIONS];
		regionButtons = new AnimatedTextButtonSpriteMenuItem[instance.NUMBER_OF_REGIONS];
		
		SpriteBackground background = new SpriteBackground(new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT,
				instance.mapTextureRegion, activity.getVertexBufferObjectManager()));
		
		setBackground(background);
		
		setRegionButtons();
		
		setOnMenuItemClickListener(this);
	
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void setRegionButtons() {
		
		for(int i = 0; i < instance.NUMBER_OF_REGIONS; i++) {
			
			regionButtons[i] = new AnimatedTextButtonSpriteMenuItem(regionButtonID[i], 
					instance.regionButtonTiledTextureRegion.getWidth(), 
					instance.regionButtonTiledTextureRegion.getHeight(), instance.regionButtonTiledTextureRegion, 
					activity.getVertexBufferObjectManager(), "1", instance.mFont);
			
			int x = (instance.regions[i].getStratCenter().x * MainActivity.CAMERA_WIDTH)/100;
			int y = (instance.regions[i].getStratCenter().y * MainActivity.CAMERA_HEIGHT)/100;
			regionButtons[i].setPosition(x, y);
			//regionButtons[i].setSize((float)regionButtons[i].getWidth()*0.5, (float)regionButtons[i].getHeight()*0.5);
			regionButtons[i].setScale((float) 0.5);
			
			addMenuItem(regionButtons[i]);
			
		}
	
		
	}
	
	
}
