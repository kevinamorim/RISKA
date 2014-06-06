package feup.lpoo.riska.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.util.Log;
import feup.lpoo.riska.R;
import feup.lpoo.riska.logic.MainActivity;

public class FileRead {
	
	MainActivity activity;

	public FileRead(String filename, ArrayList<String> data) {

		try {
			
			activity = MainActivity.getSharedInstance();

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

}