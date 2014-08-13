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
		
		while(attacker > 0 && defender > 0) {
			/* Best of three */ 
			int attackerWins = 0, defenderWins = 0;
			
			for(int i = 0; i < 3; i++) {
				
				int r = Utils.randomInt(0, 1);
				if(r == 0) {
					attackerWins++;
				} else {
					defenderWins++;
				}
				
			}
			
			if(attackerWins > defenderWins) {
				defender--;
			} else {
				attacker--;
			}
			
		}
		
		result = attacker > defender;
		
		remainingDefenders = defender;
		remainingAttackers = attacker;
		
//		attackerPoints = Utils.randomInt(0, attacker);
//		defenderPoints = Utils.randomInt(0, defender);
//		
//		result = attackerPoints > defenderPoints;
//		
//		if(result)	// attacker won
//		{			
//			remainingDefenders = 0;
//			remainingAttackers = attackerPoints;
//		}
//		else		// Defender won (lol, rly)
//		{
//			remainingDefenders = defenderPoints;
//			remainingAttackers = attackerPoints;
//		}
	}
}
