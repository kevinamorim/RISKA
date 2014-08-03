package feup.lpoo.riska.elements;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Point;
import android.util.Log;
import feup.lpoo.riska.io.FileRead;


public class Map {

	private ArrayList<Region> regions;

	public Map(String regionsFilename, String neighboursFilename)
	{
		regions = readRegions(regionsFilename);
		readNeighbours(neighboursFilename);
	}

	public ArrayList<Region> getRegions()
	{
		return regions;
	}

	public Region getRegionById(int id)
	{
		for(Region region : regions)
		{
			if(region.ID == id) return region;
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

	public void handOutRegions(ArrayList<Player> players) {

		ArrayList<Integer> indexes = new ArrayList<Integer>();

		Random random = new Random();

		while(indexes.size() < regions.size())
		{

			int index = random.nextInt(regions.size());

			if(!indexes.contains(index))
			{
				indexes.add(index);
			}	
		}

		int i = 0;

		for(Integer index : indexes)
		{
			Region region = regions.get(index);
			Player player = players.get(i);

			region.setOwner(player);
			region.setColors(player.getPrimaryColor(), player.getScondaryColor());

			i = (i + 1) % players.size();
		}
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
					Log.d("Regions","Region " + reg.getName() + " (" + reg.ID + ") has neighbour " + region.getName() + " (" + region.ID + ") but not vice-versa.");
				}
			}
		}
	}
}
