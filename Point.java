/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 14, 2017
 */

public class Point
{
	private int startTime, endTime;
	private int rallyShots, game, set;
	private boolean audioAligned, serveFault;
	
	public void setFault() {
		serveFault = true;
	}
	
	public void setShots(int i) {
		rallyShots = i;
	}
	
	public int getShots() {
		return rallyShots;
	}
	
	public boolean getFault() {
		return serveFault;
	}
	
	public void setStart(int i) {
		startTime = i;
	}
	
	public int getEnd() {
		return endTime;
	}
	
	public int getStart() {
		return startTime;
	}
	
	public void setEnd(int i) {
		endTime = i;
	}
	
	//if this point is detected in acoustic sigal
	public void setAligned(boolean b) {
		this.audioAligned = b;
	}
}