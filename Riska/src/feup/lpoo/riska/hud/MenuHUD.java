package feup.lpoo.riska.hud;

import org.andengine.engine.camera.hud.HUD;

import feup.lpoo.riska.interfaces.Displayable;

public class MenuHUD extends HUD implements Displayable {

	// ==================================================
	// CONSTANTS
	// ==================================================
	public static final float doorsAnimationTime = 0.6f;
	public static final float doorsAnimationWaitingTime = 0.6f;
	
	// ==================================================
	// FIELDS
	// ==================================================
	
	public MenuHUD()
	{
		createDisplay();
	}
	
	@Override
	public void createDisplay()
	{
	}
}
