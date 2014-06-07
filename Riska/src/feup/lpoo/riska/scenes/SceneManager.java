package feup.lpoo.riska.scenes;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.io.FileRead;
import feup.lpoo.riska.logic.MainActivity;
import android.graphics.Color;
import android.graphics.Point;

public class SceneManager {
	
	private final int SEA_COLS = 4;
	private final int SEA_LINES = 2;
	
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
	
	protected BitmapTextureAtlas mFlagsTextureAtlas;
	protected ITextureRegion mFlagsTextureRegion;
	
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
	
	protected Map map;
	
	protected CameraManager cameraManager;
	
	protected BitmapTextureAtlas mSeaTextureAtlas;
	protected TiledTextureRegion mSeaTiledTextureRegion;
	
	/*=============================
	 * =============================
	 */
	
	protected Music music;
	
	
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
		
		// Loads flags. 
		mFlagsTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 256, TextureOptions.DEFAULT);
		mFlagsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mFlagsTextureAtlas, activity, "flags.png", 0, 0);
		
		mFlagsTextureAtlas.load();
		
		
		// =================================================================================
		// REGIONS 
		// ================================================================================= 
		String filename = "regions.csv";

		map = new Map(readRegions(filename));
		
		filename = "neighbours.csv";
		
		readNeighbours(filename);
		
		map.printNeighbours();
			
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
		
		mSeaTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.DEFAULT);
		mSeaTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSeaTextureAtlas, 
	    		activity.getAssets(), "sea.png", 0, 0, SEA_COLS, SEA_LINES);
		
		mSeaTextureAtlas.load();
		
		try
		{
		    music = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity, "sounds/music.mp3");
		    music.setLooping(true);
		    music.play();
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}

		
	}
	
	private Region[] readRegions(String filename) {
		ArrayList<String> mapData = new ArrayList<String>();
		
		new FileRead(filename, mapData);
		
		Region regions[];
		regions = new Region[NUMBER_OF_REGIONS];

		for(int i = 0; i < mapData.size(); i++) {
			
			int id = Integer.parseInt(mapData.get(i));
			i++;
			String name = mapData.get(i);
			i++;
			int x = Integer.parseInt(mapData.get(i));
			i++;
			int y = Integer.parseInt(mapData.get(i));
			i++;
			String continent = mapData.get(i);
			i++;
			
			Region newRegion = new Region(id, name, new Point(x, y), continent);
	
			regions[regionsCreated] = newRegion;
			regionsCreated++;
			
		}
		
		return regions;
		
	}

	private void readNeighbours(String filename) {
		
		ArrayList<String> data = new ArrayList<String>();
		new FileRead(filename, data);
		
		for(int i = 0; i < data.size(); i++) {
			int id = Integer.parseInt(data.get(i));
			Region region = map.getRegionById(id);
			i++;
			while(data.get(i) != "#") {
				Region neighbour = map.getRegionById(Integer.parseInt(data.get(i)));
				if(neighbour != null) {
					region.addNeighbour(neighbour);
				}
				i++;
			}
		}
		
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
	
	// TODO CHECK IF THESE METHODS ARE NEEDED
	

	public Font getGameFont() {
		return this.mGameFont;
	}
	
	public Font getFont() {
		return this.mFont;
	}
	
	public ITextureRegion getHUDTextureRegion() {
		return this.mLeftPanelTextureRegion;
	}
	
	public ITextureRegion getRegionFlag() {
		return this.mFlagsTextureRegion;
	}
	
	public TiledTextureRegion getRegionButtonTextureRegion() {
		return this.regionButtonTiledTextureRegion;
	}

	public TiledTextureRegion getStartButtonTextureRegion() {
		return this.mStartButtonTiledTextureRegion;
	}
}
