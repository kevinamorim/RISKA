package feup.lpoo.riska.generator;

import java.util.ArrayList;
import java.util.Random;

import feup.lpoo.riska.elements.Unit;

/**
 * Generates and resolves combats between armies.
 */
public class BattleGenerator {
		
	protected int attackerPoints;
	protected int defenderPoints;

	/**
	 * Default constructor.
	 * Initializes the class members.
	 */
	public BattleGenerator() {
		
		this.attackerPoints = 0;
		this.defenderPoints = 0;
	}

	/**
	 * @return random int in the interval [min, max].
	 */
	public int generateNewRandomValueBetween(int min, int max) {
		
		Random random = new Random();
		
		return random.nextInt(max - min + 1) + min;
	}

	/**
	 * @return Random int in the interval [0, max].
	 * 
	 * @param max
	 */
	public int generateNewRandomValue(int max) {
		
		Random random = new Random();
		
		return random.nextInt(max + 1);
	}
	
	/**
	 *  Given two armies (an attacker and a defender), the method returns
	 *    whether the attacker was successful or not.
	 *    
	 *  Note: the values for attacker and defender should be accessed using the methods getAttackerPoints() and getDefenderPoints().
	 *  
	 *  
	 * @param attacker
	 * @param defender
	 * @return True if attacker won.
	 */
	public boolean simulateAttack(ArrayList<Unit> attacker, ArrayList<Unit> defender) {
		int sumAttacker, sumDefender;
		
		sumAttacker = getSum(attacker, true);
		sumDefender = getSum(defender, false);
		
		this.attackerPoints = sumAttacker;
		this.defenderPoints = sumDefender;
		
		return (sumAttacker > sumDefender);
	}

	/**
	 * Gets the sum of attacks or defenses of an army.
	 * 
	 * @param army
	 * @param isAttacker
	 * @return
	 */
	private int getSum(ArrayList<Unit> army, boolean isAttacker) {
		int sum = 0;
		
		for(Unit unit : army) {
			sum += this.generateNewRandomValue(isAttacker ? unit.getAttack() : unit.getDefense());
		}
		
		return sum;
	}

	/**
	 * @return The points of the attacker.
	 */
	public int getAttackerPoints() {
		return attackerPoints;
	}

	/**
	 * @return The points of the defender.
	 */
	public int getDefenderPoints() {
		return defenderPoints;
	}
	
}
