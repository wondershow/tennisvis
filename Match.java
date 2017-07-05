import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 14, 2017
 */

public class Match implements Serializable
{
	List<TennisSet> sets;
	private int lastset = 0, lastgame = 0, lastpoint = 0, total = 0; 
	
	
	public Match() {
		sets = new ArrayList();
	}
	
	public void reset() {
		lastset = 0;
		lastgame = 0;
		lastpoint = 0;
		total = 0;
	} 
	
	public TennisSet addSet() {
		TennisSet s = new TennisSet(sets.size());
		sets.add(s);
		return s;
	}
	
	public TennisSet getSet(int setOrder) {
		return sets.get(setOrder);
	}
	
	
	public Point nextPoint() {
		//Point res = sets.get(lastset).games.get(lastgame).points.get(lastpoint)
		if (lastset == sets.size()) return null;
		TennisSet s = sets.get(lastset);
		TennisGame g = s.games.get(lastgame);
		Point res = g.points.get(lastpoint);
		//System.out.println(total + ":" + lastset + " : " + lastgame + " : " + lastpoint +  ":" + g.points.size());
		lastpoint++;
		total++;
		if (lastpoint == g.points.size()) {
			lastpoint = 0;
			lastgame++;
			if (lastgame == s.games.size()) {
				lastgame = 0;
				lastset++;
			}
		}
		return res;
	}
	
	public void printHighlights() {
		reset();
		Point p = this.nextPoint();
		while (p != null) {
			
			///*
			System.out.println("set: " + p.getSetOrder() + ", game : "
					+ p.getGameOrder() + ", " + p.getTxtPnt() 
					+ ", time = " +  Util.toHMS(p.getStart(), 4073));
			//*/
			//if (p.getTxtPnt() != null)
			//	System.out.println(Util.toHMS(p.getStart(), 4073));
			p = this.nextPoint();
		}
	}
}
