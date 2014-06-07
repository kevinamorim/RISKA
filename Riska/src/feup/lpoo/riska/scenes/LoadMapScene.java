package feup.lpoo.riska.scenes;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.util.adt.color.Color;

import android.graphics.Point;
import feup.lpoo.riska.io.FileRead;
import feup.lpoo.riska.logic.MainActivity;

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
		
//		ArrayList<String> mapData = new String[VALUES * REGIONS];
//		String filename = "regions.txt";
//		
//		new FileRead(filename, mapData);
//		
//		for(int i = 0; i < mapData.size(); i++) {
//			Region newRegion = new Region(mapData.get(i), new Point(Integer.parseInt(mapData[i++]),
//					Integer.parseInt(mapData[i++])));
//			instance.map.regions[i] = newRegion;
//		
//		}
		
	}

}
