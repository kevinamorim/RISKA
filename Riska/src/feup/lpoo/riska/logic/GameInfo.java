package feup.lpoo.riska.logic;

import org.andengine.util.adt.color.Color;

public class GameInfo
{
	// ======================================================
	// CONSTANTS
	// ======================================================
	private static final Color COLORS[][] = {
		{
			new Color(0f, 0.45f, 0.9f),		// Blue
			new Color(1f, 1f, 1f)},			// White
		{
			new Color(0.78f, 0f, 0f),		// Red
			new Color(0.9f, 0.75f, 0.30f)},	// Burned Yellow
		{
			new Color(0f,0.7f,0.16f),		// Green
			new Color(1f,1f,0.35f)},		// Light Yellow
		{
			new Color(0f,0f,0f),			// Black
			new Color(1f,0.40f,0f)},		// Orange
	};
	
	// ======================================================
	// FIELDS
	// ======================================================
	public static final int maxPlayers = 4;
	public static final int minPlayers = 2;
	
	public static int numberOfPlayers = 2;
	
	private static int[] playerFaction = new int[maxPlayers]; 
	private static boolean[] playerIsCPU = new boolean[maxPlayers];
	
	public static int currentMapIndex = 0;
	
	// ======================================================
	// ======================================================
	public static Color[] getColors(int index)
	{
		return COLORS[index];
	}
	
	public static Color getPriColor(int index)
	{
		return COLORS[index][0];
	}
	
	public static Color getSecColor(int index)
	{
		return COLORS[index][1];
	}
	
	public static Color[] getPlayerColors(int playerIndex)
	{
		return COLORS[playerFaction[playerIndex]];
	}
	
	public static int getNumberOfColors()
	{
		return COLORS.length;
	}

	public static void assignPlayerFaction(int currentPlayer, int selectedFactionIndex)
	{
		playerFaction[currentPlayer] = selectedFactionIndex;
		playerIsCPU[currentPlayer] = false;
	}

	public static void assignRemainingFactions(int currentPlayer)
	{
		for(int i = currentPlayer + 1; i < numberOfPlayers; i++)
		{
			int factionIndex = 0;
			while(inArray(factionIndex, playerFaction))
			{
				factionIndex++;
			}
			playerFaction[i] = factionIndex;
			playerIsCPU[i] = true;
		}
	}

	private static boolean inArray(int factionIndex, int[] playerFaction)
	{
		for(int i = 0; i < playerFaction.length; i++)
		{
			if(playerFaction[i] == factionIndex)
			{
				return true;
			}
		}
		
		return false;
	}

	public static void clearFactions()
	{
		for(int i = 0; i < playerFaction.length; i++)
		{
			playerFaction[i] = -1;
		}
	}

	public static boolean getPlayerType(int i) {
		return playerIsCPU[i];
	}
	
}
