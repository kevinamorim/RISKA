package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;

import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.SceneManager.SceneType;
import feup.lpoo.riska.utilities.Utilities;

public class DetailScene extends BaseScene {
	
	// ======================================================
	// CONSTANTS
	// ======================================================
	private static final float SCALE_FACTOR = 0.9f;

	// ======================================================
	// FIELDS
	// ======================================================
	Text playerRegionName, enemyRegionName;
	
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
	
	private void createDisplay() {
		
		createBackground();
		createText();

		setScale(SCALE_FACTOR);
		
		setPosition((float) ((1 - SCALE_FACTOR)/2) * resources.camera.getWidth(), 
				(float) ((1 - SCALE_FACTOR)/2)* resources.camera.getHeight());

		setVisible(false);
		
	}

	public void update(Region playerRegion, Region enemyRegion) {
		
		playerRegionName.setText(Utilities.wrapText(resources.mGameFont, 
				(playerRegion != null) ? playerRegion.getName() : "", this.getWidth()/2));
		
		enemyRegionName.setText(Utilities.wrapText(resources.mGameFont, 
				(enemyRegion != null) ? enemyRegion.getName() : "", this.getWidth()/2));	
		
	}
	
	private void createBackground() {
		
		Sprite background = new Sprite(resources.camera.getCenterX(),
				resources.camera.getCenterY(),
				resources.camera.getWidth(),
				resources.camera.getHeight(),
				resources.windowRegion,
				resources.vbom);

		setBackgroundEnabled(false);
		attachChild(background);
		
	}
	
	private void createText() {
		
		float textPosY = (float) 0.75 * resources.camera.getHeight();

		playerRegionName = new Text(resources.camera.getWidth()/4 - getWidth()/2, 
				textPosY, resources.mGameFont, "", 1000, resources.vbom);
		
		enemyRegionName = new Text((float)(resources.camera.getWidth() * 0.75 - getWidth()/2),
				textPosY, resources.mGameFont, "", 1000, resources.vbom);

		attachChild(playerRegionName);
		attachChild(enemyRegionName);
		
	}
	
}
