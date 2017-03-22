import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 20, 2017
 */

public class AcousticTextAlignment
{

	public static void main(String[] args) {
		///*
		int[] acustic = new int[] {21, 6, 15, 11, 16};
		int[] texts = new int[] {28, 31, 14};
		
		//*/
		
		/*
		int[] acustic = new int[] {3, 4, 7};
		int[] texts = new int[] {6, 8};
		//*/
		
		List<Integer> l1 = new ArrayList();
		List<Integer> l2 = new ArrayList();
		for (int i = 0; i < acustic.length; i++) l1.add(acustic[i]);
		for (int i = 0; i < texts.length; i++) l2.add(texts[i]);
		
		List<Integer> res = alignGames2(l1, l2);
		System.out.println(res.size());
		for (int i : res) {
			System.out.println(res);
		}
	}
	
	
	private static int minCost;
	private static List<Integer> res;
	/**
	 * Problem Statement: Given two inputs, acousticGames and textGames
	 *    each contains how many ball hits in each GAME, try to align
	 *    acousticGames with textGames
	 * e.g. 1. acousticGames [13, 15, 31, 28] (4 games detected, 13 hits in 1st....)  
	 *         textGames [27, 33, 28] (3 games, 27 hits in 1st game)
	 *      since textGames is the ground truth, we have to merge
	 *      game 1 and game 2 in  acousticGames making it [28, 31, 28].
	 *         
	 * **/
	private static List<Integer> alignGames2(List<Integer> acousticGames, List<Integer> textGames) {
		minCost = Integer.MAX_VALUE;
		res = new ArrayList();
		List<Integer> path = new ArrayList<Integer>();
		alignGames2Helper(acousticGames, textGames, path, 0);
		return res;
	}
	
	public static List<Integer> alignGames(List<Integer> acousticGames, List<Game> games) {
		if (acousticGames.size() == games.size()) return acousticGames;
		List<Integer> textGames = new ArrayList<Integer>();
		for (Game g : games) textGames.add(g.getTotalShots());
		return alignGames2(acousticGames, textGames);
	}
	
	private static void alignGames2Helper(List<Integer> acousticGames, List<Integer> textGames, List<Integer> cur, int index1) {
		if (index1 == acousticGames.size()) {
			if (cur.size() == textGames.size()) {
				int cost = 0;
				for (int i = 0; i < cur.size(); i++) {
					cost += Math.abs(cur.get(i) - textGames.get(i));
				}
				if (cost < minCost) {
					minCost = cost;
					res = new ArrayList(cur);
				}
			}
			return;
		}
		
		if (cur.size()  >= textGames.size()) return;
		
		int sum = 0;
		for (int i = index1; i < acousticGames.size(); i++) {
			sum += acousticGames.get(i);
			cur.add(sum);
			alignGames2Helper(acousticGames, textGames, cur, i + 1);
			cur.remove(cur.size() - 1);
		}
	}
	
	/*
	private static List<Integer> alignment(List<Integer> acousticGames, List<Integer> textGames) {
		minCost = Integer.MAX_VALUE;
		res = new ArrayList();
		List<Integer> path = new ArrayList<Integer>();
		helper(acousticGames, textGames, path, 0);
		return res;
	}*/
	
	//
	private static List<Integer> res1;
	public static void alignGame(List<AcousticPlay> plays, Game textGame) {
		List<Integer> path = new ArrayList<Integer>();
		//List<Integer> res = new ArrayList();
		int[] cost = new int[] {Integer.MAX_VALUE};
		alignGameHelper(plays, textGame.points, path, 0, cost);
		
		int cur = 0;
		for (int i = 0; i < textGame.points.size(); i++) {
			int len = res1.get(i);
			if (len > 0) {
				textGame.points.get(i).setAligned(true);
				textGame.points.get(i).setStart(plays.get(cur).begin);
				textGame.points.get(i).setEnd(plays.get(cur + len - 1).end);
			}
			cur += len;
		}
		//return res;
	}
	
	private static void alignGameHelper(List<AcousticPlay> plays, List<Point> points, List <Integer> path, int index, int[] cost) {
		//System.out.println(points.size() + " " + index +" : " + path);
		if (path.size() == points.size()) {
			int thisCost = computeCost(path, plays, points);
			if (thisCost < cost[0]) {
				cost[0] = thisCost;
				res1 = new ArrayList(path);
			}
			return;
		}
		if (path.size() >= points.size()) return;
		int j = path.size();
		for (int i = index; i <= plays.size(); i++) {
			int size = i - index;
			if (size > 0) {
				int duration = plays.get(i - 1).end - plays.get(index).begin;
				int rallyShots = points.get(j).getShots();
				if (duration > 3 * (rallyShots - 1) * Constants.MAX_HITS_GAP) {
					//System.out.println("Duration too large size = " + size + ", index = " + index);
					//System.out.println((i - 1) + "th play in audio tries align with " + j + "th text point");
					break;
				}
			}
			path.add(size);
			alignGameHelper(plays, points, path, i, cost);
			path.remove(path.size() - 1);
		}
	}
	
	private static int computeCost(List <Integer> path, List<AcousticPlay> plays, List<Point> points) {
		//System.out.println("asdf");
		int cost = 0;
		int cur = 0;
		for (int i = 0; i < path.size(); i++) {
			int len = path.get(i);
			int aHits = 0;
			for (int j = cur; j < cur + len; j++) {
				aHits += plays.get(j).hits;
			}
			cost += Math.abs(points.get(i).getShots() - aHits);
			cur = cur + len;
		}
		
		return cost;
	}
}
