package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;

import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.logic.MainActivity;
import feup.lpoo.riska.resources.ResourceCache;

public class DetailScene extends Scene {
	
	// ======================================================
	// CONSTANTS
	// ======================================================
	private static final float SCALE_FACTOR = 0.9f;

	// ======================================================
	// SINGLETONS
	// ======================================================
	MainActivity activity;
	//SceneManager sceneManager;
	//CameraManager cameraManager;
	ResourceCache resources;

	// ======================================================
	// FIELDS
	// ======================================================
	private Sprite background;

	private Region playerRegion, enemyRegion;
	
	Text playerRegionName, enemyRegionName;

	public DetailScene() {
		
		activity = MainActivity.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		
		this.playerRegion = null;
		this.enemyRegion = null;

		createDisplay();
	}
	
	private void createDisplay() {
		background = new Sprite(MainActivity.CAMERA_WIDTH / 2,
				MainActivity.CAMERA_HEIGHT / 2,
				MainActivity.CAMERA_WIDTH,
				MainActivity.CAMERA_HEIGHT,
				resources.windowRegion,
				activity.getVertexBufferObjectManager());

		setBackgroundEnabled(false);
		attachChild(background);
		
		float textPosY = (float) 0.75 * MainActivity.CAMERA_HEIGHT;

		playerRegionName = new Text(MainActivity.CAMERA_WIDTH/4 - this.getWidth()/2, 
				textPosY, resources.mGameFont, "", 1000, activity.getVertexBufferObjectManager());
		enemyRegionName = new Text((float)(MainActivity.CAMERA_WIDTH * 0.75 - this.getWidth()/2),
				textPosY, resources.mGameFont, "", 1000, activity.getVertexBufferObjectManager());

		attachChild(playerRegionName);
		attachChild(enemyRegionName);

		setScale(SCALE_FACTOR);
		
		setPosition((float) ((1 - SCALE_FACTOR)/2)*MainActivity.CAMERA_WIDTH, 
				(float) ((1 - SCALE_FACTOR)/2)* MainActivity.CAMERA_HEIGHT);

		setVisible(false);
	}

	public void update() {
		
		playerRegionName.setText(wrapText(resources.mGameFont, 
				(playerRegion != null) ? playerRegion.getName() : "", this.getWidth()/2));
		
		enemyRegionName.setText(wrapText(resources.mGameFont, 
				(enemyRegion != null) ? enemyRegion.getName() : "", this.getWidth()/2));	
	}
	
	public void setAttributes(Region playerRegion, Region enemyRegion) {
		this.playerRegion = playerRegion;
		this.enemyRegion = enemyRegion;
		
		update();
	}
	
	
	private String wrapText(Font pFont, String pString, float maxWidth) {

		Text pText = new Text(0, 0, pFont, pString, 1000, activity.getVertexBufferObjectManager());

		if(pText.getWidth() < maxWidth) {
			return pString;
		}

		// Split the entire string into separated words.
		String[] words = pText.getText().toString().split(" ");

		String wrappedText = ""; /* Final string. */
		String line = ""; /* Temp variable */

		for(String word : words) {

			pText.setText(line + word);
			if(pText.getWidth() > maxWidth) {			
				wrappedText += line + "\n\n";
				line = "";

			}

			line += word + " ";

		}

		wrappedText += line;

		return wrappedText;

	}
}
