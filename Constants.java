import java.util.HashMap;
import java.util.Map;

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
	
	public static final int MAX_HITS_GAP = 53000; // around 1.2 secs
	
	//the minimum gap between same peak but different correlations
	public static final int PEAK_GAP = 10000;
	
	//minimum seconds between two plays;
	public static final int PLAY_GAP = 3;
	
	//
	public static final int[] breakStyle = new int[] {3, 2, 2, 2, 2, 2};
	
	//duration of a short break
	public static final int SHORT_BREAK = 25;
	
	//duration of a long break
	public static final int LONG_BREAK = 90;
	
	//where all the input files are located
	public static final String DOC_ROOT = "/Users/"
			+ "leizhang/Desktop/tennis/winbledon/2014_final/";
	
	
	//duration of a set break
	/**
	 * 2014 Wimbeldon final
	 * set 1 to set 2 :  52:07 to 54:23
	 * set 2 to set 3 : 1:34:36 to 1:38:40
	 * set 3 to set 4 : 2:23:38 to 2:26:03
	 * set 4 to set 5 : 3:13:23 to 3:17:44
	 ***/
	public static final int SET_BREAK = 120;
	
	// at least 4 plays in a game
	public static final int LEAST_PLAY_IN_GAME = 4;
	
	
	/**
	 * This parameter specifies the max duration 
	 * of an audio play's given the number of shots 
	 * of text play.
	 * **/
	public static final Map<Integer, Integer> AUDIO_TXT_MAX_DURATION;
	
	static {
		AUDIO_TXT_MAX_DURATION = new HashMap();
		
		AUDIO_TXT_MAX_DURATION.put(1, 0);
		
		//if there are 2 hits in the text play, we can accept 
		//at most 1 to 3 hits in audio
		AUDIO_TXT_MAX_DURATION.put(2, MAX_HITS_GAP * 2);
		
		//if there are 3 hits in the text play, we can accept 
		//at  2 to 4 hits in audio
		AUDIO_TXT_MAX_DURATION.put(3, 3 * MAX_HITS_GAP);
		AUDIO_TXT_MAX_DURATION.put(4, 4 * MAX_HITS_GAP);
		for (int i = 5; i < 40; i++)
			AUDIO_TXT_MAX_DURATION.put(i, i * MAX_HITS_GAP);
    }
	
	
	//
	public static final Map<Integer, Integer> AUDIO_TXT_MIN_DURATION;
	static {
		AUDIO_TXT_MIN_DURATION = new HashMap();
		
		AUDIO_TXT_MIN_DURATION.put(1, 0);
		
		//if there are 2 hits in the text play, we can accept 1 - 3 audio hits
		//in the audio
		AUDIO_TXT_MIN_DURATION.put(2, 0);
		
		//if there are 3 hits in the text play, we can accept 2 - 4 audio hits
		AUDIO_TXT_MIN_DURATION.put(3, MAX_HITS_GAP);
		
		//if there are 4 hits in the text play, we can accept 3 - 5 audio hits
		AUDIO_TXT_MIN_DURATION.put(4, 2 * MAX_HITS_GAP);
		
		for (int i = 5; i < 40; i++)
			AUDIO_TXT_MIN_DURATION.put(i, (i - 2) * MAX_HITS_GAP);
    }
	
	
}
