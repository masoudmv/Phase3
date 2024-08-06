package game.model.charactersModel.blackOrb;

import game.controller.Game;
import game.controller.Utils;
import game.model.charactersModel.BarricadosModel;
import game.model.charactersModel.GeoShapeModel;
import game.model.reflection.Enemy;
import server.DataBase;
import shared.constants.EntityConstants;
import game.model.FinalPanelModel;
import game.model.entities.Profile;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static game.model.FinalPanelModel.intersect;
import static shared.constants.Constants.FRAME_DIMENSION;

public class BlackOrb implements Enemy { // todo panels should be created with delay?
    Random random = new Random();
    FinalPanelModel[] panels = new FinalPanelModel[5];
    Orb[] orbs = new Orb[5];
    int numCreatedOrbs = 0;
    public static CopyOnWriteArrayList<Laser> lasers = new CopyOnWriteArrayList<>();
    double lastCreatedOrbTime = 0;
    private final Point2D[] vertices = new Point2D[5];
    private final Point2D movePanelLocation = new Point2D.Double(125, 125);
    public static CopyOnWriteArrayList<BlackOrb> blackOrbs = new CopyOnWriteArrayList<>();
    private boolean avalancheIsSet = false;
    private double avalancheBirthTime;
    private String gameID;
    private static double edgeLength = 350; // Distance between adjacent vertices

    public BlackOrb(Point2D pivot, String gameID) {
        this.gameID = gameID;
        this.avalancheBirthTime = random.nextInt((int) (findGame(gameID).ELAPSED_TIME + 6), (int) (findGame(gameID).ELAPSED_TIME + 15));

        double radius = edgeLength / (2 * Math.sin(Math.PI / 5)); // Circumradius of the pentagon
        for (int i = 0; i < 5; i++) {
            double angle = 2 * Math.PI * i / 5 + Math.PI / 2; // Central angle for each vertex, adjusted for upside-down orientation
            vertices[i] = new Point2D.Double(
                    pivot.getX() + radius * Math.cos(angle),
                    pivot.getY() + radius * Math.sin(angle)
            );
        }
        blackOrbs.add(this);
    }

    public BlackOrb() {
    }

    private Game findGame(String gameID){
        return DataBase.getDataBase().findGame(gameID);
    }

    public boolean dontUpdate(){
        double now = findGame(gameID).ELAPSED_TIME;
        double slumberInitiation = findGame(gameID).getProfile().slumberInitiationTime;
        return now - slumberInitiation < 10;
    }

    public void update(){
        if (dontUpdate()) return;
//        Laser.performAoeDamage();

        double now = findGame(gameID).ELAPSED_TIME;
        if (now - lastCreatedOrbTime > EntityConstants.ORB_PANEL_CREATION_DELAY && numCreatedOrbs == 5) {
            // Run initializeOrbs in a separate thread
//            executorService.submit(this::initializedOrbs); // TODO ADD CONURENCY TO REQUIRED METHODS IN GAMELOOP.
            System.out.println("HERE");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    initializedOrbs();
                }
            }).start();

            numCreatedOrbs++;
        }
        if ( now - lastCreatedOrbTime < EntityConstants.ORB_PANEL_CREATION_DELAY || numCreatedOrbs > 4) return;
        FinalPanelModel p = new FinalPanelModel(vertices[numCreatedOrbs], getPanelDimension(), gameID);
        Game game = findGame(gameID);
        for (GeoShapeModel model : game.entities){
            if (model instanceof BarricadosModel){
                FinalPanelModel panelModel = ((BarricadosModel) model).getF();
                if (intersect(panelModel, p)) {
                    panelModel.setRigid(false);
                    model.eliminate();
                }
            }

        }


        p.setIsometric(true);
        panels[numCreatedOrbs] = p;
        lastCreatedOrbTime = now;
        numCreatedOrbs ++;


    }

    private void initializedOrbs(){
        for (int i = 0; i < 5; i++) {
            orbs[i] = new Orb(Utils.addVectors(vertices[i], movePanelLocation), gameID);
            orbs[i].setPanel(panels[i]);
//            updateGeoShapeViewProperties();

        }
        setLasers();
//        updateGeoShapeViewProperties();
    }


    private Dimension getPanelDimension(){
        return new Dimension(250, 250);

    }

    private void setLasers() {
        for (int i = 0; i < 5; i++) {
            for (int j = i+1; j < 5; j++) {
                new Laser(orbs[i], orbs[j], gameID);
//                updateGeoShapeViewProperties();

            }
        }
    }


    @Override
    public void create(String gameID) {
        Random random = new Random();
        double offset = 520;
        double width = FRAME_DIMENSION.getWidth();
        double x = random.nextDouble(offset, width-offset);
        double y = random.nextDouble(350, 450);
        Point2D pivot = new Point2D.Double(x, y);
        new BlackOrb(pivot, gameID);
    }



    @Override
    public int getMinSpawnWave() {
        return 5;
    }

    @Override
    public boolean isUniquePerWave(){
        return true;
    }
}
