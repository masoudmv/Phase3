package org.example;

import controller.Game;
import model.MyPolygon;
import model.entities.Profile;
import model.charactersModel.*;
import model.charactersModel.smiley.Fist;
import model.charactersModel.smiley.Hand;
import model.charactersModel.smiley.LeftHand;
import model.charactersModel.smiley.Smiley;
import view.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

    public static void main(String[] args) throws LineUnavailableException, IOException, UnsupportedAudioFileException, InterruptedException {
//        new Profile();

//    Rigid Panels should somehow be Collidable ...
//    isometric panels should lack shrinkage and bullet impact ...

//        BufferedImage baf = EpsilonModel.loadImage();
//        String id = ((GeoShapeModel) EpsilonModel.getINSTANCE()).getId();
//        System.out.println(id);

        SmileyBullet.loadImage();

//        new SmileyBullet(new Point2D.Double(300, 300)).rapidFire(10, 180);


        BufferedImage b = EpsilonModel.loadImage();
        GraphicalObject bows = new GraphicalObject(b);
        MyPolygon pol = bows.myBoundingPolygon;
        String id = new EpsilonModel(new Point2D.Double(1000, 700), pol).getId();



        MainFrame.getINSTANCE();
        new Profile();
        new Game(); // uncoment this todo



//        I am talking about this part: // uncoment this part todo
//        BufferedImage bImg = OmenoctModel.loadImage();
//        GraphicalObject bowser = new GraphicalObject(bImg);
//        MyPolygon p = bowser.myBoundingPolygon;
//        new OmenoctModel(new Point2D.Double(300, 400), p);
//        new OmenoctModel(new Point2D.Double(300, 400), p);
//        new OmenoctModel(new Point2D.Double(300, 400), p);
//        new OmenoctModel(new Point2D.Double(300, 400), p);
//        new OmenoctModel(new Point2D.Double(300, 400), p);
//        new OmenoctModel(new Point2D.Double(300, 400), p);
//        new OmenoctModel(new Point2D.Double(300, 400), p);
//        new OmenoctModel(new Point2D.Double(300, 400), p);
//        new OmenoctModel(new Point2D.Double(300, 400), p);
//        new OmenoctModel(new Point2D.Double(300, 400), p);
//        new OmenoctModel(new Point2D.Double(300, 400), p);
//        new OmenoctModel(new Point2D.Double(300, 400), p);
//        new OmenoctModel(new Point2D.Double(300, 400), p);
//        new OmenoctModel(new Point2D.Double(300, 400), p);

//        BufferedImage bg = BarricadosModel.loadImage();
//        GraphicalObject ber = new GraphicalObject(bg);
//        MyPolygon pl = ber.myBoundingPolygon;
//        new BarricadosModel(new Point2D.Double(900, 400), pl);

//        Orb.loadImage();
//        new BlackOrb();




//        BufferedImage bx = ArchmireModel.loadImage();
//        GraphicalObject bv = new GraphicalObject(bx);
//        MyPolygon pp = bv.myBoundingPolygon;
////        until hear
        ArchmireModel.loadImage();
//        new ArchmireModel(new Point2D.Double(300, 400));
//        new ArchmireModel(new Point2D.Double(300, 400));
//        new ArchmireModel(new Point2D.Double(300, 400));
//        new ArchmireModel(new Point2D.Double(300, 400));
//        new ArchmireModel(new Point2D.Double(300, 400));
//        new ArchmireModel(new Point2D.Double(300, 400));
//        new ArchmireModel(new Point2D.Double(300, 400));
        BabyArchmire.loadImage();
//
//

//        new BabyArchmire(new Point2D.Double(300, 800));
//        new ArchmireModel(new Point2D.Double(300, 500), pol);
//        new ArchmireModel(new Point2D.Double(300, 600), pol);
//        new ArchmireModel(new Point2D.Double(300, 700), pol);




//        BufferedImage bq = NecropickModel.loadImage();
//        GraphicalObject boww = new GraphicalObject(bq);
//        MyPolygon polll = boww.myBoundingPolygon;
//        new NecropickModel(new Point2D.Double(-200, -200), polll);



//        new FinalPanelModel();
//todo---

//        BufferedImage bq = Hand.loadImage();
//        GraphicalObject bb = new GraphicalObject(bq);
//        MyPolygon pool = bb.myBoundingPolygon;
//        new Hand(new Point2D.Double(600, 500), pool);
//todo---
//
//      SmileyBullet.rapidFire(10, 180);


//        GraphicalObject bows = new GraphicalObject(baf);
//        MyPolygon ol = bows.myBoundingPolygon;
//        new EpsilonModel(new Point2D.Double(200, 200));


//        LeftHand.loadImage();
//        new LeftHand(new Point2D.Double(200, 300));

//        new Hand(new Point2D.Double(1300, 500), pol);
//        new FinalPanelView()

//        new Panelss();
//        GamePanelView panel = new GamePanelView();

//        Point2D loc = new Point2D.Double(300, 300);
//        Dimension size = new Dimension(500, 500);
//        new FinalPanelModel(loc, size).setIsometric(false);


//        Point2D l = new Point2D.Double(699, 100);
//        Dimension s = new Dimension(700, 600);
//        FinalPanelModel f = new FinalPanelModel(l, s);
//        f.setRigid(false);
//        f.setIsometric(false);

        Smiley.loadImage();

//        BufferedImage baf = Smiley.loadImage();
//        GraphicalObject a = new GraphicalObject(baf);
//        MyPolygon l = a.myBoundingPolygon;
//        new Smiley(new Point2D.Double(400, 400));


        Fist.loadImage();
        ArchmireModel.loadImage();

//        new Fist(new Point2D.Double(200, 200));
        Hand.loadImage();
        LeftHand.loadImage();


        Hand l = new LeftHand(new Point2D.Double(500, 401));
        Hand r = new Hand(new Point2D.Double(1500, 400));
        new Smiley(new Point2D.Double(1000, 200), l , r);








//        new ArchmireModel(new Point2D.Double(100, 500));
//        new ArchmireModel(new Point2D.Double(100, 500));
//        new ArchmireModel(new Point2D.Double(100, 500));
//        new ArchmireModel(new Point2D.Double(100, 500));
//        new ArchmireModel(new Point2D.Double(100, 500));
//        new ArchmireModel(new Point2D.Double(100, 500));
//        new ArchmireModel(new Point2D.Double(100, 500));




//        new TrigorathModel(new Point2D.Double(500, 500));
//
//
//
//        BufferedImage ba = SmileyBullet.loadImage();
//        GraphicalObject bos = new GraphicalObject(ba);
//        MyPolygon pl = bos.myBoundingPolygon;
//        new SmileyBullet(new Point2D.Double(500, 500), pl).rapidFire(8, 180);


//        BufferedImage ba = Fist.loadImage();
//        GraphicalObject bos = new GraphicalObject(ba);
//        MyPolygon pl = bos.myBoundingPolygon;
//        new Fist(new Point2D.Double(500, 500), pl);





    }





    private static void loadImages(){
        try {
            bufferedImage = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\background2.png"));
            background = bufferedImage.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            // handle exception...
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\banish2.png"));
            banish = bufferedImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
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
            BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\aceso.png"));
            aceso = bufferedImage.getScaledInstance(170, 350, Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            // handle exception...
        }

        try {
            BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\proteus.png"));
            proteus = bufferedImage.getScaledInstance(170, 350, Image.SCALE_REPLICATE);
        } catch (IOException ex) {
            // handle exception...
        }

        try {
            BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\ares10.png"));
            ares_ = bufferedImage.getScaledInstance(170, 350, Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            // handle exception...
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\aceso10.png"));
            aceso_ = bufferedImage.getScaledInstance(170, 350, Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            // handle exception...
        }

        try {
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