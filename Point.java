import java.io.Serializable;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 14, 2017
 */

public class Point implements Serializable
{
	private int startTime, endTime;
	private int rallyShots, game, set;
	private boolean audioAligned, serveFault;
	private int pointNo;
	private int gameNo, setNo;
	private String start, end;
	private int hooray;
	private String txtPoint;
	
	public Point () {
		
	}
	
	public void setTextPoint(String txt) {
		txtPoint = txt;
	}
	
	public String getTxtPnt() {
		return txtPoint;
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
	
	public void setHooray(int r) {
		this.hooray = r;
	}
	
	public int getHooray() {
		return this.hooray;
	}
	
	public String toString() {
		String res = "";
		res = "Set " + (setNo) + " Game " + (gameNo) + " Point " 
		             + this.pointNo + " : ("+ this.audioAligned+")" + this.getShots()
		             + ", from = " + Util.toHMS(startTime)
		             + ", to = " + Util.toHMS(endTime) + " score " + this.txtPoint;
		return res;
	}
	
	//private String getDuration()
}