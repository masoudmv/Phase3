package model.charactersModel.blackOrb;

import controller.Game;
import model.FinalPanelModel;
import model.charactersModel.BarricadosModel;
import model.charactersModel.GeoShapeModel;
import model.entities.Profile;
import model.interfaces.Enemy;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.UserInterfaceController.updateGeoShapeViewProperties;
import static controller.Utils.*;
import static controller.constants.Constants.FRAME_DIMENSION;
import static controller.constants.EntityConstants.ORB_PANEL_CREATION_DELAY;
import static model.FinalPanelModel.intersect;
import static model.entities.Entity.entities;

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

    public BlackOrb(Point2D pivot) {
        this.avalancheBirthTime = random.nextInt((int) (Game.elapsedTime + 6), (int) (Game.elapsedTime + 15));

        double edgeLength = 350;
        double radius = edgeLength / (2 * Math.sin(Math.PI / 5));
        for (int i = 0; i < 5; i++) {
            double angle = 2 * Math.PI * i / 5 + Math.PI / 2;
            vertices[i] = new Point2D.Double(
                    pivot.getX() + radius * Math.cos(angle),
                    pivot.getY() + radius * Math.sin(angle)
            );
        }
        blackOrbs.add(this);
        Game.getINSTANCE().getGameLoop().getWaveManager().incrementGeneratedEnemies();
    }

    public BlackOrb() {
    }

    public boolean dontUpdate(){
        double now = Game.elapsedTime;
        double slumberInitiation = Profile.getCurrent().slumberInitiationTime;
        return now - slumberInitiation < 10;
    }

    public void update(){
        if (dontUpdate()) return;
        initiateAvalanche();
        Laser.performAoeDamage();

        double now = Game.elapsedTime;
        if (now - lastCreatedOrbTime > ORB_PANEL_CREATION_DELAY && numCreatedOrbs == 5) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    initializedOrbs();
                }
            }).start();
            numCreatedOrbs++;
        }
        if ( now - lastCreatedOrbTime < ORB_PANEL_CREATION_DELAY || numCreatedOrbs > 4) return;
        FinalPanelModel p = new FinalPanelModel(vertices[numCreatedOrbs], getPanelDimension());

        for (GeoShapeModel model : entities){
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
            orbs[i] = new Orb(addVectors(vertices[i], movePanelLocation));
            orbs[i].setPanel(panels[i]);

        } setLasers();

    }


    private Dimension getPanelDimension(){
        return new Dimension(250, 250);

    }

    private void setLasers() {
        for (int i = 0; i < 5; i++) {
            for (int j = i+1; j < 5; j++) {
                new Laser(orbs[i], orbs[j]);
            }
        }
    }

    public void initiateAvalanche(){
        double now = Game.elapsedTime;
        if (now - lastCreatedOrbTime < avalancheBirthTime || avalancheIsSet) return;
        int index = random.nextInt(lasers.size());
        Laser laser = lasers.get(index);
        laser.setAvalanche(true);
        laser.updateView();
        avalancheIsSet = true;
    }

    @Override
    public void create() {
        Random random = new Random();
        double offset = 520;
        double width = FRAME_DIMENSION.getWidth();
        double x = random.nextDouble(offset, width-offset);
        double y = random.nextDouble(350, 450);
        Point2D pivot = new Point2D.Double(x, y);
        new BlackOrb(pivot);
    }



    @Override
    public int getMinSpawnWave() {
        return 1;
    }

    @Override
    public boolean isUniquePerWave(){
        return true;
    }
}
