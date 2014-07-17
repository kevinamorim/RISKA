package feup.lpoo.riska.logic;

import java.util.ArrayList;
import java.util.Random;
import org.andengine.util.adt.color.Color;
import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Player;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.generator.BattleGenerator;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.SceneManager;

public class GameLogic {
	
	// ======================================================
	// SINGLETONS
	// ======================================================
	//private MainActivity activity;
	private ResourceCache resources;
	private SceneManager sceneManager;
	
	// ======================================================
	// CONSTANTS
	// ======================================================
	private final int MIN_PLAYERS_IN_GAME = 2;
	private final float BONUS_FACTOR = 1.0f;
	private final float CPU_DELAY = 1.0f;
	
	public enum GAME_STATE {
		
		PAUSED,
		DEPLOYMENT,
		PLAY,
		GAMEOVER
		
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
	
	public boolean turnDone;
	
	public GameLogic() {
		
		//activity = MainActivity.getSharedInstance();
		resources = ResourceCache.getSharedInstance();
		sceneManager = SceneManager.getSharedInstance();
		
		map = resources.getMap();
		
		createsPlayers();
		
		/* Distributes regions equally between each player */
		handOutRegions();
		
		state = GAME_STATE.PAUSED;
		turnDone = false;
		
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
				sceneManager.getGameScene().setInfoTabToChooseOwnRegion();
				sceneManager.getGameScene().hideAutoDeploy();
				state = GAME_STATE.PLAY;
			} else {
				
				if(currentPlayer.isCPU()) {
					sceneManager.getGameScene().setInfoTabText("Waiting for deployment...");
					currentPlayer.deploy();
				}
				
			}
			
			
		}
		
	}

	public void updateGame() {
				
		if(checkGameOver()) {
			
			state = GAME_STATE.GAMEOVER;
			
		} else {
			
			if(turnDone) {
				currentPlayer = getNextPlayer();
				turnDone = false;
			}
			
			if(!currentPlayer.hasPossibleMoves()) {		
				currentPlayer = getNextPlayer();
			}
			
			if(currentPlayer.isCPU()) {
				automaticMove();
			}
			
		}	
		
	}
	
	private void automaticMove() {
		
		Region region1 = currentPlayer.selectRegion();
		
		if(region1 != null) {
			
			Region region2 = region1.selectTargetRegion();
			
			if(region2 != null) {
				sceneManager.getGameScene().simulateCPU(CPU_DELAY, region1, region2);
			}

		}
	
	}
	
	private boolean checkGameOver() {
		
		int remainingPlayers = players.size();
		
		for(Player player : players) {
			if(player.getRegions().size() == 0) {
				remainingPlayers--;
			}
		}
		
		return (remainingPlayers < MIN_PLAYERS_IN_GAME);
		
	}

	public void attack(Region region1, Region region2) {
		
		BattleGenerator battleGenerator = new BattleGenerator();
		
		boolean won = battleGenerator.simulateAttack(region1.getSoldiers(), region2.getSoldiers());
		
		sceneManager.getGameScene().showBattleScene(won);
				
		if(won) {
			attackerWon(region1, region2);
		} else {
			defenderWon(region1, region2);
		}			
	}
	
	public void attackerWon(Region region1, Region region2) {
		
		region2.changeOwner(region1.getOwner());
		region2.addSoldiers(region1.getNumberOfSoldiers());
		region1.clearSoldiers();
		region1.addSoldiers(1);
		
		region1.unfocus();
		
	}
	
	public void defenderWon(Region region1, Region region2) {
		
		region1.clearSoldiers();
		region1.addSoldiers(1);
		
		region1.unfocus();
		region2.unfocus();
		
	}
	
	public void autoDeployment(Player player) {
		player.deploy();
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
	
	public ArrayList<Player> getPlayers() {
		return players;
	}

}
