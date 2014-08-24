package feup.lpoo.riska.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import android.util.Log;
import android.view.MotionEvent;
import feup.lpoo.riska.gameInterface.MenuHUD;
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
	private final int MAX_NAME_CHARS = 50;

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

	private enum CHILD { ANY, MAIN, OPTIONS, START_GAME, CHOOSE_MAP, CHOOSE_PLAYERS, CHOOSE_FACTION, CHOOSE_DIFFICULTY};
	private enum PLAYERS_BUTTON { ADD, REMOVE, NAME, CPU_BOX};
	private enum OPTIONS_BUTTON { SFX, MUSIC, ANIMATIONS};

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
	//private RiskaMenuItem startButton;
	private RiskaMenuItem optionsButton;

	// START MENU
	private RiskaMenuItem newGameButton;
	private RiskaMenuItem loadGameButton;

	// OPTIONS MENU

	private RiskaCanvas optionsMenuAudioCanvas;
	private RiskaCanvas optionsMenuGraphicsCanvas;
	private ButtonSprite switchAnimations;
	private Text textAnimations;
	private ButtonSprite switchSFX;
	private ButtonSprite switchMusic;
	private Text textSFX;
	private Text textMusic;

	// CHOOSE PLAYERS MENU
	private RiskaMenuItem choosePlayerNextButton;
	private Text titleTextChoosePlayers;
	private Text textIsCPU;

	private ButtonSprite[] addPlayerButton;
	private ButtonSprite[] removePlayerButton;
	private ButtonSprite[] playerNameButton;
	private Text[] playerName;
	private ButtonSprite[] cpuCheckBox;

	private boolean[] playerIsCPU;
	private boolean[] playerActive;
	private boolean[] playerRemovable;
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
			// TODO : change this
			setBackground(background);
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

		setTouchAreaBindingOnActionDownEnabled(true);
		setTouchAreaBindingOnActionMoveEnabled(true);
	}

	@Override
	public void onSceneShow()
	{
		menuHUD.openSlideDoors();
	}

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
				resources.buttonRegion, 
				vbom, "Start", resources.mainMenuFont,
				null, null, resources.barHRegion, resources.barHRegion);

		Utils.wrap(startButton, 0.5f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		startButton.setPosition(camera.getCenterX(), 0.53f * camera.getHeight());
		//startButton.debug();

		optionsButton = new RiskaMenuItem(MAIN_OPTIONS,
				resources.buttonRegion, 
				vbom, "Options", resources.mainMenuFont,
				null, null, resources.barHRegion, resources.barHRegion);

		Utils.wrap(optionsButton, 0.5f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		optionsButton.setPosition(camera.getCenterX(), 0.23f * camera.getHeight());

		mainMenu.addMenuItem(startButton);
		mainMenu.addMenuItem(optionsButton);
		mainMenu.setOnMenuItemClickListener(this);
	}

	// ==================================================
	// START GAME
	// ==================================================
	private void createStartGameMenu()
	{
		startGameMenu = new MenuScene(camera);

		startGameMenu.setBackgroundEnabled(false);


		newGameButton = new RiskaMenuItem(START_NEW,
				resources.buttonRegion,
				vbom, "New", resources.mainMenuFont,
				null, null, resources.barHRegion, resources.barHRegion);

		loadGameButton = new RiskaMenuItem(START_LOAD,
				resources.buttonRegion,
				vbom, "Load", resources.mainMenuFont,
				null, null, resources.barHRegion, resources.barHRegion);


		Utils.wrap(newGameButton, 0.5f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		newGameButton.setPosition(camera.getCenterX(), 0.53f * camera.getHeight());

		Utils.wrap(loadGameButton, 0.5f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		loadGameButton.setPosition(camera.getCenterX(), 0.23f * camera.getHeight());

		startGameMenu.addMenuItem(newGameButton);
		startGameMenu.addMenuItem(loadGameButton);

		startGameMenu.setOnMenuItemClickListener(this);
	}

	// ==================================================
	// OPTIONS
	// ==================================================
	private void createOptionsMenu()
	{
		optionsMenu = new MenuScene(camera);
		optionsMenu.setBackgroundEnabled(false);

		createGraphicsMenu();

		createAudioMenu();

		optionsMenuGraphicsCanvas.setVisible(true);
	}

	private void createGraphicsMenu()
	{
		int numberOfItems = 1;

		float heightFactor = 1f / (numberOfItems + 1);

		int index = 0;

		switchAnimations = new ButtonSprite(0, 0, resources.switchRegion, vbom) {

			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{

				case MotionEvent.ACTION_UP:
					if(Utils.isBetween(pX, 0, this.getWidth()) && Utils.isBetween(pY, 0, this.getHeight()))
					{
						onButtonTouched(this, OPTIONS_BUTTON.ANIMATIONS);
					}
					break;
				}

				return true;
			}

		};
		optionsMenu.registerTouchArea(switchAnimations);
		switchAnimations.setCurrentTileIndex(GameOptions.menuAnimationsEnabled() ? 0 : 1);
		
		textAnimations = new Text(0, 0, resources.mainMenuFont, "Menu Animations", vbom);
		textAnimations.setColor(Color.WHITE);

		optionsMenuGraphicsCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), camera.getWidth(), 0.65f * camera.getHeight());

		index++;
		optionsMenuGraphicsCanvas.addGraphic(textAnimations, 0.25f , index * heightFactor, 0.25f, 0.17f);
		optionsMenuGraphicsCanvas.addGraphic(switchAnimations, 0.75f , index * heightFactor, 0.25f, 0.17f);

		optionsMenuGraphicsCanvas.setVisible(false);

		optionsMenu.attachChild(optionsMenuGraphicsCanvas);
	}

	private void createAudioMenu()
	{
		int numberOfItems = 2;

		float heightFactor = 1f / (numberOfItems + 1);

		int index = 0;

		switchSFX = new ButtonSprite(0, 0, resources.switchRegion, vbom) {

			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{

				case MotionEvent.ACTION_UP:
					if(Utils.isBetween(pX, 0, this.getWidth()) && Utils.isBetween(pY, 0, this.getHeight()))
					{
						onButtonTouched(this, OPTIONS_BUTTON.SFX);
					}
					break;
				}

				return true;
			}

		};
		optionsMenu.registerTouchArea(switchSFX);

		switchMusic = new ButtonSprite(0, 0, resources.switchRegion, vbom) {

			@Override
			public boolean onAreaTouched(TouchEvent ev, float pX, float pY)
			{
				switch(ev.getMotionEvent().getActionMasked()) 
				{

				case MotionEvent.ACTION_UP:
					if(Utils.isBetween(pX, 0, this.getWidth()) && Utils.isBetween(pY, 0, this.getHeight()))
					{
						onButtonTouched(this, OPTIONS_BUTTON.MUSIC);
					}
					break;
				}

				return true;
			}

		};
		optionsMenu.registerTouchArea(switchMusic);

		textMusic = new Text(0, 0, resources.mainMenuFont, "Music", vbom);
		textMusic.setColor(Color.WHITE);

		textSFX = new Text(0, 0, resources.mainMenuFont, "SFX", vbom);
		textSFX.setColor(Color.WHITE);

		switchMusic.setCurrentTileIndex(GameOptions.musicEnabled() ? 0 : 1);
		switchSFX.setCurrentTileIndex(GameOptions.sfxEnabled() ? 0 : 1);

		optionsMenuAudioCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), camera.getWidth(), 0.65f * camera.getHeight());

		index++;
		optionsMenuAudioCanvas.addGraphic(textMusic, 0.25f , index * heightFactor, 0.25f, 0.17f);
		optionsMenuAudioCanvas.addGraphic(switchMusic, 0.75f , index * heightFactor, 0.25f, 0.17f);

		index++;
		optionsMenuAudioCanvas.addGraphic(textSFX, 0.25f , index * heightFactor, 0.25f, 0.17f);
		optionsMenuAudioCanvas.addGraphic(switchSFX, 0.75f , index * heightFactor, 0.25f, 0.17f);

		optionsMenuAudioCanvas.setVisible(false);

		optionsMenu.attachChild(optionsMenuAudioCanvas);
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

		case ANIMATIONS:
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

		addPlayerButton = new ButtonSprite[GameInfo.maxPlayers];
		removePlayerButton = new ButtonSprite[GameInfo.maxPlayers];
		playerNameButton = new ButtonSprite[GameInfo.maxPlayers];
		playerName = new Text[GameInfo.maxPlayers];
		cpuCheckBox = new ButtonSprite[GameInfo.maxPlayers];

		playerIsCPU = new boolean[GameInfo.maxPlayers];
		playerActive = new boolean[GameInfo.maxPlayers];
		playerEditable = new boolean[GameInfo.maxPlayers];
		playerRemovable = new boolean[GameInfo.maxPlayers];

		createPlayerSpots();

		titleTextChoosePlayers = new Text(0, 0, resources.mainMenuFont, "EDIT PLAYERS", vbom);
		Utils.wrap(titleTextChoosePlayers, 0.5f * camera.getWidth(), 0.11f * camera.getHeight(), 0.8f);
		titleTextChoosePlayers.setPosition( 0.5f * camera.getWidth(), 0.94f * camera.getHeight());
		titleTextChoosePlayers.setColor(Color.WHITE);

		choosePlayerNextButton = new RiskaMenuItem(PLAYERS_SELECT,
				resources.buttonRegion,
				vbom, "Next", resources.mainMenuFont,
				resources.barVRegion, resources.barVRegion, null, null);

		choosePlayerNextButton.setSize(0.5f * camera.getWidth(), 0.12f * camera.getHeight());
		choosePlayerNextButton.setPosition(0.5f * camera.getWidth(), 0.06f * camera.getHeight());

		choosePlayersMenu.attachChild(titleTextChoosePlayers);
		choosePlayersMenu.attachChild(textIsCPU);

		choosePlayersMenu.addMenuItem(choosePlayerNextButton);

		choosePlayersMenu.setOnMenuItemClickListener(this);
	}

	private void createPlayerSpots()
	{
		// Creates all players buttons
		for(int i = 0; i < GameInfo.maxPlayers; i++)
		{

			addPlayerButton[i] = new ButtonSprite(0, 0, resources.playerAddButtonRegion, vbom)
			{
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
				{
					switch(ev.getMotionEvent().getActionMasked()) 
					{
					case MotionEvent.ACTION_DOWN:
						onButtonPressed(this, getTag(), PLAYERS_BUTTON.ADD);
						break;

					case MotionEvent.ACTION_UP:
						if(Utils.isBetween(pX, 0, this.getWidth()) && Utils.isBetween(pY, 0, this.getHeight()))
						{
							onButtonTouched(this, getTag(), PLAYERS_BUTTON.ADD);
						}
						else
						{
							onButtonReleased(this, getTag(), PLAYERS_BUTTON.ADD);
						}
						break;
					}

					return true;
				}
			};

			addPlayerButton[i].setTag(i);
			choosePlayersMenu.registerTouchArea(addPlayerButton[i]);

			removePlayerButton[i] = new ButtonSprite(0, 0, resources.playerRemoveButtonRegion, vbom)
			{
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
				{
					switch(ev.getAction()) 
					{
					case MotionEvent.ACTION_DOWN:
						onButtonPressed(this, getTag(), PLAYERS_BUTTON.REMOVE);
						break;

					case MotionEvent.ACTION_UP:
						if(Utils.isBetween(pX, 0, this.getWidth()) && Utils.isBetween(pY, 0, this.getHeight()))
						{
							onButtonTouched(this, getTag(), PLAYERS_BUTTON.REMOVE);
						}
						else
						{
							onButtonReleased(this, getTag(), PLAYERS_BUTTON.REMOVE);
						}
					}

					return true;
				}
			};
			removePlayerButton[i].setTag(i);
			choosePlayersMenu.registerTouchArea(removePlayerButton[i]);

			playerName[i] = new Text(0, 0, resources.mainMenuFont, "", MAX_NAME_CHARS, vbom);
			playerNameButton[i] = new ButtonSprite(0, 0, resources.buttonRegion, vbom)
			{
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
				{
					switch(ev.getAction()) 
					{
					case MotionEvent.ACTION_DOWN:
						onButtonPressed(this, getTag(), PLAYERS_BUTTON.NAME);
						break;

					case MotionEvent.ACTION_UP:
						if(Utils.isBetween(pX, 0, this.getWidth()) && Utils.isBetween(pY, 0, this.getHeight()))
						{
							onButtonTouched(this, getTag(), PLAYERS_BUTTON.NAME);
						}
						else
						{
							onButtonReleased(this, getTag(), PLAYERS_BUTTON.NAME);
						}
					}

					return true;
				}
			};
			playerNameButton[i].setTag(i);
			choosePlayersMenu.registerTouchArea(playerNameButton[i]);

			cpuCheckBox[i] = new ButtonSprite(0, 0, resources.playerCheckBoxButtonRegion, vbom)
			{
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
				{
					switch(ev.getAction()) 
					{
					case MotionEvent.ACTION_UP:
						if(Utils.isBetween(pX, 0, this.getWidth()) && Utils.isBetween(pY, 0, this.getHeight()))
						{
							Log.d("Buttons", "Touched switch CPU BOX");
							onButtonTouched(this, getTag(), PLAYERS_BUTTON.CPU_BOX);
						}
					}

					return true;
				}
			};
			cpuCheckBox[i].setTag(i);
			choosePlayersMenu.registerTouchArea(cpuCheckBox[i]);
		}

		setInitialPlayersVariables();

		float heightFactor = 1f / (GameInfo.maxPlayers + 1);	
		float buttonsHeight = heightFactor * 0.8f * camera.getHeight();
		float buttonsWidth = 0.15f * camera.getWidth();

		textIsCPU = new Text(0, 0, resources.mainMenuFont, "cpu?", vbom);
		Utils.wrap(textIsCPU, 0.2f * camera.getWidth(), buttonsHeight, 0.5f);
		textIsCPU.setPosition(0.75f * camera.getWidth(), (1f - heightFactor) * 0.75f * camera.getHeight() + 0.15f * camera.getHeight());
		textIsCPU.setColor(Color.WHITE);

		// Places all players buttons
		for(int i = 0; i < GameInfo.maxPlayers; i++)
		{	
			float buttonsY = (1f - ((i + 1) * heightFactor)) * 0.75f * camera.getHeight() + 0.15f * camera.getHeight();

			Utils.wrap(addPlayerButton[i], buttonsWidth, buttonsHeight, 0.7f);
			addPlayerButton[i].setPosition(0.25f * camera.getWidth(), buttonsY);

			Utils.wrap(removePlayerButton[i], buttonsWidth, buttonsHeight, 0.7f);
			removePlayerButton[i].setPosition(addPlayerButton[i]);

			Utils.wrap(playerNameButton[i], 0.5f * camera.getWidth(), buttonsHeight, 0.9f);
			playerNameButton[i].setPosition(0.5f * camera.getWidth(), buttonsY);

			Utils.wrap(playerName[i], 1f * playerNameButton[i].getWidth(), 0.8f * playerNameButton[i].getHeight(), 0.85f);
			playerName[i].setPosition(Utils.getCenterX(playerNameButton[i]), Utils.getCenterY(playerNameButton[i]));
			playerName[i].setColor(Color.WHITE);
			playerNameButton[i].attachChild(playerName[i]);

			Utils.wrap(cpuCheckBox[i], 1f * camera.getWidth(), buttonsHeight, 0.8f);
			cpuCheckBox[i].setPosition(0.75f * camera.getWidth(), buttonsY);

			choosePlayersMenu.attachChild(cpuCheckBox[i]);
			choosePlayersMenu.attachChild(playerNameButton[i]);
			choosePlayersMenu.attachChild(addPlayerButton[i]);
			choosePlayersMenu.attachChild(removePlayerButton[i]);
		}
	}

	private void setInitialPlayersVariables()
	{
		for(int i = 0; i < GameInfo.maxPlayers; i++)
		{
			playerIsCPU[i] = (i < GameInfo.minHumanPlayers) ? false : true;
			playerActive[i] = (i < GameInfo.minPlayers) ? true : false;
			playerEditable[i] = (i < GameInfo.minHumanPlayers) ? false : true;
			playerRemovable[i] = (i < GameInfo.minPlayers) ? false : true;

			playerName[i].setText(playerIsCPU[i] ? "CPU": "Player");

			addPlayerButton[i].setVisible((i < GameInfo.minPlayers) ? false : true);

			removePlayerButton[i].setVisible(false);

			cpuCheckBox[i].setCurrentTileIndex(playerIsCPU[i] ? 0 : 1);
			cpuCheckBox[i].setVisible(playerActive[i] && playerEditable[i]);

			playerNameButton[i].setVisible(playerActive[i]);
			playerName[i].setVisible(playerActive[i]);
		}
	}

	private void onButtonReleased(ButtonSprite buttonSprite, int tag, PLAYERS_BUTTON x)
	{
		switch(x)
		{

		case ADD:
			buttonSprite.setCurrentTileIndex(0);
			break;

		case REMOVE:
			buttonSprite.setCurrentTileIndex(0);
			break;

		case NAME:
			playerName[tag].setVisible(true);
			//buttonSprite.setCurrentTileIndex(0);
			break;

		case CPU_BOX:
			buttonSprite.setCurrentTileIndex(0);
			break;

		default:
			break;
		}	
	}

	private void onButtonPressed(ButtonSprite buttonSprite, int tag, PLAYERS_BUTTON x)
	{
		switch(x)
		{

		case ADD:
			buttonSprite.setCurrentTileIndex(1);
			break;

		case REMOVE:
			buttonSprite.setCurrentTileIndex(1);
			break;

		case NAME:
			playerName[tag].setVisible(false);
			//buttonSprite.setCurrentTileIndex(1);
			break;
			
		default:
			break;
		}	
	}

	private void onButtonTouched(ButtonSprite buttonSprite, int tag, PLAYERS_BUTTON x)
	{
		switch(x)
		{

		case ADD:
			playerNameButton[tag].setVisible(true);
			playerName[tag].setVisible(true);
			addPlayerButton[tag].setVisible(false);

			removePlayerButton[tag].setVisible(true);
			cpuCheckBox[tag].setVisible(true);

			playerActive[tag] = true;

			onButtonReleased(buttonSprite, tag, x);
			break;

		case REMOVE:
			playerNameButton[tag].setVisible(false);
			playerName[tag].setVisible(false);
			addPlayerButton[tag].setVisible(true);

			removePlayerButton[tag].setVisible(false);
			cpuCheckBox[tag].setVisible(false);

			playerActive[tag] = false;

			onButtonReleased(buttonSprite, tag, x);
			break;

		case NAME:
			// TODO : edit name
			onButtonReleased(buttonSprite, tag, x);
			break;

		case CPU_BOX:
			setPlayerAs(tag, playerIsCPU[tag]);
			break;

		default:
			break;
		}
	}

	private void setPlayerAs(int tag, boolean value)
	{
		if(value)
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
	}

	private void updatePlayerName(int tag, String pName)
	{
		playerName[tag].setText(pName);
		Utils.wrap(playerName[tag], 1f * playerNameButton[tag].getWidth(), 0.8f * playerNameButton[tag].getHeight(), 0.85f);
	}

	// ==================================================
	// CHOOSE FACTION
	// ==================================================	
	private void createChooseFactionMenu()
	{
		chooseFactionMenu = new MenuScene(camera);

		chooseFactionMenu.setBackgroundEnabled(false);

		playerFaction = new int[GameInfo.numberOfFactions];
		setInitialFactionsVariables();

		titleTextChooseFaction = new Text(0, 0, resources.mainMenuFont, "Choose Faction: (Player " + currentPlayer + ")", 100, vbom);

		Utils.wrap(titleTextChooseFaction, 0.5f * camera.getWidth(), 0.2f * camera.getHeight(), 0.9f);

		titleTextChooseFaction.setPosition( 0.5f * camera.getWidth(), 0.8f * camera.getHeight());
		titleTextChooseFaction.setColor(Color.WHITE);

		chooseFactionNextButton = new RiskaMenuItem(FACTION_NEXT,
				resources.buttonRegion,
				vbom, ">", resources.mainMenuFont,
				resources.barVRegion, resources.barVRegion, null, null);

		chooseFactionPreviousButton = new RiskaMenuItem(FACTION_PREVIOUS,
				resources.buttonRegion,
				vbom, "<", resources.mainMenuFont,
				resources.barVRegion, resources.barVRegion, null, null);

		chooseFactionSelectButton = new RiskaMenuItem(FACTION_SELECT,
				resources.buttonRegion,
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
	}

	private void setInitialFactionsVariables()
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
					Log.e("Riska", "Circled through all factions. None available for chosing!");
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
					Log.e("Riska", "Circled through all factions. None available for chosing!");
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
			Log.i("Riska","MainMenuScene > getChildScene() > No valid child exists.");
			return null;
		}
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pX, float pY)
	{

		switch(pMenuItem.getID())
		{

		case MAIN_START:
			changeChildSceneFromTo(CHILD.MAIN, CHILD.START_GAME);	
			break;

		case MAIN_OPTIONS:
			changeChildSceneFromTo(CHILD.MAIN, CHILD.OPTIONS);
			break;

		case START_NEW:
			resetGameInfo(); // TODO : verify necessity of reseting
			changeChildSceneFromTo(CHILD.START_GAME, CHILD.CHOOSE_PLAYERS);
			setBackground(new Background(Color.BLACK));
			break;

		case START_LOAD:
			//loadGameButton.close();
			//sceneManager.loadGameScene(engine);
			break;

		case FACTION_NEXT:
			selectFaction(1);			
			break;

		case FACTION_PREVIOUS:
			selectFaction(-1);
			break;

		case FACTION_SELECT:
			if(currentPlayer < GameInfo.humanPlayers)
			{
				chooseFactionSelectButton.animate();
				playerFaction[currentPlayer] = selectedFaction;
				currentPlayer++;
				titleTextChooseFaction.setText("Choose Faction: (Player " + currentPlayer + ")");

				Utils.wrap(titleTextChooseFaction, 0.5f * camera.getWidth(), 0.11f * camera.getHeight(), 0.8f);
				selectFaction(1);
			}

			if(currentPlayer == GameInfo.humanPlayers)
			{
				chooseFactionSelectButton.close();
				saveFactionsInfo();
				changeSceneToGame();
			}
			break;

		case PLAYERS_SELECT:
			savePlayersInfo();
			changeChildSceneFromTo(CHILD.CHOOSE_PLAYERS, CHILD.CHOOSE_FACTION);
			break;

		default:
			break;
		}

		return false;
	}

	private void openButtons(CHILD x)
	{
		switch(x)
		{

		case MAIN:
			startButton.open();
			optionsButton.open();
			break;

		case START_GAME:
			newGameButton.open();
			loadGameButton.open();
			break;

		case OPTIONS:
			// TODO
			break;

		case CHOOSE_FACTION:
			chooseFactionSelectButton.open();
			chooseFactionNextButton.open();
			chooseFactionPreviousButton.open();
			break;

		case CHOOSE_PLAYERS:
			choosePlayerNextButton.open();
			break;

		default:
			Log.i("Riska","MainMenuScene > getChildScene() > No valid child exists.");
			break;
		}
	}

	private void closeButtons(CHILD x)
	{
		switch(x)
		{

		case MAIN:
			startButton.close();
			optionsButton.close();
			break;

		case START_GAME:
			newGameButton.close();
			loadGameButton.close();
			break;

		case OPTIONS:
			// TODO
			break;

		case CHOOSE_FACTION:
			chooseFactionSelectButton.close();
			chooseFactionNextButton.close();
			chooseFactionPreviousButton.close();
			break;

		case CHOOSE_PLAYERS:
			choosePlayerNextButton.close();
			break;

		default:
			Log.i("Riska","MainMenuScene > getChildScene() > No valid child exists.");
			break;
		}
	}

	private void changeChildSceneFromTo(final CHILD from, final CHILD to)
	{
		final MenuScene child = getChildScene(to);

		if(GameOptions.menuAnimationsEnabled())
		{
			if(from != CHILD.ANY)
			{
				closeButtons(from);
			}

			menuHUD.animateSlideDoors();

			waitForAnimation = new DelayModifier(MenuHUD.doorsAnimationWaitingTime)
			{
				@Override
				protected void onModifierFinished(IEntity pItem)
				{
					setChildScene(child);
					openButtons(to);
					openButtons(from);
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

		Log.d("Riska", "[MainMenuScene] Saving players info...");
		Log.d("Riska", "[MainMenuScene] Number of players: " + GameInfo.numberOfPlayers);
		Log.d("Riska", "[MainMenuScene] HUMAN players:     " + GameInfo.humanPlayers);
		Log.d("Riska", "[MainMenuScene] CPU players:     " + GameInfo.cpuPlayers);
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
		setInitialPlayersVariables();	
		setInitialFactionsVariables();
	}

}
