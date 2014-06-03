package feup.lpoo.riska.logic;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

public class CameraManager {

	Point start, mid, temp;
	
	MainActivity activity;

//	protected static final int MODE_NONE = 0;
//	protected static final int MODE_PAN = 1;
//	protected static final int MODE_ZOOM = 2;
	
	protected static final float MIN_DIST = 5.0f;
	protected static final float MAX_ZOOM_FACTOR = 5.0f;
	protected static final float MIN_ZOOM_FACTOR = 1.0f;
	protected static final int NEAR_DIST = 10;
	
	protected float initialDistance;
	protected float finalDistance;
	protected float zoomFactor;
	
	protected int mode;
	
	static CameraManager instance;
	
	public CameraManager(MainActivity activity) {
		instance = this;
		
		this.activity = activity;
		this.start = new Point();
		this.mid = new Point();
		this.temp = new Point();
	}
	
	public void setStartPoint(float x, float y) {
		start.set((int)x, (int)y);
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public void setZoomFactor(float factor) {
		activity.mCamera.setZoomFactor(factor);
		zoomFactor = factor;
	}
	
	public void panToStart() {

		activity.mCamera.setCenter(start.x, start.y);
	}

	public static CameraManager getSharedInstance() {
		return instance;
	}

	public void setPoint(float x, float y) {
		temp.set((int)x, (int)y);
	}

	public boolean pointsNotNear() {
		int x = start.x - temp.x;
		int y = start.y - temp.y;
		
		return(Math.sqrt(x * x + y * y) > NEAR_DIST);
	}
 

}
