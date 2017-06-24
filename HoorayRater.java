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
		topHighlights(119);
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
	
	public static void printPoints(Match m) {
		Point p;
		while ((p = m.nextPoint()) != null) {
			System.out.println(Util.toHMS(p.getEnd()));
		}
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
