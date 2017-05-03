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
		}
		
		List<List<Integer>> inputSets = Util.chopWithGap(hits, Constants.SET_BREAK);
		
		List<Integer> input = new ArrayList();
		for (List<Integer> l : inputSets) {
			input.add(l.size());
		}
		
		List<Integer> path = count(input, target);
		System.out.println(path);
		
		List<List<Integer>> combinedSets = Util.combineMultiSets(inputSets, path);
		for (List<Integer> set : combinedSets) {
			System.out.println(Util.toHMS(set.get(0)) + "   to "
					+ Util.toHMS(set.get(set.size() - 1)));
		}
		System.out.println();
	}
	
	private void analyzeSets(int[][] limits) {
		List<List<Integer>> sets = Util.chopWithLimits(hits, limits);
		for (int i = 0; i < sets.size(); i++) {
			List<Integer> set = sets.get(i);
			SetAnalyzer sa = new SetAnalyzer(set, m.getSet(i), 0);
			sa.analyzeSet();
		}
	}
	
	
	
	private List<Integer> count(List<Integer> input, List<Integer> target) {
		MultiSetCounter msc = new MultiSetCounter();
		return msc.count(input, target);
	}
}