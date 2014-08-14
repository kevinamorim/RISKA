package feup.lpoo.riska.scenes;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import feup.lpoo.riska.gameInterface.CameraManager;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.logic.SceneManager;
import feup.lpoo.riska.logic.SceneManager.SCENE_TYPE;
import feup.lpoo.riska.resources.ResourceCache;

public abstract class BaseScene extends Scene {

	// ==================================================
	// FIELDS
	// ==================================================
	protected Engine engine;
	protected MainActivity activity;
	protected ResourceCache resources;
	protected VertexBufferObjectManager vbom;
	protected CameraManager camera;
	protected SceneManager sceneManager;

	// ==================================================
	// CONSTRUCTOR
	// ==================================================
	public BaseScene()
	{
		this.resources = ResourceCache.getSharedInstance();
		this.sceneManager = SceneManager.getSharedInstance();
		this.engine = resources.engine;
		this.activity = resources.activity;
		this.vbom = resources.vbom;
		this.camera = resources.camera;
		
		createScene();
	}

	// ==================================================
	// ABSTRACTION
	// ==================================================
	public abstract void createScene();
	
	public abstract void onSceneShow();

	public abstract void onBackKeyPressed();

	public abstract SCENE_TYPE getSceneType();

	public abstract void disposeScene();

}
