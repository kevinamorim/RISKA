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

import android.view.MotionEvent;
import feup.lpoo.riska.gameInterface.RiskaButtonSprite;
import feup.lpoo.riska.gameInterface.RiskaSprite;
import feup.lpoo.riska.gameInterface.RiskaCanvas;
import feup.lpoo.riska.gameInterface.RiskaMenuItem;
import feup.lpoo.riska.hud.MenuHUD;
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
	private enum CHILD { MAIN, OPTIONS, START, NEW, GO, NONE };

	private enum OPTIONS_BUTTON { SFX, MUSIC, MENU_ANIMATIONS, GAME_ANIMATIONS };

	private enum MENU_BUTTON {OPTIONS, START };

	private enum START_BUTTON {NEW, LOAD };

	private enum NEWGAME_TAB { MAP, PLAYERS, LEVEL, GO, NONE };

	private static float animationTime = GameOptions.animationTime;

	private CHILD currentChild;
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

	private final int NEW_GO = 4;

	private SpriteBackground background;

	// --------------------------
	// MAIN MENU
	private RiskaSprite menuMainRiskaTitle;
	//private RiskaSprite menuMainCreditsTitle;
	private RiskaSprite menuMainStartButton;
	private RiskaSprite menuMainOptionsButton;

	// --------------------------
	// START MENU
	private RiskaSprite startTitle;
	private RiskaSprite menuStartNewButton;
	private RiskaSprite menuStartLoadButton;

	// --------------------------
	// OPTIONS MENU
	private RiskaSprite optionsTitle;
	private RiskaButtonSprite switchAnimations;
	private RiskaButtonSprite switchAudioSfx;
	private RiskaButtonSprite switchAudioMusic;
	private RiskaSprite textAnimations;
	private RiskaSprite textAudioSfx;
	private RiskaSprite textAudioMusic;

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

	private int levelIndex = GameOptions.defaultLvlIndex;
	private int mapIndex = GameOptions.defaultMapIndex;

	// ==================================================
	// ==================================================

	@Override
	public void createScene()
	{
		createDisplay();

		changeChildScene(CHILD.NONE, CHILD.MAIN);
		currentNewGameTab = NEWGAME_TAB.NONE;
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
	public void onMenuKeyPressed()
	{
		// TODO Auto-generated method stub
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
		createNewGameMenu();
	}

	private void createBackground()
	{
		background = new SpriteBackground(new Sprite(
				camera.getCenterX(), 
				camera.getCenterY(),
				camera.getWidth(),
				camera.getHeight(),
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

		menuMainRiskaTitle = new RiskaSprite(resources.riskaTitle, vbom);
		Utils.wrap(menuMainRiskaTitle, camera.getWidth(), 0.33f * camera.getHeight(), 1f);
		menuMainRiskaTitle.setPosition(0.5f * camera.getWidth(), 0.78f * camera.getHeight());

		menuMainStartButton = new RiskaSprite(resources.startTitle, vbom)
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
					if(Utils.contains(this, pX, pY))
					{
						onMenuButtonTouched(MENU_BUTTON.START);		
					}
					break;
				}

				return true;
			}
		};

		Utils.wrap(menuMainStartButton, 0.5f * camera.getWidth(), 0.5f * camera.getHeight(), 1f);
		menuMainStartButton.setPosition(0.72f * camera.getWidth(), 0.35f * camera.getHeight());

		menuMainOptionsButton = new RiskaSprite(resources.optionsTitle, vbom)
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
					if(Utils.contains(this, pX, pY))
					{
						onMenuButtonTouched(MENU_BUTTON.OPTIONS);		
					}
					break;
				}

				return true;
			}
		};

		Utils.wrap(menuMainOptionsButton, 0.5f * camera.getWidth(), 0.5f * camera.getHeight(), 1f);
		menuMainOptionsButton.setPosition(0.28f * camera.getWidth(), 0.35f * camera.getHeight());

		menuMain.attachChild(menuMainRiskaTitle);
		menuMain.attachChild(menuMainStartButton);
		menuMain.attachChild(menuMainOptionsButton);

		menuMain.setOnMenuItemClickListener(this);
		menuMain.setTouchAreaBindingOnActionDownEnabled(true);
		//menuMain.setTouchAreaBindingOnActionMoveEnabled(true);

		menuMainRiskaTitle.fadeOut(0f);
		menuMainStartButton.fadeOut(0f);
		menuMainOptionsButton.fadeOut(0f);
	}

	private void onMenuButtonTouched(MENU_BUTTON x)
	{
		switch(x)
		{

		case START:
			changeChildScene(CHILD.MAIN, CHILD.START);	
			break;

		case OPTIONS:
			changeChildScene(CHILD.MAIN, CHILD.OPTIONS);
			break;

		default:
			break;
		}
	}

	// ==================================================
	// START MENU
	// ==================================================
	private void createStartGameMenu()
	{
		float textBoundingFactor = 0.8f;

		menuStart = new MenuScene(camera);
		menuStart.setBackgroundEnabled(false);

		menuStartNewButton = new RiskaSprite(resources.labelRegion, vbom, "New", resources.mMenuFont)
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
					if(Utils.contains(this, pX, pY))
					{
						onStartButtonTouched(START_BUTTON.NEW);		
					}
					break;
				}

				return true;
			}
		};
		//Utils.wrap(menuStartNewButton, 0.45f * camera.getHeight(), 0.45f * camera.getHeight(), 1f);
		menuStartNewButton.setSize(0.5f * camera.getWidth(), 0.25f * camera.getHeight());
		menuStartNewButton.setPosition(0.5f * camera.getWidth(), 0.5f * camera.getHeight());
		menuStartNewButton.setTextBoundingFactor(textBoundingFactor);

		menuStartLoadButton = new RiskaSprite(resources.labelLargeRegion, vbom, "Load", resources.mMenuFont)
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
					if(Utils.contains(this, pX, pY))
					{
						onStartButtonTouched(START_BUTTON.LOAD);		
					}
					break;
				}

				return true;
			}
		};
		//Utils.wrap(menuStartLoadButton, 0.4f * camera.getWidth(), 0.25f * camera.getHeight(), 1f);
		menuStartLoadButton.setSize(menuStartNewButton.getWidth(), menuStartNewButton.getHeight());
		menuStartLoadButton.setPosition(menuStartNewButton.getX(), 0.22f * camera.getHeight());
		menuStartLoadButton.setTextBoundingFactor(textBoundingFactor);

		menuStart.attachChild(menuStartNewButton);
		menuStart.attachChild(menuStartLoadButton);

		menuStart.setOnMenuItemClickListener(this);
		menuStart.setTouchAreaBindingOnActionDownEnabled(true);
		//menuStart.setTouchAreaBindingOnActionMoveEnabled(true);

		menuStartNewButton.fadeOut(0f);
		menuStartLoadButton.fadeOut(0f);

		startTitle = new RiskaSprite(resources.labelLargeRegion, vbom, "Start", resources.mMenuFont);
		startTitle.setTextColor(Color.BLACK);
		startTitle.setSize(0.6f * camera.getWidth(), 0.25f * camera.getHeight());
		startTitle.setTextBoundingFactor(0.9f);
		startTitle.setPosition(camera.getCenterX(), 0.85f * camera.getHeight());
		startTitle.fadeOut(0f);

		menuStart.attachChild(startTitle);
	}

	private void onStartButtonTouched(START_BUTTON x)
	{
		switch(x)
		{

		case NEW:
			resetGameInfo();
			changeChildScene(CHILD.START, CHILD.NEW);
			break;

		case LOAD:
			// TODO
			break;

			//		case GO:
			//			onNewGameTabSelected(NEWGAME_TAB.GO);
			//			saveGameInfo();
			//			changeSceneToGame();
			//			break;

		default:
			break;
		}
	}

	// ==================================================
	// OPTIONS MENU
	// ==================================================
	// ==================================================
	// OPTIONS
	// ==================================================
	private void createOptionsMenu()
	{
		menuOptions = new MenuScene(camera);
		menuOptions.setBackgroundEnabled(false);

		createOptions();

		menuOptions.setTouchAreaBindingOnActionDownEnabled(true);
		//menuOptions.setTouchAreaBindingOnActionMoveEnabled(true);
	}

	private void createOptions()
	{
		float textBoundingFactor = 0.7f;

		switchAudioMusic = new RiskaButtonSprite(resources.checkBoxRegion, vbom)
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
		switchAudioMusic.setSize(0.15f * camera.getHeight(), 0.15f * camera.getHeight());
		switchAudioMusic.setPosition(0.5f * camera.getWidth(), 0.60f * camera.getHeight());
		switchAudioMusic.setCurrentTileIndex(0);

		textAudioMusic = new RiskaSprite(resources.labelRegion, vbom, "Music", resources.mMenuFont)
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
		textAudioMusic.setSize(0.4f * camera.getWidth(), 0.2f * camera.getHeight());
		textAudioMusic.setPosition(0.5f * camera.getWidth(), switchAudioMusic.getY());	
		textAudioMusic.setTextBoundingFactor(textBoundingFactor);
		textAudioMusic.setTextColor(GameOptions.musicEnabled() ? Color.WHITE : Utils.OtherColors.DEACTIVATED_TEXT);
		textAudioMusic.setColor(GameOptions.musicEnabled() ? Color.WHITE : Utils.OtherColors.DEACTIVATED);

		switchAudioSfx = new RiskaButtonSprite(resources.checkBoxRegion, vbom)
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
		switchAudioSfx.setSize(switchAudioMusic.getHeight(), switchAudioMusic.getHeight());
		switchAudioSfx.setPosition(switchAudioMusic.getX(), 0.40f * camera.getHeight());
		switchAudioSfx.setCurrentTileIndex(0);	

		textAudioSfx = new RiskaSprite(resources.labelLargeRegion, vbom, "Sound Effects", resources.mMenuFont)
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
		textAudioSfx.setTextColor(GameOptions.sfxEnabled() ? Color.WHITE : Utils.OtherColors.DEACTIVATED_TEXT);
		textAudioSfx.setColor(GameOptions.sfxEnabled() ? Color.WHITE : Utils.OtherColors.DEACTIVATED);	
		textAudioSfx.setSize(textAudioMusic.getWidth(), textAudioMusic.getHeight());
		textAudioSfx.setTextBoundingFactor(textBoundingFactor);
		textAudioSfx.setPosition(textAudioMusic.getX(), switchAudioSfx.getY());

		switchAnimations = new RiskaButtonSprite(resources.checkBoxRegion, vbom)
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
		switchAnimations.setSize(switchAudioMusic.getHeight(), switchAudioMusic.getHeight());
		switchAnimations.setPosition(switchAudioMusic.getX(), 0.20f * camera.getHeight());
		switchAnimations.setCurrentTileIndex(0);

		textAnimations = new RiskaSprite(resources.labelRegion, vbom, "Animations", resources.mMenuFont)
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
		textAnimations.setTextColor(GameOptions.animationsEnabled() ? Color.WHITE : Utils.OtherColors.DEACTIVATED_TEXT);
		textAnimations.setColor(GameOptions.animationsEnabled() ? Color.WHITE : Utils.OtherColors.DEACTIVATED);
		textAnimations.setSize(textAudioMusic.getWidth(), textAudioMusic.getHeight());
		textAnimations.setTextBoundingFactor(textBoundingFactor);
		textAnimations.setPosition(textAudioMusic.getX(), switchAnimations.getY());

		menuOptions.attachChild(textAudioMusic);
		menuOptions.attachChild(textAudioSfx);
		menuOptions.attachChild(textAnimations);

		menuOptions.attachChild(switchAudioMusic);
		menuOptions.attachChild(switchAudioSfx);
		menuOptions.attachChild(switchAnimations);

		switchAudioMusic.fadeOut(0f);
		switchAudioSfx.fadeOut(0f);
		switchAnimations.fadeOut(0f);
		textAudioMusic.fadeOut(0f);
		textAudioSfx.fadeOut(0f);
		textAnimations.fadeOut(0f);


		optionsTitle = new RiskaSprite(resources.labelLargeRegion, vbom, "Options", resources.mMenuFont);
		optionsTitle.setTextColor(Color.BLACK);
		optionsTitle.setSize(0.6f * camera.getWidth(), 0.25f * camera.getHeight());
		optionsTitle.setTextBoundingFactor(0.9f);
		optionsTitle.setPosition(camera.getCenterX(), 0.85f * camera.getHeight());
		optionsTitle.fadeOut(0f);

		menuOptions.attachChild(optionsTitle);
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
				switchAudioSfx.fadeIn(animationTime);
				textAudioSfx.setTextColor(Utils.OtherColors.DEACTIVATED_TEXT);
				//textAudioSfx.setColor(Utils.OtherColors.DEACTIVATED);
			}
			else
			{
				GameOptions.setSfxEnabled(true);
				switchAudioSfx.fadeOut(animationTime);
				textAudioSfx.setTextColor(Utils.OtherColors.WHITE);
				//textAudioSfx.setColor(Utils.OtherColors.WHITE);
			}
			break;

		case MUSIC:
			if(GameOptions.musicEnabled())
			{
				GameOptions.setMusicEnabled(false);
				switchAudioMusic.fadeIn(animationTime);
				textAudioMusic.setTextColor(Utils.OtherColors.DEACTIVATED_TEXT);
				//textAudioMusic.setColor(Utils.OtherColors.DEACTIVATED);
			}
			else
			{
				GameOptions.setMusicEnabled(true);
				switchAudioMusic.fadeOut(animationTime);
				textAudioMusic.setTextColor(Utils.OtherColors.WHITE);
				//textAudioMusic.setColor(Utils.OtherColors.WHITE);
			}
			break;

		case MENU_ANIMATIONS:
			if(GameOptions.animationsEnabled())
			{
				GameOptions.setAnimationsEnabled(false);
				switchAnimations.fadeIn(animationTime);
				textAnimations.setTextColor(Utils.OtherColors.DEACTIVATED_TEXT);
				//textAnimations.setColor(Utils.OtherColors.DEACTIVATED);
			}
			else
			{
				GameOptions.setAnimationsEnabled(true);
				switchAnimations.fadeOut(animationTime);
				textAnimations.setTextColor(Utils.OtherColors.WHITE);
				//textAnimations.setColor(Utils.OtherColors.WHITE);
			}
			animationTime = GameOptions.animationTime;
			break;

		default:
			break;
		}
	}

	// ==================================================
	// NEW GAME MENU
	// ==================================================
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
		//menuNewGame.setTouchAreaBindingOnActionMoveEnabled(true);
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

			Text text = new Text(0f, 0f, resources.mMenuFont, "Map " + i, 20, vbom);
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

		name = new RiskaSprite(resources.emptyButtonRegion, vbom, "Name", resources.mMenuFont);
		isActive = new RiskaSprite(resources.emptyButtonRegion, vbom, "Active?", resources.mMenuFont);
		isCpu = new RiskaSprite(resources.emptyButtonRegion, vbom, "Cpu?", resources.mMenuFont);
		color = new RiskaSprite(resources.emptyButtonRegion, vbom, "Colors", resources.mMenuFont);

		titleCanvas.addGraphic(name, 0.2f, 0.5f, 0.4f, 1f);
		titleCanvas.addGraphic(isActive, 0.9f, 0.5f, 0.2f, 1f);
		titleCanvas.addGraphic(isCpu, 0.75f, 0.5f, 0.2f, 1f);
		titleCanvas.addGraphic(color, 0.5f, 0.5f, 0.2f, 1f);

		int index = maxPlayers;

		for(int i = 0; i < maxPlayers; i++)
		{
			playerCanvas[i] = new RiskaCanvas(0f, 0f, titleCanvas.getWidth(), titleCanvas.getHeight());

			playerName[i] = new RiskaSprite(resources.emptyButtonRegion, vbom, "", resources.mMenuFont)
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
						onLevelSelected(getTag());
						break;

					default:
						break;
					}

					return true;
				}

			};
			levelCheckBox[i].setTag(i);

			levelText[i] = new Text(0f, 0f, resources.mMenuFont, GameOptions.getLevelDescr(i), 100, vbom)
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
						onLevelSelected(getTag());
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

		onLevelSelected(0);

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

		menuNewGameMapTab = new RiskaSprite(resources.tabRegion, vbom, "Map", resources.mMenuFont)
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

		menuNewGamePlayersTab = new RiskaSprite(resources.tabRegion, vbom, "Players", resources.mMenuFont)
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

		menuNewGameLevelTab = new RiskaSprite(resources.tabRegion, vbom, "Level", resources.mMenuFont)
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

		menuNewGameGoTab = new RiskaMenuItem(NEW_GO, resources.tabRegion, vbom, "GO!", resources.mMenuFont);

		menuNewGameMapTab.setSize(factor * menuNewGameMapCanvas.getWidth(), 0.1f * camera.getHeight());
		menuNewGameMapTab.setPosition(Utils.leftGlobal(menuNewGameMapCanvas) + 0.5f * factor * menuNewGameMapCanvas.getWidth(),
				Utils.bottomGlobal(menuNewGameMapCanvas) - 1f * Utils.halfY(menuNewGameMapTab));

		menuNewGamePlayersTab.setSize(menuNewGameMapTab.getWidth(), menuNewGameMapTab.getHeight());
		menuNewGamePlayersTab.setPosition(Utils.rightGlobal(menuNewGameMapTab) + Utils.halfX(menuNewGamePlayersTab), menuNewGameMapTab.getY());

		menuNewGameLevelTab.setSize(menuNewGameMapTab.getWidth(), menuNewGameMapTab.getHeight());
		menuNewGameLevelTab.setPosition(Utils.rightGlobal(menuNewGamePlayersTab) + Utils.halfX(menuNewGameLevelTab), menuNewGameMapTab.getY());

		menuNewGameGoTab.setSize(menuNewGameMapTab.getWidth(), menuNewGameMapTab.getHeight());
		menuNewGameGoTab.setPosition(Utils.rightGlobal(menuNewGameLevelTab) + Utils.halfX(menuNewGameGoTab), menuNewGameMapTab.getY());

		menuNewGame.attachChild(menuNewGameMapTab);
		menuNewGame.attachChild(menuNewGamePlayersTab);
		menuNewGame.attachChild(menuNewGameLevelTab);

		menuNewGame.addMenuItem(menuNewGameGoTab);

		menuNewGame.registerTouchArea(menuNewGameMapTab);
		menuNewGame.registerTouchArea(menuNewGamePlayersTab);
		menuNewGame.registerTouchArea(menuNewGameLevelTab);

		// Map tab selected
		menuNewGameMapTab.setColor(Utils.OtherColors.DARK_GREY);
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
				mapIndex = i;
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
			int count = 0;
			int color = playerColor[index];

			while(count < playerColor.length)
			{
				color++;
				color %= (GameOptions.numberOfFactions + 1);
				if(color == GameOptions.numberOfFactions || !Utils.inArray(color, playerColor))
				{
					playerColor[index] = color;
					break;
				}

				count++;
			}

			updateFactionColors(index);
		}	
	}

	private void updateFactionColors(int index)
	{
		ButtonSprite priColor = (ButtonSprite)playerColorButton[index].getChildByIndex(1);
		ButtonSprite secColor = (ButtonSprite)playerColorButton[index].getChildByIndex(0);

		if(playerColor[index] == GameOptions.numberOfFactions)
		{
			priColor.setColor(Color.WHITE);
			secColor.setColor(Color.WHITE);

			priColor.setCurrentTileIndex(2);
			secColor.setCurrentTileIndex(2);
		}
		else
		{
			priColor.setColor(GameOptions.getPriColor(playerColor[index]));
			secColor.setColor(GameOptions.getSecColor(playerColor[index]));

			priColor.setCurrentTileIndex(0);
			secColor.setCurrentTileIndex(1);
		}
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

	private void onLevelSelected(int index)
	{
		//Log.d("Riska", " > Level Selected : " + index);

		for(int i = 0; i < GameOptions.numberOfLevels; i++)
		{
			if(i == index)
			{
				levelIndex = i;
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
	// OTHER METHODS
	// ==================================================
	// ==================================================
	// UPDATE DATA
	// ==================================================
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
			menuMainRiskaTitle.fadeIn(animationTime);
			menuMainStartButton.fadeIn(animationTime);
			menuMainOptionsButton.fadeIn(animationTime);
			menuMain.registerTouchArea(menuMainStartButton);
			menuMain.registerTouchArea(menuMainOptionsButton);
			break;

		case START:
			startTitle.fadeIn(animationTime);
			menuStartNewButton.fadeIn(animationTime);
			menuStartLoadButton.fadeIn(animationTime);
			menuStart.registerTouchArea(menuStartNewButton);
			menuStart.registerTouchArea(menuStartLoadButton);
			break;

		case OPTIONS:
			optionsTitle.fadeIn(animationTime);
			if(!GameOptions.animationsEnabled()) switchAnimations.fadeIn(animationTime);
			if(!GameOptions.sfxEnabled()) switchAudioSfx.fadeIn(animationTime);
			if(!GameOptions.musicEnabled()) switchAudioMusic.fadeIn(animationTime);
			textAnimations.fadeIn(animationTime);
			textAudioSfx.fadeIn(animationTime);
			textAudioMusic.fadeIn(animationTime);
			menuOptions.registerTouchArea(textAnimations);
			menuOptions.registerTouchArea(textAudioSfx);
			menuOptions.registerTouchArea(textAudioMusic);
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
			menuMainRiskaTitle.fadeOut(animationTime);
			menuMainStartButton.fadeOut(animationTime);
			menuMainOptionsButton.fadeOut(animationTime);		
			menuMain.unregisterTouchArea(menuMainStartButton);
			menuMain.unregisterTouchArea(menuMainOptionsButton);
			break;

		case START:
			startTitle.fadeOut(animationTime);
			menuStartNewButton.fadeOut(animationTime);
			menuStartLoadButton.fadeOut(animationTime);		
			menuStart.unregisterTouchArea(menuStartNewButton);
			menuStart.unregisterTouchArea(menuStartLoadButton);
			break;

		case OPTIONS:
			optionsTitle.fadeOut(animationTime);
			if(!GameOptions.animationsEnabled()) switchAnimations.fadeOut(animationTime);
			if(!GameOptions.sfxEnabled()) switchAudioSfx.fadeOut(animationTime);
			if(!GameOptions.musicEnabled()) switchAudioMusic.fadeOut(animationTime);
			textAnimations.fadeOut(animationTime);
			textAudioSfx.fadeOut(animationTime);
			textAudioMusic.fadeOut(animationTime);	
			menuOptions.unregisterTouchArea(textAnimations);
			menuOptions.unregisterTouchArea(textAudioSfx);
			menuOptions.unregisterTouchArea(textAudioMusic);
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
		hideChild(currentChild);

		if(GameOptions.animationsEnabled())
		{

			DelayModifier waitForAnimation = new DelayModifier(1.2f * animationTime)
			{
				@Override
				protected void onModifierFinished(IEntity pItem)
				{
					camera.setHUD(null);
					sceneManager.createGameScene();
				}
			};
			registerEntityModifier(waitForAnimation);
		}
		else
		{
			camera.setHUD(null);
			sceneManager.createGameScene();		
		}
	}

	private void saveGameInfo()
	{
		int numOfPlayers = 0;

		// Defines number of active players
		for(int i = 0; i < GameOptions.maxPlayers; i++)
		{
			if(playerActive[i])
			{
				numOfPlayers++;
			}
			else
			{
				playerColor[i] = GameOptions.numberOfFactions;
			}
		}

		// Assigns automatic colors if not assigned by player
		for(int i = 0; i < GameOptions.maxPlayers; i++)
		{
			if(playerActive[i] && playerColor[i] == GameOptions.numberOfFactions)
			{
				for(int j = 0; j < GameOptions.numberOfFactions; j++)
				{
					if(!Utils.inArray(j, playerColor))
					{
						playerColor[i] = j;
						break;
					}
				}
			}
		}

		// Creates the players
		GameInfo.setNumberOfPlayers(numOfPlayers);
		GameInfo.clearPlayers();

		for(int i = 0; i < GameOptions.maxPlayers; i++)
		{
			if(playerActive[i])
			{
				GameInfo.addPlayer(playerName[i].getText(), playerIsCpu[i], playerColor[i]);
			}	
		}

		GameInfo.setMap(mapIndex);
		GameInfo.setLevel(levelIndex);
	}

	private void resetGameInfo()
	{
		currentNewGameTab = NEWGAME_TAB.MAP;

		for(int i = 0; i < GameOptions.maxPlayers; i++)
		{	
			playerIsCpu[i] = (i < GameOptions.minHumanPlayers) ? false : true;
			playerActive[i] = (i < GameOptions.minPlayers) ? true : false;
			playerActivable[i] = (i < GameOptions.minPlayers) ? false : true;
			playerEditable[i] = (i < GameOptions.minHumanPlayers) ? false : true;

			playerColor[i] = (playerActive[i] ? i : GameOptions.numberOfFactions);
		}
	}


}
