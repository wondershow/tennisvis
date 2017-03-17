/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 17, 2017
 */

public class Constants
{
	private Constants() {
		
	}
	
	public static final int SAMPLE_RATE = 44100;
	public static final double HIT_THRESHOLD_NORM = 0.4;
	public static final int SCALE = 10000;
	public static final int HIT_THRESHOLD = 3900;
	
	//the minimum gap between two ball hits 1000 (sample rate is 44100)
	public static final int HITS_GAP = 5000;
	
	//the minimum gap between same peak but different correlations
	public static final int PEAK_GAP = 10000;
	
	//minimum seconds between two plays;
	public static final int PLAY_GAP = 8;
	
	//
	public static final int[] breakStyle = new int[] {3, 2, 2, 2, 2};
	
	//duration of a short break
	public static final int SHORT_BREAK = 25;
	
	//duration of a long break
	public static final int LONG_BREAK = 80;
	
	// at least 4 plays in a game
	public static final int LEAST_PLAY_IN_GAME = 4;
}
