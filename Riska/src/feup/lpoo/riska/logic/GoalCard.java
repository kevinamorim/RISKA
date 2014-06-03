package feup.lpoo.riska.logic;

import org.andengine.entity.sprite.Sprite;

public class GoalCard extends Card {
	
	protected String description;

	public GoalCard(int x, int y, Sprite t, String name, String descr) {
		super(x, y, t, name);
		this.description = descr;
	}
	
	public boolean isCompleted() {
		// TODO Must analyze the game and return true or false.
		return false;
	}

}
