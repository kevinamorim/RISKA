package feup.lpoo.riska;

import org.andengine.entity.scene.menu.item.AnimatedSpriteMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.region.color.RegionColor;
import feup.lpoo.riska.region.color.TransparencyMask;
import android.graphics.Point;

public class Region extends AnimatedSpriteMenuItem {

	protected RegionColor color;
	protected Point stratCenter;
	
	protected int width;
	protected int height;
	protected float posX;

	protected float posY;

	protected boolean selected;

	protected boolean owned;
	
	protected TransparencyMask mask;
	
	//protected Sprite sprite;
	
	public Region(int pID, float width, float height,
			float posX, float posY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		
		super(pID, width, height, pTiledTextureRegion, pVertexBufferObjectManager);
		
		this.posX = posX;
		this.posY = posY;
		
		setColor(Color.RED); /* Cor inicial */
	}
	

	@Override
	public void onSelected() {
		this.setColor(Color.GREEN);
		super.onSelected();
	}



	@Override
	public void onUnselected() {
		this.setColor(Color.RED);
		super.onUnselected();
	}


	
//	public void toggle() {
//		selected = !selected;
//		
//		if(selected) {
//			sprite.setColor(Color.RED);
//		} else {
//			sprite.setColor(Color.GREEN);
//		}
//		
//	}
	
	/* ======================================================================
	 * ======================================================================    
	 *                          GETTERS & SETTERS 
	 * ======================================================================    
	 * ======================================================================    
	 */

//	public RegionColor getColor() {
//		return color;
//	}
//
//	public int getWidth() {
//		return width;
//	}
//
//	public void setWidth(int width) {
//		this.width = width;
//	}
//
//	public int getHeight() {
//		return height;
//	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setRegionColor(RegionColor color) {
		this.color = color;
	}
	
	public void setStratCenter(int x, int y) {
		this.stratCenter.set(x, y);
	}
	
	public Point getStratCenter() {
		return this.stratCenter;
	}

	public TransparencyMask getMask() {
		return mask;
	}

	public void setMask(TransparencyMask mask) {
		this.mask = mask;
	}
	
	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}


	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	/**
	 * @return the owned
	 */
	public boolean isOwned() {
		return owned;
	}


	/**
	 * @param owned the owned to set
	 */
	public void setOwned(boolean owned) {
		this.owned = owned;
	}
	
	/**
	 * @return the posX
	 */
	public float getPosX() {
		return posX;
	}


	/**
	 * @param posX the posX to set
	 */
	public void setPosX(float posX) {
		this.posX = posX;
	}

	
	/**
	 * @return the posY
	 */
	public float getPosY() {
		return posY;
	}


	/**
	 * @param posY the posY to set
	 */
	public void setPosY(float posY) {
		this.posY = posY;
	}




}
