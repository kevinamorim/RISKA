package feup.lpoo.riska.elements;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;
import feup.lpoo.riska.io.FileRead;


public class Map {
	
	//public final int MIN_SOLDIERS_PER_REGION = 1;

	private ArrayList<Region> regions;
	
	private String descr;
	
	private int INDEX;

	public Map(int mapIndex, String descrFilename, String regionsFilename, String neighboursFilename)
	{
		INDEX = mapIndex;
		
		readDescription(descrFilename);
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

	public void initRegions(int minGarrison)
	{
		for(Region region : regions)
		{
			region.setSoldiers(Math.max(minGarrison, region.getGarrison()));
		}
	}
	
	private ArrayList<Region> readRegions(String filename)
	{
		ArrayList<String> mapData = new ArrayList<String>();

		FileRead.ReadCSV(filename, mapData);

		ArrayList<Region> regions = new ArrayList<Region>();

		for(int i = 0; i < mapData.size(); i++) {

			int id = Integer.parseInt(mapData.get(i));
			i++;
			String name = mapData.get(i);
			i++;
			int stratX = Integer.parseInt(mapData.get(i));
			i++;
			int stratY = Integer.parseInt(mapData.get(i));
			i++;
			String continent = mapData.get(i);
			i++;
			float posX = Float.parseFloat(mapData.get(i));
			i++;
			float posY = Float.parseFloat(mapData.get(i));
			i++;

			Region newRegion = new Region(id, name, posX, posY, stratX, stratY, continent);

			regions.add(newRegion);
		}

		return regions;
	}

	private void readDescription(String filename)
	{
		ArrayList<String> mapDescr = new ArrayList<String>();

		FileRead.ReadDES(filename, mapDescr);
		
		//this.descr = mapDescr.get(0);
	}

	public void handOutRegions(Player[] players) {

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
			Player player = players[i];

			region.setOwner(player);
			region.setColors(player.priColor, player.secColor);

			i = (i + 1) % players.length;
		}
	}

	private void readNeighbours(String filename) {

		ArrayList<String> data = new ArrayList<String>();
		FileRead.ReadCSV(filename, data);

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

	private void checkNeighbours()
	{
		for(Region reg : getRegions())
		{
			for(Region region : reg.getNeighbours())
			{
				if(!region.getNeighbours().contains(reg))
				{
					Log.d("Regions","Region " + reg.name + " (" + reg.ID + ") has neighbour " + region.name + " (" + region.ID + ") but not vice-versa.");
				}
			}
		}
	}

	public Region getRegionByIndex(int index)
	{
		return regions.get(index);
	}
	
	public String getDescription()
	{
		return descr;
	}
	
	public int getIndex()
	{
		return INDEX;
	}
	
	
}
