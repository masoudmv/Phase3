package game.model.charactersModel.blackOrb;

import game.controller.Game;
import game.controller.Utils;
import game.controller.UserInterfaceController;
import game.controller.constants.Constants;
import shared.Model.MyPolygon;
import game.model.entities.AttackTypes;
import game.model.charactersModel.CollectibleModel;
import game.model.charactersModel.GeoShapeModel;
import game.model.charactersModel.SmileyBullet;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static game.controller.Utils.*;

public class Laser extends GeoShapeModel {
    private double laserThickness = 60;
    private Orb[] OrbsOfALaser = new Orb[2];
    private boolean isAvalanche = false;
    private double avalancheInitiation;

    public Laser(Orb orb1, Orb orb2) {
        super();
        this.isHovering = true;
        OrbsOfALaser[0] = orb1; OrbsOfALaser[1] = orb2;
        Point2D o1 = new Point2D.Double(orb1.getCircle().getCenterX(), orb1.getCircle().getCenterY());
        Point2D o2 = new Point2D.Double(orb2.getCircle().getCenterX(), orb2.getCircle().getCenterY());
        Line2D laserCenterLine = new Line2D.Double(o1, o2);
        findLaserBoundary(laserCenterLine);
        setAnchor();
        BlackOrb.lasers.add(this);
        UserInterfaceController.createLaserView(id);
        damageSize.put(AttackTypes.AOE, 12);
    }


    private void findLaserBoundary(Line2D laserCenterLine){
        Point2D vector = Utils.relativeLocation(laserCenterLine.getP2(), laserCenterLine.getP1());

        Point2D movement1 = Utils.adjustVectorMagnitude(Utils.perpendicularClockwise(vector), laserThickness/2);
        Point2D movement2 = Utils.adjustVectorMagnitude(Utils.PerpendicularCounterClockwise(vector), laserThickness/2);

        Line2D b1 = Utils.moveLine(laserCenterLine, movement1);
        Line2D b2 = Utils.moveLine(laserCenterLine, movement2);

        double[] xPoints = new double[4];
        double[] yPoints = new double[4];

        xPoints[0] = b1.getX1(); yPoints[0] = b1.getY1();
        xPoints[1] = b1.getX2(); yPoints[1] = b1.getY2();
        xPoints[2] = b2.getX2(); yPoints[2] = b2.getY2();
        xPoints[3] = b2.getX1(); yPoints[3] = b2.getY1();

        setMyPolygon(new MyPolygon(xPoints, yPoints, 4));
    }

    private void setAnchor(){
        double xAvg = 0;
        double yAvg = 0;
        for (int i = 0; i < 4; i++) {
            xAvg += myPolygon.xpoints[i];
            yAvg += myPolygon.ypoints[i];
        }
        anchor = new Point2D.Double(xAvg, yAvg);
    }

    public Orb[] getOrbsOfALaser() {
        return OrbsOfALaser;
    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {
        super.myPolygon = myPolygon;
    }

    @Override
    public void eliminate() {
        super.eliminate();
    }


    public boolean isAvalanche() {
        return isAvalanche;
    }

    public void setAvalanche(boolean avalanche) {
        isAvalanche = avalanche;
        avalancheInitiation = Game.ELAPSED_TIME + Constants.AVALANCHE_INITIATION_DELAY;
    }

    public double getAvalancheInitiation() {
        return avalancheInitiation;
    }

    private boolean applyAvalanche(){
        for (GeoShapeModel model : entities){
            // todo i think collectible does not have proper get bounds method ... fix it
            ArrayList<Point2D> bound = model.getBoundingPoints();
            boolean isInside = true;
            for (Point2D point : bound) {
                if (!isPointInPolygon(point, myPolygon.getVertices())){
                    isInside = false;
                }
            }
            if (isInside) {
                model.initiateFall();
                return true;
            }
        }
        return false;
    }

    private boolean applyAoEDamage(){
        for (GeoShapeModel model : entities){
            if (!(model instanceof CollectibleModel) && !(model instanceof SmileyBullet)) {
                ArrayList<Point2D> bound = model.getBoundingPoints();
                boolean isInside = true;

                for (Point2D point : bound) {
                    if (!isPointInPolygon(point, myPolygon.getVertices())){
                        isInside = false;
                    }
                }

                if (isInside) {
                    this.damage(model, AttackTypes.AOE);
                    return true;
                }
            }
        }

        return false;
    }

    private boolean executeAoe() {
        if (isAvalanche) {
            double now = Game.ELAPSED_TIME;
            double avalancheStart = avalancheInitiation;
            double avalancheEnd = avalancheStart + Constants.AVALANCHE_DURATION;

            if (avalancheStart < now && now < avalancheEnd) return applyAvalanche();
            else return applyAoEDamage();
        }

        else return applyAoEDamage();
    }

    public static void performAoeDamage(){
        for (Laser laser : BlackOrb.lasers){
            boolean done = laser.executeAoe();
            if (done) return;
        }
    }
}
