package feup.lpoo.riska.gameInterface;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import android.view.MotionEvent;
import feup.lpoo.riska.utilities.Utils;

public class RiskaMenuSliderFloat extends RiskaMenuItem {

	protected RiskaSprite background;
	protected RiskaSprite slider;
	
	protected AlphaModifier alphaModifier;
	protected RotationModifier rotationModifier;
	protected boolean rotating = false;
	
	protected float value = 0f;
	protected float min = 0f;
	protected float max = 0f;
	
	public RiskaMenuSliderFloat(float pX, float pY, float minBound, float maxBound, RiskaSprite pBackground, RiskaSprite pSlider)
	{
		super(pX, pY);
		
		background = pBackground;
		slider = pSlider;
		
		this.setSize(background.getWidth(), background.getHeight());
		this.min = minBound;
		this.max = maxBound;
		
		attachChild(background);
		attachChild(slider);
	}
	
	/**
	 * Changes only the sprite color.
	 * The background color remains unchanged.
	 */
	public void setColor(Color pColor)
	{
		slider.setColor(pColor);
	}
	
	public void setBackgroundColor(Color pColor)
	{
		background.setColor(pColor);
	}
	
	// --------------
	//   Overrides
	// --------------
	@Override
	public boolean onAreaTouched(TouchEvent ev, float pX, float pY) {

		switch(ev.getMotionEvent().getActionMasked()) 
		{

		case MotionEvent.ACTION_UP:
			if(Utils.contains(this, pX, pY))
			{
				this.setValue(pX / getWidth() * max);
				this.slider.setPosition(pX, slider.getY());
			}
			break;
		}
		
		return true;
	}
	
	@Override
	public void setSize(float pWidth, float pHeight)
	{
		background.setSize(pWidth, pHeight);
		
		float fWidth, fHeight;
		
		fWidth = getWidth() * pWidth / slider.getWidth();
		fHeight = getHeight() * pHeight / slider.getHeight();
		
		slider.setSize(fWidth, fHeight);
				
		super.setSize(pWidth, pHeight);
	}

	@Override
	public void setScale(float pScale)
	{
		background.setScale(pScale);
		slider.setScale(pScale);

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
	
	@Override
	public void fadeOut(float deltaTime)
	{
		unregisterEntityModifier(alphaModifier);
		
		if(getAlpha() > 0f)
		{
			if(deltaTime <= 0f)
			{
				setAlpha(0f);
			}
			else
			{
				alphaModifier = new AlphaModifier(deltaTime, getAlpha(), 0f);
				registerEntityModifier(alphaModifier);
			}
		}
	}

	@Override
	public void fadeIn(float deltaTime)
	{
		unregisterEntityModifier(alphaModifier);
		
		if(getAlpha() < 1f)
		{
			if(deltaTime <= 0f)
			{
				setAlpha(1f);
			}
			else
			{
				alphaModifier = new AlphaModifier(deltaTime, getAlpha(), 1f);
				registerEntityModifier(alphaModifier);
			}
		}
	}
	
	@Override
	public void rotate(float deltaTime)
	{
		rotate(deltaTime, 0, -360);
	}
	
	@Override
	public void rotate(float pSpeed, float pStartingAngle, float pEndingAngle)
	{
		if(!rotating)
		{
			rotationModifier = new RotationModifier(pSpeed, pStartingAngle, pEndingAngle);

			registerEntityModifier(new LoopEntityModifier(rotationModifier));
		}
	}
	
	@Override
	public void stopRotation()
	{
		unregisterEntityModifier(rotationModifier);
		
		rotating = false;
	}
	
	@Override
	public void fadeOutAndStopRotation(float deltaTime)
	{
		unregisterEntityModifier(alphaModifier);
		
		if(deltaTime <= 0f)
		{
			setAlpha(0f);
			stopRotation();
		}
		else
		{
			alphaModifier = new AlphaModifier(deltaTime, 1f, 0f)
			{
				@Override
				protected void onModifierFinished(IEntity pItem)
				{
					stopRotation();
					super.onModifierFinished(pItem);
				}
				
			};
			registerEntityModifier(alphaModifier);
		}
	}
	
	@Override
	public boolean isVisible()
	{
		return (getAlpha() > 0) && super.isVisible();
	}
	
	private void clampValue()
	{
		if(value < min)
			value = min;
		else if(value > max)
			value = max;
	}

	public void setValue(float pValue)
	{
		this.value = pValue;
		clampValue();
	}
	
	public float getValue()
	{
		return value;
	}
}
