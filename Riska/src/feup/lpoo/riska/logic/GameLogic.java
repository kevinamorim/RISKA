package feup.lpoo.riska.logic;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
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
	private SceneManager sceneManager;
	private BattleGenerator battleGenerator;

	// ======================================================
	// CONSTANTS
	// ======================================================
	private final int MIN_PLAYERS_IN_GAME = 2;
	private final float BONUS_FACTOR = 1.0f;
	private final float CPU_DELAY = 1.0f;

	private final int SOLDIER_INC = 1;

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

	private GameScene gameScene;

	public boolean turnDone;

	public Region selectedRegion;
	public Region targetedRegion;

	public GameLogic(GameScene scene) {

		resources = ResourceCache.getSharedInstance();
		sceneManager = SceneManager.getSharedInstance();

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
					sceneManager.getGameScene().setInfoTabText("Waiting for deployment...");
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
				currentPlayer = getNextPlayer();
				turnDone = false;
			}
			
			if(!getNextPossiblePlayer())
			{
				state = GAME_STATE.GAMEOVER;
				return;
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

		Region attacker = currentPlayer.selectRegion();

		if(attacker != null)
		{

			Region defender = attacker.selectTargetRegion();

			if(defender != null)
			{
				simulateCPU(attacker, defender);
			}

		}
	}

	public void simulateCPU(final Region attacker, final Region defender)
	{

		long delay = (long)(CPU_DELAY * 1000);

		gameScene.lockUserInput();
		gameScene.lockHUD();

		/*
		DelayModifier selectRegionMod = new DelayModifier(CPU_DELAY) {

			@Override
			protected void onModifierFinished(IEntity pItem) {
				attacker.focus();
			}
		};
		*/
		
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		attacker.focus();

		/*
		DelayModifier targetRegionMod = new DelayModifier(CPU_DELAY * 2) {

			@Override
			protected void onModifierFinished(IEntity pItem) {
				defender.focus();
			}

		};
		*/
		
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		defender.focus();

		/*
		DelayModifier attackMod = new DelayModifier(CPU_DELAY * 3) {

			@Override
			protected void onModifierFinished(IEntity pItem) {

				gameScene.unlockUserInput();
				gameScene.unlockHUD();	
			}

		};
		*/
		
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		selectRegion(attacker);
		targetRegion(defender);
		attack(attacker, defender);
		
		gameScene.unlockUserInput();
		gameScene.unlockHUD();

		//		registerEntityModifier(selectRegionMod);
		//		registerEntityModifier(targetRegionMod);
		//		registerEntityModifier(attackMod);
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

	public void attack(Region attacker, Region defender)
	{

		boolean won = battleGenerator.simulateAttack(attacker.getSoldiers(), defender.getSoldiers());

		if(won)
		{
			attackerWon(attacker, defender);
		}
		else
		{
			defenderWon(attacker, defender);
		}	
		
		gameScene.showBattleScene(won);
	}

	public void attackerWon(Region attacker, Region defender)
	{
		defender.changeOwner(attacker.getOwner());
		defender.setSoldiers(attacker.getNumberOfSoldiers());
		attacker.setSoldiers(1);

		attacker.unfocus();
		defender.unfocus();
	}

	public void defenderWon(Region attacker, Region defender)
	{
		attacker.setSoldiers(1);

		attacker.unfocus();
		defender.unfocus();
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

	public void setCurrentPlayerByIndex(int index)
	{
		this.setCurrentPlayer(players.get(index));
	}

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
		if(!pRegion.isFocused()) {

			if(currentPlayer.ownsRegion(pRegion)) {
				selectRegion(pRegion);
			}
			else {
				targetRegion(pRegion);
			}

		}
		else {

			if(currentPlayer.ownsRegion(pRegion))
			{
				unselectRegion(pRegion);
			}
			else
			{
				untargetRegion(pRegion);
			}

		}	
	}

	private void selectRegion(Region pRegion) {

		// Shouldn't happen. 
		if(selectedRegion != null) {
			unselectRegion(selectedRegion);
		}

		if(pRegion.canAttack()) {

			selectedRegion = pRegion;
			selectedRegion.focus();

			//detailScene.setAttributes(selectedRegion, null);

			//hud.showDetailButton();
			//setInfoTabToChooseEnemyRegion();

			//howOnlyNeighbourRegions(pRegion);	
		}
	}

	private void unselectRegion(Region pRegion)
	{
		if(targetedRegion != null) {
			targetedRegion.unfocus();
			targetedRegion = null;

			//hud.hideAttackButton();
		}

		//detailScene.setAttributes(null, null);
		//hud.hideDetailButton();

		selectedRegion.unfocus();
		selectedRegion = null;

		//showAllRegions();

		//setInfoTabToChooseOwnRegion();
	}

	private void targetRegion(Region pRegion)
	{
		if(selectedRegion != null)
		{
			if(pRegion.isNeighbourOf(selectedRegion))
			{

				if(targetedRegion != null)
				{
					untargetRegion(targetedRegion);
				}

				targetedRegion = pRegion;
				targetedRegion.focus();

				//detailScene.setAttributes(selectedRegion, targetedRegion);

				//hud.showAttackButton();	
				//setInfoTabToProceedToAttack();
			}

		}
	}

	private void untargetRegion(Region pRegion) 
	{

		targetedRegion.unfocus();	
		targetedRegion = null;

		//detailScene.setAttributes(selectedRegion, null);
		//hud.hideAttackButton();

		//setInfoTabToChooseEnemyRegion();
	}
}
