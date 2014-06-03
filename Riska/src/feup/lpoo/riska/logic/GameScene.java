package feup.lpoo.riska.logic;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.graphics.Point;
import android.view.MotionEvent;

public class GameScene extends Scene {

	MainActivity activity;
	SceneManager instance;
	
	GameHUD hud;

	CameraManager cameraManager;

	protected Point touchPoint;
	protected Sprite background;

	private int regionButtonID[];
	private AnimatedTextButtonSpriteMenuItem regionButtons[];

	protected Map map; /* Not used for now */

	public GameScene() {

		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();
		
		cameraManager = instance.cameraManager;
		cameraManager.setZoomFactor(2.0f);
		
		regionButtonID = new int[instance.NUMBER_OF_REGIONS];
		regionButtons = new AnimatedTextButtonSpriteMenuItem[instance.NUMBER_OF_REGIONS];

//		SpriteBackground background = new SpriteBackground(new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
//				MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT,
//				instance.mapTextureRegion, activity.getVertexBufferObjectManager()));
//
//		setBackground(background);

		Sprite map = new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT,
				instance.mapTextureRegion, activity.getVertexBufferObjectManager());

		attachChild(map);
		
		hud = new GameHUD();
		activity.mCamera.setHUD(hud);
		
		setRegionButtons();

		//setOnMenuItemClickListener(this);
		this.setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(final Scene scene, final TouchEvent ev) {
				
				float x = ev.getX();
				float y = ev.getY();
				
				final int action = ev.getMotionEvent().getActionMasked();
				
				switch (action) 
				{
				case MotionEvent.ACTION_UP: // first finger up
					//cameraManager.setStartPoint(x, y);
					cameraManager.panToStart();
					break;
				case MotionEvent.ACTION_DOWN: // first finger down
					
					cameraManager.setStartPoint(x, y);
					//cameraManager.panToStart();
					break;
				case MotionEvent.ACTION_MOVE:
					
					cameraManager.setPoint(x, y);
					
					if(cameraManager.pointsNotNear()) {	
						cameraManager.setStartPoint(x, y);
						cameraManager.panToStart();
					}
					break;
				default:
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
