package feup.lpoo.riska.interfaces;

public interface Animated {
	
	public void fadeOut(float deltaTime);

	public void fadeOut();

	public void fadeIn(float deltaTime);

	public void fadeIn();

	public void rotate();
	
	public void rotate(float pSpeed);
	
	public void rotate(float pSpeed, float pStartingAngle, float pEndingAngle);
	
	public void stopRotation();
	
	public void fadeOutAndStopRotation(float deltaTime);

}
