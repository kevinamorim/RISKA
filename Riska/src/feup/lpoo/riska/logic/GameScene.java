package feup.lpoo.riska.logic;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

public class GameScene extends Scene {

	MainActivity activity;
	SceneManager instance;

	CameraManager cameraManager;

	protected Point touchPoint;
	protected Sprite background;

	private int regionButtonID[];
	private AnimatedTextButtonSpriteMenuItem regionButtons[];

	protected Map map; /* Not used for now */

	public GameScene() {

		//super(MainActivity.getSharedInstance().mCamera);

		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();

		cameraManager = new CameraManager(activity);
		cameraManager = CameraManager.getSharedInstance();
		

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

		setRegionButtons();

		//setOnMenuItemClickListener(this);
		setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent event) {
				
				MotionEvent ev = event.getMotionEvent();

				switch (ev.getActionMasked()) 
				{
				case MotionEvent.ACTION_DOWN: // first finger down
					
					cameraManager.setStartPoint(ev.getX(), ev.getY());			
					cameraManager.setMode(CameraManager.MODE_PAN);
					
					break;
					
				case MotionEvent.ACTION_UP: // first finger up			
					break;
					
				case MotionEvent.ACTION_POINTER_UP: // second finger up

					cameraManager.setMode(CameraManager.MODE_NONE);  

					break;
					
				case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

					cameraManager.setInitialDistance(ev);
					cameraManager.setMode(CameraManager.MODE_ZOOM);
					
					cameraManager.setMidPoint(ev);
					
					break;

				case MotionEvent.ACTION_MOVE:

					switch(cameraManager.mode)
					{
					case CameraManager.MODE_PAN:

						cameraManager.pan();

						break;

					case CameraManager.MODE_ZOOM:

						cameraManager.setFinalDistance(ev);

						cameraManager.zoom();
						break;
					default:

						break;
					}

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
