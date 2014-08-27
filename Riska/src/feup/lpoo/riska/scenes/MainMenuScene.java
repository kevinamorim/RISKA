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
	private enum CHILD { MAIN, OPTIONS, START_GAME, NEW_GAME, CHOOSE_MAP, CHOOSE_PLAYERS, CHOOSE_FACTION, CHOOSE_DIFFICULTY, ANY};

	private enum PLAYERS_BUTTON { NAME, ACTIVE, CPU_BOX, COLORS };

	private enum OPTIONS_TAB { ANIMATIONS, AUDIO, GRAPHICS};
	private enum OPTIONS_BUTTON { SFX, MUSIC, MENU_ANIMATIONS, GRAPHICS};

	private enum NEWGAME_TAB { MAP, PLAYERS, LEVEL };

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
	private boolean[] playerIsCPU;
	private boolean[] playerActive;
	private boolean[] playerActivable;
	private boolean[] playerEditable;

	// CHOOSE FACTION MENU
	private Text titleTextChooseFaction;
	private RiskaMenuItem chooseFactionSelectButton;

	private ButtonSprite factionPriColor;
	private ButtonSprite factionSecColor;

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
		
		playerIsCPU = new boolean[GameOptions.maxPlayers];
		playerActive = new boolean[GameOptions.maxPlayers];
		playerActivable = new boolean[GameOptions.maxPlayers];
		playerEditable = new boolean[GameOptions.maxPlayers];

		createMapCanvas();
		createPlayersCanvas();
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
		//int maxRows = 2;
		int maxRows = numberOfMaps / maxCols + (numberOfMaps % maxCols > 0 ? 1 : 0 );

		RiskaCanvas[] mapCanvas = new RiskaCanvas[numberOfMaps];
		final Sprite[] cover = new Sprite[numberOfMaps];

		float heightFactor = 1f / (maxRows + 1);
		float widthFactor = 1f / (maxCols + 1);

		frame = new Sprite(0, 0, resources.frameRegion, vbom);

		menuNewGameMapCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), 0.8f * camera.getWidth(), 0.65f * camera.getHeight());

		for(int i = 0; i < numberOfMaps; i++)
		{
			final int index = i;
			
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
					Log.d("Riska", "Touched Canvas");
					switch(ev.getMotionEvent().getActionMasked()) 
					{

					case MotionEvent.ACTION_DOWN:
						break;

					case MotionEvent.ACTION_OUTSIDE:
						break;

					case MotionEvent.ACTION_UP:
						onMapSelected(getTag(), cover[index]);
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

			cover[i] = new Sprite(0, 0, resources.coveredSmallFrameRegion, vbom);
			if(i > 0)
			{
				cover[i].setAlpha(0.8f);
			}
			else
			{
				cover[i].setAlpha(0f);
			}
			mapCanvas[i].addGraphic(cover[i], 0.5f, 0.5f, 1f, 1f);

			menuNewGameMapCanvas.addGraphic(mapCanvas[i], x, y, 0.9f * widthFactor, 0.9f * heightFactor);
			
			menuNewGame.registerTouchArea(mapCanvas[i]);
		}

		menuNewGameMapCanvas.addGraphic(frame, 0.5f, 0.5f, 1f, 1f);

		menuNewGameMapCanvas.setVisible(true);

		menuNewGame.attachChild(menuNewGameMapCanvas);
	}

	private void createPlayersCanvas()
	{
		Sprite frame;
		frame = new Sprite(0, 0, resources.frameRegion, vbom);

		menuNewGamePlayersCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), 0.8f * camera.getWidth(), 0.65f * camera.getHeight());

		int maxPlayers = GameOptions.maxPlayers;

		RiskaCanvas[] playerCanvas = new RiskaCanvas[maxPlayers];
		RiskaSprite[] playerName = new RiskaSprite[maxPlayers];
		ButtonSprite[] playerActiveButton = new ButtonSprite[maxPlayers];
		ButtonSprite[] playerCpuButton = new ButtonSprite[maxPlayers];
		RiskaCanvas[] playerColor = new RiskaCanvas[maxPlayers];

		setPlayersVariables();

		float heightFactor = 1f / (maxPlayers + 2);

		int index = maxPlayers + 1;

		for(int i = 0; i < maxPlayers; i++)
		{
			playerCanvas[i] = new RiskaCanvas(0f, 0f,
					0.9f * menuNewGamePlayersCanvas.getWidth(),
					1f * heightFactor * menuNewGamePlayersCanvas.getHeight());

			playerName[i] = new RiskaSprite(resources.emptyButtonRegion, vbom, "", resources.mainMenuFont,
					null, null, resources.barHRegion, resources.barHRegion)
			{
				@Override
				public boolean onAreaTouched(TouchEvent ev, float pX, float pY) 
				{
					switch(ev.getAction()) 
					{

					case MotionEvent.ACTION_DOWN:
						onNamePressed(this, getTag(), PLAYERS_BUTTON.NAME);
						break;

					case MotionEvent.ACTION_OUTSIDE:
						onNameReleased(this, getTag(), PLAYERS_BUTTON.NAME);
						break;

					case MotionEvent.ACTION_UP:
						onNameTouched(this, getTag(), PLAYERS_BUTTON.NAME);
						break;

					default:
						break;
					}

					return true;
				}
			};
			playerName[i].setTag(i);
			playerName[i].setText("Player" + (i + 1));
			
			playerCanvas[i].addGraphic(playerName[i], 0.2f, 0.5f, 0.4f, 1f);

			playerActiveButton[i] = new ButtonSprite(0, 0, resources.switchRegion, vbom) {

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
						onPlayersButtonTouched(this, getTag(), PLAYERS_BUTTON.ACTIVE);
						break;
					}

					return true;
				}

			};
			playerActiveButton[i].setTag(i);
			playerActiveButton[i].setCurrentTileIndex(playerActive[i] ? 0 : 1);
			
			playerCanvas[i].addGraphicWrap(playerActiveButton[i], 0.55f, 0.5f, 0.1f, 1f);
			
			playerCpuButton[i] = new ButtonSprite(0, 0, resources.switchRegion, vbom) {

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
						onPlayersButtonTouched(this, getTag(), PLAYERS_BUTTON.CPU_BOX);
						break;
					}

					return true;
				}

			};
			playerCpuButton[i].setTag(i);
			playerCpuButton[i].setCurrentTileIndex(playerIsCPU[i] ? 0 : 1);
			
			playerCanvas[i].addGraphicWrap(playerCpuButton[i], 0.75f, 0.5f, 0.1f, 1f);
			
			playerColor[i] = new RiskaCanvas(0f, 0f, 1f, 1f);
			playerCanvas[i].addGraphicWrap(playerColor[i], 0.95f, 0.5f, 0.1f, 1f);
			
			menuNewGamePlayersCanvas.addGraphic(playerCanvas[i], 0.5f, index * heightFactor, 1f, heightFactor);
			index--;
			
			menuNewGame.registerTouchArea(playerName[i]);
			menuNewGame.registerTouchArea(playerActiveButton[i]);
			menuNewGame.registerTouchArea(playerCpuButton[i]);
		}

		menuNewGamePlayersCanvas.addGraphic(frame, 0.5f, 0.5f, 1f, 1f);

		menuNewGamePlayersCanvas.setVisible(false);

		menuNewGame.attachChild(menuNewGamePlayersCanvas);
	}

	private void createLevelCanvas()
	{
		menuNewGameLevelCanvas = new RiskaCanvas(camera.getCenterX(), camera.getCenterY(), 0.8f * camera.getWidth(), 0.65f * camera.getHeight());
	}

	private void createNewGameTabs()
	{
		int numberOfMenus = 4;
		float factor = 1f / (numberOfMenus + 1);

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

		menuNewGameMapTab.setSize(factor * menuNewGameMapCanvas.getWidth(), 0.1f * camera.getHeight());
		menuNewGameMapTab.setPosition(Utils.left(menuNewGameMapCanvas) + 0.5f * factor * menuNewGameMapCanvas.getWidth(),
				Utils.bottom(menuNewGameMapCanvas) - 1f * Utils.halfY(menuNewGameMapTab));

		menuNewGamePlayersTab.setSize(menuNewGameMapTab.getWidth(), menuNewGameMapTab.getHeight());
		menuNewGamePlayersTab.setPosition(Utils.right(menuNewGameMapTab) + Utils.halfX(menuNewGamePlayersTab), menuNewGameMapTab.getY());

		menuNewGameLevelTab.setSize(menuNewGameMapTab.getWidth(), menuNewGameMapTab.getHeight());
		menuNewGameLevelTab.setPosition(Utils.right(menuNewGamePlayersTab) + Utils.halfX(menuNewGameLevelTab), menuNewGameMapTab.getY());

		menuNewGame.attachChild(menuNewGameMapTab);
		menuNewGame.attachChild(menuNewGamePlayersTab);
		menuNewGame.attachChild(menuNewGameLevelTab);

		menuNewGame.registerTouchArea(menuNewGameMapTab);
		menuNewGame.registerTouchArea(menuNewGamePlayersTab);
		menuNewGame.registerTouchArea(menuNewGameLevelTab);

		// Map tab selected
		menuNewGameMapTab.setColor(Utils.OtherColors.WHITE);
		menuNewGamePlayersTab.setColor(Utils.OtherColors.DARK_GREY);
		menuNewGameLevelTab.setColor(Utils.OtherColors.DARK_GREY);
	}

	private void onNewGameTabSelected(NEWGAME_TAB x)
	{
		switch(x)
		{

		case MAP:
			menuNewGameMapTab.setColor(Utils.OtherColors.WHITE);
			menuNewGamePlayersTab.setColor(Utils.OtherColors.DARK_GREY);
			menuNewGameLevelTab.setColor(Utils.OtherColors.DARK_GREY);

			menuNewGameMapCanvas.setVisible(true);
			menuNewGamePlayersCanvas.setVisible(false);
			menuNewGameLevelCanvas.setVisible(false);
			break;

		case PLAYERS:
			menuNewGameMapTab.setColor(Utils.OtherColors.DARK_GREY);
			menuNewGamePlayersTab.setColor(Utils.OtherColors.WHITE);
			menuNewGameLevelTab.setColor(Utils.OtherColors.DARK_GREY);

			menuNewGameMapCanvas.setVisible(false);
			menuNewGamePlayersCanvas.setVisible(true);
			menuNewGameLevelCanvas.setVisible(false);
			break;

		case LEVEL:
			menuNewGameMapTab.setColor(Utils.OtherColors.DARK_GREY);
			menuNewGamePlayersTab.setColor(Utils.OtherColors.DARK_GREY);
			menuNewGameLevelTab.setColor(Utils.OtherColors.WHITE);

			menuNewGameMapCanvas.setVisible(false);
			menuNewGamePlayersCanvas.setVisible(false);
			menuNewGameLevelCanvas.setVisible(true);
			break;

		default:
			break;
		}
	}

	private void onMapSelected(int index, Sprite x)
	{
		for(int i = 0; i < GameOptions.numberOfMaps; i++)
		{
			if(i != index)
			{
				x.setAlpha(0.8f);
			}
			else
			{
				x.setAlpha(0f);
			}
		}
	}

	private void onNameReleased(RiskaSprite pSprite, int tag, PLAYERS_BUTTON x)
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

	private void onNamePressed(RiskaSprite pSprite, int tag, PLAYERS_BUTTON x)
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

	private void onNameTouched(RiskaSprite pSprite, int tag, PLAYERS_BUTTON x)
	{
		switch(x)
		{

		case NAME:
			// TODO : edit text
			onNameReleased(pSprite, tag, x);
			break;

		default:
			break;
		}	
	}

	private void onPlayersButtonTouched(ButtonSprite pSprite, int tag, PLAYERS_BUTTON x)
	{
		switch(x)
		{

		case ACTIVE:
			if(playerActivable[tag])
			{
				if(playerActive[tag])
				{
					playerActive[tag] = false;
					pSprite.setCurrentTileIndex(1);
				}
				else
				{
					playerActive[tag] = true;
					pSprite.setCurrentTileIndex(0);
				}
			}
			break;

		case CPU_BOX:
			if(playerEditable[tag])
			{
				if(playerIsCPU[tag])
				{
					playerIsCPU[tag] = false;
					pSprite.setCurrentTileIndex(1);
				}
				else
				{
					playerIsCPU[tag] = true;
					pSprite.setCurrentTileIndex(0);
				}
			}
			break;

		default:
			break;
		}	
	}

	private void setPlayersVariables()
	{
		for(int i = 0; i < GameOptions.maxPlayers; i++)
		{
			playerIsCPU[i] = (i < GameOptions.minHumanPlayers) ? false : true;
			playerActive[i] = (i < GameOptions.minPlayers) ? true : false;
			playerActivable[i] = (i < GameOptions.minPlayers) ? false : true;
			playerEditable[i] = (i < GameOptions.minHumanPlayers) ? false : true;
		}
	}
	// ==================================================
	// CHOOSE FACTION
	// ==================================================	

	// ==================================================
	// FACTION
	// ==================================================
	private void resetFactionsVariables()
	{
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
		factionPriColor.setColor(GameOptions.getPriColor(selectedFaction));
		factionSecColor.setColor(GameOptions.getSecColor(selectedFaction));
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

		for(int i = 0; i < GameOptions.maxPlayers; i++)
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
		setPlayersVariables();	
		resetFactionsVariables();
	}

}
