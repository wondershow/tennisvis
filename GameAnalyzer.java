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
		String fromAndTo = Util.toHMS(hits.get(0)) + " to " + 
	                       Util.toHMS(hits.get(hits.size() - 1));
		//System.out.println("Analyzing a Game, hits start from " 
	    //                   + fromAndTo  + " hits : " +  hits.size());
		long start = System.currentTimeMillis();
		
		List<Integer> target = new ArrayList();
		for (int i = 0; i < g.points.size(); i++) {
			target.add(g.points.get(i).getShots());
		}
		
		List<List<Integer>> inputPoints = 
				Util.chopWithGap(hits, Constants.PLAY_GAP);
		
		
		for (int i = 0; i < inputPoints.size(); i++) {
			List<Integer> point = inputPoints.get(i);
			//System.out.println(i + ":" + point.size() + " = " + Util.toHMS(point.get(0)) + "-" 
			//				+ Util.toHMS(point.get(point.size() - 1)));
		}
		
		List<Integer> input = new ArrayList();
		for (List<Integer> l : inputPoints) {
			input.add(l.size());
		}
		int n = input.size();
		int m = target.size();
		
		/*
		if (m + n > 32) {
			System.out.println("m = " + m + ", n = "+ n +", time elapsed in ms : TLE" );
			return;
		}*/
		
		List<Integer> mset = count(input, inputPoints, target);
		List<List<Integer>> plays = Util.combineMultiSets(inputPoints, mset);
		for (int i = 0; i < plays.size(); i++) {
			List<Integer> play = plays.get(i);
			Point p = g.points.get(i);
			if (play.size() != 0) {
				p.setAligned(true);
				p.setStart(play.get(0));
				p.setEnd(play.get(play.size() - 1));
			}
			//System.out.println(p.toString());
		}
		
		long end = System.currentTimeMillis();
		long elapsed = end - start;
		System.out.println("m = " + m + ", n = "+ n +", time elapsed in ms : " + elapsed);
		System.out.println();
	}
	
	private List<Integer> count(List<Integer> input, List<List<Integer>> inputPoints, List<Integer> target) {
		MultiSetCounter msc = new MultiSetCounter();
		return msc.count(input, inputPoints, target);
	}
}
