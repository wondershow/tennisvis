/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 14, 2017
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TennisGame implements Serializable
{
	//starts from 0
	int gameNo, setNo;
	List<Point> points;
	
	public TennisGame(int order) {
		gameNo = order;
		points = new ArrayList();
	}
	
	public void addPoint(Point p) {
		p.setOrder(new int[] {setNo, gameNo, points.size()});
		points.add(p);
	}
	
	public void setSetOrder(int s) {
		setNo = s;
	}
	
	public int getOrder() {
		return gameNo;
	}
	
	public String outputAlignment() {
		String s = "Game " + gameNo + " :";
		for (Point p : points) {
			if (p.getAligned()) s += " Y ";
			else s += " N "; 
		}
		
		s += "(" + points.get(0).getStart() / 44100 + " - " 
				 + points.get(points.size() - 1).getEnd() / 44100 + ")"; 
		return s;
	}
	
	
	
	
	public int getTotalShots() {
		int res = 0;
		for (Point p : this.points) 
			res += p.getShots();
		return res;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append("Game " + this.gameNo + " : \n");
		for (Point p : points) {
			sb.append(p.toString() + "\n");
		}
		
		return sb.toString();
	}
	
	public List<Integer> toList() {
		List<Integer> res = new ArrayList<Integer>();
		for (Point point : points) {
			res.add(point.getShots());
		}
		return res;
	}
}
