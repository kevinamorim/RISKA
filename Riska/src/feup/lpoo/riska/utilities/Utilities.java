package feup.lpoo.riska.utilities;

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
	
}
