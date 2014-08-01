package feup.lpoo.riska.logic;

import java.util.ArrayList;
import org.andengine.util.adt.color.Color;

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
	public final int WIN_BONUS = 2;
	public final int TURN_REPLENISHMENT = 3;
	public final int MIN_SOLDIERS_PER_REGION = 1;
	private final int MIN_PLAYERS_IN_GAME = 2;
	private Color[] PLAYER_COLOR = { new Color(0f, 0.447f, 0.898f), new Color(1f, 1f, 0f) };
	private Color[] CPU_COLOR = { new Color(1f, 0f, 0f), new Color(0f, 0f, 0f) };
	
	public enum GAME_STATE { PAUSED, SETUP,  DEPLOYMENT, ATTACK, MOVE, CPU, PLAY, GAMEOVER };

	// ======================================================
	// FIELDS
	// ======================================================
	private ArrayList<Player> players;
	private Map map;
	private GAME_STATE state;
	private Player currentPlayer;
	private GameScene gameScene;
	public boolean attackDone;
	public boolean turnDone;
	public Region selectedRegion;
	public Region targetedRegion;
	
	public int attackingSoldiers = 0;
	
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
	}

	public void updateGame() {

		if(gameOver())
		{
			state = GAME_STATE.GAMEOVER;
		}
		else
		{
			if(turnDone)
			{
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
				if(currentPlayer.isCPU())
				{
					pauseGame();
					automaticMove();
				}
			}
		}	
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
		Region pRegion1 = currentPlayer.selectRegion();

		if(pRegion1 != null)
		{
			Region pRegion2 = pRegion1.selectTargetRegion();

			if(pRegion2 != null)
			{
				this.attackingSoldiers = pRegion1.getNumberOfSoldiers() - MIN_SOLDIERS_PER_REGION;
				gameScene.showCpuMove(pRegion1, pRegion2);	
			}
		}		
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

	public void setup() {

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
				if(currentPlayer.isCPU())
				{
					currentPlayer.deployAllSoldiers();
				}
			}
		}

	}

	public void attack()
	{
		if(selectedRegion == null || targetedRegion == null)
		{
			return;
		}
		
		Region pRegion1 = selectedRegion;
		Region pRegion2 = targetedRegion;
		
		battleGenerator.simulateAttack(attackingSoldiers, pRegion2.getNumberOfSoldiers());
		
		boolean won = battleGenerator.result;
			
		gameScene.showBattleScene(selectedRegion, targetedRegion, battleGenerator);

		if(won)
		{
			pRegion1.setSoldiers(pRegion1.getNumberOfSoldiers() - attackingSoldiers);
			pRegion2.changeOwner(currentPlayer);
			pRegion2.setSoldiers(battleGenerator.remainingAttackers);
			currentPlayer.setSoldiersToDeploy(WIN_BONUS * TURN_REPLENISHMENT);
		}
		else
		{
			pRegion1.setSoldiers(pRegion1.getNumberOfSoldiers() - attackingSoldiers);
			pRegion2.setSoldiers(battleGenerator.remainingDefenders);
			currentPlayer.setSoldiersToDeploy(TURN_REPLENISHMENT);
		}
			
		untargetRegion();
		unselectRegion();
		
		attackDone = true;
	}

	public void deploy() {
		
		if(currentPlayer.hasSoldiersLeftToDeploy() && currentPlayer.isCPU()) {
			currentPlayer.deployAllSoldiers();
		}
		
		if(!currentPlayer.hasSoldiersLeftToDeploy()) {
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
	// GETTERS & SETTERS
	// ======================================================

	public GAME_STATE getState()
	{
		return state;
	}

	public void setState(GAME_STATE state) {
		this.state = state;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public int getCurrentPlayerIndex() {
		for(int i = 0; i < players.size(); i++)
		{
			if(players.get(i) == currentPlayer)
			{
				return i;
			}
		}

		return -1;
	}

	public void setCurrentPlayer(Player currentPlayer)
	{
		this.currentPlayer = currentPlayer;
	}

	public Map getMap() {
		return map;
	}
	
	public Player getNextPlayer() {

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

	public void setCurrentPlayerByIndex(int index)
	{
		this.setCurrentPlayer(players.get(index));
	}

	// ======================================================
	// REGIONS RELATED
	// ======================================================
	public void onRegionTouched(Region pRegion)
	{
		switch(state)
		{
		case SETUP:
			pRegion.deploy(currentPlayer);
			break;
		case DEPLOYMENT:
			pRegion.deploy(currentPlayer);
			break;
		case PLAY:
			onPlayHandler(pRegion);
			break;

		default:
			break;
		}
	}

	private void onPlayHandler(Region pRegion)
	{
		if(!pRegion.isFocused())
		{
			if(currentPlayer.ownsRegion(pRegion))
			{
				if(pRegion.canAttack()) // Shouldn't be here
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
			if(currentPlayer.ownsRegion(pRegion))
			{
				unselectRegion();
			}
			else
			{
				untargetRegion();
			}

		}	
	}

	public void selectRegion(Region pRegion) {

		// Shouldn't happen. 
		if(selectedRegion != null)
		{
			unselectRegion();
		}

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
