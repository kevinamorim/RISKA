package feup.lpoo.riska;

import org.andengine.entity.scene.menu.item.AnimatedSpriteMenuItem;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class AnimatedButtonSpriteMenuItem extends AnimatedSpriteMenuItem {
	
	private float textX = 0;
	private float textY = 0;
	private Text buttonText;
	
	public AnimatedButtonSpriteMenuItem(int pID, float pWidth, float pHeight, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, String pText, Font pFont) {
		
		super(pID, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);
		
		buttonText = new Text(0, 0, pFont, pText, pVertexBufferObjectManager);
		
		textX = this.getWidth()/2;
		textY = this.getHeight()/2;
		
		buttonText.setPosition(textX, textY);
		
		attachChild(buttonText);
		
	}
	
	@Override
	public void onSelected() {
		
		setCurrentTileIndex(1);
		super.onSelected();
		
	}
	
	@Override
	public void onUnselected() {
		
		setCurrentTileIndex(0);
		super.onUnselected();
		
	}

}
