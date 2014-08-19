package feup.lpoo.riska.logic;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;

import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Player;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.generator.BattleGenerator;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.GameScene;

public class GameLogic
{
	// ======================================================
	// SINGLETONS
	// ======================================================
	private BattleGenerator battleGenerator;

	// ======================================================
	// CONSTANTS
	// ======================================================
	public final int MIN_SOLDIERS_PER_REGION = 1;
	private final int WIN_BONUS = 2;
	private final int TURN_REPLENISHMENT = 3;
	private final int MIN_PLAYERS_IN_GAME = 2;
	private final int SOLDIER_INC = 1;

	public enum GAME_STATE { PAUSED, SETUP,  DEPLOYMENT, ATTACK, MOVE, CPU, PLAY, GAMEOVER };

	// ======================================================
	// FIELDS
	// ======================================================
	public Map map;

	private ArrayList<Player> players;
	private GAME_STATE state;
	private GAME_STATE tempState;
	private Player currentPlayer;
	private GameScene gameScene;

	public Region selectedRegion;
	public Region targetedRegion;

	public int attackingSoldiers;
	public int defendingSoldiers;
	public int movingSoldiers;

	// ======================================================
	// ======================================================

	public GameLogic(GameScene scene)
	{
		gameScene = scene;

		createsPlayers();
		createMap();

		state = GAME_STATE.SETUP;

		battleGenerator = new BattleGenerator();

		attackingSoldiers = MIN_SOLDIERS_PER_REGION;
		defendingSoldiers = MIN_SOLDIERS_PER_REGION;
		movingSoldiers = 0;
	}

	private void createsPlayers()
	{
		players = new ArrayList<Player>();

		int INITIAL_SOLDIERS_TO_DEPLOY = 38;

		for(int i = 0; i < GameInfo.numberOfPlayers; i++)
		{
			//Log.d("Riska", "[GameLogic] Player " + i + (GameInfo.playerIsCPU[i] ? " is " : " is not ") + "CPU");

			Player player = new Player(GameInfo.playerIsCPU[i], GameInfo.getPlayerColors(i), "PLAYER " + i);
			player.setSoldiersToDeploy(INITIAL_SOLDIERS_TO_DEPLOY);

			players.add(player);
		}

		currentPlayer = players.get(0);	
	}

	private void createMap()
	{
		map = ResourceCache.getSharedInstance().maps.get(GameInfo.currentMapIndex);

		map.handOutRegions(players);
		map.initRegions();
	}

	// ======================================================
	// ======================================================
	private void nextTurn() {
		
		if(!getNextPossiblePlayer()) {
			state = GAME_STATE.GAMEOVER;
		} else {
			unselectRegion();
			untargetRegion();
			gameScene.setInitialHUD();
		}
		
		/* TODO: Not here. Just testing.(temporary) */
		if(currentPlayer.isCPU) {
			pauseGame();
			automaticMove();
		}
		
	}

	private void setNextState() {
		
		Log.d("debug", "STATE: " + state);
		
		if(gameOver()) {
			state = GAME_STATE.GAMEOVER;
		}
		
		switch(state) {
		case SETUP:
			state = GAME_STATE.PLAY; /* Change to attack */
			break;
		case PLAY: /* Change to attack */
			state = GAME_STATE.MOVE;
			break;
		case MOVE:
			state = GAME_STATE.DEPLOYMENT;
			break;
		case DEPLOYMENT:
			state = GAME_STATE.PLAY; /* Change to attack */
			break;
		case PAUSED:
			state = GAME_STATE.PLAY;
			break;
		default:
			break;
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
				setNextState();
			}
			else
			{
				if(currentPlayer.isCPU)
				{
					currentPlayer.deployAllSoldiers();
					setup();
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
		
		setNextState();
		gameScene.showBattleScene(selectedRegion, targetedRegion, battleGenerator);

		untargetRegion();
		unselectRegion();	
		
	}

	public void deploy() {

		if(currentPlayer.hasSoldiersLeftToDeploy() && currentPlayer.isCPU)
		{
			currentPlayer.deployAllSoldiers();
		}

		if(!currentPlayer.hasSoldiersLeftToDeploy())
		{
			nextTurn();
			setNextState();
		}
	}

	public void move() {

		if(selectedRegion != null && targetedRegion != null) {
			Region pRegion1 = selectedRegion;
			Region pRegion2 = targetedRegion;

			pRegion1.setSoldiers(pRegion1.getNumberOfSoldiers() - movingSoldiers);
			pRegion2.setSoldiers(pRegion2.getNumberOfSoldiers() + movingSoldiers);

			untargetRegion();
			unselectRegion();
			
			setNextState();
		}

	}

	public void cpu()
	{
		pauseGame();
		automaticMove();
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

	public boolean isPaused() {
		return state == GAME_STATE.PAUSED;
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
			if(currentPlayer.hasPossibleMoves())
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
		Region pRegion1 = currentPlayer.pickRegion();

		if(pRegion1 != null)
		{
			Region pRegion2 = selectTargetRegion(pRegion1);

			if(pRegion2 != null)
			{
				gameScene.showCpuMove(pRegion1, pRegion2);	
			}
		}		
	}


	// ======================================================
	// REGIONS RELATED
	// ======================================================
	public void onRegionTouched(Region pRegion)
	{
		if(!currentPlayer.isCPU)
		{
			switch(state)
			{

			case SETUP:
				deploySoldiers(pRegion);

				break;

			case DEPLOYMENT:
				deploySoldiers(pRegion);
				break;

			case PLAY:
				onPlayHandler(pRegion);
				break;

			case MOVE:
				onMoveHandler(pRegion);
				break;

			default:
				break;
			}
		}
	}

	private void onMoveHandler(Region pRegion) {
		
		if(!pRegion.isFocused()) {
			if(pRegion.hasOwner(currentPlayer)) {
				if(this.selectedRegion == null) {
					if(pRegion.canAttack()) { /* Using canAttack because the criteria is the same */
						selectRegion(pRegion);
						gameScene.showOnlyPlayerNeighbourRegions(pRegion);
					}

				} else {
					targetRegion(pRegion);
				}
			}
		} else {
			if(pRegion.ID == this.selectedRegion.ID) {
				unselectRegion();
			} else {
				untargetRegion();
			}
		}
		
	}

	private void deploySoldiers(Region pRegion)
	{
		if(pRegion.hasOwner(currentPlayer))
		{
			currentPlayer.deploy(SOLDIER_INC, pRegion);
		}	
	}

	private void onPlayHandler(Region pRegion)
	{
		if(!pRegion.isFocused())
		{
			if(pRegion.hasOwner(currentPlayer))
			{
				if(pRegion.canAttack())
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
		if(pRegion1.hasEnemyNeighbor())
		{
			ArrayList<Region> neighbours = getEnemyNeighbours(pRegion1);
			Random r = new Random();
			int i = r.nextInt(neighbours.size());
			return neighbours.get(i);
		}

		return null;
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

		if(selectedRegion != null)
		{
			selectedRegion.unfocus();
			selectedRegion = null;
		}
	}

	private void untargetRegion() 
	{
		if(targetedRegion != null)
		{
			targetedRegion.unfocus();	
			targetedRegion = null;
		}	
	}

	public void pauseGame()
	{
		tempState = state;
		state = GAME_STATE.PAUSED;
	}

	public void resumeGame()
	{
		if(state.equals(GAME_STATE.PAUSED)) state = tempState;
	}
}
