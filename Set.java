import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 14, 2017
 */

public class Set
{
	public List<Game> games;
	
	public Set() {
		games = new ArrayList();
	}
	
	public Game addGame() {
		Game g = new Game();
		games.add(g);
		return g;
	}
	
	public List<Integer> toList() {
		List<Integer> res = new ArrayList<Integer>();
		for (Game g : games) {
			List<Integer> l = g.toList();
			res.addAll(l);
		}
		return res;
	}
}
