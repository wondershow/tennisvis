import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Apr 10, 2017
 */

public class AcousticSet
{
	private int start, end;
	private List<Integer> hits;
	
	public static void main(String[] args)
	{
		//TODO Auto-generated method stub
	}
	
	public AcousticSet() {
		hits = new ArrayList();
	}
	
	public void setStart(int s) {
		start = s;
	}
	
	public void setEnd(int e) {
		end = e;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}
	
	public void addHit(int moment) {
		hits.add(moment);
	}
}
