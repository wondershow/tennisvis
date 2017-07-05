import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Apr 2, 2017
 */

public class HoorayRater
{
	static String ratePath = Constants.DOC_ROOT + "rating";
	
	public static void main(String[] args) {
		computeRatesByMoments();
	}
	
	public static int getMoment(String hms) {
		hms = hms.trim();
		if (hms.equals("1:07:45")) return -1;
		return Util.HMS2Sample(hms.trim());
	}
	
	public static String getHMS(String line) {
		String[] tmp = line.trim().split(",");
		return tmp[0];
	}

	public static void rate(Match m) {
		FileInputStream inputStream = null;
		Scanner sc = null;
		String path = "/Users/leizhang/Desktop/tennis/winbledon/video_clips/audiowave.csv";
		//List<Point> points = m.getSet(0).getGames(0);
		
		Point p = m.nextPoint();
		int sample = 0, rating = 0, sampleSum = 0, count = 0;
		boolean track = false;
		boolean debug = false;
		int time = 0;
		try {
			inputStream = new FileInputStream(path);
		    sc = new Scanner(inputStream, "UTF-8");
		    
		    while (sc.hasNextLine() && p != null) {
		        int val = Math.abs(Integer.parseInt(sc.nextLine()));
		        time++;
		        //System.out.println(sample + " : " + val);
		        if (p != null && sample == p.getEnd()) {
		        		track = true;
		        		rating = 0;
		        		sampleSum = 0;
		        }
		        
		        if (track) {
		        		sampleSum += val;
		        		count++;
		        }
		        
		        
		        if (sample % 4410000 == 0) {
		        		System.out.println(Util.toHMS(sample));
		        }
		        
		        
		        if (count == 4410) {
		        		int hooray = sampleSum / 4410;
		        		sampleSum = 0;
		        		if (hooray >= 1000) {
		        			rating++;
		        		}
		        		
		        		count = 0;
		        		//either the track tag is true or the time is within 5 secs
				    //after the last hit of that point
		        		if (hooray < 750 && sample > p.getEnd() + Constants.SAMPLE_RATE * 3 ) {
		        			track = false;
		        			p.setHooray(rating);
		        			System.out.println(Util.toHMS(p.getStart()) + " : " + Util.toHMS(p.getEnd()) + "  " + rating);
		        			if (rating == 125) debug = true;
		        			rating = 0;
		        			while (true) {
		        				p = m.nextPoint();
		        				if (p == null) break;
		        				if (p.getAligned() && p.getStart() > sample) break;
		        			}
		        		}
		        }
		        
		        sample++;
		    }
		    
		    // note that Scanner suppresses exceptions
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
		        try {
		        		inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		    if (sc != null) {
		        sc.close();
		    }
		}
	}
	
	public static void computeRatesByMoments() {
		FileInputStream inputStream = null, inputStream2 = null;
		Scanner sc = null, sc2 = null;
		String wavepath = "/Users/leizhang/Desktop/tennis/winbledon/video_clips/audiowave.csv";
		String timepath1 = "/Applications/Tomcat/webapps/tennis/WEB-INF/matches/20140706-M-Wimbledon-F-Novak_Djokovic-Roger_Federer.csv";
		//List<Point> points = m.getSet(0).getGames(0);
		
		int sample = 0, rating = 0, sampleSum = 0, count = 0;
		boolean track = false;
		boolean debug = false;
		int time = 0;
		try {
			inputStream = new FileInputStream(wavepath);
		    sc = new Scanner(inputStream, "UTF-8");
		    
		    inputStream2 = new FileInputStream(timepath1);
		    sc2 = new Scanner(inputStream2, "UTF-8");
		    
		    String curHMS = getHMS(sc2.nextLine());
		    String nextHMS = getHMS(sc2.nextLine());
		    int curMoment, nextMoment;
		    int big = 0;
		    while (sc.hasNextLine()) {
		        int val = Math.abs(Integer.parseInt(sc.nextLine()));
		        time++;
		        curMoment = getMoment(curHMS);
		        nextMoment = getMoment(nextHMS);
		        //System.out.println(sample + " : " + val);
		        if (sample == curMoment) {
		        		track = true;
		        		rating = 0;
		        		sampleSum = 0;
		        }
		        
		        /*
		        testSum += val;
		        if (sample % 4410 == 0) {
		        		if (track)
		        			System.out.println("testSum = " + testSum / 4410);
		        		testSum  = 0;
		        }*/
		        
		        if (track) {
		        		sampleSum += val;
		        		count++;
		        }
		        
		        if (count == 4410) {
		        		int hooray = sampleSum / 4410;
		        		//System.out.println("hooray = " + hooray);
		        		sampleSum = 0;
		        		if (hooray >= 1000) {
		        			rating++;
		        		}
		        		
		        		count = 0;
		        		//either the track tag is true or the time is within 5 secs
				    //after the last hit of that point
		        		if (sample >= nextMoment - 44100 * 5 
		        				|| sample >= curMoment + 44100 * 40) {
		        			//System.out.println("curMoment = " + Util.toHMS(curMoment));
		        			track = false;
		        			System.out.println(curHMS + "," + getScaleRating(rating, 240));
		        			big = Math.max(big, rating);
		        			rating = 0;
		        			curHMS = nextHMS;
		        			nextHMS = getHMS(sc2.nextLine());
		        			while (nextHMS.equals("1:07:45")) {
		        				System.out.println("1:07:45,0");
		        				nextHMS = getHMS(sc2.nextLine());
		        			}
		        		}
		        }
		        sample++;
		    }
		    
		    System.out.println("big = " + big);
		    // note that Scanner suppresses exceptions
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
		        try {
		        		inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		    if (sc != null) {
		        sc.close();
		    }
		}
	}
	
	public static void printPoints(Match m) {
		Point p;
		while ((p = m.nextPoint()) != null) {
			System.out.println(Util.toHMS(p.getEnd()));
		}
	}
	
	public static void printRates() {
		Match m = null ;
		try {
			FileInputStream streamIn = new FileInputStream(ratePath);
		    ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
		    m = (Match) objectinputstream.readObject();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		for (TennisSet s : m.sets) {
			for (TennisGame g : s.games) {
				for (Point p : g.points) {
					double norm = (double)100 * (double)p.getHooray() / (double) 264;
					int rating = (int) norm;
					System.out.println("set: " + p.getSetOrder() + ", game : "
        					+ p.getGameOrder() + ", " + p.getTxtPnt() 
        					+ ", time = " +  Util.toHMS(p.getStart(), 4065) + ", " + rating);
					
					
					//if (p.getTxtPnt() != null)
					//	System.out.println(Util.toHMS(p.getStart(), 4065));
				}
			}
		}
	}
	
	/**
	 * Compute a scale (0 - 100) rating for a unscaled one
	 * */
	public static int getScaleRating(int rating , int max) {
		double res = (double) 100 * (double) rating / (double) max;
		return (int)Math.round(res);
	}
	
	public static void topHighlights(int num) {
		Match m = null ;
		try {
			FileInputStream streamIn = new FileInputStream(ratePath);
		    ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
		    m = (Match) objectinputstream.readObject();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		List<Point> li = new LinkedList();
		for (TennisSet s : m.sets) {
			for (TennisGame g : s.games) {
				for (Point p : g.points) {
					li.add(p);
				}
			}
		}
		
		Collections.sort(li,  new Comparator<Point>() {
			public int compare(Point o1, Point o2)
			{
				return o2.getHooray() - o1.getHooray();
			}
		});
		
		System.out.println("heighlights");
		
		for (int i = 0; i < li.size() && i < num; i++) {
			Point p = li.get(i);
			System.out.println("rank: " + i + ": Set" + (p.getSetOrder() + 1) + ", Game " + (p.getGameOrder() + 1)
					+ "  " + p.getTxtPnt() + " " + 
					Util.toHMS(p.getStart()) + "  " + 
					Util.toHMS(p.getEnd()) + ", rating = " + p.getHooray());
		}
	}
}
