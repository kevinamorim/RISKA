package feup.lpoo.riska.generator;

import feup.lpoo.riska.utilities.*;

/**
 * Generates and resolves combats between armies.
 */
public class BattleGenerator {
	
	public boolean result;
	public int attackerPoints;
	public int defenderPoints;
	public int remainingAttackers;
	public int remainingDefenders;

	public BattleGenerator() { }

	public void simulateAttack(int attacker, int defender)
	{
		attackerPoints = Utilities.randomInt(1, attacker);
		defenderPoints = Utilities.randomInt(1, defender);
		
		result = attackerPoints > defenderPoints;
		
		if(result)	// attacker won
		{
			remainingDefenders = 0;
			remainingAttackers = attackerPoints;
		}
		else		// Defender won (lol, rly)
		{
			remainingDefenders = defenderPoints;
			remainingAttackers = 0;
		}
	}
}
