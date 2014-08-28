package feup.lpoo.riska.resources;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
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
import feup.lpoo.riska.gameInterface.CameraManager;
import feup.lpoo.riska.logic.GameOptions;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.music.Conductor;
import feup.lpoo.riska.utilities.Utils;

public class ResourceCache {

	// ======================================================
	// FIELDS
	// ======================================================
	public MainActivity activity;
	public Engine engine;
	public CameraManager camera;
	public VertexBufferObjectManager vbom;

	private static ResourceCache instance = new ResourceCache();

	private static String currentTheme = "dark/";

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

	private BuildableBitmapTextureAtlas mainMenuTextureAtlas;
	public ITextureRegion buttonRegion;
	public ITextureRegion emptyButtonRegion;
	
	public TiledTextureRegion factionSpriteRegion;
	public ITextureRegion smallFrameRegion;
	public ITextureRegion coveredSmallFrameRegion;
	
	public TiledTextureRegion addRemoveButtonRegion;
	public TiledTextureRegion playerCheckBoxButtonRegion;
	
	private BuildableBitmapTextureAtlas propsTextureAtlas;
	public ITextureRegion doorLeftRegion;
	public ITextureRegion doorRightRegion;
	public ITiledTextureRegion switchRegion;
	public ITiledTextureRegion checkBoxRegion;
	public ITiledTextureRegion mapsRegion;
	
	public ITextureRegion barHRegion, barVRegion;
	
	public ITextureRegion tabRegion;
	public ITextureRegion frameRegion;
	
	// ==================================================
	// GAME RESOURCES
	// ==================================================
	public ITiledTextureRegion regionBtnRegion;
	public ITiledTextureRegion attackBtnRegion;
	public ITiledTextureRegion detailsBtnRegion;
	public ITiledTextureRegion autoDeployBtnRegion;
	public ITiledTextureRegion moveBtnRegion;
	public ITiledTextureRegion nextTurnBtnRegion;
	public ITiledTextureRegion infoTabRegion;
	public ITextureRegion windowRegion;
	public ITextureRegion windowRegionGeneric;
	public ITextureRegion windowRegionGenericInverted;
	public ITiledTextureRegion arrowRightRegion;
	public ITiledTextureRegion seaRegion;
	public ITextureRegion mapRegion;
	private BuildableBitmapTextureAtlas gameTextureAtlas;
	private BitmapTextureAtlas mapTextureAtlas;
	public ArrayList<Map> maps;	
	
	// ==================================================
	// GAMEOVER RESOURCES
	// ==================================================
	
	// ======================================================
	// FONTS
	// ======================================================
	public Font mSplashFont;
	public Font mainMenuFont;
	public Font mInfoTabFont;
	public Font mGameFont;
	public Font mGameOverFont;

	protected BitmapTextureAtlas mFontTexture;
	protected Font mFont;

	protected BitmapTextureAtlas mGameFontTexture;

	
	// ==================================================
	// SPLASH SCENE
	// ==================================================
	public void loadSplashScene()
	{
		createSplashSceneResources();
		loadSplashSceneResources();
	}

