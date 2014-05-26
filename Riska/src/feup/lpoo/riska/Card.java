package feup.lpoo.riska;

import org.andengine.entity.sprite.Sprite;

public class Card extends Element {
	
	protected Sprite template;
	protected String name;
	
	public Card(float x, float y, Sprite t, String name) {
		super(x,y);
		this.template = t;
		this.name = name;
	}
	
	public Card(float x, float y, String TAG, Sprite t, String name) {
		super(x,y,TAG);
		this.template = t;
		this.name = name;
	}

	public Sprite getTemplate() {
		return template;
	}

	public void setTemplate(Sprite template) {
		this.template = template;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
