package model.charactersModel;


import controller.Game;
import model.DoubleDimension2D;
import model.FinalPanelModel;
import model.MyPolygon;
import model.charactersModel.blackOrb.Orb;
import model.charactersModel.smiley.Fist;
import model.charactersModel.smiley.Hand;
import model.charactersModel.smiley.Smiley;
import model.collision.Collidable;
import model.collision.CollisionState;
import model.collision.Impactable;
import model.entities.AttackTypes;
import model.entities.Entity;
import model.entities.Profile;
import model.movement.Direction;
import model.movement.Movable;
import view.charactersView.GeoShapeView;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.UserInterfaceController.findGeoShapeView;
import static controller.constants.Constants.*;
import static controller.UserInterfaceController.createEpsilonView;
import static controller.Utils.*;
import static controller.Utils.normalizeVector;
import static model.imagetools.ToolBox.getBufferedImage;

public class EpsilonModel extends GeoShapeModel implements Movable, Collidable, Impactable {
    static BufferedImage image; // transient to avoid serialization
    private static EpsilonModel INSTANCE;
    private double lastCerebrus = -Double.MAX_VALUE;
    private boolean impactInProgress = false;
    public int numberOfVertices = 0;
    public ArrayList<Point2D> vertices = new ArrayList<>();
    BabyEpsilon[] babies = new BabyEpsilon[3];
    public static List<EpsilonModel> epsilons = new CopyOnWriteArrayList<>();
    private FinalPanelModel localPanel;

    public EpsilonModel(Point2D anchor, MyPolygon myPolygon) {
        super(anchor, image, myPolygon, true);
        INSTANCE = this;
        Point2D loc = new Point2D.Double(getAnchor().getX() - 250, getAnchor().getY() - 250);
        DoubleDimension2D size = new DoubleDimension2D(500, 500);
        localPanel = new FinalPanelModel(loc, size);
        localPanel.setIsometric(false);
        Point vector = new Point(0, 0); //todo shitty design
        this.direction = new Direction(vector);
        collidables.add(this);
        movables.add(this);
        createEpsilonView(id, image);
        damageSize.put(AttackTypes.ASTRAPE, 0);
        impactables.add(this);
        this.health = 5000000;
        epsilons.add(this);
    }

