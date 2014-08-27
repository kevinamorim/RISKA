package feup.lpoo.riska.gameInterface;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.text.Text;

import feup.lpoo.riska.utilities.Utils;

/**
 * Class created with the single purpose of holding other entities
 *
 */
public class RiskaCanvas extends Entity implements IEntity {

	private RiskaSprite canvasSprite;

	public RiskaCanvas(float pX, float pY, float pWidth, float pHeight)
	{
		super(pX, pY, pWidth, pHeight);
	}
	
	public RiskaCanvas(Camera camera)
	{
		super(camera.getCenterX(), camera.getCenterY(), camera.getWidth(), camera.getHeight());
	}
	
	public void setCanvasSprite(RiskaSprite object)
	{
		canvasSprite = object;

		canvasSprite.setSize(getWidth(), getHeight());
		canvasSprite.setPosition(0.5f * getWidth(), 0.5f * getHeight());
		
		attachChild(canvasSprite);
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
			
			object.setSize(pWidth * getWidth(), pHeight * getHeight());	
			object.setPosition(pX * getWidth(), pY * getHeight());
		}
	}
	
	public void addGraphicWrap(Entity object, float pX, float pY, float pWidth, float pHeight)
	{
		if(!object.hasParent())
		{
			attachChild(object);
			
			Utils.wrap(object, pWidth * getWidth(), pHeight * getHeight(), 1f);
			object.setPosition(pX * getWidth(), pY * getHeight());
		}
	}
	
	public void addText(Text text, float pX, float pY, float pWidth, float pHeight)
	{
		if(!text.hasParent())
		{
			attachChild(text);
			
			Utils.wrap(text, pWidth * getWidth(), pHeight * getHeight(), 1f);
			text.setPosition(pX * getWidth(), pY * getHeight());
		}
	}
	
	public void close()
	{
		if(canvasSprite != null)
		{
			canvasSprite.close();
		}
	}
	
	public void open()
	{
		if(canvasSprite != null)
		{
			canvasSprite.open();
		}
	}
	
	public void animate()
	{
		if(canvasSprite != null)
		{
			canvasSprite.animate();
		}
	}
	
	@Override
	public void setVisible(boolean pVisible)
	{
		super.setVisible(pVisible);

		Utils.setChildrenVisible(this, pVisible);
	}
	
	public float left()
	{
		return this.getX() - 0.5f * this.getWidth();
	}
	
	public float right()
	{
		return this.getX() + 0.5f * this.getWidth();
	}
	
	public float top()
	{
		return this.getY() + 0.5f * this.getHeight();
	}
	
	public float bottom()
	{
		return this.getY() - 0.5f * this.getHeight();
	}

	public float halfX()
	{
		return 0.5f * this.getWidth();
	}
	
	public float halfY()
	{
		return 0.5f * this.getHeight();
	}
	
	@Override
	public void setSize(float pWidth, float pHeight)
	{
		for(int i = 0; i < getChildCount(); i++)
		{
			IEntity e = getChildByIndex(i);
			
			e.setSize(e.getWidth() * pWidth / this.getWidth(), e.getHeight() * pHeight / this.getHeight());
			e.setPosition(e.getX() * pWidth / this.getWidth(), e.getY() * pHeight / this.getHeight());
		}
		
		super.setSize(pWidth, pHeight);
	}

	@Override
	public void setPosition(float pX, float pY)
	{	
		super.setPosition(pX, pY);
	}
}
