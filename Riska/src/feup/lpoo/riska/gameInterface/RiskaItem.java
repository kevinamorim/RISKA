package feup.lpoo.riska.gameInterface;

import org.andengine.entity.Entity;

import feup.lpoo.riska.interfaces.Animated;

public abstract class RiskaItem extends Entity implements Animated {
	
	public RiskaItem(float pX, float pY)
	{
		super(pX, pY, 0f, 0f);
	}
	
	public RiskaItem(float pX, float pY, float pWidth, float pHeight)
	{
		super(pX, pY, pWidth, pHeight);
	}

	@Override
	public abstract void fadeOut(float deltaTime);
	
	@Override
	public abstract void fadeIn(float deltaTime);

	@Override
	public abstract void rotate(float deltaTime);

	@Override
	public abstract void rotate(float deltaTime, float startingAngle, float endingAngle);

	@Override
	public abstract void stopRotation();

	@Override
	public abstract void fadeOutAndStopRotation(float deltaTime);

}
