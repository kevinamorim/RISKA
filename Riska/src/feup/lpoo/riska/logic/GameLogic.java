package feup.lpoo.riska.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import org.andengine.util.adt.color.Color;

import android.util.Log;
import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Player;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.generator.BattleGenerator;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.GameScene;
import feup.lpoo.riska.scenes.SceneManager;

public class GameLogic {

	// ======================================================
	// SINGLETONS
	// ======================================================
	private ResourceCache resources;
	private BattleGenerator battleGenerator;

	// ======================================================
	// CONSTANTS
	// ======================================================
	private final int WIN_BONUS = 2;
	private final int TURN_REPLENISHMENT = 3;
	public final int MIN_SOLDIERS_PER_REGION = 1;
	private final int MIN_SOLDIERS_FOR_AN_ATTACK = 2;
	private final int MIN_PLAYERS_IN_GAME = 2;
	private final int SOLDIER_INC = 1;
	private Color[] PLAYER_COLOR = { new Color(0f, 0.447f, 0.898f), new Color(1f, 1f, 0f) };
	private Color[] CPU_COLOR = { new Color(1f, 0f, 0f), new Color(0f, 0f, 0f) };

	public enum GAME_STATE { PAUSED, SETUP,  DEPLOYMENT, ATTACK, MOVE, CPU, PLAY, GAMEOVER };

	// ======================================================
	// FIELDS
	// ======================================================
	public Map map;
	
	private ArrayList<Player> players;
	private GAME_STATE state;
	private Player currentPlayer;
	private GameScene gameScene;
	private boolean attackDone;
	private boolean turnDone;
	public Region selectedRegion;
	public Region targetedRegion;

	public int attackingSoldiers = MIN_SOLDIERS_PER_REGION;
	public int defendingSoldiers = MIN_SOLDIERS_PER_REGION;
	
	// ======================================================
	// ======================================================

	public GameLogic(GameScene scene) {

		resources = ResourceCache.getSharedInstance();
		SceneManager.getSharedInstance();

		gameScene = scene;

		createsPlayers();
		createMap();

		state = GAME_STATE.SETUP;
		attackDone = false;
		turnDone = false;

		battleGenerator = new BattleGenerator();
	}

	private void createsPlayers() {

		players = new ArrayList<Player>();

		int INITIAL_SOLDIERS_TO_DEPLOY = 38;

		Player player = new Player(false, PLAYER_COLOR[0], PLAYER_COLOR[1], "PLAYER");
		player.setSoldiersToDeploy(INITIAL_SOLDIERS_TO_DEPLOY);

		Player cpu = new Player(true, CPU_COLOR[0], CPU_COLOR[1], "CPU");
		cpu.setSoldiersToDeploy(INITIAL_SOLDIERS_TO_DEPLOY);

		players.add(player);
		players.add(cpu);

		currentPlayer = players.get(0);	
	}

	private void createMap() {
		map = resources.map;

		map.handOutRegions(players);

		verifiyNumberOfSoldiersInRegions();
	}

	// ======================================================
	// ======================================================

	public void update() {

		if(gameOver())
		{
			state = GAME_STATE.GAMEOVER;
		}
		else
		{
			if(turnDone)
			{	
				for(Player player : players)
				{
					Log.d("Regions","Player " + player.getName() + " has " + player.getRegions().size() + " regions out of " + map.getNumberOfRegions());
				}

				if(selectedRegion != null)
				{
					unselectRegion();
				}
				if(targetedRegion != null)
				{
					untargetRegion();
				}

				if(!getNextPossiblePlayer())
				{
					state = GAME_STATE.GAMEOVER;
					return;
				}
				else
				{
					attackDone = false;
					turnDone = false;
					gameScene.setInitialHUD();
					state = GAME_STATE.PLAY;
				}
			}
			else if(attackDone)
			{
				state = GAME_STATE.DEPLOYMENT;
			}
			else
			{
				if(currentPlayer.isCPU)
				{
					pauseGame();
					automaticMove();
				}
			}
		}	
	}

