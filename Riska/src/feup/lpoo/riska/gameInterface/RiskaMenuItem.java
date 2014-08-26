package feup.lpoo.riska.gameInterface;

import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class RiskaMenuItem extends RiskaSprite implements IMenuItem {

	// ==================================================
	// FIELDS
	// ==================================================
	private int pID;
	
	public RiskaMenuItem(int ID, ITextureRegion pTexture, VertexBufferObjectManager vbom,
			ITextureRegion pLeft, ITextureRegion pRight, ITextureRegion pTop, ITextureRegion pBottom)
	{
		this(ID, pTexture, vbom, null, null, pLeft, pRight, pTop, pBottom);
	}

	public RiskaMenuItem(int ID, ITextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont)
	{
		this(ID, pTexture, vbom, pString, pFont, null, null, null, null);
	}
	
	public RiskaMenuItem(int ID, ITextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont,
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
