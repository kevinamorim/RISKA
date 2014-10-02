package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import feup.lpoo.riska.logic.SceneManager.SCENE_TYPE;

public class LoadingScene extends BaseScene {

	private SpriteBackground background;
	private Text loadingText;
	
	@Override
	public void createScene()
	{
		background = new SpriteBackground(new Sprite(camera.getCenterX(), camera.getCenterY(), camera.getWidth(), camera.getHeight(), resources.loadingBackground, vbom));
		
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
	public SCENE_TYPE getSceneType()
	{
		return SCENE_TYPE.LOADING;
	}

	@Override
	public void onMenuKeyPressed() {
		// TODO Auto-generated method stub
		
	}

}
