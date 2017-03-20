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
		int[] texts = new int[] {26, 31, 14};
		//*/
		
		/*
		int[] acustic = new int[] {3, 4, 7};
		int[] texts = new int[] {6, 8};
		//*/
		
		List<Integer> l1 = new ArrayList();
		List<Integer> l2 = new ArrayList();
		for (int i = 0; i < acustic.length; i++) l1.add(acustic[i]);
		for (int i = 0; i < texts.length; i++) l2.add(texts[i]);
		
		List<Integer> res = align(l1, l2);
		System.out.println(res.size());
		for (int i : res) {
			System.out.println(res);
		}
	}
	
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
	private static int minCost;
	private static List<Integer> res;
	public static List<Integer> align(List<Integer> acousticGames, List<Integer> textGames) {
		if (acousticGames.size() == textGames.size()) return acousticGames;
		List<Integer> path = new ArrayList<Integer>();
		minCost = Integer.MAX_VALUE;
		res = new ArrayList();
		helper(acousticGames, textGames, path, 0);
		return res;
	}
	
	private static void helper(List<Integer> acousticGames, List<Integer> textGames, List<Integer> cur, int index1) {
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
		
		int sum = 0;
		for (int i = index1; i < acousticGames.size(); i++) {
			sum += acousticGames.get(i);
			cur.add(sum);
			helper(acousticGames, textGames, cur, i + 1);
			cur.remove(cur.size() - 1);
		}
	}
}
