package model.charactersModel;

import model.FinalPanelModel;
import model.MyPolygon;
import model.collision.Collidable;
import model.collision.CollisionState;
import model.collision.Impactable;
import model.entities.AttackTypes;
import model.entities.Entity;
import model.entities.Profile;
import model.movement.Direction;
import model.movement.Movable;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.UserInterfaceController.*;
import static controller.constants.Constants.BULLET_RADIUS;
import static controller.constants.Constants.BULLET_VELOCITY;
import static controller.Utils.*;

public class BulletModel extends GeoShapeModel implements Movable, Collidable, Impactable {
    public static CopyOnWriteArrayList<BulletModel> bulletModels = new CopyOnWriteArrayList<>();
    private boolean createdByEpsilon = true;

    public BulletModel(Point2D anchor, Direction direction) {
        super();

        this.radius = BULLET_RADIUS;
        this.anchor = anchor;
        this.direction = direction;
        this.health = Integer.MAX_VALUE;

        // is needed? not think so!
        bulletModels.add(this);

        movables.add(this);
        collidables.add(this);
        creatBulletView(id);

        int damage  = Profile.getCurrent().BULLET_DAMAGE;
        damageSize.put(AttackTypes.MELEE, damage);
        updateBulletDamage();


    }

    private void updateBulletDamage(){
        if (Profile.getCurrent().BULLET_DAMAGE > 5) Profile.getCurrent().BULLET_DAMAGE = 5;
    }


    public BulletModel(Point2D anchor, Direction direction, boolean createdByEpsilon) {
        super();
        this.radius = BULLET_RADIUS;
        this.anchor = anchor;
        this.direction = direction;
        // is needed? not think so!
        this.createdByEpsilon = createdByEpsilon;
        bulletModels.add(this);

        movables.add(this);
        collidables.add(this);
        creatBulletView(id);
        damageSize.put(AttackTypes.MELEE, 5);
    }





    public String getId() {
        return id;
    }

    @Override
    public boolean isCircular() {
        return true;
    }

    @Override
    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public void setDirection(Direction direction) {}


    // keeping this in case needed!
//    public void bulletImpact(BulletModel bulletModel, Point2D collisionPoint, Collidable collidable) {
//        ((Movable) collidable).bulletImpact(bulletModel, collisionPoint);
//        for (Movable movable : movables){
//            if (movable != this && movable != bulletModel && movable!= collidable){
//                ((Impactable)movable).impact(new CollisionState(collisionPoint));
//            }
//        }
//        this.eliminate();
//    }


    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public void update(Direction direction) {
        Point2D movement = multiplyVector(direction.getNormalizedDirectionVector(), BULLET_VELOCITY);
        this.anchor = addVectors(anchor, movement);
    }

    @Override
    public void update() {
        super.update();
        update(direction);
    }

    @Override
    public void friction() {}

    @Override
    public Point2D getAnchor() {
        return this.anchor;
    }

    @Override
    public void setMyPolygon(MyPolygon myPolygon) {

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
    public boolean isImpactInProgress() {
        return false;
    }

    @Override
    public void setImpactInProgress(boolean impactInProgress) {

    }

    @Override
    public void impact(CollisionState collisionState) {}

    @Override
    public void impact(Point2D normalVector, Point2D collisionPoint, Collidable polygon) {}


    @Override
    public double getImpactCoefficient(Point2D collisionRelativeVector) {
        return 0;
    }

    @Override
    public void banish() {}

    public boolean isCreatedByEpsilon() {
        return createdByEpsilon;
    }

    public void setCreatedByEpsilon(boolean createdByEpsilon) {
        this.createdByEpsilon = createdByEpsilon;
    }

    @Override
    public void eliminate(){
        // is needed?
        bulletModels.remove(this);

        collidables.remove(this);
        movables.remove(this);
        eliminateBulletView(id);
    }

    @Override
    public void onCollision(Collidable other, Point2D intersection) {
        if ( other instanceof CollectibleModel || other instanceof BulletModel) return;
        else if (other instanceof NecropickModel){
            if (((NecropickModel) other).isHovering()) return;
            if (!createdByEpsilon) {
                return;
            }
        }
        else if (other instanceof FinalPanelModel) {
            createImpactWave(this, other, intersection);
            eliminate();
            return;
        }
        if (other instanceof EpsilonModel){
            if (createdByEpsilon) return;
            else {
                this.damage((Entity) other, AttackTypes.MELEE);
                eliminate();
                return;
            }
        }

        // todo: change for multiplayer part ...
        EpsilonModel epsilon = EpsilonModel.getINSTANCE();
        epsilon.health += (int) Profile.getCurrent().EPSILON_HEALTH_REGAIN;
        this.damage((Entity) other, AttackTypes.MELEE);
        eliminate();

    }

    @Override
    public void onCollision(Collidable other, Point2D coll1, Point2D coll2) {

    }
}
