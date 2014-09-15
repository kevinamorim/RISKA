package feup.lpoo.riska.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.interfaces.Displayable;
import feup.lpoo.riska.logic.SceneManager.SCENE_TYPE;

public class LoadMapScene extends BaseScene implements Displayable {
	
	final int VALUES = 3;
	final int REGIONS = 5;
	
	@Override
	public void createScene()
	{
		createDisplay();

		loadMapResources();
	}

	@Override
	public void onBackKeyPressed() { }

	@Override
	public SCENE_TYPE getSceneType()
	{
		return SCENE_TYPE.LOAD_MAP;
	}

	@Override
	public void disposeScene()
	{
		dispose();
	}

	@Override
	public void createDisplay()
	{
		setBackground(new Background(Color.BLACK));
	}
	
	private void loadMapResources()
	{
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

	@Override
	public void onMenuKeyPressed() {
		// TODO Auto-generated method stub
		
	}
}
