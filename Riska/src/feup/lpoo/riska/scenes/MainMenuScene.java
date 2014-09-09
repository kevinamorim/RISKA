package feup.lpoo.riska.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import android.util.Log;
import android.view.MotionEvent;
import feup.lpoo.riska.gameInterface.MenuHUD;
import feup.lpoo.riska.gameInterface.RiskaSprite;
import feup.lpoo.riska.gameInterface.RiskaCanvas;
import feup.lpoo.riska.gameInterface.RiskaMenuItem;
import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.io.IOManager;
import feup.lpoo.riska.logic.GameInfo;
import feup.lpoo.riska.logic.GameOptions;
import feup.lpoo.riska.logic.SceneManager.SCENE_TYPE;
import feup.lpoo.riska.utilities.Utils;

public class MainMenuScene extends BaseScene implements Displayable, IOnMenuItemClickListener {

	// ==================================================
	// CONSTANTS
	// ==================================================
	private enum CHILD { MAIN, OPTIONS, START, NEW, GO, ANY, ALL};

	private enum OPTIONS_TAB { MENU, AUDIO, GAME, NONE };
	private enum OPTIONS_BUTTON { SFX, MUSIC, MENU_ANIMATIONS, GAME_ANIMATIONS};

	private enum NEWGAME_TAB { MAP, PLAYERS, LEVEL, GO, NONE };

	private static float animationTime = GameOptions.animationTime;

	private CHILD currentChild = CHILD.MAIN;
	private OPTIONS_TAB currentOptionsTab;
	private NEWGAME_TAB currentNewGameTab;

	// ==================================================
	// FIELDS
	// ==================================================
	private MenuScene menuMain;
	private MenuScene menuOptions;
	private MenuScene menuStart;
	private MenuScene menuNewGame;
	//private MenuScene menuLoadGame;

	private MenuHUD menuHUD;

	private final int MAIN_START = 0;
	private final int MAIN_OPTIONS = 1;

	private final int START_NEW = 2;
	private final int START_LOAD = 3;

	private final int NEW_GO = 4;

	private SpriteBackground background;

	// --------------------------
	// MAIN MENU
	private RiskaMenuItem menuMainStartButton;
	private RiskaMenuItem menuMainOptionsButton;

	// --------------------------
	// START MENU
	private RiskaMenuItem menuStartNewButton;
	private RiskaMenuItem menuStartLoadButton;

	// --------------------------
	// OPTIONS MENU
	private RiskaSprite menuOptionsMenuTab;
	private RiskaCanvas menuOptionsMenuCanvas;
	private ButtonSprite switchMenuAnimations;
	private Text textMenuAnimations;

	private RiskaSprite menuOptionsGameTab;
	private RiskaCanvas menuOptionsGameCanvas;
	private ButtonSprite switchGameAnimations;
	private Text textGameAnimations;

	private RiskaSprite menuOptionsAudioTab;
	private RiskaCanvas menuOptionsAudioCanvas;
	private ButtonSprite switchAudioSfx;
	private ButtonSprite switchAudioMusic;
	private Text textAudioSfx;
	private Text textAudioMusic;

	// --------------------------
	// NEW GAME MENU
	private RiskaSprite menuNewGameMapTab;
	private RiskaCanvas menuNewGameMapCanvas;
	private RiskaCanvas[] mapCanvas;

	private RiskaSprite menuNewGamePlayersTab;
	private RiskaCanvas menuNewGamePlayersCanvas;
	private RiskaCanvas[] playerCanvas;
	private RiskaSprite[] playerName;
	private ButtonSprite[] playerActiveButton;
	private ButtonSprite[] playerCpuButton;
	private RiskaCanvas[] playerColorButton;

	private RiskaSprite menuNewGameLevelTab;
	private RiskaCanvas menuNewGameLevelCanvas;
	private ButtonSprite[] levelCheckBox;
	private Text[] levelText;

	private RiskaMenuItem menuNewGameGoTab;
	private RiskaCanvas menuNewGameGoCanvas;

	private boolean[] playerIsCpu;
	private boolean[] playerActive;
	private boolean[] playerActivable;
	private boolean[] playerEditable;

	private int[] playerColor;

	private int level = GameOptions.defaultLvl;

	// ==================================================
	// ==================================================

	@Override
	public void createScene()
	{
		currentChild = CHILD.MAIN;
		currentOptionsTab = OPTIONS_TAB.NONE;
		currentNewGameTab = NEWGAME_TAB.NONE;

		createDisplay();
	}

	@Override
	public void onBackKeyPressed()
	{
		if(getEntityModifierCount() == 0)
		{

			if(getChildScene().equals(menuMain))
			{
				IOManager.saveGameOptions();
				System.exit(0);
			}
			else
			{
				//detachChildren();
				changeChildScene(currentChild, CHILD.MAIN);
			}
		}
	}

	@Override
	public void disposeScene()
	{
		detachChildren();
		dispose();
	}

	@Override
	public SCENE_TYPE getSceneType()
	{
		return SCENE_TYPE.MAIN_MENU;
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed)
	{
		super.onManagedUpdate(pSecondsElapsed);
	}


	// ==================================================
	// CREATE DISPLAY
	// ==================================================
	@Override
	public void createDisplay()
	{
		createBackground();
		createHUD();

		createMainMenu();
		createStartGameMenu();
		createOptionsMenu();
		createNewGameMenu();

		setChildScene(currentChild);
	}

	private void createBackground()
	{
		background = new SpriteBackground(new Sprite(
				camera.getCenterX(), 
				camera.getCenterY(),
				camera.getWidth() + 4,
				camera.getHeight() + 4,
				resources.menuBackgroundRegion, 
				vbom));

		setBackground(background);
	}

	private void createHUD()
	{
		menuHUD = new MenuHUD();

		camera.setHUD(menuHUD);
	}

