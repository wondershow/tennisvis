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
	public static void main(String args[]) throws UnsupportedAudioFileException, IOException {
		
		String path_p2p_desc = "/Users/leizhang/Desktop/tennis/winbledon/match_stats/winbeldon_2014.pointbypoint.txt";
		Match m = PointToPointParser.parseMatchFacts(path_p2p_desc);
		
		for (int i = 0; i < 5; i++) {
			System.out.println(i + " : " + m.getSet(i).getTotalShots());
		}
		//m.getSet(0).printSet();
		//System.out.println(m.getSet(0).getTotalShots());
		
		AcousticHitParser ap = new AcousticHitParser();
		ap.alignSet(m);
		HoorayRater.rate(m);
		
		m.reset();
		Point p = m.nextPoint();
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		while (p != null) {
			if (p.getGameOrder() == 0 && p.getPointOrder() == 0) {
				System.out.println("Set " + (p.getSetOrder() + 1)
				+ ", Game " + (p.getGameOrder() + 1) + ", Point : "
				+ (p.getPointOrder() + 1));
				System.out.println();
			}
			
			if (p.getPointOrder() == 0) {
				System.out.println("Set " + (p.getSetOrder() + 1)
				+ ", Game " + (p.getGameOrder() + 1) + ", Point : "
				+ (p.getPointOrder() + 1));
				System.out.println();
			}
			
			int max_diff = (int)(Constants.AUDIO_TXT_MIN_DURATION.get(p.getShots())
					   * (double)Constants.SAMPLE_RATE);
			
			int duration = (p.getEnd() - p.getStart());
			double secs = (double)(p.getEnd() - p.getStart()) / (double)44100;
			System.out.println(p.getSetOrder() + " : " + p.getGameOrder() + " : " + p.getPointOrder()
			+ " : ("+ p.getAligned() +") : " + Util.toHMS(p.getStart()) +
			" ---->  " +  Util.toHMS(p.getEnd()) + " " + p.getShots()
			+ ", duration = " + duration + ", secs = " + secs + ", rating = " + p.getHooray());
			p = m.nextPoint();
		}
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
					if (Math.abs(minTime - val) < Constants.PEAK_GAP) {
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
	 * Given the cross-correlation of a signal, 
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
		    		if (a > Constants.HIT_THRESHOLD) {
		    			if (res.size() == 0) {
		    				res.add(cur);
		    			} else if (cur - last > Constants.HITS_GAP) {
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
				//pw.write(toHMS(i));// + ",");
				pw.print(i);
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
				pw.write(Util.toHMS(times[0]) + "," + Util.toHMS(times[1]));
				pw.print("\n");
			}
			pw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * return the start time and end time of each play
	 * (from serve to last hit)
	 * **/
	private static List<int[]> getPlay(List<Integer> hits) {
		List<int[]> res = new ArrayList();
		
		int lastHit = hits.get(0);
		int begin = lastHit;
		for (int i = 1; i < hits.size(); i++) {
			if (hits.get(i) - lastHit > Constants.SAMPLE_RATE * Constants.PLAY_GAP) {
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
			if (tmp.size() == 0 || 
				play[0] - tmp.get(tmp.size() - 1)[1] > Constants.LONG_BREAK * Constants.SAMPLE_RATE) {
				getGamesHelper(tmp, Constants.breakStyle[j++], res);
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
			if (start - last > Constants.SHORT_BREAK * Constants.SAMPLE_RATE && i - lastIndex > Constants.LEAST_PLAY_IN_GAME) {
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
			if (hits.get(res) - last > Constants.PLAY_GAP * Constants.SAMPLE_RATE) {
				break;
			}
			last = hits.get(res);
			res++;
		}
		return res - 1;
	}
}


