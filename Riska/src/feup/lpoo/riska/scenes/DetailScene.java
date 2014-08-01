package feup.lpoo.riska.scenes;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.scenes.SceneManager.SceneType;
import feup.lpoo.riska.utilities.Utilities;

public class DetailScene extends BaseScene {

	/* TODO : increase details */

	// ======================================================
	// CONSTANTS
	// ======================================================

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
	public void onBackKeyPressed() { }

	@Override
	public SceneType getSceneType() { return null; }

	@Override
	public void disposeScene() { }

	// ======================================================
	// CREATE DISPLAY
	// ======================================================
	private void createDisplay()
	{
		setBackgroundEnabled(false);
		
		createBackground();
		createText();
		
		attachChild(window);
		window.attachChild(playerRegionName);
		window.attachChild(enemyRegionName);
	}

	private void createBackground()
	{

		window = new Sprite(
				camera.getCenterX(),
				camera.getCenterY(),
				camera.getWidth(),
				camera.getHeight(),
				resources.windowRegion,
				vbom);
		
		window.setScale(0.9f);
	}

	private void createText()
	{
		playerRegionName = new Text(
				0.25f * Utilities.getBoundsX(window), 
				0.75f * Utilities.getBoundsY(window),
				resources.mGameFont, "", 1000, resources.vbom);

		enemyRegionName = new Text(
				0.75f * Utilities.getBoundsX(window),
				0.75f * Utilities.getBoundsY(window),
				resources.mGameFont, "", 1000, resources.vbom);
	}

	// ======================================================
	// UPDATE DATA
	// ======================================================
	public void update(Region playerRegion, Region enemyRegion) {

		playerRegionName.setText(Utilities.wrapText(resources.mGameFont, 
				(playerRegion != null) ? playerRegion.getName() : "", this.getWidth()/2));

		enemyRegionName.setText(Utilities.wrapText(resources.mGameFont, 
				(enemyRegion != null) ? enemyRegion.getName() : "", this.getWidth()/2));	

	}

}
