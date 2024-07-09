package controller;

import org.example.Main;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;

import static org.example.Main.soundVolume;

public class Sound {
    public static Clip collisionSound;
    public static Clip deathSound;
    public static Clip victorySound;
    public static Clip theme;
    public Sound() {
        try {
            File file = new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\collide.wav");
            if (file.exists()) {
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                // load the sound into memory (a Clip)
                collisionSound = AudioSystem.getClip();
                collisionSound.open(sound);
                FloatControl gainControl =
                        (FloatControl) collisionSound.getControl(FloatControl.Type.MASTER_GAIN);

//                gainControl.setValue(calculateVolumeDecrement());

            }
            else {
                throw new RuntimeException("Sound: file not found: ");
            }

            File deathSoundFile = new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\death.wav");
            if (deathSoundFile.exists()) {
                AudioInputStream sound = AudioSystem.getAudioInputStream(deathSoundFile);
                // load the sound into memory (a Clip)
                deathSound = AudioSystem.getClip();
                deathSound.open(sound);
                FloatControl gainControl =
                        (FloatControl) deathSound.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue((calculateVolumeDecrement()));
            }
            else {
                throw new RuntimeException("Sound: file not found: ");
            }


            File victorySoundFile = new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\victory.wav");
            if (victorySoundFile.exists()) {
                AudioInputStream sound = AudioSystem.getAudioInputStream(victorySoundFile);
                // load the sound into memory (a Clip)
                victorySound = AudioSystem.getClip();
                victorySound.open(sound);
                FloatControl gainControl =
                        (FloatControl) victorySound.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(calculateVolumeDecrement());
            }
            else {
                throw new RuntimeException("Sound: file not found: ");
            }

            File themeFile = new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\theme.wav");
            if (themeFile.exists()) {
                AudioInputStream sound = AudioSystem.getAudioInputStream(themeFile);
                // load the sound into memory (a Clip)
                theme = AudioSystem.getClip();
                theme.open(sound);
                FloatControl gainControl =
                        (FloatControl) theme.getControl(FloatControl.Type.MASTER_GAIN);

                gainControl.setValue(calculateVolumeDecrement());

            }
            else {
                throw new RuntimeException("Sound: file not found: ");
            }



        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Malformed URL: " + e);
        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Unsupported Audio File: " + e);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Input/Output Error: " + e);
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Line Unavailable Exception Error: " + e);
        }

    }
    public static void playBubble(){
        if (soundVolume>0) {
            collisionSound.setFramePosition(0);  // Must always rewind!
            collisionSound.loop(0);
        }
    }

    public static void playDeathSound(){
        if (soundVolume>0) {
            deathSound.setFramePosition(0);  // Must always rewind!
            deathSound.loop(0);
        }
    }

    public static void playVictorySound(){
        if (soundVolume>0 && !victorySound.isRunning()) {
            victorySound.setFramePosition(10);  // Must always rewind!
            victorySound.loop(0);

        }
    }

    public static void playThemeSound(){
        if (soundVolume>0 && calculateVolumeDecrement()>-20) {
            theme.setFramePosition(0);  // Must always rewind!
            theme.loop(Clip.LOOP_CONTINUOUSLY);

        }
    }


    public static void stopThemeSound(){
//        if (soundVolume>0 && calculateVolumeDecrement()>-20) {
//            theme.setFramePosition(0);  // Must always rewind!
//            theme.loop(Clip.LOOP_CONTINUOUSLY);
            theme.stop();

//        }
    }

    public static void stopVictorySound(){
//        if (soundVolume>0 && calculateVolumeDecrement()>-20) {
//            theme.setFramePosition(0);  // Must always rewind!
//            theme.loop(Clip.LOOP_CONTINUOUSLY);
            victorySound.stop();

//        }
    }

    private static float calculateVolumeDecrement(){
        return (float) 0.8*(soundVolume)-80;
    }

}