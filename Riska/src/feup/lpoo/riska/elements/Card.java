package feup.lpoo.riska.elements;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

public class Card extends Element {
	
	public Sprite sprite;
	protected Text text;

	public Card(int x, int y, Sprite pSprite, String pName)
	{
		super(x,y,pName);
		
		this.sprite = pSprite;
		this.text = null;
	}
	
	public void setText(Text pText)
	{
		text = pText;
	}
}
