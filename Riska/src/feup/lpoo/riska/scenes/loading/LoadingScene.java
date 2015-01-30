package feup.lpoo.riska.scenes.loading;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.scenes.BaseScene;
import feup.lpoo.riska.utilities.Utils;

public class LoadingScene extends BaseScene implements Displayable {

	private SpriteBackground background;
	private Text loadingText;
	
	@Override
	public void createScene()
	{
		createDisplay();
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

    @Override
    public void createDisplay() {
        SpriteBackground background = new SpriteBackground(new Sprite(
                camera.getCenterX(),
                camera.getCenterY(),
                camera.getWidth() + 2,
                camera.getHeight() + 2,
                resources.loadingBackground, vbom));

        setBackground(background);
    }
}
