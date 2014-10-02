package feup.lpoo.riska.logic;

import java.util.ArrayList;
import java.util.Random;

import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Player;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.hud.GameHUD;
import feup.lpoo.riska.scenes.GameScene;

public class GameLogic
{
	// ======================================================
	// CONSTANTS
	// ======================================================
	private final int WIN_BONUS = 2;
	private final int TURN_REPLENISHMENT = 3;

	public enum GAME_STATE { PAUSED, SETUP, DEPLOY, ATTACK, MOVE, CPU, PLAY, ENDTURN, GAMEOVER };

	public enum MODE { NONE, SUMMON, DEPLOY, ATTACK };

	private final GAME_STATE startingState = GAME_STATE.PLAY;

	private GAME_STATE currentState;
	private GAME_STATE tempState;
	private MODE currentMode;

	// ======================================================
	// FIELDS
	// ======================================================
	public Map map;

	private Player[] players;

	private GameScene gameScene;
	private GameHUD gameHUD;

	private Player currentPlayer;
	private int currentPlayerIndex;

	public Region selectedRegion;
	public Region targetedRegion;
	
	private boolean targeted = false;
	private boolean selected = false;

	public int attackingSoldiers;
	public int defendingSoldiers;
	public int movingSoldiers;

	// ======================================================
	// ======================================================

	public GameLogic(GameScene scene, GameHUD hud)
	{
		gameScene = scene;
		gameHUD = hud;
		createPlayers();
		createMap();

		currentState = startingState;
		currentMode = MODE.NONE;
	}

	private void createPlayers()
	{
		players = GameInfo.players();
		currentPlayer = players[0];

		for(Player p : players)
		{
			p.moves = GameInfo.defaultPlayerMoves;
			p.soldiersPool = GameInfo.defaultSummonPoolSize;
		}
	}

	private void createMap()
	{
		map = GameInfo.currentMap;

		map.handOutRegions(players);
		map.initRegions();
	}

	// ======================================================
	// ======================================================
	public void UpdatePlayer()
	{
		switch(currentState)
		{
		
		case PLAY:
			break;

		case SETUP:
			currentState = GAME_STATE.PLAY;
			break;

		default:
			break;
		}
	}

	private void nextTurn()
	{
		if(!getNextPossiblePlayer())
		{
			currentState = GAME_STATE.GAMEOVER;
		}
		else
		{
			Unselect();
			Untarget();
			gameScene.setInitialHUD();

			if(currentPlayer.isCpu) /* if the new player is cpu, then auto-update */
			{
				UpdatePlayer();
			}
		}

		UpdatePlayer();
	}

	private void OnSetupUpdate()
	{
		if(!currentPlayer.hasSoldiersInPool()) /* If player has deployed all of his soldiers. */
		{
			currentPlayer = getNextPlayer();

			if(currentPlayer.equals(players[0])) /* If all players have deployed their soldiers. */
			{
				currentState = GAME_STATE.PLAY;
			}
			else
			{
				if(currentPlayer.isCpu)
				{
					deployAllSoldiers(currentPlayer);	
				}
			}
		}
	}
	
	public void deployAllSoldiers(Player player)
	{
		int i = 0;
		
		while(player.soldiersPool > 0)
		{
			int toDeploy = Math.min(GameInfo.defaultDeployable, player.soldiersPool);
			
			Region pRegion = player.getRegions().get(i);
			
			pRegion.addSoldiers(toDeploy);
			player.soldiersPool -= toDeploy;
			
			i++;
			i %= player.getRegions().size();
			
			gameScene.UpdateRegion(pRegion.ID);
		}
		
		UpdatePlayer();
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

		Untarget();
		Unselect();
	}

	public void updateCPU()
	{
		if(selectedRegion != null && targetedRegion != null)
		{

		}
		else
		{

		}
	}

	private Region cpuSelectRegion()
	{
		if(currentState.equals(GAME_STATE.ATTACK))
		{
			return currentPlayer.pickRegionForAttack();
		}
		if(currentState.equals(GAME_STATE.MOVE))
		{
			return currentPlayer.pickRegionForMove();
		}
		return null;
	}

