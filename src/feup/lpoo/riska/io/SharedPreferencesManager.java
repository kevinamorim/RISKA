package feup.lpoo.riska.io;

import feup.lpoo.riska.logic.MainActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPreferencesManager {
	
	public static void SaveBoolean(String key, boolean value) {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.instance);
		Editor editor = prefs.edit();
		
		editor.putBoolean(key, value);
		
		editor.commit();
		
	}
	
	public static boolean LoadBoolean(String key) {
		
		boolean defaultValue = true;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.instance);
		
		return prefs.getBoolean(key, defaultValue);
		
	}

}
