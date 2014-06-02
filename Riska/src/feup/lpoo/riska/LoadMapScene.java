package feup.lpoo.riska;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.util.adt.color.Color;

import android.graphics.Point;
import feup.lpoo.riska.io.FileRead;

public class LoadMapScene extends Scene {
	
	MainActivity activity;
	SceneManager instance;
	
	final int VALUES = 3;
	final int REGIONS = 5;
	
	public LoadMapScene() {
		
		activity = MainActivity.getSharedInstance();
		instance = SceneManager.getSharedInstance();
		
		setBackground(new Background(Color.BLACK));
		
		loadMapResources();
		
	}

	private void loadMapResources() {
		
//		String[] mapData = new String[VALUES * REGIONS];
//		String filename = "regions.txt";
//		
//		new FileRead(filename, mapData);
//		
//		for(int i = 0; i < mapData.length; i++) {
//			Region newRegion = new Region(mapData[i], new Point(Integer.parseInt(mapData[i++]),
//					Integer.parseInt(mapData[i++])));
//			instance.regions[i] = newRegion;
//		
//		}
		
	}

}
