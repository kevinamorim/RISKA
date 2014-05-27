package feup.lpoo.riska;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

public class SceneManager {
	
	private SceneType currentScene;
	private MainActivity activity;
	private Engine engine;
	private Camera camera;
	
	private Scene splashScene, mainMenuScene; /* Create more scene if needed, like gameScene. */
	
	public static SceneManager instance;
	
	/* Everytime a new scene is created, a new entry is also added. */
	public enum SceneType {
		SPLASH, 
		MENU,
		GAME,
		GAME_OVER
	};
	
	// Textures
	public BitmapTextureAtlas logoTexture;
	public ITextureRegion logoTextureRegion;
	
	protected BitmapTextureAtlas mMenuBackground;
	protected ITextureRegion mMenuBackgroundTextureRegion;
	
	protected BitmapTextureAtlas mFontTexture;
	protected Font mFont;
	
	public Bitmap mapRegions;
	public BitmapTextureAtlas mapTexture;
	public ITextureRegion mapTextureRegion;
	
	public SceneManager(MainActivity activity, Engine engine, Camera camera) {
		this.activity = activity;
		this.engine = engine;
		this.camera = camera;
		instance = this;
	}
	
	public static SceneManager getSharedInstance() {
		return instance;
	}
	
	/**
	 * Loads all resources needed by our splash scene. 
	 */
	public void loadSplashSceneResources() {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		/* 
		 * NEW READ BITMAP
		 */
		InputStream bitmapIs = null;
		try {
			bitmapIs = activity.getAssets().open("gfx/map_regions.png");
			//bitmapIs = activity.getAssets().open("gfx/map_influence.png");
			mapRegions = BitmapFactory.decodeStream(bitmapIs);
		} catch (IOException e) {
		}
		/*
		 * END OF NEW READ BITMAP
		 */
		
		logoTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.DEFAULT);
		
		logoTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(logoTexture, activity, 
				"logo.png", 0, 0);
		
		logoTexture.load();
		
	}
	
	/**
	 * Loads all resources needed by our main menu.
	 */
	public void loadMainMenuResources() {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		mMenuBackground = new BitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.DEFAULT);
		mMenuBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMenuBackground, 
				activity, "background.png", 0, 0);
		
		mMenuBackground.load();
		
		FontFactory.setAssetBasePath("fonts/");
		
		mFont = FontFactory.createFromAsset(engine.getFontManager(),
				engine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
	            activity.getAssets(), "reprise.ttf", 125f, true,
	            Color.BLACK);
		
	    mFont.load();
		
	}
	
	public Scene createSplashScene() {
		
		splashScene = new SplashScene();
		return splashScene;
		
	}
	
	public void createGameScenes() {
		
		mainMenuScene = new MainMenuScene();
		//gameScene = new GameScene();
		
	}
	
	public SceneType getCurrentScene() {
		return currentScene;
	}
	
	public void setCurrentScene(SceneType scene) {
		
		currentScene = scene;
		
		switch(scene) {
		case SPLASH: 
			break;
		case MENU:
			engine.setScene(mainMenuScene);
			break;
		default:
				break;
		}
		
	}
	
	public void cleanGame() {
		/*
		 * Do stuff before exiting the game.
		 * p.e. save game state.
		 */
	}
	
	

}
