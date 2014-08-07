package feup.lpoo.riska.scenes;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.logic.SceneManager.SCENE_TYPE;
import feup.lpoo.riska.utilities.Utils;

public class DetailScene extends BaseScene {

	/* TODO : increase details */

	// ======================================================
	// FIELDS
	// ======================================================
	Sprite window;
	Text playerRegionName;
	Text enemyRegionName;


	// ======================================================
	// ======================================================
	@Override
	public void createScene() {
		createDisplay();
	}

	@Override
	public void onBackKeyPressed()
	{
		((GameScene)getParent()).onBackKeyPressed();
	}

	@Override
	public SCENE_TYPE getSceneType()
	{
		return SCENE_TYPE.DETAIL;
	}

	@Override
	public void disposeScene() { }

	// ======================================================
	// CREATE DISPLAY
	// ======================================================
	private void createDisplay()
	{
		createBackground();
		createText();
	}

	private void createBackground()
	{
		
		this.setBackgroundEnabled(false);

		window = new Sprite(
				camera.getCenterX(),
				camera.getCenterY(),
				camera.getWidth(),
				camera.getHeight(),
				resources.windowRegion,
				vbom);
		
		window.setScale(0.9f);
		
		attachChild(window);
	}

	private void createText()
	{
		playerRegionName = new Text(
				0.25f * Utils.getRightBoundsX(window), 
				0.75f * Utils.getUpperBoundsY(window),
				resources.mGameFont, "", 1000, resources.vbom);

		enemyRegionName = new Text(
				0.75f * Utils.getRightBoundsX(window),
				0.75f * Utils.getUpperBoundsY(window),
				resources.mGameFont, "", 1000, resources.vbom);
		
		window.attachChild(playerRegionName);
		window.attachChild(enemyRegionName);
	}

	// ======================================================
	// UPDATE DATA
	// ======================================================
	public void update(Region playerRegion, Region enemyRegion)
	{
		String text = (playerRegion != null ? playerRegion.getName() : "");
		
		playerRegionName.setText(Utils.wrapText(resources.mGameFont, text, 0.5f * window.getWidth(), vbom));
	
		text = (enemyRegion != null ? enemyRegion.getName() : "");
		
		enemyRegionName.setText(Utils.wrapText(resources.mGameFont, text, 0.5f * window.getWidth(), vbom));	
	}

}
