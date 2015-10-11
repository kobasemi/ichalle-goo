package jp.ac.kansai_u.kutc.firefly.waltzforai.frame;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
 
public class PlayBGM {
	Clip line ;
    public void start(String file) {
        try {
            final AudioInputStream ais =
                    AudioSystem.getAudioInputStream(new File(file+".wav"));
            ais.getFormat();
            line = AudioSystem.getClip();
            line.open(ais);
            line.start();
        } catch (final Exception e) {
			System.out.println(e);
		}
	}

	public void stop(){
    	try{
       line.stop();
        line.close();
        System.out.println("stop");
    	}catch(final Exception e) {
			System.out.println(e);
		}
	}
}