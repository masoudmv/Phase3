package model.charactersModel;

import model.FinalPanelModel;
import model.MyPolygon;
import model.charactersModel.smiley.Fist;
import model.charactersModel.smiley.Smiley;
import model.charactersModel.smiley.SmileyBullet;
import model.collision.Collidable;
import model.collision.CollisionState;
import model.collision.Impactable;
import model.movement.Direction;
import model.movement.Movable;
import view.MainFrame;
//import view.MainPanel;
//import view.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.UUID;

import static controller.constants.Constants.*;
import static controller.Controller.createEpsilonView;
import static controller.Utils.*;
import static controller.Utils.normalizeVector;
import static model.imagetools.ToolBox.getBufferedImage;

public class EpsilonModel extends GeoShapeModel implements Movable, Collidable, Impactable {
    static BufferedImage image;
    private static EpsilonModel INSTANCE;
    private int hp = 100;
//    Point2D anchor;
//    double radius;
    private boolean impactInProgress = false;
//    String id;
//    Direction direction;
    public static ArrayList<EpsilonModel> epsilonModels = new ArrayList<>();

    public int numberOfVertices = 0;
//    private double angle=0;

    public ArrayList<Point2D> vertices = new ArrayList<>();


    public static FinalPanelModel localPanel;

    public EpsilonModel(Point2D anchor, MyPolygon myPolygon) {
        super(anchor, image, myPolygon);
        System.out.println("epsilon");
        INSTANCE = this;

        Point2D loc = new Point2D.Double(300, 300);
        Dimension size = new Dimension(500, 500);
        localPanel = new FinalPanelModel(loc, size);
        localPanel.setIsometric(false);



//        this.anchor = anchor;
//        this.radius = 20; //todo

//        this.id= UUID.randomUUID().toString();
        Point vector = new Point(0,0); //todo shitty design
        this.direction=new Direction(vector);

        epsilonModels.add(this);
        collidables.add(this);
        movables.add(this);
//        impactables.add(this);




        createEpsilonView(id);

    }

