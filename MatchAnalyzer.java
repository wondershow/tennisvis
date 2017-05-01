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
		ma.analyzeSet();
	}
	
	public MatchAnalyzer(String p2p, String hpath, int ofset) {
		offset = ofset;
		p2pFilePath = p2p;
		hitsFilePath = hpath;
		m = PointToPointParser.parseMatchFacts(p2p);
		hits = Util.loadCSVInts(hpath, ofset);
	}
	
	private void analyzeSet() {
		List<Integer> target = new ArrayList();
		for (Set s : m.sets) {
			target.add(s.getTotalShots());
		}
		List<List<Integer>> inputSets = splitMoments(hits, Constants.SET_BREAK);
		
		List<Integer> input = new ArrayList();
		for (List<Integer> l : inputSets) {
			input.add(l.size());
		}
		
		List<Integer> path = count(input, target);
		System.out.println(path);
		List<List<Integer>> combinedSets = combineMultiSets(inputSets, path);
		for (List<Integer> set : combinedSets) {
			System.out.println(Util.toHMS(set.get(0)) + "   to "
					+ Util.toHMS(set.get(set.size() - 1)));
		}
		System.out.println();
	}
	
	private List<List<Integer>> combineMultiSets(List<List<Integer>> list, 
			List<Integer> setSize) {
		List<List<Integer>> res = new ArrayList();
		
		int index = 0;
		for (int i = 0; i < setSize.size(); i++) {
			List<Integer> tmp = new ArrayList();
			int size = setSize.get(i);
			for (int j = index; j < index + size; j++) {
				tmp.addAll(list.get(j));
			}
			index += size;
			res.add(tmp);
		}
		return res;
	}
	
	/**
	 * Given a time serious data, split it into pieces, 
	 * where each piece(internal list in return value)
	 * is at least breaktime away from another
	 * **/
	private List<List<Integer>> splitMoments(List<Integer> rawhits, int breaktime) {
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		List<Integer> tmp = new ArrayList<Integer>();
		tmp.add(rawhits.get(0));
		int gap = breaktime * Constants.SAMPLE_RATE;
		for (int i = 1; i < rawhits.size(); i++) {
			if (rawhits.get(i) - rawhits.get(i - 1) > gap) {
				res.add(tmp);
				tmp = new ArrayList<Integer>();
			}
			tmp.add(rawhits.get(i));
		}
		res.add(tmp);
		return res;
	}
	
	private List<Integer> count(List<Integer> input, List<Integer> target) {
		MultiSetCounter msc = new MultiSetCounter();
		return msc.count(input, target);
	}
}
