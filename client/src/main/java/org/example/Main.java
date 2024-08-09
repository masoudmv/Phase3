package org.example;

import model.charactersModel.blackOrb.Orb;
import model.entities.Profile;
import model.charactersModel.*;
import model.charactersModel.smiley.Fist;
import model.charactersModel.smiley.Hand;
import model.charactersModel.smiley.LeftHand;
import model.charactersModel.smiley.Smiley;
import model.entities.Skill;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.IOException;

import view.junks.Menu;


public class Main {

    public static Image background;

    public static int soundVolume = 100;
    public static double sensitivity = 50;
    public static int totalXP = 0;


    public static void main(String[] args) throws LineUnavailableException, IOException, UnsupportedAudioFileException, InterruptedException {
        LeftHand.loadImage();
        Hand.loadImage();
        SmileyBullet.loadImage();
        Smiley.loadImage();
        SquarantineModel.loadImage();
        TrigorathModel.loadImage();
        OmenoctModel.loadImage();
        NecropickModel.loadImage();
        BabyArchmire.loadImage();
        ArchmireModel.loadImage();
        Orb.loadImage();
        BarricadosModel.loadImage();
        Fist.loadImage();
        TrigorathModel.loadImage();

        new Profile();
        Skill.initializeSkills();




        new Menu();




    }
}