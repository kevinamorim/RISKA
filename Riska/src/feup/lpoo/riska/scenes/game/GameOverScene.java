package feup.lpoo.riska.scenes.game;

import feup.lpoo.riska.scenes.BaseScene;
import feup.lpoo.riska.utilities.Utils;

public class GameOverScene extends BaseScene {

	@Override
	public void createScene() {
	}

	@Override
	public void onBackKeyPressed() {
		sceneManager.loadScene(Utils.CONTEXT.MENU);
	}

	@Override
	public Utils.CONTEXT getSceneType() {
		return Utils.CONTEXT.GAME_OVER;
	}

	@Override
	public void disposeScene() {
		detachSelf();
		dispose();
	}

	@Override
	public void onMenuKeyPressed() {
		// TODO Auto-generated method stub
		
	}

}
