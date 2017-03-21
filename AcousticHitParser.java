import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 17, 2017
 */

public class AcousticHitParser
{
	List<Integer> hits;
	Match match;
	public AcousticHitParser() {
		//match = m;
		//hits = h;
	}
	
	public void alignSet(List<Integer> h, Match m) {
		Set firstSet = m.getSet(0);
		chopHitsInSet(h, firstSet);
		System.out.println(firstSet.outputAlignment());
	}
	
	//Try to chop hit moments into games
	private void chopHitsInSet(List<Integer> h, Set set) {
		List<Integer> tmp = new LinkedList();
		int segments = 0;
		int gameFrom = 0;
		for (int i = 0; i < h.size(); i++) {
			if (i < h.size() - 1 && h.get(i + 1) - h.get(i)
				> Constants.SAMPLE_RATE * Constants.LONG_BREAK) {
				int secs = h.get(i) / 44100;
				System.out.println("Long break : "  + secs / 60 + ":" + secs % 60);
				alignGames(tmp, set.getGames(gameFrom, gameFrom + Constants.breakStyle[segments] - 1));
				gameFrom = gameFrom + Constants.breakStyle[segments++];
				tmp.clear();
			}
			if (segments == 4) {
				alignGames(h.subList(i, h.size() - 1), set.getGames(gameFrom));
				break;
			}
			tmp.add(h.get(i));
		}
	}
	
	/**
	 * Align a few games with hits moment
	 * */
	private void alignGames(List<Integer> h, List<Game> games) {
		int gamesInAcoustic = 1;
		int begin = 0;
		System.out.print("fact " + games.size() + "games : ");
		
		List<Integer> acusticGamesSize = new ArrayList<Integer>();
		
		for (int i = 1; i < h.size(); i++) {
			if (h.get(i) - h.get(i - 1) > Constants.SAMPLE_RATE * Constants.SHORT_BREAK) {
				acusticGamesSize.add(i - begin);
				begin = i;
			}
			if (i == h.size() - 1) {
				acusticGamesSize.add(i - begin + 1);
			}
		}
		
		List<Integer> acusticGames = AcousticTextAlignment.alignGames(acusticGamesSize, games);
		
		for (int i = 0; i < acusticGames.size(); i++) {
			System.out.println(games.get(i).getOrder() + " : " 
		                     + games.get(i).getTotalShots() + " vs " + acusticGames.get(i));
		}
		
		int cur = 0;
		for (int i = 0; i < games.size(); i++) {
			List<Integer> hits = h.subList(cur, cur + acusticGames.get(i) - 1);
			
			AcousticTextAlignment.alignGame(, games.get(i));
			cur = cur + acusticGames.get(i);
		}
	}
	
	private void debugPrintHitBreaks(List<Integer> h) {
		int begin = 0;
		for (int i = 1; i < h.size() ; i++) {
			if (h.get(i) - h.get(i - 1) > Constants.SAMPLE_RATE * Constants.SHORT_BREAK) {
				System.out.println(BallHitDetector.toHMS(h.get(begin)) 
						+ " - " + BallHitDetector.toHMS(h.get(i - 1)) + "," + (i - 1 - begin) + "shots");
				begin = i;
			}
			if (i == h.size() - 1) {
				System.out.println(BallHitDetector.toHMS(h.get(begin)) 
						+ " - " + BallHitDetector.toHMS(h.get(i)) + "," + (i - begin) + "shots");
			}
		}
	}
	
	
	/*
	public static List<Integer> getPlayHits(List<Integer> hits) {
		List<Integer> res = new ArrayList();
		int begin = 0;
		for (int i = 1; i < hits.size(); i++) {
			if (hits.get(i) - hits.get(i - 1) > Constants.SAMPLE_RATE * Constants.PLAY_GAP)
		}
		return res;
	}*/
	
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
}
