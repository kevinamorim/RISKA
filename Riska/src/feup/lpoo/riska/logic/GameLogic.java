package feup.lpoo.riska.logic;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Player;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.SceneManager;

public class GameLogic {
	
	private MainActivity activity;
	private ResourceCache resources;
	private SceneManager sceneManager;
	
	// ======================================================
	// CONSTANTS
	// ======================================================
	private final float BONUS_FACTOR = 0.1f;
	private static final int INITIAL_SOLDIERS_IN_REGION = 1;
	
	public enum GAME_STATE {
		
		PAUSED,
		DEPLOYMENT,
		PLAY
		
	};
	
	private Color[] PLAYER_COLOR = { new Color(0f, 0.6f, 0f), new Color(1f, 1f, 0f) };
	private Color[] CPU_COLOR = { new Color(1f, 0f, 0f), new Color(1f, 1f, 0f) };


	// ======================================================
	// FIELDS
	// ======================================================
	private ArrayList<Player> players;
	private Map map;
	private GAME_STATE state;

	private Player currentPlayer;
	
	
	public GameLogic() {
		
		activity = MainActivity.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		sceneManager = SceneManager.getSharedInstance();
		
		map = resources.getMap();
		
		createsPlayers();
		
		/* Distributes regions equally between each player */
		handOutRegions();
		
		state = GAME_STATE.PAUSED;
		
	}
	
	private void createsPlayers() {
		
		players = new ArrayList<Player>();
		
		int MAX_SOLDIERS_TO_DEPLOY = (int) (map.getNumberOfRegions() * BONUS_FACTOR);
		
		Player player = new Player(false, PLAYER_COLOR[0], PLAYER_COLOR[1]);
		player.setSoldiersToDeploy(MAX_SOLDIERS_TO_DEPLOY);
		
		Player cpu = new Player(true, CPU_COLOR[0], CPU_COLOR[1]);
		cpu.setSoldiersToDeploy(MAX_SOLDIERS_TO_DEPLOY);
		
		players.add(player);
		players.add(cpu);
		
		currentPlayer = players.get(0);
		
	}

	private void handOutRegions() {
		
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		
		Random random = new Random();
		
		while(indexes.size() < map.getNumberOfRegions()) {
			
			int index = random.nextInt(map.getNumberOfRegions());
			
			if(!indexes.contains(index)) {
				indexes.add(index);
			}
			
		}
		
		int i = 0;
		
		for(Integer index : indexes) {
			
			Region region = map.getRegions().get(index);
			Player player = players.get(i);
			
			player.addRegion(region);
			
			region.addSoldiers(1);
			region.updateSoldiers();
			
			region.setOwner(player);
			region.setColors(player.getPrimaryColor(), player.getScondaryColor());
			
			i++;
			i = i % players.size(); 
		}
	}
	
	public void updateDeployment() {
		
		
		if(!currentPlayer.hasSoldiersLeftToDeploy()) { /* If player has deployed all of his soldiers. */
			
			currentPlayer = getNextPlayer();
			
			if(currentPlayer.equals(players.get(0))) { /* If all players have deployed their soldiers. */
				state = GAME_STATE.PLAY;
			} else {
				
				if(currentPlayer.isCPU()) {
					sceneManager.getGameScene().setInfoTabText("Waiting for deployment...");
					currentPlayer.deploy();
				}
				
			}
			
			
		}
		
	}

	public void attack(Region region1, Region region2) {
		
		
	}
	
	// ======================================================
	// GETTERS & SETTERS
	// ======================================================

	public GAME_STATE getState() {
		return state;
	}

	public void setState(GAME_STATE state) {
		this.state = state;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	
	private Player getNextPlayer() {
		
		int i = players.indexOf(currentPlayer);
		
		if(i < players.size() - 1) {
			return players.get(i + 1);
		} else {
			return players.get(0);
		}
		
		
	}
	

}
