package feup.lpoo.riska.utilities;

import java.util.Random;

import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import feup.lpoo.riska.logic.MainActivity;

public class Utils
{

	private static MainActivity activity = MainActivity.getSharedInstance();

	private static Random r = new Random();

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

	// ======================================================
	// ======================================================

	public static boolean isValidTouch(long lastTouch, long firstTouch, long minTouchInterval)
	{
		return ((lastTouch - firstTouch) > minTouchInterval);
	}

	public static String getString(int resID)
	{
		return activity.getString(resID);
	}

	// ======================================================
	// ======================================================

	public static String wrapText(Font pFont, String pString, float maxWidth, VertexBufferObjectManager vbom) {

		Text pText = new Text(0, 0, pFont, pString, 1000, vbom);

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

	public static void wrap(Entity child, Entity parent, float boundingFactor)
	{
		wrap(child, parent.getWidth(), parent.getHeight(), boundingFactor);
	}

	public static void wrap(Entity child, float pWidth, float pHeight, float boundingFactor)
	{
		if(child.getWidth() / pWidth > child.getHeight() / pHeight)
		{
			// Dealing in X
			child.setScale(boundingFactor * pWidth / child.getWidth());
		}
		else
		{
			// Dealing in Y
			child.setScale(boundingFactor * pHeight / child.getHeight());
		}
	}

	public static void expand(Entity child, Entity parent, float boundingFactor)
	{
		expand(child, parent.getWidth(), parent.getHeight(), boundingFactor);
	}

	public static void expand(Entity child, float pWidth, float pHeight, float boundingFactor)
	{
		if(child.getWidth() / pWidth < child.getHeight() / pHeight)
		{
			// Dealing in X
			child.setScale(boundingFactor * pWidth / child.getWidth());
		}
		else
		{
			// Dealing in Y
			child.setScale(boundingFactor * pHeight / child.getHeight());
		}
	}

	public static boolean outOfBounds(Entity child, Entity parent, float factor)
	{
		return (child.getWidth() > (factor * parent.getWidth()) || child.getHeight() > (factor * parent.getHeight()));
	}

	public static boolean notFilling(Entity child, Entity parent, float factor)
	{
		return (child.getWidth() < (factor * parent.getWidth()) || child.getHeight() < (factor * parent.getHeight()));
	}

	// ======================================================
	// ======================================================

	public static float getRightBoundsX(Entity e)
	{
		return getRightBoundsX(e.getX(), e.getWidth());
	}

	public static float getRightBoundsX(float pX, float pWidth)
	{
		return (pX + 0.5f * pWidth);
	}

	public static float getLeftBoundsX(Entity e)
	{
		return getLeftBoundsX(e.getX(), e.getWidth());
	}

	public static float getLeftBoundsX(float pX, float pWidth)
	{
		return (pX - 0.5f * pWidth);
	}

	public static float getUpperBoundsY(Entity e)
	{
		return getUpperBoundsY(e.getY(), e.getHeight());
	}

	public static float getUpperBoundsY(float pY, float pHeight)
	{
		return (pY + 0.5f * pHeight);
	}

	public static float getLowerBoundsY(Entity e)
	{
		return getLowerBoundsY(e.getY(), e.getHeight());
	}

	public static float getLowerBoundsY(float pY, float pHeight)
	{
		return (pY - 0.5f * pHeight);
	}

	// ======================================================
	// ======================================================

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

	// ======================================================
	// ======================================================

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

	// ======================================================
	// ======================================================
	public static <X> void fill(X[] array, X value)
	{
		for(int i = 0; i < array.length; i++)
		{
			array[i] = value;
		}
	}
	
	public static void fill(int[] array, int value)
	{
		for(int i = 0; i < array.length; i++)
		{
			array[i] = value;
		}
	}
}
