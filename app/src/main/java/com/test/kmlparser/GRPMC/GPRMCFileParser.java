package com.test.kmlparser.GRPMC;

import android.util.Log;
import com.google.android.gms.maps.model.LatLng;

import java.io.*;

public class GPRMCFileParser {
	BufferedReader bf = null;
	
	public GPRMCFileParser(String path) {
		try {
			File f = new File(path); 
			bf = new BufferedReader(new FileReader(f));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void process() {
		GPRMCParser parser = new GPRMCParser();
		try {
			String line = bf.readLine();
			while(line != null) {
				if(parser.parse(line)) {
					LatLng p = parser.getLatLng();
					Log.d("GPRMCFileParser", "lat : " + Double.toString(p.latitude) + ", lng : " + Double.toString(p.longitude));
				}
				line = bf.readLine();
			}
			bf.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
}