	private Region cpuTargetRegion(Region pRegion)
	{
		switch(currentState)
		{
		case ATTACK:
			return currentPlayer.pickNeighbourEnemyRegion(pRegion);

		case MOVE:
			return currentPlayer.pickNeighbourAlliedRegion(pRegion);

		default:
			return null;
		}
	}

	public void setMode(MODE pMode)
	{
		this.currentMode = pMode;

		gameScene.draw();
	}

	public MODE Mode()
	{
		return currentMode;
	}

	// ======================================================
	// GAME STATE
	// ======================================================

	public GAME_STATE getState()
	{
		return currentState;
	}

	public void setState(GAME_STATE state)
	{
		this.currentState = state;
	}

	public boolean isPaused() {
		return currentState == GAME_STATE.PAUSED;
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

		return (remainingPlayers < GameOptions.minPlayers);

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
		switch(currentState)
		{
		case PLAY:
			if(pRegion.equals(selectedRegion))
			{
				Untarget();
				Unselect();
				return;
			}
			if(pRegion.equals(targetedRegion))
			{
				Untarget();
				return;
			}
			if(pRegion.ownerIs(currentPlayer))
			{
				if(Targeted())
				{
					Untarget();
				}

				if(Selected())
				{
					if(pRegion.isNeighbourOf(selectedRegion))
					{
						Target(pRegion);
					}	
					return;
				}

				Select(pRegion);
				return;
			}
			else
			{
				if(Selected())
				{
					if(Targeted())
					{
						Untarget();
					}

					if(pRegion.isNeighbourOf(selectedRegion))
					{
						Target(pRegion);
					}
				}
				return;
			}

		case SETUP:
			if(pRegion.ownerIs(currentPlayer))
			{
				SummonOn(pRegion);
			}
			break;

		default:
			break;
		}
		
		gameScene.updateHUD();
	}

	private void SummonOn(Region targetRegion)
	{
		// Can't summon more than the soldiers on the player's pool
		int soldiersToDeploy = Math.min(GameInfo.defaultDeployable, currentPlayer.soldiersPool);

		// Subtract the summoned players from the player's pool
		currentPlayer.subtractFromSoldiersPool(soldiersToDeploy);
		
		// Add summoned soldiers to the region's garrison
		targetRegion.addSoldiers(soldiersToDeploy);
		
		// Update the region's visual data
		gameScene.UpdateRegion(targetRegion.ID);
	}

	public boolean Selected()
	{
		return selected;
	}

	public boolean Targeted()
	{
		return targeted;
	}

	private boolean canDeploy(Region pRegion)
	{
		return pRegion.getGarrison() > GameInfo.minGarrison;
	}

	private boolean canAttack(Region pRegion)
	{
		return pRegion.getGarrison() > GameInfo.minGarrison;
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

	public void Select(Region pRegion)
	{
		selectedRegion = pRegion;
		selected = true;

		gameScene.Select(pRegion.ID);
	}

	public void Target(Region pRegion)
	{
		targetedRegion = pRegion;
		targeted = true;
		
		gameScene.Target(pRegion.ID);
	}

	private void Unselect()
	{
		if(selectedRegion != null)
		{
			gameScene.Unselect(selectedRegion.ID);
			
			selectedRegion = null;
			selected = false;
		}
	}

	private void Untarget() 
	{
		if(targetedRegion != null)
		{
			gameScene.Untarget(targetedRegion.ID);
			
			targetedRegion = null;
			targeted = false;
		}	
	}

	public void pauseGame()
	{
		tempState = currentState;
		currentState = GAME_STATE.PAUSED;
	}

	public void resumeGame()
	{
		if(currentState.equals(GAME_STATE.PAUSED)) currentState = tempState;
	}

	public void onDeploy(Region from, Region to, int soldiers)
	{
	}

	public void onAttack(Region attacker, int soldiersSent)
	{
		attacker.addSoldiers(-soldiersSent);
	}

	public void onVictory(Region attacker, Region defender, int soldiersLeft)
	{
		defender.setSoldiers(soldiersLeft);
		defender.setOwner(attacker.owner());
	}

	public void onDefeat(Region attacker, Region defender, int soldiersLeft)
	{
		defender.setSoldiers(soldiersLeft);
	}

	public void onSummon(Region receiver, int value)
	{
	}

}
