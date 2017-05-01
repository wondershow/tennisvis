import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
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
		
	}
	
	public void alignSet(Match m) {
		for (int i = 0; i < m.sets.size(); i++) {
			Set set = m.getSet(i);
			String hitPath = MatchDetails.wbd_2014final_hitpaths[i];
			List<Integer> h = loadHitMoments(hitPath, MatchDetails.wbd_2014final_offsets[i]);
			//System.out.println();
			chopHitsInSet(h, MatchDetails.wbd_2014final_games[i], set);
			System.out.println(set.outputAlignment());
			return;
		}
	}
	
	//Try to chop hit moments into games
	private void chopHitsInSet(List<Integer> h, Set set) {
		List<Integer> tmp = new LinkedList();
		int segments = 0;
		int gameFrom = 0;
		for (int i = 0; i < h.size(); i++) {
			if (i > 0 && h.get(i) - h.get(i - 1)
				> Constants.SAMPLE_RATE * Constants.LONG_BREAK) {
				int secs = h.get(i) / 44100;
				System.out.println("Long break : "  + secs / 60 + ":" + secs % 60);
				alignGames(tmp, set.getGames(gameFrom, gameFrom + Constants.breakStyle[segments] - 1));
				gameFrom = gameFrom + Constants.breakStyle[segments++];
				tmp.clear();
			}
			if (segments == 2) {
				alignGames(h.subList(i, h.size() - 1), set.getGames(gameFrom));
				break;
			}
			tmp.add(h.get(i));
		}
	}
	/***
	 * 
	 * if aux == null, auto chop of games otherwise manual chopping
	 * 
	 ***/
	private void chopHitsInSet(List<Integer> h, int[][] aux, Set set) {
		if (aux == null) {
			chopHitsInSet(h, set);
			return;
		} 
		List<Integer> tmp = new LinkedList();
		int i = 0, j = 0;
		
		for (; i < h.size(); i++) {
			//int moment = ;
			/*System.out.println(h.get(i));
			
			if (h.get(i) == 8179923) {
				System.out.println();
			}*/
			if (j >= aux.length) break;
			if(h.get(i) > aux[j][0] && h.get(i) < aux[j][1]) {
				tmp.add(h.get(i));
			} else if (h.get(i) >= aux[j][1]) {

				List<AcousticPlay> plays = getPlay(tmp);
				AcousticTextAlignment.alignGame(plays, set.games.get(j));
				tmp.clear();
				i--;
				j++;
			}
		}
	}
	
	
	/**
	 * Align a few games with hits moment
	 * Given hit moments of a few games,
	 * try to chop hit moments into sections
	 * and allocate each section into a game.
	 **/
	private void alignGames(List<Integer> h, List<Game> games) {
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
		
		List<AcousticGame> agList = AcousticGame.chopIntoAcusticGames(h);
		
		List<Integer> acusticGames = AcousticTextAlignment.alignGames(acusticGamesSize, games);
		
		
		System.out.println("acusticGames size = " + acusticGames.size() 
		                   + ", text game size = " + games.size());
		for (int i = 0; i < acusticGames.size(); i++) {
			System.out.println(games.get(i).getOrder() + " : " 
		                     + games.get(i).getTotalShots() + " vs " + acusticGames.get(i));
		}
		
		
		int cur = 0;
		for (int i = 0; i < games.size(); i++) {
			List<Integer> hits = h.subList(cur, cur + acusticGames.get(i) - 1);
			List<AcousticPlay> plays = getPlay(hits);
			AcousticTextAlignment.alignGame(plays, games.get(i));
			
			/*
			for (Point p : games.get(i).points) {
				int max_diff = (int)(Constants.AUDIO_TXT_MIN_DURATION.get(p.getShots())
						   * (double)Constants.SAMPLE_RATE);
				
				int duration = (p.getEnd() - p.getStart());
				double secs = (double)(p.getEnd() - p.getStart()) / (double)44100;
				System.out.println(p.getSetOrder() + " : " + p.getGameOrder() + " : " + p.getPointOrder()
				+ " : ("+ p.getAligned() +") : " + BallHitDetector.toHMS(p.getStart()) +
				" ---->  " +  BallHitDetector.toHMS(p.getEnd()) + " " + p.getShots()
				+ ", duration = " + duration + ", secs = " + secs);
			}*/
			cur = cur + acusticGames.get(i);
		}
	}
	
	private void debugPrintHitBreaks(List<Integer> h) {
		int begin = 0;
		for (int i = 1; i < h.size() ; i++) {
			if (h.get(i) - h.get(i - 1) > Constants.SAMPLE_RATE * Constants.SHORT_BREAK) {
				System.out.println(Util.toHMS(h.get(begin)) 
						+ " - " + Util.toHMS(h.get(i - 1)) + "," + (i - 1 - begin) + "shots");
				begin = i;
			}
			if (i == h.size() - 1) {
				System.out.println(Util.toHMS(h.get(begin)) 
						+ " - " + Util.toHMS(h.get(i)) + "," + (i - begin) + "shots");
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
	
	public static List<AcousticSet> chopSets(List<Integer> hits) {
		List<AcousticSet> res = new ArrayList<AcousticSet>();
		AcousticSet as = new AcousticSet();
		as.setStart(hits.get(0));
		as.addHit(hits.get(0));
		
		for (int i = 1; i < hits.size(); i++) {
			if (hits.get(i) - hits.get(i - 1) 
					> Constants.SAMPLE_RATE * Constants.LONG_BREAK) {
				as.setEnd(hits.get(i - 1));
				res.add(as);
			}
			as.addHit(hits.get(i));
		}
		
		return res;
	}
	
	private static List<Integer> loadHitMoments(String path, int offset) {
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
}
