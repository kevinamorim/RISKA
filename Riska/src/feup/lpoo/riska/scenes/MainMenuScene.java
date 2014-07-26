package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import feup.lpoo.riska.gameInterface.AnimatedButtonSpriteMenuItem;
import feup.lpoo.riska.gameInterface.AnimatedTextButtonSpriteMenuItem;
import feup.lpoo.riska.scenes.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener {
		
	// ==================================================
	// FIELDS
	// ==================================================
	private MenuScene mainMenuChildScene;
	private MenuScene startGameMenuChildScene;

	final int MENU_START = 0;
	final int MENU_NEW = 1;
	final int MENU_LOAD = 2;
	final int MENU_RETURN = 3;
	final int MENU_OPTIONS = 4;
	
	@Override
	public void createScene() {
		createBackground();
		createMenuChildScene();
		createStartGameMenuChildScene();
		createBorder();
	}

	@Override
	public void onBackKeyPressed() {
		if(this.getChildScene().equals(mainMenuChildScene))
		{
			System.exit(0);
		}
		else if(this.getChildScene().equals(startGameMenuChildScene))
		{
			detachChildren();
			setChildScene(mainMenuChildScene);
		}
	}

	@Override
	public void disposeScene() {
		detachChildren();
		mainMenuChildScene = null;
		startGameMenuChildScene = null;
		dispose();
	}
	
	@Override
	public SceneType getSceneType() {
		return SceneType.MAIN_MENU;
	}
	
	private void createBackground() {
		
		SpriteBackground background = new SpriteBackground(new Sprite(camera.getWidth()/2, 
				camera.getHeight()/2, 
				resources.menuBackgroundRegion, 
				vbom));
		
		setBackground(background);
	}

	private void createMenuChildScene() {
		
		mainMenuChildScene = new MenuScene(camera);
		
		final AnimatedTextButtonSpriteMenuItem startBtn = new AnimatedTextButtonSpriteMenuItem(MENU_START, 
						resources.textBtnRegion.getWidth(), 
						resources.textBtnRegion.getHeight(),
						resources.textBtnRegion, 
						vbom, "START", resources.mainMenuFont);
		
		final AnimatedButtonSpriteMenuItem optionsBtn = new AnimatedButtonSpriteMenuItem(MENU_OPTIONS, 
						0.3f*resources.optionsBtnRegion.getWidth(),
						0.3f*resources.optionsBtnRegion.getHeight(), 
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
		
		final AnimatedTextButtonSpriteMenuItem newGameBtn = new AnimatedTextButtonSpriteMenuItem(MENU_NEW,
						resources.textBtnRegion.getWidth(),
						resources.textBtnRegion.getHeight(),
						resources.textBtnRegion,
						vbom, "NEW", resources.mainMenuFont);
		
		final AnimatedTextButtonSpriteMenuItem loadGameBtn = new AnimatedTextButtonSpriteMenuItem(MENU_LOAD,
						resources.textBtnRegion.getWidth(),
						resources.textBtnRegion.getHeight(),
						resources.textBtnRegion,
						vbom, "LOAD", resources.mainMenuFont);

		final AnimatedButtonSpriteMenuItem returnBtn = new AnimatedButtonSpriteMenuItem(MENU_RETURN,
				0.3f*resources.returnBtnRegion.getWidth(), 
				0.3f*resources.returnBtnRegion.getHeight(),
				resources.returnBtnRegion,
				vbom);
		
		newGameBtn.setPosition(camera.getCenterX(), camera.getCenterY() + newGameBtn.getHeight()/2);
		loadGameBtn.setPosition(camera.getCenterX(), camera.getCenterY() - loadGameBtn.getHeight()/2);
		returnBtn.setPosition(returnBtn.getWidth()/2, returnBtn.getHeight()/2);
		
		startGameMenuChildScene.addMenuItem(returnBtn);
		startGameMenuChildScene.addMenuItem(newGameBtn);
		startGameMenuChildScene.addMenuItem(loadGameBtn);
		startGameMenuChildScene.setBackgroundEnabled(false);
		startGameMenuChildScene.setOnMenuItemClickListener(this);
		
	}
	
	private void createBorder()
	{
		Sprite border = new Sprite(camera.getWidth()/2, camera.getHeight()/2, resources.menuBorderRegion, vbom);
		border.setSize(camera.getWidth(), camera.getHeight());

		mainMenuChildScene.attachChild(border);
		
		Sprite border2 = new Sprite(camera.getWidth()/2, camera.getHeight()/2, resources.menuBorderRegion, vbom);
		border2.setSize(camera.getWidth(), camera.getHeight());
		
		startGameMenuChildScene.attachChild(border2);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pX, float pY)
	{
		switch(pMenuItem.getID())
		{
		case MENU_START:
			setChildScene(startGameMenuChildScene);
			break;
		case MENU_OPTIONS:
			sceneManager.createOptionsScene();
			break;
		case MENU_NEW:
			sceneManager.createGameScene();
			break;
		case MENU_LOAD:
			//sceneManager.loadGameScene(engine);
			break;
		case MENU_RETURN:
			setChildScene(mainMenuChildScene);
			break;
		default:
			break;
		}
		
		return false;
	}





	
	

}
