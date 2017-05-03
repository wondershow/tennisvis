import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Apr 13, 2017
 */

public class Util {
	/**
	 * Convert hh:mm:ss to sample time moment 
	 ***/
	public static int toMoments(String hms) {
		int offset = 3;
		String[] tmp = hms.split(":");
		int res = 0;
		for (int i = 0; i < tmp.length; i++) {
			int cur = Integer.parseInt(tmp[i]);
			res = res * 60 + cur;
		}
		
		res = res  * Constants.SAMPLE_RATE;
		return res;
	}
	
	/***
	 * Covert a moment to hh:mm:ss
	 */
	public static String toHMS(int time) {
		time = time / Constants.SAMPLE_RATE;
		int hour = time / 3600;
		
		time = time - hour * 3600;
		int min = time / 60;
		
		int sec = time % 60;
		
		String hms = (hour == 0 ? "" : (hour + ":")) 
					+  (min < 10 ? "0" + min : min)  + ":" 
					+  (sec < 10 ? "0" + sec : sec);
		//System.out.println(hms);
	    return hms;
	}
	
	/**
	 * Load integral numerical values from a csv file, 
	 * add offset to each value if specified.
	 ****/
	public static List<Integer> loadCSVInts(String path, int offset) {
		List<Integer> res = new ArrayList();
		
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    		res.add(Integer.parseInt(line.trim()) + offset);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	/**
	 * Chop a time serious data into different lists, each 
	 * list is delimited by a start and end time
	 * ***/
	public static List<List<Integer>> chopWithLimits(List<Integer> moments, int[][] limits) { 
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		int index = 0;
		for (int[] limit : limits) {
			List<Integer> tmp = new ArrayList<Integer>();
			while (moments.get(index) < limit[0]) index++;
			while (index < moments.size() && moments.get(index) < limit[1]) {
				tmp.add(moments.get(index));
				index++;
			}
			res.add(tmp);
		}
		return res;
	}
	
	/**
	 * Given a time serious data, split it into pieces, 
	 * where each piece(internal list in return value)
	 * is at least breaktime away from another
	 * **/
	public static List<List<Integer>> chopWithGap(List<Integer> rawhits, int breaktime) {
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
	
	
	public static List<List<Integer>> combineMultiSets(List<List<Integer>> list, 
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
}
