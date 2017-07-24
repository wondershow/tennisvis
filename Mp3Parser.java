import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Apr 2, 2017
 */

public class Mp3Parser
{

	public static void main(String[] args) {
		String path = "/Users/leizhang/Desktop/tennis/winbledon/video_clips/winbeldon_2014_final_set1.mp3";
		/*
		try {
	        File file = new File(path);
	        AudioInputStream in = AudioSystem.getAudioInputStream(file);
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

	        play(decodedFormat, din);
	        //spi(decodedFormat, in);
	        in.close();
	    } catch (Exception e) {
	        System.out.println("MP3");
	    }*/
		try {
			File file = new File(path);
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
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
			//din.
			
			
			//BufferedInputStream bis = new BufferedInputStream(is);
	        AudioInputStream ais = AudioSystem.getAudioInputStream(in);
	        AudioFormat fmt = ais.getFormat();
			
	        int sampleRate = (int) fmt.getSampleRate();
	        int format = fmt.getChannels();
	        
	        System.out.println(sampleRate);
	        System.out.println(format);
	        
		} catch (Exception e) {
			e.printStackTrace();
	        System.out.println("MP3");
	    }

	}

	/*
	private void play(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException {

	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    byte[] data = new byte[4096];
	    SourceDataLine line = getLine(targetFormat);

	        int nBytesRead = 0, nBytesWritten = 0;
	        while (nBytesRead != -1) {
	            nBytesRead = din.read(data, 0, data.length);
	            if (nBytesRead != -1) {
	                nBytesWritten = line.write(data, 0, nBytesRead);
	                out.write(data, 0, 4096);
	            }

	        }

	       byte[] audio = out.toByteArray();

	}*/
}