	public void setup()
	{
		if(!currentPlayer.hasSoldiersLeftToDeploy()) /* If player has deployed all of his soldiers. */
		{
			currentPlayer = getNextPlayer();

			if(currentPlayer.equals(players.get(0))) /* If all players have deployed their soldiers. */
			{
				gameScene.setInitialHUD();
				state = GAME_STATE.PLAY;
			}
			else
			{
				if(currentPlayer.isCPU)
				{
					currentPlayer.deployAllSoldiers();
				}
			}
		}

	}

	public void attack()
	{
		boolean attackerWon;

		if(selectedRegion == null || targetedRegion == null)
		{
			return;
		}

		Region pRegion1 = selectedRegion;
		Region pRegion2 = targetedRegion;

		battleGenerator.simulateAttack(attackingSoldiers, defendingSoldiers);

		attackerWon = battleGenerator.result;

		gameScene.showBattleScene(selectedRegion, targetedRegion, battleGenerator);

		if(attackerWon)
		{
			pRegion1.setSoldiers(Math.max(MIN_SOLDIERS_PER_REGION, pRegion1.getNumberOfSoldiers() - attackingSoldiers));
			pRegion2.setOwner(currentPlayer);
			pRegion2.setSoldiers(Math.max(MIN_SOLDIERS_PER_REGION, battleGenerator.remainingAttackers));

			// If the player wins an attack, he/she gets a bonus of soldiers to deploys next turn
			currentPlayer.setSoldiersToDeploy(WIN_BONUS * TURN_REPLENISHMENT);
		}
		else
		{
			pRegion1.setSoldiers(Math.max(MIN_SOLDIERS_PER_REGION, pRegion1.getNumberOfSoldiers() - attackingSoldiers + battleGenerator.remainingAttackers));
			pRegion2.setSoldiers(Math.max(MIN_SOLDIERS_PER_REGION, battleGenerator.remainingDefenders));

			currentPlayer.setSoldiersToDeploy(TURN_REPLENISHMENT);
		}

		untargetRegion();
		unselectRegion();

		attackDone = true;
	}

	public void deploy() {

		if(currentPlayer.hasSoldiersLeftToDeploy() && currentPlayer.isCPU)
		{
			currentPlayer.deployAllSoldiers();
		}

		if(!currentPlayer.hasSoldiersLeftToDeploy())
		{
			//state = GAME_STATE.ATTACK;
			state = GAME_STATE.PLAY;
			turnDone = true;
		}

	}

	public void move() {

	}

	public void cpu() {

	}

	// ======================================================
	// GAME STATE
	// ======================================================

	public GAME_STATE getState()
	{
		return state;
	}

	public void setState(GAME_STATE state) {
		this.state = state;
	}

	private boolean gameOver()
	{
		int remainingPlayers = players.size();

		for(Player player : players)
		{
			if(player.getRegions().size() <= 0)
			{
				remainingPlayers--;
			}
		}

		return (remainingPlayers < MIN_PLAYERS_IN_GAME);

	}
	
	// ======================================================
	// PLAYERS
	// ======================================================

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public int getCurrentPlayerIndex() {
		return players.indexOf(currentPlayer);
	}

	public void setCurrentPlayer(Player currentPlayer)
	{
		this.currentPlayer = currentPlayer;
	}

