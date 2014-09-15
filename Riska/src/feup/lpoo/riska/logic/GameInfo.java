package feup.lpoo.riska.logic;

import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Player;
import feup.lpoo.riska.resources.ResourceCache;

public class GameInfo
{
	private static Player[] players;
	private static int playersIndex = 0;
	
	public static int defaultSummonPoolSize;
	public static int defaultPlayerMoves = 1;
	
	public static final int maxMoves = 5;
	public static final int maxSummonPool = 15;
	public static final int minSummonPool = 0;
	
	public static int defaultDeployable = 1;

	public static final int minGarrison = 1;
	public static final int maxGarrison = 5;
	
	public static int currentLevelIndex;

	public static int numberOfPlayers;
	public static int cpuPlayers;
	public static int humanPlayers;

	public static Map currentMap;
	public static int currentMapIndex;

	// ======================================================
	// ======================================================
	public static void setLevel(int levelIndex)
	{
		currentLevelIndex = levelIndex;
	}
	
	public static void setNumberOfPlayers(int value)
	{
		numberOfPlayers = Math.min(value, GameOptions.maxPlayers);
		players = new Player[numberOfPlayers];
	}
	
	public static void clearPlayers()
	{
		playersIndex = 0;
	}

	public static void addPlayer(String pName, boolean isCpu, int pColor)
	{
		players[playersIndex] = new Player(isCpu, GameOptions.getColors(pColor), pName);
		playersIndex++;
	}

	public static Player[] players()
	{
		return players;
	}

	public static void setMap(int mapIndex)
	{
		currentMapIndex = mapIndex;
		currentMap = ResourceCache.instance.maps.get(currentMapIndex);
		
		setPlayersVars();
	}
	
	private static void setPlayersVars()
	{
		defaultSummonPoolSize = (currentMap.getNumberOfRegions() / numberOfPlayers * 2);
	}
}
