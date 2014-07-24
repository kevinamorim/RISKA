package feup.lpoo.riska.elements;

import java.util.ArrayList;

import android.graphics.Point;
import android.util.Log;
import feup.lpoo.riska.io.FileRead;


public class Map {
	
	protected ArrayList<Region> regions;
	

	public Map(String regionsFilename, String neighboursFilename) {
		regions = readRegions(regionsFilename);
		readNeighbours(neighboursFilename);
	}


	public ArrayList<Region> getRegions() {
		return regions;
	}
	

	public Region getRegionById(int id) {
		for(Region region : regions) {
			if(region.getId() == id) return region;
		}
		return null;
	}
	

	public int getNumberOfRegions() {
		return regions.size();
	}
	
	private ArrayList<Region> readRegions(String filename) {
		ArrayList<String> mapData = new ArrayList<String>();

		new FileRead(filename, mapData);

		ArrayList<Region> regions = new ArrayList<Region>();

		for(int i = 0; i < mapData.size(); i++) {

			int id = Integer.parseInt(mapData.get(i));
			i++;
			String name = mapData.get(i);
			i++;
			int x = Integer.parseInt(mapData.get(i));
			i++;
			int y = Integer.parseInt(mapData.get(i));
			i++;
			String continent = mapData.get(i);
			i++;

			Region newRegion = new Region(id, name, new Point(x, y), continent);

			regions.add(newRegion);
		}

		return regions;

	}
	
	private void readNeighbours(String filename) {

		ArrayList<String> data = new ArrayList<String>();
		new FileRead(filename, data);

		for(int i = 0; i < data.size(); i++) {
			int id = Integer.parseInt(data.get(i));
			Region region = getRegionById(id);
			i++;
			while(data.get(i) != "#") {
				Region neighbour = getRegionById(Integer.parseInt(data.get(i)));
				if(neighbour != null) {
					region.addNeighbour(neighbour);
				}
				i++;
			}
		}
		
		checkNeighbours();
	}

	private void checkNeighbours() {
		for(Region reg : getRegions())
		{
			for(Region region : reg.getNeighbours())
			{
				if(!region.getNeighbours().contains(reg))
				{
					Log.d("Regions","Region " + reg.getName() + " (" + reg.getId() + ") has neighbour " + region.getName() + " (" + region.getId() + ") but not vice-versa.");
				}
			}
		}
	}
}
