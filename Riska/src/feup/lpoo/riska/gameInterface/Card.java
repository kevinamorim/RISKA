package feup.lpoo.riska.gameInterface;

import org.andengine.entity.sprite.Sprite;

import feup.lpoo.riska.elements.Element;

public class Card extends Element {
	
	protected Sprite template;
	
	public Card(int x, int y, Sprite t, String name) {
		super(x,y,name);
		this.template = t;
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