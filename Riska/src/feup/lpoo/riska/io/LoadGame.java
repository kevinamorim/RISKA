package feup.lpoo.riska.io;

import feup.lpoo.riska.logic.GameLogic;
import feup.lpoo.riska.logic.GameLogic.GAME_STATE;
import feup.lpoo.riska.logic.MainActivity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class LoadGame {
	
	private SharedPreferences prefs;
	private GameLogic logic;
	
	public LoadGame(MainActivity activity) {
		prefs = PreferenceManager.getDefaultSharedPreferences(activity);	
	}
	
	public void setLogic(GameLogic logic)
	{
		this.logic = logic;
	}
	
	public boolean checkLoadGame() {
		
		if(!prefs.contains("State")) {
			return false;
		}
		
		if(!prefs.contains("CurrentPlayer")) {
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
		Log.d("Loading", "Loading");
		loadGameState();
		loadPlayerRegions();
		loadCpuRegions();
		loadSoldiers();
	}

	private void loadCpuRegions() {
		
		int size = prefs.getInt("cpuRegionsSize", 0);
		for(int i = 0; i < size; i++) {
			int id = prefs.getInt("cpuRegion_" + i, 0);

			logic.map.getRegionById(id).setOwner(logic.getPlayers().get(1));
		}
		
	}

	private void loadPlayerRegions() {
		
		int size = prefs.getInt("playerRegionsSize", 0);
		for(int i = 0; i < size; i++) {
			int id = prefs.getInt("playerRegion_" + i, 0);

			logic.map.getRegionById(id).setOwner(logic.getPlayers().get(0));

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
		
		logic.setCurrentPlayerByIndex(prefs.getInt("CurrentPlayer", 0));
	}
	
	private void loadSoldiers() {
		
		int size = prefs.getInt("totalRegions", 0);
		
		Log.d("Loading", "Loading soldiers: " + size);
		
		for(int i = 0; i < size; i++) {
			int soldiers = prefs.getInt("soldiers_" + i, 1);
			Log.d("Loading", "Soldiers: " + soldiers);
			logic.map.getRegionById(i).setSoldiers(soldiers);
		}
	}

}
