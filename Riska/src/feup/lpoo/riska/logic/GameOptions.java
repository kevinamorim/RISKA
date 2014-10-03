package feup.lpoo.riska.logic;

import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.utilities.Utils;

public class GameOptions {
	
	// ======================================================
	// CONSTANTS
	// ======================================================
	private static final Color COLORS[][] = {
		{
			Utils.OtherColors.CYAN,
			Utils.OtherColors.WHITE},
		{
			Utils.OtherColors.DARK_RED,
			Utils.OtherColors.DARK_YELLOW},
		{
			Utils.OtherColors.GREEN,
			Utils.OtherColors.YELLOW},
		{
			Utils.OtherColors.ORANGE,
			Utils.OtherColors.DARK_GREY},
		{
			Utils.OtherColors.WHITE,
			Utils.OtherColors.DARK_GREY},
	};
	
	// ======================================================
	// FIELDS
	// ======================================================
	// TODO : values must be loaded from last saved values!
	public static final int maxPlayers = 5; // IMPORTANT : COLORS[][] size is depending on this number!
	public static final int minPlayers = 2;
	public static final int minHumanPlayers = 1;
	public static final int numberOfFactions = COLORS.length;
	public static final int numberOfColors = COLORS.length;
	
	public static int numberOfLevels = 4;
	public static int defaultLvlIndex = 0;
	
	public static int numberOfMaps = 1;
	public static int defaultMapIndex = 0;
	
	private static boolean music = true;
	private static boolean sfx = true;
	private static boolean menuAnimations = true;
	
	private static final float defaultAnimationTime = 0.21f;

	public static float animationTime = defaultAnimationTime;
	
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
			animationTime = defaultAnimationTime;
		}
		else
		{
			menuAnimations = false;
			animationTime = 0f;
		}
	}

	public static String getLevelDescr(int lvl)
	{
		switch(lvl)
		{
		case 0:
			return "Normal";

		case 1:
			return "Hard";

		case 2:
			return "Very Hard";

		case 3:
			return "Master";

		default:
			return "Honestly? No difficulty at all...";
		}
	}
	
}
