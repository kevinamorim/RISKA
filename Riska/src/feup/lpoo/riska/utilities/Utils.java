package feup.lpoo.riska.utilities;

import java.util.Random;

import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

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
	
	public static void wrap(Text text, Entity parent, float boundingFactor)
	{
		wrap(text, parent.getWidth(), parent.getHeight(), boundingFactor);
	}
	
	public static void wrap(Text text, float pWidth, float pHeight, float boundingFactor)
	{
		float scale = getWrapScale(text, pWidth, pHeight, boundingFactor);

		text.setScale(scale);
		//child.setSize(child.getWidth() * scale, child.getHeight() * scale);
	}

	public static void wrap(Entity child, Entity parent, float boundingFactor)
	{
		wrap(child, parent.getWidth(), parent.getHeight(), boundingFactor);
	}

	public static void wrap(Entity child, float pWidth, float pHeight, float boundingFactor)
	{
		float scale = getWrapScale(child, pWidth, pHeight, boundingFactor);

		//child.setScale(scale);
		child.setSize(child.getWidth() * scale, child.getHeight() * scale);
	}

	public static void getWrapScale(Entity child, Entity parent, float boundingFactor)
	{
		getWrapScale(child, parent.getWidth(), parent.getHeight(), boundingFactor);
	}

	public static float getWrapScale(Entity child, float pWidth, float pHeight, float boundingFactor)
	{
		if(child.getWidth() / pWidth > child.getHeight() / pHeight)
		{
			// Dealing in X
			return boundingFactor * pWidth / child.getWidth();
		}
		else
		{
			// Dealing in Y
			return boundingFactor * pHeight / child.getHeight();
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
		return getRightBoundsX(e.getX(), e);
	}

	public static float getRightBoundsX(float pX, Entity e)
	{
		return (pX + 0.5f * getScaledWidth(e));
	}

	public static float getLeftBoundsX(Entity e)
	{
		return getLeftBoundsX(e.getX(), e);
	}

	public static float getLeftBoundsX(float pX, Entity e)
	{
		return (pX - 0.5f * getScaledWidth(e));
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

	/**
	 * Works with scale
	 */
	public static float getScaledWidth(Entity e)
	{
		return (Math.abs(e.getScaleX() * e.getWidth()));
	}

	/**
	 * Works with scale
	 */
	public static float getScaledHeight(Entity e)
	{
		return (Math.abs(e.getScaleY() * e.getHeight()));
	}

	public static float getCenterX(Entity e)
	{
		return (0.5f * e.getWidth());
	}

	public static float getCenterY(Entity e)
	{
		return (0.5f * e.getHeight());
	}

	public static float getScaledCenterX(Entity e)
	{
		return (0.5f * getScaledWidth(e));
	}

	public static float getScaledCenterY(Entity e)
	{
		return (0.5f * getScaledHeight(e));
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

	public static void fill(long[] array, long value)
	{
		for(int i = 0; i < array.length; i++)
		{
			array[i] = value;
		}
	} 

	public static <X> boolean inArray(X value, X[] array)
	{
		for(int i = 0; i < array.length; i++)
		{
			if(array[i] == value)
			{
				return true;
			}
		}

		return false;
	}

	public static boolean inArray(int value, int[] array)
	{
		for(int i = 0; i < array.length; i++)
		{
			if(array[i] == value)
			{
				return true;
			}
		}

		return false;
	}

	public static <X> void saveFromTo(X[] from, X[] to)
	{		
		for(int i = 0; i < from.length && i < to.length; i++)
		{
			to[i] = from[i];
		}
	}

	public static void saveFromTo(boolean[] from, boolean[] to)
	{		
		for(int i = 0; i < from.length && i < to.length; i++)
		{
			to[i] = from[i];
		}
	}

	public static void slideX(Entity e, float distance)
	{
		e.setX(e.getX() + distance);
	}

	// ======================================================
	// ======================================================
	public static float left(Entity e)
	{
		return e.getX() - 0.5f * e.getWidth();
	}
	
	public static float right(Entity e)
	{
		return e.getX() + 0.5f * e.getWidth();
	}
	
	public static float top(Entity e)
	{
		return e.getY() + 0.5f * e.getHeight();
	}
	
	public static float bottom(Entity e)
	{
		return e.getY() - 0.5f * e.getHeight();
	}

	public static float halfX(Entity e)
	{
		return 0.5f * e.getWidth();
	}
	
	public static float halfY(Entity e)
	{
		return 0.5f * e.getHeight();
	}
	
	public static class OtherColors {
		
		public final static Color BLACK = new Color(0.1f, 0.1f, 0.1f);
		public final static Color DARK_GREY = new Color(0.3f, 0.3f, 0.3f);
		public final static Color GREY = new Color(0.5f, 0.5f, 0.5f);
		public final static Color LIGHT_GREY = new Color(0.7f, 0.7f, 0.7f);
		public final static Color WHITE = new Color(0.9f, 0.9f, 0.9f);
	}
}