    public static EpsilonModel getINSTANCE() {
//        if (INSTANCE == null) INSTANCE = new EpsilonModel(
//                new Point2D.Double(600, 600)); // todo revert
        return INSTANCE;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean isImpactInProgress() {
        return impactInProgress;
    }

    @Override
    public void setImpactInProgress(boolean impactInProgress) {
        this.impactInProgress = impactInProgress;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void bulletImpact(BulletModel bulletModel, Point2D collisionPoint){}
    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public Point2D getAnchor() {
        return anchor;
    }

    @Override
    public void impact(CollisionState collisionState) {
        Point2D collisionPoint = collisionState.collisionPoint;
        Point2D collisionRelativeVector = relativeLocation(this.getAnchor(), collisionPoint);
        double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
        Point2D impactVector = normalizeVector(collisionRelativeVector);
        impactVector = multiplyVector(impactVector ,impactCoefficient);
        Point2D r2 = addVectors(this.getDirection().getNormalizedDirectionVector(), impactVector);
        Direction direction = new Direction((r2));
        if (impactCoefficient != 0) this.setDirection(direction);
    }


    @Override
    public void impact(Point2D normalVector, Point2D collisionPoint, Collidable polygon) {
        double impactCoefficient = getImpactCoefficient(normalVector);
        Point2D impactVector = reflect(relativeLocation(getAnchor(), collisionPoint));
        impactVector = multiplyVector(impactVector ,impactCoefficient);
        if (this.getDirection().getMagnitude() < 2){
            setDirection(new Direction(normalizeVector(relativeLocation(getAnchor(), collisionPoint))));
        }
        else {
            setDirection(new Direction(normalizeVector(impactVector)));
        }
    }

    @Override
    public double getImpactCoefficient(Point2D collisionRelativeVector) {
        double distance = Math.hypot(collisionRelativeVector.getX(), collisionRelativeVector.getY());
        double impactCoefficient;
        if (distance < 50) {
            setImpactInProgress(true);
            impactCoefficient = 4;
        } else if (distance > 150) {
            setImpactInProgress(false);
            impactCoefficient = 0;
        } else {
            setImpactInProgress(true);
            double coefficient = 1-(distance- 50)/(150 - 50);
            impactCoefficient = coefficient * 4;
        }
        return impactCoefficient;
    }

    @Override
    public void banish() {

    }

    @Override
    public Point2D[] getVertices() {
        return null;
    }

    @Override
    public ArrayList<Line2D> getEdges() {
        return null;
    }

    @Override
    public void move(Direction direction) {
        Point2D movement = multiplyVector(direction.getNormalizedDirectionVector(), direction.getMagnitude());
        this.anchor = addVectors(anchor, movement);
        for (int i = 0; i < numberOfVertices; i++) {
            vertices.set(i, addVectors(vertices.get(i), movement));
        }
//        movePolygon(movement);

    }

    @Override
    public void move() {
        updateLocalPanel();
        move(direction);
    }

    @Override
    public boolean isCircular() {
        return true;
    }

    @Override
    public double getRadius() {
        return RADIUS;
    }

    @Override
    public void eliminate() {

    }

    @Override
    public void friction(){


        if (isImpactInProgress()){
//            direction.setMagnitude(direction.getMagnitude() * FRICTION);
            if (direction.getMagnitude() < 1){
//                direction.setMagnitude(0);
                setImpactInProgress(false);
            }
        } else {
            direction.setMagnitude(direction.getMagnitude() * 0.93);
            if (direction.getMagnitude() < 0.5){
                direction.setMagnitude(0);
            }
        }

    }

    public Point2D reflect(Point2D normalVector){

        double dotProduct = dotVectors(getDirection().getDirectionVector(), normalVector);
        Point2D reflection = addVectors(
                getDirection().getDirectionVector(),
                multiplyVector(normalVector,-2*dotProduct
                ));
        return normalizeVector(reflection);
    }


    // Writ of Proteus:

    public void addVertex(){
        numberOfVertices++;
        vertices.clear();

        for (int i = 0; i < numberOfVertices; i++) {
            double alpha = 2*PI*i/numberOfVertices;
            vertices.add(new Point2D.Double(getAnchor().getX()+RADIUS*Math.cos(alpha), getAnchor().getY()+RADIUS*Math.sin(alpha)));
        }
    }
    public void updateVertices(){
        for (int i = 0; i < numberOfVertices; i++) {
            double alpha = 2*PI*i/numberOfVertices+angle;
            vertices.set(i, new Point2D.Double(getAnchor().getX()+RADIUS*Math.cos(alpha), getAnchor().getY()+RADIUS*Math.sin(alpha)));
        }
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void sumHpWith(int hp){
        this.hp += hp;
        if (this.hp >100) this.hp=100;
    }

    public int getHp() {
        return hp;
    }
    public void damage(int damage){
        this.hp -= damage;
    }
    public double getAngle() {
        return angle;
    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {

    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./src/epsilon.png").getImage();
        EpsilonModel.image = getBufferedImage(img);
        return EpsilonModel.image;
    }


    public void setAngle(double angle) {
        this.angle = angle;
    }

    public static void nullifyEpsilon(){
        INSTANCE = null;
    }
    @Override
    public void onCollision(Collidable other) {

    }

    private void updateLocalPanel(){
        if (localPanel != null) if (isInFinalPanelModel(localPanel)) return;;
        for (FinalPanelModel panel : FinalPanelModel.finalPanelModels){
            if (isInFinalPanelModel(panel)) {
                localPanel = panel;
                return;
            }
        } localPanel = null;
    }
    private boolean isInFinalPanelModel(FinalPanelModel panel) {
        Point2D left = new Point2D.Double(getAnchor().getX() - getRadius(), getAnchor().getY());
        Point2D right = new Point2D.Double(getAnchor().getX() + getRadius(), getAnchor().getY());
        Point2D bottom = new Point2D.Double(getAnchor().getX(), getAnchor().getY() + getRadius());
        Point2D top = new Point2D.Double(getAnchor().getX(), getAnchor().getY() - getRadius());

        boolean leftIn = isPointInPolygon(left, panel.getVertices());
        boolean rightIn = isPointInPolygon(right, panel.getVertices());
        boolean bottomIn = isPointInPolygon(bottom, panel.getVertices());
        boolean topIn = isPointInPolygon(top, panel.getVertices());

        if (!leftIn || !rightIn || !bottomIn || !topIn) return false;
        return true;
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
//        System.out.println(intersection);
//        System.out.println(getAnchor());
//        System.out.println("================");
        if (other instanceof Smiley) impact(new CollisionState(intersection));
        if (other instanceof Fist) impact(new CollisionState(intersection));
        if (other instanceof BarricadosModel) impact(new CollisionState(intersection));
        if (other instanceof CollectibleModel);
        if (other instanceof BulletModel);
        if (other instanceof SmileyBullet);
        if (other instanceof FinalPanelModel) {
//            System.out.println("+_+_+");
            impact(new CollisionState(intersection));
        }

    }
}