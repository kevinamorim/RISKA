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
	private Sprite border;
	
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
		createBorder();
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

		doorLeft.setPosition(0.25f * camera.getWidth(), 0.5f * camera.getHeight());
		doorLeft.setSize(0.5f * camera.getWidth(), camera.getHeight());

		doorRight.setPosition(0.75f * camera.getWidth(), 0.5f * camera.getHeight());
		doorRight.setSize(0.5f * camera.getWidth(), camera.getHeight());

		attachChild(doorLeft);
		attachChild(doorRight);
	}
	
	public void openSlideDoors()
	{
		doorLeft.slideX(doorsAnimationTime, -0.25f * camera.getWidth()/* + 0.1f * Utils.getScaledWidth(doorLeft)*/);
		doorRight.slideX(doorsAnimationTime, 1.25f * camera.getWidth()/* - 0.1f * Utils.getScaledWidth(doorRight)*/);
	}

	public void closeSlideDoors()
	{
		doorLeft.slideX(doorsAnimationTime, 0.25f * camera.getWidth());
		doorRight.slideX(doorsAnimationTime, 0.75f * camera.getWidth());
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
