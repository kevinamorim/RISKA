package feup.lpoo.riska.gameInterface;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.interfaces.Animated;
import feup.lpoo.riska.utilities.Utils;

public class UIElement extends Entity implements Animated {

	VertexBufferObjectManager vbom;

	private Text text;
	private RiskaButtonSprite[] sprites;

	private float textBoundingFactorX = 1f;
	private float textBoundingFactorY = 1f;
	
	private Color spriteColor = Color.WHITE;
	private Color textColor = Color.BLACK;

	public UIElement(float pSize, ITiledTextureRegion pTexture, VertexBufferObjectManager pVbom)
	{
		this(pSize, pSize, pTexture, pVbom);
	}
	
	public UIElement(float pWidth, float pHeight, ITiledTextureRegion pTexture, VertexBufferObjectManager pVbom)
	{
		super(0f, 0f, pWidth, pHeight);
		
		setSprite(pTexture);

		this.vbom = pVbom;
	}

	public void setSprite(ITiledTextureRegion pTiledTexture)
	{
		if(sprites == null)
		{
			sprites = new RiskaButtonSprite[9];
		}
		else
		{
			for(RiskaButtonSprite spr : sprites)
			{
				spr.detachSelf();
			}
		}
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				int index = j + i*3;
				
				sprites[index] = new RiskaButtonSprite(pTiledTexture.getWidth(), pTiledTexture.getWidth(), pTiledTexture, vbom);

				Utils.wrap(sprites[index], this, 1/3f);
				
				sprites[index].setCurrentTileIndex(6 + j - 3*i);

				attachChild(sprites[index]);
			}	
		}

		wrapTiles();
	}

	public void setText(String pText, IFont pFont)
	{
		if(text == null)
		{	
			text = new Text(Utils.getCenterX(this), Utils.getCenterY(this), pFont, pText, Utils.maxTextChars, vbom);
			attachChild(text);
		}
		else
		{
			text = new Text(Utils.getCenterX(this), Utils.getCenterY(this), pFont, pText, Utils.maxTextChars, vbom);
		}

		wrapText();
	}

	private void wrapText()
	{
		if(text != null)
		{	
			Utils.wrapX(text, this, textBoundingFactorX);
			Utils.wrapY(text, this, textBoundingFactorY);

			text.setPosition(Utils.halfX(this), Utils.halfY(this));
		}
	}
	
	public void setTextBoundingFactor(float pX, float pY)
	{
		this.textBoundingFactorX = pX;
		this.textBoundingFactorY = pY;
		
		wrapText();
	}

	// ==================================================
	// ==================================================
	@Override
	public void setSize(float pWidth, float pHeight)
	{
		super.setSize(pWidth, pHeight);

		if(text != null)
		{
			wrapText();
		}

		if(sprites != null)
		{
			wrapTiles();
		}
	}

	private void wrapTiles()
	{
		// CORNERS	
		float scale = Utils.getWrapScale(sprites[0], this, 1/3f);

		sprites[0].setSize(scale * sprites[0].getWidth(), scale * sprites[0].getHeight());
		sprites[0].setPosition(Utils.halfX(sprites[0]), Utils.halfY(sprites[0]));

		sprites[2].setSize(sprites[0].getWidth(), sprites[0].getHeight());
		sprites[2].setPosition(Utils.rightLocal(this) - Utils.halfX(sprites[2]), Utils.halfY(sprites[2]));

		sprites[6].setSize(sprites[0].getWidth(), sprites[0].getHeight());
		sprites[6].setPosition(Utils.halfX(sprites[6]), Utils.topLocal(this) - Utils.halfY(sprites[6]));

		sprites[8].setSize(sprites[0].getWidth(), sprites[0].getHeight());
		sprites[8].setPosition(Utils.rightLocal(this) - Utils.halfX(sprites[8]), Utils.topLocal(this) - Utils.halfY(sprites[8]));

		// MIDDLE
		sprites[4].setSize(Utils.leftGlobal(sprites[2]) - Utils.rightGlobal(sprites[0]), Utils.bottomGlobal(sprites[6]) - Utils.topGlobal(sprites[0]));
		sprites[4].setPosition(Utils.halfX(this), Utils.halfY(this));

		// EDGES
		sprites[1].setSize(sprites[4].getWidth(), sprites[0].getHeight());
		sprites[1].setPosition(sprites[4].getX(), sprites[0].getY());

		sprites[3].setSize(sprites[0].getWidth(), sprites[4].getHeight());
		sprites[3].setPosition(sprites[0].getX(), sprites[4].getY());

		sprites[5].setSize(sprites[0].getWidth(), sprites[4].getHeight());
		sprites[5].setPosition(sprites[8].getX(), sprites[4].getY());

		sprites[7].setSize(sprites[4].getWidth(), sprites[8].getHeight());
		sprites[7].setPosition(sprites[4].getX(), sprites[8].getY());
	}

	@Override
	public void setScale(float pScale)
	{
		for(int i = 0; i < getChildCount(); i++)
		{
			IEntity e = getChildByIndex(i);

			e.setScale(pScale);
		}

		super.setScale(pScale);
	}
	
	public void setSpriteAlpha(float pAlpha)
	{
		if(sprites != null)
		{
			for(TiledSprite spr: sprites)
			{
				spr.setAlpha(pAlpha);
			}
		}
	}

	public void setSpriteColor(Color pColor)
	{
		this.spriteColor = pColor;
		
		updateSprite();
	}

	public void setTextColor(Color pColor)
	{
		this.textColor = pColor;
		
		updateText();
	}

	public void setColorChildren(Color pColor)
	{
		for(int i = 0; i < getChildCount(); i++)
		{
			IEntity e = getChildByIndex(i);

			e.setColor(pColor);
		}
	}
	
	@Override
	public void setColor(Color pColor)
	{
		setSpriteColor(pColor);
	}

	private void updateSprite()
	{
		if(sprites != null)
		{
			for(TiledSprite spr : sprites)
			{
				spr.setColor(Utils.getColorWithAlpha(spriteColor, spr.getAlpha()));
			}
		}
	}
	
	private void updateText()
	{
		if(text != null)
		{
			text.setColor(textColor);
		}
	}

	// ==================================================
	// 'Animated' interface
	// ==================================================
	@Override
	public void fadeOut(float deltaTime)
	{
		for(RiskaButtonSprite spr : sprites)
		{
			spr.fadeOut(deltaTime);
		}
	}

	@Override
	public void fadeIn(float deltaTime)
	{
		for(RiskaButtonSprite spr : sprites)
		{
			spr.fadeIn(deltaTime);
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
		for(RiskaButtonSprite spr : sprites)
		{
			spr.rotate(pSpeed, pStartingAngle, pEndingAngle);
		}
	}

	@Override
	public void stopRotation()
	{
		for(RiskaButtonSprite spr : sprites)
		{
			spr.stopRotation();
		}
	}

	@Override
	public void fadeOutAndStopRotation(float deltaTime)
	{
		for(RiskaButtonSprite spr : sprites)
		{
			spr.fadeOutAndStopRotation(deltaTime);
		}
	}

}
