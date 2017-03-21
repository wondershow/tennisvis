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
	
	//index from 0 
	public List<Game> getGames(int from, int to) {
		List<Game> res = new ArrayList();
		for (int i = from; i <= to; i++) {
			res.add(games.get(i));
		}
		return res;
	}
	
	public List<Game> getGames(int from) {
		List<Game> res = new ArrayList();
		for (int i = from; i < games.size(); i++) {
			res.add(games.get(i));
		}
		return res;
	}
	
	public String outputAlignment() {
		String res = "";
		for (Game g : games) {
			res += g.outputAlignment() + "\n";
		}
		return res;
	}
	
	public int getTotalShots() {
		int res = 0;
		for (Game g : games) {
			res += g.getTotalShots();
		}
		return res;
	}
	
	public void printSet() {
		for (Game g : games) {
			System.out.println(g.toString());
		}
	}
}
