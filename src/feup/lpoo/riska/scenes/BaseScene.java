package feup.lpoo.riska.scenes;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import feup.lpoo.riska.gameInterface.CameraManager;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.logic.SceneManager;
import feup.lpoo.riska.resources.ResourceManager;
import feup.lpoo.riska.utilities.Utils;

public abstract class BaseScene extends Scene {

	// ==================================================
	// FIELDS
	// ==================================================
	protected Engine engine;
	protected MainActivity activity;
	protected ResourceManager resources;
	protected VertexBufferObjectManager vbom;
	protected CameraManager camera;
	protected SceneManager sceneManager;

	// ==================================================
	// CONSTRUCTOR
	// ==================================================
	public BaseScene()
	{
		this.resources = ResourceManager.instance;
		this.sceneManager = SceneManager.instance;
		this.engine = MainActivity.instance.getEngine();
		this.activity = MainActivity.instance;
		this.vbom = MainActivity.instance.getVertexBufferObjectManager();
		this.camera = MainActivity.instance.mCamera;
		
		createScene();
	}

	// ==================================================
	// ABSTRACTION
	// ==================================================
	public abstract void createScene();
	
	public abstract void onBackKeyPressed();

	public abstract Utils.CONTEXT getSceneType();

	public abstract void disposeScene();

	public abstract void onMenuKeyPressed();

}
