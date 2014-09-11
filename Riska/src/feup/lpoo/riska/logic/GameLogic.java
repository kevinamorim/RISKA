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

	public enum GAME_STATE { PAUSED, SETUP,  DEPLOYMENT, ATTACK, MOVE, CPU, PLAY, ENDTURN, GAMEOVER };

	// ======================================================
	// FIELDS
	// ======================================================
	public Map map;

	private Player[] players;
	private GAME_STATE state;
	private GAME_STATE tempState;

	private GameScene gameScene;
	
	private Player currentPlayer;
	private int currentPlayerIndex;
	
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

		players = GameInfo.players();
		currentPlayer = players[0];

		createMap();

		state = GAME_STATE.SETUP;

		battleGenerator = new BattleGenerator();

		attackingSoldiers = MIN_SOLDIERS_PER_REGION;
		defendingSoldiers = MIN_SOLDIERS_PER_REGION;
		movingSoldiers = 0;
	}

	private void createMap()
	{
		map = ResourceCache.getSharedInstance().maps.get(GameInfo.currentMapIndex);

		map.handOutRegions(players);
		map.initRegions();
	}

	// ======================================================
	// ======================================================
	public void update()
	{
		if(currentPlayer.isCpu)
		{
			updateCPU();
		}

		switch(state)
		{
		
		case SETUP:
			setup();
			break;
			
		case ATTACK:
			attack();
			break;
			
		case MOVE:
			move();
			break;
			
		case DEPLOYMENT:
			deploy();
			break;
			
		case ENDTURN:
			nextTurn();
			break;
			
		default:
			break;
		}
	}

	private void nextTurn()
	{
		if(!getNextPossiblePlayer()) {
			state = GAME_STATE.GAMEOVER;
		} else {
			unselectRegion();
			untargetRegion();
			setNextState();
			gameScene.setInitialHUD();
			
			if(currentPlayer.isCpu) /* if the new player is cpu, then auto-update */
			{
				update();
			}
		}
	}

	private void setNextState()
	{
		if(gameOver())
		{
			state = GAME_STATE.GAMEOVER;
		}

		switch(state) {
		case SETUP:
			state = GAME_STATE.ATTACK;
			break;
			
		case ATTACK: 
			state = GAME_STATE.MOVE;
			break;
			
		case MOVE:
			state = GAME_STATE.DEPLOYMENT;
			break;
			
		case DEPLOYMENT:
			state = GAME_STATE.ENDTURN;
			break;
			
		case PAUSED:
			state = GAME_STATE.ATTACK;
			break;
			
		case ENDTURN:
			state = GAME_STATE.ATTACK;
			break;
			
		default:
			break;
		}

		Log.d("debug", "STATE: " + state);
	}

	private void setup()
	{
		if(!currentPlayer.hasSoldiersInPool()) /* If player has deployed all of his soldiers. */
		{
			currentPlayer = getNextPlayer();

			if(currentPlayer.equals(players[0])) /* If all players have deployed their soldiers. */
			{
				gameScene.setInitialHUD();
				setNextState();
			}
			else
			{
				if(currentPlayer.isCpu)
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
			pRegion1.setSoldiers(Math.max(MIN_SOLDIERS_PER_REGION, pRegion1.numberOfSoldiers() - attackingSoldiers));
			pRegion2.setOwner(currentPlayer);
			pRegion2.setSoldiers(Math.max(MIN_SOLDIERS_PER_REGION, battleGenerator.remainingAttackers));

			// If the player wins an attack, he/she gets a bonus of soldiers to deploys next turn
			currentPlayer.setSoldiersPool(WIN_BONUS * TURN_REPLENISHMENT);
		}
		else
		{
			pRegion1.setSoldiers(Math.max(MIN_SOLDIERS_PER_REGION, pRegion1.numberOfSoldiers() - attackingSoldiers + battleGenerator.remainingAttackers));
			pRegion2.setSoldiers(Math.max(MIN_SOLDIERS_PER_REGION, battleGenerator.remainingDefenders));

			currentPlayer.setSoldiersPool(TURN_REPLENISHMENT);
		}

		setNextState();
		gameScene.showBattleScene(selectedRegion, targetedRegion, battleGenerator);

		untargetRegion();
		unselectRegion();	

	}

	private void deploy()
	{
		if(currentPlayer.hasSoldiersInPool() && currentPlayer.isCpu)
		{
			currentPlayer.deployAllSoldiers();
		}

		if(!currentPlayer.hasSoldiersInPool())
		{
			setNextState();
		}
	}

	public void move()
	{
		if(selectedRegion != null && targetedRegion != null)
		{
			Region pRegion1 = selectedRegion;
			Region pRegion2 = targetedRegion;

			pRegion1.setSoldiers(pRegion1.numberOfSoldiers() - movingSoldiers);
			pRegion2.setSoldiers(pRegion2.numberOfSoldiers() + movingSoldiers);

			untargetRegion();
			unselectRegion();

			setNextState();
		}

	}

	public void updateCPU()
	{
		if(selectedRegion != null && targetedRegion != null)
		{
			switch(state)
			{
			case ATTACK: 
				attack();
				break;
				
			case MOVE:
				move();
				break;
				
			default:
				break;
			}
			setNextState();
		}
		else
		{
			if(state.equals(GAME_STATE.DEPLOYMENT))
			{
				deploy();
				nextTurn();
			}
			else
			{
				Region pRegion = cpuSelectRegion();
				gameScene.showCpuMove(pRegion, cpuTargetRegion(pRegion));
			}
		}
	}

	private Region cpuSelectRegion()
	{
		if(state.equals(GAME_STATE.ATTACK))
		{
			return currentPlayer.pickRegionForAttack();
		}
		if(state.equals(GAME_STATE.MOVE))
		{
			return currentPlayer.pickRegionForMove();
		}
		return null;
	}
	
	private Region cpuTargetRegion(Region pRegion)
	{
		switch(state)
		{
		case ATTACK:
			return currentPlayer.pickNeighbourEnemyRegion(pRegion);
			
		case MOVE:
			return currentPlayer.pickNeighbourAlliedRegion(pRegion);
			
		default:
			return null;
		}
	}
	// ======================================================
	// GAME STATE
	// ======================================================

	public GAME_STATE getState()
	{
		return state;
	}

	public void setState(GAME_STATE state)
	{
		this.state = state;
	}

	public boolean isPaused() {
		return state == GAME_STATE.PAUSED;
	}

	private boolean gameOver()
	{
		int remainingPlayers = players.length;

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

	public Player getCurrentPlayer()
	{
		return currentPlayer;
	}

	public int getCurrentPlayerIndex()
	{
		return currentPlayerIndex;
	}

	public Player getNextPlayer()
	{
		currentPlayerIndex++;
		currentPlayerIndex = currentPlayerIndex % players.length;

		return players[currentPlayerIndex];
	}

	public Player[] getPlayers()
	{
		return players;
	}

	public void setCurrentPlayer(int index)
	{
		currentPlayer = players[index];
		currentPlayerIndex = index;
	}

	private boolean getNextPossiblePlayer()
	{
		int count = 0;

		currentPlayer = getNextPlayer();

		while(count < players.length)
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

	// ======================================================
	// REGIONS RELATED
	// ======================================================
	public void onRegionTouched(Region pRegion)
	{
		if(!currentPlayer.isCpu)
		{
			switch(state)
			{

			case SETUP:
				deploySoldiers(pRegion);
				break;

			case DEPLOYMENT:
				deploySoldiers(pRegion);
				break;

			case ATTACK:
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

	private void onMoveHandler(Region pRegion)
	{
		if(!pRegion.isFocused())
		{
			if(pRegion.hasOwner(currentPlayer))
			{
				if(this.selectedRegion == null)
				{
					if(pRegion.canAttack())
					{
						selectRegion(pRegion);
					}
				} else {
					targetRegion(pRegion);
				}
			}
		}
		else
		{
			if(pRegion.ID == this.selectedRegion.ID)
			{
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
			deploy();
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
			if(!neighbour.owner().equals(pRegion.owner()))
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

		attackingSoldiers = pRegion.numberOfSoldiers() - MIN_SOLDIERS_PER_REGION;

		selectedRegion = pRegion;
		selectedRegion.setFocus(true);
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
				defendingSoldiers = pRegion.numberOfSoldiers();

				targetedRegion = pRegion;
				targetedRegion.setFocus(true);
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
			selectedRegion.setFocus(false);
			selectedRegion = null;
		}
	}

	private void untargetRegion() 
	{
		if(targetedRegion != null)
		{
			targetedRegion.setFocus(false);	
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
