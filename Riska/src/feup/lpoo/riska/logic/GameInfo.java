package feup.lpoo.riska.logic;

import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.utilities.Utils;

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
		{
			new Color(1f,1f,1f),			// White
			new Color(0f,0f,0f)},			// Black
	};
	
	// ======================================================
	// FIELDS
	// ======================================================
	public static final int maxPlayers = 5; // IMPORTANT : COLORS[][] size is depending on this number!
	public static final int minPlayers = 2;
	public static final int minHumanPlayers = 1;
	public static final int numberOfFactions = COLORS.length;
	
	private static int[] playerFaction = new int[maxPlayers]; 
	public static boolean[] playerIsCPU = new boolean[maxPlayers];
	
	public static int numberOfPlayers = 0;
	public static int cpuPlayers = 0;
	public static int humanPlayers = 0;
	
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

	public static void assignPlayerFaction(int index, int factionIndex)
	{
		playerFaction[index] = factionIndex;
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
}