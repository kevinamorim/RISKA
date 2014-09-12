package feup.lpoo.riska.logic;

import feup.lpoo.riska.elements.Player;

public class GameInfo
{
	private static Player[] players;
	private static int playersIndex = 0;
	
	public static final int maxMoves = 5;
	public static final int maxSummoned = 5;
	public static int defaultDeployable = 1;

	public static final int minGarrison = 1;
	public static final int maxGarrison = 5;
	
	public static int level = 0;

	public static int numberOfPlayers = 0;
	public static int cpuPlayers = 0;
	public static int humanPlayers = 0;

	public static int currentMapIndex = 0;

	// ======================================================
	// ======================================================	
	public static void assignPlayerFaction(int index)
	{
		
	}
	
	public static void setLevel(int lvl)
	{
		level = lvl;
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
}
