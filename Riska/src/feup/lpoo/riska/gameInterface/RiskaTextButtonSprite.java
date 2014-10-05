package feup.lpoo.riska.gameInterface;

import org.andengine.entity.IEntity;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.utilities.Utils;

public class RiskaTextButtonSprite extends RiskaButtonSprite
{
	private Text text;

	private static float textBoundingFactor = 0.8f;
	
	public Color textColor = Color.PINK;

	// ==================================================
	// ==================================================	
	public RiskaTextButtonSprite(ITiledTextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont)
	{
		this(pTexture, vbom, pString, pFont, Utils.maxNumericChars);
	}

	public RiskaTextButtonSprite(ITiledTextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont, int maxCharacters)
	{
		super(pTexture, vbom);


		createText(pString, pFont, vbom, maxCharacters);
		wrapText();
	}

	private void createText(String pString, Font pFont, VertexBufferObjectManager vbom, int maxCharacters)
	{
		if(pString != null)
		{
			text = new Text(0f, 0f, pFont, pString, maxCharacters, vbom);
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
	
	private void updateText()
	{
		if(text != null)
		{
			text.setColor(textColor);
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
	public boolean isVisible()
	{
		return (getAlpha() > 0) && super.isVisible();
	}

}
