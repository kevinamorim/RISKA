package feup.lpoo.riska.hud;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;

import feup.lpoo.riska.gameInterface.CameraManager;
import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.utilities.Utils;

public class MenuHUD extends HUD implements Displayable {

	// ==================================================
	// CONSTANTS
	// ==================================================
	public static final float doorsAnimationTime = 0.6f;
	public static final float doorsAnimationWaitingTime = 0.6f;
	
	private ResourceCache resources;
	private CameraManager camera;
	
	// ==================================================
	// FIELDS
	// ==================================================
	
	private float leftDoorOpenX, rightDoorOpenX;
	private float leftBarOpenX, rightBarOpenX;
	
	private float leftDoorCloseX, rightDoorCloseX;
	private float leftBarCloseX, rightBarCloseX;
	
	public MenuHUD()
	{
		resources = ResourceCache.instance;
		
		camera = resources.camera;

		createDisplay();
	}
	
	@Override
	public void createDisplay()
	{
	}
}
