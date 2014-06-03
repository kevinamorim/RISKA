package feup.lpoo.riska.logic;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.graphics.Point;
import android.view.MotionEvent;

public class GameScene extends Scene implements IOnSceneTouchListener {

	MainActivity activity;
	SceneManager instance;
	
	GameHUD hud;

	CameraManager cameraManager;

	protected Point touchPoint;
	protected Sprite background;

	protected Map map; /* Not used for now */

	public GameScene() {

		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();
		
		cameraManager = instance.cameraManager;

		Sprite map = new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT,
				instance.mapTextureRegion, activity.getVertexBufferObjectManager());

		attachChild(map);
		
		hud = new GameHUD();
		activity.mCamera.setHUD(hud);
		
		setOnSceneTouchListener(this);
		
		setRegionButtons();

		this.setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(final Scene scene, final TouchEvent ev) {
				
				float x = ev.getX();
				float y = ev.getY();
				
				Point p = new Point();
				p.set((int)x, (int)y);
				
				final int action = ev.getMotionEvent().getActionMasked();
				
				switch (action) 
				{
				case MotionEvent.ACTION_UP: // first finger up
					cameraManager.panToStart();
					break;
				case MotionEvent.ACTION_DOWN: // first finger down
					
					cameraManager.setStartPoint(p.x, p.y);
					break;
				case MotionEvent.ACTION_MOVE:
					
					cameraManager.setPoint(p.x, p.y);
					
					if(cameraManager.pointsNotNear()) {	
						cameraManager.setStartPoint(p.x, p.y);
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

	private void setRegionButtons() {

		for(int i = 0; i < instance.NUMBER_OF_REGIONS; i++) {

			int x = (instance.regions[i].getStratCenter().x * MainActivity.CAMERA_WIDTH)/100;
			int y = (instance.regions[i].getStratCenter().y * MainActivity.CAMERA_HEIGHT)/100;
			instance.regions[i].button.setPosition(x, y);
			instance.regions[i].button.setScale((float) 0.5);
			attachChild(instance.regions[i].button);
			registerTouchArea(instance.regions[i].button);

		}

	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}

}
