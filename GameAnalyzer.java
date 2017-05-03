import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * May 2, 2017
 */

public class GameAnalyzer
{
	TennisGame g;
	List<Integer> hits;
	int offset;
	
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
	}
	
	public GameAnalyzer(List<Integer> moments, TennisGame t, int ofset) {
		offset = ofset;
		hits = moments;
		g = t;
	}
	
	public void analyzeGame() {
		List<Integer> target = new ArrayList();
		for (int i = 0; i < g.points.size(); i++) {
			target.add(g.points.get(i).getShots());
		}
		
		List<List<Integer>> inputPoints = 
				Util.chopWithGap(hits, Constants.SHORT_BREAK);
		
		List<Integer> input = new ArrayList();
		for (List<Integer> l : inputPoints) {
			input.add(l.size());
		}
		List<Integer> mset = count(input, target);
		List<List<Integer>> plays = Util.combineMultiSets(inputPoints, mset);
		for (int i = 0; i < plays.size(); i++) {
			List<Integer> play = plays.get(i);
			Point p = g.points.get(i);
			if (play.size() != 0) {
				p.setAligned(true);
				p.setStart(play.get(0));
				p.setEnd(play.get(play.size() - 1));
			}
			System.out.println(p.toString());
		}
		
	}
	
	private List<Integer> count(List<Integer> input, List<Integer> target) {
		MultiSetCounter msc = new MultiSetCounter();
		return msc.count(input, target);
	}
}
