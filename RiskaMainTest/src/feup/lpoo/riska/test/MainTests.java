package feup.lpoo.riska.test;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import feup.lpoo.riska.Dice;
import feup.lpoo.riska.MainActivity;

public class MainTests extends ActivityUnitTestCase<MainActivity> {
		
	private MainActivity activity;
	
	public MainTests() {
		super(MainActivity.class);
	}
	
	/*
	 * Initialize activity.
	 */
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent(getInstrumentation().getTargetContext(), 
				MainActivity.class);
		
		startActivity(intent, null, null);
		activity = getActivity();
	}
	
	public void testDice() {
		
		int NUMBER_OF_TESTS = 100;
		
		Dice dice = new Dice(0, 0);
		
		int[] values = new int[NUMBER_OF_TESTS];
		
		for(int i = 0; i < NUMBER_OF_TESTS; i++) {
			dice.generateNewValue();
			values[i] = dice.getValue();
		}
		
		Arrays.sort(values);
		
		Assert.assertTrue(values[0] >= dice.MIN_VALUE);
		Assert.assertTrue(values[values.length - 1] <= dice.MAX_VALUE);
		
	}
	
	
}
