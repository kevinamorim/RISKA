package feup.lpoo.riska.resources;

import org.andengine.engine.Engine;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
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
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Color;
import android.graphics.Typeface;
import feup.lpoo.riska.gameInterface.CameraManager;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.sound.Conductor;
import feup.lpoo.riska.sound.RiskaMusic;
import feup.lpoo.riska.sound.RiskaSound;

public class ResourceCache {

	// ======================================================
	// FIELDS
	// ======================================================
	public MainActivity activity;
	public Engine engine;
	public CameraManager camera;
	public VertexBufferObjectManager vbom;

	public static ResourceCache instance = new ResourceCache();

	// CURRENT THEME - CHANGE IF NEED BE
	private static String currentTheme = "new/";

    public static enum RESOURCE { SPLASH, LOADING, MENU, GAME, GAME_MAP };

    // ======================================================
	// RESOURCES
    // ======================================================
	private BitmapTextureAtlas splashTextureAtlas;
	public TextureRegion splashRegion;

	private BitmapTextureAtlas loadingTextureAtlas;
	//public ITextureRegion loadingBackground;

	private BuildableBitmapTextureAtlas menuBackgroundTextureAtlas;
	public ITextureRegion menuBackground;

	private BuildableBitmapTextureAtlas mainMenuTextureAtlas;
	public ITextureRegion solidNoBorder;
	public ITextureRegion buttonGenericSmall;
	public ITextureRegion buttonGenericMedium;
	public ITextureRegion buttonGenericLarge;
	public ITiledTextureRegion buttonRegion;
	
	private BuildableBitmapTextureAtlas gameTextureAtlas;

	// FONTS
	private BitmapTextureAtlas fontTextureAtlas;
	private BitmapTextureAtlas menuFontTextureAtlas;

	//public Font mSplashFont;
	public Font mMenuFont;
	public Font mInfoTabFont;
	public Font mGameFont;
	public Font mGameNumbersFont;
	public Font mGameOverFont;


    // ==================================================
    // PUBLIC METHODS
    // ==================================================
    public void createAndLoadResources(RESOURCE x) {

        switch(x) {
            case SPLASH:
                break;

            case LOADING:
                break;

            case MENU:
                break;

            case GAME:
                break;

            case GAME_MAP:
                 break;

            default:
                return;
        }

        createResources(x);
        loadResources(x);
    }

    public void createResources(RESOURCE x) {

        switch(x) {
            case SPLASH:
                createSplashScene();
                break;

            case LOADING:
                createLoadingScene();
                break;

            case MENU:
                createMenuBackground();
                createMenuButtons();
                createMenuMaps();
                createMenuFonts();
                //createMenuMusic(); TODO
                //createMenuSounds(); TODO
                break;

            case GAME:
                createGameBackground();
                createGameButtons();
                createGameFonts();
                //createGameMusic(); TODO
                //createGameSounds(); TODO
                break;

            case GAME_MAP:
                // TODO
                break;

            default:
                Debug.d("Resources","createResources(RESOURCE x) : x is not a valid RESOURCE");
                break;
        }
    }

    public void loadResources(RESOURCE x) {

        switch(x) {
            case SPLASH:
                splashTextureAtlas.load();
                break;

            case LOADING:
                loadingTextureAtlas.load();
                break;

            case MENU:
                menuBackgroundTextureAtlas.load();
                mainMenuTextureAtlas.load();
                mMenuFont.load();
                break;

            case GAME:
                gameTextureAtlas.load();
                mGameFont.load();
                mInfoTabFont.load();
                mGameNumbersFont.load();
                break;

            case GAME_MAP:
                // TODO
                break;

            default:
                Debug.d("Resources","loadResources(RESOURCE) : " + x + " is not a valid RESOURCE");
                break;
        }
    }

    public void unloadResources(RESOURCE x) {

        switch(x) {
            case SPLASH:
                splashTextureAtlas.unload();
                break;

            case LOADING:
                loadingTextureAtlas.unload();
                break;

            case MENU:
                menuBackgroundTextureAtlas.unload();
                mainMenuTextureAtlas.unload();
                mMenuFont.unload();
                break;

            case GAME:
                gameTextureAtlas.unload();
                mGameFont.unload();
                mInfoTabFont.unload();
                mGameNumbersFont.unload();
                break;

            case GAME_MAP:
                // TODO
                break;

            default:
                Debug.d("Resources","unloadResources(RESOURCE) : " + x + " is not a valid RESOURCE");
                break;
        }
    }

