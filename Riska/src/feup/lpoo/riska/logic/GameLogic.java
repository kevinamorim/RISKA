package feup.lpoo.riska.logic;

import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Player;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.hud.GameHUD;
import feup.lpoo.riska.scenes.game.GameScene;

public class GameLogic
{
	// ======================================================
	// CONSTANTS
	// ======================================================
	public enum GAME_STATE { PAUSED, SETUP, DEPLOY, ATTACK, MOVE, CPU, PLAY, ENDTURN, GAMEOVER };

	public enum MODE { NONE, SUMMON, DEPLOY, ATTACK };

	private final GAME_STATE startingState = GAME_STATE.SETUP;

	private GAME_STATE currentState;
	private GAME_STATE tempState;
	private MODE currentMode;

	// ======================================================
	// FIELDS
	// ======================================================
	public Map map;

	private Player[] players;

	private GameScene gameScene;

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
		createPlayers();
		createMap();

		currentState = startingState;
		currentMode = MODE.NONE;
	}

	private void createPlayers()
	{
/*		players = GameInfo.players();
		currentPlayer = players[0];

		for(Player p : players)
		{
			p.moves = GameInfo.defaultPlayerMoves;
			p.soldiersPool = GameInfo.defaultSummonPoolSize;
		}*/
	}

	private void createMap()
	{
		/*map = GameInfo.currentMap;

		map.handOutRegions(players);
		map.initRegions(GameInfo.minGarrison);*/
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
	
	public void deployAllSoldiers(Player player)
	{
		int i = 0;
		
		while(player.soldiersPool > 0)
		{
			//int toDeploy = Math.min(GameInfo.defaultDeployable, player.soldiersPool);
			
			//player.deploySoldiersTo(i, toDeploy);
			
			gameScene.UpdateRegion(player.getRegion(i));

			i++;
			i %= player.getRegions().size();
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
		//int toDeploy = Math.min(GameInfo.defaultDeployable, currentPlayer.soldiersPool);

		// Subtract the summoned players from the player's pool
		//currentPlayer.subtractFromSoldiersPool(toDeploy);
		
		// Add summoned soldiers to the region's garrison
		//targetRegion.addSoldiers(toDeploy);
		
		// Update the region's visual data
		gameScene.UpdateRegion(targetRegion);
	}

	public boolean Selected()
	{
		return selected;
	}

	public boolean Targeted()
	{
		return targeted;
	}

	public void Select(Region pRegion)
	{
		selectedRegion = pRegion;
		selected = true;

		gameScene.Select(pRegion);
	}

	public void Target(Region pRegion)
	{
		targetedRegion = pRegion;
		targeted = true;
		
		gameScene.Target(pRegion);
	}

	private void Unselect()
	{
		if(selectedRegion != null)
		{
			gameScene.Unselect(selectedRegion);
			
			selectedRegion = null;
			selected = false;
		}
	}

	private void Untarget() 
	{
		if(targetedRegion != null)
		{
			gameScene.Untarget(targetedRegion);
			
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
