package feup.lpoo.riska.gameInterface;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

public class RiskaButtonSprite extends ButtonSprite {

	public void setColor(Color pColor, float pAlpha)
	{
		setColor(new Color(pColor.getRed(), pColor.getGreen(), pColor.getBlue(), pAlpha));
	}
	
	public RiskaButtonSprite(ITiledTextureRegion pTexture, VertexBufferObjectManager vbom)
	{	
		this(0f, 0f, pTexture, vbom);
	}

	public RiskaButtonSprite(float pX, float pY, ITiledTextureRegion pTexture, VertexBufferObjectManager vbom)
	{	
		super(pX, pY, pTexture, vbom);
	}

}
