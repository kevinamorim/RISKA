package feup.lpoo.riska.gameInterface;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.utilities.Utils;

public class RiskaSprite extends Sprite {

	private Text text;

	private static final float textBoundingFactor = 0.55f;
	private static final float animationTime = 0.2f;

	// ==================================================
	// ==================================================
	public RiskaSprite(ITextureRegion pTexture, VertexBufferObjectManager vbom)
	{
		this(pTexture, vbom, null, null);
	}

	public RiskaSprite(ITextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont)
	{
		super(0f, 0f, pTexture, vbom);


		createText(pString, pFont, vbom);
		wrapText();
	}

	private void createText(String pString, Font pFont, VertexBufferObjectManager vbom)
	{
		if(pString != null)
		{
			text = new Text(0f, 0f, pFont, pString, 100, vbom);
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

	public void fadeOut()
	{
		fadeOut(animationTime);
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

	public void fadeIn()
	{
		fadeIn(animationTime);
	}

	public void animate()
	{
		final float newAnimationTime = 0.5f * animationTime;

		DelayModifier waitForAnim = new DelayModifier(newAnimationTime)
		{
			@Override
			protected void onModifierFinished(IEntity pItem)
			{
				fadeIn(newAnimationTime);
			}

		};
		registerEntityModifier(waitForAnim);

		fadeOut(newAnimationTime);
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
	public void setColor(Color pColor)
	{
		for(int i = 0; i < getChildCount(); i++)
		{
			IEntity e = this.getChildByIndex(i);

			e.setColor(pColor);
		}

		super.setColor(pColor);
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
}
