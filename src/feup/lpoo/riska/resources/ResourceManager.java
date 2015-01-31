package feup.lpoo.riska.resources;

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
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;

import android.graphics.Color;
import android.graphics.Typeface;

import feup.lpoo.riska.gameInterface.CameraManager;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.sound.Conductor;
import feup.lpoo.riska.sound.RiskaMusic;
import feup.lpoo.riska.sound.RiskaSound;
import feup.lpoo.riska.utilities.Utils;


public class ResourceManager {

	// ======================================================
	// FIELDS
	// ======================================================
	private MainActivity activity;
    private Engine engine;
	private static String currentTheme = "new/";

    public VertexBufferObjectManager vbom;

    // ======================================================
	// RESOURCES
    // ======================================================
	private BitmapTextureAtlas splashTextureAtlas;
	public ITextureRegion splashLogo;

	private BuildableBitmapTextureAtlas loadingTextureAtlasBackground;
	public ITextureRegion loadingBackground;

	private BuildableBitmapTextureAtlas menuTextureAtlasBackground;
    private BuildableBitmapTextureAtlas menuTextureAtlasButtons;
	public ITextureRegion menuBackground;
    public ITextureRegion menuButtonStartGame;
	
	private BuildableBitmapTextureAtlas gameTextureAtlasBackground;
    public ITextureRegion gameBackground;

	//public Font mSplashFont;
	public Font mMenuFont;
	public Font mInfoTabFont;
	public Font mGameFont;
	public Font mGameNumbersFont;
	//public Font mGameOverFont;

    // ==================================================
    // SINGLETON
    // ==================================================
    public static ResourceManager instance = new ResourceManager();

    // ==================================================
    // PUBLIC METHODS
    // ==================================================
    public void createAndLoadResources(Utils.CONTEXT x) {

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

    public void createResources(Utils.CONTEXT x) {

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
                //createMenuAudio(); TODO
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
                Debug.d("Resources","createResources(CONTEXT x) : x is not a valid CONTEXT");
                break;
        }
    }

    public void loadResources(Utils.CONTEXT x) {

        switch(x) {
            case SPLASH:
                splashTextureAtlas.load();
                break;

            case LOADING:
                loadingTextureAtlasBackground.load();
                break;

            case MENU:
                menuTextureAtlasBackground.load();
                menuTextureAtlasButtons.load();
                mMenuFont.load();
                break;

            case GAME:
                gameTextureAtlasBackground.load();
                mGameFont.load();
                mInfoTabFont.load();
                mGameNumbersFont.load();
                break;

            case GAME_MAP:
                // TODO
                break;

            default:
                Debug.d("Resources","loadResources(CONTEXT) : " + x + " is not a valid CONTEXT");
                break;
        }
    }

    public void unloadResources(Utils.CONTEXT x) {

        switch(x) {
            case SPLASH:
                splashTextureAtlas.unload();
                break;

            case LOADING:
                loadingTextureAtlasBackground.unload();
                break;

            case MENU:
                menuTextureAtlasBackground.unload();
                menuTextureAtlasButtons.unload();
                mMenuFont.unload();
                break;

            case GAME:
                gameTextureAtlasBackground.unload();
                mGameFont.unload();
                mInfoTabFont.unload();
                mGameNumbersFont.unload();
                break;

            case GAME_MAP:
                // TODO
                break;

            default:
                Debug.d("Resources","unloadResources(CONTEXT) : " + x + " is not a valid CONTEXT");
                break;
        }
    }

