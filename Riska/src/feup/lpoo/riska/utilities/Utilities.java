package feup.lpoo.riska.utilities;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;

import android.util.Log;
import feup.lpoo.riska.logic.MainActivity;

public class Utilities {
	
	private static MainActivity activity = MainActivity.getSharedInstance();

	public static boolean isValidTouch(long lastTouch, long firstTouch, long minTouchInterval)
	{
		return ((lastTouch - firstTouch) > minTouchInterval);
	}

	public static String getString(int resID) {
		return activity.getString(resID);
	}
	
	public static String wrapText(Font pFont, String pString, float maxWidth) {

		Text pText = new Text(0, 0, pFont, pString, 1000, activity.getVertexBufferObjectManager());
		
		Log.d("Riska","Text width: " + pText.getWidth() + " of " + maxWidth);
		
		if(pText.getWidth() < maxWidth) {
			return pString;
		}

		// Split the entire string into separated words.
		String[] words = pText.getText().toString().split(" ");

		String wrappedText = ""; /* Final string. */
		String line = ""; /* Temp variable */

		for(String word : words) {

			pText.setText(line + word);
			
			Log.d("Riska","Text width: " + pText.getWidth() + " of " + maxWidth);
			
			if(pText.getWidth() > maxWidth) {			
				wrappedText += line + "\n\n";
				line = "";
			}

			line += word + " ";

		}

		wrappedText += line;

		return wrappedText;
	}
}
