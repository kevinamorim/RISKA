package feup.lpoo.riska.gameInterface;

import org.andengine.entity.scene.menu.item.AnimatedSpriteMenuItem;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.graphics.Color;

public class AnimatedTextButtonSpriteMenuItem extends AnimatedSpriteMenuItem {
	
	private float textX = 0;
	private float textY = 0;
	private Text buttonText;
	
	public AnimatedTextButtonSpriteMenuItem(int pID, float pWidth, float pHeight, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, String pText, Font pFont) {
		
		super(pID, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);
		
		buttonText = new Text(0, 0, pFont, pText, pVertexBufferObjectManager);
		
		textX = this.getWidth()/2;
		textY = this.getHeight()/2;
		
		buttonText.setPosition(textX, textY);
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
	
	public void setText(String pText) {
		buttonText.setText(pText);	
	}

}
