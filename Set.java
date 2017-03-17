import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 14, 2017
 */

public class Set
{
	private int setNo;
	public List<Game> games;
	
	public Set(int order) {
		setNo = order;
		games = new ArrayList();
	}
	
	public Game addGame() {
		Game g = new Game(games.size());
		g.setSetOrder(setNo);
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
