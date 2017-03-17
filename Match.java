import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 14, 2017
 */

public class Match
{
	List<Set> sets;
	private int lastset = 0, lastgame = 0, lastpoint = 0, total = 0; 
	
	public Match() {
		sets = new ArrayList();
	}
	
	public Set addSet() {
		Set s = new Set(sets.size());
		sets.add(s);
		return s;
	}
	
	
	public Point nextPoint() {
		//Point res = sets.get(lastset).games.get(lastgame).points.get(lastpoint)
		if (lastset == sets.size()) return null;
		Set s = sets.get(lastset);
		Game g = s.games.get(lastgame);
		Point res = g.points.get(lastpoint);
		System.out.println(total + ":" + lastset + " : " + lastgame + " : " + lastpoint +  ":" + g.points.size());
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
}
