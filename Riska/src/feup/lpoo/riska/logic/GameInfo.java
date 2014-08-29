package feup.lpoo.riska.logic;

import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.utilities.Utils;

public class GameInfo
{

	private static int[] playerFaction = new int[GameOptions.maxPlayers]; 
	public static boolean[] playerIsCPU = new boolean[GameOptions.maxPlayers];

	public static int level = 0;

	public static int numberOfPlayers = 0;
	public static int cpuPlayers = 0;
	public static int humanPlayers = 0;

	public static int currentMapIndex = 0;

	// ======================================================
	// ======================================================	
	public static Color[] getPlayerColors(int playerIndex)
	{
		return GameOptions.getColors(playerFaction[playerIndex]);
	}

	public static void assignPlayerFaction(int playerIndex, int factionIndex)
	{
		playerFaction[playerIndex] = factionIndex;
	}

	public static void assignPlayerFaction(int index)
	{
		int factionIndex = 0;

		while(Utils.inArray(factionIndex, playerFaction))
		{
			factionIndex++;
		}

		playerFaction[index] = factionIndex;
	}

	public static void clearFactions()
	{
		Utils.fill(playerFaction, -1);
	}
	
	public void setLevel(int lvl)
	{
		level = lvl;
	}
}
