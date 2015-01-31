package feup.lpoo.riska.scenes.menu;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.sprite.Sprite;

import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.logic.GameOptions;
import feup.lpoo.riska.scenes.BaseScene;
import feup.lpoo.riska.utilities.Utils;

public class BaseMenuScene extends BaseScene implements Displayable, IOnMenuItemClickListener {

	// ==================================================
	// CONSTANTS
	// ==================================================
	private enum CHILD {
		MAIN,
		OPTIONS,
		NEW_GAME
	};
	
	private CHILD currentChild = CHILD.MAIN;

	private static float animationTime = GameOptions.animationTime;

	// ==================================================
	// FIELDS
	// ==================================================

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
			System.exit(0);
//			if(getChildScene().equals(mainMenu))
//			{
//				//IOManager.saveGameOptions();
//				System.exit(0);
//			}
//			else
//			{
//				//detachChildren();
//				changeChildScene(currentChild, CHILD.MAIN);
//			}
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
	public Utils.CONTEXT getSceneType()
	{
		return Utils.CONTEXT.MENU;
	}

	// ==================================================
	// CREATE DISPLAY
	// ==================================================
	@Override
	public void createDisplay()
	{
        createBackground();
	}

    private void createBackground()
    {
        SpriteBackground background = new SpriteBackground(new Sprite(
                camera.getCenterX(),
                camera.getCenterY(),
                camera.getWidth() + 2,
                camera.getHeight() + 2,
                resources.menuBackground, vbom));

        setBackground(background);
    }

	// ==================================================
	// MAIN MENU
	// ==================================================
	
	void createMenuPlay()
	{
		// TODO
	}
	
	// ==================================================
	// UPDATE DATA
	// ==================================================
	private void changeChildScene(final CHILD from, final CHILD to)
	{
		hideChild(from);

		DelayModifier waitForAnimation = new DelayModifier(1.2f * animationTime)
		{
			@Override
			protected void onModifierFinished(IEntity pItem)
			{
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

		case NEW_GAME:
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

		case NEW_GAME:
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
					sceneManager.loadScene(Utils.CONTEXT.GAME);
				}
			};
			registerEntityModifier(waitForAnimation);
		}
		else
		{
			camera.setHUD(null);
			sceneManager.loadScene(Utils.CONTEXT.GAME);
		}
	}

	@Override
	public boolean onMenuItemClicked(MenuScene menuScene, IMenuItem menuItem, float pX, float pY) {
		// TODO Auto-generated method stub
		return false;
	}

}