package feup.lpoo.riska.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import feup.lpoo.riska.gameInterface.AnimatedButtonSpriteMenuItem;
import feup.lpoo.riska.gameInterface.AnimatedTextButtonSpriteMenuItem;
import feup.lpoo.riska.gameInterface.MenuHUD;
import feup.lpoo.riska.gameInterface.RiskaMenuItem;
import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.io.SharedPreferencesManager;
import feup.lpoo.riska.logic.GameInfo;
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

	private enum CHILD { MAIN, OPTIONS, START_GAME, CHOOSE_MAP, CHOOSE_PLAYERS, CHOOSE_FACTION, CHOOSE_DIFFICULTY};
	private enum BUTTON { ADD, REMOVE, NAME, CPU_BOX};

	private final int MAIN_START = 0;
	private final int MAIN_OPTIONS = 1;

	private final int START_NEW = 2;
	private final int START_LOAD = 3;
	private final int START_RETURN = 4;

	private final int OPTIONS_SFX = 5;
	private final int OPTIONS_MUSIC = 6;
	private final int OPTIONS_RETURN = 7;

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
	private AnimatedButtonSpriteMenuItem sliderSFX;
	private AnimatedButtonSpriteMenuItem sliderMusic;
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
	private RiskaMenuItem nextFactionButton;
	private RiskaMenuItem previousFactionButton;
	private RiskaMenuItem selectFactionButton;
	
	private ButtonSprite factionPriColor;
	private ButtonSprite factionSecColor;
	private Sprite factionFrame;

	private int currentPlayer = 0;
	private int selectedFaction = 0;
	private int[] playerFaction;


	// MUSIC OPTIONS
	private boolean musicOn = true;
	private boolean sfxOn = false;

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
		if(this.getChildScene().equals(mainMenu))
		{
			System.exit(0);
		}
		else
		{				
			detachChildren();
			resetGameInfo();

			changeChildSceneTo(mainMenu);
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

		createChildScene(CHILD.MAIN);
		createChildScene(CHILD.OPTIONS);
		createChildScene(CHILD.START_GAME);

		//createChildScene(CHILD.CHOOSE_MAP);
		createChildScene(CHILD.CHOOSE_PLAYERS);
		createChildScene(CHILD.CHOOSE_FACTION);
		//createChildScene(CHILD.CHOOSE_DIFFICULTY);

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

	private void createChildScene(CHILD x)
	{
		switch(x)
		{

		case MAIN:
			createMainMenu();
			break;

		case START_GAME:
			createStartGameMenu();
			break;

		case OPTIONS:
			createOptionsMenu();
			break;

		case CHOOSE_PLAYERS:
			createChoosePlayersMenu();
			break;

		case CHOOSE_FACTION:
			createChooseFactionMenu();
			break;

		default:
			Log.i("Riska","MainMenuScene > createChildScene() > Child not in the featured options.");
			break;
		}
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
				vbom, "Start", resources.mainMenuFont);
		
		Utils.wrap(startButton, 0.5f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		startButton.setPosition(camera.getCenterX(), 0.53f * camera.getHeight());
		

		optionsButton = new RiskaMenuItem(MAIN_OPTIONS,
				resources.buttonRegion, 
				vbom, "Options", resources.mainMenuFont);

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
				vbom, "New", resources.mainMenuFont);

		loadGameButton = new RiskaMenuItem(START_LOAD,
				resources.buttonRegion,
				vbom, "Load", resources.mainMenuFont);


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

		sliderSFX= new AnimatedButtonSpriteMenuItem(OPTIONS_SFX,
				resources.switchRegion.getWidth(),
				resources.switchRegion.getHeight(),
				resources.switchRegion, vbom);

		sliderMusic = new AnimatedButtonSpriteMenuItem(OPTIONS_MUSIC,
				resources.switchRegion.getWidth(),
				resources.switchRegion.getHeight(),
				resources.switchRegion, vbom);



		textMusic = new Text(0, 0, resources.mainMenuFont, "MUSIC", vbom);
		Utils.wrap(textMusic, 0.25f * camera.getWidth(), 0.2f * camera.getHeight(), 0.8f);
		textMusic.setPosition( 0.3f * camera.getWidth(), 0.75f * camera.getHeight());
		textMusic.setColor(Color.WHITE);

		textSFX = new Text(0, 0, resources.mainMenuFont, "SFX", vbom);
		Utils.wrap(textSFX, 0.25f * camera.getWidth(), 0.2f * camera.getHeight(), 0.8f);
		textSFX.setPosition(0.3f * camera.getWidth(), 0.50f * camera.getHeight());
		textSFX.setColor(Color.WHITE);

		Utils.wrap(sliderMusic, 0.25f * camera.getWidth(), 0.3f * camera.getHeight(), 1f);
		sliderMusic.setPosition(0.6f * camera.getWidth(), textMusic.getY());
		sliderMusic.setCurrentTileIndex(musicOn ? 0 : 1);

		Utils.wrap(sliderSFX, 0.25f * camera.getWidth(), 0.3f * camera.getHeight(), 1f);
		sliderSFX.setPosition(0.6f * camera.getWidth(), textSFX.getY());
		sliderSFX.setCurrentTileIndex(sfxOn ? 0 : 1);

		optionsMenu.addMenuItem(sliderMusic);
		optionsMenu.addMenuItem(sliderSFX);

		optionsMenu.attachChild(textMusic);
		optionsMenu.attachChild(textSFX);

		optionsMenu.setOnMenuItemClickListener(this);	
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
				vbom, "Next", resources.mainMenuFont);

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
						onButtonPressed(this, getTag(), BUTTON.ADD);
						break;
						
					case MotionEvent.ACTION_UP:
						if(Utils.isBetween(pX, 0, this.getWidth()) && Utils.isBetween(pY, 0, this.getHeight()))
						{
							onButtonTouched(this, getTag(), BUTTON.ADD);
						}
						else
						{
							onButtonReleased(this, getTag(), BUTTON.ADD);
						}
						break;
					}

					return true;
				}
			};
			
			addPlayerButton[i].setTag(i);
			registerTouchArea(addPlayerButton[i]);

			removePlayerButton[i] = new ButtonSprite(0, 0, resources.playerRemoveButtonRegion, vbom)
			{
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
				{
					switch(ev.getAction()) 
					{
					case MotionEvent.ACTION_DOWN:
						onButtonPressed(this, getTag(), BUTTON.REMOVE);
						break;
						
					case MotionEvent.ACTION_UP:
						if(Utils.isBetween(pX, 0, this.getWidth()) && Utils.isBetween(pY, 0, this.getHeight()))
						{
							onButtonTouched(this, getTag(), BUTTON.REMOVE);
						}
						else
						{
							onButtonReleased(this, getTag(), BUTTON.REMOVE);
						}
					}

					return true;
				}
			};
			removePlayerButton[i].setTag(i);
			registerTouchArea(removePlayerButton[i]);

			playerName[i] = new Text(0, 0, resources.mainMenuFont, "", MAX_NAME_CHARS, vbom);
			playerNameButton[i] = new ButtonSprite(0, 0, resources.buttonRegion, vbom)
			{
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
				{
					switch(ev.getAction()) 
					{
					case MotionEvent.ACTION_DOWN:
						onButtonPressed(this, getTag(), BUTTON.NAME);
						break;
						
					case MotionEvent.ACTION_UP:
						if(Utils.isBetween(pX, 0, this.getWidth()) && Utils.isBetween(pY, 0, this.getHeight()))
						{
							onButtonTouched(this, getTag(), BUTTON.NAME);
						}
						else
						{
							onButtonReleased(this, getTag(), BUTTON.NAME);
						}
					}

					return true;
				}
			};
			playerNameButton[i].setTag(i);
			registerTouchArea(playerNameButton[i]);

			cpuCheckBox[i] = new ButtonSprite(0, 0, resources.playerCheckBoxButtonRegion, vbom)
			{
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
				{
					switch(ev.getAction()) 
					{
					case MotionEvent.ACTION_DOWN:
						onButtonPressed(this, getTag(), BUTTON.CPU_BOX);
						break;
						
					case MotionEvent.ACTION_UP:
						if(Utils.isBetween(pX, 0, this.getWidth()) && Utils.isBetween(pY, 0, this.getHeight()))
						{
							onButtonTouched(this, getTag(), BUTTON.CPU_BOX);
						}
					}

					return true;
				}
			};
			cpuCheckBox[i].setTag(i);
			registerTouchArea(cpuCheckBox[i]);
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

			choosePlayersMenu.attachChild(playerNameButton[i]);
			choosePlayersMenu.attachChild(addPlayerButton[i]);
			choosePlayersMenu.attachChild(removePlayerButton[i]);
			choosePlayersMenu.attachChild(cpuCheckBox[i]);
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

			cpuCheckBox[i].setCurrentTileIndex(playerIsCPU[i] ? 1 : 0);
			cpuCheckBox[i].setVisible(playerActive[i] && playerEditable[i]);

			playerNameButton[i].setVisible(playerActive[i]);
			playerName[i].setVisible(playerActive[i]);
		}
	}

	private void onButtonReleased(ButtonSprite buttonSprite, int tag, BUTTON x)
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

	private void onButtonPressed(ButtonSprite buttonSprite, int tag, BUTTON x)
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

		case CPU_BOX:
			// Nothing now
			break;

		default:
			break;
		}	
	}

	private void onButtonTouched(ButtonSprite buttonSprite, int tag, BUTTON x)
	{
		switch(x)
		{

		case ADD:
			playerNameButton[tag].setVisible(true);
			playerName[tag].setVisible(true);
			addPlayerButton[tag].setVisible(false);

			removePlayerButton[tag].setVisible(true);
			cpuCheckBox[tag].setVisible(true);

			//TODO : should this happen or not?
			//setPlayerAsCPU(tag, true);

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
			setPlayerAsCPU(tag, !playerIsCPU[tag]);
			break;

		default:
			break;
		}
	}

	private void setPlayerAsCPU(int tag, boolean value)
	{
		if(value)
		{
			playerIsCPU[tag] = true;
			updatePlayerName(tag, "CPU");
			cpuCheckBox[tag].setCurrentTileIndex(1);
		}
		else
		{
			playerIsCPU[tag] = false;
			updatePlayerName(tag, "Player");
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

		Utils.wrap(titleTextChooseFaction, 0.5f * camera.getWidth(), 0.11f * camera.getHeight(), 0.8f);

		titleTextChooseFaction.setPosition( 0.5f * camera.getWidth(), 0.94f * camera.getHeight());
		titleTextChooseFaction.setColor(Color.WHITE);

		nextFactionButton = new RiskaMenuItem(FACTION_NEXT,
				resources.buttonRegion,
				vbom, ">", resources.mainMenuFont);

		previousFactionButton = new RiskaMenuItem(FACTION_PREVIOUS,
				resources.buttonRegion,
				vbom, "<", resources.mainMenuFont);

		selectFactionButton = new RiskaMenuItem(FACTION_SELECT,
				resources.buttonRegion,
				vbom, "NEXT", resources.mainMenuFont);

		nextFactionButton.setSize(0.2f * camera.getWidth(), 0.2f * camera.getHeight());
		nextFactionButton.setPosition(0.75f * camera.getWidth(), 0.5f * camera.getHeight());

		previousFactionButton.setSize(0.2f * camera.getWidth(), 0.2f * camera.getHeight());
		previousFactionButton.setPosition(0.25f * camera.getWidth(), 0.5f * camera.getHeight());

		selectFactionButton.setSize(0.5f * camera.getWidth(), 0.12f * camera.getHeight());
		selectFactionButton.setPosition(0.5f * camera.getWidth(), 0.06f * camera.getHeight());

		
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

		chooseFactionMenu.addMenuItem(nextFactionButton);
		chooseFactionMenu.addMenuItem(previousFactionButton);
		chooseFactionMenu.addMenuItem(selectFactionButton);

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
			setBackground(new Background(Color.BLACK));
			changeChildSceneTo(startGameMenu);
			break;

		case MAIN_OPTIONS:
			changeChildSceneTo(optionsMenu);
			break;

		case START_NEW:
			resetGameInfo(); // TODO : verify necessity of reseting
			changeChildSceneTo(choosePlayersMenu);
			break;

		case START_LOAD:
			//sceneManager.loadGameScene(engine);
			break;

		case START_RETURN:
			//changeChildSceneTo(mainMenu);
			break;

		case OPTIONS_SFX:
			sfxOn = !sfxOn;
			sliderSFX.setCurrentTileIndex(sfxOn ? 0 : 1);
			break;

		case OPTIONS_MUSIC:
			musicOn = !musicOn;
			sliderMusic.setCurrentTileIndex(musicOn ? 0 : 1);
			break;

		case OPTIONS_RETURN:
			// TODO: implement music conductor
			SharedPreferencesManager.SaveBoolean("musicOn", musicOn);
			SharedPreferencesManager.SaveBoolean("sfxOn", sfxOn);
			changeChildSceneTo(mainMenu);
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
				playerFaction[currentPlayer] = selectedFaction;
				currentPlayer++;
				titleTextChooseFaction.setText("Choose Faction: (Player " + currentPlayer + ")");
				Utils.wrap(titleTextChooseFaction, 0.5f * camera.getWidth(), 0.11f * camera.getHeight(), 0.8f);
				selectFaction(1);
			}

			if(currentPlayer == GameInfo.humanPlayers)
			{
				camera.setHUD(null);
				saveFactionsInfo();
				sceneManager.createGameScene();
			}
			break;

		case PLAYERS_SELECT:
			savePlayersInfo();
			changeChildSceneTo(chooseFactionMenu);
			break;

		default:
			break;
		}

		return false;
	}

	private void changeChildSceneTo(final MenuScene child)
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
