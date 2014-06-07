package feup.lpoo.riska.gameInterface;

import org.andengine.entity.sprite.Sprite;

import feup.lpoo.riska.elements.Element;

public class Button extends Element {
	
	protected Sprite template;
	protected String text;
	
	public Button(int x, int y, Sprite t, String text) {
		super(x,y, null);
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
