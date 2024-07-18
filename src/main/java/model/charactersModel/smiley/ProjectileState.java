package model.charactersModel.smiley;

import controller.Utils;
import model.charactersModel.EpsilonModel;

import java.awt.geom.Point2D;

public class ProjectileState {
    private boolean isProjecting;
    private double totalRotationAngle;
    private double angularSpeed = 5;
    private double angleToEpsilon;
    private double lastShotBulletTime = 0;
    private Point2D center = EpsilonModel.getINSTANCE().getAnchor();

    public void start() {
        totalRotationAngle = 0;
        isProjecting = true;
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