	// ==================================================
	// SPLASH SCENE
	// ==================================================
	private void createSplashScene() {
		int textureWidth = 512, textureHeight = 512;

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "splash/");

		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 
				textureWidth, textureHeight, TextureOptions.BILINEAR);
		
		splashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, 
				activity, "splash.png", 0, 0);
	}

	// ==================================================
	// LOADING SCENE
	// ==================================================
	private void createLoadingScene() {

		int textureWidth = 4096, textureHeight = 2048;

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "loading/");

		loadingTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 
				textureWidth, textureHeight, TextureOptions.BILINEAR);
	}

	// ==================================================
	// MAIN MENU SCENE
	// ==================================================
	private void createMenuBackground() {
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "menu/background/");
		
		int TextureWidth = 1024, TextureHeight = 1024;
		
		menuBackgroundTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 
				TextureWidth, TextureHeight, TextureOptions.BILINEAR);
		
		menuBackground = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(menuBackgroundTextureAtlas,
				activity, "background_0.svg", 960, 540);
		
		try
		{
			menuBackgroundTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
		}
		catch(final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}
	}

	private void createMenuButtons() {

		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "menu/props/");
		
		int atlasWidth = 1024, atlasHeight = 1024;
		
		mainMenuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 
				atlasWidth, atlasHeight, TextureOptions.BILINEAR);
		
		solidNoBorder = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTextureAtlas,
				activity, "blank_region.svg", 96, 54);
		
		buttonGenericSmall = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTextureAtlas,
				activity, "button_generic_small.svg", 64, 64);
		
		buttonGenericMedium = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTextureAtlas,
				activity, "button_generic_medium.svg", 256, 256);

		buttonGenericLarge = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTextureAtlas,
				activity, "button_generic_diamond.svg", 512, 512);

		try
		{
			mainMenuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
		}
		catch(final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}
	}
	
	private void createMenuMaps() {
		// TODO
	}

	private void createMenuFonts() {

		FontFactory.setAssetBasePath("fonts/");
		
		menuFontTextureAtlas = new BitmapTextureAtlas(engine.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

		mMenuFont = FontFactory.create(engine.getFontManager(), menuFontTextureAtlas,
				Typeface.createFromAsset(activity.getAssets(), FontFactory.getAssetBasePath() + "Cursive Option.ttf"),
				125, true, Color.WHITE);
	}

    private void createMenuMusic() {

        String musicBasePath = "sounds/menu/music/";

        RiskaMusic music;
        music = new RiskaMusic("background", musicBasePath + "background.mp3", engine.getMusicManager(), activity);

        Conductor.instance.createMusicFolder("menu");

        Conductor.instance.addMusicToFolder("menu", music);
    }

    private void createMenuSounds() {

        String soundBasePath = "sounds/menu/sfx/";

        RiskaSound sound;
        sound = new RiskaSound("clicked", soundBasePath + "background.mp3", engine.getSoundManager(), activity);

        Conductor.instance.createSoundFolder("menu");

        Conductor.instance.addSoundToFolder("menu", sound);
    }

	// ==================================================
	// GAME SCENE
	// ==================================================
	private void createGameBackground() {

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "game/");
		
		int gameTextureWidth = 1024, gameTextureHeight = 1024;

		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),
				gameTextureWidth, gameTextureHeight, TextureOptions.BILINEAR);
		
		try
		{
			gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
		}
		catch(final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}
	}

    private void createGameButtons() {

    }

	private void createGameFonts() {

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
	
	private void createMapsLiterally() {
        // TODO : move this to an appropriate place
/*		if(maps == null)
		{
			maps = new ArrayList<Map>();
		}
		
		for(int i = 0; i < GameOptions.numberOfMaps; i++)
		{
			String currentMapPath = "maps/" + i + "/";
			
			maps.add(new Map(i, currentMapPath + "descr.des", currentMapPath + "regions.csv", currentMapPath + "neighbours.csv"));
		}*/
	}

    private void createGameMusic() {

        String musicBasePath = "sounds/game/music";

        RiskaMusic music;
        music = new RiskaMusic("background", musicBasePath + "background.mp3",engine.getMusicManager(),activity);

        Conductor.instance.createMusicFolder("menu");

        Conductor.instance.addMusicToFolder("menu", music);
    }

    private void createGameSounds() {

        String soundBasePath = "sounds/game/sfx/";

        RiskaSound sound;
        sound = new RiskaSound("background", soundBasePath + "background.mp3",engine.getSoundManager(),activity);

        Conductor.instance.createSoundFolder("game");

        Conductor.instance.addSoundToFolder("game", sound);
    }

	public static void prepareManager(Engine engine, MainActivity activity, CameraManager camera, VertexBufferObjectManager vbom) {
		instance.engine = engine;
		instance.activity = activity;
		instance.camera = camera;
		instance.vbom = vbom;
	}

}
