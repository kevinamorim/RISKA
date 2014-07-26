package feup.lpoo.riska.logic;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.util.adt.color.Color;

import feup.lpoo.riska.R;
import feup.lpoo.riska.elements.Map;
import feup.lpoo.riska.elements.Player;
import feup.lpoo.riska.elements.Region;
import feup.lpoo.riska.generator.BattleGenerator;
import feup.lpoo.riska.resources.ResourceCache;
import feup.lpoo.riska.scenes.GameScene;
import feup.lpoo.riska.scenes.SceneManager;
import feup.lpoo.riska.utilities.Utilities;

public class GameLogic {

	// ======================================================
	// SINGLETONS
	// ======================================================
	private ResourceCache resources;
	private BattleGenerator battleGenerator;

	// ======================================================
	// CONSTANTS
	// ======================================================
	private final int MIN_PLAYERS_IN_GAME = 2;
	private final float BONUS_FACTOR = 1.0f;

	private final int SOLDIER_INC = 1;

	public enum GAME_STATE {
		PAUSED,
		DEPLOYMENT,
		PLAY,
		PAUSED_PLAY,
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

	private GameScene gameScene;

	public boolean turnDone;

	public Region selectedRegion;
	public Region targetedRegion;

	
	public GameLogic(GameScene scene) {

		resources = ResourceCache.getSharedInstance();
		SceneManager.getSharedInstance();

		gameScene = scene;

		map = resources.map;

		createsPlayers();

		/* Distributes regions equally between each player */
		handOutRegions();

		state = GAME_STATE.DEPLOYMENT;
		turnDone = false;

		battleGenerator = new BattleGenerator();
	}

	private void createsPlayers() {

		players = new ArrayList<Player>();

		int MAX_SOLDIERS_TO_DEPLOY = (int) (map.getNumberOfRegions() * BONUS_FACTOR);

		Player player = new Player(false, PLAYER_COLOR[0], PLAYER_COLOR[1], "PLAYER");
		player.setSoldiersToDeploy(MAX_SOLDIERS_TO_DEPLOY);

		Player cpu = new Player(true, CPU_COLOR[0], CPU_COLOR[1], "CPU");
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

			region.setSoldiers(1);

			region.setOwner(player);
			region.setColors(player.getPrimaryColor(), player.getScondaryColor());

			i++;
			i = i % players.size(); 
		}
	}

	
	public void updateDeployment() {

		if(!currentPlayer.hasSoldiersLeftToDeploy()) /* If player has deployed all of his soldiers. */
		{
			currentPlayer = getNextPlayer();

			if(currentPlayer.equals(players.get(0))) /* If all players have deployed their soldiers. */
			{
				state = GAME_STATE.PLAY;
			}
			else
			{

				if(currentPlayer.isCPU())
				{
					gameScene.setInfoTabText(Utilities.getString(R.string.game_info_wait_for_CPU));
					currentPlayer.deploy();
				}

			}
		}

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
				if(!getNextPossiblePlayer())
				{
					state = GAME_STATE.GAMEOVER;
					return;
				}
				else
				{
					turnDone = false;
				}
			}	

			if(currentPlayer.isCPU())
			{
				automaticMove();
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
				simulateCPU(pRegion1, pRegion2);
			}
		}
	}

	public void simulateCPU(final Region attacker, final Region defender)
	{

		gameScene.lockUserInput();
		gameScene.lockHUD();

		selectRegion(attacker);
		targetRegion(defender);
		attack();
		
		gameScene.unlockUserInput();
		gameScene.unlockHUD();
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

	public void attack()
	{
		Region attacker = selectedRegion;
		Region defender = targetedRegion;
		
		pauseGame();

		// TEMP - battleGenerator will have to provide more precise insight on the results
		boolean won = battleGenerator.simulateAttack(attacker.getSoldiers(), defender.getSoldiers());

		gameScene.showBattleResult(selectedRegion, targetedRegion, won);
		
		if(won)
		{
			defender.changeOwner(attacker.getOwner());
			defender.setSoldiers(attacker.getNumberOfSoldiers());
			attacker.setSoldiers(1);		
		}
		else
		{
			attacker.setSoldiers(1);
		}
		
		turnDone = true;
		
		untargetRegion();
		unselectRegion();
	}

	public void autoDeployment(Player player)
	{
		player.deploy();
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

	public int getCurrentPlayerByIndex() {
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
		case DEPLOYMENT:
			onDeploymentHandler(pRegion);
			break;

		case PLAY:
			onPlayHandler(pRegion);
			break;

		default:
			break;
		}
	}

	private void onDeploymentHandler(Region pRegion)
	{
		if(pRegion.getOwner().equals(currentPlayer))
		{
			if(currentPlayer.hasSoldiersLeftToDeploy())
			{
				int deployed = currentPlayer.deploySoldiers(SOLDIER_INC);
				pRegion.addSoldiers(deployed);

				//hud.setInfoTabText(logic.getCurrentPlayer().getSoldiersToDeploy() + " left to deploy");
			}
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

	private void selectRegion(Region pRegion) {

		// Shouldn't happen. 
		if(selectedRegion != null)
		{
			unselectRegion();
		}

		selectedRegion = pRegion;
		selectedRegion.focus();
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

	private void targetRegion(Region pRegion)
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

	private void untargetRegion() 
	{
		targetedRegion.unfocus();	
		targetedRegion = null;
	}

	
	public void pauseGame()
	{
		state = GAME_STATE.PAUSED_PLAY;
	}
	
	public void resumeGame()
	{
		state = GAME_STATE.PLAY;
	}
}
