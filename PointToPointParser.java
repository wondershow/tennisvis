import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 14, 2017
 */

public class PointToPointParser
{
	public static void main(String[] args) {
		String path = "/Users/leizhang/Desktop/tennis/winbledon/match_stats/winbeldon_2014.pointbypoint.txt";
		parseMatchFacts(path);
	}
	
	public static Match parseMatchFacts(String path) {
		Match res = new Match();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    int last = 0, max = 0;
		    while ((line = br.readLine()) != null) {
		    		String[] fields = line.split("  + |\\t");
		    		System.out.println(fields.length);
		    	
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}