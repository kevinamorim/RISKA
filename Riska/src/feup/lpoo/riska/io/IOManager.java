package feup.lpoo.riska.io;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import feup.lpoo.riska.logic.GameOptions;
import feup.lpoo.riska.logic.MainActivity;

public class IOManager {
	
	private static SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getSharedInstance());
	
	public static void saveGameOptions()
	{
		Editor editor = prefs.edit();
		
		editor.putBoolean("Music", GameOptions.musicEnabled());
		editor.putBoolean("SFX", GameOptions.sfxEnabled());
		editor.putBoolean("MenuAnimations", GameOptions.animationsEnabled());
		
		editor.commit();
	}
	
	public static void loadGameOptions()
	{	
		GameOptions.setMusicEnabled(prefs.getBoolean("Music", true));
		GameOptions.setSfxEnabled(prefs.getBoolean("SFX", true));
		GameOptions.setAnimationsEnabled(prefs.getBoolean("MenuAnimations", false /* TODO : change this to true, debug only*/));
	}
}
