package feup.lpoo.riska.logic;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

public class CameraManager {

	Point start, mid;
	
	MainActivity activity;
	
	protected static final int MODE_NONE = 0;
	protected static final int MODE_PAN = 1;
	protected static final int MODE_ZOOM = 2;
	
	protected static final float MIN_DIST = 5.0f;
	
	protected float initialDistance;
	protected float finalDistance;
	
	protected int mode;
	
	static CameraManager instance;
	
	public CameraManager(MainActivity activity) {
		instance = this;
		
		this.activity = activity;
		this.start = new Point();
		this.mid = new Point();
		
		this.mode = MODE_NONE;
	}
	
	public void setStartPoint(float x, float y) {
		start.set((int)x, (int)y);
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public void zoom() {
		float factor = (finalDistance / initialDistance) * activity.mCamera.getZoomFactor();
		
		if(factor < 1.0f) factor = 1.0f;
		
		activity.mCamera.setCenter(mid.x, mid.y);
		activity.mCamera.setZoomFactor(factor);
	}
	
	public void pan() {

		activity.mCamera.setCenter(start.x, start.y);
	}

	public static CameraManager getSharedInstance() {
		return instance;
	}

	public void setInitialDistance(MotionEvent ev) {
		float d = spacing(ev);
		
		if(d > MIN_DIST) {
			initialDistance = d;
		}
	}
	
	public void setFinalDistance(MotionEvent ev) {
		float d = spacing(ev);
		
		if(d > MIN_DIST) {
			finalDistance = d;
		}
	}
	
	public void setMidPoint(MotionEvent event) 
	{
	    float x = ( event.getX(0) + event.getX(1) ) / 2;
	    float y = ( event.getY(0) + event.getY(1) ) / 2;
	    mid.set((int)x, (int)y);
	}
	
	private float spacing(MotionEvent ev) {
	    float x = ev.getX(0) - ev.getX(1);
	    float y = ev.getY(0) - ev.getY(1);
	    return (float) Math.sqrt(x * x + y * y);
	}

}
