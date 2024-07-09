package model.movement;

import java.awt.*;
import java.awt.geom.Point2D;

import static controller.Constants.EPSILON_MAX_SPEED;
import static controller.Constants.SPEED;
import static controller.Utils.multiplyVector;

public class Direction {
    public boolean isUpward = false;
    public boolean isDownward = false;
    double slope;
    double magnitude;
    private DirectionState state = DirectionState.neutral;
    public Direction(Point2D point){
        if (point.getX()==0 && point.getY()>0) isUpward = true;
        if (point.getX()==0 && point.getY()<0) isDownward=true;
        if (point.getX()==0 && point.getY()==0) state=DirectionState.neutral;
        else {
            this.slope = point.getY() / point.getX();
            if (point.getX() > 0) this.state = DirectionState.positive;
            else this.state=DirectionState.negative;
        }
        this.magnitude = Math.hypot(point.getX(),point.getY());
    }

    public Direction(double angle) {
        angle=angle - Math.floor(angle/360) * 360;
        if (angle==90) isUpward=true;
        if (angle==270) isDownward=true;
        double x=Math.cos(Math.toRadians(angle));
        double y=Math.sin(Math.toRadians(angle));
        this.slope=y/x;

        if ((angle>=0 && angle<180)) state=DirectionState.positive;
        else state=DirectionState.negative;
    }

    public Point2D getNormalizedDirectionVector(){
        if (state==DirectionState.neutral) return new Point(0,0);
        if (isUpward) return new Point2D.Double(0,1);
        if (isDownward) return new Point2D.Double(0,-1);
        double magnitude=Math.sqrt(1+slope*slope);
        Point2D.Double normalVector=new Point2D.Double(1/magnitude,slope/magnitude);
        if (state==DirectionState.negative) normalVector=new Point2D.Double(-normalVector.x,-normalVector.y);
        return normalVector;
    }

    public Point2D getTrigorathNormalizedDirectionVector(){
        if (state==DirectionState.neutral) return new Point(0,0);
        if (isUpward) return new Point2D.Double(0,1);
        if (isDownward) return new Point2D.Double(0,-1);
        double magnitude=Math.sqrt(1+slope*slope);
        Point2D.Double normalVector=new Point2D.Double(1/magnitude,slope/magnitude);
        if (state==DirectionState.negative) normalVector=new Point2D.Double(-normalVector.x,-normalVector.y);
        return multiplyVector(normalVector, 1.5);
    }



    public Point2D getDirectionVector(){
        if (state==DirectionState.neutral) return new Point(0,0);
        if (isUpward) return new Point2D.Double(0,magnitude);
        if (isDownward) return new Point2D.Double(0,-magnitude);
        double magnit = Math.sqrt(1+slope*slope);
        Point2D.Double normalVector=new Point2D.Double(magnitude/magnit,magnitude * slope/magnit);
        if (state==DirectionState.negative) normalVector=new Point2D.Double(-normalVector.x,-normalVector.y);
        return normalVector;
    }

    public void accelerateDirection(double speed){
//        magnitude *= 1.2;
        if (magnitude < speed){
            magnitude *= 1.15;
        }
    }

    public void decelerateDirection(double speed){
//        magnitude *= 1.2;
        if (magnitude > speed){
            magnitude = magnitude / 0.8;
        }
    }




    public void adjustDirectionMagnitude(){
        if (magnitude >SPEED) this.magnitude = SPEED;
    }
    public void adjustTrigorathDirectionMagnitude(){
        if (magnitude > 1.5) this.magnitude = 1.5;
    }
    public void adjustEpsilonDirectionMagnitude(){
        if (magnitude >EPSILON_MAX_SPEED) this.magnitude = EPSILON_MAX_SPEED;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public enum DirectionState{
        negative,positive,neutral
    }
}