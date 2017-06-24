import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 14, 2017
 */

public class TennisSet implements Serializable
{
	private int setNo;
	public List<TennisGame> games;
	
	public TennisSet(int order) {
		setNo = order;
		games = new ArrayList();
	}
	
	public TennisGame addGame() {
		TennisGame g = new TennisGame(games.size());
		g.setSetOrder(setNo);
		games.add(g);
		return g;
	}
	
	public List<Integer> toList() {
		List<Integer> res = new ArrayList<Integer>();
		for (TennisGame g : games) {
			List<Integer> l = g.toList();
			res.addAll(l);
		}
		return res;
	}
	
	//index from 0 
	public List<TennisGame> getGames(int from, int to) {
		List<TennisGame> res = new ArrayList();
		for (int i = from; i <= to; i++) {
			res.add(games.get(i));
		}
		return res;
	}
	
	public List<TennisGame> getGames(int from) {
		List<TennisGame> res = new ArrayList();
		for (int i = from; i < games.size(); i++) {
			res.add(games.get(i));
		}
		return res;
	}
	
	public String outputAlignment() {
		String res = "";
		for (TennisGame g : games) {
			res += g.outputAlignment() + "\n";
		}
		return res;
	}
	
	public int getTotalShots() {
		int res = 0;
		for (TennisGame g : games) {
			res += g.getTotalShots();
		}
		return res;
	}
	
	public void printSet() {
		for (TennisGame g : games) {
			System.out.println(g.toString());
		}
	}
	
	public int getSetNo() {
		return setNo;
	}
}
