package feup.lpoo.riska.test;

import java.util.Arrays;
import java.util.List;

import feup.lpoo.riska.logic.Dice;
import feup.lpoo.riska.logic.MainActivity;
import junit.framework.Assert;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;

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
	

	
	/*
	* ==================
	*       ELEMENT
	* ==================
	*/
	// Creates a new element, giving it values to it's x and y attributes and tests if
	// it's initialization is good.
	public void testElementInit() { }
	
	// Changes the x and y values and checks if the new values are correct.
	public void testElementMove() { }
	
	/*
	* ==================
	*       PLAYER
	* ==================
	*/
	
	/*
	* ==================
	*       REGION
	* ==================
	*/
	
	// Creates a Region and checks if the creation corresponds to what we expected.
	public void testCreateRegion() { }
	
	/*
	* ==================
	*       UNIT
	* ==================
	*/
	
	// Creates an Unit and checks if the creation corresponds to what we expected.
	public void testCreateUnit() { }
	
	// Moves an Unit and checks if the new position is where we wanted.
	public void testMoveUnit() { }
	
	// Kills an Unit and checks if the state changed.
	public void testKillUnit() { }
	
	// Tells an Unit to attack and checks if the attack is successfully made.
	public void testUnitAttack() { }
	
	/*
	* ==================
	*       CARD
	* ==================
	*/
	
	// Creates a card with some values and checks if the initialization went good.
	public void testCreateCard() { }
	
	// Creates an Unit Card and checks if the initialization went good, including the inheritance.
	public void testCreateUnitCard() { }
	
	// Creates an Region Card and checks if the initialization went good, including the inheritance.
	public void testCreateRegionCard() { }
	
	// Creates an Goal Card and checks if the initialization went good, including the inheritance.
	public void testCreateGoalCard() { }
	
	/*
	* ==================
	*       DICE
	* ==================
	*/
	
	/*
	* Tests dice
	*/
	public void testDice() {
		
		int NUMBER_OF_TESTS = 5000;
		
		Dice dice = new Dice(0, 0);
		
		int[] values = new int[NUMBER_OF_TESTS];
		
		for(int i = 0; i < NUMBER_OF_TESTS; i++) {
			dice.generateNewValue();
			values[i] = dice.getValue();
		}
		
		Arrays.sort(values);
		
		Assert.assertTrue(values[0] == Dice.MIN_VALUE);
		Assert.assertTrue(values[values.length - 1] == Dice.MAX_VALUE);
		
	}
	
	/*
	* ==================
	*        MAP
	* ==================
	*/
	
	// Creates a map with a given sprite and name and checks if the creation corresponds to what we expected.
	public void testCreateMap() { }
	
	/*
	* ==================
	*      CONFIG
	* ==================
	*/
	
	// Changes the music option, and checks if the music started/stopped playing.
	public void testChangeMusic() { }
	
	// Changes the sound option, and checks if the sounds started/stopped playing.
	public void testChangeSound() { }
	
	// Changes the quality option, and checks if the quality really changed.
	public void testChangeQuality() { }
	
	// Changes the game difficulty, starts a new game and checks if the difficulty really changed.
	public void testChangeDifficulty() { }
	
	/*
	* ==================
	*       BUTTON
	* ==================
	*/
	
	// Creates an instance of a button, with a given text, and checks if the creation corresponds to what we expected.
	public void testCreateButton() { }
	
	// Presses a button and checks if the sprite changed accordingly.
	public void testPressButtonAnimation() { }
	
	// Presses a button and checks if the action corresponds to what we expected.
	public void testPressButtonAction() { }
	
	
}
