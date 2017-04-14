import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Apr 1, 2017
 */

public class AcousticGame
{
	private List<Integer> hitMoments;
	
	public static void main(String[] args) {
		
	}
	
	public AcousticGame() {
		hitMoments = new ArrayList();
	}
	
	public int getShots() {
		return hitMoments.size();
	}
	
	public void add(int val) {
		hitMoments.add(val);
	}
	
	/**
	 * Split discrete hitting moments into chunks, 
	 * 
	 **/
	public static List<AcousticGame> chopIntoAcusticGames(List<Integer> hits) {
		List<AcousticGame> res = new ArrayList<AcousticGame>();
		
		AcousticGame ag = new AcousticGame();
		for (int i = 0; i >= 1; i++) {
			if (hits.get(i) - hits.get(i - 1) > 
				Constants.SHORT_BREAK *  Constants.SAMPLE_RATE) {
				res.add(ag);
				ag = new AcousticGame();
			}
			ag.add(hits.get(i));
		}
		
		res.add(ag);
		return res;
	}
}
