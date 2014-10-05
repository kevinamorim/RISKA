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

	public static ResourceCache instance = new ResourceCache();

	private static String currentTheme = "arrrrgh/";

	// ==================================================
	// SPLASH RESOURCES
	// ==================================================
	private BitmapTextureAtlas splashTextureAtlas;
	public ITextureRegion splashRegion;
	
	// ==================================================
	// LOADING SCREEN RESOURCES
	// ==================================================
	private BitmapTextureAtlas loadingTextureAtlas;
	public ITextureRegion loadingBackground;
	
	// ==================================================
	// MAIN MENU RESOURCES
	// ==================================================
	private BuildableBitmapTextureAtlas mainMenuNewTextureAtlas;
	public ITextureRegion riskaTitle, optionsTitle, startTitle;
	
	private BitmapTextureAtlas menuBackgroundTextureAtlas;
	public ITextureRegion menuBackgroundRegion;

	private BuildableBitmapTextureAtlas mainMenuTextureAtlas;
	public ITextureRegion button;
	public ITextureRegion emptyButtonRegion;
	
	private BuildableBitmapTextureAtlas propsTextureAtlas;
	public ITiledTextureRegion switchRegion;
	public ITiledTextureRegion checkBoxRegion;
	public ITiledTextureRegion factionColorRegion;
	
	private BuildableBitmapTextureAtlas mapsTextureAtlas;
	public ITiledTextureRegion mapsRegion;
	
	public ITiledTextureRegion labelSmallRegion;
	public ITextureRegion labelRegion;
	public ITextureRegion labelLargeRegion;
	
	public ITextureRegion tabRegion;
	
	// ==================================================
	// GAME RESOURCES
	// ==================================================
	public ITiledTextureRegion buttonRegion;
	public ITiledTextureRegion buttonRegionBig;
	public ITiledTextureRegion empty;
	
	private BuildableBitmapTextureAtlas mapTextureAtlas;
	public ITextureRegion map;
	
	private BuildableBitmapTextureAtlas gameTextureAtlas;	
	public ITiledTextureRegion infoTabRegion;
	public ITextureRegion windowRegion;
	public ITextureRegion windowRegionGeneric;
	public ITextureRegion windowRegionGenericInverted;

	private BuildableBitmapTextureAtlas gameNEWTextureAtlas;
	public ITiledTextureRegion spriteAttack;
	public ITiledTextureRegion spriteDeploy;
	public ITiledTextureRegion spriteSummon;

	public ITiledTextureRegion bottomLeft;
	public ITiledTextureRegion bottomCenter;
	public ITiledTextureRegion bottomRight;
	public ITiledTextureRegion midLeft;
	public ITiledTextureRegion midCenter;
	public ITiledTextureRegion midRight;
	public ITiledTextureRegion topLeft;
	public ITiledTextureRegion topCenter;
	public ITiledTextureRegion topRight;
	
	public ITiledTextureRegion midCenterSmall;
	
	public ITiledTextureRegion bottomCenterNoBorder;
	public ITiledTextureRegion midCenterNoBorder;
	public ITiledTextureRegion topCenterNoBorder;
	public ITiledTextureRegion midLeftNoBorder;
	public ITiledTextureRegion midRightNoBorder;
	
	public ITiledTextureRegion midCenterGlow;
	
	public ITextureRegion fillSquareRegion;
	
	public ITextureRegion[] regions;
	
	
	public ITextureRegion selection;
	
	
	public ArrayList<Map> maps;	
	
	// ==================================================
	// GAMEOVER RESOURCES
	// ==================================================
	
	// ======================================================
	// FONTS
	// ======================================================
	private BitmapTextureAtlas fontTextureAtlas;
	private BitmapTextureAtlas menuFontTextureAtlas;
	
	public Font mSplashFont;
	public Font mMenuFont;
	public Font mInfoTabFont;
	public Font mGameFont;
	public Font mGameNumbersFont;
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
	// LOADING SCENE
	// ==================================================
	public void createLoadingSceneResources()
	{
		int textureWidth = 4096, textureHeight = 2048;

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "loading/");

		loadingTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 
				textureWidth, textureHeight, TextureOptions.BILINEAR);
		
		loadingBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(loadingTextureAtlas, 
				activity, "background.png", 0, 0);
	}
	
	public void loadLoadingSceneResources()
	{
		loadingTextureAtlas.load();
	}
	
	public void unloadLoadingSceneResources()
	{
		loadingTextureAtlas.unload();
	}
	
	// ==================================================
	// MAIN MENU SCENE
	// ==================================================
	
	public void createMainMenuResources()
	{
		createMainMenuGraphics();
		createMainMenuNewGraphics();
		
		createMainMenuFonts();
	}

	private void createMainMenuNewGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "menu/main/");
		
		int mainTextureWidth = 4096, mainTextureHeight = 4096;
		int bgTextureWidth = 4096, bgTextureHeight = 2048;

		
		menuBackgroundTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 
				bgTextureWidth, bgTextureHeight, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
		
		menuBackgroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuBackgroundTextureAtlas, 
				activity, "Background.png", 0, 0);
		
		
		mainMenuNewTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 
				mainTextureWidth, mainTextureHeight, TextureOptions.BILINEAR);
		
		riskaTitle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuNewTextureAtlas, 
				activity, "RiskaTitle.png");
		
		optionsTitle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuNewTextureAtlas, 
				activity, "OptionsTitle.png");
		
		startTitle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuNewTextureAtlas, 
				activity, "StartTitle.png");
		
		try
		{
			mainMenuNewTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
		}
		catch(final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}
	}

	private void createMainMenuGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "menu/");

		int mainTextureWidth = 2048, mainTextureHeight = 2048;

		mainMenuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 
				mainTextureWidth, mainTextureHeight, TextureOptions.REPEATING_BILINEAR);
		
		button = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTextureAtlas, activity, 
				"button.png");
		
		emptyButtonRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTextureAtlas, activity, 
				"empty_button.png");

		
		int propsTextureWidth = 4096, propsTextureHeight = 2048;
		
		propsTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 
				propsTextureWidth, propsTextureHeight, TextureOptions.REPEATING_BILINEAR);
		
		labelSmallRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(propsTextureAtlas, activity, 
				"props/label_small.png", 1, 3);
		
		labelRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(propsTextureAtlas, activity, 
				"props/label.png");
		
		labelLargeRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(propsTextureAtlas, activity, 
				"props/label_large.png");
		
		switchRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(propsTextureAtlas, activity, 
				"switch.png", 1, 3);
		
		checkBoxRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(propsTextureAtlas, activity, 
				"check_box.png", 1, 3);
		
		factionColorRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(propsTextureAtlas, activity,
				"faction_color_button.png", 1, 3);
		
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
		
		menuFontTextureAtlas = new BitmapTextureAtlas(engine.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

		mMenuFont = FontFactory.create(
				engine.getFontManager(),
				menuFontTextureAtlas,
				Typeface.createFromAsset(activity.getAssets(), FontFactory.getAssetBasePath() + "Cursive Option.ttf"),
				125, true, Color.WHITE);
	}
	
	public void loadMainMenuResources()
	{
		// Graphics
		menuBackgroundTextureAtlas.load();		
		mainMenuTextureAtlas.load();
		mainMenuNewTextureAtlas.load();
		propsTextureAtlas.load();
		
		//SVGTextureAtlas.load();
		
		// Fonts
		mMenuFont.load();
	}
	
	public void unloadMainMenuResources()
	{
		mainMenuTextureAtlas.unload();
		mainMenuNewTextureAtlas.unload();
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

	private void createGameGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "game/");
		
		int gameTextureWidth = 4096, gameTextureHeight = 2048;

		
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),
				gameTextureWidth, gameTextureHeight, TextureOptions.BILINEAR);

		buttonRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, 
				"button_region.png", 1, 3);
		
		buttonRegionBig = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, 
				"button_region_big.png", 1, 3);
		
		empty = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, 
				"empty.png", 1, 3);
		
		selection = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, 
				"animated/selection.png");

		infoTabRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, 
				"info_tab.png", 1, 2);

		windowRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"window.png");

		windowRegionGeneric = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity,
				"window_general.png");

		windowRegionGenericInverted = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, 
				"window_general_inverted.png");
	
		
		gameNEWTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),
				gameTextureWidth, gameTextureHeight, TextureOptions.BILINEAR);
		
		spriteAttack = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity,
				"sprite_attack.png", 1, 3);
		
		spriteDeploy = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity,
				"sprite_deploy.png", 1, 3);
		
		spriteSummon = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity,
				"sprite_summon.png", 1, 3);
		
		
		midCenter = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity, 
				"mid_center.png", 3, 3);
		
		midLeft = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity, 
				"mid_left.png", 3, 3);
		
		midRight = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity, 
				"mid_right.png", 3, 3);
		
		bottomCenter = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity, 
				"bottom_center.png", 3, 3);
		
		bottomLeft = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity, 
				"bottom_left.png", 3, 3);
		
		bottomRight = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity, 
				"bottom_right.png", 3, 3);
		
		topCenter = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity, 
				"top_center.png", 3, 3);
		
		topLeft = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity, 
				"top_left.png", 3, 3);
		
		topRight = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity, 
				"top_right.png", 3, 3);
		
		midCenterSmall = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity, 
				"mid_center_small.png", 3, 3);
		
		bottomCenterNoBorder = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity, 
				"bottom_center_no_border.png", 3, 3);
		
		midCenterNoBorder = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity, 
				"mid_center_no_border.png", 3, 3);
		
		topCenterNoBorder = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity, 
				"top_center_no_border.png", 3, 3);
		
		midLeftNoBorder = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity, 
				"mid_left_no_border.png", 3, 3);
		
		midRightNoBorder = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity, 
				"mid_right_no_border.png", 3, 3);
		
		fillSquareRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameNEWTextureAtlas, activity,
				"fill_square.png");
		
		midCenterGlow = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameNEWTextureAtlas, activity, 
				"mid_center_glow.png", 3, 3);
		
		try
		{
			gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			gameNEWTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
		}
		catch(final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}
	}

	private void createGameFonts()
	{
		FontFactory.setAssetBasePath("fonts/");

		mGameFont = FontFactory.create(
				engine.getFontManager(), 
				engine.getTextureManager(), 512, 512, TextureOptions.BILINEAR,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48f,
				Color.WHITE);
		
		fontTextureAtlas = new BitmapTextureAtlas(engine.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		
		mGameNumbersFont = FontFactory.create(
				engine.getFontManager(),
				fontTextureAtlas,
				Typeface.createFromAsset(activity.getAssets(), FontFactory.getAssetBasePath() + "Prototype.ttf"),
				48, true, Color.WHITE);

		mInfoTabFont = FontFactory.create(engine.getFontManager(), 
				engine.getTextureManager(), 512, 512, TextureOptions.BILINEAR,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32f,
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
			
			maps.add(new Map(i, currentMapPath + "descr.des", currentMapPath + "regions.csv", currentMapPath + "neighbours.csv"));
		}
	}
	
	public void loadGameSceneResources()
	{		
		// Texture Atlas
		gameTextureAtlas.load();
		gameNEWTextureAtlas.load();
		
		// Fonts
		mGameFont.load();
		mInfoTabFont.load();
		mGameNumbersFont.load();
		
		createAndLoadGameMapResources();
	}

	private void createAndLoadGameMapResources()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "game/" /*+ GameInfo.currentMapIndex + "/"*/);
		
		int mapTextureWidth = 4096, mapTextureHeight = 4096;

		mapTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),
				mapTextureWidth, mapTextureHeight, TextureOptions.REPEATING_BILINEAR);
		
		map = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mapTextureAtlas, activity,
				"map.png");
		
//		regions = new ITextureRegion[GameInfo.currentMap.getNumberOfRegions()];
//		
//		for(int i = 0; i < GameInfo.currentMap.getNumberOfRegions(); i++)
//		{
//			regions[i] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mapTextureAtlas, activity,
//					i + ".png");
//		}
		
		try
		{
			mapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
		}
		catch(final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}
		
		mapTextureAtlas.load();
	}
	
	public void unloadGameSceneResources()
	{
		gameTextureAtlas.unload();
		gameNEWTextureAtlas.unload();
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
//	public static ResourceCache getSharedInstance() {
//		return instance;	
//	}

}
