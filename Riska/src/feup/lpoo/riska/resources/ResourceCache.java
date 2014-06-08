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
import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.io.FileRead;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.music.Conductor;
import feup.lpoo.riska.scenes.CameraManager;

public class ResourceCache {

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

	public final int NUMBER_OF_REGIONS = 38;
	private int regionsCreated = 0;

	protected Map map;

	protected BitmapTextureAtlas mSeaTextureAtlas;
	protected TiledTextureRegion mSeaTiledTextureRegion;

	/*=============================
	 * =============================
	 */

	private final int SEA_COLS = 4;
	private final int SEA_LINES = 2;

	private MainActivity activity;
	private Engine engine;
	private Conductor conductor;

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


		// =================================================================================
		// MAP / REGIONS 
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

		mSeaTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.DEFAULT);
		mSeaTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSeaTextureAtlas, 
				activity.getAssets(), "sea.png", 0, 0, SEA_COLS, SEA_LINES);

		mSeaTextureAtlas.load();
	}

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

	/**
	 * 
	 * @return TextureRegion for the HUD panel.
	 */
	public ITextureRegion getHUDPanelTexture() {
		return this.mLeftPanelTextureRegion;
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
		return this.mMenuBackgroundTextureRegion;
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
		return this.logoTextureRegion;
	}

	public ITextureRegion getRegionFlags() {
		return this.mFlagsTextureRegion;
	}

	public ITiledTextureRegion getRegionButtonTexture() {
		return this.regionButtonTiledTextureRegion;
	}


}
