package controller;

import model.charactersModel.BulletModel;
import model.charactersModel.EpsilonModel;
import model.movement.Direction;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import static controller.UserInterfaceController.updateGeoShapeViewProperties;
import static controller.constants.Constants.BULLET_VELOCITY;
import static controller.constants.Constants.PI;
import static controller.Game.*;
import static controller.GameLoop.lastShot;
import static controller.Utils.relativeLocation;
//import static controller.Sound.sound;
//import static controller.SoundHandler.doPlay;
//import static controller.SoundHandler.playSound;

public class MouseController implements MouseListener,MouseMotionListener {
    private EpsilonModel epsilon;
    public static Point2D mousePosition = null;
    public static boolean tripleShot=false;
    public static BulletModel lastBullet=null;

    public MouseController(){
        this.epsilon = EpsilonModel.getINSTANCE();
        mousePosition = null;
        tripleShot=false;
        lastBullet=null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (ELAPSED_TIME -lastShot>0.01) {

            double startX = epsilon.getAnchor().getX();
            double startY = epsilon.getAnchor().getY();
            double mouseX = e.getX();
            double mouseY = e.getY();

            double deltaX = mouseX - startX;
            double deltaY = mouseY - startY;
            double pot = Math.hypot(deltaX, deltaY);

            double velX = deltaX * (BULLET_VELOCITY / pot);
            double velY = deltaY * (BULLET_VELOCITY / pot);
            Point2D direction = new Point2D.Double(velX, velY);
            mousePosition=new Point2D.Double(e.getX(), e.getY());
            lastBullet = new BulletModel(epsilon.getAnchor(), new Direction(direction));
            lastShot= ELAPSED_TIME;
            if (ELAPSED_TIME < empowerEndTime) tripleShot=true;

            // sets the initial location of bullet and all entities (needs to be improved ... )
//            updateGeoShapeViewProperties();

        }



    }

    @Override
    public void mousePressed(MouseEvent e) {
//        Sound sound = new Sound("C:\\Users\\masoo\\Desktop\\Projects\\windowkill_AP\\src\\main\\resources\\burst2.wav");
//        sound.play();


    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        playSound();

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        EpsilonModel epsilon = EpsilonModel.getINSTANCE();
        Point2D vectorFromEpsilon = relativeLocation(e.getPoint(), epsilon.getAnchor());
        double x = vectorFromEpsilon.getX();
        double y = vectorFromEpsilon.getY();
        double tangentOfAlpha = vectorFromEpsilon.getY() / vectorFromEpsilon.getX();
        double alpha=0;
        if (0<=x && 0<=y) alpha =  Math.atan(tangentOfAlpha);
        if (x<0 && 0<y)alpha =  Math.atan(tangentOfAlpha)+PI;
        if (x<0 && y<0)alpha =  Math.atan(tangentOfAlpha)+PI;
        if (0<x && y<0) alpha =  Math.atan(tangentOfAlpha)+2*PI;
        epsilon.setAngle(alpha);
        epsilon.updateVertices();
    }

}

