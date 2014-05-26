package feup.lpoo.riska;

public class Element extends Object {
	
	protected float x;
	protected float y;
	
	// TODO private float depth;
	
	public String TAG; 
	
	protected Element(float x, float y) {
		this.x = x;
		this.y = y;
		this.TAG = null;
	}
	
	protected Element(float x, float y, String TAG) {
		this.x = x;
		this.y = y;
		this.TAG = TAG;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public String getTAG() {
		return TAG;
	}

	public void setTAG(String tAG) {
		TAG = tAG;
	}

}
