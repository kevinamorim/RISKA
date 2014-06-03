package feup.lpoo.riska.logic;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

public class GameHUD extends HUD {
	
	MainActivity activity;
	SceneManager instance;
	CameraManager cameraManager;
	
	static final int ZOOM_X = 30;
	static final int ZOOM_Y = MainActivity.CAMERA_HEIGHT / 2;
	static final int ZOOM_SL_WIDTH = ZOOM_X * 2;
	static final int ZOOM_SL_HEIGHT = MainActivity.CAMERA_HEIGHT;
	
	static final int ZOOM_BT_WIDTH = ZOOM_SL_WIDTH + 10;
	static final int ZOOM_BT_HEIGHT = ZOOM_SL_HEIGHT / 5;
	
	protected Sprite zoomSlider;
	protected Sprite zoomButton;

	public GameHUD() {
		
		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();
		cameraManager = CameraManager.getSharedInstance();
		
		zoomSlider = new Sprite(ZOOM_X, ZOOM_Y, ZOOM_SL_WIDTH, ZOOM_SL_HEIGHT,
				instance.zoomSliderTextureRegion, activity.getVertexBufferObjectManager()) {

					@Override
					public boolean onAreaTouched(TouchEvent ev, float x, float y) {
						if(y < (ZOOM_SL_HEIGHT - ZOOM_BT_HEIGHT) ) {
							
							zoomButton.setPosition(ZOOM_X, ZOOM_Y - ZOOM_SL_HEIGHT/2 + y + ZOOM_BT_HEIGHT / 2);
							
							cameraManager.setZoomFactorFromPerc(zoomButton.getY() / zoomSlider.getHeight());
						}
						return true;
					}
					
				};
		
		zoomButton = new Sprite( ZOOM_X, ZOOM_Y, ZOOM_BT_WIDTH, ZOOM_BT_HEIGHT,
				instance.zoomButtonTextureRegion, activity.getVertexBufferObjectManager());
		
		attachChild(zoomSlider);
		attachChild(zoomButton);
		
		registerTouchArea(zoomSlider);
	}
}
