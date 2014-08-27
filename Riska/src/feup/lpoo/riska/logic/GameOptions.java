package feup.lpoo.riska.logic;

import org.andengine.util.adt.color.Color;

public class GameOptions {
	
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
			new Color(0.2f,0.2f,0.2f),			// Black
			new Color(1f,0.40f,0f)},		// Orange
		{
			new Color(1f,1f,1f),			// White
			new Color(0.2f,0.2f,0.2f)},			// Black
	};
	
	// ======================================================
	// FIELDS
	// ======================================================
	// TODO : values must be loaded from last saved values!
	public static final int maxPlayers = 5; // IMPORTANT : COLORS[][] size is depending on this number!
	public static final int minPlayers = 2;
	public static final int minHumanPlayers = 1;
	public static final int numberOfFactions = COLORS.length;
	
	public static int numberOfMaps = 1;
	public static int initialMap = 1;
	
	private static boolean music = true;
	private static boolean sfx = true;
	private static boolean menuAnimations = true;
	
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

	public static boolean musicEnabled()
	{
		return music;
	}
	
	public static void setMusicEnabled(boolean value)
	{
		music = value;
	}
	
	public static boolean sfxEnabled()
	{
		return sfx;
	}
	
	public static void setSfxEnabled(boolean value)
	{
		sfx = value;
	}
	
	public static boolean menuAnimationsEnabled()
	{
		return menuAnimations;
	}
	
	public static void setMenuAnimationsEnabled(boolean value)
	{
		if(value)
		{
			menuAnimations = true;
		}
		else
		{
			menuAnimations = false;
		}
	}
}
