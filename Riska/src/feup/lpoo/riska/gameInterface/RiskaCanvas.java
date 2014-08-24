package feup.lpoo.riska.gameInterface;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;

import feup.lpoo.riska.utilities.Utils;

/**
 * Class created with the single purpose of holding other entities
 *
 */
public class RiskaCanvas extends Entity {

	public RiskaCanvas(float pX, float pY, float pWidth, float pHeight)
	{
		super(pX, pY, pWidth, pHeight);
	}
	
	public RiskaCanvas(Camera camera)
	{
		super(camera.getCenterX(), camera.getCenterY(), camera.getWidth(), camera.getHeight());
	}
	
	/**
	 * Adds a graphic to the canvas.
	 * 
	 * @param object : graphic
	 * @param pX : normalized x coordinate relative to the canvas
	 * @param pY : normalized y coordinate relative to the canvas
	 * @param pWidth : normalized width relative to the canvas
	 * @param pHeight : normalized height coordinate relative to the canvas
	 */
	public void addGraphic(Entity object, float pX, float pY, float pWidth, float pHeight)
	{
		if(!object.hasParent())
		{
			attachChild(object);
			
			Utils.wrap(object, pWidth * getWidth(), pHeight * getHeight(), 1f);
			//object.setSize(pWidth * getWidth(), pHeight * getHeight());	
			object.setPosition(pX * getWidth(), pY * getHeight());
		}
	}
}
