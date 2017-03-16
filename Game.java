/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 14, 2017
 */

import java.util.ArrayList;
import java.util.List;

public class Game
{
	List<Point> points;
	
	public Game() {
		points = new ArrayList();
	}
	
	public void addPoint(Point p) {
		points.add(p);
	}
	
	/*
	public String toString() {
		String StringBuilder sb = new StringBuilder("");
		
		for (Point p : points) {
			
		}
		
		return res
	}*/
	
	public List<Integer> toList() {
		List<Integer> res = new ArrayList<Integer>();
		for (Point point : points) {
			res.add(point.getShots());
		}
		return res;
	}
}
