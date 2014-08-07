package feup.lpoo.riska.gameInterface;

import org.andengine.entity.scene.menu.item.AnimatedSpriteMenuItem;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import feup.lpoo.riska.utilities.Utils;
import android.graphics.Color;

/**
 * Simple class that extends the AnimatedSpriteMenuItem class into tiled buttons with text.
 * 
 * @see AnimatedSpriteMenuItem
 */
public class AnimatedTextButtonSpriteMenuItem extends AnimatedSpriteMenuItem {

	private Text buttonText;
	private Font buttonFont;
	
	private float textBoundingFactor = 0.80f;	// Text should never measure beyond 90% of the button size
	
	public AnimatedTextButtonSpriteMenuItem(int pID, float pWidth, float pHeight, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager vbom, String pString, Font pFont) {
		
		super(pID, pWidth, pHeight, pTiledTextureRegion, vbom);
		
		buttonFont = pFont;
		
		buttonText = new Text(0f, 0f, buttonFont, pString, vbom);
		
		if(Utils.outOfBounds(buttonText, this, textBoundingFactor))
		{
			Utils.wrap(buttonText, this, textBoundingFactor);
		}
		buttonText.setPosition(0.5f * getWidth(), 0.5f * getHeight());	
		buttonText.setColor(Color.BLACK);
		
		attachChild(buttonText);
	}
	
	@Override
	public void onSelected() {
		
		buttonText.setColor(Color.DKGRAY);
		setCurrentTileIndex(1);
		super.onSelected();
		
	}
	
	@Override
	public void onUnselected() {
		
		buttonText.setColor(Color.BLACK);
		setCurrentTileIndex(0);
		super.onUnselected();
		
	}
	
	public void setText(String pText)
	{
		buttonText.setText(pText);	
	}
	
	
	@Override
	public void setSize(float pWidth, float pHeight)
	{
		super.setSize(pWidth, pHeight);
		
		if(buttonText != null)
		{
			if(Utils.outOfBounds(buttonText, this, textBoundingFactor))
			{
				Utils.wrap(buttonText, this, textBoundingFactor);
			}
			
			buttonText.setPosition(0.5f * getWidth(), 0.5f * getHeight());
		}		
	}
	
	public void setTextScale(float newScale)
	{
		textBoundingFactor = newScale;
		
		if(buttonText != null)
		{
			Utils.wrap(buttonText, this, textBoundingFactor);
			
			buttonText.setPosition(0.5f * getWidth(), 0.5f * getHeight());
		}
	}
}
