package feup.lpoo.riska.interfaces;

public interface Animated {
	
	public void fadeOut(float deltaTime);

	public void fadeIn(float deltaTime);
	
	public void rotate(float deltaTime);

	public void rotate(float deltaTime, float startingAngle, float endingAngle);
	
	public void stopRotation();
	
	public void fadeOutAndStopRotation(float deltaTime);

}
