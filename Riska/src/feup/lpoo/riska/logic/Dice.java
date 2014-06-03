package feup.lpoo.riska.logic;

import java.util.Random;

public class Dice extends Element {
	
	public static int MIN_VALUE = 1;
	public static int MAX_VALUE = 6;
	
	protected int value;

	public Dice(int x, int y) {
		super(x, y, null);
		this.value = 0;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Generates a new random value assigning it to the value attribute of the Dice.
	 */
	public void generateNewValue() {
		
		Random random = new Random();
		
		this.value = random.nextInt(MAX_VALUE - MIN_VALUE + 1) + MIN_VALUE;
		
	}

}