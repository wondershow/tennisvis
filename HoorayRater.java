import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Apr 2, 2017
 */

public class HoorayRater
{
	public static void main(String[] args) {
		
	}
	
	public static void rate(Match m) {
		FileInputStream inputStream = null;
		Scanner sc = null;
		String path = "/Users/leizhang/Desktop/tennis/winbledon/video_clips/winbeldon_set1.txt";
		//List<Point> points = m.getSet(0).getGames(0);
		
		Point p = m.nextPoint();
		int sample = 0, rating = 0, sampleSum = 0, count = 0;
		boolean track = false;
		try {
			inputStream = new FileInputStream(path);
		    sc = new Scanner(inputStream, "UTF-8");
		    
		    while (sc.hasNextLine() && p != null) {
		        int val = Math.abs(Integer.parseInt(sc.nextLine()));
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
		        
		        if (count == 4410) {
		        		int hooray = sampleSum / 4410;
		        		sampleSum = 0;
		        		if (hooray >= 250) {
		        			rating++;
		        		}
		        		
		        		count = 0;
		        		//either the track tag is true or the time is within 5 secs
				    //after the last hit of that point
		        		if (hooray < 250 && sample > p.getEnd() + Constants.SAMPLE_RATE * 5 ) {
		        			track = false;
		        			p.setHooray(rating);
		        			System.out.println(BallHitDetector.toHMS(p.getStart()) + " : " + rating);
		        			while (true) {
		        				p = m.nextPoint();
		        				if (p == null) break;
		        				if (p.getAligned()) break;
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
}
