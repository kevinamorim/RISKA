package feup.lpoo.riska.gameInterface;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.interfaces.Animated;
import feup.lpoo.riska.utilities.Utils;

public class RiskaSprite extends Sprite implements Animated {

	private float textBoundingFactor = 0.8f;
	
	private boolean rotating = false;

	private Text text;
	
	private AlphaModifier alphaModifier;
	private RotationModifier rotationModifier;

	// ==================================================
	// ==================================================
	public RiskaSprite(float pWidth, float pHeight, ITextureRegion pTexture, VertexBufferObjectManager vbom)
	{
		this(pTexture, vbom, null, null, 0);
		
		setSize(pWidth, pHeight);
	}
	
	public RiskaSprite(ITextureRegion pTexture, VertexBufferObjectManager vbom)
	{
		this(pTexture, vbom, null, null, 0);
	}
	
	public RiskaSprite(ITextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont)
	{
		super(0f, 0f, pTexture, vbom);

		createText(pString, pFont, vbom, Utils.maxNumericChars);
		wrapText();
	}

	public RiskaSprite(ITextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont, int maxChars)
	{
		super(0f, 0f, pTexture, vbom);

		createText(pString, pFont, vbom, maxChars);
		wrapText();
	}

	private void createText(String pString, Font pFont, VertexBufferObjectManager vbom, int maxChars)
	{
		if(pString != null)
		{
			text = new Text(0f, 0f, pFont, pString, maxChars, vbom);
			text.setColor(Color.WHITE);
			attachChild(text);
		}
	}

	// ==================================================
	// METHODS
	// ==================================================
	private void wrapText()
	{
		if(text != null)
		{
			Utils.wrap(text, this, textBoundingFactor);

			text.setPosition(Utils.halfX(this), Utils.halfY(this));
		}
	}

	public String getText()
	{
		return text.getText().toString();
	}

	public void showText()
	{
		if(text != null)
		{
			text.setVisible(true);
		}
	}

	public void hideText()
	{
		if(text != null)
		{
			text.setVisible(false);
		}
	}

	public void setText(String pString)
	{
		if(text != null)
		{
			text.setText(pString);

			wrapText();
		}
	}

	public void setTextBoundingFactor(float pFactor)
	{
		textBoundingFactor = pFactor;
		wrapText();
	}
	
	public void setTextColor(Color pColor)
	{
		if(text != null)
		{
			text.setColor(pColor);
		}
	}
	
	// ==================================================
	// OVERRIDE
	// ==================================================
	@Override
	public void setSize(float pWidth, float pHeight)
	{
		super.setSize(pWidth, pHeight);

		wrapText();
	}

	@Override
	public void setScale(float pScale)
	{
		for(int i = 0; i < getChildCount(); i++)
		{
			IEntity e = this.getChildByIndex(i);

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
	
	@Override
	public void fadeOut(float deltaTime)
	{
		unregisterEntityModifier(alphaModifier);
		
		if(getAlpha() > 0f)
		{
			if(deltaTime == 0f)
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
			if(deltaTime == 0f)
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
			rotating = true;
			
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
		
		if(deltaTime == 0f)
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
	
}