	public Player getNextPlayer() {

		int index = getCurrentPlayerIndex();

		return players.get((index + 1) % players.size());
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setCurrentPlayerByIndex(int index)
	{
		this.setCurrentPlayer(players.get(index));
	}

	private boolean getNextPossiblePlayer()
	{
		int count = 0;

		currentPlayer = getNextPlayer();

		while(count < players.size())
		{
			if(hasPossibleMoves(currentPlayer))
			{
				return true;
			}
			else
			{
				currentPlayer = getNextPlayer();
				count++;
			}
		}

		return false;
	}

	private void automaticMove()
	{	
		Region pRegion1 = selectRegion(currentPlayer);

		if(pRegion1 != null)
		{
			Region pRegion2 = selectTargetRegion(pRegion1);

			if(pRegion2 != null)
			{
				gameScene.showCpuMove(pRegion1, pRegion2);	
			}
		}		
	}

	private boolean hasPossibleMoves(Player player)
	{
		for(Region pRegion : player.getRegions())
		{
			if(hasEnemyNeighbor(pRegion) && canAttack(pRegion))
			{
				//Log.d("Region", "True" + pRegion.getName());
				return true;
			}
		}

		return false;
	}

	// ======================================================
	// REGIONS RELATED
	// ======================================================
	public void onRegionTouched(Region pRegion)
	{
		if(!currentPlayer.isCPU) // some kind of bug
		{
			switch(state)
			{
			case SETUP:
				currentPlayer.deploy(SOLDIER_INC, pRegion);
				break;
			case DEPLOYMENT:
				currentPlayer.deploy(SOLDIER_INC, pRegion);
				break;
			case PLAY:
				onPlayHandler(pRegion);
				break;

			default:
				break;
			}
		}
	}

	private void onPlayHandler(Region pRegion)
	{
		if(!pRegion.isFocused())
		{
			if(pRegion.hasOwner(currentPlayer))
			{
				if(canAttack(pRegion))
				{
					selectRegion(pRegion);
					gameScene.showOnlyNeighbourRegions(pRegion);
				}
			}
			else
			{
				targetRegion(pRegion);
			}
		}
		else
		{
			if(pRegion.hasOwner(currentPlayer))
			{
				unselectRegion();
			}
			else
			{
				untargetRegion();
			}

		}	
	}

	private Region selectTargetRegion(Region pRegion1)
	{
		if(hasEnemyNeighbor(pRegion1))
		{
			ArrayList<Region> neighbours = getEnemyNeighbours(pRegion1);
			Random r = new Random();
			int i = r.nextInt(neighbours.size());
			return neighbours.get(i);
		}

		return null;
	}

	private void verifiyNumberOfSoldiersInRegions()
	{
		for(Region region : map.getRegions())
		{
			region.setSoldiers(Math.max(MIN_SOLDIERS_PER_REGION, region.getNumberOfSoldiers()));
		}
	}

	private ArrayList<Region> getEnemyNeighbours(Region pRegion)
	{
		ArrayList<Region> result = new ArrayList<Region>();

		for(Region neighbour : pRegion.getNeighbours())
		{
			if(!neighbour.getOwner().equals(pRegion.getOwner()))
			{
				result.add(neighbour);
			}
		}

		return result;
	}

	private boolean hasEnemyNeighbor(Region pRegion)
	{
		for(Region neighbour : pRegion.getNeighbours())
		{
			if(!neighbour.getOwner().equals(pRegion.getOwner()))
			{
				return true;
			}
		}

		return false;
	}

	private boolean canAttack(Region pRegion)
	{
		return pRegion.getNumberOfSoldiers() >= MIN_SOLDIERS_FOR_AN_ATTACK;
	}

	private Region selectRegion(Player player) {
		Collections.sort(player.getRegions(), new Comparator<Region>()
				{
			@Override
			public int compare(Region region1, Region region2)
			{
				return region1.getNumberOfSoldiers() - region2.getNumberOfSoldiers();
			}
				});

		for(Region pRegion : player.getRegions())
		{
			if(hasEnemyNeighbor(pRegion) && canAttack(pRegion))
			{
				return pRegion;
			}
		}

		return null;
	}

	public void selectRegion(Region pRegion) {

		// Shouldn't happen. 
		if(selectedRegion != null)
		{
			unselectRegion();
		}

		attackingSoldiers = pRegion.getNumberOfSoldiers() - MIN_SOLDIERS_PER_REGION;

		selectedRegion = pRegion;
		selectedRegion.focus();
	}

	public void targetRegion(Region pRegion)
	{
		if(selectedRegion != null)
		{
			if(pRegion.isNeighbourOf(selectedRegion))
			{

				if(targetedRegion != null)
				{
					untargetRegion();
				}
				defendingSoldiers = pRegion.getNumberOfSoldiers();

				targetedRegion = pRegion;
				targetedRegion.focus();
			}
		}
	}

	private void unselectRegion()
	{
		if(targetedRegion != null)
		{
			untargetRegion();
		}

		selectedRegion.unfocus();
		selectedRegion = null;
	}

	private void untargetRegion() 
	{
		targetedRegion.unfocus();	
		targetedRegion = null;
	}

	public void pauseGame()
	{
		state = GAME_STATE.PAUSED;
	}

	public void resumeGame()
	{
		if(state.equals(GAME_STATE.PAUSED)) state = GAME_STATE.PLAY;
	}
}
