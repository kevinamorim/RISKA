package feup.lpoo.riska.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.util.Log;
import feup.lpoo.riska.R;
import feup.lpoo.riska.logic.MainActivity;

/**
 * Class that handles the reading of external files.
 *
 */
public class FileRead {
	
	static MainActivity activity = MainActivity.getSharedInstance();;

	/**
	 * Reads the given filename to the given ArrayList data
	 * 
	 * @param filename : filename to read
	 * @param data : container to store the data of the file
	 */
	public static void ReadCSV(String filename, ArrayList<String> data) {
		
		Log.d("File Read", "Filename: " + filename);

		try {

			BufferedReader br = new BufferedReader(
					new InputStreamReader(activity.getAssets().open(filename)));

			String csvSplitBy = activity.getResources()
					.getString(R.string.csvSplitBy);

			String strLine;

			// Read file line by line.
			br.readLine();
			while((strLine = br.readLine()) != null) {

				String[] split = strLine.split(csvSplitBy);
				
				for(String s : split) {
					data.add(s);
				}
				
				data.add("#");

			}		

			br.close();

		} catch (Exception e) {

			Log.e("File Read", "Error: " + e.getMessage());

		}
	}

	public static void ReadDES(String filename, ArrayList<String> data) {
		
		Log.d("File Read", "Filename: " + filename);

		try {

			BufferedReader br = new BufferedReader(
					new InputStreamReader(activity.getAssets().open(filename)));

			String strLine;

			// Read file line by line.
			br.readLine();
			while((strLine = br.readLine()) != null)
			{
				data.add(strLine);
				
				data.add("#");
			}		

			br.close();

		} catch (Exception e) {

			Log.e("File Read", "Error: " + e.getMessage());

		}
	}
	
}