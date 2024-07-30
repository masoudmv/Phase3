//package model.collision;
//
//import controller.Game;
//import model.charactersModel.BulletModel;
//import model.charactersModel.CollectibleModel;
//
//import java.awt.geom.Point2D;
//import java.util.ArrayList;
//
//import static controller.Utils.relativeLocation;
//
//public interface Coll {
//    ArrayList<Coll> colls = new ArrayList<>();
//    default void checkCollision(Coll coll) {
//        if (isCircular() && !coll.isCircular() && !(this instanceof CollectibleModel)) {
////            handleCirclePolygonCollision(collidable);
//
//        } else if (!isCircular() && coll.isCircular()) {
////            collidable.checkCollision(this);
//        } else if (!isCircular() && !coll.isCircular()) {
////            handlePolygonPolygonCollision(collidable);
//        } else if (isCircular() && coll.isCircular() ) {
////            handleCircleCircleCollision(coll);
//        }
//    }
//
////    private void handleCircleCircleCollision(Coll collidable) {
////        if (!(this instanceof CollectibleModel && collidable instanceof CollectibleModel)) {
////            Point2D dist = relativeLocation(getAnchor(), collidable.getAnchor());
////            double distance = dist.distance(0, 0);
////            if (distance < getRadius() + collidable.getRadius()) {
////                Game.getINSTANCE().sumInGameXpWith(5);
////                if (collidable instanceof CollectibleModel) {
////                    ((CollectibleModel) collidable).remove();
////                }
////            }
////        }
////    }
//
//
//    public Point2D getAnchor();
//    public void onCollision(Coll other, Point2D intersection);
//    public void onCollision(Coll other);
//    boolean isCircular();
//
//}
