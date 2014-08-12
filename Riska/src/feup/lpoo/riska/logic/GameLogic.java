package feup.lpoo.riska.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

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
	private boolean movingDone;
	private boolean turnDone;
	
	public Region selectedRegion;
	public Region targetedRegion;

	public int attackingSoldiers;
	public int defendingSoldiers;
	public int movingSoldiers;
	
	// ======================================================
	// ======================================================

	public GameLogic(GameScene scene)
	{
		resources = ResourceCache.getSharedInstance();

		gameScene = scene;

		createsPlayers();
		createMap();

		state = GAME_STATE.SETUP;
		attackDone = false;
		movingDone = false;
		turnDone = false;

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
		 // TODO : maps
		map = resources.maps.get(GameInfo.currentMapIndex);

		map.handOutRegions(players);

		verifiyNumberOfSoldiersInRegions();
	}

	// ======================================================
	// ======================================================

	public void update()
	{

		if(gameOver())
		{
			state = GAME_STATE.GAMEOVER;
		}
		else
		{
			if(turnDone)
			{
				if(!getNextPossiblePlayer())
				{
					state = GAME_STATE.GAMEOVER;
					return;
				}
				else
				{
					attackDone = false;
					movingDone = false;
					turnDone = false;
					
					unselectRegion();
					untargetRegion();
					gameScene.setInitialHUD();
					state = GAME_STATE.PLAY;
				}
			}
			else if(movingDone)
			{
				state = GAME_STATE.DEPLOYMENT;	
			} else if(attackDone) {
				state = GAME_STATE.MOVE;
			}
			else
			{
				if(currentPlayer.isCPU)
				{
					cpu();
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
		
		/* TODO: Temporary */
		if(currentPlayer.isCPU) {
			turnDone = true;
		}
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
		
		if(selectedRegion != null && targetedRegion != null) {
			Region pRegion1 = selectedRegion;
			Region pRegion2 = targetedRegion;
			
			pRegion1.setSoldiers(pRegion1.getNumberOfSoldiers() - movingSoldiers);
			pRegion2.setSoldiers(pRegion2.getNumberOfSoldiers() + movingSoldiers);
			
			untargetRegion();
			unselectRegion();
			state = GAME_STATE.DEPLOYMENT;
			movingDone = true;
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
					selectRegion(pRegion);
					gameScene.showOnlyPlayerNeighbourRegions(pRegion);
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
		state = GAME_STATE.PAUSED;
	}

	public void resumeGame()
	{
		if(state.equals(GAME_STATE.PAUSED)) state = GAME_STATE.PLAY;
	}
}
