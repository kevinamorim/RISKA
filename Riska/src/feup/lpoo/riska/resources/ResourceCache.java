package feup.lpoo.riska.resources;

import java.io.IOException;
import java.util.ArrayList;

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
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.Log;
import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.io.FileRead;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.music.Conductor;
import feup.lpoo.riska.scenes.CameraManager;


public class ResourceCache {

	// ======================================================
	// FIELDS
	// ======================================================
	public MainActivity activity;
	public Engine engine;
	public SmoothCamera camera;
	public VertexBufferObjectManager vbom;
	public Conductor conductor;
	
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
	public TiledTextureRegion textBtnRegion;
	public TiledTextureRegion optionsBtnRegion;

	// ==================================================
	// GAME RESOURCES
	// ==================================================
	
	// Texture used as the background
	protected BitmapTextureAtlas mWindowTexture;
	protected ITextureRegion mWindowTextureRegion;
	
	// Texture used as the result
	protected BitmapTextureAtlas mResultTexture;
	protected ITextureRegion mResultTextureRegion;



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

	protected BitmapTextureAtlas autoDeployButtonTextureAtlas;
	protected TiledTextureRegion autoDeployButtonTiledTextureRegion;

	protected BitmapTextureAtlas mSeaTextureAtlas;
	protected TiledTextureRegion mSeaTiledTextureRegion;

	protected BitmapTextureAtlas mapTextureAtlas;
	protected ITextureRegion mapTextureRegion;

	// ======================================================
	// FONTS
	// ======================================================
	public Font mSplashFont;
	public Font mainMenuFont;

	protected BitmapTextureAtlas mFontTexture;
	protected Font mFont;

	protected BitmapTextureAtlas mGameFontTexture;
	protected Font mGameFont;

	public Font mInfoTabFont;
	// ======================================================
	// ======================================================

	protected Map map;

	private static int SEA_COLS = 4;
	private static int SEA_LINES = 2;	

	private static ResourceCache instance = new ResourceCache();
	// ======================================================
	// ======================================================

	
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
	
	private void loadMainMenuGraphics() {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		
		int bgTextureWidth = 2048, bgTextureHeight = 1024;
		
		menuBackgroundTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 
				bgTextureWidth, bgTextureHeight, TextureOptions.DEFAULT);
		menuBackgroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuBackgroundTextureAtlas, 
				activity, "background.png", 0, 0);
		
		menuBackgroundTextureAtlas.load();
		
		int mainTextureWidth = 512, mainTextureHeight = 2048;
		
		mainMenuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 
				mainTextureWidth, mainTextureHeight, TextureOptions.BILINEAR);
		
		textBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mainMenuTextureAtlas, activity, 
				"button.png", 1, 2);
		optionsBtnRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mainMenuTextureAtlas, activity, 
				"options.png", 1, 2); 
			
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
				engine.getTextureManager(), 256, 512, TextureOptions.BILINEAR,
				activity.getAssets(), "reprise.ttf", 125f, true,
				Color.WHITE);

		mainMenuFont.load();
		
	}
	
	// ==================================================
	// GAME SCENE
	// ==================================================


	public void loadGameSceneResources() {

		try
		{
			String fontPath = activity.getResources().getString(R.string.path_font_game);

			String imgMap = activity.getResources().getString(R.string.path_img_map);
			String imgRegionButtons = activity.getResources().getString(R.string.path_img_region_button);
			String imgFlags = activity.getResources().getString(R.string.path_img_flags);
			String imgSea = activity.getResources().getString(R.string.path_img_sea);
			String imgAttackButton = activity.getResources().getString(R.string.path_img_attack_button);
			String imgInfoTab = activity.getResources().getString(R.string.path_img_info_tab);
			String imgDetailsButton = activity.getResources().getString(R.string.path_img_details_button);
			String imgAutoDeployButton = activity.getResources().getString(R.string.path_img_auto_deploy_button);
			String imgWindow = activity.getResources().getString(R.string.path_img_window);
			String imgResult = activity.getResources().getString(R.string.path_img_result_button);

			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
			
			// LOADS MAP TEXTURE
			mapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 4096, 2048, TextureOptions.DEFAULT);
			mapTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mapTextureAtlas, activity, imgMap, 0, 0);

			mapTextureAtlas.load();

			// LOADS REGION CIRCULAR BUTTON
			regionButtonTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 128, 256, TextureOptions.DEFAULT);
			regionButtonTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(regionButtonTextureAtlas, 
					activity.getAssets(), imgRegionButtons, 0, 0, 1, 2);

			regionButtonTextureAtlas.load();

			new CameraManager();

			// Loads flags. 
			mFlagsTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 256, TextureOptions.DEFAULT);
			mFlagsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mFlagsTextureAtlas, activity, imgFlags, 0, 0);

			mFlagsTextureAtlas.load();

			// LOADS FONT
			FontFactory.setAssetBasePath("fonts/");

			mGameFont = FontFactory.createFromAsset(engine.getFontManager(),
					engine.getTextureManager(), 256, 512, TextureOptions.BILINEAR,
					activity.getAssets(), fontPath, 48f, true,
					Color.WHITE); /* Load the font in white, so setColor() works. */

			mGameFont.load();

			/* TODO: Change info tab font. */
			mInfoTabFont = FontFactory.create(engine.getFontManager(), 
					engine.getTextureManager(), 512, 512, 
					Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 28);

			mInfoTabFont.load();

			// SEA
			mSeaTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.DEFAULT);
			mSeaTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSeaTextureAtlas, 
					activity.getAssets(), imgSea, 0, 0, SEA_COLS, SEA_LINES);

			mSeaTextureAtlas.load();

			// =================================================================================
			// MAP / REGIONS 
			// ================================================================================= 
			String regionsFile = activity.getResources().getString(R.string.path_file_regions);
			String neighboursFile = activity.getResources().getString(R.string.path_file_neighbours);

			map = new Map(readRegions(regionsFile));
			readNeighbours(neighboursFile);


			// HUD
