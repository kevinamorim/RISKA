package feup.lpoo.riska.gameInterface;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.resources.ResourceCache;

public class MenuHUD extends HUD implements Displayable {

	// ==================================================
	// CONSTANTS
	// ==================================================
	public static final float doorsAnimationTime = 0.6f;
	public static final float doorsAnimationWaitingTime = 1f;
	
	private ResourceCache resources;
	private CameraManager camera;
	// ==================================================
	// FIELDS
	// ==================================================

	private RiskaSprite doorLeft, doorRight;
	//private Sprite border;
	
	private float leftDoorOpenX, rightDoorOpenX;
	private float leftDoorCloseX, rightDoorCloseX;
	
	
	public MenuHUD()
	{
		resources = ResourceCache.getSharedInstance();
		
		camera = resources.camera;

		createDisplay();
	}
	
	@Override
	public void createDisplay()
	{		
		createDoors();
	}

	private void createDoors()
	{
		doorLeft = new RiskaSprite(0, 0, resources.doorLeftRegion, resources.vbom);
		doorRight = new RiskaSprite(0, 0, resources.doorRightRegion, resources.vbom);
		
		doorLeft.setPosition(0.25f * camera.getWidth(), 0.5f * camera.getHeight());
		doorLeft.setSize(0.5f * camera.getWidth(), 1f * camera.getHeight());

		doorRight.setPosition(0.75f * camera.getWidth(), 0.5f * camera.getHeight());
		doorRight.setSize(0.5f * camera.getWidth(), 1f * camera.getHeight());	
		
		leftDoorOpenX = -0.25f * camera.getWidth() + 0.1f * doorLeft.getWidth();
		rightDoorOpenX = 1.25f * camera.getWidth() - 0.1f * doorRight.getWidth();
		leftDoorCloseX = 0.25f * camera.getWidth()/* + 0.1f * doorLeft.getWidth()*/;
		rightDoorCloseX = 0.75f * camera.getWidth()/* - 0.1f * doorRight.getWidth()*/;
		
		attachChild(doorLeft);
		attachChild(doorRight);
	}
	
	public void openSlideDoors()
	{	
		doorLeft.slideX(doorsAnimationTime, leftDoorOpenX);
		doorRight.slideX(doorsAnimationTime, rightDoorOpenX);
	}

	public void closeSlideDoors()
	{
		doorLeft.slideX(doorsAnimationTime, leftDoorCloseX);
		doorRight.slideX(doorsAnimationTime, rightDoorCloseX);
	}

	public void animateSlideDoors()
	{	
		DelayModifier waitForClosing = new DelayModifier(doorsAnimationWaitingTime)
		{
			@Override
			protected void onModifierFinished(IEntity pItem)
			{
				openSlideDoors();
			}

		};
		registerEntityModifier(waitForClosing);
		
		closeSlideDoors();
	}

}
