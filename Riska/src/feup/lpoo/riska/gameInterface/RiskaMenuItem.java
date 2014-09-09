package feup.lpoo.riska.gameInterface;

import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import feup.lpoo.riska.utilities.Utils;

public class RiskaMenuItem extends RiskaSprite implements IMenuItem {

	// ==================================================
	// FIELDS
	// ==================================================
	private int pID;
	
	public RiskaMenuItem(int ID, ITextureRegion pTexture, VertexBufferObjectManager vbom)
	{
		this(ID, pTexture, vbom, null, null, 0);
	}
	
	public RiskaMenuItem(int ID, ITextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont)
	{
		super(pTexture, vbom, pString, pFont, Utils.maxNumericChars);
		
		this.pID = ID;
	}
	
	public RiskaMenuItem(int ID, ITextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont, int maxChars)
	{
		super(pTexture, vbom, pString, pFont, maxChars);
		
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
