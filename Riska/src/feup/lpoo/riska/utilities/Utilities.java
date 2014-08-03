package feup.lpoo.riska.utilities;

import java.util.Random;

import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;

import feup.lpoo.riska.logic.MainActivity;

public class Utilities {
	
	private static MainActivity activity = MainActivity.getSharedInstance();
	
	private static Random r = new Random();

	public static boolean isValidTouch(long lastTouch, long firstTouch, long minTouchInterval)
	{
		return ((lastTouch - firstTouch) > minTouchInterval);
	}

	public static String getString(int resID) {
		return activity.getString(resID);
	}
	
	public static String wrapText(Font pFont, String pString, float maxWidth) {

		Text pText = new Text(0, 0, pFont, pString, 1000, activity.getVertexBufferObjectManager());

		if(pText.getWidth() < maxWidth) {
			return pString;
		}

		// Split the entire string into separated words.
		String[] words = pText.getText().toString().split(" ");

		String wrappedText = ""; /* Final string. */
		String line = ""; /* Temp variable */

		for(String word : words) {

			pText.setText(line + word);
			if(pText.getWidth() > maxWidth) {			
				wrappedText += line + "\n\n";
				line = "";

			}

			line += word + " ";

		}

		wrappedText += line;

		return wrappedText;

	}
	
	public static boolean isBetween(int value, int min, int max)
	{
		return (value >= min && value <= max);
	}
	
	public static boolean isBetween(float value, float min, float max)
	{
		return (value >= min && value <= max);
	}
	
	public static int randomInt(int min, int max)
	{	
		return (r.nextInt(max) + min);
	}
	
	public static float getBoundsX(Entity e)
	{
		return (e.getX() + (0.5f * e.getWidth()));
	}
	
	public static float getBoundsY(Entity e)
	{
		return (e.getY() + (0.5f * e.getHeight()));
	}
	
	public static float calculateChanceOfSuccess(int val_1, int val_2)
	{
		int min = 0;
		
		float prob1 = 1f / (val_1 + (1 - min));
		float prob2 = 1f / (val_2 + (1 - min));
		
		float probSum = 0f;
		
		for(int i = 0; i <= val_1; i++)
		{
			float probSumI = 0f;
			
			for(int j = 0; j <= val_2 && j < i; j++)
			{
				probSumI += prob2;
			}
			
			probSum += prob1 * probSumI;
		}
		
		//Log.d("Riska", "Probability for " + val_1 + " and " + val_2 + " : " + probSum);
		
		return probSum;
	}
}
