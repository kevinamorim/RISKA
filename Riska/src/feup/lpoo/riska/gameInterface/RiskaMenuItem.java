package feup.lpoo.riska.gameInterface;

import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.utilities.Utils;

public class RiskaMenuItem extends Sprite implements IMenuItem {

	private int pID;
	private Text text;
	
	private static final float textBoundingFactor = 0.55f;

	public RiskaMenuItem(int ID, ITextureRegion pTexture, VertexBufferObjectManager vbom, String pString, Font pFont)
	{
		super(0f, 0f, pTexture, vbom);

		pID = ID;

		text = new Text(0f, 0f, pFont, pString, 100, vbom);
		text.setColor(Color.WHITE);
		
		if(Utils.outOfBounds(text, this, textBoundingFactor))
		{
			Utils.wrap(text, this, textBoundingFactor);
		}
		text.setPosition(0.5f * getWidth(), 0.5f * getHeight());	
		
		attachChild(text);
	}

	public RiskaMenuItem(int ID, TiledTextureRegion pTexture, VertexBufferObjectManager vbom)
	{
		super(0f, 0f, pTexture, vbom);
		
		pID = ID;
		
		text = null;
	}
	
	@Override
	public void setSize(float pWidth, float pHeight)
	{
		super.setSize(pWidth, pHeight);
		
		if(text != null)
		{
			if(Utils.outOfBounds(text, this, textBoundingFactor))
			{
				Utils.wrap(text, this, textBoundingFactor);
			}
			
			text.setPosition(0.5f * getWidth(), 0.5f * getHeight());
		}		
	}

	@Override
	public int getID()
	{
		return this.pID;
	}

	@Override
	public void onSelected()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onUnselected()
	{
		// TODO Auto-generated method stub

	}

}
