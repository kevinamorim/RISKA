package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import android.graphics.Color;
import android.util.Log;
import feup.lpoo.riska.gameInterface.AnimatedButtonSpriteMenuItem;
import feup.lpoo.riska.gameInterface.AnimatedTextButtonSpriteMenuItem;
import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.io.SharedPreferencesManager;
import feup.lpoo.riska.logic.SceneManager.SCENE_TYPE;
import feup.lpoo.riska.utilities.Utils;

public class MainMenuScene extends BaseScene implements Displayable, IOnMenuItemClickListener {

	// ==================================================
	// FIELDS
	// ==================================================
	private MenuScene mainMenu;
	private MenuScene optionsMenu;
	private MenuScene startGameMenu;
	
	private MenuScene chooseMapMenu;
	private MenuScene chooseFactionMenu;
	private MenuScene chooseDifficultyMenu;

	private enum CHILD { MAIN, OPTIONS, START_GAME, CHOOSE_MAP, CHOOSE_FACTION, CHOOSE_DIFFICULTY};

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
	
	private AnimatedTextButtonSpriteMenuItem startButton;
	private AnimatedButtonSpriteMenuItem optionsButton;
	
	private AnimatedTextButtonSpriteMenuItem newGameButton;
	private AnimatedTextButtonSpriteMenuItem loadGameButton;
	private AnimatedButtonSpriteMenuItem returnButtonStart;
	
	private AnimatedButtonSpriteMenuItem returnButtonOptions;
	private AnimatedButtonSpriteMenuItem sliderSFX;
	private AnimatedButtonSpriteMenuItem sliderMusic;
	private Text textSFX;
	private Text textMusic;
	
	private Text titleTextChooseFaction;
	private AnimatedTextButtonSpriteMenuItem nextFactionButton;
	private AnimatedTextButtonSpriteMenuItem previousFactionButton;
	private AnimatedTextButtonSpriteMenuItem selectFactionButton;
	private Sprite factionBorder;
	private ButtonSprite factionSprite;
	private ButtonSprite factionSpriteCenter;
	
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
		
		createChildScene(CHILD.CHOOSE_MAP);
		createChildScene(CHILD.CHOOSE_FACTION);
		createChildScene(CHILD.CHOOSE_DIFFICULTY);
		
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

		startButton.setPosition(camera.getCenterX(), camera.getCenterY());
		
		optionsButton.setScale(0.3f);
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
		
		
		newGameButton.setPosition(camera.getCenterX(), 0.66f * camera.getHeight());
		
		loadGameButton.setPosition(camera.getCenterX(), 0.33f * camera.getHeight());
		
		returnButtonStart.setScale(0.3f);
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
		textMusic.setPosition( 0.25f * camera.getWidth(), 0.75f * camera.getHeight());
		textMusic.setColor(Color.BLACK);

		textSFX = new Text(0, 0, resources.mainMenuFont, "SFX", vbom);
		textSFX.setPosition(0.25f * camera.getWidth(), 0.50f * camera.getHeight());
		textSFX.setColor(Color.BLACK);

		
		sliderMusic.setScale(0.3f);
		sliderMusic.setPosition(0.75f * camera.getWidth(), textMusic.getY());
		sliderMusic.setCurrentTileIndex(musicOn ? 0 : 1);
		
		sliderSFX.setScale(0.3f);
		sliderSFX.setPosition(0.75f * camera.getWidth(), textSFX.getY());
		sliderSFX.setCurrentTileIndex(sfxOn ? 0 : 1);
		
		returnButtonOptions.setScale(0.3f);
		returnButtonOptions.setPosition(0.5f * Utils.getWidth(returnButtonOptions) - 2, 0.5f * Utils.getHeight(returnButtonOptions) - 2);
		
		
		optionsMenu.addMenuItem(returnButtonOptions);
		optionsMenu.addMenuItem(sliderMusic);
		optionsMenu.addMenuItem(sliderSFX);

		optionsMenu.attachChild(textMusic);
		optionsMenu.attachChild(textSFX);

		optionsMenu.setOnMenuItemClickListener(this);	
	}

	private void createChooseFactionMenu()
	{
		chooseFactionMenu = new MenuScene(camera);
		
		chooseFactionMenu.setBackgroundEnabled(false);
		
		titleTextChooseFaction = new Text(0, 0, resources.mainMenuFont, "CHOOSE YOUR FACTION COLORS", vbom);
		
		Utils.wrapText(titleTextChooseFaction, 0.9f * camera.getWidth(), 0.15f * camera.getHeight(), 1f);
		
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
		
		nextFactionButton.setTextScale(0.5f);
		nextFactionButton.setSize(0.2f * camera.getWidth(), 0.2f * camera.getHeight());
		nextFactionButton.setPosition(0.75f * camera.getWidth(), 0.2f * camera.getHeight());
		
		previousFactionButton.setTextScale(0.5f);
		previousFactionButton.setSize(0.2f* camera.getWidth(), 0.2f * camera.getHeight());
		previousFactionButton.setPosition(0.25f * camera.getWidth(), 0.2f * camera.getHeight());
		
		selectFactionButton.setSize(0.2f* camera.getWidth(), 0.2f * camera.getHeight());
		selectFactionButton.setPosition(0.5f * camera.getWidth(), 0.2f * camera.getHeight());
		
		factionBorder.setSize(0.45f* camera.getHeight(), 0.45f * camera.getHeight());
		factionBorder.setPosition(0.5f * camera.getWidth(), 0.6f * camera.getHeight());
		
		factionSprite.setCurrentTileIndex(0);
		factionSprite.setColor(Color.WHITE);
		factionSprite.setSize(factionBorder.getWidth() - 2, factionBorder.getHeight() - 2);
		factionSprite.setPosition(factionBorder.getX(), factionBorder.getY());
		
		factionSpriteCenter.setCurrentTileIndex(1);
		factionSpriteCenter.setColor(Color.BLACK);
		factionSpriteCenter.setSize(0.8f * factionSprite.getWidth(), 0.8f * factionSprite.getHeight());
		factionSpriteCenter.setPosition(factionSprite.getX(), factionSprite.getY());
		
		chooseFactionMenu.addMenuItem(nextFactionButton);
		chooseFactionMenu.addMenuItem(previousFactionButton);
		chooseFactionMenu.addMenuItem(selectFactionButton);
		
		chooseFactionMenu.attachChild(factionSprite);
		chooseFactionMenu.attachChild(factionSpriteCenter);
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
			setChildScene(chooseFactionMenu);
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
			break;
			
		case FACTION_PREVIOUS:
			break;
			
		case FACTION_SELECT:
			sceneManager.createGameScene();
			break;
			
		default:
			break;
		}

		return false;
	}

}
