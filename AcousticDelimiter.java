import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Apr 10, 2017
 */

public class AcousticDelimiter
{

	public static void main(String[] args)
	{

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
	
}
