package model.charactersModel.smiley;

import model.charactersModel.EpsilonModel;

import java.awt.geom.Point2D;

import static controller.Utils.findAngleBetweenTwoVectors;
import static controller.Utils.relativeLocation;

public class ProjectileState {
    private boolean isProjecting;
    private double totalRotationAngle;
    private double angularSpeed = 1.5;
    private double angleToEpsilon;
    private double lastShotBulletTime = 0;
    private Point2D center = EpsilonModel.getINSTANCE().getAnchor();

    public void start(Point2D anchor) {
        setAngleToEpsilon(anchor);
        totalRotationAngle = 0;
        isProjecting = true;
    }

    public void setAngleToEpsilon(Point2D anchor){
        center = EpsilonModel.getINSTANCE().getAnchor();
        Point2D rightVec = new Point2D.Double(1, 0);
        Point2D handVec = relativeLocation(anchor, center);
        double angle = findAngleBetweenTwoVectors(rightVec, handVec);
//            angleToEpsilon = angle;

        if (handVec.getY() < 0) angleToEpsilon = Math.toDegrees(angle);
        else angleToEpsilon = Math.toDegrees(angle) + 180;

    }



    public boolean updateRotation() {
        angleToEpsilon += angularSpeed;
        if (angleToEpsilon >= 360) angleToEpsilon -= 360;
        totalRotationAngle += angularSpeed;
        if (totalRotationAngle >= 360) {
            isProjecting = false;
            totalRotationAngle = 0;
        }
        return isProjecting;
    }

    public boolean isProjecting() {
        return isProjecting;
    }

    public double getAngleToEpsilon() {
        return angleToEpsilon;
    }

    public double getAngularSpeed() {
        return angularSpeed;
    }

    public Point2D getCenter() {
        return center;
    }

    public void updateLastShotBulletTime(double time) {
        lastShotBulletTime = time;
    }

    public double getLastShotBulletTime() {
        return lastShotBulletTime;
    }
}