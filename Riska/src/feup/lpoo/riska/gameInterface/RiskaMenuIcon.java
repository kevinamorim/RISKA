package feup.lpoo.riska.gameInterface;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.utilities.Utils;

public class RiskaMenuIcon extends RiskaMenuItem {
	private RiskaSprite background;
	private RiskaSprite image;
	
	private AlphaModifier alphaModifier;
	private RotationModifier rotationModifier;
	private boolean rotating = false;
	
	private int value = 0;
	
	public RiskaMenuIcon(float pX, float pY, float pWidth, float pHeight, RiskaSprite pBackground, RiskaSprite pImage)
	{
		super(pX, pY);
		
		background = pBackground;
		image = pImage;
		
		setSize(pWidth, pHeight);
		
		attachChild(background);
		attachChild(image);
		
		pBackground.setPosition(Utils.halfX(this), Utils.halfY(this));
		pImage.setPosition(Utils.halfX(this), Utils.halfY(this));
	}
	
	/**
	 * Changes only the sprite color.
	 * The background color remains unchanged.
	 */
	public void setColor(Color pColor)
	{
		image.setColor(pColor);
	}
	
	public void setBackgroundColor(Color pColor)
	{
		background.setColor(pColor);
	}
	
	// --------------
	//   Overrides
	// --------------
	@Override
	public void setSize(float pWidth, float pHeight)
	{
		background.setSize(pWidth, pHeight);		
		image.setSize(pWidth, pHeight);
				
		super.setSize(pWidth, pHeight);
	}

	@Override
	public void setScale(float pScale)
	{
		background.setScale(pScale);
		image.setScale(pScale);

		super.setScale(pScale);
	}

	@Override
	public void setAlpha(float pAlpha)
	{
		background.setAlpha(pAlpha);
		image.setAlpha(pAlpha);

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

	public void setValue(int pValue)
	{
		value = pValue;
	}

	public int getValue()
	{
		return value;
	}
}
