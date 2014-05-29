package feup.lpoo.riska;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;

public class GameScene extends Scene {
	MainActivity activity;
	SceneManager manager;
	
	protected Point touchPoint;
	protected Sprite background;
	
	final int COLS = 4;
	final int ROWS = 3;
	
	protected Sprite regions[];
	
	protected Map map;
	
	public GameScene() {
		
		activity = MainActivity.getSharedInstance();
		manager = SceneManager.getSharedInstance();
		
		setBackground(new Background(Color.CYAN));
		
		createMap();
		
		//this.map = map;
//		
//		this.touchPoint = new Point();
//		
//		background = new Sprite(MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 
//				manager.mapTextureRegion, activity.getVertexBufferObjectManager()) {
//
//					@Override
//					public boolean onAreaTouched(TouchEvent ev, float touchX, float touchY) {
//						// TODO
//						switch(MotionEventCompat.getActionMasked(ev.getMotionEvent())) {
//						case TouchEvent.ACTION_DOWN:
//							int x,y;
//							x = (int)(touchX);
//							y = (int)(touchY);
//							
//							touchPoint.set(x,y);
//							
//							convertTouchPointToBitmapCoord(touchPoint);
//							
//							Log.d("map", "touched: (" + touchPoint.x + "," + touchPoint.y + ")");
//							
//							break;
//						default:
//							break;
//						}
//
//						return true;
//					}
//			
//		};
//		
//		background.setSize(MainActivity.CAMERA_WIDTH, MainActivity.CAMERA_HEIGHT);
//		
//		registerTouchArea(background);
//		
//		attachChild(background);
//		
//		background.setColor(new Color(Color.GREEN));
		
	}

	protected Region getTouchedRegion(Point point) {
		
		for(Region region: map.getRegions()) {
			if(isBetween(point.x, region.getX(), region.getX() + region.getWidth())
					&& isBetween(point.y, region.getY(), region.getY() + region.getHeight())) {
				//Log.d("map", "can be: " + region.toString());
				if(region.getMask().isOpaque(point.x, point.y)) {
					return region;
				}
			}
		}
		
		return null;
	}

	private boolean isBetween(int comp, float left, float right) {
		return (comp >= left && comp <= right);
	}

	protected void convertTouchPointToBitmapCoord(Point point) {
		float horizontalConversionScale = ((float)manager.mapTexture.getWidth()) / background.getWidth();
		float verticalConversionScale = ((float)manager.mapTexture.getWidth()) / background.getWidth();
		
		point.x = (int) (point.x * horizontalConversionScale);
		
		// Since in bitmap the coordinates start in the upper left corner
		point.y = (int) (background.getHeight() - point.y);
		point.y = (int) (point.y * verticalConversionScale);
	}
	
	private void createMap() {
		
		regions = new Sprite[COLS*ROWS];
		
		float width = MainActivity.CAMERA_WIDTH/COLS;
		float height = MainActivity.CAMERA_HEIGHT/ROWS;
		
		int pos = 0;
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				regions[pos] = new Sprite((width * j) + (width/2), (height * i) + (height/2), 
						manager.mRegionsTextureRegions[pos],
						activity.getVertexBufferObjectManager());
				regions[pos].setSize(width, height);
				regions[pos].setColor(Color.RED);
				attachChild(regions[pos]);
				pos++;
			}
		}
		//(width * j) + (width/2), (height * i) + (height/2)
		
	}
	
	
}
