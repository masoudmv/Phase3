package org.example;
import controller.Game;
import controller.Sound;
import model.charactersModel.SquarantineModel;
import view.*;
import view.Menu;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.Arrays;
import java.util.Iterator;


public class Main {
    static BufferedImage bufferedImage;
    public static Image background;
    public static Image ares;
    public static Image aceso;
    public static Image proteus;
    public static Image ares_;
    public static Image aceso_;
    public static Image proteus_;
    public static Image banish;
    public static Image heal;
    public static Image empower;
    public static int soundVolume = 100;
    public static double sensitivity = 50;
    public static Graphics2D g2d;
    public static Difficulty difficulty = Difficulty.normal;
    public static int totalXP=0;

    public static boolean aresIsActivated = false;
    public static boolean acesoIsActivated = false;
    public static boolean proteusIsActivated = false;

    public static void main(String[] args) throws LineUnavailableException, IOException, UnsupportedAudioFileException {

        MainFrame.getINSTANCE();



        loadImages();






        new Sound();
//        new GameOverPanel();
//        new Game();
        new Menu();
//
//        new SkillTreeMenu();
//
//        new SettingsMenu();
//
//        new KeyBindingMenu();

    }
    private static void loadImages(){
        try {
//            squarantine = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\squarantine.png"));
            bufferedImage = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\background2.png"));
            background = bufferedImage.getScaledInstance(600, 600, Image.SCALE_SMOOTH);


        } catch (IOException ex) {
            // handle exception...

        }
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\banish2.png"));
            banish = bufferedImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
//            banish = bufferedImage


        } catch (IOException ex) {
            // handle exception...

        }
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\heal.png"));
            heal = bufferedImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);


        } catch (IOException ex) {
            // handle exception...

        }

        try {
            BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\empower.png"));
            empower = bufferedImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);


        } catch (IOException ex) {
            // handle exception...

        }

        try {
//            squarantine = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\squarantine.png"));
            BufferedImage buffered = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\ares2.png"));
            BufferedImage bufferedImageResult = new BufferedImage(
                    170,
                    350,
                    bufferedImage.getType()
            );

            g2d = bufferedImageResult.createGraphics();

            ares = buffered.getScaledInstance(170, 350, Image.SCALE_DEFAULT);


        } catch (IOException ex) {
            // handle exception...

        }


        try {
//            squarantine = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\squarantine.png"));
            BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\aceso.png"));
            aceso = bufferedImage.getScaledInstance(170, 350, Image.SCALE_SMOOTH);


        } catch (IOException ex) {
            // handle exception...

        }

        try {
//            squarantine = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\squarantine.png"));
            BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\proteus.png"));
            proteus = bufferedImage.getScaledInstance(170, 350, Image.SCALE_REPLICATE);
//            bufferedImage.se
//            proteus = bufferedImage;


        } catch (IOException ex) {
            // handle exception...

        }

        try {
//            squarantine = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\squarantine.png"));
            BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\ares10.png"));
            ares_ = bufferedImage.getScaledInstance(170, 350, Image.SCALE_SMOOTH);


        } catch (IOException ex) {
            // handle exception...

        }

        try {
//            squarantine = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\squarantine.png"));
            BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\aceso10.png"));
            aceso_ = bufferedImage.getScaledInstance(170, 350, Image.SCALE_SMOOTH);


        } catch (IOException ex) {
            // handle exception...

        }

        try {
//            squarantine = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\squarantine.png"));
            BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\proteus10.png"));
            proteus_ = bufferedImage.getScaledInstance(170, 350, Image.SCALE_SMOOTH);


        } catch (IOException ex) {
            // handle exception...

        }

    }

    public enum Difficulty{
        easy,
        normal,
        hard
    }
}