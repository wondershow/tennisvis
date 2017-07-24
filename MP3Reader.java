import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 20, 2017
 */

public class MP3Reader
{
	public static void main(String[] args) {
		String path = "/Users/leizhang/Desktop/tennis/winbledon/video_clips/winbeldon.mp3";
		
		try {
		File file = new File(path);
		AudioInputStream in= AudioSystem.getAudioInputStream(file);
		AudioInputStream din = null;
		AudioFormat baseFormat = in.getFormat();
		AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 
		                                            baseFormat.getSampleRate(),
		                                            16,
		                                            baseFormat.getChannels(),
		                                            baseFormat.getChannels() * 2,
		                                            baseFormat.getSampleRate(),
		                                            false);
		din = AudioSystem.getAudioInputStream(decodedFormat, in);
		while (true){
		    int currentByte = din.read();
		    if (currentByte == -1) break;
		    // Handling code
		    din.
		    
		}
		
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
