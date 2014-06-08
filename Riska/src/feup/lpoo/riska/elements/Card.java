package feup.lpoo.riska.elements;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Class that represents a game card.
 */
public class Card extends Element {
	
	protected Sprite template;
	protected Text text;
	
	/**
	 * Constructor for a card.
	 * 
	 * @param x : x coordinate
	 * @param y : y coordinate
	 * @param t : 
	 * @param name
	 */
	public Card(int x, int y, Sprite pSprite, String pName) {
		super(x,y,pName);
		
		this.template = pSprite;
		this.text = null;
	}

	/**
	 * @return The card's sprite
	 */
	public Sprite getSprite() {
		return template;
	}
	
	/**
	 * Sets the text for this sprite.
	 * 
	 * @param pText : text to set
	 * @param pFont : Font
	 * @param pBuffer : VertexBufferObjectManager
	 */
	public void setText(String pText, Font pFont, VertexBufferObjectManager pBuffer) {
		if(text == null) {
			// TODO : change this
			text = new Text(0, 0, pFont, pText, 1000, pBuffer);
		}
	}

}
