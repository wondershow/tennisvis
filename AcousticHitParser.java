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
			if (segments == 3) {
				alignGames(h.subList(i, h.size() - 1), set.getGames(gameFrom, gameFrom + 1));
			}
			tmp.add(h.get(i));
		}
	}
	
	/**
	 * Align a few games
	 * */
	private void alignGames(List<Integer> h, List<Game> games) {
		
		int gamesInAcoustic = 1;
		int begin = 0;
		System.out.print("fact " + games.size() + "games : ");
		for (int i = 1; i < h.size() ; i++) {
			if (h.get(i) - h.get(i - 1) > Constants.SAMPLE_RATE * Constants.SHORT_BREAK) {
				System.out.print(BallHitDetector.toHMS(h.get(begin)) 
						+ " - " + BallHitDetector.toHMS(h.get(i - 1)) + ",");
				begin = i;
				gamesInAcoustic++;
			}
		}
		System.out.println();
		
		List<AcousticPlay> list;
		if (gamesInAcoustic == games.size()) {
			int start = 0;
			int gameCur = 0;
			for (int i = 1; i < h.size() && gameCur < games.size(); i++) {
				if (h.get(i) - h.get(i - 1) > Constants.SAMPLE_RATE * Constants.SHORT_BREAK) {
					list = getPlay(h.subList(start, i - 1));
					alignGame(list, games.get(gameCur++));
					start = i;
					if (gameCur == games.size() - 2) {
						alignGame(getPlay(h.subList(start, h.size() - 1)), games.get(gameCur));
					}
				}
			}
		} else {
			int start = 0;
			int gameCur = 0;
			
			//for (Game)
			for (int i = 1; i < h.size() && gameCur < games.size(); i++) {
				if (h.get(i) - h.get(i - 1) > Constants.SAMPLE_RATE * Constants.SHORT_BREAK 
					&& games.get(gameCur).getTotalShots() - (i - start) < 10) {
					list = getPlay(h.subList(start, i - 1));
					alignGame(list, games.get(gameCur++));
					start = i;
					if (gameCur == games.size() - 2) {
						alignGame(getPlay(h.subList(start, h.size() - 1)), games.get(gameCur));
					}
				}
			}
		}
	}
	
	private void alignGame(List<AcousticPlay> plays, Game g) {
		//while (true) {
		//int order = 0, max = 0;
		int i = 0, j = 0;
		
		while (i < plays.size() && j < g.points.size()) {
			AcousticPlay play = plays.get(i);
			Point p = g.points.get(j);
			
			if (play.hits >= p.getShots()) {
				p.setStart(play.begin);
				p.setEnd(play.end);
				p.setAligned(true);
				i++;
				j++;
			} else if (j == g.points.size() - 1) {
				// alreay last point, nothing fancy
				//we just mapp all rest plays and this point
				p.setStart(play.begin);
				p.setEnd(plays.get(plays.size() - 1).end);
				p.setAligned(true);
				j++;
			} else if (i == plays.size() - 1) {
				// already last play, nothing fancy
				//we just map this play with all left points
				while (j < g.points.size()) {
					g.points.get(j).setAligned(true);
					g.points.get(j).setStart(p.getStart());
					g.points.get(j).setEnd(p.getEnd());
					j++;
				}
			} else { // play hits lower than p.shots
				AcousticPlay nextAP = plays.get(j + 1);
				if (play.hits == 1) {
					// very likely this is a false detection
					if (nextAP.hits == p.getShots()) {
						p.setStart(nextAP.begin);
						p.setEnd(nextAP.end);
						p.setAligned(true);
						j++;
						i += 2;
					} else if (nextAP.end - play.begin < Constants.SAMPLE_RATE * p.getShots()) {
						p.setStart(play.begin);
						p.setEnd(play.end);
						p.setAligned(true);
						j++;
						i++;
					} else if (p.getShots() == 2) {
						p.setAligned(true);
						p.setStart(play.begin);
						p.setEnd(play.end);
						i++;
						j++;
					} else { // missing detection of p
						i++;
					}
				} else {
					p.setStart(play.begin);
					p.setEnd(play.end);
					p.setAligned(true);
					i++;
					j++;
				}
			} 
		}
	}
	
	
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
