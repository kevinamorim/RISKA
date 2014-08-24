package feup.lpoo.riska.gameInterface;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class RiskaSprite extends Sprite {
	
	public RiskaSprite(float pX, float pY, float pWidth, float pHeight, ITextureRegion pTextureRegion, VertexBufferObjectManager vbom)
	{
		super(pX, pY, pWidth, pHeight, pTextureRegion, vbom);
	}
	
	public RiskaSprite(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager vbom)
	{
		super(pX, pY, pTextureRegion, vbom);
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

	public float left()
	{
		return this.getX() - 0.5f * this.getWidth();
	}
	
	public float right()
	{
		return this.getX() + 0.5f * this.getWidth();
	}
	
	public float top()
	{
		return this.getY() + 0.5f * this.getHeight();
	}
	
	public float bottom()
	{
		return this.getY() - 0.5f * this.getHeight();
	}

	public float halfX()
	{
		return 0.5f * this.getWidth();
	}
	
	public float halfY()
	{
		return 0.5f * this.getHeight();
	}
}
