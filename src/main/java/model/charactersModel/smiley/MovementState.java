//package model.charactersModel.smiley;
//
//import controller.Utils;
//
//import java.awt.geom.Point2D;
//
//import static controller.Utils.findDistance;
//
//public class MovementState {
//    private boolean isMovingToDestination;
//    private boolean isDecelerating;
//    private double speed = 0;
//    private double acceleration;
//    private double deceleration;
//    private double distance;
//    private double halfwayDistance;
//    private double maxSpeed;
//    private Point2D destination;
//
//    public boolean isMoving() {
//        return isMovingToDestination;
//    }
//
//    public double getSpeed() {
//        return speed;
//    }
//
//    public void startMove(Point2D destination, Point2D startPosition) {
//        this.destination = destination;
//        this.isMovingToDestination = true;
//        this.isDecelerating = false;
//        this.speed = 0;
//
//        distance = Utils.findDistance(startPosition, destination);
//        halfwayDistance = distance / 2;
//
//        double t = 8; // Total time in frames (assuming 4 seconds at 60 FPS)
//        acceleration = 4 * distance / (t * t);
//        deceleration = -acceleration;
//        maxSpeed = acceleration * t / 2;
//    }
//
//    public void updateSpeed() {
//        double currentDistance = Utils.findDistance(destination, destination);
//        if (!isDecelerating) {
//            speed += acceleration * (1.0 / 60);
//            if (speed > maxSpeed) {
//                speed = maxSpeed;
//            }
//            if (currentDistance <= halfwayDistance) {
//                isDecelerating = true;
//            }
//        } else {
//            speed += deceleration * (1.0 / 60);
//            if (speed < 0 || currentDistance <= 0) {
//                speed = 0;
//                isMovingToDestination = false;
//            }
//        }
//    }
//}
