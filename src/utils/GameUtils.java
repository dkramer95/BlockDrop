package utils;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.awt.*;
import java.io.File;
import java.util.Random;

/**
 * Contains some useful static utility methods that can be used
 * throughout the application.
 * Created by David Kramer on 2/10/2016.
 */
public class GameUtils {
    public static Random rng = new Random();


    /**
     * Plays a sound from the specified string file path.
     * @param file - String to file
     */
    public static void playSound(String file) {
        File f = new File(file);
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(f));
            clip.start();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {	// close out when finished playing
                    clip.close();
                }
            });
            Logger.log(MessageLevel.INFO, "Playing sound %s", file);
        } catch (Exception e) {
            Logger.log(MessageLevel.ERROR, "Failed to play sound %s", file);
        }
    };

    /**
     * Generates a random RGB and Optional (alpha) color.
     * @param withAlpha - should this color contain an alpha value
     * @return RGB(A) color
     */
    public static Color getRandomColor(boolean withAlpha) {
        Color color;
        int randRed = rng.nextInt(255);
        int randGreen = rng.nextInt(255);
        int randBlue = rng.nextInt(255);
        if (withAlpha) {
            int randAlpha = rng.nextInt(255);
            color = new Color(randRed, randGreen, randBlue, randAlpha);
        } else {
            color = new Color(randRed, randGreen, randBlue);
        }
        return color;
    }
}
