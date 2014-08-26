package feup.lpoo.riska.logic;

public class GameOptions {
	
	public static int numberOfMaps = 1;
	public static int initialMap = 1;
	
	// TODO : values must be loaded from last saved values!
	private static boolean music = true;
	private static boolean sfx = true;
	private static boolean menuAnimations = true;

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
