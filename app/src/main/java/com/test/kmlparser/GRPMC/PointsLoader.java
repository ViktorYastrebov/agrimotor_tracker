package com.test.kmlparser.GRPMC;

/*
import java.util.*;
import java.io.*;
import java.awt.Point;


public class PointsLoader {
	
	private List<Point> m_points;
	
	public PointsLoader(String path) {
		m_points = new ArrayList<Point>();
		
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			
			String line = br.readLine();
			while(line != null) {
				String coords[] = line.split(",");
				if(coords.length >= 2) {
					Point p = new Point(Integer.parseInt(coords[0]),
										Integer.parseInt(coords[1]));
					m_points.add(p);
				}
				line = br.readLine();
			}
			br.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	List<Point> getCoords() {
		return m_points;
	}
}
*/