    public static EpsilonModel getINSTANCE() {
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
    public Direction getDirection() {
        return direction;
    }

    @Override
    public Point2D getAnchor() {
        return anchor;
    }

    public void setBabies(BabyEpsilon[] babies) {
        this.babies = babies;
    }

    @Override
    public void impact(CollisionState collisionState) {
        Point2D collisionPoint = collisionState.collisionPoint;
        Point2D collisionRelativeVector = relativeLocation(this.getAnchor(), collisionPoint);
        double impactCoefficient = getImpactCoefficient(collisionRelativeVector);
        Point2D impactVector = normalizeVector(collisionRelativeVector);
        impactVector = multiplyVector(impactVector, impactCoefficient);
        Point2D r2 = addVectors(this.getDirection().getNormalizedDirectionVector(), impactVector);
        Direction direction = new Direction((r2));
        if (impactCoefficient != 0) this.setDirection(direction);
    }

    @Override
    public void impact(Point2D normalVector, Point2D collisionPoint, Collidable polygon) {
        double impactCoefficient = getImpactCoefficient(normalVector);
        Point2D impactVector = reflect(relativeLocation(getAnchor(), collisionPoint));
        impactVector = multiplyVector(impactVector, impactCoefficient);
        if (this.getDirection().getMagnitude() < 2) {
            setDirection(new Direction(normalizeVector(relativeLocation(getAnchor(), collisionPoint))));
        } else {
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
            double coefficient = 1 - (distance - 50) / (150 - 50);
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
    public void update(Direction direction) {
        if (localPanel== null) System.out.println("null");
        Point2D movement = multiplyVector(direction.getNormalizedDirectionVector(), direction.getMagnitude());
        this.anchor = addVectors(anchor, movement);
        for (int i = 0; i < numberOfVertices; i++) {
            vertices.set(i, addVectors(vertices.get(i), movement));
        }

        if (isOnFall) updateVelocityOnFall();
        friction();
    }

    private void checkForDeath(){
        if (isOnFall) System.out.println("YOU ARE DEAD!");
    }



    @Override
    public void update() {
        applyDismay();
        updateLocalPanel();
        moveBabies(direction);
        update(direction);
        applyCerebrus();
    }

    private void moveBabies(Direction direction){
        if (babies[0] == null) return;
        for (int i = 0; i < 3; i++) {
            babies[i].move(direction);
        }
    }

    public void empusa(){
        Image img = new ImageIcon("./client/src/epsilon2.png").getImage();
        EpsilonModel.image = getBufferedImage(img);
        GeoShapeView view = findGeoShapeView(id);
        view.setImage(image);
        Profile.getCurrent().EPSILON_RADIUS = 18;
        this.radius *= 0.90;
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
    public void friction() {
        if (isOnFall) return;

        if (isImpactInProgress()) {
            if (direction.getMagnitude() < 1) {
                setImpactInProgress(false);
            }
        } else {
            direction.setMagnitude(direction.getMagnitude() * 0.96);
            if (direction.getMagnitude() < 0.5) {
                direction.setMagnitude(0);
            }
        }
    }

    public Point2D reflect(Point2D normalVector) {
        double dotProduct = dotVectors(getDirection().getDirectionVector(), normalVector);
        Point2D reflection = addVectors(
                getDirection().getDirectionVector(),
                multiplyVector(normalVector, -2 * dotProduct)
        );
        return normalizeVector(reflection);
    }

    public void addVertex() {
        numberOfVertices++;
        vertices.clear();
        for (int i = 0; i < numberOfVertices; i++) {
            double alpha = 2 * PI * i / numberOfVertices;
            vertices.add(new Point2D.Double(getAnchor().getX() + RADIUS * Math.cos(alpha), getAnchor().getY() + RADIUS * Math.sin(alpha)));
        }
    }

    public void updateVertices() {
        for (int i = 0; i < numberOfVertices; i++) {
            double alpha = 2 * PI * i / numberOfVertices + angle;
            vertices.set(i, new Point2D.Double(getAnchor().getX() + RADIUS * Math.cos(alpha), getAnchor().getY() + RADIUS * Math.sin(alpha)));
        }
    }

    public void setHp(int hp) {
        this.health = hp;
    }

    public void sumHpWith(int hp) {
        this.health += hp;
        if (this.health > 100) this.health = 100;
    }

    public int getHp() {
        return health;
    }

    public void damage(int damage) {
        this.health -= damage;
    }

    public double getAngle() {
        return angle;
    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {
    }

    public static BufferedImage loadImage() {
        Image img = new ImageIcon("./client/src/epsilon.png").getImage();
        EpsilonModel.image = getBufferedImage(img);
        return EpsilonModel.image;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public static void nullifyEpsilon() {
        INSTANCE = null;
    }

    public void cerebrus(){
        BabyEpsilon.createBabies();
    }

    @Override
    public ArrayList<Point2D> getBoundingPoints(){
        ArrayList<Point2D> bound = new ArrayList<>();
        bound.add(new Point2D.Double(getAnchor().getX() - getRadius(), getAnchor().getY()));
        bound.add(new Point2D.Double(getAnchor().getX() + getRadius(), getAnchor().getY()));
        bound.add(new Point2D.Double(getAnchor().getX(), getAnchor().getY() + getRadius()));
        bound.add(new Point2D.Double(getAnchor().getX(), getAnchor().getY() - getRadius()));
        return bound;
    }


    private void updateLocalPanel() {
        if (localPanel != null) if (isInFinalPanelModel(localPanel)) return;
        for (FinalPanelModel panel : FinalPanelModel.finalPanelModels) {
            if (isInFinalPanelModel(panel)) {
                localPanel = panel;
                return;
            }
        }
        localPanel = null;
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

    public Point2D getClosestPointOnCircumference(Point2D center, Point2D point) {
        double dx = point.getX() - center.getX();
        double dy = point.getY() - center.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Scale the vector (dx, dy) to the circle's radius
        double radius = getRadius();  // Assuming you have a method to get the radius of the circle
        double scale = radius / distance;

        // Calculate the closest point on the circumference
        double closestX = center.getX() + dx * scale;
        double closestY = center.getY() + dy * scale;

        return new Point2D.Double(closestX, closestY);
    }


    private void transferOutside(Point2D intersection){
        Point2D closestPointOnCircumference = getClosestPointOnCircumference(getAnchor(), intersection);
        Point2D offset = new Point2D.Double(intersection.getX() - closestPointOnCircumference.getX(),
                intersection.getY() - closestPointOnCircumference.getY());
        anchor = addVectors(offset, getAnchor());
    }


    private void applyCerebrus(){
        if (babies[0] == null) return;

        for (GeoShapeModel enemy : entities){
            if (enemy instanceof EpsilonModel || enemy instanceof BabyEpsilon) {

            }
            else {
                for (int i = 0; i < babies.length; i++) {
                    List<Point2D> points = babies[i].getBoundingPoints();
                    boolean apply = babies[i].isInside(enemy.myPolygon.getVertices());
                    double now = Game.elapsedTime;
                    if (apply && now - lastCerebrus > 15) {
                        babies[i].damage(enemy, AttackTypes.MELEE);
                        lastCerebrus = Game.elapsedTime;
                    }
                }

            }
        }
    }

    public FinalPanelModel getLocalPanel() {
        return localPanel;
    }

    public void setLocalPanel(FinalPanelModel localPanel) {
        this.localPanel = localPanel;
    }


    private void applyDismay() {
        double now = Game.elapsedTime;
        double initiationTime = Profile.getCurrent().dismayInitiationTime;
        if (now - initiationTime > 10) return;

        double radius = 100;
        for (GeoShapeModel nonHovering : entities){

            boolean trig = nonHovering instanceof TrigorathModel;
            boolean square = nonHovering instanceof SquarantineModel;
            boolean Omen = nonHovering instanceof OmenoctModel;
            boolean wyrm = nonHovering instanceof Wyrm;;

            if (trig || square || Omen || wyrm) {
                Point2D anchor = nonHovering.getAnchor();
                // todo don't use singleton epsilon ...
                Point2D epsilonAnchor = EpsilonModel.getINSTANCE().getAnchor();
                double dis = anchor.distance(epsilonAnchor);

                if (dis < radius){
                    double offset = radius - dis;
                    Point2D dir = relativeLocation(anchor, epsilonAnchor);
                    dir = normalizeVector(dir);
                    dir = multiplyVector(dir, offset);
                    nonHovering.movePolygon(dir);

                }
            }
        }
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
        if (other instanceof FinalPanelModel) {
            if (!isOnFall) impact(new CollisionState(intersection));
            return;
        }

        if (other instanceof CollectibleModel || other instanceof SmileyBullet){
            return;
        }

        // todo this may need to change
        damage((Entity) other, AttackTypes.ASTRAPE);

        if (other instanceof Smiley) impact(new CollisionState(intersection));
        if (other instanceof Fist) impact(new CollisionState(intersection));
        if (other instanceof SquarantineModel) {


            impact(relativeLocation(getAnchor(), intersection), intersection, other);
        }
        if (other instanceof TrigorathModel) impact(relativeLocation(getAnchor(), intersection), intersection, other);
        if (other instanceof Hand) {



            impact(new CollisionState(intersection));
        }
        if (other instanceof BarricadosModel) impact(new CollisionState(intersection));
        if (other instanceof OmenoctModel) impact(new CollisionState(intersection));
        if (other instanceof Orb) {
            transferOutside(intersection);
            if (!isOnFall) impact(new CollisionState(intersection));
        }
        if (other instanceof NecropickModel) if (!((NecropickModel) other).isHovering()) impact(new CollisionState(intersection)); // :)
//        if (other instanceof CollectibleModel) Profile.getCurrent().inGameXP += ((CollectibleModel) other).getCollectibleXP();
//        if (other instanceof BulletModel) ;
//        if (other instanceof SmileyBullet) ;

    }

    @Override
    public void onCollision(Collidable other, Point2D poly1, Point2D poly2) {

    }
}
