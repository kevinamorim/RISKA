package feup.lpoo.riska.logic;

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

import feup.lpoo.riska.io.FileRead;
import android.graphics.Color;
import android.graphics.Point;

public class SceneManager {
	
	private SceneType currentScene;
	private MainActivity activity;
	private Engine engine;
	
	private Scene splashScene, mainMenuScene, optionsScene, 
		loadMapScene, gameScene; /* Create more scene if needed, like gameScene. */
	
	public static SceneManager instance;
	
	/* Every time a new scene is created, a new entry is also added. */
	public enum SceneType {
		SPLASH, 
		MENU,
		OPTIONS,
		LOAD_MAP,
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
	
	protected BitmapTextureAtlas mGameFontTexture;
	protected Font mGameFont;
	
	protected BitmapTextureAtlas mLeftPanelTextureAtlas;
	protected ITextureRegion mLeftPanelTextureRegion;
	
	
	/* =============================
	 *           MAP
	 * =============================
	 */
	
	protected BitmapTextureAtlas regionButtonTextureAtlas;
	protected TiledTextureRegion regionButtonTiledTextureRegion;
	
	protected BitmapTextureAtlas mapTextureAtlas;
	protected ITextureRegion mapTextureRegion;
	
	private final int VALUES = 4;
	protected final int NUMBER_OF_REGIONS = 38;
	private int regionsCreated = 0;
	
	protected Region regions[];
	
	protected CameraManager cameraManager;
	
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

		// LOADS MENU BACKGROUND
		mMenuBackground = new BitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.DEFAULT);
		mMenuBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMenuBackground, 
				activity, "background.png", 0, 0);
		
		mMenuBackground.load();
		
		// LOADS FONT
		FontFactory.setAssetBasePath("fonts/");
		
		mFont = FontFactory.createFromAsset(engine.getFontManager(),
				engine.getTextureManager(), 256, 512, TextureOptions.BILINEAR,
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
		
		// LOADS MAP TEXTURE
		mapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 4096, 2048, TextureOptions.DEFAULT);
		mapTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mapTextureAtlas, activity, "map.png", 0, 0);
		
		mapTextureAtlas.load();
		
		// LOADS REGION CIRCULAR BUTTON
		regionButtonTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 128, 256, TextureOptions.DEFAULT);
		regionButtonTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(regionButtonTextureAtlas, 
	    		activity.getAssets(), "region_button.png", 0, 0, 1, 2);
		
		regionButtonTextureAtlas.load();
		
		cameraManager = new CameraManager();
		
		String filename = "regions.csv";
		String[] mapData = new String[VALUES * NUMBER_OF_REGIONS];
		
		new FileRead(filename, mapData);
		
		regions = new Region[NUMBER_OF_REGIONS];

		for(int i = 0; i < mapData.length; i++) {
			
			String name = mapData[i];
			i++;
			int x = Integer.parseInt(mapData[i]);
			i++;
			int y = Integer.parseInt(mapData[i]);
			i++;
			String continent = mapData[i];
			
			Region newRegion = new Region(name, new Point(x, y), continent);
			regions[regionsCreated] = newRegion;
			regionsCreated++;
			
		}
		
		
		mLeftPanelTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.DEFAULT);
		mLeftPanelTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLeftPanelTextureAtlas, 
				activity, "panel_left.png", 0, 0);
		
		mLeftPanelTextureAtlas.load();
		
		// LOADS FONT
		FontFactory.setAssetBasePath("fonts/");
		
		mGameFont = FontFactory.createFromAsset(engine.getFontManager(),
				engine.getTextureManager(), 256, 512, TextureOptions.BILINEAR,
	            activity.getAssets(), "reprise.ttf", 48f, true,
	            Color.WHITE); /* Load the font in white, so setColor() works. */
		
		mGameFont.load();
		
	}
	
	public Scene createSplashScene() {
		
		splashScene = new SplashScene();
		return splashScene;
		
	}
	
	public void createGameScenes() {
		
		mainMenuScene = new MainMenuScene();
		optionsScene = new OptionsScene();
		//loadMapScene = new LoadMapScene();
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
		case LOAD_MAP:
			engine.setScene(loadMapScene);
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
	
	public Scene getGameScene() {
		return gameScene;
	}
	
	
	public void addRegion(Region region) {
		
		if(region.getName() != null && 
				region.getName().length() > 0) {
			
			if(regionsCreated < NUMBER_OF_REGIONS) {
				regions[regionsCreated] = region;
				regionsCreated++;
			}		
		}
		
	}
	

}
