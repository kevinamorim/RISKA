package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.scenes.SceneManager.SceneType;

public class LoadingScene extends BaseScene {

	@Override
	public void createScene() {
		setBackground(new Background(Color.BLACK));
		
		attachChild(new Text(camera.getCenterX(), camera.getCenterY(), 
				resources.mainMenuFont, "Loading...", vbom));
		
	}

	@Override
	public void onBackKeyPressed() {
		return;
	}

	@Override
	public void disposeScene() {
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.LOADING;
	}

}
