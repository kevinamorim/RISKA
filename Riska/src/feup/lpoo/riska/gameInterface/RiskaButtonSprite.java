package feup.lpoo.riska.gameInterface;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.utilities.Utils;

public class RiskaButtonSprite extends ButtonSprite {

	private Color spriteColor = Utils.OtherColors.GREY;

	public RiskaButtonSprite(float pWidth, float pHeight, ITiledTextureRegion pTexture, VertexBufferObjectManager vbom)
	{	
		super(0f, 0f, pTexture, vbom);
		
		setSize(pWidth, pHeight);
	}

	
	public RiskaButtonSprite(ITiledTextureRegion pTexture, VertexBufferObjectManager vbom)
	{
		this(0f, 0f, pTexture, vbom);
	}

	private void update()
	{
		setColor(spriteColor);
	}

	@Override
	public void setColor(Color pColor)
	{
		spriteColor = Utils.getColorWithAlpha(pColor, getAlpha());
		
		super.setColor(spriteColor);
	}

}
