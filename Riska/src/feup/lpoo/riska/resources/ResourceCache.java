package feup.lpoo.riska.resources;

import java.io.IOException;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import feup.lpoo.riska.R;
import android.graphics.Color;
import android.graphics.Typeface;
import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.music.Conductor;
import feup.lpoo.riska.scenes.CameraManager;


public class ResourceCache {

	// ======================================================
	// FIELDS
	// ======================================================
	public MainActivity activity;
	public Engine engine;
	public CameraManager camera;
	public VertexBufferObjectManager vbom;
	public Conductor conductor;
	
	private static ResourceCache instance = new ResourceCache();
	
	// ==================================================
	// SPLASH RESOURCES
	// ==================================================
	private BitmapTextureAtlas splashTextureAtlas;
	public ITextureRegion splashRegion;
	
	// ==================================================
	// MAIN MENU RESOURCES
	// ==================================================
	private BitmapTextureAtlas menuBackgroundTextureAtlas;
	public ITextureRegion menuBackgroundRegion;
	
	private BitmapTextureAtlas menuBorderTextureAtlas;
	public ITextureRegion menuBorderRegion;
	
	private BuildableBitmapTextureAtlas mainMenuTextureAtlas;
	public TiledTextureRegion textBtnRegion;
	public TiledTextureRegion optionsBtnRegion;
	public TiledTextureRegion returnBtnRegion;
	public TiledTextureRegion sliderBtnRegion;
	
	// ==================================================
	// GAME RESOURCES
	// ==================================================
	public ITiledTextureRegion regionBtnRegion;
	public ITiledTextureRegion attackBtnRegion;
	public ITiledTextureRegion detailsBtnRegion;
	public ITiledTextureRegion autoDeployBtnRegion;
	public ITiledTextureRegion infoTabRegion;
	public ITextureRegion windowRegion;
	public ITiledTextureRegion seaRegion;
	public ITextureRegion mapRegion;
	private BuildableBitmapTextureAtlas gameTextureAtlas;
	private BitmapTextureAtlas mapTextureAtlas;
	private BitmapTextureAtlas seaTextureAtlas;
	public Map map;
	
	// ======================================================
	// FONTS
	// ======================================================
	public Font mSplashFont;
	public Font mainMenuFont;
	public Font mInfoTabFont;
	public Font mGameFont;

	protected BitmapTextureAtlas mFontTexture;
	protected Font mFont;

	protected BitmapTextureAtlas mGameFontTexture;
		

	// ==================================================
	// SPLASH SCENE
	// ==================================================
	public void loadSplashSceneResources() {
		loadSplashGraphics();
	}
	
	public void unloadSplashSceneResources() {
		splashTextureAtlas.unload();
		splashRegion = null;
	}

