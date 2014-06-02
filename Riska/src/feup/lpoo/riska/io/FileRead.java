package feup.lpoo.riska.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import feup.lpoo.riska.MainActivity;
import feup.lpoo.riska.R;

public class FileRead {

	public FileRead(String filename, String[] data) {

		try {

			FileInputStream fstream = new FileInputStream(filename);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String csvSplitBy = MainActivity.getSharedInstance().getResources()
					.getString(R.string.csvSplitBy);

			String strLine;

			// Read file line by line.
			while((strLine = br.readLine()) != null) {

				data = strLine.split(csvSplitBy);

			}
			
			for(String s : data) {
				System.out.println(s);
			}

			in.close();

		} catch (Exception e) {

			System.err.println("Error: " + e.getMessage());

		}
	}

}