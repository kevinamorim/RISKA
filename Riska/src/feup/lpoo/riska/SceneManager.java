package feup.lpoo.riska;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.graphics.Color;

public class SceneManager {
	
	private SceneType currentScene;
	private MainActivity activity;
	private Engine engine;
	
	private Scene splashScene, mainMenuScene, optionsScene, gameScene; /* Create more scene if needed, like gameScene. */
	
	public static SceneManager instance;
	
	/* Everytime a new scene is created, a new entry is also added. */
	public enum SceneType {
		SPLASH, 
		MENU,
		OPTIONS,
		GAME,
		GAME_OVER
	};
	
	// Textures
	protected BitmapTextureAtlas logoTexture;
	protected ITextureRegion logoTextureRegion;
	
	protected BitmapTextureAtlas mMenuBackground;
	protected ITextureRegion mMenuBackgroundTextureRegion;
	
	protected BitmapTextureAtlas mStartButtonTextureAtlas;
	protected TiledTextureRegion mStartButtonTiledTextureRegion;
	
	protected BitmapTextureAtlas mOptionsButtonTextureAtlas;
	protected TiledTextureRegion mOptionsButtonTiledTextureRegion;
	
	protected BitmapTextureAtlas mReturnButtonTextureAtlas;
	protected TiledTextureRegion mReturnButtonTiledTextureRegion;
	
	protected BitmapTextureAtlas mSliderButtonTextureAtlas;
	protected TiledTextureRegion mSliderButtonTiledTextureRegion;
	
	protected BitmapTextureAtlas mFontTexture;
	protected Font mFont;
	
	protected BitmapTextureAtlas mapTexture;
	protected ITextureRegion mapTextureRegion;
	
	
	/* =============================
	 *           MAP
	 * =============================
	 */
	protected BitmapTextureAtlas mRegionsTextureAtlas[];
	protected TiledTextureRegion mRegionsTextureRegions[];
	
	final int NUMBER_OF_REGIONS = 12;
	/*=============================
	 * =============================
	 */
	
	
	public SceneManager(MainActivity activity, Engine engine, Camera camera) {
		this.activity = activity;
		this.engine = engine;
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
		
		// LOADS LOGO PIC
		logoTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.DEFAULT);
		
		logoTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(logoTexture, activity, "logo.png", 0, 0);
		
		logoTexture.load();	
		
	}
	
	/**
	 * Loads all resources needed by our main menu.
	 */
	public void loadMainMenuResources() {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// LOADS GAME BACKGROUND *MUST BE MOVED FROM HERE*
		mapTexture = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);
		mapTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mapTexture, activity, "map_regions.png", 0, 0);
		mapTexture.load();

		// LOADS MENU BACKGROUND
		mMenuBackground = new BitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.DEFAULT);
		mMenuBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMenuBackground, 
				activity, "background.png", 0, 0);
		
		mMenuBackground.load();
		
		// LOADS FONT
		FontFactory.setAssetBasePath("fonts/");
		
		mFont = FontFactory.createFromAsset(engine.getFontManager(),
				engine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
	            activity.getAssets(), "reprise.ttf", 125f, true,
	            Color.WHITE); /* Load the font in white, so setColor() works. */
		
	    mFont.load();
	    
	    // LOADS START BUTTON
	    mStartButtonTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.DEFAULT);
	    mStartButtonTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mStartButtonTextureAtlas, 
	    		activity.getAssets(), "button.png", 0, 0, 1, 2);
	    
	    mStartButtonTextureAtlas.load();
	    
	    // LOADS OPTIONS BUTTON
	    mOptionsButtonTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 1024, TextureOptions.DEFAULT);
	    mOptionsButtonTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mOptionsButtonTextureAtlas, 
	    		activity.getAssets(), "options_button.png", 0, 0, 1, 2);
	    
	    mOptionsButtonTextureAtlas.load();
	    
	    // LOADS RETURN BUTTON
	    mReturnButtonTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 1024, TextureOptions.DEFAULT);
	    mReturnButtonTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mReturnButtonTextureAtlas, 
	    		activity.getAssets(), "return_button.png", 0, 0, 1, 2);
	    
	    mReturnButtonTextureAtlas.load();
	    
	    // LOADS SLIDER BUTTON
	    mSliderButtonTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);
	    mSliderButtonTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSliderButtonTextureAtlas, 
	    		activity.getAssets(), "slider_button.png", 0, 0, 1, 2);
	    
	    mSliderButtonTextureAtlas.load();
	    
		
	}
	
	public void loadGameSceneResources() {
		
		mRegionsTextureAtlas = new BitmapTextureAtlas[NUMBER_OF_REGIONS];
		mRegionsTextureRegions = new TiledTextureRegion[NUMBER_OF_REGIONS];
		
		for(int i = 0; i < NUMBER_OF_REGIONS; i++) {
			String path = "region_" + i + ".png";
			mRegionsTextureAtlas[i] = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.DEFAULT);
			mRegionsTextureRegions[i] = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mRegionsTextureAtlas[i], 
					activity.getAssets(), path, 0, 0, 1, 1);
			mRegionsTextureAtlas[i].load();
		}
		
	}
	
	public Scene createSplashScene() {
		
		splashScene = new SplashScene();
		return splashScene;
		
	}
	
	public void createGameScenes() {
		
		mainMenuScene = new MainMenuScene();
		optionsScene = new OptionsScene();
		gameScene = new GameScene();
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
		case OPTIONS:
			engine.setScene(optionsScene);
			break;
		case GAME:
			engine.setScene(gameScene);
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
