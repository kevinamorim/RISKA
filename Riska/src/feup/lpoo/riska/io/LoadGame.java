package feup.lpoo.riska.io;

import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.logic.GameLogic.GAME_STATE;
import feup.lpoo.riska.logic.MainActivity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LoadGame {
	
	private SharedPreferences prefs;
	private GameLogic logic;
	
	public LoadGame(MainActivity activity, GameLogic logic) {
		
		this.logic = logic;
		
		prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		
	}
	
	public boolean checkLoadGame() {
		
		if(!prefs.contains("State")) {
			return false;
		}
		
		if(!prefs.contains("playerRegionsSize")) {
			return false;
		}
		
		if(!prefs.contains("cpuRegionsSize")) {
			return false;
		}
		
		return true;
	}
	
	public void load() {
		loadGameState();
		loadPlayerRegions();
		loadCpuRegions();
	}

	private void loadCpuRegions() {
		
		int size = prefs.getInt("cpuRegionsSize", 0);
		for(int i = 0; i < size; i++) {
			logic.getPlayers().get(1).addRegion(logic.getMap()
					.getRegionById(prefs.getInt("cpuRegion_" + i, 0)));
		}
		
	}

	private void loadPlayerRegions() {
		
		int size = prefs.getInt("playerRegionsSize", 0);
		for(int i = 0; i < size; i++) {
			logic.getPlayers().get(0).addRegion(logic.getMap()
					.getRegionById(prefs.getInt("playerRegion_" + i, 0)));
		}
		
	}

	private void loadGameState() {
		
		int tmp = prefs.getInt("State", 0);
		
		GAME_STATE state = GAME_STATE.PAUSED;
		switch(tmp) {
		case 0:
			state = GAME_STATE.PAUSED;
			break;
		case 1:
			state = GAME_STATE.DEPLOYMENT;
			break;
		case 2:
			state = GAME_STATE.PLAY;
			break;
		default:
			break;
		}
		

		logic.setState(state);
		
		
	}

}
