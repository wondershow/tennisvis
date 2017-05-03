import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * May 1, 2017
 */

public class SetAnalyzer
{
	List<Integer> hits;
	int offset;
	TennisSet set;
	
	public static void main(String[] args)
	{
		
	}
	
	public SetAnalyzer(List<Integer> moments, TennisSet t, int ofset) {
		offset = ofset;
		hits = moments;
		set = t;
	}
	
	//Try to chop hit moments into games
	public void analyzeSet() {
		//shot size of each changeover in match facts(p2p description)
		List<Integer> target = new ArrayList();
		int index = 0;
		for (int i = 0; i < Constants.breakStyle.length; i++) {
			int changeoverShots = 0;
			for (int j = 0; j < Constants.breakStyle[i]; j++) {
				changeoverShots += set.games.get(index).getTotalShots();
				index++;
				if (index == set.games.size()) break;
			}
			target.add(changeoverShots);
			if (index == set.games.size()) break;
		}
		
		//input changeovers
		List<List<Integer>> inputCOs = Util.chopWithGap(hits, Constants.LONG_BREAK);
		List<Integer> input = new ArrayList();
		for (List<Integer> l : inputCOs) {
			input.add(l.size());
		}
		
		List<Integer> path = count(input, target);
		List<List<Integer>> combinedCOs = Util.combineMultiSets(inputCOs, path);
		for (List<Integer> co : combinedCOs) {
			if (co.size() == 0) System.out.println("skip");
			else
			System.out.println(Util.toHMS(co.get(0)) + "   to "
					+ Util.toHMS(co.get(co.size() - 1)));
		}
		//System.out.println(path);
	}
	
	public void analyzeSet(int[][] limits) { 
		List<List<Integer>> games = Util.chopWithLimits(hits, limits);
		for (int i = 0; i < games.size(); i++) {
			
		}
	}
	
	private void analyzeChangeOver() {
		
	}
	
	private List<Integer> count(List<Integer> input, List<Integer> target) {
		MultiSetCounter msc = new MultiSetCounter();
		return msc.count(input, target);
	}
}
