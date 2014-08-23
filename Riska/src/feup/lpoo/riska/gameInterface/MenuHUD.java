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
	private RiskaSprite barLeft, barRight;
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
		
		doorLeft = new RiskaSprite(0, 0, resources.doorLeftRegion, resources.vbom);
		doorRight = new RiskaSprite(0, 0, resources.doorRightRegion, resources.vbom);
		barLeft = new RiskaSprite(0, 0, resources.barVRegion, resources.vbom);
		barRight = new RiskaSprite(0, 0, resources.barVRegion, resources.vbom);
		
		doorLeft.setSize(doorsWidth, doorsHeight);
		doorLeft.setPosition(0.27f * camera.getWidth(), camera.getCenterY());
		
		barLeft.setSize(0.03f * camera.getWidth(), camera.getHeight());
		barLeft.setPosition(doorLeft.right() - barLeft.halfX(), camera.getCenterY());

		doorRight.setSize(doorsWidth, doorsHeight);	
		doorRight.setPosition(0.73f * camera.getWidth(), camera.getCenterY());
		
		barRight.setSize(0.03f * camera.getWidth(), camera.getHeight());
		barRight.setPosition(doorRight.left() + barRight.halfX(), camera.getCenterY());
		
		leftDoorOpenX = -doorLeft.halfX() + barLeft.getWidth();
		rightDoorOpenX = camera.getWidth() + doorRight.halfX() - barRight.getWidth();
		leftBarOpenX = barLeft.halfX();
		rightBarOpenX = camera.getWidth() - barRight.halfX();
		
		
		leftDoorCloseX = doorLeft.halfX();
		rightDoorCloseX = camera.getWidth() - doorRight.halfX();
		leftBarCloseX = camera.getCenterX();
		rightBarCloseX = camera.getCenterX();
		
		
		//doorLeft.setAlpha(0.6f);
		//doorRight.setAlpha(0.6f);
		
		attachChild(doorLeft);
		attachChild(doorRight);
		attachChild(barLeft);
		attachChild(barRight);
	}
	
	public void openSlideDoors()
	{	
		doorLeft.slideX(doorsAnimationTime, leftDoorOpenX);
		doorRight.slideX(doorsAnimationTime, rightDoorOpenX);
		
		barLeft.slideX(doorsAnimationTime, leftBarOpenX);
		barRight.slideX(doorsAnimationTime, rightBarOpenX);
	}

	public void closeSlideDoors()
	{
		doorLeft.slideX(doorsAnimationTime, leftDoorCloseX);
		doorRight.slideX(doorsAnimationTime, rightDoorCloseX);
		
		barLeft.slideX(doorsAnimationTime, leftBarCloseX);
		barRight.slideX(doorsAnimationTime, rightBarCloseX);
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
