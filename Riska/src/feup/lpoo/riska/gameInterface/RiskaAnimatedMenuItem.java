package feup.lpoo.riska.gameInterface;

import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class RiskaAnimatedMenuItem extends RiskaAnimatedSprite implements IMenuItem {

	// ==================================================
	// FIELDS
	// ==================================================
	private int pID;

	public RiskaAnimatedMenuItem(int ID, ITextureRegion pTexture,
			VertexBufferObjectManager vbom, String pString, Font pFont)
	{
		super(pTexture, vbom, pString, pFont);

		this.pID = ID;
	}
	
	public RiskaAnimatedMenuItem(int ID, ITextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont,
			ITextureRegion pLeft, ITextureRegion pRight, ITextureRegion pTop, ITextureRegion pBottom)
	{
		super(pTexture, vbom, pString, pFont, pLeft, pRight, pTop, pBottom);
		
		this.pID = ID;
	}
	
	@Override
	public int getID()
	{
		return this.pID;
	}

	@Override
	public void onSelected() { }

	@Override
	public void onUnselected() { }

}
