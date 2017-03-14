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
	public static final int HIT_THRESHOLD = 4400;
	
	//the minimum gap between two ball hits 1000 (sample rate is 44100)
	public static final int HITS_GAP = 5000;
	
	//the minimum gap between same peak but different correlations
	public static final int PEAK_GAP = 20000;
	
	//minimum seconds between two plays;
	public static final int PLAY_GAP = 8;
	
	public static void main(String args[]) throws UnsupportedAudioFileException, IOException {
		String path_NDBH = "/Users/leizhang/Desktop/tennis/winbledon/xcorr_res/set1/xcorr_ND_backhand.txt";
		String path_NDS = "/Users/leizhang/Desktop/tennis/winbledon/xcorr_res/set1/xcorr_ND_serve.txt";
		String path_NDFH = "/Users/leizhang/Desktop/tennis/winbledon/xcorr_res/set1/xcorr_ND_forehand.txt";
		String path_RFBH = "/Users/leizhang/Desktop/tennis/winbledon/xcorr_res/set1/xcorr_RF_backhand.txt";
		String path_RFS = "/Users/leizhang/Desktop/tennis/winbledon/xcorr_res/set1/xcorr_RF_serve.txt";
		String path_RFFH = "/Users/leizhang/Desktop/tennis/winbledon/xcorr_res/set1/xcorr_RF_forehand.txt";
		
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
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000 );
		outputCSV(hits, "1.csv");
		outputCSV2(plays, "2.csv");
		System.out.println(hits.size());
	}
	
	/**
	 * Try to return all ball hitting moments in the game
	 * 
	 * **/
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
}


