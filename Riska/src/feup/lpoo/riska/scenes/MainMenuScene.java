package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.sprite.Sprite;
import feup.lpoo.riska.gameInterface.AnimatedButtonSpriteMenuItem;
import feup.lpoo.riska.gameInterface.AnimatedTextButtonSpriteMenuItem;
import feup.lpoo.riska.scenes.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener {
		
	// ==================================================
	// FIELDS
	// ==================================================
	private MenuScene mainMenuChildScene;
	private MenuScene startGameMenuChildScene;
	private final int MENU_START = 0;
	private final int MENU_OPTIONS = 1;
	private final int MENU_NEW = 2;
	private final int MENU_LOAD = 3;

	
	@Override
	public void createScene() {
		createBackground();
		createMenuChildScene();
		createStartGameMenuChildScene();
	}


	@Override
	public void onBackKeyPressed() {
		if(this.getChildScene().equals(mainMenuChildScene)) {
			System.exit(0);
		} else if(this.getChildScene().equals(startGameMenuChildScene)) {
			detachChildren();
			setChildScene(mainMenuChildScene);
		}
	}


	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public SceneType getSceneType() {
		return SceneType.MAIN_MENU;
	}
	
	private void createBackground() {
		
		SpriteBackground background = new SpriteBackground(new Sprite(camera.getWidth()/2, 
				camera.getHeight()/2, 
				resources.menuBackgroundRegion, 
				activity.getVertexBufferObjectManager()));
		
		setBackground(background);
	}


	
	private void createMenuChildScene() {
		
		mainMenuChildScene = new MenuScene(camera);
		
		final AnimatedTextButtonSpriteMenuItem startBtn = 
				new AnimatedTextButtonSpriteMenuItem(
						MENU_START, 
						resources.textBtnRegion.getWidth(), 
						resources.textBtnRegion.getHeight(),
						resources.textBtnRegion, 
						vbom, "START", resources.mainMenuFont);
		
		final AnimatedButtonSpriteMenuItem optionsBtn = 
				new AnimatedButtonSpriteMenuItem(
						MENU_OPTIONS, 
						(float)(0.3*resources.optionsBtnRegion.getWidth()),
						(float)(0.3*resources.optionsBtnRegion.getHeight()), 
						resources.optionsBtnRegion, 
						vbom);
		
		startBtn.setPosition(camera.getCenterX(), camera.getCenterY());
		optionsBtn.setPosition(optionsBtn.getWidth()/2, optionsBtn.getHeight()/2);
		
		mainMenuChildScene.addMenuItem(startBtn);
		mainMenuChildScene.addMenuItem(optionsBtn);
		mainMenuChildScene.setBackgroundEnabled(false);
		mainMenuChildScene.setOnMenuItemClickListener(this);
		
		setChildScene(mainMenuChildScene);
		
	}
	
	private void createStartGameMenuChildScene() {
		
		startGameMenuChildScene = new MenuScene(camera);
		
		final AnimatedTextButtonSpriteMenuItem newGameBtn =
				new AnimatedTextButtonSpriteMenuItem(
						MENU_NEW,
						resources.textBtnRegion.getWidth(),
						resources.textBtnRegion.getHeight(),
						resources.textBtnRegion,
						vbom, "NEW", resources.mainMenuFont);
		
		final AnimatedTextButtonSpriteMenuItem loadGameBtn =
				new AnimatedTextButtonSpriteMenuItem(
						MENU_LOAD,
						resources.textBtnRegion.getWidth(),
						resources.textBtnRegion.getHeight(),
						resources.textBtnRegion,
						vbom, "LOAD", resources.mainMenuFont);
		
		newGameBtn.setPosition(camera.getCenterX(), camera.getCenterY() + newGameBtn.getHeight()/2);
		loadGameBtn.setPosition(camera.getCenterX(), camera.getCenterY() - loadGameBtn.getHeight()/2);
		
		startGameMenuChildScene.addMenuItem(newGameBtn);
		startGameMenuChildScene.addMenuItem(loadGameBtn);
		startGameMenuChildScene.setBackgroundEnabled(false);
		startGameMenuChildScene.setOnMenuItemClickListener(this);
		
	}


	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch(pMenuItem.getID()) {
		case MENU_START:
			detachChildren();
			setChildScene(startGameMenuChildScene);
			break;
		case MENU_OPTIONS:
			SceneManager.getSharedInstance().createOptionsScene();
			break;
		case MENU_NEW:
			SceneManager.getSharedInstance().createGameScene();
			break;
		case MENU_LOAD:
			//SceneManager.getSharedInstance().loadGameScene(engine);
			break;
		default:
			break;
		}
		return false;
	}





	
	

}
