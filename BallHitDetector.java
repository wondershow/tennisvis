import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 5, 2017
 */

public class BallHitDetector
{
	public static final int SAMPLE_RATE = 44100;
	public static final double HIT_THRESHOLD_NORM = 0.4;
	public static final int SCALE = 10000;
	public static final int HIT_THRESHOLD = 3900;
	
	//the minimum gap between two ball hits 1000 (sample rate is 44100)
	public static final int HITS_GAP = 5000;
	
	//the minimum gap between same peak but different correlations
	public static final int PEAK_GAP = 10000;
	
	//minimum seconds between two plays;
	public static final int PLAY_GAP = 8;
	
	//
	public static final int[] breakStyle = new int[] {3, 2, 2, 2, 2};
	
	//duration of a short break
	public static final int SHORT_BREAK = 25;
	
	//duration of a long break
	public static final int LONG_BREAK = 80;
	
	// at least 4 plays in a game
	public static final int LEAST_PLAY_IN_GAME = 4;
	
	public static void main(String args[]) throws UnsupportedAudioFileException, IOException {
		String path_NDBH = "/Users/leizhang/Desktop/tennis/winbledon/xcorr_res/set1/xcorr_ND_backhand.txt";
		String path_NDS = "/Users/leizhang/Desktop/tennis/winbledon/xcorr_res/set1/xcorr_ND_serve.txt";
		String path_NDFH = "/Users/leizhang/Desktop/tennis/winbledon/xcorr_res/set1/xcorr_ND_forehand.txt";
		String path_RFBH = "/Users/leizhang/Desktop/tennis/winbledon/xcorr_res/set1/xcorr_RF_backhand.txt";
		String path_RFS = "/Users/leizhang/Desktop/tennis/winbledon/xcorr_res/set1/xcorr_RF_serve.txt";
		String path_RFFH = "/Users/leizhang/Desktop/tennis/winbledon/xcorr_res/set1/xcorr_RF_forehand.txt";
		String path_p2p_desc = "/Users/leizhang/Desktop/tennis/winbledon/match_stats/winbeldon_2014.pointbypoint.txt";
		
		long start = System.currentTimeMillis();
		
		List<List<Integer>> lists = new ArrayList();
		lists.add(getPeaks(path_NDBH));
		lists.add(getPeaks(path_NDS));
		lists.add(getPeaks(path_NDFH));
		lists.add(getPeaks(path_RFBH));
		lists.add(getPeaks(path_RFS));
		lists.add(getPeaks(path_RFFH));
		
		List<Integer> hits = getHitmoments(lists);
		
		List<int[]> plays = getPlay(hits);
		List<int[]> games = getGames(plays);
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000 );
		outputCSV(hits, "1.csv");
		outputCSV2(plays, "2.csv");
		outputCSV2(games, "games.csv");
		Match m = PointToPointParser.parseMatchFacts(path_p2p_desc);
		alignEachPlay(m, hits);
		System.out.println(hits.size());
	}
	
	
	
	
	/**
	 * Try to return all ball hitting moments in the match
	 ***/
	public static List<Integer> getHitmoments(List<List<Integer>> peaks) {
		int size = peaks.size();
		List<Integer> res = new ArrayList();
		int[] cursors = new int[size];
		
		while (true) {
			int countOfFull = 0;
			int minTime = Integer.MAX_VALUE;
			for (int i = 0; i < size; i++) {
				if (cursors[i] < peaks.get(i).size()) {
					minTime = Math.min(minTime, peaks.get(i).get(cursors[i]));
				} else {
					countOfFull++;
				}
			}
			if (countOfFull == size) break;
			
			//voting technique
			int votes = 0;
			for (int i = 0; i < size; i++) {
				if (cursors[i] < peaks.get(i).size()) {
					int val = peaks.get(i).get(cursors[i]);
					if (Math.abs(minTime - val) < PEAK_GAP) {
						cursors[i]++;
						votes++;
					}
				}
			}
			
			if (votes > 1) {
				res.add(minTime);
			}
		}
		return res;
	}
	
	/***
	 * Given the cross-correlation of a  signal, 
	 * return all the peak moments of that cross correlation
	 * */
	public static List<Integer> getPeaks(String path) {
		List<Integer> res = new ArrayList<Integer>();
		int cur = 1;
		
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    int last = 0, max = 0;
		    while ((line = br.readLine()) != null) {
		    		// process the line.
		    		int a = Math.abs(Integer.parseInt(line.trim()));
		    		if (a > HIT_THRESHOLD) {
		    			if (res.size() == 0) {
		    				res.add(cur);
		    			} else if (cur - last > HITS_GAP) {
		    				res.add(cur);
		    			}
		    			last = cur;
		    		}
		    		cur++;
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static void outputCSV(List<Integer> list, String fileName) {
		try {
			PrintWriter pw = new PrintWriter(new File(fileName));
			for (Integer i : list) {
				pw.write(toHMS(i));// + ",");
				//pw.write(i + " ");
				pw.print("\n");
			}
			pw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void outputCSV2(List<int[]> list, String fileName) {
		try {
			PrintWriter pw = new PrintWriter(new File(fileName));
			for (int[] times : list) {
				pw.write(toHMS(times[0]) + "," + toHMS(times[1]));
				pw.print("\n");
			}
			pw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Covert a moment to hh:mm:ss
	public static String toHMS(int time) {
		time = time / 44100;
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
	 * return the start time and end time of each play
	 * (from serve to last hit)
	 * **/
	public static List<int[]> getPlay(List<Integer> hits) {
		List<int[]> res = new ArrayList();
		
		int lastHit = hits.get(0);
		int begin = lastHit;
		for (int i = 1; i < hits.size(); i++) {
			if (hits.get(i) - lastHit > SAMPLE_RATE * PLAY_GAP) {
				res.add(new int[] {begin, lastHit});
				begin = hits.get(i);
			}
			lastHit = hits.get(i);
		}
		res.add(new int[] {begin, lastHit});
		return res;
	}
	
	/**
	 * Given all the plays, return the start end time of each game.
	 ***/
	public static List<int[]> getGames(List<int[]> plays) {
		List<int[]> res = new ArrayList();
		List<int[]> tmp = new ArrayList();
		tmp.add(plays.get(0));
		int j = 0;
		
		for (int i = 1; i < plays.size(); i++) {
			int[] play = plays.get(i);
			if (tmp.size() == 0 || play[0] - tmp.get(tmp.size() - 1)[1] > LONG_BREAK * SAMPLE_RATE) {
				getGamesHelper(tmp, breakStyle[j++], res);
				tmp.clear();
			}
			tmp.add(play);
		}
		
		return res;
	}
	
	/**
	 * Given the plays, try to split it into numGames games
	 * **/
	public static void getGamesHelper(List<int[]> plays, int numGames, List<int[]> res) {
		int begin = plays.get(0)[0];
		int last = plays.get(0)[1];
		int lastIndex = 0;
		for (int i = 1; i < plays.size(); i++) {
			int start = plays.get(i)[0];
			if (start - last > SHORT_BREAK * SAMPLE_RATE && i - lastIndex > LEAST_PLAY_IN_GAME) {
				res.add(new int[] {begin, last});
				begin = start;
			}
			last = plays.get(i)[1];
		}
		res.add(new int[] {begin, last});
	}
	
	/**
	 * Given a serve hit moment, find its end moment(index)
	 * **/
	private static int getEndOfPlay(List<Integer> hits, int startIndex) {
		int res = startIndex;
		int last = hits.get(res);
		while (res < hits.size()) {
			if (hits.get(res) - last > PLAY_GAP * SAMPLE_RATE) {
				break;
			}
			last = hits.get(res);
			res++;
		}
		return res - 1;
	}
	
	/**
	 * Given the match report(from txt stats input)
	 * and detected hits, try to align all the 
	 * **/
	private static void alignEachPlay(Match m, List<Integer> hits) {
		int audioStart = 0;
		
		//System.out.println(hits.size());
		while (audioStart < hits.size()) {
			Point p = m.nextPoint();
			int shots = p.getShots();
			System.out.println("shots = " + shots);
			int audioEnd = getEndOfPlay(hits, audioStart);
			int audiohits = audioEnd - audioStart + 1;
			
			//when the detected acoustic shots equals txt desc
			if (audiohits == shots) {
				p.setAligned(true);
				p.setStart(hits.get(audioStart));
				p.setEnd(hits.get(audioEnd));
				// move to next play
				audioStart = audioEnd + 1;
			} else {
				//when acoustic shots more than txt desc
				if (audiohits > shots) {
					p.setAligned(true);
					p.setStart(hits.get(audioStart));
					p.setEnd(hits.get(audioEnd));
				} else {
					int nextPointEnds = getEndOfPlay(hits, audioEnd + 1);
					int nextPointPlays = nextPointEnds - audioEnd;
					
					int gapCur = Math.abs(audiohits - shots);
					int gapNext = Math.abs(nextPointPlays - shots);
					
					if (gapCur < gapNext) { // align point with current acoustic play
						p.setAligned(true);
						p.setStart(hits.get(audioStart));
						p.setEnd(hits.get(audioEnd));
						audioStart = audioEnd + 1;
					} else { // align point with next acoustic play
						p.setAligned(true);
						p.setStart(hits.get(audioEnd + 1));
						p.setEnd(hits.get(nextPointEnds));
						audioStart = nextPointEnds + 1;
					}
				}
			}
			System.out.println(p.getStart() / 44100 + " - " + p.getEnd() / 44100 );
		}
	}
}


