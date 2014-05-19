package feup.lpoo.riska;

import java.util.Random;

public class Dice extends Element {
	
	public static int MIN_VALUE = 1;
	public static int MAX_VALUE = 6;
	
	private int value;

	public Dice(float x, float y) {
		super(x, y);
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	
	public void generateNewValue() {
		
		Random random = new Random();
		
		this.value = random.nextInt(MAX_VALUE - MIN_VALUE) + MIN_VALUE;
		
	}

}
