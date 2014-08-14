package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.logic.SceneManager.SCENE_TYPE;

public class GameOverScene extends BaseScene {

	@Override
	public void createScene() {
		createBackground();
		createText();
	}

	@Override
	public void onBackKeyPressed() {
		sceneManager.loadMainMenuScene(engine);
	}

	@Override
	public SCENE_TYPE getSceneType() {
		return SCENE_TYPE.GAMEOVER;
	}

	@Override
	public void disposeScene() {
		detachSelf();
		dispose();
	}
	
	private void createBackground() {
		setBackground(new Background(Color.BLACK));
	}
	
	private void createText() {
		Text text = new Text(0, 0, resources.mGameOverFont, "", 1000, vbom);
		text.setText("Game Over...");
		text.setPosition(camera.getCenterX() - text.getWidth(), camera.getCenterY());
		attachChild(text);
	}

	@Override
	public void onSceneShow() {
		// TODO Auto-generated method stub
		
	}

}
