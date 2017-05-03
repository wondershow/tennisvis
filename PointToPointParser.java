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
		Match m = parseMatchFacts(path);
		System.out.println("a");
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
		    TennisSet curSet = null;
		    TennisGame curGame = null;
		    while ((line = br.readLine()) != null) {
		    		if (line.length() < 20) continue;
		    		//System.out.println(line);
		    		String[] fields = line.split("  + |\\t");
		    		String player = fields[0];
		    		String set = fields[1];
		    		if (!set.equals(lastSet) && !set.equals(reverse(lastSet))) {
		    			curSet = res.addSet();
		    			lastSet = set;
		    			//System.out.println(lastSet);
		    		}
		    		
		    		String game = fields[2];
		    		if (!game.equals(lastGame)) {
		    			curGame = curSet.addGame();
		    			lastGame = game;
		    		}
		    		
		    		parseScoreDesc(curGame, fields[4]);
		    		
		    		//String gameScore = fields[3];
		    		//String scoreDesc = fields[4];
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private static void parseScoreDesc(TennisGame g, String descTxt) {
		if (descTxt.toLowerCase().contains("double fault")) {
			handleFault(g);
			handleFault(g);
			return;
		}
		if (descTxt.toLowerCase().contains("fault")) {
			handleFault(g);
			int pos = descTxt.indexOf("fault");
			while (pos < descTxt.length() && descTxt.charAt(pos) != '.') pos++;
			descTxt = descTxt.substring(pos);
		}
		Point p = new Point();
		int shots = getRallyShots(descTxt);
		p.setShots(shots);
		g.addPoint(p);
	}
	
	private static void handleFault(TennisGame g) {
		Point p = new Point();
		p.setFault();
		p.setShots(1);
		g.addPoint(p);
	}
	
	/***
	 * return how many shots in this play
	 * */
	private static int getRallyShots(String text) {
		int pos = text.indexOf("-shot rally");
		if (pos > 0) {
			int p = pos - 1;
			while (p >= 0 && Character.isDigit(text.charAt(p))) p--;
			int shots = Integer.parseInt(text.substring(p + 1, pos));
			if (text.indexOf("error") > 0) shots++;
			return shots;
		}
		int res = 1;
		for (int i = 0; i < text.length(); i++)
			if (text.charAt(i) == ';') res++;
		
		return res;
	}
	
	private static String reverse(String txt) {
		String res = "";
		
		for (int i = txt.length() - 1; i >= 0; i--)
			res += txt.charAt(i);
		
		return res;
	}
}