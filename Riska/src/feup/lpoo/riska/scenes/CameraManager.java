package feup.lpoo.riska.scenes;

import org.andengine.engine.camera.SmoothCamera;

import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.logic.MainActivity;
import android.graphics.Point;

public class CameraManager extends SmoothCamera {

	Point start, mid, temp, center;
	
	private final float MAX_ZOOM_FACTOR = 2.0f;
	private final float MIN_ZOOM_FACTOR = 1.0f;
	private final int NEAR_DIST = 5;
	
	private float zoomFactor;
	
	public CameraManager(float pX, float pY, float pWidth, float pHeight, float pMaxVelocityX, float pMaxVelocityY, 
			float pMaxZoomFactorChange) {
		
		super(pX, pY, pWidth, pHeight, pMaxVelocityX, pMaxVelocityY, pMaxZoomFactorChange);
		
		this.start = new Point();
		this.mid = new Point();
		this.temp = new Point();
		this.center = new Point();
		zoomFactor = MIN_ZOOM_FACTOR;
		
		center.set((int) getCenterX(), (int) getCenterY());
		
	}
	
	public void setStartPoint(float x, float y) {
		start.set((int)x, (int)y);
	}
	
	public void setZoomFactor(float factor) {
		factor = checkFactor(factor);
		
		if(factor < zoomFactor) {
			panTo(center);
			setStartPoint(center.x, center.y);
		}
		
		super.setZoomFactor(factor);
		zoomFactor = factor;
	}
	
	public void setZoomFactorFromPerc(float perc) {
		float factor = perc * MAX_ZOOM_FACTOR;
			
		setZoomFactor(factor);
	}

	public void panToStart() {
		setCenter(start.x, start.y);
	}
	
	public void panToCenter() {
		setCenter(center.x, center.y);
	}
	
	public void panTo(Point p) {
		setCenter(p.x, p.y);
	}
	
	public void jumpTo(Point p) {
		setCenterDirect(p.x, p.y);
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
		if(getZoomFactor() == 1.0f) {
			super.setZoomFactor(MAX_ZOOM_FACTOR);
			panTo(p);
		} else {
			super.setZoomFactor(MIN_ZOOM_FACTOR);
			panTo(center);
		}
	}
	
	public void zoomIn() {
		super.setZoomFactor(MAX_ZOOM_FACTOR);
	}
	
	public void zoomOut() {
		super.setZoomFactor(MIN_ZOOM_FACTOR);
	}
	
	public void focusOnRegion(Region region) {
		setBoundsEnabled(false);
		super.setZoomFactor(MAX_ZOOM_FACTOR);
		Point p = new Point((int) (((region.getStratCenter().x * getWidth()/100) - (getHeight() * 0.25))),
				(int) ((region.getStratCenter().y * getHeight())/100));
		panTo(p);
	}
	

}
