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
	private int pointNo;
	private int gameNo, setNo;
	
	public Point () {
		
	}
	
	public void setOrder(int[] orders) {
		setNo = orders[0];
		gameNo = orders[1];
		pointNo = orders[2];
	}
	
	public int getPointOrder() {
		return pointNo;
	}
	
	public int getGameOrder() {
		return gameNo;
	}
	
	public int getSetOrder() {
		return setNo;
	}
	
	
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
	
	public boolean getAligned() {
		return this.audioAligned;
	}
}