package feup.lpoo.riska.logic;

import org.andengine.entity.scene.menu.item.AnimatedSpriteMenuItem;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class AnimatedButtonSpriteMenuItem extends AnimatedSpriteMenuItem {
	
	public AnimatedButtonSpriteMenuItem(int pID, float pWidth, float pHeight, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		
		super(pID, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);
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
