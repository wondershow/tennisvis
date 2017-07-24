import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Apr 30, 2017
 */

public class MultiSetCounter
{

	public static void main(String[] args)
	{
		int[] hits = new int[]{306, 98, 168, 186, 49, 283, 36, 149};
		int[] target = new int[]{437, 337, 319, 406, 299};
		List<Integer> l1 = new ArrayList();
		List<Integer> l2 = new ArrayList();
		for (int i : hits) l1.add(i);
		for (int i : target) l2.add(i);
		
		List<Integer> res = count(l1, l2);
		System.out.println(res);
	}
	
	/***
	 *  Solution to the original Counting of Multiset problem,
	 *  Should be noticed that, the size of the input should
	 *  not be too big, since the time complexity is o(n!), so 
	 *  the size of the input should be under 20
	 ***/
	static List<Integer> countRes;
	
	public static List<Integer> count(List<Integer> input, List<List<Integer>> inputMoments, List<Integer> target) {
		countRes = new ArrayList();
		int[] minCost = new int[] {Integer.MAX_VALUE};
		countHelper(input, inputMoments, target, new ArrayList(), 0, minCost);
		return countRes;
	}
	
	public static List<Integer> count(List<Integer> input, List<Integer> target) {
		countRes = new ArrayList();
		int[] minCost = new int[] {Integer.MAX_VALUE};
		countHelper(input, null, target, new ArrayList(), 0, minCost);
		return countRes;
	}
	
	public static void countHelper(List<Integer> input, 
			List<List<Integer>> inputMoments, List<Integer> target, 
			List<Integer> setSize, int index, int[] minCost) {
			//System.out.println(index);
			if (index == input.size()) {
				if (setSize.size() == target.size()) {
					int cost = computeCost(input, target, setSize);
					if (cost < minCost[0]) {
						//System.out.println(setSize);
						countRes = new ArrayList(setSize);
						minCost[0] = cost;
					}
					return;
				}
			}
			if (setSize.size() >= target.size()) return;
			for (int i = index; i <= input.size(); i++) {
				if (inputMoments != null && i != index) { 
					//apply the recursion pruning rule
					List<Integer> hits = inputMoments.get(i - 1);
					int lastHitMoment = 
							hits.get(hits.size() - 1);
					int firstHitMoment = 
							inputMoments.get(index).get(0);
					int hitnum = target.get(setSize.size());
					
					if (testMerge(hitnum, firstHitMoment, lastHitMoment)) {
						setSize.add(i - index);
						countHelper(input, inputMoments, target, setSize, i, minCost);
						setSize.remove(setSize.size() - 1);
					}
				} else {
					setSize.add(i - index);
					countHelper(input, inputMoments, target, setSize, i, minCost);
					setSize.remove(setSize.size() - 1);
				}
			}
	}
	
	/**
	 * Given two time moments, start and end, test if these
	 * two moment can be considered as in single play.
	 * if the time gap is too large or too small, it will return false;
	 * **/
	private static boolean testMerge(int hits, int start, int end) {
		if (hits == 1) return start == end;
		int gaps = hits - 1;
		int time = end - start;
		
		//gap too large?
		if (time > ((gaps * Constants.SAMPLE_RATE) << 1)) return false;
		
		//gap too small?
		//if (time < ((gaps * Constants.SAMPLE_RATE) >> 1)) return false;
		return true;
	}
	
	public static int computeCost(List<Integer> input, List<Integer> target, List<Integer> setSize) {
		int res = 0;
		int index = 0;
		for (int i = 0; i < setSize.size(); i++) {
			int sum = 0;
			for (int j = 0; j < setSize.get(i); j++) {
				sum += input.get(index);
				index++;
			}
			res += Math.abs(sum - target.get(i));
		}
		return res;
	}
}
