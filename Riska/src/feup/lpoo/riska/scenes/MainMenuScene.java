package feup.lpoo.riska.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

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
	private enum CHILD { MAIN, OPTIONS, START_GAME, NEW_GAME, CHOOSE_MAP, CHOOSE_PLAYERS, CHOOSE_FACTION, CHOOSE_DIFFICULTY, ANY};

	private enum PLAYERS_BUTTON { ADD_REMOVE, NAME, CPU_BOX, };

	private enum OPTIONS_TAB { ANIMATIONS, AUDIO, GRAPHICS};
	private enum OPTIONS_BUTTON { SFX, MUSIC, MENU_ANIMATIONS, GRAPHICS};

	// ==================================================
	// FIELDS
	// ==================================================
	private MenuScene menuMain;
	private MenuScene menuOptions;
	private MenuScene menuStart;
	private MenuScene menuNewGame;
	//private MenuScene menuLoad;

	//private MenuScene chooseMapMenu;			// TODO
	private MenuScene choosePlayersMenu;
	private MenuScene chooseFactionMenu;
	//private MenuScene chooseDifficultyMenu;	// TODO

	private MenuHUD menuHUD;

	private final int MAIN_START = 0;
	private final int MAIN_OPTIONS = 1;

	private final int START_NEW = 2;
	private final int START_LOAD = 3;

	private final int FACTION_NEXT = 8;
	private final int FACTION_PREVIOUS = 9;
	private final int FACTION_SELECT = 10;

	private final int PLAYERS_SELECT = 11;

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
	
	private RiskaSprite menuNewGamePlayersTab;
	private RiskaCanvas menuNewGamePlayersCanvas;
	
	private RiskaSprite menuNewGameLevelTab;
	private RiskaCanvas menuNewGameLevelCanvas;
	

	// CHOOSE PLAYERS MENU
	private RiskaMenuItem choosePlayerNextButton;
	private Text titleTextChoosePlayers;
	private Text textIsCPU;

	private ButtonSprite[] addRemovePlayerButton;
	private RiskaCanvas[] playerInfoCanvas;
	private RiskaSprite[] playerNameButton;
	private ButtonSprite[] cpuCheckBox;

	private boolean[] playerIsCPU;
	private boolean[] playerActive;
	private boolean[] playerEditable;

	// CHOOSE FACTION MENU
	private Text titleTextChooseFaction;
	private RiskaMenuItem chooseFactionNextButton;
	private RiskaMenuItem chooseFactionPreviousButton;
	private RiskaMenuItem chooseFactionSelectButton;

	private ButtonSprite factionPriColor;
	private ButtonSprite factionSecColor;
	private Sprite factionFrame;

	private int currentPlayer = 0;
	private int selectedFaction = 0;
	private int[] playerFaction;

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
		if(getChildScene().equals(menuMain))
		{
			IOManager.saveGameOptions();
			System.exit(0);
		}
		else
		{
			detachChildren();
			resetGameInfo();

			changeChildSceneFromTo(CHILD.ANY, CHILD.MAIN);
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

		//createChooseMapMenu();
		createChoosePlayersMenu();
		createChooseFactionMenu();
		//createChooseDifficultyMenu();

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
				resources.emptyButtonRegion, 
				vbom, "Start", resources.mainMenuFont,
				null, null, resources.barHRegion, resources.barHRegion);

		Utils.wrap(menuMainStartButton, 0.5f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		menuMainStartButton.setPosition(camera.getCenterX(), 0.53f * camera.getHeight());
		//startButton.debug();

		menuMainOptionsButton = new RiskaMenuItem(MAIN_OPTIONS,
				resources.emptyButtonRegion, 
				vbom, "Options", resources.mainMenuFont,
				null, null, resources.barHRegion, resources.barHRegion);

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
				resources.emptyButtonRegion,
				vbom, "New", resources.mainMenuFont,
				null, null, resources.barHRegion, resources.barHRegion);

		menuStartLoadButton = new RiskaMenuItem(START_LOAD,
				resources.emptyButtonRegion,
				vbom, "Load", resources.mainMenuFont,
				null, null, resources.barHRegion, resources.barHRegion);


		Utils.wrap(menuStartNewButton, 0.5f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		menuStartNewButton.setPosition(camera.getCenterX(), 0.53f * camera.getHeight());

		Utils.wrap(menuStartLoadButton, 0.5f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		menuStartLoadButton.setPosition(camera.getCenterX(), 0.23f * camera.getHeight());

		menuStart.addMenuItem(menuStartNewButton);
		menuStart.addMenuItem(menuStartLoadButton);

		menuStart.setOnMenuItemClickListener(this);
		menuStart.setTouchAreaBindingOnActionDownEnabled(true);
		menuStart.setTouchAreaBindingOnActionMoveEnabled(true);
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

		switchMenuAnimations = new ButtonSprite(0, 0, resources.switchRegion, vbom) {

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

		menuOptionsAnimationsCanvas.setVisible(false);

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

		switchX = new ButtonSprite(0, 0, resources.switchRegion, vbom) {

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
					onOptionsButtonTouched(this, OPTIONS_BUTTON.GRAPHICS);
					break;
				}

				return true;
			}

		};
		menuOptions.registerTouchArea(switchX);
		//TODO : switchX.setCurrentTileIndex(GameOptions.menuAnimationsEnabled() ? 0 : 1);

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

		switchSFX = new ButtonSprite(0, 0, resources.switchRegion, vbom) {

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

		switchMusic = new ButtonSprite(0, 0, resources.switchRegion, vbom) {

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
		int numberOfMenus = 2;
		float factor = 1f / (numberOfMenus + 1);

		menuOptionsAnimationsTab = new RiskaSprite(resources.tabRegion, vbom, "Animations", resources.mainMenuFont)
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
					onOptionsTabSelected(OPTIONS_TAB.ANIMATIONS);
					break;
				}

				return true;
			}
		};

		menuOptionsGraphicsTab = new RiskaSprite(resources.tabRegion, vbom, "Graphics", resources.mainMenuFont)
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
					onOptionsTabSelected(OPTIONS_TAB.GRAPHICS);
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

		menuOptionsAnimationsCanvas.setVisible(true);
	}

	private void onOptionsTabSelected(OPTIONS_TAB x)
	{
		switch(x)
		{

		case ANIMATIONS:
			menuOptionsAnimationsTab.setColor(Utils.OtherColors.WHITE);
			menuOptionsGraphicsTab.setColor(Utils.OtherColors.DARK_GREY);
			menuOptionsAudioTab.setColor(Utils.OtherColors.DARK_GREY);

			menuOptionsAnimationsCanvas.setVisible(true);
			menuOptionsGraphicsCanvas.setVisible(false);
			menuOptionsAudioCanvas.setVisible(false);
			break;

		case GRAPHICS:
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

		createMapCanvas();
		createPlayersCanvas();
		createColorsCanvas();	
		createLevelCanvas();
		
		createNewGameTabs();

		menuNewGame.setTouchAreaBindingOnActionDownEnabled(true);
		menuNewGame.setTouchAreaBindingOnActionMoveEnabled(true);
	}

	private void createMapCanvas()
	{
		Sprite frame;
		
		int numberOfMaps = GameOptions.numberOfMaps;
	
		int maxCols = 4;
		int maxRows = numberOfMaps / maxCols + (numberOfMaps % maxCols > 0 ? 1 : 0 );

		RiskaCanvas[] mapCanvas = new RiskaCanvas[maxCols * maxRows];
		
		float heightFactor = 1f / (maxRows + 1);
		float widthFactor = 1f / (maxRows + 1);

		int index = maxRows * maxCols;

		frame = new Sprite(0, 0, resources.frameRegion, vbom);

//		switchMenuAnimations = new ButtonSprite(0, 0, resources.switchRegion, vbom) {
//
//			@Override
//			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
//			{
//				switch(ev.getMotionEvent().getActionMasked()) 
//				{
//
//				case MotionEvent.ACTION_DOWN:
//					break;
//
//				case MotionEvent.ACTION_OUTSIDE:
//					break;
//
//				case MotionEvent.ACTION_UP:
//					onOptionsButtonTouched(this, OPTIONS_BUTTON.MENU_ANIMATIONS);
//					break;
//				}
//
//				return true;
//			}
//
//		};
//		menuOptions.registerTouchArea(switchMenuAnimations);
//		switchMenuAnimations.setCurrentTileIndex(GameOptions.menuAnimationsEnabled() ? 0 : 1);
//
//		textMenuAnimations = new Text(0, 0, resources.mainMenuFont, "Menu", vbom);
//		textMenuAnimations.setColor(Color.WHITE);
//
//		menuOptionsAnimationsCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), 0.8f * camera.getWidth(), 0.65f * camera.getHeight());
//
//
//		menuOptionsAnimationsCanvas.addText(textMenuAnimations, 0.25f , index * heightFactor, 0.45f, 0.17f);
//		menuOptionsAnimationsCanvas.addGraphicWrap(switchMenuAnimations, 0.75f , index * heightFactor, 0.25f, 0.17f);
//		index--;

		menuOptionsAnimationsCanvas.addGraphic(frame, 0.5f, 0.5f, 1f, 1f);

		menuOptionsAnimationsCanvas.setVisible(false);

		menuOptions.attachChild(menuOptionsAnimationsCanvas);
	}

	private void createPlayersCanvas()
	{
		// TODO Auto-generated method stub
		
	}

	private void createColorsCanvas()
	{
		// TODO Auto-generated method stub
		
	}

	private void createLevelCanvas()
	{
		// TODO Auto-generated method stub
		
	}

	private void createNewGameTabs()
	{
		// TODO Auto-generated method stub
		
	}

	// ==================================================
	// CHOOSE PLAYERS
	// ==================================================
	private void createChoosePlayersMenu()
	{		
		choosePlayersMenu = new MenuScene(camera);
		choosePlayersMenu.setBackgroundEnabled(false);

		addRemovePlayerButton = new ButtonSprite[GameInfo.maxPlayers];

		playerIsCPU = new boolean[GameInfo.maxPlayers];
		playerActive = new boolean[GameInfo.maxPlayers];
		playerEditable = new boolean[GameInfo.maxPlayers];

		createPlayerSpots();

		titleTextChoosePlayers = new Text(0, 0, resources.mainMenuFont, "EDIT PLAYERS", vbom);
		Utils.wrap(titleTextChoosePlayers, 0.5f * camera.getWidth(), 0.11f * camera.getHeight(), 0.8f);
		titleTextChoosePlayers.setPosition( 0.5f * camera.getWidth(), 0.94f * camera.getHeight());
		titleTextChoosePlayers.setColor(Color.WHITE);

		choosePlayerNextButton = new RiskaMenuItem(PLAYERS_SELECT,
				resources.emptyButtonRegion,
				vbom, "Next", resources.mainMenuFont,
				resources.barVRegion, resources.barVRegion, null, null);

		choosePlayerNextButton.setSize(0.5f * camera.getWidth(), 0.12f * camera.getHeight());
		choosePlayerNextButton.setPosition(0.5f * camera.getWidth(), 0.06f * camera.getHeight());

		choosePlayersMenu.attachChild(titleTextChoosePlayers);

		choosePlayersMenu.addMenuItem(choosePlayerNextButton);

		choosePlayersMenu.setOnMenuItemClickListener(this);
		choosePlayersMenu.setTouchAreaBindingOnActionDownEnabled(true);
		choosePlayersMenu.setTouchAreaBindingOnActionMoveEnabled(true);
	}

	private void createPlayerSpots()
	{
		createPlayerButtons();

		resetPlayersVariables();

		placePlayerButtons();
	}

	private void createPlayerButtons()
	{
		playerNameButton = new RiskaSprite[GameInfo.maxPlayers];
		cpuCheckBox = new ButtonSprite[GameInfo.maxPlayers];
		playerInfoCanvas = new RiskaCanvas[GameInfo.maxPlayers];

		// Creates all players buttons
		for(int i = 0; i < GameInfo.maxPlayers; i++)
		{

			addRemovePlayerButton[i] = new ButtonSprite(0, 0, resources.addRemoveButtonRegion, vbom)
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
						onButtonTouched(this, getTag(), PLAYERS_BUTTON.ADD_REMOVE);
						break;

					default:
						break;
					}

					return true;
				}
			};
			addRemovePlayerButton[i].setTag(i);

			playerNameButton[i] = new RiskaSprite(resources.emptyButtonRegion, vbom, "", resources.mainMenuFont,
					null, null, resources.barHRegion, resources.barHRegion)
			{
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
				{
					switch(ev.getAction()) 
					{

					case MotionEvent.ACTION_DOWN:
						onRiskaAnimatedSpritePressed(this, getTag(), PLAYERS_BUTTON.NAME);
						break;

					case MotionEvent.ACTION_OUTSIDE:
						onRiskaAnimatedSpriteReleased(this, getTag(), PLAYERS_BUTTON.NAME);
						break;

					case MotionEvent.ACTION_UP:
						onRiskaAnimatedSpriteTouched(this, getTag(), PLAYERS_BUTTON.NAME);
						break;

					default:
						break;
					}

					return true;
				}
			};
			playerNameButton[i].setTag(i);

			cpuCheckBox[i] = new ButtonSprite(0, 0, resources.playerCheckBoxButtonRegion, vbom)
			{
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
				{
					switch(ev.getAction()) 
					{

					case MotionEvent.ACTION_DOWN:
						break;

					case MotionEvent.ACTION_OUTSIDE:
						break;

					case MotionEvent.ACTION_UP:
						onButtonTouched(this, getTag(), PLAYERS_BUTTON.CPU_BOX);
						break;

					default:
						break;
					}

					return true;
				}
			};
			cpuCheckBox[i].setTag(i);

			choosePlayersMenu.registerTouchArea(playerNameButton[i]);
			choosePlayersMenu.registerTouchArea(addRemovePlayerButton[i]);
			choosePlayersMenu.registerTouchArea(cpuCheckBox[i]);
		}

		textIsCPU = new Text(0, 0, resources.mainMenuFont, "cpu?", vbom);
	}

	private void placePlayerButtons()
	{
		float heightFactor = 1f / (GameInfo.maxPlayers + 1);	
		float buttonsHeight = heightFactor * 0.8f * camera.getHeight();
		float buttonsWidth = 0.15f * camera.getWidth();
		float canvasWidth = 0.75f * camera.getWidth();
		float canvasHeight = buttonsHeight;

		Utils.wrap(textIsCPU, 0.2f * camera.getWidth(), buttonsWidth, 0.5f);
		textIsCPU.setPosition(0.75f * camera.getWidth(), (1f - heightFactor) * 0.75f * camera.getHeight() + 0.15f * camera.getHeight());
		textIsCPU.setColor(Color.WHITE);
		choosePlayersMenu.attachChild(textIsCPU);

		// Places all players buttons
		for(int i = 0; i < GameInfo.maxPlayers; i++)
		{
			float canvasX = 0.5f * camera.getWidth();
			float canvasY = (1f - ((i + 1) * heightFactor)) * 0.75f * camera.getHeight() + 0.15f * camera.getHeight();

			playerInfoCanvas[i] = new RiskaCanvas(canvasX, canvasY, canvasWidth, canvasHeight);

			Utils.wrap(addRemovePlayerButton[i], buttonsWidth, buttonsHeight, 0.7f);
			addRemovePlayerButton[i].setPosition(0.25f * camera.getWidth(), canvasY);

			playerInfoCanvas[i].addGraphic(playerNameButton[i], 0.5f, 0.5f, 0.4f, 1f);
			playerInfoCanvas[i].addGraphicWrap(cpuCheckBox[i], 0.82f, 0.5f, 0.2f, 0.75f);

			playerInfoCanvas[i].setVisible(playerActive[i]);
			choosePlayersMenu.attachChild(playerInfoCanvas[i]);
			choosePlayersMenu.attachChild(addRemovePlayerButton[i]);
		}
	}

	private void onRiskaAnimatedSpriteReleased(RiskaSprite pSprite, int tag, PLAYERS_BUTTON x)
	{
		switch(x)
		{

		case NAME:
			if(GameOptions.menuAnimationsEnabled())
			{
				pSprite.animate();
			}
			pSprite.showText();
			break;

		default:
			break;
		}	
	}

	private void onRiskaAnimatedSpritePressed(RiskaSprite pSprite, int tag, PLAYERS_BUTTON x)
	{
		switch(x)
		{

		case NAME:
			pSprite.hideText();
			break;

		default:
			break;
		}	
	}

	private void onRiskaAnimatedSpriteTouched(RiskaSprite pSprite, int tag, PLAYERS_BUTTON x)
	{
		switch(x)
		{

		case NAME:
			// TODO : edit text
			onRiskaAnimatedSpriteReleased(pSprite, tag, x);
			break;

		default:
			break;
		}	
	}

	private void onButtonTouched(ButtonSprite buttonSprite, int tag, PLAYERS_BUTTON x)
	{
		switch(x)
		{

		case ADD_REMOVE:
			if(playerActive[tag])
			{
				buttonSprite.setCurrentTileIndex(0);
				playerInfoCanvas[tag].setVisible(false);
				playerActive[tag] = false;
			}
			else
			{
				buttonSprite.setCurrentTileIndex(1);
				playerInfoCanvas[tag].setVisible(true);
				playerActive[tag] = true;
			}
			break;

		case CPU_BOX:
			if(playerIsCPU[tag])
			{
				playerIsCPU[tag] = false;
				updatePlayerName(tag, "Player");
				cpuCheckBox[tag].setCurrentTileIndex(1);

			}
			else
			{
				playerIsCPU[tag] = true;
				updatePlayerName(tag, "CPU");
				cpuCheckBox[tag].setCurrentTileIndex(0);
			}
			break;

		default:
			break;
		}
	}

	private void updatePlayerName(int tag, String pName)
	{
		playerNameButton[tag].setText(pName);
	}

	private void resetPlayersVariables()
	{
		for(int i = 0; i < GameInfo.maxPlayers; i++)
		{
			playerIsCPU[i] = (i < GameInfo.minHumanPlayers) ? false : true;
			playerActive[i] = (i < GameInfo.minPlayers) ? true : false;
			playerEditable[i] = (i < GameInfo.minHumanPlayers) ? false : true;

			playerNameButton[i].setText(playerIsCPU[i] ? "CPU": "Player");

			addRemovePlayerButton[i].setVisible(i < GameInfo.minPlayers ? false : true);
			addRemovePlayerButton[i].setCurrentTileIndex(playerActive[i] ? 1 : 0);

			cpuCheckBox[i].setVisible(playerEditable[i] ? true : false);
		}
	}
	// ==================================================
	// CHOOSE FACTION
	// ==================================================	
	private void createChooseFactionMenu()
	{
		chooseFactionMenu = new MenuScene(camera);

		chooseFactionMenu.setBackgroundEnabled(false);

		playerFaction = new int[GameInfo.numberOfFactions];
		resetFactionsVariables();

		titleTextChooseFaction = new Text(0, 0, resources.mainMenuFont, "Choose Faction: (Player " + currentPlayer + ")", 100, vbom);

		Utils.wrap(titleTextChooseFaction, 0.5f * camera.getWidth(), 0.2f * camera.getHeight(), 0.9f);

		titleTextChooseFaction.setPosition( 0.5f * camera.getWidth(), 0.8f * camera.getHeight());
		titleTextChooseFaction.setColor(Color.WHITE);

		chooseFactionNextButton = new RiskaMenuItem(FACTION_NEXT,
				resources.emptyButtonRegion,
				vbom, ">", resources.mainMenuFont,
				resources.barVRegion, resources.barVRegion, null, null);

		chooseFactionPreviousButton = new RiskaMenuItem(FACTION_PREVIOUS,
				resources.emptyButtonRegion,
				vbom, "<", resources.mainMenuFont,
				resources.barVRegion, resources.barVRegion, null, null);

		chooseFactionSelectButton = new RiskaMenuItem(FACTION_SELECT,
				resources.emptyButtonRegion,
				vbom, "NEXT", resources.mainMenuFont,
				resources.barVRegion, resources.barVRegion, null, null);

		chooseFactionNextButton.setSize(0.2f * camera.getWidth(), 0.2f * camera.getHeight());
		chooseFactionNextButton.setPosition(0.75f * camera.getWidth(), 0.5f * camera.getHeight());

		chooseFactionPreviousButton.setSize(0.2f * camera.getWidth(), 0.2f * camera.getHeight());
		chooseFactionPreviousButton.setPosition(0.25f * camera.getWidth(), 0.5f * camera.getHeight());

		chooseFactionSelectButton.setSize(0.5f * camera.getWidth(), 0.12f * camera.getHeight());
		chooseFactionSelectButton.setPosition(0.5f * camera.getWidth(), 0.06f * camera.getHeight());


		factionPriColor = new ButtonSprite(0, 0, resources.factionSpriteRegion, vbom);
		factionSecColor = new ButtonSprite(0, 0, resources.factionSpriteRegion, vbom);

		factionPriColor.setCurrentTileIndex(0);
		factionPriColor.setSize(0.37f * camera.getHeight(), 0.37f * camera.getHeight());
		factionPriColor.setPosition(0.5f * camera.getWidth(), 0.5f * camera.getHeight());

		factionSecColor.setCurrentTileIndex(1);
		factionSecColor.setSize(factionPriColor.getWidth(), factionPriColor.getHeight());
		factionSecColor.setPosition(factionPriColor);

		factionFrame = new Sprite(0, 0, resources.smallFrameRegion, vbom);
		factionFrame.setSize(0.4f * camera.getHeight(), 0.4f * camera.getHeight());
		factionFrame.setPosition(factionPriColor);

		updateFactionVisual();

		chooseFactionMenu.addMenuItem(chooseFactionNextButton);
		chooseFactionMenu.addMenuItem(chooseFactionPreviousButton);
		chooseFactionMenu.addMenuItem(chooseFactionSelectButton);

		chooseFactionMenu.attachChild(factionSecColor);
		chooseFactionMenu.attachChild(factionPriColor);
		chooseFactionMenu.attachChild(factionFrame);

		chooseFactionMenu.attachChild(titleTextChooseFaction);

		chooseFactionMenu.setOnMenuItemClickListener(this);
		//chooseFactionMenu.setTouchAreaBindingOnActionDownEnabled(true);
		//chooseFactionMenu.setTouchAreaBindingOnActionMoveEnabled(true);
	}

	private void resetFactionsVariables()
	{
		Utils.fill(playerFaction, -1);
		GameInfo.clearFactions();
		selectedFaction = 0;
		currentPlayer = 0;
	}

	private void selectFaction(int direction)
	{
		int index = selectedFaction;

		if(direction > 0)
		{
			do
			{
				index += 1;
				index = index % playerFaction.length;

				if(index == selectedFaction)
				{
					//Log.e("Riska", "Circled through all factions. None available for chosing!");
					break;
				}

			}while(Utils.inArray(index, playerFaction));
		}

		if(direction < 0)
		{
			do
			{

				index -= 1;

				if(index - 1 < 0)
				{
					index = playerFaction.length - 1;
				}

				if(index == selectedFaction)
				{
					//Log.e("Riska", "Circled through all factions. None available for chosing!");
					break;
				}

			}while(Utils.inArray(index, playerFaction));
		}

		selectedFaction = index;
		updateFactionVisual();
	}

	private void updateFactionVisual()
	{
		factionPriColor.setColor(GameInfo.getPriColor(selectedFaction));
		factionSecColor.setColor(GameInfo.getSecColor(selectedFaction));
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

		case START_GAME:
			return menuStart;

		case OPTIONS:
			return menuOptions;

		case NEW_GAME:
			return menuNewGame;

		case CHOOSE_PLAYERS:
			return choosePlayersMenu;

		case CHOOSE_FACTION:
			return chooseFactionMenu;

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
			if(GameOptions.menuAnimationsEnabled())
			{
				menuMainStartButton.animate();
			}
			changeChildSceneFromTo(CHILD.MAIN, CHILD.START_GAME);	
			break;

		case MAIN_OPTIONS:
			if(GameOptions.menuAnimationsEnabled())
			{
				menuMainOptionsButton.animate();
			}
			changeChildSceneFromTo(CHILD.MAIN, CHILD.OPTIONS);
			break;

		case START_NEW:
			if(GameOptions.menuAnimationsEnabled())
			{
				menuStartNewButton.animate();
			}
			resetGameInfo(); // TODO : verify necessity of reseting
			changeChildSceneFromTo(CHILD.START_GAME, CHILD.NEW_GAME);
			break;

		case START_LOAD:
			//			if(GameOptions.menuAnimationsEnabled())
			//			{
			//				loadGameButton.animate();
			//			}
			//			sceneManager.loadGameScene(engine);
			break;

		case FACTION_NEXT:
			selectFaction(1);			
			break;

		case FACTION_PREVIOUS:
			selectFaction(-1);
			break;

		case FACTION_SELECT:
			if(GameOptions.menuAnimationsEnabled())
			{
				chooseFactionSelectButton.animate();
			}
			if(currentPlayer < GameInfo.humanPlayers)
			{
				playerFaction[currentPlayer] = selectedFaction;
				currentPlayer++;
				titleTextChooseFaction.setText("Choose Faction: (Player " + currentPlayer + ")");

				Utils.wrap(titleTextChooseFaction, 0.5f * camera.getWidth(), 0.11f * camera.getHeight(), 0.8f);
				selectFaction(1);
			}

			if(currentPlayer == GameInfo.humanPlayers)
			{
				saveFactionsInfo();
				changeSceneToGame();
			}
			break;

		case PLAYERS_SELECT:
			if(GameOptions.menuAnimationsEnabled())
			{
				choosePlayerNextButton.animate();
			}
			savePlayersInfo();
			changeChildSceneFromTo(CHILD.CHOOSE_PLAYERS, CHILD.CHOOSE_FACTION);
			break;

		default:
			break;
		}

		return false;
	}

	private void changeChildSceneFromTo(final CHILD from, final CHILD to)
	{
		final MenuScene child = getChildScene(to);

		if(GameOptions.menuAnimationsEnabled())
		{
			menuHUD.animateSlideDoors();

			DelayModifier waitForAnimation = new DelayModifier(MenuHUD.doorsAnimationWaitingTime)
			{
				@Override
				protected void onModifierFinished(IEntity pItem)
				{
					setChildScene(child);
				}
			};
			registerEntityModifier(waitForAnimation);
		}
		else
		{
			setChildScene(child);
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

	private void savePlayersInfo()
	{
		int numOfPlayers = 0;
		int cpu = 0;
		int human = 0;

		for(int i = 0; i < GameInfo.maxPlayers; i++)
		{
			if(playerActive[i])
			{
				numOfPlayers++;
				if(playerIsCPU[i])
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
		Utils.saveFromTo(playerIsCPU, GameInfo.playerIsCPU);

		//		Log.d("Riska", "[MainMenuScene] Saving players info...");
		//		Log.d("Riska", "[MainMenuScene] Number of players: " + GameInfo.numberOfPlayers);
		//		Log.d("Riska", "[MainMenuScene] HUMAN players:     " + GameInfo.humanPlayers);
		//		Log.d("Riska", "[MainMenuScene] CPU players:     " + GameInfo.cpuPlayers);
	}

	private void saveFactionsInfo()
	{
		for(int i = 0; i < GameInfo.humanPlayers; i++)
		{
			GameInfo.assignPlayerFaction(i, playerFaction[i]);
		}

		for(int i = GameInfo.humanPlayers; i < GameInfo.numberOfPlayers; i++)
		{
			GameInfo.assignPlayerFaction(i);
		}
	}

	private void resetGameInfo()
	{
		resetPlayersVariables();	
		resetFactionsVariables();
	}

}
