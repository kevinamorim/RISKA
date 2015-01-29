package feup.lpoo.riska.scenes.menu;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;

import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.resources.ResourceCache;

public abstract class RiskaMenuScene extends MenuScene implements Displayable {
	
	protected Camera camera;
	protected ResourceCache resources;

	// ------------------
	//  Methods
	public RiskaMenuScene(Camera pCamera)
	{
		super(pCamera);
		this.setBackgroundEnabled(false);
		
		this.camera = pCamera;
		
		this.resources = ResourceCache.instance;
	}
	
}
