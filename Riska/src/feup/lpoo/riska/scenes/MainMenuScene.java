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

	// ==================================================
	// FIELDS
	// ==================================================
	private MenuScene mainMenu;
	private MenuScene optionsMenu;
	private MenuScene startGameMenu;

	//private MenuScene chooseMapMenu;			// TODO
	private MenuScene choosePlayersMenu;
	private MenuScene chooseFactionMenu;
	//private MenuScene chooseDifficultyMenu;	// TODO

	private MenuHUD menuHUD;

	private enum CHILD { MAIN, OPTIONS, START_GAME, CHOOSE_MAP, CHOOSE_PLAYERS, CHOOSE_FACTION, CHOOSE_DIFFICULTY, ANY};
	private enum PLAYERS_BUTTON { ADD_REMOVE, NAME, CPU_BOX, };
	private enum OPTIONS_TAB { ANIMATIONS, AUDIO, GRAPHICS};
	private enum OPTIONS_BUTTON { SFX, MUSIC, MENU_ANIMATIONS, GRAPHICS};

	private final int MAIN_START = 0;
	private final int MAIN_OPTIONS = 1;

	private final int START_NEW = 2;
	private final int START_LOAD = 3;

	private final int FACTION_NEXT = 8;
	private final int FACTION_PREVIOUS = 9;
	private final int FACTION_SELECT = 10;

	private final int PLAYERS_SELECT = 11;

	private SpriteBackground background;

	// MAIN MENU
	private RiskaMenuItem startButton;
	private RiskaMenuItem optionsButton;

	// START MENU
	private RiskaMenuItem newGameButton;
	private RiskaMenuItem loadGameButton;

	// OPTIONS MENU
	private RiskaSprite animationsTab;
	private RiskaSprite graphicsTab;
	private RiskaSprite audioTab;
	private RiskaCanvas optionsMenuAnimationsCanvas;
	private RiskaCanvas optionsMenuGraphicsCanvas;
	private RiskaCanvas optionsMenuAudioCanvas;


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

	// NEW
	DelayModifier waitForAnimation;

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
		if(getChildScene().equals(mainMenu))
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

		//createChooseMapMenu();
		createChoosePlayersMenu();
		createChooseFactionMenu();
		//createChooseDifficultyMenu();

		setChildScene(CHILD.MAIN);
	}

	@Override
	public void onSceneShow() { }

	@Override
	protected void onManagedUpdate(float pSecondsElapsed)
	{
		super.onManagedUpdate(pSecondsElapsed);
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
		mainMenu = new MenuScene(camera);

		mainMenu.setBackgroundEnabled(false);

		startButton = new RiskaMenuItem(MAIN_START,
				resources.emptyButtonRegion, 
				vbom, "Start", resources.mainMenuFont,
				null, null, resources.barHRegion, resources.barHRegion);

		Utils.wrap(startButton, 0.5f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		startButton.setPosition(camera.getCenterX(), 0.53f * camera.getHeight());
		//startButton.debug();

		optionsButton = new RiskaMenuItem(MAIN_OPTIONS,
				resources.emptyButtonRegion, 
				vbom, "Options", resources.mainMenuFont,
				null, null, resources.barHRegion, resources.barHRegion);

		Utils.wrap(optionsButton, 0.5f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		optionsButton.setPosition(camera.getCenterX(), 0.23f * camera.getHeight());

		mainMenu.addMenuItem(startButton);
		mainMenu.addMenuItem(optionsButton);
		mainMenu.setOnMenuItemClickListener(this);
		mainMenu.setTouchAreaBindingOnActionDownEnabled(true);
		mainMenu.setTouchAreaBindingOnActionMoveEnabled(true);
	}

	// ==================================================
	// START GAME
	// ==================================================
	private void createStartGameMenu()
	{
		startGameMenu = new MenuScene(camera);

		startGameMenu.setBackgroundEnabled(false);


		newGameButton = new RiskaMenuItem(START_NEW,
				resources.emptyButtonRegion,
				vbom, "New", resources.mainMenuFont,
				null, null, resources.barHRegion, resources.barHRegion);

		loadGameButton = new RiskaMenuItem(START_LOAD,
				resources.emptyButtonRegion,
				vbom, "Load", resources.mainMenuFont,
				null, null, resources.barHRegion, resources.barHRegion);


		Utils.wrap(newGameButton, 0.5f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		newGameButton.setPosition(camera.getCenterX(), 0.53f * camera.getHeight());

		Utils.wrap(loadGameButton, 0.5f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		loadGameButton.setPosition(camera.getCenterX(), 0.23f * camera.getHeight());

		startGameMenu.addMenuItem(newGameButton);
		startGameMenu.addMenuItem(loadGameButton);

		startGameMenu.setOnMenuItemClickListener(this);
		startGameMenu.setTouchAreaBindingOnActionDownEnabled(true);
		startGameMenu.setTouchAreaBindingOnActionMoveEnabled(true);
	}

	// ==================================================
	// OPTIONS
	// ==================================================
	private void createOptionsMenu()
	{
		optionsMenu = new MenuScene(camera);
		optionsMenu.setBackgroundEnabled(false);

		createOptionsAnimationsMenu();
		createOptionsGraphicsMenu();	
		createOptionsAudioMenu();
		createOptionsTabs();

		optionsMenu.setTouchAreaBindingOnActionDownEnabled(true);
		optionsMenu.setTouchAreaBindingOnActionMoveEnabled(true);
	}

	private void createOptionsAnimationsMenu()
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
					onButtonTouched(this, OPTIONS_BUTTON.MENU_ANIMATIONS);
					break;
				}

				return true;
			}

		};
		optionsMenu.registerTouchArea(switchMenuAnimations);
		switchMenuAnimations.setCurrentTileIndex(GameOptions.menuAnimationsEnabled() ? 0 : 1);

		textMenuAnimations = new Text(0, 0, resources.mainMenuFont, "Menu", vbom);
		textMenuAnimations.setColor(Color.WHITE);

		optionsMenuAnimationsCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), 0.8f * camera.getWidth(), 0.65f * camera.getHeight());


		optionsMenuAnimationsCanvas.addText(textMenuAnimations, 0.25f , index * heightFactor, 0.45f, 0.17f);
		optionsMenuAnimationsCanvas.addGraphicWrap(switchMenuAnimations, 0.75f , index * heightFactor, 0.25f, 0.17f);
		index--;

		optionsMenuAnimationsCanvas.addGraphic(frame, 0.5f, 0.5f, 1f, 1f);

		optionsMenuAnimationsCanvas.setVisible(false);

		optionsMenu.attachChild(optionsMenuAnimationsCanvas);
	}

	private void createOptionsGraphicsMenu()
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
					onButtonTouched(this, OPTIONS_BUTTON.GRAPHICS);
					break;
				}

				return true;
			}

		};
		optionsMenu.registerTouchArea(switchX);
		//TODO : switchX.setCurrentTileIndex(GameOptions.menuAnimationsEnabled() ? 0 : 1);

		textX = new Text(0, 0, resources.mainMenuFont, "graphical stuff", vbom);
		textX.setColor(Color.WHITE);

		optionsMenuGraphicsCanvas = new RiskaCanvas(
				camera.getCenterX(), camera.getCenterY(),
				0.8f * camera.getWidth(), 0.65f * camera.getHeight());


		optionsMenuGraphicsCanvas.addText(textX, 0.25f , index * heightFactor, 0.45f, 0.17f);
		optionsMenuGraphicsCanvas.addGraphicWrap(switchX, 0.75f , index * heightFactor, 0.25f, 0.17f);
		index--;

		optionsMenuGraphicsCanvas.addGraphic(frame, 0.5f, 0.5f, 1f, 1f);

		optionsMenuGraphicsCanvas.setVisible(false);

		optionsMenu.attachChild(optionsMenuGraphicsCanvas);
	}

	private void createOptionsAudioMenu()
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
					onButtonTouched(this, OPTIONS_BUTTON.SFX);
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
					onButtonTouched(this, OPTIONS_BUTTON.MUSIC);
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


		optionsMenuAudioCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), 0.8f * camera.getWidth(), 0.65f * camera.getHeight());


		optionsMenuAudioCanvas.addText(textMusic, 0.25f , index * heightFactor, 0.45f, 0.17f);
		optionsMenuAudioCanvas.addGraphicWrap(switchMusic, 0.75f , index * heightFactor, 0.25f, 0.17f);
		index--;

		optionsMenuAudioCanvas.addText(textSFX, 0.25f , index * heightFactor, 0.45f, 0.17f);
		optionsMenuAudioCanvas.addGraphicWrap(switchSFX, 0.75f , index * heightFactor, 0.25f, 0.17f);
		optionsMenuAudioCanvas.setVisible(false);
		index--;

		optionsMenuAudioCanvas.addGraphic(frame, 0.5f, 0.5f, 1f, 1f);

		optionsMenu.registerTouchArea(switchSFX);
		optionsMenu.registerTouchArea(switchMusic);

		optionsMenu.attachChild(optionsMenuAudioCanvas);
	}

	private void createOptionsTabs()
	{
		int numberOfMenus = 2;
		float factor = 1f / (numberOfMenus + 1);

		animationsTab = new RiskaSprite(resources.tabRegion, vbom, "Animations", resources.mainMenuFont)
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
					onTabTouched(this, OPTIONS_TAB.ANIMATIONS);
					break;
				}

				return true;
			}
		};

		graphicsTab = new RiskaSprite(resources.tabRegion, vbom, "Graphics", resources.mainMenuFont)
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
					onTabTouched(this, OPTIONS_TAB.GRAPHICS);
					break;
				}

				return true;
			}
		};

		audioTab = new RiskaSprite(resources.tabRegion, vbom, "Audio", resources.mainMenuFont)
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
					onTabTouched(this, OPTIONS_TAB.AUDIO);
					break;
				}

				return true;
			}
		};

		animationsTab.setSize(factor * optionsMenuAnimationsCanvas.getWidth(), 0.1f * camera.getHeight());
		animationsTab.setPosition(Utils.left(optionsMenuAnimationsCanvas) + 0.5f * factor * optionsMenuAnimationsCanvas.getWidth(),
				Utils.bottom(optionsMenuAnimationsCanvas) - 1f * Utils.halfY(animationsTab));

		graphicsTab.setSize(animationsTab.getWidth(), animationsTab.getHeight());
		graphicsTab.setPosition(Utils.right(animationsTab) + Utils.halfX(graphicsTab), animationsTab.getY());

		audioTab.setSize(animationsTab.getWidth(), animationsTab.getHeight());
		audioTab.setPosition(Utils.right(graphicsTab) + Utils.halfX(audioTab), animationsTab.getY());

		optionsMenu.attachChild(animationsTab);
		optionsMenu.attachChild(graphicsTab);
		optionsMenu.attachChild(audioTab);

		optionsMenu.registerTouchArea(animationsTab);
		optionsMenu.registerTouchArea(graphicsTab);
		optionsMenu.registerTouchArea(audioTab);

		// Animations tab selected
		animationsTab.setColor(Utils.OtherColors.WHITE);
		graphicsTab.setColor(Utils.OtherColors.DARK_GREY);
		audioTab.setColor(Utils.OtherColors.DARK_GREY);

		optionsMenuAnimationsCanvas.setVisible(true);
	}

	private void onTabTouched(RiskaSprite riskaSprite, OPTIONS_TAB x)
	{

		switch(x)
		{

		case ANIMATIONS:
			onTabSelected(x);
			break;

		case GRAPHICS:
			onTabSelected(x);
			break;

		case AUDIO:
			onTabSelected(x);
			break;

		default:
			break;
		}
	}

	private void onTabSelected(OPTIONS_TAB x)
	{
		switch(x)
		{

		case ANIMATIONS:
			animationsTab.setColor(Utils.OtherColors.WHITE);
			graphicsTab.setColor(Utils.OtherColors.DARK_GREY);
			audioTab.setColor(Utils.OtherColors.DARK_GREY);

			optionsMenuAnimationsCanvas.setVisible(true);
			optionsMenuGraphicsCanvas.setVisible(false);
			optionsMenuAudioCanvas.setVisible(false);
			break;

		case GRAPHICS:
			animationsTab.setColor(Utils.OtherColors.DARK_GREY);
			graphicsTab.setColor(Utils.OtherColors.WHITE);
			audioTab.setColor(Utils.OtherColors.DARK_GREY);

			optionsMenuAnimationsCanvas.setVisible(false);
			optionsMenuGraphicsCanvas.setVisible(true);
			optionsMenuAudioCanvas.setVisible(false);
			break;

		case AUDIO:
			animationsTab.setColor(Utils.OtherColors.DARK_GREY);
			graphicsTab.setColor(Utils.OtherColors.DARK_GREY);
			audioTab.setColor(Utils.OtherColors.WHITE);

			optionsMenuAnimationsCanvas.setVisible(false);
			optionsMenuGraphicsCanvas.setVisible(false);
			optionsMenuAudioCanvas.setVisible(true);
			break;

		default:
			break;
		}
	}
	
	private void onButtonTouched(ButtonSprite buttonSprite, OPTIONS_BUTTON x)
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
			return mainMenu;

		case START_GAME:
			return startGameMenu;

		case OPTIONS:
			return optionsMenu;

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
				startButton.animate();
			}
			changeChildSceneFromTo(CHILD.MAIN, CHILD.START_GAME);	
			break;

		case MAIN_OPTIONS:
			if(GameOptions.menuAnimationsEnabled())
			{
				optionsButton.animate();
			}
			changeChildSceneFromTo(CHILD.MAIN, CHILD.OPTIONS);
			break;

		case START_NEW:
			if(GameOptions.menuAnimationsEnabled())
			{
				newGameButton.animate();
			}
			resetGameInfo(); // TODO : verify necessity of reseting
			changeChildSceneFromTo(CHILD.START_GAME, CHILD.CHOOSE_PLAYERS);
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

			waitForAnimation = new DelayModifier(MenuHUD.doorsAnimationWaitingTime)
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

			waitForAnimation = new DelayModifier(MenuHUD.doorsAnimationWaitingTime)
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
