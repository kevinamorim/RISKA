package feup.lpoo.riska.logic;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.graphics.Point;
import android.util.Log;

public class GameScene extends Scene {
	
	MainActivity activity;
	SceneManager instance;
	
	protected Point touchPoint;
	protected Sprite background;
	
	private int regionButtonID[];
	private AnimatedTextButtonSpriteMenuItem regionButtons[];
	
	protected Map map; /* Not used for now */
	
	public GameScene() {
		
		//super(MainActivity.getSharedInstance().mCamera);
		
		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();
		
		regionButtonID = new int[instance.NUMBER_OF_REGIONS];
		regionButtons = new AnimatedTextButtonSpriteMenuItem[instance.NUMBER_OF_REGIONS];
		
		SpriteBackground background = new SpriteBackground(new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT,
				instance.mapTextureRegion, activity.getVertexBufferObjectManager()));
		
		setBackground(background);
				
		setRegionButtons();
		
		Sprite logoSprite;
		
		logoSprite = new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 50, 100, 
				instance.logoTextureRegion, activity.getVertexBufferObjectManager());
	
		
		attachChild(logoSprite);
		
		//setOnMenuItemClickListener(this);
		setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					activity.mCamera.setCenter(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					activity.mCamera.setZoomFactor(5.0f);
					break;
				case TouchEvent.ACTION_UP:
					activity.mCamera.setCenter(0, 0);
					activity.mCamera.setZoomFactor(1.0f);
					break;
				}
				
				return true;
			}
		});
		
	}


//	@Override
//	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
//			float pMenuItemLocalX, float pMenuItemLocalY) {
//		// TODO Auto-generated method stub
//		return false;
//	}
	
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
			
			//addMenuItem(regionButtons[i]);
			
		}
	
		
	}
	
	
}
