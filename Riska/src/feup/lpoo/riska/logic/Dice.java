package feup.lpoo.riska.logic;

import java.util.ArrayList;
import java.util.Random;

public class Dice extends Element {
	
	protected int value;

	public Dice(int x, int y) {
		super(x, y, null);
		this.value = 0;
	}

	/**
	 * @return the random value
	 *
	 */
	public int getRandomValue() {
		return value;
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
	public int generateNewRandomValueBetween(int max) {
		
		Random random = new Random();
		
		return random.nextInt(max + 1);
	}
	
	/**
	 *  Given two armies (an attacker and a defender), the method return whether the attacker was successful or not.
	 *  
	 * @param attacker
	 * @param defender
	 * @return True if attacker won.
	 */
	public boolean isAttackSuccessful(ArrayList<Unit> attacker, ArrayList<Unit> defender) {
		int sumAttacker, sumDefender;
		
		sumAttacker = getSum(attacker, true);
		
		sumDefender = getSum(defender, false);
		
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
			sum += this.generateNewRandomValueBetween(isAttacker ? unit.getAttack() : unit.getDefense());
		}
		
		return sum;
	}

}
