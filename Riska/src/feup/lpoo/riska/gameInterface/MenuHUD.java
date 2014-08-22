package feup.lpoo.riska.gameInterface;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.sprite.Sprite;

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
//	private RiskaSprite doorBottom, doorTop;
	private Sprite border;
	
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
		//createBorder();
	}

	private void createBorder()
	{
		border = new Sprite(0, 0, resources.menuBorderRegion, resources.vbom);
		
		border.setPosition(0.5f * camera.getWidth(), 0.5f * camera.getHeight());
		border.setSize(camera.getWidth(), camera.getHeight());
		
		attachChild(border);
	}

	private void createDoors()
	{
		doorLeft = new RiskaSprite(0, 0, resources.doorLeftRegion, resources.vbom);
		doorRight = new RiskaSprite(0, 0, resources.doorRightRegion, resources.vbom);
//		doorBottom = new RiskaSprite(0, 0, resources.doorBottomRegion, resources.vbom);
//		doorTop = new RiskaSprite(0, 0, resources.doorTopRegion, resources.vbom);
		
		doorLeft.setPosition(0.25f * camera.getWidth(), 0.5f * camera.getHeight());
		doorLeft.setSize(0.5f * camera.getWidth(), 1f * camera.getHeight());

		doorRight.setPosition(0.75f * camera.getWidth(), 0.5f * camera.getHeight());
		doorRight.setSize(0.5f * camera.getWidth(), 1f * camera.getHeight());	
		
		leftDoorOpenX = -0.25f * camera.getWidth() + 0.1f * doorLeft.getWidth();
		rightDoorOpenX = 1.25f * camera.getWidth() - 0.1f * doorRight.getWidth();
		leftDoorCloseX = 0.25f * camera.getWidth()/* + 0.1f * doorLeft.getWidth()*/;
		rightDoorCloseX = 0.75f * camera.getWidth()/* - 0.1f * doorRight.getWidth()*/;
		
//		doorTop.setSize(0.5f * camera.getWidth(), 0.12f * camera.getHeight());
//		doorTop.setPosition(0.5f * camera.getWidth(), camera.getHeight() - 0.5f * doorTop.getHeight());
//		
//		doorBottom.setSize(0.5f * camera.getWidth(), 0.12f * camera.getHeight());
//		doorBottom.setPosition(0.5f * camera.getWidth(), 0.5f * doorBottom.getHeight());
		
		
		attachChild(doorLeft);
		attachChild(doorRight);
//		attachChild(doorBottom);
//		attachChild(doorTop);
	}
	
	public void openSlideDoors()
	{
//		doorBottom.slideY(doorsAnimationTime, -0.5f * doorBottom.getHeight());
//		doorTop.slideY(doorsAnimationTime,  camera.getHeight() + 0.5f * doorTop.getHeight());
		
		doorLeft.slideX(doorsAnimationTime, leftDoorOpenX);
		doorRight.slideX(doorsAnimationTime, rightDoorOpenX);
	}

	public void closeSlideDoors()
	{
//		doorBottom.slideY(doorsAnimationTime, 0.5f * doorBottom.getHeight());
//		doorTop.slideY(doorsAnimationTime,  camera.getHeight() - 0.5f * doorTop.getHeight());
		
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
