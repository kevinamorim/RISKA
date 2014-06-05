package feup.lpoo.riska.logic;

import android.graphics.Point;

public class CameraManager {

	Point start, mid, temp, center;
	
	MainActivity activity;

//	protected static final int MODE_NONE = 0;
//	protected static final int MODE_PAN = 1;
//	protected static final int MODE_ZOOM = 2;
	
	protected static final float MAX_ZOOM_FACTOR = 2.0f;
	protected static final float MIN_ZOOM_FACTOR = 1.0f;
	protected static final int NEAR_DIST = 5;
	
	protected float initialDistance;
	protected float finalDistance;
	protected float zoomFactor;
	
	protected int mode;
	
	static CameraManager instance;
	
	public CameraManager() {
		
		instance = this;
		
		this.activity = MainActivity.getSharedInstance();
		
		this.start = new Point();
		this.mid = new Point();
		this.temp = new Point();
		this.center = new Point();
		
		center.set(MainActivity.CAMERA_WIDTH / 2, MainActivity.CAMERA_HEIGHT / 2);
		
		this.zoomFactor = 1.0f;
		
	}
	
	public void setStartPoint(float x, float y) {
		start.set((int)x, (int)y);
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public void setZoomFactor(float factor) {
		factor = checkFactor(factor);
		
		if(factor < zoomFactor) {
			panTo(center);
			setStartPoint(center.x, center.y);
		}
		
		activity.mCamera.setZoomFactor(factor);
		zoomFactor = factor;
	}
	
	public void setZoomFactorFromPerc(float perc) {
		float factor = perc * MAX_ZOOM_FACTOR;
			
		setZoomFactor(factor);
	}

	public void panToStart() {
		activity.mCamera.setCenter(start.x, start.y);
	}
	
	public void panToCenter() {
		activity.mCamera.setCenter(center.x, center.y);
	}
	
	public void panTo(Point p) {
		activity.mCamera.setCenter(p.x, p.y);
	}
	
	public void jumpTo(Point p) {
		activity.mCamera.setCenterDirect(p.x, p.y);
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
	
	public float getZoomFactorPercentage() {
		return ((zoomFactor - MIN_ZOOM_FACTOR)/ MAX_ZOOM_FACTOR);
	}
 
	private float checkFactor(float factor) {
		return Math.min(MAX_ZOOM_FACTOR, Math.max(MIN_ZOOM_FACTOR, factor));
	}
	
	public void setAutomaticZoom(Point p) {
		if(activity.mCamera.getZoomFactor() == 1.0f) {
			activity.mCamera.setZoomFactor(MAX_ZOOM_FACTOR);
			panTo(p);
		} else {
			activity.mCamera.setZoomFactor(MIN_ZOOM_FACTOR);
			panTo(center);
		}
	}
	
	public void zoomIn() {
		activity.mCamera.setZoomFactor(MAX_ZOOM_FACTOR);
	}
	
	public void zoomOut() {
		activity.mCamera.setZoomFactor(MIN_ZOOM_FACTOR);
	}
	
	public void focusOnRegion(Region region) {
		activity.mCamera.setBoundsEnabled(false);
		activity.mCamera.setZoomFactor(MAX_ZOOM_FACTOR);
		Point p = new Point((int) (((region.stratCenter.x * MainActivity.CAMERA_WIDTH)/100) - (MainActivity.CAMERA_HEIGHT * 0.25)),
				((region.stratCenter.y * MainActivity.CAMERA_HEIGHT)/100));
		panTo(p);
		//activity.mCamera.setBoundsEnabled(true);
	}
	

}