	// ==================================================
	// MAIN MENU
	// ==================================================
	private void createMainMenu()
	{
		menuMain = new MenuScene(camera);

		menuMain.setBackgroundEnabled(false);

		menuMainStartButton = new RiskaMenuItem(MAIN_START,
				resources.buttonRegion, 
				vbom, "Start", resources.mainMenuFont);

		Utils.wrap(menuMainStartButton, 0.5f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		menuMainStartButton.setPosition(camera.getCenterX(), 0.53f * camera.getHeight());
		//startButton.debug();

		menuMainOptionsButton = new RiskaMenuItem(MAIN_OPTIONS,
				resources.buttonRegion, 
				vbom, "Options", resources.mainMenuFont);

		Utils.wrap(menuMainOptionsButton, 0.5f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		menuMainOptionsButton.setPosition(camera.getCenterX(), 0.23f * camera.getHeight());

		menuMain.addMenuItem(menuMainStartButton);
		menuMain.addMenuItem(menuMainOptionsButton);
		menuMain.setOnMenuItemClickListener(this);
		menuMain.setTouchAreaBindingOnActionDownEnabled(true);
		menuMain.setTouchAreaBindingOnActionMoveEnabled(true);
	}

	// ==================================================
	// START GAME
	// ==================================================
	private void createStartGameMenu()
	{
		menuStart = new MenuScene(camera);

		menuStart.setBackgroundEnabled(false);


		menuStartNewButton = new RiskaMenuItem(START_NEW,
				resources.buttonRegion,
				vbom, "New", resources.mainMenuFont);

		menuStartLoadButton = new RiskaMenuItem(START_LOAD,
				resources.buttonRegion,
				vbom, "Load", resources.mainMenuFont);


		Utils.wrap(menuStartNewButton, 0.5f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		menuStartNewButton.setPosition(camera.getCenterX(), 0.53f * camera.getHeight());

		Utils.wrap(menuStartLoadButton, 0.5f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		menuStartLoadButton.setPosition(camera.getCenterX(), 0.23f * camera.getHeight());

		menuStart.addMenuItem(menuStartNewButton);
		menuStart.addMenuItem(menuStartLoadButton);

		menuStart.setOnMenuItemClickListener(this);
		menuStart.setTouchAreaBindingOnActionDownEnabled(true);
		menuStart.setTouchAreaBindingOnActionMoveEnabled(true);

		menuStartNewButton.setAlpha(0f);
		menuStartLoadButton.setAlpha(0f);
	}

	// ==================================================
	// OPTIONS
	// ==================================================
	private void createOptionsMenu()
	{
		menuOptions = new MenuScene(camera);
		menuOptions.setBackgroundEnabled(false);

		createMenuCanvas();
		createGameCanvas();	
		createAudioCanvas();
		createOptionsTabs();

		onOptionsTabSelected(OPTIONS_TAB.MENU);

		menuOptionsMenuCanvas.setAlpha(0f);
		menuOptionsGameCanvas.setAlpha(0f);
		menuOptionsAudioCanvas.setAlpha(0f);

		menuOptionsMenuTab.setAlpha(0f);
		menuOptionsGameTab.setAlpha(0f);
		menuOptionsAudioTab.setAlpha(0f);

		menuOptions.setTouchAreaBindingOnActionDownEnabled(true);
		menuOptions.setTouchAreaBindingOnActionMoveEnabled(true);
	}

	private void createMenuCanvas()
	{
		Sprite frame;

		int maxNumberOfItems = 4;
		float heightFactor = 1f / (maxNumberOfItems + 1);

		int index = maxNumberOfItems;

		frame = new Sprite(0, 0, resources.frameRegion, vbom);

		switchMenuAnimations = new ButtonSprite(0, 0, resources.checkBoxRegion, vbom)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{

				case MotionEvent.ACTION_DOWN:
					break;

				case MotionEvent.ACTION_OUTSIDE:
					break;

				case MotionEvent.ACTION_UP:
					onOptionsButtonTouched(OPTIONS_BUTTON.MENU_ANIMATIONS);
					break;
				}

				return true;
			}
		};
		switchMenuAnimations.setCurrentTileIndex(GameOptions.menuAnimationsEnabled() ? 0 : 1);

		textMenuAnimations = new Text(0, 0, resources.mainMenuFont, "Menu Animations", vbom)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{

				case MotionEvent.ACTION_DOWN:
					break;

				case MotionEvent.ACTION_OUTSIDE:
					break;

				case MotionEvent.ACTION_UP:
					onOptionsButtonTouched(OPTIONS_BUTTON.MENU_ANIMATIONS);
					break;
				}

				return true;
			}
		};
		textMenuAnimations.setColor(GameOptions.menuAnimationsEnabled() ? Color.WHITE : Utils.OtherColors.DARK_GREY);

		menuOptionsMenuCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), 0.8f * camera.getWidth(), 0.65f * camera.getHeight());


		menuOptionsMenuCanvas.addText(textMenuAnimations, 0.25f , index * heightFactor, 0.45f, 0.17f);
		menuOptionsMenuCanvas.addGraphicWrap(switchMenuAnimations, 0.75f , index * heightFactor, 0.25f, 0.17f);
		index--;

		menuOptionsMenuCanvas.addGraphic(frame, 0.5f, 0.5f, 1f, 1f);

		menuOptions.attachChild(menuOptionsMenuCanvas);
	}

	private void createGameCanvas()
	{
		Sprite frame;

		int maxNumberOfItems = 4;
		float heightFactor = 1f / (maxNumberOfItems + 1);

		int index = maxNumberOfItems;

		frame = new Sprite(0, 0, resources.frameRegion, vbom);

		switchGameAnimations = new ButtonSprite(0, 0, resources.checkBoxRegion, vbom)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{

				case MotionEvent.ACTION_DOWN:
					break;

				case MotionEvent.ACTION_OUTSIDE:
					break;

				case MotionEvent.ACTION_UP:
					onOptionsButtonTouched(OPTIONS_BUTTON.GAME_ANIMATIONS);
					break;
				}

				return true;
			}

		};
		switchGameAnimations.setCurrentTileIndex(GameOptions.menuAnimationsEnabled() ? 0 : 1);

		textGameAnimations = new Text(0, 0, resources.mainMenuFont, "Game Animations", vbom);
		textGameAnimations.setColor(Color.WHITE);

		menuOptionsGameCanvas = new RiskaCanvas(
				camera.getCenterX(), camera.getCenterY(),
				0.8f * camera.getWidth(), 0.65f * camera.getHeight());


		menuOptionsGameCanvas.addText(textGameAnimations, 0.25f , index * heightFactor, 0.45f, 0.17f);
		menuOptionsGameCanvas.addGraphicWrap(switchGameAnimations, 0.75f , index * heightFactor, 0.25f, 0.17f);
		index--;

		menuOptionsGameCanvas.addGraphic(frame, 0.5f, 0.5f, 1f, 1f);

		menuOptions.attachChild(menuOptionsGameCanvas);
	}

	private void createAudioCanvas()
	{
		Sprite frame;

		int maxNumberOfItems = 4;
		float heightFactor = 1f / (maxNumberOfItems + 1);

		int index = maxNumberOfItems;

		frame = new Sprite(0, 0, resources.frameRegion, vbom);

		switchAudioMusic = new ButtonSprite(0, 0, resources.checkBoxRegion, vbom)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{

				case MotionEvent.ACTION_DOWN:
					break;

				case MotionEvent.ACTION_OUTSIDE:
					break;

				case MotionEvent.ACTION_UP:
					onOptionsButtonTouched(OPTIONS_BUTTON.MUSIC);
					break;
				}

				return true;
			}

		};

		switchAudioSfx = new ButtonSprite(0, 0, resources.checkBoxRegion, vbom)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{

				case MotionEvent.ACTION_DOWN:
					break;

				case MotionEvent.ACTION_OUTSIDE:
					break;

				case MotionEvent.ACTION_UP:
					onOptionsButtonTouched(OPTIONS_BUTTON.SFX);
					break;
				}

				return true;
			}

		};

		textAudioMusic = new Text(0, 0, resources.mainMenuFont, "Music", vbom)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{

				case MotionEvent.ACTION_DOWN:
					break;

				case MotionEvent.ACTION_OUTSIDE:
					break;

				case MotionEvent.ACTION_UP:
					onOptionsButtonTouched(OPTIONS_BUTTON.MUSIC);
					break;
				}

				return true;
			}

		};
		textAudioMusic.setColor(GameOptions.musicEnabled() ? Color.WHITE : Utils.OtherColors.DARK_GREY);

		textAudioSfx = new Text(0, 0, resources.mainMenuFont, "SFX", vbom)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{

				case MotionEvent.ACTION_DOWN:
					break;

				case MotionEvent.ACTION_OUTSIDE:
					break;

				case MotionEvent.ACTION_UP:
					onOptionsButtonTouched(OPTIONS_BUTTON.SFX);
					break;
				}

				return true;
			}

		};
		textAudioSfx.setColor(GameOptions.sfxEnabled() ? Color.WHITE : Utils.OtherColors.DARK_GREY);

		switchAudioMusic.setCurrentTileIndex(GameOptions.musicEnabled() ? 0 : 1);
		switchAudioSfx.setCurrentTileIndex(GameOptions.sfxEnabled() ? 0 : 1);

		menuOptionsAudioCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), 0.8f * camera.getWidth(), 0.65f * camera.getHeight());


		menuOptionsAudioCanvas.addText(textAudioMusic, 0.25f , index * heightFactor, 0.45f, 0.17f);
		menuOptionsAudioCanvas.addGraphicWrap(switchAudioMusic, 0.75f , index * heightFactor, 0.25f, 0.17f);
		index--;

		menuOptionsAudioCanvas.addText(textAudioSfx, 0.25f , index * heightFactor, 0.45f, 0.17f);
		menuOptionsAudioCanvas.addGraphicWrap(switchAudioSfx, 0.75f , index * heightFactor, 0.25f, 0.17f);
		index--;

		menuOptionsAudioCanvas.addGraphic(frame, 0.5f, 0.5f, 1f, 1f);

		menuOptions.attachChild(menuOptionsAudioCanvas);
	}

	private void createOptionsTabs()
	{
		int numberOfMenus = 3;
		float factor = 1f / numberOfMenus;

		menuOptionsMenuTab = new RiskaSprite(resources.tabRegion, vbom, "Menu", resources.mainMenuFont)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{
				case MotionEvent.ACTION_DOWN:
					break;

				case MotionEvent.ACTION_OUTSIDE:
					break;

				case MotionEvent.ACTION_UP:
					onOptionsTabSelected(OPTIONS_TAB.MENU);
					break;
				}

				return true;
			}
		};

		menuOptionsGameTab = new RiskaSprite(resources.tabRegion, vbom, "Game", resources.mainMenuFont)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{
				case MotionEvent.ACTION_DOWN:
					break;

				case MotionEvent.ACTION_OUTSIDE:
					break;

				case MotionEvent.ACTION_UP:
					onOptionsTabSelected(OPTIONS_TAB.GAME);
					break;
				}

				return true;
			}
		};

		menuOptionsAudioTab = new RiskaSprite(resources.tabRegion, vbom, "Audio", resources.mainMenuFont)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{
				case MotionEvent.ACTION_DOWN:
					break;

				case MotionEvent.ACTION_OUTSIDE:
					break;

				case MotionEvent.ACTION_UP:
					onOptionsTabSelected(OPTIONS_TAB.AUDIO);
					break;
				}

				return true;
			}
		};

		menuOptionsMenuTab.setSize(factor * menuOptionsMenuCanvas.getWidth(), 0.1f * camera.getHeight());
		menuOptionsMenuTab.setPosition(Utils.left(menuOptionsMenuCanvas) + 0.5f * factor * menuOptionsMenuCanvas.getWidth(),
				Utils.bottom(menuOptionsMenuCanvas) - 1f * Utils.halfY(menuOptionsMenuTab));

		menuOptionsGameTab.setSize(menuOptionsMenuTab.getWidth(), menuOptionsMenuTab.getHeight());
		menuOptionsGameTab.setPosition(Utils.right(menuOptionsMenuTab) + Utils.halfX(menuOptionsGameTab), menuOptionsMenuTab.getY());

		menuOptionsAudioTab.setSize(menuOptionsMenuTab.getWidth(), menuOptionsMenuTab.getHeight());
		menuOptionsAudioTab.setPosition(Utils.right(menuOptionsGameTab) + Utils.halfX(menuOptionsAudioTab), menuOptionsMenuTab.getY());

		menuOptions.attachChild(menuOptionsMenuTab);
		menuOptions.attachChild(menuOptionsGameTab);
		menuOptions.attachChild(menuOptionsAudioTab);

		menuOptions.registerTouchArea(menuOptionsMenuTab);
		menuOptions.registerTouchArea(menuOptionsGameTab);
		menuOptions.registerTouchArea(menuOptionsAudioTab);

		// Animations tab selected
		menuOptionsMenuTab.setColor(Utils.OtherColors.DARK_GREY);
		menuOptionsGameTab.setColor(Utils.OtherColors.DARK_GREY);
		menuOptionsAudioTab.setColor(Utils.OtherColors.DARK_GREY);
	}

	private void onOptionsTabSelected(OPTIONS_TAB newTab)
	{
		if(!currentOptionsTab.equals(newTab))
		{
			switch(currentOptionsTab)
			{
			case MENU:
				menuOptionsMenuTab.setColor(Utils.OtherColors.DARK_GREY);
				menuOptionsMenuCanvas.fadeOut(animationTime);
				menuOptions.unregisterTouchArea(switchMenuAnimations);
				menuOptions.unregisterTouchArea(textMenuAnimations);
				break;

			case GAME:
				menuOptionsGameTab.setColor(Utils.OtherColors.DARK_GREY);
				menuOptionsGameCanvas.fadeOut(animationTime);
				menuOptions.unregisterTouchArea(switchGameAnimations);
				break;

			case AUDIO:
				menuOptionsAudioTab.setColor(Utils.OtherColors.DARK_GREY);
				menuOptionsAudioCanvas.fadeOut(animationTime);
				menuOptions.unregisterTouchArea(switchAudioMusic);
				menuOptions.unregisterTouchArea(switchAudioSfx);
				menuOptions.unregisterTouchArea(textAudioMusic);
				menuOptions.unregisterTouchArea(textAudioSfx);
				break;

			default:
				break;
			}

			currentOptionsTab = newTab;

			switch(newTab)
			{

			case MENU:
				menuOptionsMenuTab.setColor(Color.WHITE);
				menuOptionsMenuCanvas.fadeIn(animationTime);
				menuOptions.registerTouchArea(switchMenuAnimations);
				menuOptions.registerTouchArea(textMenuAnimations);
				break;

			case GAME:
				menuOptionsGameTab.setColor(Color.WHITE);
				menuOptionsGameCanvas.fadeIn(animationTime);
				menuOptions.registerTouchArea(switchGameAnimations);
				break;

			case AUDIO:
				menuOptionsAudioTab.setColor(Color.WHITE);
				menuOptionsAudioCanvas.fadeIn(animationTime);
				menuOptions.registerTouchArea(switchAudioMusic);
				menuOptions.registerTouchArea(switchAudioSfx);
				menuOptions.registerTouchArea(textAudioMusic);
				menuOptions.registerTouchArea(textAudioSfx);
				break;

			default:
				break;
			}
		}
	}

	private void onOptionsButtonTouched(OPTIONS_BUTTON x)
	{
		switch(x)
		{

		case SFX:
			// Enable or disable SFX
			if(GameOptions.sfxEnabled())
			{
				GameOptions.setSfxEnabled(false);
				switchAudioSfx.setCurrentTileIndex(1);
				textAudioSfx.setColor(Utils.OtherColors.DARK_GREY);
			}
			else
			{
				GameOptions.setSfxEnabled(true);
				switchAudioSfx.setCurrentTileIndex(0);
				textAudioSfx.setColor(Color.WHITE);
			}
			break;

		case MUSIC:
			if(GameOptions.musicEnabled())
			{
				GameOptions.setMusicEnabled(false);
				switchAudioMusic.setCurrentTileIndex(1);
				textAudioMusic.setColor(Utils.OtherColors.DARK_GREY);
			}
			else
			{
				GameOptions.setMusicEnabled(true);
				switchAudioMusic.setCurrentTileIndex(0);
				textAudioMusic.setColor(Color.WHITE);
			}
			break;

		case MENU_ANIMATIONS:
			if(GameOptions.menuAnimationsEnabled())
			{
				GameOptions.setMenuAnimationsEnabled(false);
				switchMenuAnimations.setCurrentTileIndex(1);
				textMenuAnimations.setColor(Utils.OtherColors.DARK_GREY);
			}
			else
			{
				GameOptions.setMenuAnimationsEnabled(true);
				switchMenuAnimations.setCurrentTileIndex(0);
				textMenuAnimations.setColor(Color.WHITE);
			}
			animationTime = GameOptions.animationTime;
			break;

		default:
			break;
		}
	}

	// ==================================================
	// NEW GAME
	// ==================================================
	private void createNewGameMenu()
	{
		menuNewGame = new MenuScene(camera);
		menuNewGame.setBackgroundEnabled(false);

		playerIsCpu = new boolean[GameOptions.maxPlayers];
		playerActive = new boolean[GameOptions.maxPlayers];
		playerActivable = new boolean[GameOptions.maxPlayers];
		playerEditable = new boolean[GameOptions.maxPlayers];

		playerColor = new int[GameOptions.maxPlayers];

		createMapCanvas();
		createPlayersCanvas();
		createLevelCanvas();
		createGoCanvas();

		createNewGameTabs();

		onNewGameTabSelected(NEWGAME_TAB.MAP);

		menuNewGameMapCanvas.setAlpha(0f);
		menuNewGamePlayersCanvas.setAlpha(0f);
		menuNewGameLevelCanvas.setAlpha(0f);
		menuNewGameGoCanvas.setAlpha(0f);

		menuNewGameMapTab.setAlpha(0f);
		menuNewGamePlayersTab.setAlpha(0f);
		menuNewGameLevelTab.setAlpha(0f);
		menuNewGameGoTab.setAlpha(0f);			

		menuNewGame.setOnMenuItemClickListener(this);
		menuNewGame.setTouchAreaBindingOnActionDownEnabled(true);
		menuNewGame.setTouchAreaBindingOnActionMoveEnabled(true);
	}

	private void createMapCanvas()
	{
		Sprite frame;

		int numberOfMaps = GameOptions.numberOfMaps;

		int maxCols = 4;
		//int maxRows = 2;
		int maxRows = numberOfMaps / maxCols + (numberOfMaps % maxCols > 0 ? 1 : 0 );

		mapCanvas = new RiskaCanvas[numberOfMaps];

		float heightFactor = 1f / (maxRows + 1);
		float widthFactor = 1f / (maxCols + 1);

		frame = new Sprite(0, 0, resources.frameRegion, vbom);

		menuNewGameMapCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), 0.8f * camera.getWidth(), 0.65f * camera.getHeight());

		for(int i = 0; i < numberOfMaps; i++)
		{			
			int hIndex = i % maxCols;
			int vIndex = i / maxCols;

			float x = (hIndex * widthFactor) + widthFactor;
			float y = (vIndex * heightFactor) + heightFactor;

			mapCanvas[i] = new RiskaCanvas(0f, 0f,
					0.9f * widthFactor * menuNewGameMapCanvas.getWidth(),
					0.9f * heightFactor * menuNewGameMapCanvas.getHeight())
			{

				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
				{
					switch(ev.getMotionEvent().getActionMasked()) 
					{

					case MotionEvent.ACTION_DOWN:
						break;

					case MotionEvent.ACTION_OUTSIDE:
						break;

					case MotionEvent.ACTION_UP:
						onMapSelected(getTag());
						break;

					default:
						break;
					}

					return true;
				}

			};
			mapCanvas[i].setTag(i);

			TiledSprite map = new TiledSprite(0, 0, resources.mapsRegion, vbom);
			map.setCurrentTileIndex(i);
			mapCanvas[i].addGraphicWrap(map, 0.5f, 0.725f, 0.9f, 0.55f);

			Text text = new Text(0f, 0f, resources.mainMenuFont, "Map " + i, 20, vbom);
			mapCanvas[i].addText(text, 0.5f, 0.25f, 0.9f, 0.35f);

			Sprite mapFrame = new Sprite(0, 0, resources.smallFrameRegion, vbom);
			mapCanvas[i].addGraphic(mapFrame, 0.5f, 0.5f, 1f, 1f);

			mapCanvas[i].setColor(i == 0 ? Color.WHITE : Utils.OtherColors.DARK_GREY);

			menuNewGameMapCanvas.addGraphic(mapCanvas[i], x, y, 0.9f * widthFactor, 0.9f * heightFactor);
		}

		menuNewGameMapCanvas.addGraphic(frame, 0.5f, 0.5f, 1f, 1f);

		menuNewGame.attachChild(menuNewGameMapCanvas);
	}

	private void createPlayersCanvas()
	{
		Sprite frame;
		frame = new Sprite(0, 0, resources.frameRegion, vbom);

		menuNewGamePlayersCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), 0.8f * camera.getWidth(), 0.65f * camera.getHeight());

		int maxPlayers = GameOptions.maxPlayers;

		playerCanvas = new RiskaCanvas[maxPlayers];
		playerName = new RiskaSprite[maxPlayers];
		playerActiveButton = new ButtonSprite[maxPlayers];
		playerCpuButton = new ButtonSprite[maxPlayers];
		playerColorButton = new RiskaCanvas[maxPlayers];

		RiskaSprite name, isActive, isCpu, color;

		resetGameInfo();

		float heightFactor = 1f / (maxPlayers + 2);

		float titleY = (maxPlayers + 1) * heightFactor;

		RiskaCanvas titleCanvas = new RiskaCanvas(0f, 0f,
				1f * menuNewGamePlayersCanvas.getWidth(),
				1f * heightFactor * menuNewGamePlayersCanvas.getHeight());

		name = new RiskaSprite(resources.emptyButtonRegion, vbom, "Name", resources.mainMenuFont);
		isActive = new RiskaSprite(resources.emptyButtonRegion, vbom, "Active?", resources.mainMenuFont);
		isCpu = new RiskaSprite(resources.emptyButtonRegion, vbom, "Cpu?", resources.mainMenuFont);
		color = new RiskaSprite(resources.emptyButtonRegion, vbom, "Colors", resources.mainMenuFont);

		titleCanvas.addGraphic(name, 0.2f, 0.5f, 0.4f, 1f);
		titleCanvas.addGraphic(isActive, 0.9f, 0.5f, 0.2f, 1f);
		titleCanvas.addGraphic(isCpu, 0.75f, 0.5f, 0.2f, 1f);
		titleCanvas.addGraphic(color, 0.5f, 0.5f, 0.2f, 1f);

		int index = maxPlayers;

		for(int i = 0; i < maxPlayers; i++)
		{
			playerCanvas[i] = new RiskaCanvas(0f, 0f, titleCanvas.getWidth(), titleCanvas.getHeight());

			playerName[i] = new RiskaSprite(resources.emptyButtonRegion, vbom, "", resources.mainMenuFont)
			{
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
				{
					switch(ev.getMotionEvent().getActionMasked()) 
					{

					case MotionEvent.ACTION_DOWN:
						onNamePressed(getTag());
						break;

					case MotionEvent.ACTION_OUTSIDE:
						onNameReleased(getTag());
						break;

					case MotionEvent.ACTION_UP:
						onNameTouched(getTag());
						break;

					default:
						break;
					}

					return true;
				}
			};
			playerName[i].setTag(i);
			playerName[i].setText("Player " + (i + 1));

			playerCanvas[i].addGraphic(playerName[i], 0.2f, 0.5f, 0.4f, 1f);

			playerActiveButton[i] = new ButtonSprite(0, 0, resources.checkBoxRegion, vbom)
			{
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
				{
					switch(ev.getMotionEvent().getActionMasked()) 
					{

					case MotionEvent.ACTION_DOWN:
						break;

					case MotionEvent.ACTION_OUTSIDE:
						break;

					case MotionEvent.ACTION_UP:
						setPlayerActive(getTag(), !playerActive[getTag()]);
						break;
					}

					return true;
				}
			};
			playerActiveButton[i].setTag(i);
			playerActiveButton[i].setCurrentTileIndex(playerActive[i] ? 0 : 1);
			playerActiveButton[i].setEnabled(playerActivable[i]);
			//playerActiveButton[i].setVisible(playerActivable[i]); // NEW

			playerCanvas[i].addGraphicWrap(playerActiveButton[i], 0.9f, 0.5f, 0.1f, 1f);

			playerCpuButton[i] = new ButtonSprite(0, 0, resources.checkBoxRegion, vbom)
			{
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
				{
					switch(ev.getMotionEvent().getActionMasked()) 
					{

					case MotionEvent.ACTION_DOWN:
						break;

					case MotionEvent.ACTION_OUTSIDE:
						break;

					case MotionEvent.ACTION_UP:
						setPlayerAsCpu(getTag(), !playerIsCpu[getTag()]);
						break;
					}

					return true;
				}

			};
			playerCpuButton[i].setTag(i);
			playerCpuButton[i].setEnabled(playerEditable[i]);
			//playerCpuButton[i].setVisible(playerEditable[i]); // NEW

			playerCanvas[i].addGraphicWrap(playerCpuButton[i], 0.75f, 0.5f, 0.1f, 1f);

			final ButtonSprite priColor = new ButtonSprite(0, 0, resources.factionColorRegion, vbom);
			priColor.setCurrentTileIndex(0);
			final ButtonSprite secColor = new ButtonSprite(0, 0, resources.factionColorRegion, vbom);
			secColor.setCurrentTileIndex(1);
			playerColorButton[i] = new RiskaCanvas(0f, 0f, 1f, 1f)
			{
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
				{
					switch(ev.getMotionEvent().getActionMasked()) 
					{

					case MotionEvent.ACTION_DOWN:
						break;

					case MotionEvent.ACTION_OUTSIDE:
						break;

					case MotionEvent.ACTION_UP:
						selectNextColor(getTag());
						break;

					default:
						break;
					}

					return true;
				}
			};
			playerColorButton[i].setTag(i);
			playerColorButton[i].addGraphicWrap(secColor, 0.5f, 0.5f, 0.95f, 0.95f);
			playerColorButton[i].addGraphicWrap(priColor, 0.5f, 0.5f, 0.95f, 0.95f);

			playerCanvas[i].addGraphicWrap(playerColorButton[i], 0.5f, 0.5f, 0.1f, 1f);

			menuNewGamePlayersCanvas.addGraphic(playerCanvas[i], 0.5f, index * heightFactor, 0.9f, heightFactor);
			index--;

			updateFactionColors(i);
			setPlayerActive(i, playerActive[i]);
			setPlayerAsCpu(i, playerIsCpu[i]);
		}
		menuNewGamePlayersCanvas.addGraphic(titleCanvas, 0.5f, titleY, 0.9f, heightFactor);
		menuNewGamePlayersCanvas.addGraphic(frame, 0.5f, 0.5f, 1f, 1f);

		menuNewGame.attachChild(menuNewGamePlayersCanvas);
	}

	private void createLevelCanvas()
	{
		Sprite frame;
		frame = new Sprite(0, 0, resources.frameRegion, vbom);

		int levels = GameOptions.numberOfLevels;

		float factor = 1f / (levels + 1);
		int index = levels;

		menuNewGameLevelCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), 0.8f * camera.getWidth(), 0.65f * camera.getHeight());

		levelCheckBox = new ButtonSprite[GameOptions.numberOfLevels];
		levelText = new Text[GameOptions.numberOfLevels];

		for(int i = 0; i < GameOptions.numberOfLevels; i++)
		{
			levelCheckBox[i] = new ButtonSprite(0f, 0f, resources.checkBoxRegion, vbom)
			{	
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
				{				
					switch(ev.getMotionEvent().getActionMasked()) 
					{

					case MotionEvent.ACTION_DOWN:
						break;

					case MotionEvent.ACTION_OUTSIDE:
						break;

					case MotionEvent.ACTION_UP:
						selectLevel(getTag());
						break;

					default:
						break;
					}

					return true;
				}

			};
			levelCheckBox[i].setTag(i);

			levelText[i] = new Text(0f, 0f, resources.mainMenuFont, GameOptions.getLevelDescr(i), 100, vbom)
			{	
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
				{				
					switch(ev.getMotionEvent().getActionMasked()) 
					{

					case MotionEvent.ACTION_DOWN:
						break;

					case MotionEvent.ACTION_OUTSIDE:
						break;

					case MotionEvent.ACTION_UP:
						selectLevel(getTag());
						break;

					default:
						break;
					}

					return true;
				}

			};
			levelText[i].setTag(i);

			menuNewGameLevelCanvas.addText(levelText[i], 0.5f, index * factor, 0.5f, 0.8f * factor);

			//menuNewGameLevelCanvas.addGraphicWrap(levelCheckBox[i], 0.2f, index * factor, 0.2f, 0.8f * factor);

			index--;
		}

		selectLevel(0);

		// TODO

		menuNewGameLevelCanvas.addGraphic(frame, 0.5f, 0.5f, 1f, 1f);

		menuNewGame.attachChild(menuNewGameLevelCanvas);
	}

	private void createGoCanvas()
	{
		menuNewGameGoCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), 0.8f * camera.getWidth(), 0.65f * camera.getHeight());
	}

	private void createNewGameTabs()
	{
		int numberOfMenus = 4;
		float factor = 1f / numberOfMenus;

		menuNewGameMapTab = new RiskaSprite(resources.tabRegion, vbom, "Map", resources.mainMenuFont)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{
				case MotionEvent.ACTION_DOWN:
					break;

				case MotionEvent.ACTION_OUTSIDE:
					break;

				case MotionEvent.ACTION_UP:
					onNewGameTabSelected(NEWGAME_TAB.MAP);
					break;
				}

				return true;
			}
		};

		menuNewGamePlayersTab = new RiskaSprite(resources.tabRegion, vbom, "Players", resources.mainMenuFont)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{
				case MotionEvent.ACTION_DOWN:
					break;

				case MotionEvent.ACTION_OUTSIDE:
					break;

				case MotionEvent.ACTION_UP:
					onNewGameTabSelected(NEWGAME_TAB.PLAYERS);
					break;
				}

				return true;
			}
		};

		menuNewGameLevelTab = new RiskaSprite(resources.tabRegion, vbom, "Level", resources.mainMenuFont)
		{
			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{
				case MotionEvent.ACTION_DOWN:
					break;

				case MotionEvent.ACTION_OUTSIDE:
					break;

				case MotionEvent.ACTION_UP:
					onNewGameTabSelected(NEWGAME_TAB.LEVEL);
					break;
				}

				return true;
			}
		};

		menuNewGameGoTab = new RiskaMenuItem(NEW_GO, resources.tabRegion, vbom, "GO!", resources.mainMenuFont);

		menuNewGameMapTab.setSize(factor * menuNewGameMapCanvas.getWidth(), 0.1f * camera.getHeight());
		menuNewGameMapTab.setPosition(Utils.left(menuNewGameMapCanvas) + 0.5f * factor * menuNewGameMapCanvas.getWidth(),
				Utils.bottom(menuNewGameMapCanvas) - 1f * Utils.halfY(menuNewGameMapTab));

		menuNewGamePlayersTab.setSize(menuNewGameMapTab.getWidth(), menuNewGameMapTab.getHeight());
		menuNewGamePlayersTab.setPosition(Utils.right(menuNewGameMapTab) + Utils.halfX(menuNewGamePlayersTab), menuNewGameMapTab.getY());

		menuNewGameLevelTab.setSize(menuNewGameMapTab.getWidth(), menuNewGameMapTab.getHeight());
		menuNewGameLevelTab.setPosition(Utils.right(menuNewGamePlayersTab) + Utils.halfX(menuNewGameLevelTab), menuNewGameMapTab.getY());

		menuNewGameGoTab.setSize(menuNewGameMapTab.getWidth(), menuNewGameMapTab.getHeight());
		menuNewGameGoTab.setPosition(Utils.right(menuNewGameLevelTab) + Utils.halfX(menuNewGameGoTab), menuNewGameMapTab.getY());

		menuNewGame.attachChild(menuNewGameMapTab);
		menuNewGame.attachChild(menuNewGamePlayersTab);
		menuNewGame.attachChild(menuNewGameLevelTab);

		menuNewGame.addMenuItem(menuNewGameGoTab);

		menuNewGame.registerTouchArea(menuNewGameMapTab);
		menuNewGame.registerTouchArea(menuNewGamePlayersTab);
		menuNewGame.registerTouchArea(menuNewGameLevelTab);

		// Map tab selected
		menuNewGameMapTab.setColor(Utils.OtherColors.WHITE);
		menuNewGamePlayersTab.setColor(Utils.OtherColors.DARK_GREY);
		menuNewGameLevelTab.setColor(Utils.OtherColors.DARK_GREY);
		menuNewGameGoTab.setColor(Utils.OtherColors.DARK_GREY);
	}

	private void onNewGameTabSelected(NEWGAME_TAB newTab)
	{

		if(!currentNewGameTab.equals(newTab))
		{
			switch(currentNewGameTab)
			{
			case MAP:
				menuNewGameMapTab.setColor(Utils.OtherColors.DARK_GREY);
				menuNewGameMapCanvas.fadeOut(animationTime);
				// Unregister all touch areas in that canvas
				for(int i = 0; i < mapCanvas.length; i++)
				{
					menuNewGame.unregisterTouchArea(mapCanvas[i]);
				}
				break;

			case PLAYERS:
				menuNewGamePlayersTab.setColor(Utils.OtherColors.DARK_GREY);
				menuNewGamePlayersCanvas.fadeOut(animationTime);
				// Unregister all touch areas in that canvas
				for(int i = 0; i < playerName.length; i++)
				{
					menuNewGame.unregisterTouchArea(playerColorButton[i]);
					menuNewGame.unregisterTouchArea(playerName[i]);
					menuNewGame.unregisterTouchArea(playerActiveButton[i]);
					menuNewGame.unregisterTouchArea(playerCpuButton[i]);
				}
				break;

			case LEVEL:
				menuNewGameLevelTab.setColor(Utils.OtherColors.DARK_GREY);
				menuNewGameLevelCanvas.fadeOut(animationTime);
				// Unregister all touch areas in that canvas
				for(int i = 0; i < levelCheckBox.length; i++)
				{
					menuNewGame.unregisterTouchArea(levelCheckBox[i]);
					menuNewGame.unregisterTouchArea(levelText[i]);
				}	
				break;

			case GO:
				menuNewGameGoTab.setColor(Utils.OtherColors.DARK_GREY);
				menuNewGameGoCanvas.fadeOut(animationTime);
				break;

			default:
				break;
			}

			currentNewGameTab = newTab;

			switch(newTab)
			{

			case MAP:
				menuNewGameMapTab.setColor(Color.WHITE);
				menuNewGameMapCanvas.fadeIn(animationTime);
				// Register all touch areas in that canvas
				for(int i = 0; i < mapCanvas.length; i++)
				{
					menuNewGame.registerTouchArea(mapCanvas[i]);
				}
				break;

			case PLAYERS:
				menuNewGamePlayersTab.setColor(Color.WHITE);
				menuNewGamePlayersCanvas.fadeIn(animationTime);
				// Register all touch areas in that canvas
				for(int i = 0; i < playerName.length; i++)
				{
					menuNewGame.registerTouchArea(playerColorButton[i]);
					menuNewGame.registerTouchArea(playerName[i]);
					menuNewGame.registerTouchArea(playerActiveButton[i]);
					menuNewGame.registerTouchArea(playerCpuButton[i]);
				}
				break;

			case LEVEL:
				menuNewGameLevelTab.setColor(Color.WHITE);
				menuNewGameLevelCanvas.fadeIn(animationTime);
				// Register all touch areas in that canvas
				for(int i = 0; i < levelCheckBox.length; i++)
				{
					menuNewGame.registerTouchArea(levelCheckBox[i]);
					menuNewGame.registerTouchArea(levelText[i]);
				}	
				break;

			case GO:
				menuNewGameGoTab.setColor(Color.WHITE);
				menuNewGameGoCanvas.fadeIn(animationTime);
				break;

			default:
				break;
			}
		}
	}

	private void onMapSelected(int index)
	{
		//Log.d("Riska", " > Map Selected");

		for(int i = 0; i < GameOptions.numberOfMaps; i++)
		{
			if(i != index)
			{
				mapCanvas[index].setColor(Color.WHITE);
			}
			else
			{
				mapCanvas[index].setColor(Utils.OtherColors.DARK_GREY);
				GameInfo.currentMapIndex = i;
			}
		}
	}

	private void onNameReleased(int index)
	{
		if(playerActive[index])
		{
			playerName[index].showText();
		}	
	}

	private void onNamePressed(int index)
	{
		if(playerActive[index])
		{
			playerName[index].hideText();
		}
	}

	private void onNameTouched(int index)
	{
		//Log.d("Riska", " > Name Selected");

		if(playerActive[index])
		{
			// TODO : edit name

			onNameReleased(index);
		}
	}

	private void selectNextColor(int index)
	{
		if(playerActive[index])
		{
			// TODO
			playerColor[index] += 1;
			playerColor[index] = playerColor[index] % GameOptions.numberOfColors;

			updateFactionColors(index);
		}	
	}

	private void updateFactionColors(int index)
	{
		ButtonSprite priColor = (ButtonSprite)playerColorButton[index].getChildByIndex(1);
		ButtonSprite secColor = (ButtonSprite)playerColorButton[index].getChildByIndex(0);

		priColor.setColor(GameOptions.getPriColor(playerColor[index]));
		secColor.setColor(GameOptions.getSecColor(playerColor[index]));
	}

	private void setPlayerActive(int index, boolean pActive)
	{
		if(playerActivable[index])
		{
			if(pActive)
			{
				playerActive[index] = true;
				playerActiveButton[index].setCurrentTileIndex(0);
				playerCanvas[index].setColor(Color.WHITE);
				playerColorButton[index].setColor(Color.WHITE);
				updateFactionColors(index);
				playerCpuButton[index].setColor(Color.WHITE);
				playerActiveButton[index].setColor(Color.WHITE);
			}
			else
			{
				playerActive[index] = false;
				playerActiveButton[index].setCurrentTileIndex(1);
				playerCanvas[index].setColor(Utils.OtherColors.DARK_GREY);
				playerColorButton[index].setColor(Utils.OtherColors.DARK_GREY);
				playerCpuButton[index].setColor(Utils.OtherColors.DARK_GREY);
				playerActiveButton[index].setColor(Color.WHITE);
			}
		}
	}

	private void setPlayerAsCpu(int index, boolean pCPU)
	{
		if(playerActive[index])
		{
			if(playerEditable[index])
			{
				if(pCPU)
				{
					playerIsCpu[index] = true;
					playerCpuButton[index].setCurrentTileIndex(0);
				}
				else
				{
					playerIsCpu[index] = false;
					playerCpuButton[index].setCurrentTileIndex(1);
				}
			}
		}
	}

	private void selectLevel(int index)
	{
		//Log.d("Riska", " > Level Selected : " + index);

		for(int i = 0; i < GameOptions.numberOfLevels; i++)
		{
			if(i == index)
			{
				level = i;
				levelCheckBox[i].setColor(Color.WHITE);
				levelText[i].setColor(Color.WHITE);
			}
			else
			{
				levelCheckBox[i].setColor(Utils.OtherColors.DARK_GREY);
				levelText[i].setColor(Utils.OtherColors.DARK_GREY);
			}
		}
	}

	// ==================================================
	// UPDATE DATA
	// ==================================================
	private void setChildScene(CHILD x)
	{	
		MenuScene toSet = getChildScene(x);

		if(toSet != null)
		{
			if(toSet.hasParent())
			{
				toSet.detachSelf();
			}
			setChildScene(toSet);
		}
	}

	private MenuScene getChildScene(CHILD x)
	{
		switch(x)
		{

		case MAIN:
			return menuMain;

		case START:
			return menuStart;

		case OPTIONS:
			return menuOptions;

		case NEW:
			return menuNewGame;

		default:
			return null;
		}
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pX, float pY)
	{

		switch(pMenuItem.getID())
		{

		case MAIN_START:
			changeChildScene(CHILD.MAIN, CHILD.START);	
			break;

		case MAIN_OPTIONS:
			changeChildScene(CHILD.MAIN, CHILD.OPTIONS);
			break;

		case START_NEW:
			resetGameInfo();
			changeChildScene(CHILD.START, CHILD.NEW);
			break;

		case START_LOAD:
			// TODO
			break;

		case NEW_GO:
			onNewGameTabSelected(NEWGAME_TAB.GO);
			saveGameInfo();
			changeSceneToGame();
			break;

		default:
			break;
		}

		return true;
	}

	private void changeChildScene(final CHILD from, final CHILD to)
	{
		final MenuScene child = getChildScene(to);

		hideChild(from);

		DelayModifier waitForAnimation = new DelayModifier(1.2f * animationTime)
		{
			@Override
			protected void onModifierFinished(IEntity pItem)
			{
				setChildScene(child);
				showChild(to);
				currentChild = to;
			}
		};
		registerEntityModifier(waitForAnimation);

	}

	private void showChild(CHILD toShow)
	{
		switch(toShow)
		{

		case MAIN:
			menuMainStartButton.fadeIn(animationTime);
			menuMainOptionsButton.fadeIn(animationTime);
			break;

		case START:
			menuStartNewButton.fadeIn(animationTime);
			menuStartLoadButton.fadeIn(animationTime);
			break;

		case OPTIONS:
			menuOptionsMenuTab.fadeIn(animationTime);
			menuOptionsAudioTab.fadeIn(animationTime);
			menuOptionsGameTab.fadeIn(animationTime);

			switch(currentOptionsTab)
			{
			case MENU:
				menuOptionsMenuCanvas.fadeIn(animationTime);
				break;

			case GAME:
				menuOptionsGameCanvas.fadeIn(animationTime);
				break;

			case AUDIO:
				menuOptionsAudioCanvas.fadeIn(animationTime);
				break;
				
			default:
				break;
			}
			
			break;

		case NEW:
			menuNewGameMapTab.fadeIn(animationTime);
			menuNewGamePlayersTab.fadeIn(animationTime);
			menuNewGameLevelTab.fadeIn(animationTime);
			menuNewGameGoTab.fadeIn(animationTime);
			
			switch(currentNewGameTab)
			{
			case MAP:
				menuNewGameMapCanvas.fadeIn(animationTime);
				break;

			case PLAYERS:
				menuNewGamePlayersCanvas.fadeIn(animationTime);
				break;

			case LEVEL:
				menuNewGameLevelCanvas.fadeIn(animationTime);
				break;

			case GO:
				menuNewGameGoCanvas.fadeIn(animationTime);
				break;

			default:
				break;
			}
			
			break;

		default:
			break;
		}
	}

	private void hideChild(CHILD x)
	{
		switch(x)
		{

		case MAIN:
			menuMainStartButton.fadeOut(animationTime);
			menuMainOptionsButton.fadeOut(animationTime);
			break;

		case START:
			menuStartNewButton.fadeOut(animationTime);
			menuStartLoadButton.fadeOut(animationTime);
			break;

		case OPTIONS:
			menuOptionsMenuTab.fadeOut(animationTime);
			menuOptionsAudioTab.fadeOut(animationTime);
			menuOptionsGameTab.fadeOut(animationTime);

			switch(currentOptionsTab)
			{
			case MENU:
				menuOptionsMenuCanvas.fadeOut(animationTime);
				break;

			case GAME:
				menuOptionsGameCanvas.fadeOut(animationTime);
				break;

			case AUDIO:
				menuOptionsAudioCanvas.fadeOut(animationTime);
				break;
				
			default:
				break;
			}
			
			break;

		case NEW:
			menuNewGameMapTab.fadeOut(animationTime);
			menuNewGamePlayersTab.fadeOut(animationTime);
			menuNewGameLevelTab.fadeOut(animationTime);
			menuNewGameGoTab.fadeOut(animationTime);

			switch(currentNewGameTab)
			{
			case MAP:
				menuNewGameMapCanvas.fadeOut(animationTime);
				break;

			case PLAYERS:
				menuNewGamePlayersCanvas.fadeOut(animationTime);
				break;

			case LEVEL:
				menuNewGameLevelCanvas.fadeOut(animationTime);
				break;

			case GO:
				menuNewGameGoCanvas.fadeOut(animationTime);
				break;

			default:
				break;
			}
			
			break;

		default:
			break;
		}
	}

	private void changeSceneToGame()
	{
		if(GameOptions.menuAnimationsEnabled())
		{
			menuHUD.animateSlideDoors();

			DelayModifier waitForAnimation = new DelayModifier(MenuHUD.doorsAnimationWaitingTime)
			{
				@Override
				protected void onModifierFinished(IEntity pItem)
				{
					sceneManager.createGameScene();
					camera.setHUD(null);
				}
			};
			registerEntityModifier(waitForAnimation);
		}
		else
		{
			sceneManager.createGameScene();
			camera.setHUD(null);
		}
	}

	private void saveGameInfo()
	{
		int numOfPlayers = 0;
		int cpu = 0;
		int human = 0;

		for(int i = 0; i < GameOptions.maxPlayers; i++)
		{
			if(playerActive[i])
			{
				numOfPlayers++;
				if(playerIsCpu[i])
				{
					cpu++;
				}
				else
				{
					human++;
				}
			}
		}

		GameInfo.numberOfPlayers = numOfPlayers;
		GameInfo.humanPlayers = human;
		GameInfo.cpuPlayers = cpu;
		Utils.saveFromTo(playerIsCpu, GameInfo.playerIsCPU);

		GameInfo.clearFactions();

		for(int i = 0; i < GameInfo.numberOfPlayers; i++)
		{
			// TODO : assign to colors
			GameInfo.assignPlayerFaction(i);
		}

		GameInfo.setLevel(level);

		Log.d("Riska", "[MainMenuScene] Saving Game info...");
		Log.d("Riska", "[MainMenuScene] Number of players: " + GameInfo.numberOfPlayers);
		Log.d("Riska", "[MainMenuScene] HUMAN players:     " + GameInfo.humanPlayers);
		Log.d("Riska", "[MainMenuScene] CPU players:       " + GameInfo.cpuPlayers);
		Log.d("Riska", "[MainMenuScene] Level:             " + GameOptions.getLevelDescr(GameInfo.level) + " (" + GameInfo.level + ")");
	}

	private void resetGameInfo()
	{
		for(int i = 0; i < GameOptions.maxPlayers; i++)
		{
			playerIsCpu[i] = (i < GameOptions.minHumanPlayers) ? false : true;
			playerActive[i] = (i < GameOptions.minPlayers) ? true : false;
			playerActivable[i] = (i < GameOptions.minPlayers) ? false : true;
			playerEditable[i] = (i < GameOptions.minHumanPlayers) ? false : true;
		}
	}

}
