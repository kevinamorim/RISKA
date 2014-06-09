package feup.lpoo.riska.io;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import feup.lpoo.riska.elements.Player;
import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.logic.GameLogic.GAME_STATE;
import feup.lpoo.riska.logic.MainActivity;

public class SaveGame {
	
	private SharedPreferences prefs;
	
	public SaveGame(MainActivity activity, GameLogic logic) {
		
		prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		
		saveGameState(logic.getState());
		savePlayerRegions(logic.getPlayers().get(0));
		saveCpuRegions(logic.getPlayers().get(1));
		
	}

	private void saveCpuRegions(Player player) {
		
		Editor editor = prefs.edit();
		
		editor.putInt("cpuRegionsSize", player.getRegions().size());
		for(int i = 0; i < player.getRegions().size(); i++) {
			editor.putInt("cpuRegion_" + i, player.getRegions().get(i).getId());
		}
		
		editor.commit();
		
	}

	private void savePlayerRegions(Player player) {
		
		Editor editor = prefs.edit();
		
		editor.putInt("playerRegionsSize", player.getRegions().size());
		for(int i = 0; i < player.getRegions().size(); i++) {
			editor.putInt("playerRegion_" + i, player.getRegions().get(i).getId());
		}
		
		editor.commit();
		
	}

	private void saveGameState(GAME_STATE state) {
		
		Editor editor = prefs.edit();
		
		editor.putInt("State", state.ordinal());
		editor.commit();
		
	}

}
