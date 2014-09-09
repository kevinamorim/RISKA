package feup.lpoo.riska.gameInterface;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

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
	
	// ==================================================
	// METHODS
	// ==================================================
	public void setCanvasSprite(RiskaSprite object)
	{
		canvasSprite = object;

		canvasSprite.setSize(getWidth(), getHeight());
		canvasSprite.setPosition(0.5f * getWidth(), 0.5f * getHeight());
		
		attachChild(canvasSprite);
	}

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
	
	public void fadeOut(float deltaTime)
	{
		if(deltaTime == 0f)
		{
			setAlpha(0f);
		}
		else
		{
			AlphaModifier alphaOut = new AlphaModifier(deltaTime, 1f, 0f);
			registerEntityModifier(alphaOut);
		}	
	}
	
	public void fadeIn(float deltaTime)
	{
		if(deltaTime == 0f)
		{
			setAlpha(1f);
		}
		else
		{
			AlphaModifier alphaIn = new AlphaModifier(deltaTime, 0f, 1f);
			registerEntityModifier(alphaIn);
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

	// ==================================================
	// OVERRIDE
	// ==================================================
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
	
	@Override
	public void setColor(Color pColor)
	{
		for(int i = 0; i < getChildCount(); i++)
		{
			IEntity e = getChildByIndex(i);
			
			e.setColor(pColor);
		}
		
		super.setColor(pColor);
	}
	
	@Override
	public void setScale(float pScale)
	{
		for(int i = 0; i < getChildCount(); i++)
		{
			IEntity e = getChildByIndex(i);
			
			e.setScale(pScale);
		}
		
		super.setScale(pScale);
	}

	@Override
	public void setAlpha(float pAlpha)
	{
		for(int i = 0; i < getChildCount(); i++)
		{
			IEntity e = getChildByIndex(i);
			
			e.setAlpha(pAlpha);
		}
		
		super.setAlpha(pAlpha);
	}
}
