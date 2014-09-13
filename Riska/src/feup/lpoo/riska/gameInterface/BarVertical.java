package feup.lpoo.riska.gameInterface;

import org.andengine.entity.Entity;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import android.util.Log;
import feup.lpoo.riska.utilities.Utils;

public class BarVertical extends Entity {

	private int used;
	private int removed;
	
	private int min;
	private int capacity;
	
	private UIElement[] elements;
	
	private Color usedColor = Color.GREEN;
	private Color blockedColor = Color.BLACK;
	private Color removedColor = Color.RED; 
	
	public BarVertical(float pWidth, float pHeight, int pCapacity, int pMinCapacity, ITiledTextureRegion elementsTexture, VertexBufferObjectManager pVbom)
	{
		super(0f, 0f, pWidth, pHeight);
		
		this.capacity = pCapacity;
		this.min = pMinCapacity;
		this.used = 0;
		this.removed = 0;
		
		this.elements = new UIElement[capacity];
		
		
		float width = 0.9f * pWidth;
		float height = 0.9f * (pHeight/ capacity);
		
		float pX = 0.5f * pWidth;
		float factor = pHeight / capacity;
		float offset = 0.5f * factor;
		
		for(int i = 0; i < capacity; i++)
		{
			float pY = i * factor + offset;
			
			elements[i] = new UIElement(width, height, elementsTexture, pVbom);
			elements[i].setPosition(pX, pY);
			
			attachChild(elements[i]);
		}
		
		updateColor();
	}
	
	public void setColors(Color pBlockedColor, Color pUsedColor, Color pRemovedColor)
	{
		this.usedColor = pUsedColor;
		this.blockedColor = pBlockedColor;
		this.removedColor = pRemovedColor;
		
		updateColor();
	}
	
	public void setValues(int pUsed)
	{
		this.used = pUsed;
		this.removed = 0;
		
		updateColor();
	}
	
	public void setValues(int pUsed, int pRemoved)
	{
		this.used = pUsed;
		this.removed = pRemoved;
		
		updateColor();
	}
	
	private void updateColor()
	{		
		for(int i = 0; i < capacity; i++)
		{
			if(i < min)
			{
				elements[i].setSpriteColor(blockedColor);
				continue;
			}
			if(i < (min + used))
			{
				elements[i].setSpriteColor(usedColor);
				continue;
			}
			if(i < (min + used + removed))
			{
				elements[i].setSpriteColor(removedColor);
				continue;
			}
			
			elements[i].setSpriteColor(Color.TRANSPARENT);
		}
		
		updateAlpha();
	}
	
	private void updateAlpha()
	{
		for(int i = 0; i < capacity; i++)
		{
			if(i < min)
			{
				elements[i].setAlpha(getAlpha());
				continue;
			}
			if(i < (min + used))
			{
				elements[i].setAlpha(getAlpha());
				continue;
			}
			if(i < (min + used + removed))
			{
				elements[i].setAlpha(getAlpha());
				continue;
			}
			elements[i].setAlpha(0f);
		}
	}

	@Override
	public void setPosition(float pX, float pY)
	{		
		super.setPosition(pX, pY);
	}

	@Override
	public void setSize(float pWidth, float pHeight)
	{
		for(int i = 0; i < capacity; i++)
		{
			UIElement e = elements[i];
			
			e.setSize(e.getWidth() * pWidth / this.getWidth(), e.getHeight() * pHeight / this.getHeight());
			e.setPosition(e.getX() * pWidth / this.getWidth(), e.getY() * pHeight / this.getHeight());
		}
		
		super.setSize(pWidth, pHeight);
	}
	
	@Override
	public void setAlpha(float pAlpha)
	{
		super.setAlpha(pAlpha);
		
		updateAlpha();
	}
		
}
