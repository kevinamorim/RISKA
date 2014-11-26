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
	private GameLogic logic;
	
	public SaveGame(MainActivity activity, GameLogic logic) {
		
		prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		
		this.logic = logic;
		
		saveGameState(logic.getState());
		savePlayerRegions(logic.getPlayers()[0]);
		saveCpuRegions(logic.getPlayers()[0]);
		saveSoldiers();	
	}

	private void saveCpuRegions(Player player) {
		
		Editor editor = prefs.edit();
		
		editor.putInt("cpuRegionsSize", player.getRegions().size());
		for(int i = 0; i < player.getRegions().size(); i++) {
			editor.putInt("cpuRegion_" + i, player.getRegions().get(i).ID);
		}
		
		editor.commit();
		
	}

	private void savePlayerRegions(Player player) {
		
		Editor editor = prefs.edit();
		
		editor.putInt("playerRegionsSize", player.getRegions().size());
		for(int i = 0; i < player.getRegions().size(); i++) {
			editor.putInt("playerRegion_" + i, player.getRegions().get(i).ID);
		}
		
		editor.commit();
		
	}
	
	private void saveSoldiers() {
		
		Editor editor = prefs.edit();
		
		editor.putInt("totalRegions", logic.map.getNumberOfRegions());
		
		int regionsSize = logic.map.getRegions().size();
		
		for(int i = 0; i < regionsSize; i++) {
			editor.putInt("soldiers_" + i, logic.map.getRegionById(i).getSoldiers());
		}
		
		editor.commit();
		
	}

	private void saveGameState(GAME_STATE state) {
		
		Editor editor = prefs.edit();
		
		editor.putInt("State", state.ordinal());
		editor.commit();
		
		editor.putInt("CurrentPlayer", logic.getCurrentPlayerIndex());
		editor.commit();
	}

}
