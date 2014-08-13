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

	private static String currentTheme = "vanilla/";
	private static int numberOfMaps = 1;
	private final int INITIAL_MAP = 1;

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
	
	public TiledTextureRegion factionSpriteRegion;
	
	public TiledTextureRegion playerAddButtonRegion;
	public TiledTextureRegion playerRemoveButtonRegion;
	public TiledTextureRegion playerCheckBoxButtonRegion;
	// ==================================================
	// GAME RESOURCES
	// ==================================================
	public ITiledTextureRegion regionBtnRegion;
	public ITiledTextureRegion attackBtnRegion;
	public ITiledTextureRegion detailsBtnRegion;
	public ITiledTextureRegion autoDeployBtnRegion;
	public ITiledTextureRegion moveBtnRegion;
	public ITiledTextureRegion infoTabRegion;
	public ITextureRegion windowRegion;
	public ITextureRegion windowRegionGeneric;
	public ITextureRegion windowRegionGenericInverted;
	public ITiledTextureRegion arrowRightRegion;
	public ITiledTextureRegion seaRegion;
	public ITextureRegion mapRegion;
	private BuildableBitmapTextureAtlas gameTextureAtlas;
	private BitmapTextureAtlas mapTextureAtlas;
	private BitmapTextureAtlas seaTextureAtlas;
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
	public void loadSplashSceneResources()
	{
		loadSplashGraphics();
	}

	public void unloadSplashSceneResources()
	{
		splashTextureAtlas.unload();
		splashRegion = null;
	}

	private void loadSplashGraphics()
	{

		int textureWidth = 512, textureHeight = 512;

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "splash/");

		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 
				textureWidth, textureHeight, TextureOptions.BILINEAR);
		splashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, 
				activity, "splash.png", 0, 0);

		splashTextureAtlas.load();

	}

	// ==================================================
	// MAIN MENU SCENE
	// ==================================================
	public void loadMainMenuResources()
	{
		loadMainMenuGraphics();
		loadMainMenuFonts();
	}

	public void unloadMainMenuResources()
	{
		mainMenuTextureAtlas.unload();
		menuBackgroundTextureAtlas.unload();

	}

	private void loadMainMenuGraphics()
	{		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "menu/");

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

		int mainTextureWidth = 2048, mainTextureHeight = 2048;

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
		
		factionSpriteRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mainMenuTextureAtlas, activity, 
				"faction.png", 1, 3);
		
		playerAddButtonRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mainMenuTextureAtlas, activity, 
				"plus_button.png", 1, 2);
		
		playerRemoveButtonRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mainMenuTextureAtlas, activity, 
				"minus_button.png", 1, 2);
		
		playerCheckBoxButtonRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mainMenuTextureAtlas, activity, 
				"check_box.png", 1, 2);

		try
		{
			mainMenuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			
			mainMenuTextureAtlas.load();
		}
		catch(final TextureAtlasBuilderException e)
		{
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
		
		loadMaps();
		

	}
	
	public void loadMaps()
	{
		if(maps == null)
		{
			maps = new ArrayList<Map>();
		}
		
		for(int i = INITIAL_MAP; i <= numberOfMaps; i++)
		{
			maps.add(new Map("maps/" + i + "/regions.csv", "maps/" + i + "/neighbours.csv"));
		}
	}

	public void unloadGameSceneResources()
	{
		gameTextureAtlas.unload();
		seaTextureAtlas.unload();
		mapTextureAtlas.unload();
	}

	private void loadGameGraphics() {

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "game/");

		int seaTextureWidth = 4096, seaTextureHeight = 2048, seaCols = 2, seaLines = 2;
		int mapTextureWidth = 4096, mapTextureHeight = 2048;
		int gameTextureWidth = 2048, gameTextureHeight = 2048;

		seaTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), seaTextureWidth, seaTextureHeight,
				TextureOptions.DEFAULT);
		seaRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(seaTextureAtlas, activity.getAssets(),
				"sea.png", 0, 0, seaCols, seaLines);



		mapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), mapTextureWidth, mapTextureHeight, 
				TextureOptions.DEFAULT);
		mapRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mapTextureAtlas, activity,
				"map.png", 0, 0);



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
		
		moveBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, 
				"move.png", 2, 1);

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
