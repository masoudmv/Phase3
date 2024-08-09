package org.example;

import client.network.containers.MainMenu;
import controller.Game;
import javafx.scene.shape.Arc;
import model.MyPolygon;
import model.charactersModel.blackOrb.BlackOrb;
import model.charactersModel.blackOrb.Orb;
import model.entities.Profile;
import model.charactersModel.*;
import model.charactersModel.smiley.Fist;
import model.charactersModel.smiley.Hand;
import model.charactersModel.smiley.LeftHand;
import model.charactersModel.smiley.Smiley;
import model.entities.Skill;
import view.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
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




//        Hand l = new LeftHand(new Point2D.Double(500, 401));
//        Hand r = new Hand(new Point2D.Double(1500, 400));
//        new Smiley(new Point2D.Double(1000, 200), l , r);


        new Menu();




    }
}