    // ==================================================
    // PRIVATE METHODS
    // ==================================================
	// Splash Scene
    private void createSplashScene() {
		int textureWidth = 512, textureHeight = 512;

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "splash/");

		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 
				textureWidth, textureHeight, TextureOptions.BILINEAR);
		
		splashLogo = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas,
				activity, "splash.png", 0, 0);
	}

    // Loading Scene
	private void createLoadingScene() {
        SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "loading/background/");

		int textureWidth = 4096, textureHeight = 2048;

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "loading/");

		loadingTextureAtlasBackground = new BuildableBitmapTextureAtlas(activity.getTextureManager(),
				textureWidth, textureHeight, TextureOptions.BILINEAR);

        loadingBackground = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlasBackground,
                activity, "background.svg", 960, 540);

        try
        {
            loadingTextureAtlasBackground.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
        }
        catch(final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }
	}

    // Menu Scene
	private void createMenuBackground() {
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "menu/background/");
		
		int TextureWidth = 1024, TextureHeight = 1024;
		
		menuTextureAtlasBackground = new BuildableBitmapTextureAtlas(activity.getTextureManager(),
				TextureWidth, TextureHeight, TextureOptions.BILINEAR);
		
		menuBackground = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlasBackground,
				activity, "background.svg", 960, 540);
		
		try
		{
			menuTextureAtlasBackground.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
		}
		catch(final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}
	}

	private void createMenuButtons() {

		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "menu/props/");
		
		int atlasWidth = 1024, atlasHeight = 1024;
		
		menuTextureAtlasButtons = new BuildableBitmapTextureAtlas(activity.getTextureManager(),
				atlasWidth, atlasHeight, TextureOptions.BILINEAR);
		
		menuButtonStartGame = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlasButtons,
				activity, "button_generic_medium.svg", 256, 256);

		try
		{
			menuTextureAtlasButtons.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
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

        BitmapTextureAtlas menuFontTextureAtlas = new BitmapTextureAtlas(engine.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

		mMenuFont = FontFactory.create(engine.getFontManager(), menuFontTextureAtlas,
				Typeface.createFromAsset(activity.getAssets(), FontFactory.getAssetBasePath() + "Cursive Option.ttf"),
				125, true, Color.WHITE);
	}

    private void createMenuAudio() {

        String musicBasePath = "sounds/menu/music/";
        String soundBasePath = "sounds/menu/sfx/";

        Conductor.instance.createAudioFolder("menu");
        Conductor.instance.addMusicToFolder("menu", new RiskaMusic("background", musicBasePath + "background.mp3"));
        Conductor.instance.addSoundToFolder("menu", new RiskaSound("clicked", soundBasePath + "background.mp3"));
    }

    // Game Scene
	private void createGameBackground() {

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/themes/" + currentTheme + "game/background/");
		
		int gameTextureWidth = 1024, gameTextureHeight = 1024;

		gameTextureAtlasBackground = new BuildableBitmapTextureAtlas(activity.getTextureManager(),
				gameTextureWidth, gameTextureHeight, TextureOptions.BILINEAR);

        menuBackground = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlasBackground,
                activity, "background.svg", 960, 540);
		
		try
		{
			gameTextureAtlasBackground.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
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

        BitmapTextureAtlas fontTextureAtlas = new BitmapTextureAtlas(engine.getTextureManager(), 256, 256, TextureOptions.BILINEAR);

		mGameFont = FontFactory.create(
				engine.getFontManager(), 
				engine.getTextureManager(), 512, 512, TextureOptions.BILINEAR,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48f,
				Color.WHITE);
		
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
        /*
        // TODO : move this to an appropriate place
		if(maps == null)
		{
			maps = new ArrayList<Map>();
		}
		
		for(int i = 0; i < GameOptions.numberOfMaps; i++)
		{
			String currentMapPath = "maps/" + i + "/";
			
			maps.add(new Map(i, currentMapPath + "descr.des", currentMapPath + "regions.csv", currentMapPath + "neighbours.csv"));
		}*/
	}

    private void createGameAudio() {

        String musicBasePath = "sounds/game/music/";
        String soundBasePath = "sounds/game/sfx/";

        Conductor.instance.createAudioFolder("game");
        Conductor.instance.addMusicToFolder("game", new RiskaMusic("background", musicBasePath + "background.mp3"));
        Conductor.instance.addSoundToFolder("game", new RiskaSound("clicked", soundBasePath + "background.mp3"));
    }

    // ==================================================
    // STATIC METHODS
    // ==================================================
	public static void prepareManager(Engine engine, MainActivity activity, CameraManager camera, VertexBufferObjectManager vbom) {
		instance.engine = engine;
		instance.activity = activity;
        instance.vbom = vbom;
	}

}
