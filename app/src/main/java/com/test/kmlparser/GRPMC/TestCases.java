package com.test.kmlparser.GRPMC;

//import java.awt.Point;

public class TestCases {
	/*
	public void TrackerTest() {
		
		String testFiles[] = {
					"coords1.txt",
					"coords2.txt",
					"coords3.txt"
				};
		
		for(int i =0; i < testFiles.length; ++i ) {
			PointsLoader pl = new PointsLoader(testFiles[i]);
			PointTracker pt = new PointTracker();
			for(Point p : pl.getCoords()) {
				pt.add(p);
			}
			
			System.out.println("========================================================");
			
			System.out.println("First scales points :");
			for(Point p : pt.get1Scale()) {
				System.out.println("x :" + Integer.toString(p.x) + 
						           ", y:" + Integer.toString(p.y));
			}
			
			System.out.println("Second scales points :");
			for(Point p : pt.get2Scale()) {
				System.out.println("x :" + Integer.toString(p.x) + 
						           ", y:" + Integer.toString(p.y));
			}
			
			System.out.println("Third scales points :");
			for(Point p : pt.get2Scale()) {
				System.out.println("x :" + Integer.toString(p.x) + 
						           ", y:" + Integer.toString(p.y));
			}
			
		}
	}

		*/
	public void GPRMCParserTest () {
		//In the original one it sends data in ASCII (1 byte)
		// not char(UTF-16) as it in Java by default
		GPRMCFileParser p = new GPRMCFileParser("D5_1_GPS-2018-04-22.txt");
		p.process();
	}
}
