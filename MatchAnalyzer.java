import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Apr 30, 2017
 */

public class MatchAnalyzer
{
	String p2pFilePath, hitsFilePath;
	List<Integer> hits;
	int offset;
	Match m; 
	
	public static void main(String[] args) {
		String p2p = Constants.DOC_ROOT + "winbeldon_2014.pointbypoint.txt";
		String hitsPath = Constants.DOC_ROOT + "moments";
		MatchAnalyzer ma = new MatchAnalyzer(p2p, hitsPath, 0);
		//ma.analyzeSets();
		ma.analyzeSets(MatchDetails.wbd_2014final_sets);
	}
	
	public MatchAnalyzer(String p2p, String hpath, int ofset) {
		offset = ofset;
		p2pFilePath = p2p;
		hitsFilePath = hpath;
		m = PointToPointParser.parseMatchFacts(p2p);
		hits = Util.loadCSVInts(hpath, ofset);
	}
	
	private void analyzeSets() {
		List<Integer> target = new ArrayList();
		for (TennisSet s : m.sets) {
			target.add(s.getTotalShots());
			System.out.println(s.getTotalShots());
		}
		
		List<List<Integer>> inputSets = Util.chopWithGap(hits, Constants.SET_BREAK);
		
		
		for (List<Integer> set : inputSets) {
			System.out.println(set.size());
		}
		
		
		List<Integer> input = new ArrayList();
		for (List<Integer> l : inputSets) {
			input.add(l.size());
		}
		
		List<Integer> path = count(input, target);
		
		List<List<Integer>> combinedSets = Util.combineMultiSets(inputSets, path);
		
		System.out.println("Align each set with MSC algorithm:");
		for (int i = 0; i < combinedSets.size(); i++) {
			List<Integer> ls = combinedSets.get(i);
			int hitsDetected = ls.size();
			int totalHits = target.get(i);
			System.out.println("Set " + i + " : "  + Util.toHMS(ls.get(0)) + "   to "
					+ Util.toHMS(ls.get(ls.size() - 1)));
		}
		System.out.println();
		System.out.println();
	}
	
	private void analyzeSets(int[][] limits) {
		List<List<Integer>> sets = Util.chopWithLimits(hits, limits);
		for (int i = 0; i < sets.size(); i++) {
			List<Integer> set = sets.get(i);
			SetAnalyzer sa = new SetAnalyzer(set, m.getSet(i), 0);
			sa.analyzeSet();
			System.out.println(m.getSet(i).getTotalShots());
			//if (i == 0) {
				sa.analyzeSet(MatchDetails.wbd_2014final_games[i]);
			//}
		}
	}
	
	
	
	private List<Integer> count(List<Integer> input, List<Integer> target) {
		MultiSetCounter msc = new MultiSetCounter();
		return msc.count(input, target);
	}
}
