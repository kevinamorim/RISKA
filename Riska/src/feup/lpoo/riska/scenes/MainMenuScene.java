package feup.lpoo.riska.scenes;

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
import feup.lpoo.riska.gameInterface.AnimatedButtonSpriteMenuItem;
import feup.lpoo.riska.gameInterface.AnimatedTextButtonSpriteMenuItem;
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
	private AnimatedTextButtonSpriteMenuItem startButton;
	private AnimatedButtonSpriteMenuItem optionsButton;

	// START MENU
	private AnimatedTextButtonSpriteMenuItem newGameButton;
	private AnimatedTextButtonSpriteMenuItem loadGameButton;
	private AnimatedButtonSpriteMenuItem returnButtonStart;

	// OPTIONS MENU
	private AnimatedButtonSpriteMenuItem returnButtonOptions;
	private AnimatedButtonSpriteMenuItem sliderSFX;
	private AnimatedButtonSpriteMenuItem sliderMusic;
	private Text textSFX;
	private Text textMusic;

	// CHOOSE PLAYERS MENU
	private AnimatedTextButtonSpriteMenuItem choosePlayerNextButton;
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
	private AnimatedTextButtonSpriteMenuItem nextFactionButton;
	private AnimatedTextButtonSpriteMenuItem previousFactionButton;
	private AnimatedTextButtonSpriteMenuItem selectFactionButton;
	private Sprite factionBorder;
	private ButtonSprite factionSprite;
	private ButtonSprite factionSpriteCenter;
	private ButtonSprite factionSpriteBorder;

	private int currentPlayer = 0;
	private int selectedFaction = 0;
	private int[] playerFaction;


	// MUSIC OPTIONS
	private boolean musicOn = true;
	private boolean sfxOn = false;


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

			setChildScene(mainMenu);
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

		createChildScene(CHILD.MAIN);
		createChildScene(CHILD.OPTIONS);
		createChildScene(CHILD.START_GAME);

		//createChildScene(CHILD.CHOOSE_MAP);
		createChildScene(CHILD.CHOOSE_PLAYERS);
		createChildScene(CHILD.CHOOSE_FACTION);
		//createChildScene(CHILD.CHOOSE_DIFFICULTY);

		setChildScene(CHILD.MAIN);
	}

	private void createBackground() {

		background = new SpriteBackground(new Sprite(
				camera.getCenterX(), 
				camera.getCenterY(),
				camera.getWidth() + 2,
				camera.getHeight() + 2,
				resources.menuBackgroundRegion, 
				vbom));

		setBackground(background);
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

		startButton = new AnimatedTextButtonSpriteMenuItem(MAIN_START, 
				resources.textBtnRegion.getWidth(), 
				resources.textBtnRegion.getHeight(),
				resources.textBtnRegion, 
				vbom, "START", resources.mainMenuFont);

		optionsButton = new AnimatedButtonSpriteMenuItem(MAIN_OPTIONS, 
				resources.optionsBtnRegion.getWidth(),
				resources.optionsBtnRegion.getHeight(), 
				resources.optionsBtnRegion, 
				vbom);

		Utils.wrap(startButton, 1f * camera.getWidth(), 0.2f * camera.getHeight(), 1f);
		startButton.setPosition(camera.getCenterX(), camera.getCenterY());

		Utils.wrap(optionsButton, 0.2f * camera.getWidth(), 0.2f * camera.getHeight(), 1f);
		optionsButton.setPosition(0.5f * Utils.getWidth(optionsButton) - 2, 0.5f * Utils.getHeight(optionsButton) - 2);

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


		newGameButton = new AnimatedTextButtonSpriteMenuItem(START_NEW,
				resources.textBtnRegion.getWidth(),
				resources.textBtnRegion.getHeight(),
				resources.textBtnRegion,
				vbom, "NEW", resources.mainMenuFont);

		loadGameButton = new AnimatedTextButtonSpriteMenuItem(START_LOAD,
				resources.textBtnRegion.getWidth(),
				resources.textBtnRegion.getHeight(),
				resources.textBtnRegion,
				vbom, "LOAD", resources.mainMenuFont);

		returnButtonStart = new AnimatedButtonSpriteMenuItem(START_RETURN,
				resources.returnBtnRegion.getWidth(),
				resources.returnBtnRegion.getHeight(),
				resources.returnBtnRegion, vbom);


		Utils.wrap(newGameButton, 1f * camera.getWidth(), 0.2f * camera.getHeight(), 1f);
		newGameButton.setPosition(camera.getCenterX(), 0.66f * camera.getHeight());

		Utils.wrap(loadGameButton, 1f * camera.getWidth(), 0.2f * camera.getHeight(), 1f);
		loadGameButton.setPosition(camera.getCenterX(), 0.33f * camera.getHeight());

		Utils.wrap(returnButtonStart, 0.2f * camera.getWidth(), 0.2f * camera.getHeight(), 1f);
		returnButtonStart.setPosition(0.5f * Utils.getWidth(returnButtonStart) - 2, 0.5f * Utils.getHeight(returnButtonStart) - 2);	

		startGameMenu.addMenuItem(newGameButton);
		startGameMenu.addMenuItem(loadGameButton);
		startGameMenu.addMenuItem(returnButtonStart);

		startGameMenu.setOnMenuItemClickListener(this);
	}

	// ==================================================
	// OPTIONS
	// ==================================================
	private void createOptionsMenu()
	{
		optionsMenu = new MenuScene(camera);

		optionsMenu.setBackgroundEnabled(false);


		returnButtonOptions = new AnimatedButtonSpriteMenuItem(OPTIONS_RETURN,
				resources.returnBtnRegion.getWidth(),
				resources.returnBtnRegion.getHeight(),
				resources.returnBtnRegion, vbom);

		sliderSFX= new AnimatedButtonSpriteMenuItem(OPTIONS_SFX,
				resources.sliderBtnRegion.getWidth(),
				resources.sliderBtnRegion.getHeight(),
				resources.sliderBtnRegion, vbom);

		sliderMusic = new AnimatedButtonSpriteMenuItem(OPTIONS_MUSIC,
				resources.sliderBtnRegion.getWidth(),
				resources.sliderBtnRegion.getHeight(),
				resources.sliderBtnRegion, vbom);



		textMusic = new Text(0, 0, resources.mainMenuFont, "MUSIC", vbom);
		Utils.wrap(textMusic, 1f * camera.getWidth(), 0.2f * camera.getHeight(), 0.9f);
		textMusic.setPosition( 0.25f * camera.getWidth(), 0.75f * camera.getHeight());
		textMusic.setColor(Color.BLACK);

		textSFX = new Text(0, 0, resources.mainMenuFont, "SFX", vbom);
		Utils.wrap(textSFX, 1f * camera.getWidth(), 0.2f * camera.getHeight(), 0.9f);
		textSFX.setPosition(0.25f * camera.getWidth(), 0.50f * camera.getHeight());
		textSFX.setColor(Color.BLACK);

		Utils.wrap(sliderMusic, 0.3f * camera.getWidth(), 0.2f * camera.getHeight(), 1f);
		sliderMusic.setPosition(0.75f * camera.getWidth(), textMusic.getY());
		sliderMusic.setCurrentTileIndex(musicOn ? 0 : 1);

		Utils.wrap(sliderSFX, 0.3f * camera.getWidth(), 0.2f * camera.getHeight(), 1f);
		sliderSFX.setPosition(0.75f * camera.getWidth(), textSFX.getY());
		sliderSFX.setCurrentTileIndex(sfxOn ? 0 : 1);

		Utils.wrap(returnButtonOptions, 0.2f * camera.getWidth(), 0.2f * camera.getHeight(), 1f);
		returnButtonOptions.setPosition(0.5f * Utils.getWidth(returnButtonOptions) - 2, 0.5f * Utils.getHeight(returnButtonOptions) - 2);


		optionsMenu.addMenuItem(returnButtonOptions);
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
		Utils.wrap(titleTextChoosePlayers, 0.9f * camera.getWidth(), 0.1f * camera.getHeight(), 0.9f);
		titleTextChoosePlayers.setPosition( 0.5f * camera.getWidth(), 0.90f * camera.getHeight());
		titleTextChoosePlayers.setColor(Color.WHITE);

		choosePlayerNextButton = new AnimatedTextButtonSpriteMenuItem(PLAYERS_SELECT,
				resources.textBtnRegion.getWidth(),
				resources.textBtnRegion.getHeight(),
				resources.textBtnRegion,
				vbom, "Choose Faction", resources.mainMenuFont);

		choosePlayerNextButton.setSize(0.5f * camera.getWidth(), 0.15f * camera.getHeight());
		choosePlayerNextButton.setPosition(0.5f * camera.getWidth(), 0.1f * camera.getHeight());

		choosePlayersMenu.addMenuItem(choosePlayerNextButton);

		choosePlayersMenu.attachChild(titleTextChoosePlayers);
		choosePlayersMenu.attachChild(textIsCPU);

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
					switch(ev.getAction()) 
					{
					case MotionEvent.ACTION_DOWN:
						onButtonPressed(this, getTag(), BUTTON.ADD);
						break;
					case MotionEvent.ACTION_UP:
						onButtonTouched(this, getTag(), BUTTON.ADD);
						break;
					case MotionEvent.ACTION_OUTSIDE:
						onButtonReleased(this, getTag(), BUTTON.ADD);
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
						onButtonTouched(this, getTag(), BUTTON.REMOVE);
						break;
					case MotionEvent.ACTION_OUTSIDE:
						onButtonReleased(this, getTag(), BUTTON.REMOVE);
						break;
					}

					return true;
				}
			};
			removePlayerButton[i].setTag(i);
			registerTouchArea(removePlayerButton[i]);

			playerName[i] = new Text(0, 0, resources.mainMenuFont, "", MAX_NAME_CHARS, vbom);
			playerNameButton[i] = new ButtonSprite(0, 0, resources.textBtnRegion, vbom)
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
						onButtonTouched(this, getTag(), BUTTON.NAME);
						break;
					case MotionEvent.ACTION_OUTSIDE:
						onButtonReleased(this, getTag(), BUTTON.NAME);
						break;
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
						onButtonTouched(this, getTag(), BUTTON.CPU_BOX);
						break;
					case MotionEvent.ACTION_OUTSIDE:
						onButtonReleased(this, getTag(), BUTTON.CPU_BOX);
						break;
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

		textIsCPU = new Text(0, 0, resources.mainMenuFont, "cpu?", vbom);
		Utils.wrap(textIsCPU, 0.2f * camera.getWidth(), buttonsHeight, 0.5f);
		textIsCPU.setPosition(0.85f * camera.getWidth(), (1f - heightFactor) * 0.75f * camera.getHeight() + 0.15f * camera.getHeight());
		textIsCPU.setColor(Color.WHITE);

		// Places all players buttons
		for(int i = 0; i < GameInfo.maxPlayers; i++)
		{	
			float buttonsY = (1f - ((i + 1) * heightFactor)) * 0.75f * camera.getHeight() + 0.15f * camera.getHeight();

			Utils.wrap(addPlayerButton[i], 0.2f * camera.getWidth(), buttonsHeight, 0.9f);
			addPlayerButton[i].setPosition(0.15f * camera.getWidth(), buttonsY);

			Utils.wrap(removePlayerButton[i], 0.2f * camera.getWidth(), buttonsHeight, 0.9f);
			removePlayerButton[i].setPosition(addPlayerButton[i]);

			Utils.wrap(playerNameButton[i], 0.5f * camera.getWidth(), buttonsHeight, 0.9f);
			playerNameButton[i].setPosition(0.5f * camera.getWidth(), buttonsY);

			Utils.wrap(playerName[i], 1f * playerNameButton[i].getWidth(), 0.8f * playerNameButton[i].getHeight(), 0.9f);
			playerName[i].setPosition(Utils.getCenterX(playerNameButton[i]), Utils.getCenterY(playerNameButton[i]));
			playerName[i].setColor(Color.BLACK);
			playerNameButton[i].attachChild(playerName[i]);

			Utils.wrap(cpuCheckBox[i], 1f * camera.getWidth(), buttonsHeight, 1f);
			cpuCheckBox[i].setPosition(0.85f * camera.getWidth(), buttonsY);

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
		buttonSprite.setCurrentTileIndex(0);
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
		Utils.wrap(playerName[tag], 1f * playerNameButton[tag].getWidth(), 0.8f * playerNameButton[tag].getHeight(), 0.9f);
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
			buttonSprite.setCurrentTileIndex(1);
			break;

		case CPU_BOX:
			// Nothing now
			break;

		default:
			break;
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
		setInitialFactionsVariables();

		titleTextChooseFaction = new Text(0, 0, resources.mainMenuFont, "CHOOSE YOUR FACTION COLORS", vbom);

		Utils.wrap(titleTextChooseFaction, 0.9f * camera.getWidth(), 0.15f * camera.getHeight(), 1f);

		titleTextChooseFaction.setPosition( 0.5f * camera.getWidth(), 0.90f * camera.getHeight());
		titleTextChooseFaction.setColor(Color.WHITE);

		nextFactionButton = new AnimatedTextButtonSpriteMenuItem(FACTION_NEXT,
				resources.textBtnRegion.getWidth(),
				resources.textBtnRegion.getHeight(),
				resources.textBtnRegion,
				vbom, ">", resources.mainMenuFont);

		previousFactionButton = new AnimatedTextButtonSpriteMenuItem(FACTION_PREVIOUS,
				resources.textBtnRegion.getWidth(),
				resources.textBtnRegion.getHeight(),
				resources.textBtnRegion,
				vbom, "<", resources.mainMenuFont);

		selectFactionButton = new AnimatedTextButtonSpriteMenuItem(FACTION_SELECT,
				resources.textBtnRegion.getWidth(),
				resources.textBtnRegion.getHeight(),
				resources.textBtnRegion,
				vbom, "SELECT", resources.mainMenuFont);

		factionBorder = new Sprite(
				0,
				0,
				resources.menuBorderRegion.getWidth(),
				resources.menuBorderRegion.getHeight(),
				resources.menuBorderRegion,
				vbom);

		factionSprite = new ButtonSprite(0, 0, resources.factionSpriteRegion, vbom);
		factionSpriteCenter = new ButtonSprite(0, 0, resources.factionSpriteRegion, vbom);
		factionSpriteBorder = new ButtonSprite(0, 0, resources.factionSpriteRegion, vbom);


		nextFactionButton.setTextScale(0.5f);
		nextFactionButton.setSize(0.2f * camera.getWidth(), 0.2f * camera.getHeight());
		nextFactionButton.setPosition(0.75f * camera.getWidth(), 0.2f * camera.getHeight());

		previousFactionButton.setTextScale(0.5f);
		previousFactionButton.setSize(0.2f * camera.getWidth(), 0.2f * camera.getHeight());
		previousFactionButton.setPosition(0.25f * camera.getWidth(), 0.2f * camera.getHeight());

		selectFactionButton.setSize(0.2f * camera.getWidth(), 0.2f * camera.getHeight());
		selectFactionButton.setPosition(0.5f * camera.getWidth(), 0.2f * camera.getHeight());

		factionBorder.setSize(0.45f * camera.getHeight(), 0.45f * camera.getHeight());
		factionBorder.setPosition(0.5f * camera.getWidth(), 0.6f * camera.getHeight());

		factionSprite.setCurrentTileIndex(1);
		factionSprite.setSize(factionBorder.getWidth() - 2, factionBorder.getHeight() - 2);
		factionSprite.setPosition(factionBorder.getX(), factionBorder.getY());

		factionSpriteCenter.setCurrentTileIndex(2);
		factionSpriteCenter.setSize(0.8f * factionSprite.getWidth(), 0.8f * factionSprite.getHeight());
		factionSpriteCenter.setPosition(factionSprite.getX(), factionSprite.getY());

		updateFactionVisual();

		factionSpriteBorder.setCurrentTileIndex(0);
		factionSpriteBorder.setSize(0.9f * factionSprite.getWidth(), 0.9f * factionSprite.getHeight());
		factionSpriteBorder.setPosition(factionSprite.getX(), factionSprite.getY());

		chooseFactionMenu.addMenuItem(nextFactionButton);
		chooseFactionMenu.addMenuItem(previousFactionButton);
		chooseFactionMenu.addMenuItem(selectFactionButton);

		chooseFactionMenu.attachChild(factionSprite);
		chooseFactionMenu.attachChild(factionSpriteCenter);
		chooseFactionMenu.attachChild(factionSpriteBorder);
		chooseFactionMenu.attachChild(factionBorder);

		chooseFactionMenu.attachChild(titleTextChooseFaction);

		chooseFactionMenu.setOnMenuItemClickListener(this);
	}

	private void setInitialFactionsVariables()
	{
		Utils.fill(playerFaction, -1);
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

			}while(playerFaction[index] != -1);
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

			}while(playerFaction[index] != -1);
		}

		selectedFaction = index;
		updateFactionVisual();
	}

	private void updateFactionVisual()
	{
		factionSprite.setColor(GameInfo.getPriColor(selectedFaction));
		factionSpriteCenter.setColor(GameInfo.getSecColor(selectedFaction));
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
			setChildScene(startGameMenu);
			break;

		case MAIN_OPTIONS:
			setChildScene(optionsMenu);
			break;

		case START_NEW:
			setChildScene(choosePlayersMenu);
			break;

		case START_LOAD:
			//sceneManager.loadGameScene(engine);
			break;

		case START_RETURN:
			setChildScene(mainMenu);
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
			setChildScene(mainMenu);
			break;

		case FACTION_NEXT:
			selectFaction(1);			
			break;

		case FACTION_PREVIOUS:
			selectFaction(-1);
			break;

		case FACTION_SELECT:
			if(currentPlayer < GameInfo.humanPlayers - 1)
			{
				playerFaction[selectedFaction] = currentPlayer;
				currentPlayer++;
				selectFaction(1);
			}
			else
			{
				saveFactionsInfo();
				sceneManager.createGameScene();
			}
			break;

		case PLAYERS_SELECT:
			savePlayersInfo();
			setChildScene(chooseFactionMenu);
			break;

		default:
			break;
		}

		return false;
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

		Log.d("Riska", "Saving players info...");
		Log.d("Riska", "Number of players: " + GameInfo.numberOfPlayers);
		Log.d("Riska", "HUMAN players:     " + GameInfo.humanPlayers);
		Log.d("Riska", "CPU players:     " + GameInfo.cpuPlayers);
	}

	private void saveFactionsInfo()
	{
		for(int i = 0; i < playerFaction.length; i++)
		{
			if(playerFaction[i] != -1)
			{
				GameInfo.assignPlayerFaction(playerFaction[i], i);
			}	
		}

		GameInfo.assignRemainingFactions(currentPlayer);
	}

	private void resetGameInfo()
	{
		setInitialPlayersVariables();	
		setInitialFactionsVariables();
	}
}
