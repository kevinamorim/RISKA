package feup.lpoo.riska.gameInterface;

import java.util.ArrayList;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import android.view.MotionEvent;
import feup.lpoo.riska.utilities.Utils;

public class RiskaSliderList extends RiskaItem {

	private RiskaSprite background;
	private RiskaSprite slider;
	
	private AlphaModifier alphaModifier;
	private RotationModifier rotationModifier;
	private boolean rotating = false;
	
	private ArrayList<String> values;
	private int value = 0;
	
	public RiskaSliderList(float pX, float pY, ArrayList<String> pValues, RiskaSprite pBackground, RiskaSprite pSlider)
	{
		super(pX, pY);
		
		background = pBackground;
		slider = pSlider;
		
		this.setSize(background.getWidth(), background.getHeight());
		
		if(pValues == null)
		{
			values = new ArrayList<String>();
		}
		else
		{
			values = pValues;
		}
		
		attachChild(background);
		attachChild(slider);
	}
	
	public RiskaSliderList(float pX, float pY, RiskaSprite pBackground, RiskaSprite pSlider)
	{
		this(pX, pY, null, pBackground, pSlider);
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
	
	public void addValue(String str)
	{
		values.add(str);
		if(value < 0)
			value = 0;
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
				this.setValue(Math.round(pX / getWidth() * values.size()));
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
		if(value == 0)
			value = 1;
		else if(value > values.size())
			value = values.size();
	}

	public void setValue(int pValue)
	{
		this.value = pValue;
		clampValue();
	}
	
	public int getValue()
	{
		return value;
	}
}
