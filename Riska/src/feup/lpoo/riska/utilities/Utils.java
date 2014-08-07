package feup.lpoo.riska.utilities;

import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import android.util.Log;
import feup.lpoo.riska.logic.MainActivity;

public class Utils
{
	
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
	
	public static void wrapText(Text pText, Entity e, float textBoundingFactor)
	{
		wrapText(pText, e.getWidth(), e.getHeight(), textBoundingFactor);
	}
	
	public static void wrapText(Text pText, Camera camera, float textBoundingFactor)
	{
		wrapText(pText, camera.getWidth(), camera.getHeight(), textBoundingFactor);
	}
	
	public static void wrapText(Text pText, float pWidth, float pHeight, float textBoundingFactor)
	{
		
		Log.d("Riska", "Called wrapText() with Text: " + pText.toString() + ", W: " + pWidth + ", H: " + pHeight + ", F: " + textBoundingFactor);
		if(pText.getWidth() / pWidth > pText.getHeight() / pHeight) // Dealing in X
		{
			pText.setScale(textBoundingFactor * pWidth / pText.getWidth());
		}
		else	// Dealing in Y
		{
			pText.setScale(textBoundingFactor * pHeight / pText.getHeight());
		}
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
		int min = 1;
		
		float prob1 = 1f / (val_1 + (1 - min));
		float prob2 = 1f / (val_2 + (1 - min));
		
		float probSum = 0f;
		
		for(int i = min; i <= val_1; i++)
		{
			float probSumI = 0f;
			
			for(int j = min; j <= val_2 && j < i; j++)
			{
				probSumI += prob2;
			}
			
			probSum += prob1 * probSumI;
		}
		
		return probSum;
	}

	/**
	 * Works with scale
	 */
	public static float getWidth(Entity e)
	{
		return (e.getScaleX() * e.getWidth());
	}
	
	/**
	 * Works with scale
	 */
	public static float getHeight(Entity e)
	{
		return (e.getScaleY() * e.getHeight());
	}

	public static float getCenterX(Entity e1)
	{
		return (0.5f * e1.getWidth());
	}
	
	public static float getCenterY(Entity e1)
	{
		return (0.5f * e1.getHeight());
	}

	public static boolean outOfBounds(Text pText, Entity e, float factor)
	{
		return (pText.getWidth() > (factor * e.getWidth()) || pText.getHeight() > (factor * e.getHeight()));
	}

}
