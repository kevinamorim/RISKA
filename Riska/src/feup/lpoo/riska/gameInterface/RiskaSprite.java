package feup.lpoo.riska.gameInterface;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class RiskaSprite extends Sprite {

	public RiskaSprite(ITextureRegion pTexture, VertexBufferObjectManager vbom)
	{
		super(0f, 0f, pTexture, vbom);
		// TODO Auto-generated constructor stub
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
