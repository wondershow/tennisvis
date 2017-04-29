/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Apr 13, 2017
 */

public class Util {
	/**
	 * Convert hh:mm:ss to sample time moment 
	 ***/
	public static int toMoments(String hms) {
		int offset = 3;
		String[] tmp = hms.split(":");
		int res = 0;
		for (int i = 0; i < tmp.length; i++) {
			int cur = Integer.parseInt(tmp[i]);
			res = res * 60 + cur;
		}
		
		res = res  * Constants.SAMPLE_RATE;
		return res;
	}
	
	/***
	 * Covert a moment to hh:mm:ss
	 */
	public static String toHMS(int time) {
		time = time / Constants.SAMPLE_RATE;
		int hour = time / 3600;
		
		time = time - hour * 3600;
		int min = time / 60;
		
		int sec = time % 60;
		
		String hms = (hour == 0 ? "" : (hour + ":")) 
					+  (min < 10 ? "0" + min : min)  + ":" 
					+  (sec < 10 ? "0" + sec : sec);
		//System.out.println(hms);
	    return hms;
	}
}
