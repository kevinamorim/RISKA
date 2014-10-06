package feup.lpoo.riska.gameInterface;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.interfaces.Animated;
import feup.lpoo.riska.logic.GameOptions;
import feup.lpoo.riska.utilities.Utils;

public class RiskaButtonSprite extends ButtonSprite implements Animated {

	private static float textBoundingFactor = 0.8f;
	private static float animationTime = GameOptions.animationTime;
	private final float rotateAnimationTime = 3f;
	private final float rotateStartingAngle = 0f;
	private final float rotateEndingAngle = -360f;
	
	private static int maxChars = Utils.maxNumericChars;

	private boolean rotating = false;
	
	private Text text;

	private Color textColor = Color.PINK;
	private Color spriteColor = Utils.OtherColors.GREY;
	
	private AlphaModifier alphaModifier;
	private RotationModifier rotationModifier;

	public RiskaButtonSprite(ITiledTextureRegion pTexture, VertexBufferObjectManager vbom)
	{
		this(0f, 0f, pTexture, vbom);
	}
	
	public RiskaButtonSprite(ITiledTextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont)
	{
		this(pTexture, vbom, pString, pFont, Utils.maxNumericChars);
	}

	public RiskaButtonSprite(ITiledTextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont, int maxCharacters)
	{
		this(0f, 0f, pTexture, vbom, pString, pFont, maxCharacters);
	}
	
	public RiskaButtonSprite(float pWidth, float pHeight, ITiledTextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont)
	{
		this(pTexture, vbom, pString, pFont, Utils.maxNumericChars);
	}

	public RiskaButtonSprite(float pWidth, float pHeight, ITiledTextureRegion pTexture, VertexBufferObjectManager vbom)
	{	
		this(pWidth, pHeight, pTexture, vbom, null, null, Utils.maxNumericChars);
	}
	
	public RiskaButtonSprite(float pWidth, float pHeight, ITiledTextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont, int maxCharacters)
	{
		super(0f, 0f, pTexture, vbom);
		
		setSize(pWidth, pHeight);

		createText(pString, pFont, vbom, maxCharacters);
	}

	@Override
	public void setColor(Color pColor)
	{
		spriteColor = Utils.getColorWithAlpha(pColor, getAlpha());

		super.setColor(spriteColor);
	}

	// ==================================================
	// TEXT METHODS
	// ==================================================
	private void createText(String pString, Font pFont, VertexBufferObjectManager vbom, int maxCharacters)
	{
		maxChars = maxCharacters;
		
		if(pString != null)
		{
			
			text = new Text(0f, 0f, pFont, pString, maxChars, vbom);
			text.setColor(Color.WHITE);
			attachChild(text);
			
			wrapText();
		}
	}

	private void wrapText()
	{
		if(text != null)
		{
			Utils.wrap(text, this, textBoundingFactor);

			text.setPosition(Utils.halfX(this), Utils.halfY(this));
		}
	}
	
	private void updateText()
	{
		if(text != null)
		{
			text.setColor(textColor);
		}
	}
	
	public Text getText()
	{
		return text;
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
	
	public void setTextBoundingFactor(float pValue)
	{
		textBoundingFactor = pValue;
		
		wrapText();
	}

	public void setTextColor(Color pColor)
	{
		textColor = pColor;
		
		updateText();
	}

	// ==================================================
	// OVERRIDE
	// ==================================================
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
	public void fadeOut()
	{
		fadeOut(animationTime);
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
	public void fadeIn()
	{
		fadeIn(animationTime);
	}

	@Override
	public void rotate()
	{
		rotate(rotateAnimationTime, rotateStartingAngle, rotateEndingAngle);
	}
	
	@Override
	public void rotate(float pSpeed)
	{
		rotate(pSpeed, rotateStartingAngle, rotateEndingAngle);
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
	public boolean isVisible()
	{
		return (getAlpha() > 0) && super.isVisible();
	}

}
