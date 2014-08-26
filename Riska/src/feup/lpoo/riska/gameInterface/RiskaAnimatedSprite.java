package feup.lpoo.riska.gameInterface;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import android.util.Log;
import feup.lpoo.riska.utilities.Utils;

public class RiskaAnimatedSprite extends RiskaSprite {
	
	private Text text;

	private RiskaSprite top, bottom, left, right;

	private static final float textBoundingFactor = 0.55f;
	private static final float animationTime = 0.2f;

	// ==================================================
	// ==================================================
	public RiskaAnimatedSprite(ITextureRegion pTexture, VertexBufferObjectManager vbom,
			ITextureRegion pLeft, ITextureRegion pRight, ITextureRegion pTop, ITextureRegion pBottom)
	{
		super(0f, 0f, pTexture, vbom);
		
		createBorders(pLeft, pRight, pTop, pBottom, vbom);	
		wrapBorders();
	}
	
	public RiskaAnimatedSprite(ITextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont)
	{
		super(0f, 0f, pTexture, vbom);

		createText(pString, pFont, vbom);
		wrapText();	
	}

	public RiskaAnimatedSprite(ITextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont,
			ITextureRegion pLeft, ITextureRegion pRight, ITextureRegion pTop, ITextureRegion pBottom)
	{
		super(0f, 0f, pTexture, vbom);


		createText(pString, pFont, vbom);
		wrapText();

		createBorders(pLeft, pRight, pTop, pBottom, vbom);	
		wrapBorders();
	}

	public RiskaAnimatedSprite(TiledTextureRegion pTexture, VertexBufferObjectManager vbom)
	{
		super(0f, 0f, pTexture, vbom);

		text = null;
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

	private void createBorders(ITextureRegion pLeft, ITextureRegion pRight,
			ITextureRegion pTop, ITextureRegion pBottom, VertexBufferObjectManager vbom)
	{

		if(pLeft != null)
		{
			left = new RiskaSprite(0, 0, pLeft, vbom);
			attachChild(left);
		}
		if(pRight != null)
		{
			right = new RiskaSprite(0, 0, pRight, vbom);
			attachChild(right);
		}
		if(pTop != null)
		{
			top = new RiskaSprite(0, 0, pTop, vbom);
			attachChild(top);
		}
		if(pBottom != null)
		{
			bottom = new RiskaSprite(0, 0, pBottom, vbom);
			attachChild(bottom);
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
		wrapBorders();
	}

	// ==================================================
	// METHODS
	// ==================================================

	private void wrapText()
	{
		if(text != null)
		{
			Utils.wrap(text, this, textBoundingFactor);

			text.setPosition(halfX(), halfY());
		}
	}

	private void wrapBorders()
	{
		/*
		 * Positions are relative to the origin of the parent sprite
		 */
		if(left != null)
		{
			left.setSize(0.1f * getWidth(), getHeight());
			left.setPosition(left.halfX(), halfY());
		}
		if(right != null)
		{
			right.setSize(0.1f * getWidth(), getHeight());
			right.setPosition(getWidth() - right.halfX(), halfY());
		}
		if(top != null)
		{
			top.setSize(getWidth(), 0.25f * getHeight());
			top.setPosition(halfX(), getHeight() - top.halfY());
		}
		if(bottom != null)
		{
			bottom.setSize(getWidth(), 0.25f * getHeight());
			bottom.setPosition(halfX(), bottom.halfY());
		}
	}

	public void close(float deltaTime)
	{
		if(text != null)
		{
			text.setVisible(false);
		}
		/*
		 * Positions are relative to the origin of the parent sprite
		 */
		if(left != null)
		{
			left.slideX(deltaTime, halfX());
		}
		if(right != null)
		{
			right.slideX(deltaTime, halfX());
		}
		if(top != null)
		{
			top.slideY(deltaTime, halfY());
		}
		if(bottom != null)
		{
			bottom.slideY(deltaTime, halfY());
		}
	}

	public void close()
	{
		close(animationTime);
	}

	public void open(float deltaTime)
	{
		if(text != null)
		{
			text.setVisible(true);
		}
		/*
		 * Positions are relative to the origin of the parent sprite
		 */
		if(left != null)
		{
			left.slideX(deltaTime, left.halfX());
		}
		if(right != null)
		{
			right.slideX(deltaTime, getWidth() - right.halfX());
		}
		if(top != null)
		{
			top.slideY(deltaTime, getHeight() - top.halfY());
		}
		if(bottom != null)
		{
			bottom.slideY(deltaTime, bottom.halfY());
		}
	}

	public void open()
	{
		open(animationTime);
	}

	public void animate()
	{
		final float newAnimationTime = 0.5f * animationTime;

		DelayModifier waitForAnim = new DelayModifier(newAnimationTime)
		{
			@Override
			protected void onModifierFinished(IEntity pItem)
			{
				open(newAnimationTime);
			}

		};
		registerEntityModifier(waitForAnim);

		close(newAnimationTime);
	}

	public void debug()
	{
		Log.d("Menu", "RiskaMenuItem [" + text.getText() + "]");
		Log.d("Menu", " >        Pos: [" + getX() + ", " + getY() + "]");
		Log.d("Menu", " >   Text Pos: [" + text.getX() + ", " + text.getY() + "]");
		Log.d("Menu", " >   Left Pos: [" + left.getX() + ", " + left.getY() + "]");
		Log.d("Menu", " >  Right Pos: [" + right.getX() + ", " + right.getY() + "]");
		Log.d("Menu", " >    Top Pos: [" + top.getX() + ", " + top.getY() + "]");
		Log.d("Menu", " > Bottom Pos: [" + bottom.getX() + ", " + bottom.getY() + "]");
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

}
