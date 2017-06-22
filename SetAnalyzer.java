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
		
		for (int i = 0; i < input.size(); i++) {
			System.out.println(i + ":" + input.get(i));
		}
		
		List<Integer> path = count(input, target);
		List<List<Integer>> combinedCOs = Util.combineMultiSets(inputCOs, path);
		System.out.println("Align changeovers in Set " + set.getSetNo());
		for (int i = 0; i < combinedCOs.size(); i++) {
			System.out.print("Set " + set.getSetNo() + ", game " + i + " ");
			List<Integer> co = combinedCOs.get(i);
			if (co.size() == 0)  System.out.println(" skip ");
			else
				System.out.println(Util.toHMS(co.get(0)) + "   to "
					+ Util.toHMS(co.get(co.size() - 1)));
		}
	}
	
	public void analyzeSet(int[][] limits) {
		List<List<Integer>> games = Util.chopWithLimits(hits, limits);
		for (int i = 0; i < games.size(); i++) {
			List<Integer> game = games.get(i);
			
			GameAnalyzer ga = new GameAnalyzer(game, set.games.get(i), 0);
			ga.analyzeGame();
			
			/*
			List<Integer> hitsInGame = games.get(i);
			List<List<Integer>> plays = Util.chopWithGap(hits, Constants.PLAY_GAP);
			List<Integer> input = new ArrayList();
			for (List<Integer> p : plays) {
				input.add(p.size());
			}
			
			List<Integer> path = countWithConstrains(input, target);
			
			/*
			List<AcousticPlay> plays = getPlay(game);
			System.out.println("size of play " + plays.size());
			AcousticTextAlignment.alignGame(plays, set.games.get(i));*/
		}
	}
	
	/**
	 * return the start time and end time of each play
	 * (from serve to last hit)
	 * **/
	public static List<AcousticPlay> getPlay(List<Integer> hits) {
		List<AcousticPlay> res = new ArrayList();
		
		int lastHit = hits.get(0);
		int begin = lastHit;
		List<Integer> hitsInOnePlay = new ArrayList();
		hitsInOnePlay.add(lastHit);
		for (int i = 1; i < hits.size(); i++) {
			if (hits.get(i) - lastHit > Constants.SAMPLE_RATE * Constants.PLAY_GAP) {
				res.add(new AcousticPlay(hitsInOnePlay));
				begin = hits.get(i);
				hitsInOnePlay.clear();
			}
			lastHit = hits.get(i);
			hitsInOnePlay.add(lastHit);
		}
		res.add(new AcousticPlay(hitsInOnePlay));
		return res;
	}
	
	private List<Integer> count(List<Integer> input, List<Integer> target) {
		MultiSetCounter msc = new MultiSetCounter();
		return msc.count(input, target);
	}
	
	private List<Integer> countWithConstrains(List<Integer> input, List<Integer> target) {
		MultiSetCounter msc = new MultiSetCounter();
		return msc.count(input, target);
	}
}
