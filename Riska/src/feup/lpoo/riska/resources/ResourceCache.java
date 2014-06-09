package feup.lpoo.riska.resources;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.io.FileRead;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.music.Conductor;
import feup.lpoo.riska.scenes.CameraManager;

/**
 * Class that handles the loading of all of the game resources, storing them for future access.
 */
public class ResourceCache {


	// ======================================================
	// SINGLETONS
	// ======================================================

	private MainActivity activity;
	private Engine engine;
	private Conductor conductor;
	
	// ======================================================
	// FIELDS
	// ======================================================

	// Logo texture used in the splash screen.
	protected BitmapTextureAtlas mLogoTexture;
	protected ITextureRegion mLogoTextureRegion;

	// Texture used as the background in several menus and scenes.
	protected BitmapTextureAtlas mBackgroundTexture;
	protected ITextureRegion mBackgroundTextureRegion;

	// Texure used in the start button
	protected BitmapTextureAtlas mStartButtonTextureAtlas;
	protected TiledTextureRegion mStartButtonTiledTextureRegion;

	// Texture used in the options menu button
	protected BitmapTextureAtlas mOptionsButtonTextureAtlas;
	protected TiledTextureRegion mOptionsButtonTiledTextureRegion;
	
	protected BitmapTextureAtlas mReturnButtonTextureAtlas;
	protected TiledTextureRegion mReturnButtonTiledTextureRegion;

	protected BitmapTextureAtlas mSliderButtonTextureAtlas;
	protected TiledTextureRegion mSliderButtonTiledTextureRegion;

	protected BitmapTextureAtlas mFlagsTextureAtlas;
	protected ITextureRegion mFlagsTextureRegion;

	protected BitmapTextureAtlas regionButtonTextureAtlas;
	protected TiledTextureRegion regionButtonTiledTextureRegion;
	
	protected BitmapTextureAtlas attackButtonTextureAtlas;
	protected TiledTextureRegion attackButtonTiledTextureRegion;
	
	protected BitmapTextureAtlas infoTabTextureAtlas;
	protected TiledTextureRegion infoTabTiledTextureRegion;
	
	protected BitmapTextureAtlas detailsButtonTextureAtlas;
	protected TiledTextureRegion detailsButtonTiledTextureRegion;
	
	protected BitmapTextureAtlas mSeaTextureAtlas;
	protected TiledTextureRegion mSeaTiledTextureRegion;

	protected BitmapTextureAtlas mapTextureAtlas;
	protected ITextureRegion mapTextureRegion;
	
	// ======================================================
	// FONTS
	// ======================================================
	protected BitmapTextureAtlas mFontTexture;
	protected Font mFont;

	protected BitmapTextureAtlas mGameFontTexture;
	protected Font mGameFont;

	public Font mInfoTabFont;
	// ======================================================
	// ======================================================

	protected Map map;

	private final int SEA_COLS = 4;
	private final int SEA_LINES = 2;	
	
	
	static ResourceCache instance;

	public ResourceCache(MainActivity activity, Engine engine, Camera camera) {
		instance = this;

		this.activity = activity;
		this.engine = engine;
	}

	public static ResourceCache getSharedInstance() {
		return instance;	
	}

	/**
	 * Loads all resources needed by our splash scene. 
	 */
	public void loadSplashSceneResources() {

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// LOADS LOGO PIC
		mLogoTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.DEFAULT);

		mLogoTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLogoTexture, activity, "logo.png", 0, 0);

		mLogoTexture.load();	

	}

	/**
	 * Loads all resources needed by our main menu.
	 */
	public void loadMainMenuResources() {

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// LOADS MENU BACKGROUND
		mBackgroundTexture = new BitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.DEFAULT);
		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTexture, 
				activity, "background.png", 0, 0);

		mBackgroundTexture.load();

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

	/**
	 * Loads all game resources.
	 */
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

		new CameraManager();

		// Loads flags. 
		mFlagsTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 256, TextureOptions.DEFAULT);
		mFlagsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mFlagsTextureAtlas, activity, "flags.png", 0, 0);

		mFlagsTextureAtlas.load();

		// LOADS FONT
		FontFactory.setAssetBasePath("fonts/");

		mGameFont = FontFactory.createFromAsset(engine.getFontManager(),
				engine.getTextureManager(), 256, 512, TextureOptions.BILINEAR,
				activity.getAssets(), "reprise.ttf", 48f, true,
				Color.WHITE); /* Load the font in white, so setColor() works. */

		mGameFont.load();
		
		/* TODO: Change info tab font. */
		mInfoTabFont = FontFactory.create(engine.getFontManager(), 
				engine.getTextureManager(), 512, 512, 
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 20);
		
		mInfoTabFont.load();


		// =================================================================================
		// MAP / REGIONS 
		// ================================================================================= 
		String filename = "regions.csv";

		map = new Map(readRegions(filename));

		filename = "neighbours.csv";

		readNeighbours(filename);

		mSeaTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.DEFAULT);
		mSeaTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSeaTextureAtlas, 
				activity.getAssets(), "sea.png", 0, 0, SEA_COLS, SEA_LINES);

		mSeaTextureAtlas.load();
		
		/*
		 * HUD
		 */
		attackButtonTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);
		attackButtonTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(attackButtonTextureAtlas, 
				activity.getAssets(), "attack_button_5.png", 0, 0, 1, 2);
		
		attackButtonTextureAtlas.load();
		
		infoTabTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 256, TextureOptions.DEFAULT);
		infoTabTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(infoTabTextureAtlas, 
				activity.getAssets(), "info_tab.png", 0, 0, 1, 2);
		
		infoTabTextureAtlas.load();
		
		detailsButtonTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.DEFAULT);
		detailsButtonTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(detailsButtonTextureAtlas, 
				activity.getAssets(), "details_button_2.png", 0, 0, 2, 1);
		
		detailsButtonTextureAtlas.load();
	}

	/**
	 * Loads all sound related resources and creates the conductor.
	 */
	public void loadMusic() {

		conductor = new Conductor();

		try
		{
			Music backgroundMusic;
			backgroundMusic = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity, "sounds/music.mp3");

			conductor.setBackgroundMusic(backgroundMusic);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Reads the regions file.
	 * 
	 * @param filename : file to read
	 * @return The map regions
	 */
	private ArrayList<Region> readRegions(String filename) {
		ArrayList<String> mapData = new ArrayList<String>();

		new FileRead(filename, mapData);

		ArrayList<Region> regions = new ArrayList<Region>();

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

			regions.add(newRegion);
		}

		return regions;

	}

	/**
	 * Reads the neighbours file.
	 * 
	 * @param filename : file to read
	 */
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

	public Font getGameFont() {
		return this.mGameFont;
	}

	public Font getFont() {
		return this.mFont;
	}

	public ITextureRegion getMapTexture() {
		return this.mapTextureRegion;
	}

	public ITiledTextureRegion getSeaTexture() {
		return this.mSeaTiledTextureRegion;
	}

	public Map getMap() {
		return this.map;
	}

	public ITextureRegion getMenuBackgroundTexture() {
		return this.mBackgroundTextureRegion;
	}

	public TiledTextureRegion getStartButtonTexture() {
		return this.mStartButtonTiledTextureRegion;
	}

	public TiledTextureRegion getOptionsButtonTexture() {
		return this.mOptionsButtonTiledTextureRegion;
	}

	public TiledTextureRegion getReturnButtonTexture() {
		return this.mReturnButtonTiledTextureRegion;
	}

	public TiledTextureRegion getSliderButtonTexture() {
		return this.mSliderButtonTiledTextureRegion;
	}

	public ITextureRegion getLogo() {
		return this.mLogoTextureRegion;
	}

	public ITextureRegion getRegionFlags() {
		return this.mFlagsTextureRegion;
	}

	public ITiledTextureRegion getRegionButtonTexture() {
		return this.regionButtonTiledTextureRegion;
	}

	public TiledTextureRegion getAttackButtonTexture() {
		return this.attackButtonTiledTextureRegion;
	}
	

	public TiledTextureRegion getInfoTabTexture() {
		return infoTabTiledTextureRegion;
	}

	public TiledTextureRegion getDetailsButtonTexture() {
		return detailsButtonTiledTextureRegion;
	}
}
