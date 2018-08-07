package com.test.kmlparser.GRPMC;

/*
import java.util.*;
import java.awt.Point;

public class PointTracker {
	private List<Point> points;
	
	// simplified. Actually. I can be the map of scales with
	// lists
	
	private List<Point> scale1;  // < 2
	private List<Point> scale2;  // < 4
	private List<Point> scale3; //  < 8
	
	public PointTracker() 
	{
		points = new ArrayList<Point>();
		scale1 = new ArrayList<Point>();
 		scale2 = new ArrayList<Point>();
		scale3 = new ArrayList<Point>();
	}
	
	private Double calcSquare(Point p1, Point p2, Point p3) {
		double rt = 0.5 * Math.abs( 
				( (p1.getX() - p3.getX()) * (p2.getY() - p3.getY()) -
				( (p1.getY() - p3.getY()) * (p2.getX() - p3.getX())) )	);
		return new Double(rt);
	}
	
	private void addHelper(List<Point> lst, Point p,
			double scale1, double scale2, List<Point> toIns) {
		if(lst.size() > 1) {
			Point p1 = lst.get(lst.size() - 2);
			Point p2 = lst.get(lst.size() - 1);
			Double s = calcSquare(p1, p2, p);
			System.out.println("p1(" + Integer.toString(p1.x) + "," + Integer.toString(p1.y) + ") " +
					           "p2(" + Integer.toString(p2.x) + "," + Integer.toString(p2.y) + ") " + 
					           "p3(" + Integer.toString(p.x) + "," + Integer.toString(p.y) + ") " + 
					           "sqrt :" + Double.toString(s.doubleValue()));
			if(scale1 < s.doubleValue() && scale2 > s.doubleValue()) {
				toIns.add(p);
			}
		}
	}
	
	public void add(Point p) {
		
		// first point must be set for all 
		if(points.isEmpty()) {
			points.add(p);
			scale1.add(p);
			scale2.add(p);
			scale3.add(p);
			return;
		}
		
		addHelper(scale2, p, 4.0, 8.0, scale3);
		addHelper(scale1, p, 2.0, 4.0, scale2);
		addHelper(points, p, 0.0, 2.0, scale1);
		
		points.add(p);
	}
	
	public List<Point> get1Scale() {
		return scale1;
	}
	
	public List<Point> get2Scale() {
		return scale2;
	}
	
	public List<Point> get3Scale() {
		return scale3;
	}
}
*/