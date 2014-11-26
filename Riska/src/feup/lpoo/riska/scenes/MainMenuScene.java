package feup.lpoo.riska.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.util.adt.color.Color;

import android.util.Log;
import feup.lpoo.riska.gameInterface.RiskaItem;
import feup.lpoo.riska.gameInterface.RiskaIcon;
import feup.lpoo.riska.gameInterface.RiskaSprite;
import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.logic.GameOptions;
import feup.lpoo.riska.logic.SceneManager.SCENE_TYPE;
import feup.lpoo.riska.scenes.RiskaMenuScene.MODE;

public class MainMenuScene extends BaseScene implements Displayable, IOnMenuItemClickListener {

	// ==================================================
	// CONSTANTS
	// ==================================================
	private enum CHILD {
		MAIN,
		OPTIONS,
		OPTIONS_SOUND,
		PLAY,
		SINGLEPLAYER,
		GO,
		NONE
	};
	
	private CHILD currentChild;

	private static float animationTime = GameOptions.animationTime;

	// ==================================================
	// FIELDS
	// ==================================================
	private RiskaMenuScene mainMenu;

	// ==================================================
	// METHODS
	// ==================================================
	@Override
	public void createScene()
	{
		createDisplay();

		//changeChildScene(CHILD.NONE, CHILD.MAIN);
	}

	@Override
	public void onBackKeyPressed()
	{
		if(getEntityModifierCount() == 0)
		{

			if(getChildScene().equals(mainMenu))
			{
				//IOManager.saveGameOptions();
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
		createMainMenu();
		
		setChildScene(mainMenu);
	}

	private void createBackground()
	{
//		background = new SpriteBackground(new Sprite(
//				camera.getCenterX(), 
//				camera.getCenterY(),
//				camera.getWidth(),
//				camera.getHeight(),
//				resources.menuBackground, 
//				vbom));

		setBackground(new Background(Color.WHITE));
	}

	// ==================================================
	// MAIN MENU
	// ==================================================
	private void createMainMenu()
	{
		mainMenu = new RiskaMenuScene(camera);
		
		mainMenu.setDisplayMode(MODE.CIRCULAR);
		
		float iconSize = 0.2f * mainMenu.getHeight();
		RiskaItem x;
		
		x = new RiskaIcon(0f, 0f, iconSize, iconSize, new RiskaSprite(resources.windowRegion, vbom), new RiskaSprite(resources.label, vbom));
		mainMenu.addItem(x);
		
		x = new RiskaIcon(0f, 0f, iconSize, iconSize, new RiskaSprite(resources.windowRegion, vbom), new RiskaSprite(resources.label, vbom));
		mainMenu.addItem(x);
		
		x = new RiskaIcon(0f, 0f, iconSize, iconSize, new RiskaSprite(resources.windowRegion, vbom), new RiskaSprite(resources.label, vbom));
		mainMenu.addItem(x);
	}

	// ==================================================
	// START MENU
	// ==================================================

	// ==================================================
	// OPTIONS MENU
	// ==================================================

	// ==================================================
	// UPDATE DATA
	// ==================================================
	private MenuScene getChildScene(CHILD x)
	{
		switch(x)
		{

		case MAIN:
			return mainMenu;

		default:
			return null;
		}
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
			break;

		case PLAY:
			break;

		case OPTIONS:
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
//			menuMainRiskaTitle.fadeOut(animationTime);
//			menuMainStartButton.fadeOut(animationTime);
//			menuMainOptionsButton.fadeOut(animationTime);		
//			menuMain.unregisterTouchArea(menuMainStartButton);
//			menuMain.unregisterTouchArea(menuMainOptionsButton);
			break;

		case PLAY:
//			startTitle.fadeOut(animationTime);
//			menuStartNewButton.fadeOut(animationTime);
//			menuStartLoadButton.fadeOut(animationTime);		
//			menuStart.unregisterTouchArea(menuStartNewButton);
//			menuStart.unregisterTouchArea(menuStartLoadButton);
			break;

		case OPTIONS:
//			optionsTitle.fadeOut(animationTime);
//			if(!GameOptions.animationsEnabled()) switchAnimations.fadeOut(animationTime);
//			if(!GameOptions.sfxEnabled()) switchAudioSfx.fadeOut(animationTime);
//			if(!GameOptions.musicEnabled()) switchAudioMusic.fadeOut(animationTime);
//			textAnimations.fadeOut(animationTime);
//			textAudioSfx.fadeOut(animationTime);
//			textAudioMusic.fadeOut(animationTime);	
//			menuOptions.unregisterTouchArea(textAnimations);
//			menuOptions.unregisterTouchArea(textAudioSfx);
//			menuOptions.unregisterTouchArea(textAudioMusic);
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

	@Override
	public boolean onMenuItemClicked(MenuScene scene, IMenuItem item, float pX, float pY) {
		// TODO Auto-generated method stub
		return false;
	}

}
