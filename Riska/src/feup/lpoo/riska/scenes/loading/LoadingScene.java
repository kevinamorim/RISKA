package feup.lpoo.riska.scenes.loading;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.text.Text;

import feup.lpoo.riska.scenes.BaseScene;
import feup.lpoo.riska.utilities.Utils;

public class LoadingScene extends BaseScene {

	private SpriteBackground background;
	private Text loadingText;
	
	@Override
	public void createScene()
	{
		//background = new SpriteBackground(new Sprite(camera.getCenterX(), camera.getCenterY(), camera.getWidth(), camera.getHeight(), resources, vbom));
		
		loadingText = new Text(
				camera.getCenterX(),
				camera.getCenterY(), 
				resources.mMenuFont, "Loading...", vbom);
		
		loadingText.setScale(0.5f);
		
		setBackground(background);
		attachChild(loadingText);	
	}

	@Override
	public void onBackKeyPressed()
	{
		return;
	}

	@Override
	public void disposeScene() {
	}

	@Override
	public Utils.CONTEXT getSceneType()
	{
		return Utils.CONTEXT.LOADING;
	}

	@Override
	public void onMenuKeyPressed() {
		// TODO Auto-generated method stub
		
	}

}
