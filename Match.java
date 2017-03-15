import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 14, 2017
 */

public class Match
{
	List<Set> sets;
	
	public Match() {
		sets = new ArrayList();
	}
	
	public Set addSet() {
		Set s = new Set();
		sets.add(s);
		return s;
	}
}
