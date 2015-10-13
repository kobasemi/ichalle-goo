package jp.ac.kansai_u.kutc.firefly.waltzforai.frame;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
 
public class PlayBGM {
	private Clip line ;
    public void start(String file) {
        AudioInputStream ais = null;
		try {
			ais = AudioSystem.getAudioInputStream(new File(file+".wav"));
			ais.getFormat();
            line = AudioSystem.getClip();
            line.open(ais);
            line.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void stop(){
        line.stop();
        line.close();
	}
}