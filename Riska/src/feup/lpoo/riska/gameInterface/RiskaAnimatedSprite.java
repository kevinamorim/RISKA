package feup.lpoo.riska.gameInterface;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import android.util.Log;
import feup.lpoo.riska.utilities.Utils;

public class RiskaAnimatedSprite extends Sprite {
	
	private DelayModifier waitForAnimation;
	
	private Text text;

	private RiskaAnimatedSprite top, bottom, left, right;

	private static final float textBoundingFactor = 0.55f;
	private static final float animationTime = 0.2f;

	// ==================================================
	// ==================================================
	public RiskaAnimatedSprite(ITextureRegion pTexture, VertexBufferObjectManager vbom)
	{
		this(pTexture, vbom, null, null, null, null, null, null);
	}
	
	public RiskaAnimatedSprite(ITextureRegion pTexture, VertexBufferObjectManager vbom,
			ITextureRegion pLeft, ITextureRegion pRight, ITextureRegion pTop, ITextureRegion pBottom)
	{
		this(pTexture, vbom, null, null, pLeft, pRight, pTop, pBottom);	
	}
	
	public RiskaAnimatedSprite(ITextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont)
	{
		this(pTexture, vbom, pString, pFont, null, null, null, null);
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
			left = new RiskaAnimatedSprite(pLeft, vbom);
			attachChild(left);
		}
		if(pRight != null)
		{
			right = new RiskaAnimatedSprite(pRight, vbom);
			attachChild(right);
		}
		if(pTop != null)
		{
			top = new RiskaAnimatedSprite(pTop, vbom);
			attachChild(top);
		}
		if(pBottom != null)
		{
			bottom = new RiskaAnimatedSprite(pBottom, vbom);
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

			text.setPosition(Utils.halfX(this), Utils.halfY(this));
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
			left.setPosition(Utils.halfX(left), Utils.halfY(this));
		}
		if(right != null)
		{
			right.setSize(0.1f * getWidth(), getHeight());
			right.setPosition(this.getWidth() - Utils.halfX(right), Utils.halfY(this));
		}
		if(top != null)
		{
			top.setSize(getWidth(), 0.25f * getHeight());
			top.setPosition(Utils.halfX(this), this.getHeight() - Utils.halfY(top));
		}
		if(bottom != null)
		{
			bottom.setSize(getWidth(), 0.25f * getHeight());
			bottom.setPosition(Utils.halfX(this), Utils.halfY(bottom));
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
			left.slideX(deltaTime, Utils.halfX(this));
		}
		if(right != null)
		{
			right.slideX(deltaTime, Utils.halfX(this));
		}
		if(top != null)
		{
			top.slideY(deltaTime, Utils.halfY(this));
		}
		if(bottom != null)
		{
			bottom.slideY(deltaTime, Utils.halfY(this));
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
			left.slideX(deltaTime, Utils.halfX(left));
		}
		if(right != null)
		{
			right.slideX(deltaTime, getWidth() - Utils.halfX(right));
		}
		if(top != null)
		{
			top.slideY(deltaTime, getHeight() - Utils.halfY(top));
		}
		if(bottom != null)
		{
			bottom.slideY(deltaTime, Utils.halfY(bottom));
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
	
	public void slideX(float duration, float from, float to)
	{
		MoveXModifier slideX = new MoveXModifier(duration, from, to);

		this.registerEntityModifier(slideX);
	}

	public void slideX(float duration, float to)
	{
		MoveXModifier slideX = new MoveXModifier(duration, getX(), to);

		this.registerEntityModifier(slideX);
	}

	public void slideY(float duration, float from, float to)
	{
		MoveYModifier slideY = new MoveYModifier(duration, from, to);

		this.registerEntityModifier(slideY);
	}

	public void slideY(float duration, float to)
	{
		MoveYModifier slideY = new MoveYModifier(duration, getY(), to);

		this.registerEntityModifier(slideY);
	}

	public void slide(float duration, float fromX, float fromY, float toX, float toY)
	{
		MoveModifier slide = new MoveModifier(duration, fromX, fromY, toX, toY);

		this.registerEntityModifier(slide);
	}

	public void slide(float duration, float toX, float toY)
	{
		MoveModifier slide = new MoveModifier(duration, getX(), getY(), toX, toY);

		this.registerEntityModifier(slide);
	}

}