//			attackButtonTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);
//			attackButtonTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(attackButtonTextureAtlas, 
//					activity.getAssets(), imgAttackButton, 0, 0, 1, 2);
//
//			attackButtonTextureAtlas.load();
			
			attackButtonTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.DEFAULT);
			attackButtonTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(attackButtonTextureAtlas, 
					activity.getAssets(), imgAttackButton, 0, 0, 2, 1);

			attackButtonTextureAtlas.load();

			infoTabTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 256, TextureOptions.DEFAULT);
			infoTabTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(infoTabTextureAtlas, 
					activity.getAssets(), imgInfoTab, 0, 0, 1, 2);

			infoTabTextureAtlas.load();

			detailsButtonTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.DEFAULT);
			detailsButtonTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(detailsButtonTextureAtlas, 
					activity.getAssets(), imgDetailsButton, 0, 0, 2, 1);

			detailsButtonTextureAtlas.load();

			autoDeployButtonTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 512, 256, TextureOptions.DEFAULT);
			autoDeployButtonTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(autoDeployButtonTextureAtlas, 
					activity.getAssets(), imgAutoDeployButton, 0, 0, 2, 1);

			autoDeployButtonTextureAtlas.load();

			// LOADS WINDOW BACKGROUND
			mWindowTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.DEFAULT);
			mWindowTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mWindowTexture, 
					activity, imgWindow, 0, 0);

			mWindowTexture.load();
			
			mResultTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.DEFAULT);
			mResultTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mResultTexture, 
					activity, imgResult, 0, 0);

			mResultTexture.load();


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Loads all sound related resources and creates the conductor.
	 */
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

	/**
	 * Loads a music with the given filename.
	 */
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
		
		checkNeighbours();
	}

	private void checkNeighbours() {
		for(Region reg : map.getRegions())
		{
			for(Region region : reg.getNeighbours())
			{
				if(!region.getNeighbours().contains(reg))
				{
					Log.d("Regions","Region " + reg.getName() + " (" + reg.getId() + ") has neighbour " + region.getName() + " (" + region.getId() + ") but not vice-versa.");
				}
			}
		}
	}

	public Font getGameFont() {
		return this.mGameFont;
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

	public TiledTextureRegion getReturnButtonTexture() {
		return this.mReturnButtonTiledTextureRegion;
	}

	public TiledTextureRegion getSliderButtonTexture() {
		return this.mSliderButtonTiledTextureRegion;
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

	public TiledTextureRegion getAutoDeployButtonTexture() {
		return autoDeployButtonTiledTextureRegion;
	}

	public ITextureRegion getWindowTexture() {
		return mWindowTextureRegion;
	}

	public ITextureRegion getResultTexture()
	{
		return mResultTextureRegion;
	}
	
	public static void prepareManager(Engine engine, MainActivity activity, 
			SmoothCamera camera, VertexBufferObjectManager vbom) {
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