	private void loadSplashGraphics() {
		
		int textureWidth = 512, textureHeight = 512;
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/splash/");
		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 
				textureWidth, textureHeight, TextureOptions.BILINEAR);
		splashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, 
				activity, "splash.png", 0, 0);
		splashTextureAtlas.load();
		
	}
	
	// ==================================================
	// MAIN MENU SCENE
	// ==================================================
	public void loadMainMenuResources() {
		loadMainMenuGraphics();
		loadMainMenuFonts();
	}
	
	public void unloadMainMenuResources() {
		mainMenuTextureAtlas.unload();
		menuBackgroundTextureAtlas.unload();
		
	}
	
	private void loadMainMenuGraphics() {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		
		int bgTextureWidth = 2048, bgTextureHeight = 1024;
		
		menuBackgroundTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 
				bgTextureWidth, bgTextureHeight, TextureOptions.DEFAULT);
		menuBackgroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuBackgroundTextureAtlas, 
				activity, "background.png", 0, 0);
		
		menuBackgroundTextureAtlas.load();
		
		menuBorderTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 
				bgTextureWidth, bgTextureHeight, TextureOptions.DEFAULT);
		menuBorderRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuBorderTextureAtlas, 
				activity, "border.png", 0, 0);
		
		menuBorderTextureAtlas.load();
		
		int mainTextureWidth = 1024, mainTextureHeight = 2048;
		
		mainMenuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 
				mainTextureWidth, mainTextureHeight, TextureOptions.BILINEAR);
		
		textBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mainMenuTextureAtlas, activity, 
				"button.png", 1, 2);
		optionsBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mainMenuTextureAtlas, activity, 
				"options.png", 1, 2); 
		
		returnBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mainMenuTextureAtlas, activity, 
				"return.png", 1, 2);
		
		sliderBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mainMenuTextureAtlas, activity, 
				"slider.png", 1, 2);
			
		try {
			mainMenuTextureAtlas.build(
					new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			mainMenuTextureAtlas.load();
		} catch(final TextureAtlasBuilderException e) {
			Debug.e(e);
		}	
		
	}
	
	private void loadMainMenuFonts() {
		
		FontFactory.setAssetBasePath("fonts/");

		mainMenuFont = FontFactory.createFromAsset(engine.getFontManager(),
				engine.getTextureManager(), 512, 512, TextureOptions.BILINEAR,
				activity.getAssets(), "reprise.ttf", 125f, true,
				Color.WHITE);

		mainMenuFont.load();
		
	}
		
	// ==================================================
	// GAME SCENE
	// ==================================================
	public void loadGameSceneResources() {
		
		loadGameGraphics();
		loadGameFonts();
		map = new Map("regions.csv", "neighbours.csv");

	}

	public void unloadGameSceneResources() {
		gameTextureAtlas.unload();
		seaTextureAtlas.unload();
		mapTextureAtlas.unload();
	}
	
	private void loadGameGraphics() {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		
		int seaTextureWidth = 4096, seaTextureHeight = 2048, seaCols = 2, seaLines = 2;
		int mapTextureWidth = 4096, mapTextureHeight = 2048;
		int gameTextureWidth = 1024, gameTextureHeight = 1024;
		
		seaTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), seaTextureWidth, seaTextureHeight,
				TextureOptions.DEFAULT);
		seaRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(seaTextureAtlas, 
				activity.getAssets(), "sea.png", 0, 0, seaCols, seaLines);
		
		
		
		mapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), mapTextureWidth, mapTextureHeight, 
				TextureOptions.DEFAULT);
		mapRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mapTextureAtlas, activity, "map_3.png", 0, 0);
		

		
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),
				gameTextureWidth, gameTextureHeight, TextureOptions.BILINEAR);
		
		regionBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, 
				"region.png", 1, 2);
		
		attackBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, 
				"attack.png", 2, 1);
		
		detailsBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, 
				"details.png", 2, 1);
		
		autoDeployBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity,
				"auto_deploy.png", 2, 1);
		
		infoTabRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, 
				"info_tab.png", 1, 2);
		
		windowRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "window.png");

		
		try {		
			gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			gameTextureAtlas.load();
			seaTextureAtlas.load();
			mapTextureAtlas.load();
		} catch(final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		
	}
	
	private void loadGameFonts() {
		
		FontFactory.setAssetBasePath("fonts/");

		mGameFont = FontFactory.create(
				engine.getFontManager(), 
				engine.getTextureManager(), 512, 512, 
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32f,
				Color.WHITE);
		
		mInfoTabFont = FontFactory.create(engine.getFontManager(), 
				engine.getTextureManager(), 512, 512, 
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 28f,
				Color.WHITE);

		mGameFont.load();
		mInfoTabFont.load();
		
	}
	
	// ==================================================
	// SFX
	// ==================================================
	public void loadMusicResources() {

		try
		{

			String musicBackground = activity.getResources().getString(R.string.path_music_background);

			MusicFactory.setAssetBasePath("sounds/");

			Music backgroundMusic = loadMusic(musicBackground);

			conductor = new Conductor();
			conductor.addMusic(backgroundMusic, activity.getResources().getString(R.string.name_music_background));

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private Music loadMusic(String filename) {

		try
		{
			return MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity, filename);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}


	
	public static void prepareManager(Engine engine, MainActivity activity, 
			CameraManager camera, VertexBufferObjectManager vbom) {
		getSharedInstance().engine = engine;
		getSharedInstance().activity = activity;
		getSharedInstance().camera = camera;
		getSharedInstance().vbom = vbom;
	}
	
	// ==================================================
	// GETTERS & SETTERS
	// ==================================================
	public static ResourceCache getSharedInstance() {
		return instance;	
	}
	
}
