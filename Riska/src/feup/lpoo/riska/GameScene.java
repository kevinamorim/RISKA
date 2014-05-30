package feup.lpoo.riska;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.color.Color;

import android.graphics.Point;

public class GameScene extends MenuScene implements IOnMenuItemClickListener {
	MainActivity activity;
	SceneManager manager;
	
	protected Point touchPoint;
	protected Sprite background;
	
	final int COLS = 4;
	final int ROWS = 3;
	
	protected Region regions[]; /* TODO: Meter isto no mapa */ 
	
	protected Map map;
	
	public GameScene() {
		
		super(MainActivity.getSharedInstance().mCamera);
		
		activity = MainActivity.getSharedInstance();
		manager = SceneManager.getSharedInstance();
		
		setBackground(new Background(Color.CYAN));
		
		createRegions();
		drawMap();
		
		setOnMenuItemClickListener(this);
	
	}
	
	private void createRegions() {
		
		regions = new Region[COLS*ROWS];
		
		float width = MainActivity.CAMERA_WIDTH/COLS;
		float height = MainActivity.CAMERA_HEIGHT/ROWS;
		
		int pos = 0;
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
								
				regions[pos] = new Region(pos, 
						width, height,
						((width * j) + (width/2)), ((height * i) + (height/2)),
						manager.mRegionsTextureRegions[pos], 
						activity.getVertexBufferObjectManager());
			
				pos++;
				
			}
		}
		
	}
	
	private void drawMap() {
		
		for(int i = 0; i < regions.length; i++) {
			regions[i].setPosition(regions[i].getPosX(), regions[i].getPosY());
			addMenuItem(regions[i]);
		}
		
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
