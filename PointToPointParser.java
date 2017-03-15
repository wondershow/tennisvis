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
	
	/**
	 * Novak Djokovic  	0‑0	0‑0	30‑0	1st serve down the T, fault (wide). 
	 * 					2nd serve wide; forehand return crosscourt (shallow); 
	 * 					forehand down the middle; backhand approach shot crosscourt; 
	 * 					backhand slice crosscourt; backhand volley crosscourt, winner. 
	 * 					(6-shot rally)
	 * **/
	public static Match parseMatchFacts(String path) {
		Match res = new Match();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    int last = 0, max = 0;
		    
		    String lastSet = "", lastGame = "";
		    Set curSet;
		    Game curGame;
		    while ((line = br.readLine()) != null) {
		    		String[] fields = line.split("  + |\\t");
		    		String player = fields[0];
		    		String set = fields[1];
		    		if (!set.equals(lastSet)) {
		    			curSet = res.addSet()
		    		}
		    		
		    		
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/***
	 * return how many shots in this play
	 * */
	private static int getRallyShots() {
		
		
		
		
	}
}