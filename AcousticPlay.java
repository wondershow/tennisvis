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
	
	public AcousticPlay (List<Integer> moments) {
		hits = moments.size();
		begin = moments.get(0);
		end = moments.get(hits - 1);
		plays = new ArrayList(moments);
	}
}