	private void createSplashSceneResources()
	{
		int textureWidth = 512, textureHeight = 512;

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "splash/");

		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 
				textureWidth, textureHeight, TextureOptions.BILINEAR);
		
		splashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, 
				activity, "splash.png", 0, 0);
	}

	public void unloadSplashSceneResources()
	{
		splashTextureAtlas.unload();
	}

	public void loadSplashSceneResources()
	{
		splashTextureAtlas.load();
	}

	// ==================================================
	// MAIN MENU SCENE
	// ==================================================
	
	public void createMainMenuResources()
	{
		createMainMenuGraphics();
		createMainMenuFonts();
	}
	
	private void createMainMenuGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "menu/");

		int bgTextureWidth = 2048, bgTextureHeight = 1024;

		menuBackgroundTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 
				bgTextureWidth, bgTextureHeight, TextureOptions.BILINEAR);
		
		menuBackgroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuBackgroundTextureAtlas, 
				activity, "background.png", 0, 0);

		int mainTextureWidth = 2048, mainTextureHeight = 2048;

		mainMenuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 
				mainTextureWidth, mainTextureHeight, TextureOptions.BILINEAR);
		
		buttonRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTextureAtlas, activity, 
				"button.png");
		
		emptyButtonRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTextureAtlas, activity, 
				"empty_button.png");
		
		factionSpriteRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mainMenuTextureAtlas, activity, 
				"faction.png", 1, 2);
		
		smallFrameRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTextureAtlas, activity, 
				"small_frame.png");
		
		addRemoveButtonRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mainMenuTextureAtlas, activity, 
				"add_remove_button.png", 1, 2);
		
		playerCheckBoxButtonRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mainMenuTextureAtlas, activity, 
				"switch.png", 1, 2);
		
		frameRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTextureAtlas, activity, 
				"frame.png");
		
		coveredSmallFrameRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTextureAtlas, activity, 
				"small_frame_covered.png");

		
		int propsTextureWidth = 2048, propsTextureHeight = 2048;
		
		propsTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 
				propsTextureWidth, propsTextureHeight, TextureOptions.BILINEAR);
		
		doorLeftRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(propsTextureAtlas, activity, 
				"door_left.png");
		
		doorRightRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(propsTextureAtlas, activity, 
				"door_right.png");
		
		barHRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(propsTextureAtlas, activity, 
				"bar.png");
		
		barVRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(propsTextureAtlas, activity, 
				"bar_vertical.png");
		
		switchRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(propsTextureAtlas, activity, 
				"switch.png", 1, 2);
		
		checkBoxRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(propsTextureAtlas, activity, 
				"check_box.png", 1, 2);
		
		tabRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(propsTextureAtlas, activity, 
				"tab.png");
		
		mapsRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(propsTextureAtlas, activity, 
				"maps_tiled_atlas.png", 2, 2);
		
		try
		{
			mainMenuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			propsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
		}
		catch(final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}	
	}

	private void createMainMenuFonts()
	{
		FontFactory.setAssetBasePath("fonts/");

		mainMenuFont = FontFactory.createFromAsset(engine.getFontManager(),
				engine.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR,
				activity.getAssets(), "Calibri.ttf", 125f, true,
				Color.WHITE);
	}
	
	public void loadMainMenuResources()
	{
		// Graphics
		menuBackgroundTextureAtlas.load();		
		mainMenuTextureAtlas.load();
		propsTextureAtlas.load();
		
		// Fonts
		mainMenuFont.load();
	}
	
	public void unloadMainMenuResources()
	{
		mainMenuTextureAtlas.unload();
		menuBackgroundTextureAtlas.unload();
		propsTextureAtlas.unload();
	}

	// ==================================================
	// GAME SCENE
	// ==================================================
	public void createGameResources()
	{
		createGameGraphics();
		createGameFonts();	
		createMaps();
	}

	private void createGameGraphics() {

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "game/");

		int mapTextureWidth = 4096, mapTextureHeight = 2048;
		int gameTextureWidth = 4096, gameTextureHeight = 2048;

		mapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), mapTextureWidth, mapTextureHeight, 
				TextureOptions.BILINEAR);
		mapRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mapTextureAtlas, activity,
				"map.png", 0, 0);



		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),
				gameTextureWidth, gameTextureHeight, TextureOptions.BILINEAR);

		regionBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, 
				"region.png", 1, 2);

		attackBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, 
				"attack.png", 2, 1);

		detailsBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, 
				"close.png", 2, 1);

		autoDeployBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity,
				"auto_deploy.png", 2, 1);
		
		moveBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, 
				"move.png", 2, 1);
		
		nextTurnBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, 
				"next_turn.png", 2, 1);

		infoTabRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, 
				"info_tab.png", 1, 2);

		windowRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"window.png");

		windowRegionGeneric = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"window_general.png");

		windowRegionGenericInverted = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, 
				"window_general_inverted.png");

		arrowRightRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity,
				"arrow_right.png", 2, 1);
		
		try
		{		
			gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			
		}
		catch(final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}
	}

	private void createGameFonts() {

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
	}
	
	private void createMaps()
	{
		if(maps == null)
		{
			maps = new ArrayList<Map>();
		}
		
		for(int i = 0; i < GameOptions.numberOfMaps; i++)
		{
			String currentMapPath = "maps/" + i + "/";
			
			maps.add(new Map(currentMapPath + "regions.csv", currentMapPath + "neighbours.csv"));
		}
	}
	
	public void loadGameSceneResources()
	{		
		// Texture Atlas
		gameTextureAtlas.load();
		mapTextureAtlas.load();
		
		// Fonts
		mGameFont.load();
		mInfoTabFont.load();
	}

	public void unloadGameSceneResources()
	{
		gameTextureAtlas.unload();
		mapTextureAtlas.unload();
	}

	// ==================================================
	// GAMEOVER SCENE
	// ==================================================
	public void loadGameOverSceneResources() {
		loadGameOverSceneFonts();
	}
	
	public void unloadGameOverSceneResources() {
		mGameOverFont.unload();
	}
	
	private void loadGameOverSceneFonts() {
		
		FontFactory.setAssetBasePath("fonts/");
		
		int textureWidth = 512, textureHeight = 512;
		
		mGameOverFont = FontFactory.create(
				engine.getFontManager(), 
				engine.getTextureManager(), textureWidth, textureHeight, 
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32f, Color.WHITE);
		
		mGameOverFont.load();
		
	}
	// ==================================================
	// SFX
	// ==================================================
	public void loadMusicResources()
	{
		String musicBackground = Utils.getString(R.string.path_music_background);

		MusicFactory.setAssetBasePath("sounds/");

		Music backgroundMusic = loadMusic(musicBackground);

		Conductor.getSharedInstance().addMusic(backgroundMusic, activity.getResources().getString(R.string.name_music_background));
	}

	private Music loadMusic(String filename)
	{
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
			CameraManager camera, VertexBufferObjectManager vbom)
	{
		instance.engine = engine;
		instance.activity = activity;
		instance.camera = camera;
		instance.vbom = vbom;
	}

	// ==================================================
	// GETTERS & SETTERS
	// ==================================================
	public static ResourceCache getSharedInstance() {
		return instance;	
	}

}
