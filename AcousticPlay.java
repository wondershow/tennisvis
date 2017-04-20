import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 17, 2017
 */

public class AcousticPlay
{
	public int begin, end, hits;
	public List<Integer> plays;
	public boolean aligned = false;
	public String startTime, endTime;
	
	public AcousticPlay (List<Integer> moments) {
		hits = moments.size();
		begin = moments.get(0);
		end = moments.get(hits - 1);
		startTime = Util.toHMS(begin);
		endTime = Util.toHMS(end);
		plays = new ArrayList(moments);
	}
	
	private String moments() {
		StringBuilder sb = new StringBuilder("");
		int i = 0;
		for (int moment : plays) {
			sb.append( i + " : "  + moment + " : " 
		     + Util.toHMS(moment) + ". ");
			i++;
		}
		sb.setLength(0);
		return sb.toString();
	}
	
	
	public String toString() {
		return startTime + " : " + endTime + ", " + plays.size() + " shots "
			   + (end - begin) + ", secs = " 
				+ ((double) (end - begin) / (double)44100) + "\n" + moments() + "\n";
	}
}
