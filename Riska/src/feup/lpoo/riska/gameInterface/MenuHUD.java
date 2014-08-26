package feup.lpoo.riska.gameInterface;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;

import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.utilities.Utils;

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
	private RiskaAnimatedSprite leftDoor, rightDoor;
	private RiskaAnimatedSprite leftBar, rightBar;
	//private Sprite border;
	
	private float leftDoorOpenX, rightDoorOpenX;
	private float leftBarOpenX, rightBarOpenX;
	
	private float leftDoorCloseX, rightDoorCloseX;
	private float leftBarCloseX, rightBarCloseX;
	
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
		float doorsHeight = camera.getHeight();
		float doorsWidth = 0.54f * camera.getWidth();
		
		leftDoor = new RiskaAnimatedSprite(resources.doorLeftRegion, resources.vbom);
		rightDoor = new RiskaAnimatedSprite(resources.doorRightRegion, resources.vbom);
		leftBar = new RiskaAnimatedSprite(resources.barVRegion, resources.vbom);
		rightBar = new RiskaAnimatedSprite(resources.barVRegion, resources.vbom);
		
		leftDoor.setSize(doorsWidth, doorsHeight);
		
		leftBar.setSize(0.03f * camera.getWidth(), camera.getHeight());

		rightDoor.setSize(doorsWidth, doorsHeight);	
		
		rightBar.setSize(0.03f * camera.getWidth(), camera.getHeight());
		
		leftDoorOpenX = -Utils.halfX(leftDoor) + leftBar.getWidth();
		rightDoorOpenX = camera.getWidth() + Utils.halfX(rightDoor) - rightBar.getWidth();
		leftBarOpenX = Utils.halfX(leftBar);
		rightBarOpenX = camera.getWidth() - Utils.halfX(rightBar);
		
		leftDoorCloseX = Utils.halfX(leftDoor);
		rightDoorCloseX = camera.getWidth() - Utils.halfX(rightDoor);
		leftBarCloseX = camera.getCenterX();
		rightBarCloseX = camera.getCenterX();
		
		leftDoor.setPosition(leftDoorOpenX, camera.getCenterY());
		leftBar.setPosition(leftBarOpenX, camera.getCenterY());
		
		rightDoor.setPosition(rightDoorOpenX, camera.getCenterY());
		rightBar.setPosition(rightBarOpenX, camera.getCenterY());
		
		//doorLeft.setAlpha(0.6f);
		//doorRight.setAlpha(0.6f);
		
		attachChild(leftDoor);
		attachChild(rightDoor);
		attachChild(leftBar);
		attachChild(rightBar);
	}
	
	public void openSlideDoors()
	{	
		leftDoor.slideX(doorsAnimationTime, leftDoorOpenX);
		rightDoor.slideX(doorsAnimationTime, rightDoorOpenX);
		
		leftBar.slideX(doorsAnimationTime, leftBarOpenX);
		rightBar.slideX(doorsAnimationTime, rightBarOpenX);
	}

	public void closeSlideDoors()
	{
		leftDoor.slideX(doorsAnimationTime, leftDoorCloseX);
		rightDoor.slideX(doorsAnimationTime, rightDoorCloseX);
		
		leftBar.slideX(doorsAnimationTime, leftBarCloseX);
		rightBar.slideX(doorsAnimationTime, rightBarCloseX);
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
