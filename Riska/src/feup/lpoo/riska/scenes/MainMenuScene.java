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
	private ButtonSprite[] addPlayerButton;
	private ButtonSprite[] removePlayerButton;
	private ButtonSprite[] playerName;
	private ButtonSprite[] playerCPU;
	private boolean[] playerActive;

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


	// Music options
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

		Utils.wrap(startButton, 0.4f * camera.getWidth(), 0.4f * camera.getHeight(), 1f);
		startButton.setPosition(camera.getCenterX(), camera.getCenterY());

		Utils.wrap(optionsButton, 0.2f * camera.getWidth(), 0.2f * camera.getHeight(), 1f);
		optionsButton.setPosition(0.5f * Utils.getWidth(optionsButton) - 2, 0.5f * Utils.getHeight(optionsButton) - 2);

		mainMenu.addMenuItem(startButton);
		mainMenu.addMenuItem(optionsButton);
		mainMenu.setOnMenuItemClickListener(this);	
	}

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


		Utils.wrap(newGameButton, 0.4f * camera.getWidth(), 0.4f * camera.getHeight(), 1f);
		newGameButton.setPosition(camera.getCenterX(), 0.66f * camera.getHeight());

		Utils.wrap(loadGameButton, 0.4f * camera.getWidth(), 0.4f * camera.getHeight(), 1f);
		loadGameButton.setPosition(camera.getCenterX(), 0.33f * camera.getHeight());

		Utils.wrap(returnButtonStart, 0.2f * camera.getWidth(), 0.2f * camera.getHeight(), 1f);
		returnButtonStart.setPosition(0.5f * Utils.getWidth(returnButtonStart) - 2, 0.5f * Utils.getHeight(returnButtonStart) - 2);	

		startGameMenu.addMenuItem(newGameButton);
		startGameMenu.addMenuItem(loadGameButton);
		startGameMenu.addMenuItem(returnButtonStart);

		startGameMenu.setOnMenuItemClickListener(this);
	}

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

	private void createChoosePlayersMenu()
	{
		choosePlayersMenu = new MenuScene(camera);
		
		choosePlayersMenu.setBackgroundEnabled(false);
		
		addPlayerButton = new ButtonSprite[GameInfo.maxPlayers];
		removePlayerButton = new ButtonSprite[GameInfo.maxPlayers];
		playerName = new ButtonSprite[GameInfo.maxPlayers];
		playerCPU = new ButtonSprite[GameInfo.maxPlayers];
		playerActive = new boolean[GameInfo.maxPlayers];

		createPlayerSpots();
		
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
						onAddButtonPressed(this, getTag());
						break;
					case MotionEvent.ACTION_UP:
						onAddButtonTouched(this, getTag());
						break;
					case MotionEvent.ACTION_OUTSIDE:
						onAddButtonReleased(this, getTag());
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
						onRemoveButtonPressed(this, getTag());
						break;
					case MotionEvent.ACTION_UP:
						onRemovedButtonTouched(this, getTag());
						break;
					case MotionEvent.ACTION_OUTSIDE:
						onRemoveButtonReleased(this, getTag());
						break;
					}

					return true;
				}
			};
			removePlayerButton[i].setTag(i);
			registerTouchArea(removePlayerButton[i]);
			
			playerName[i] = new ButtonSprite(0, 0, resources.textBtnRegion, vbom);

			GameInfo.playerIsCPU[i] = (i < GameInfo.minHumanPlayers) ? false : true;
			playerActive[i] = (i < GameInfo.minPlayers) ? true : false;

			playerCPU[i] = new ButtonSprite(0, 0, resources.playerCheckBoxButtonRegion, vbom);

			//addPlayerButton[i].setVisible((i < GameInfo.minPlayers) ? false : true);
			//removePlayerButton[i].setVisible(false);
			addPlayerButton[i].setVisible(true);
		}
		
		// Places all players buttons
		for(int i = 0; i < GameInfo.maxPlayers; i++)
		{
			float heightFactor = 1f / (GameInfo.maxPlayers + 1);
			
			Utils.wrap(addPlayerButton[i], 0.2f * camera.getWidth(), heightFactor * camera.getHeight(), 0.9f);
			addPlayerButton[i].setPosition(0.15f * camera.getWidth(), (1f - ((i + 1) * heightFactor)) * camera.getHeight());
			
			//Utils.wrap(removePlayerButton[i], 0.2f * camera.getWidth(), heightFactor * camera.getHeight(), 0.9f);
			removePlayerButton[i].setSize(addPlayerButton[i].getWidth(), addPlayerButton[i].getHeight());
			removePlayerButton[i].setPosition(addPlayerButton[i].getX(), addPlayerButton[i].getY());
			
			Utils.wrap(playerName[i], 0.5f * camera.getWidth(), heightFactor * camera.getHeight(), 0.9f);
			//playerName[i].setSize(0.3f * camera.getWidth(), heightFactor * camera.getHeight());
			playerName[i].setPosition(0.5f * camera.getWidth(), (1f - ((i + 1) * heightFactor)) * camera.getHeight());
			
			Utils.wrap(playerCPU[i], 1f * camera.getWidth(), heightFactor * camera.getHeight(), 0.9f);
			playerCPU[i].setPosition(0.85f * camera.getWidth(), (1f - ((i + 1) * heightFactor)) * camera.getHeight());
			
			choosePlayersMenu.attachChild(playerName[i]);
			choosePlayersMenu.attachChild(addPlayerButton[i]);
			choosePlayersMenu.attachChild(playerCPU[i]);
		}
	}


	protected void onRemoveButtonReleased(ButtonSprite buttonSprite, int tag)
	{
		buttonSprite.setCurrentTileIndex(0);
	}
	
	protected void onRemovedButtonTouched(ButtonSprite buttonSprite, int tag)
	{
		onRemoveButtonReleased(buttonSprite, tag);
		
	}

	protected void onRemoveButtonPressed(ButtonSprite buttonSprite, int tag)
	{
		buttonSprite.setCurrentTileIndex(1);
	}

	
	protected void onAddButtonReleased(ButtonSprite buttonSprite, int tag)
	{
		buttonSprite.setCurrentTileIndex(0);
	}

	protected void onAddButtonTouched(ButtonSprite buttonSprite, int tag)
	{
		onAddButtonReleased(buttonSprite, tag);
	}

	protected void onAddButtonPressed(ButtonSprite buttonSprite, int tag)
	{
		buttonSprite.setCurrentTileIndex(1);
	}

	
	private void createChooseFactionMenu()
	{
		chooseFactionMenu = new MenuScene(camera);

		chooseFactionMenu.setBackgroundEnabled(false);

		GameInfo.clearFactions();

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

		updateFaction();

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
			updateFaction();
			break;

		case FACTION_PREVIOUS:
			selectFaction(-1);
			updateFaction();
			break;

		case FACTION_SELECT:
			nextScreen();
			break;

		default:
			break;
		}

		return false;
	}

	private void selectFaction(int direction)
	{
		if(direction > 0)
		{
			selectedFaction += 1;
			selectedFaction = selectedFaction % GameInfo.getNumberOfColors();
		}

		if(direction < 0)
		{
			if(selectedFaction - 1 < 0)
			{
				selectedFaction = GameInfo.getNumberOfColors() - 1;
			}
			else
			{
				selectedFaction -= 1;
			}
		}
	}

	private void updateFaction()
	{
		factionSprite.setColor(GameInfo.getPriColor(selectedFaction));
		factionSpriteCenter.setColor(GameInfo.getSecColor(selectedFaction));
	}

	private void nextScreen()
	{
		// TODO : right now next screen is the game screen, but it could be any other
		GameInfo.assignPlayerFaction(currentPlayer, selectedFaction);

		//if(currentPlayer < Info.numberOfPlayers - 1)
		//{
		//	currentPlayer++;
		//}
		//else

		GameInfo.assignRemainingFactions(currentPlayer);
		sceneManager.createGameScene();
	}

}
