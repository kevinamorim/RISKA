package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.logic.SceneManager.SCENE_TYPE;

public class LoadingScene extends BaseScene {

	//private Sprite background;
	private Text loadingText;
	
	@Override
	public void createScene()
	{
		setBackground(new Background(Color.BLACK));
		
		loadingText = new Text(
				camera.getCenterX(),
				camera.getCenterY(), 
				resources.mainMenuFont, "Loading...", vbom);
		
		loadingText.setScale(0.5f);
		
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

}
