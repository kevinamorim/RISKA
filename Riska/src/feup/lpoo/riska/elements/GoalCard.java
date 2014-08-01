package feup.lpoo.riska.elements;

import org.andengine.entity.sprite.Sprite;

public class GoalCard extends Card {
	
	protected String description;

	public GoalCard(int x, int y, Sprite pSprite, String pName, String descr)
	{
		super(x, y, pSprite, pName);
		
		this.description = descr;
	}
	
	public boolean isCompleted(Player player)
	{
		return false;
	}

}
