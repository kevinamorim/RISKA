package feup.lpoo.riska.gameInterface;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.utilities.Utils;

public class RiskaButtonSprite extends ButtonSprite {

	private Color spriteColor = Utils.OtherColors.GREY;
	
	public void setColor(Color pColor, float pAlpha)
	{
		spriteColor = Utils.getColorWithAlpha(pColor, pAlpha);
		
		update();	
	}

	public RiskaButtonSprite(ITiledTextureRegion pTexture, VertexBufferObjectManager vbom)
	{	
		this(0f, 0f, pTexture, vbom);
	}

	public RiskaButtonSprite(float pX, float pY, ITiledTextureRegion pTexture, VertexBufferObjectManager vbom)
	{	
		super(pX, pY, pTexture, vbom);
	}

	
	private void update()
	{
		setColor(spriteColor);
	}
}
