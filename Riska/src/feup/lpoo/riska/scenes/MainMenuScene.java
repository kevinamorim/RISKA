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

	private enum OPTIONS_TAB { MENU, AUDIO, GAME};
	private enum OPTIONS_BUTTON { SFX, MUSIC, MENU_ANIMATIONS, GAME_ANIMATIONS};

	private enum NEWGAME_TAB { MAP, PLAYERS, LEVEL, GO };
	
	private static float animationTime = 0.5f;
	
	private CHILD currentChild = CHILD.MAIN;

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
	private RiskaSprite menuOptionsAnimationsTab;
	private RiskaCanvas menuOptionsAnimationsCanvas;

	private RiskaSprite menuOptionsGraphicsTab;
	private RiskaCanvas menuOptionsGraphicsCanvas;

	private RiskaSprite menuOptionsAudioTab;
	private RiskaCanvas menuOptionsAudioCanvas;

	// --------------------------
	// NEW GAME MENU
	private RiskaSprite menuNewGameMapTab;
	private RiskaCanvas menuNewGameMapCanvas;
	private ButtonSprite[] mapCover;

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
				changeChildSceneFromTo(currentChild, CHILD.MAIN);
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
	public void onSceneShow() { }

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

		setChildScene(CHILD.MAIN);
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

		createAnimationsCanvas();
		createGraphicsCanvas();	
		createAudioCanvas();
		createOptionsTabs();
		
		menuOptionsAnimationsCanvas.setAlpha(0f);
		menuOptionsGraphicsCanvas.setAlpha(0f);
		menuOptionsAudioCanvas.setAlpha(0f);
		
		menuOptionsAnimationsTab.setAlpha(0f);
		menuOptionsGraphicsTab.setAlpha(0f);
		menuOptionsAudioTab.setAlpha(0f);

		menuOptions.setTouchAreaBindingOnActionDownEnabled(true);
		menuOptions.setTouchAreaBindingOnActionMoveEnabled(true);
	}

	private void createAnimationsCanvas()
	{
		Sprite frame;

		ButtonSprite switchMenuAnimations;
		Text textMenuAnimations;

		int maxNumberOfItems = 4;
		float heightFactor = 1f / (maxNumberOfItems + 1);

		int index = maxNumberOfItems;

		frame = new Sprite(0, 0, resources.frameRegion, vbom);

		switchMenuAnimations = new ButtonSprite(0, 0, resources.checkBoxRegion, vbom) {

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
					onOptionsButtonTouched(this, OPTIONS_BUTTON.MENU_ANIMATIONS);
					break;
				}

				return true;
			}

		};
		menuOptions.registerTouchArea(switchMenuAnimations);
		switchMenuAnimations.setCurrentTileIndex(GameOptions.menuAnimationsEnabled() ? 0 : 1);

		textMenuAnimations = new Text(0, 0, resources.mainMenuFont, "Menu", vbom);
		textMenuAnimations.setColor(Color.WHITE);

		menuOptionsAnimationsCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), 0.8f * camera.getWidth(), 0.65f * camera.getHeight());


		menuOptionsAnimationsCanvas.addText(textMenuAnimations, 0.25f , index * heightFactor, 0.45f, 0.17f);
		menuOptionsAnimationsCanvas.addGraphicWrap(switchMenuAnimations, 0.75f , index * heightFactor, 0.25f, 0.17f);
		index--;

		menuOptionsAnimationsCanvas.addGraphic(frame, 0.5f, 0.5f, 1f, 1f);

		menuOptionsAnimationsCanvas.setVisible(true);

		menuOptions.attachChild(menuOptionsAnimationsCanvas);
	}

	private void createGraphicsCanvas()
	{
		Sprite frame;

		ButtonSprite switchX;
		Text textX;

		int maxNumberOfItems = 4;
		float heightFactor = 1f / (maxNumberOfItems + 1);

		int index = maxNumberOfItems;

		frame = new Sprite(0, 0, resources.frameRegion, vbom);

		switchX = new ButtonSprite(0, 0, resources.checkBoxRegion, vbom) {

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
					onOptionsButtonTouched(this, OPTIONS_BUTTON.GAME_ANIMATIONS);
					break;
				}

				return true;
			}

		};
		menuOptions.registerTouchArea(switchX);
		switchX.setCurrentTileIndex(GameOptions.menuAnimationsEnabled() ? 0 : 1);

		textX = new Text(0, 0, resources.mainMenuFont, "graphical stuff", vbom);
		textX.setColor(Color.WHITE);

		menuOptionsGraphicsCanvas = new RiskaCanvas(
				camera.getCenterX(), camera.getCenterY(),
				0.8f * camera.getWidth(), 0.65f * camera.getHeight());


		menuOptionsGraphicsCanvas.addText(textX, 0.25f , index * heightFactor, 0.45f, 0.17f);
		menuOptionsGraphicsCanvas.addGraphicWrap(switchX, 0.75f , index * heightFactor, 0.25f, 0.17f);
		index--;

		menuOptionsGraphicsCanvas.addGraphic(frame, 0.5f, 0.5f, 1f, 1f);

		menuOptionsGraphicsCanvas.setVisible(false);

		menuOptions.attachChild(menuOptionsGraphicsCanvas);
	}

	private void createAudioCanvas()
	{
		Sprite frame;

		ButtonSprite switchSFX, switchMusic;
		Text textSFX, textMusic;

		int maxNumberOfItems = 4;
		float heightFactor = 1f / (maxNumberOfItems + 1);

		int index = maxNumberOfItems;

		frame = new Sprite(0, 0, resources.frameRegion, vbom);

		switchMusic = new ButtonSprite(0, 0, resources.checkBoxRegion, vbom) {

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
					onOptionsButtonTouched(this, OPTIONS_BUTTON.MUSIC);
					break;
				}

				return true;
			}

		};

		switchSFX = new ButtonSprite(0, 0, resources.checkBoxRegion, vbom) {

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
					onOptionsButtonTouched(this, OPTIONS_BUTTON.SFX);
					break;
				}

				return true;
			}

		};

		textMusic = new Text(0, 0, resources.mainMenuFont, "Music", vbom);
		textMusic.setColor(Color.WHITE);

		textSFX = new Text(0, 0, resources.mainMenuFont, "SFX", vbom);
		textSFX.setColor(Color.WHITE);

		switchMusic.setCurrentTileIndex(GameOptions.musicEnabled() ? 0 : 1);
		switchSFX.setCurrentTileIndex(GameOptions.sfxEnabled() ? 0 : 1);


		menuOptionsAudioCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), 0.8f * camera.getWidth(), 0.65f * camera.getHeight());


		menuOptionsAudioCanvas.addText(textMusic, 0.25f , index * heightFactor, 0.45f, 0.17f);
		menuOptionsAudioCanvas.addGraphicWrap(switchMusic, 0.75f , index * heightFactor, 0.25f, 0.17f);
		index--;

		menuOptionsAudioCanvas.addText(textSFX, 0.25f , index * heightFactor, 0.45f, 0.17f);
		menuOptionsAudioCanvas.addGraphicWrap(switchSFX, 0.75f , index * heightFactor, 0.25f, 0.17f);
		menuOptionsAudioCanvas.setVisible(false);
		index--;

		menuOptionsAudioCanvas.addGraphic(frame, 0.5f, 0.5f, 1f, 1f);

		menuOptions.registerTouchArea(switchSFX);
		menuOptions.registerTouchArea(switchMusic);

		menuOptions.attachChild(menuOptionsAudioCanvas);
	}

	private void createOptionsTabs()
	{
		int numberOfMenus = 3;
		float factor = 1f / numberOfMenus;

		menuOptionsAnimationsTab = new RiskaSprite(resources.tabRegion, vbom, "Menu", resources.mainMenuFont)
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

		menuOptionsGraphicsTab = new RiskaSprite(resources.tabRegion, vbom, "Game", resources.mainMenuFont)
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

		menuOptionsAnimationsTab.setSize(factor * menuOptionsAnimationsCanvas.getWidth(), 0.1f * camera.getHeight());
		menuOptionsAnimationsTab.setPosition(Utils.left(menuOptionsAnimationsCanvas) + 0.5f * factor * menuOptionsAnimationsCanvas.getWidth(),
				Utils.bottom(menuOptionsAnimationsCanvas) - 1f * Utils.halfY(menuOptionsAnimationsTab));

		menuOptionsGraphicsTab.setSize(menuOptionsAnimationsTab.getWidth(), menuOptionsAnimationsTab.getHeight());
		menuOptionsGraphicsTab.setPosition(Utils.right(menuOptionsAnimationsTab) + Utils.halfX(menuOptionsGraphicsTab), menuOptionsAnimationsTab.getY());

		menuOptionsAudioTab.setSize(menuOptionsAnimationsTab.getWidth(), menuOptionsAnimationsTab.getHeight());
		menuOptionsAudioTab.setPosition(Utils.right(menuOptionsGraphicsTab) + Utils.halfX(menuOptionsAudioTab), menuOptionsAnimationsTab.getY());

		menuOptions.attachChild(menuOptionsAnimationsTab);
		menuOptions.attachChild(menuOptionsGraphicsTab);
		menuOptions.attachChild(menuOptionsAudioTab);

		menuOptions.registerTouchArea(menuOptionsAnimationsTab);
		menuOptions.registerTouchArea(menuOptionsGraphicsTab);
		menuOptions.registerTouchArea(menuOptionsAudioTab);

		// Animations tab selected
		menuOptionsAnimationsTab.setColor(Utils.OtherColors.WHITE);
		menuOptionsGraphicsTab.setColor(Utils.OtherColors.DARK_GREY);
		menuOptionsAudioTab.setColor(Utils.OtherColors.DARK_GREY);
	}

	private void onOptionsTabSelected(OPTIONS_TAB x)
	{
		switch(x)
		{

		case MENU:
			menuOptionsAnimationsTab.setColor(Utils.OtherColors.WHITE);
			menuOptionsGraphicsTab.setColor(Utils.OtherColors.DARK_GREY);
			menuOptionsAudioTab.setColor(Utils.OtherColors.DARK_GREY);

			menuOptionsAnimationsCanvas.setVisible(true);
			menuOptionsGraphicsCanvas.setVisible(false);
			menuOptionsAudioCanvas.setVisible(false);
			break;

		case GAME:
			menuOptionsAnimationsTab.setColor(Utils.OtherColors.DARK_GREY);
			menuOptionsGraphicsTab.setColor(Utils.OtherColors.WHITE);
			menuOptionsAudioTab.setColor(Utils.OtherColors.DARK_GREY);

			menuOptionsAnimationsCanvas.setVisible(false);
			menuOptionsGraphicsCanvas.setVisible(true);
			menuOptionsAudioCanvas.setVisible(false);
			break;

		case AUDIO:
			menuOptionsAnimationsTab.setColor(Utils.OtherColors.DARK_GREY);
			menuOptionsGraphicsTab.setColor(Utils.OtherColors.DARK_GREY);
			menuOptionsAudioTab.setColor(Utils.OtherColors.WHITE);

			menuOptionsAnimationsCanvas.setVisible(false);
			menuOptionsGraphicsCanvas.setVisible(false);
			menuOptionsAudioCanvas.setVisible(true);
			break;

		default:
			break;
		}
	}

	private void onOptionsButtonTouched(ButtonSprite buttonSprite, OPTIONS_BUTTON x)
	{
		switch(x)
		{

		case SFX:
			// Enable or disable SFX
			if(GameOptions.sfxEnabled())
			{
				GameOptions.setSfxEnabled(false);
				buttonSprite.setCurrentTileIndex(1);
			}
			else
			{
				GameOptions.setSfxEnabled(true);
				buttonSprite.setCurrentTileIndex(0);
			}
			break;

		case MUSIC:
			if(GameOptions.musicEnabled())
			{
				GameOptions.setMusicEnabled(false);
				buttonSprite.setCurrentTileIndex(1);
			}
			else
			{
				GameOptions.setMusicEnabled(true);
				buttonSprite.setCurrentTileIndex(0);
			}
			break;

		case MENU_ANIMATIONS:
			if(GameOptions.menuAnimationsEnabled())
			{
				GameOptions.setMenuAnimationsEnabled(false);
				buttonSprite.setCurrentTileIndex(1);
			}
			else
			{
				GameOptions.setMenuAnimationsEnabled(true);
				buttonSprite.setCurrentTileIndex(0);
			}
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

		RiskaCanvas[] mapCanvas = new RiskaCanvas[numberOfMaps];
		mapCover = new ButtonSprite[numberOfMaps];

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
					0.9f * heightFactor * menuNewGameMapCanvas.getHeight());
			mapCanvas[i].setTag(i);

			TiledSprite map = new TiledSprite(0, 0, resources.mapsRegion, vbom);
			map.setCurrentTileIndex(i);
			mapCanvas[i].addGraphicWrap(map, 0.5f, 0.725f, 0.9f, 0.55f);

			Text text = new Text(0f, 0f, resources.mainMenuFont, "Map " + i, 20, vbom);
			mapCanvas[i].addText(text, 0.5f, 0.25f, 0.9f, 0.35f);

			Sprite mapFrame = new Sprite(0, 0, resources.smallFrameRegion, vbom);
			mapCanvas[i].addGraphic(mapFrame, 0.5f, 0.5f, 1f, 1f);

			mapCover[i] = new ButtonSprite(0, 0, resources.coveredSmallFrameRegion, vbom)
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
						selectMap(getTag());
						break;

					default:
						break;
					}

					return true;
				}
			};
			if(i > 0)
			{
				mapCover[i].setAlpha(0.8f);
			}
			else
			{
				mapCover[i].setAlpha(0f);
			}

			menuNewGameMapCanvas.addGraphic(mapCanvas[i], x, y, 0.9f * widthFactor, 0.9f * heightFactor);
			menuNewGameMapCanvas.addGraphic(mapCover[i], x, y, widthFactor, heightFactor);

			menuNewGame.registerTouchArea(mapCover[i]);
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

			playerName[i] = new RiskaSprite(resources.emptyButtonRegion, vbom, "", resources.mainMenuFont,
					null, null, null, resources.barHRegion)
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

		// Map tab selected
		menuNewGameMapTab.setColor(Utils.OtherColors.WHITE);
		menuNewGamePlayersTab.setColor(Utils.OtherColors.DARK_GREY);
		menuNewGameLevelTab.setColor(Utils.OtherColors.DARK_GREY);
		menuNewGameGoTab.setColor(Utils.OtherColors.DARK_GREY);
	}


	private void onNewGameTabSelected(NEWGAME_TAB x)
	{
		menuNewGameMapTab.setColor(Utils.OtherColors.DARK_GREY);
		menuNewGamePlayersTab.setColor(Utils.OtherColors.DARK_GREY);
		menuNewGameLevelTab.setColor(Utils.OtherColors.DARK_GREY);
		menuNewGameGoTab.setColor(Utils.OtherColors.DARK_GREY);

		menuNewGameMapCanvas.setVisible(false);
		menuNewGamePlayersCanvas.setVisible(false);
		menuNewGameLevelCanvas.setVisible(false);
		menuNewGameGoCanvas.setVisible(false);

		menuNewGame.clearTouchAreas();
		menuNewGame.registerTouchArea(menuNewGameMapTab);
		menuNewGame.registerTouchArea(menuNewGamePlayersTab);
		menuNewGame.registerTouchArea(menuNewGameLevelTab);
		menuNewGame.registerTouchArea(menuNewGameGoTab);

		switch(x)
		{

		case MAP:
			menuNewGameMapTab.setColor(Utils.OtherColors.WHITE);
			menuNewGameMapCanvas.setVisible(true);

			for(int i = 0; i < mapCover.length; i++)
			{
				menuNewGame.registerTouchArea(mapCover[i]);
			}
			break;

		case PLAYERS:
			menuNewGamePlayersTab.setColor(Utils.OtherColors.WHITE);
			menuNewGamePlayersCanvas.setVisible(true);

			for(int i = 0; i < playerName.length; i++)
			{
				menuNewGame.registerTouchArea(playerColorButton[i]);
				menuNewGame.registerTouchArea(playerName[i]);
				menuNewGame.registerTouchArea(playerActiveButton[i]);
				menuNewGame.registerTouchArea(playerCpuButton[i]);
			}
			break;

		case LEVEL:
			menuNewGameLevelTab.setColor(Utils.OtherColors.WHITE);
			menuNewGameLevelCanvas.setVisible(true);

			for(int i = 0; i < levelCheckBox.length; i++)
			{
				menuNewGame.registerTouchArea(levelCheckBox[i]);
				menuNewGame.registerTouchArea(levelText[i]);
			}	
			break;

		case GO:
			menuNewGameGoTab.setColor(Utils.OtherColors.WHITE);
			menuNewGameGoCanvas.setVisible(true);
			break;

		default:
			break;
		}
	}

	private void selectMap(int index)
	{
		//Log.d("Riska", " > Map Selected");

		for(int i = 0; i < GameOptions.numberOfMaps; i++)
		{
			if(i != index)
			{
				mapCover[index].setAlpha(0.8f);
			}
			else
			{
				mapCover[index].setAlpha(0f);
				GameInfo.currentMapIndex = i;
			}
		}
	}

	private void onNameReleased(int index)
	{
		if(playerActive[index])
		{
			if(GameOptions.menuAnimationsEnabled())
			{
				playerName[index].animate();
			}
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
			changeChildSceneFromTo(CHILD.MAIN, CHILD.START);	
			break;

		case MAIN_OPTIONS:
			changeChildSceneFromTo(CHILD.MAIN, CHILD.OPTIONS);
			break;

		case START_NEW:
			resetGameInfo();
			changeChildSceneFromTo(CHILD.START, CHILD.NEW);
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

	private void changeChildSceneFromTo(final CHILD from, final CHILD to)
	{
		final MenuScene child = getChildScene(to);

		if(GameOptions.menuAnimationsEnabled())
		{		
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
		else
		{
			setChildScene(child);
		}
	}

	private void showChild(CHILD x)
	{
		switch(x)
		{

		case MAIN:
			menuMainStartButton.open(animationTime);
			menuMainOptionsButton.open(animationTime);
			break;

		case START:
			menuStartNewButton.open(animationTime);
			menuStartLoadButton.open(animationTime);
			break;

		case OPTIONS:
			menuOptionsAnimationsTab.open(animationTime);
			menuOptionsAudioTab.open(animationTime);
			menuOptionsGraphicsTab.open(animationTime);
			
			if(menuOptionsAnimationsCanvas.isVisible())
			{
				menuOptionsAnimationsCanvas.open(animationTime);
			}
			else if(menuOptionsAudioCanvas.isVisible())
			{
				menuOptionsAudioCanvas.open(animationTime);
			}
			else if(menuOptionsGraphicsCanvas.isVisible())
			{
				menuOptionsGraphicsCanvas.open(animationTime);
			}
			break;

		case NEW:
			menuNewGameMapCanvas.open(animationTime);
			
			menuNewGameGoTab.open(animationTime);
			menuNewGameLevelTab.open(animationTime);
			menuNewGameMapTab.open(animationTime);
			menuNewGamePlayersTab.open(animationTime);
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
			menuMainStartButton.close(animationTime);
			menuMainOptionsButton.close(animationTime);
			break;

		case START:
			menuStartNewButton.close(animationTime);
			menuStartLoadButton.close(animationTime);
			break;

		case OPTIONS:
			menuOptionsAnimationsTab.close(animationTime);
			menuOptionsAudioTab.close(animationTime);
			menuOptionsGraphicsTab.close(animationTime);
		
			menuOptionsAnimationsCanvas.close(animationTime);
			menuOptionsAudioCanvas.close(animationTime);
			menuOptionsGraphicsCanvas.close(animationTime);
			break;

		case NEW:
			menuNewGameGoTab.close(animationTime);
			menuNewGameLevelTab.close(animationTime);
			menuNewGameMapTab.close(animationTime);
			menuNewGamePlayersTab.close(animationTime);
			
			menuNewGameMapCanvas.close(animationTime);
			menuNewGameGoCanvas.close(animationTime);
			menuNewGameLevelCanvas.close(animationTime);
			menuNewGamePlayersCanvas.close(animationTime);
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
