package feup.lpoo.riska;

import org.andengine.entity.sprite.Sprite;

public class GoalCard extends Card {
	
	protected String description;

	public GoalCard(float x, float y, Sprite t, String name, String descr) {
		super(x, y, t, name);
		this.description = descr;
	}

	public GoalCard(float x, float y, String TAG, Sprite t, String name, String descr) {
		super(x, y, TAG, t, name);
		this.description = descr;
	}
	
	public boolean isCompleted() {
		// TODO Must analyze the game and return true or false.
		return false;
	}

}
