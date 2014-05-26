package feup.lpoo.riska;

import org.andengine.entity.sprite.Sprite;

public class Button extends Element {
	
	protected Sprite template;
	protected String text;
	
	public Button(float x, float y) {
		super(x,y);
		this.template = null;
		this.text = null;
	}
	
	public Button(float x, float y, Sprite t, String text) {
		super(x,y);
		this.template = t;
		this.text = text;
	}

	public Sprite getTemplate() {
		return template;
	}

	public void setTemplate(Sprite template) {
		this.template = template;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
