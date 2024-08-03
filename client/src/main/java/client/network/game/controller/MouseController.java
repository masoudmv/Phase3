// TODO make this class from scratch!

//package client.network.game.controller;
//
//import client.network.game.controller.constants.Constants;
//import game.model.charactersModel.BulletModel;
//import game.model.charactersModel.EpsilonModel;
//import game.model.entities.Profile;
//import game.model.movement.Direction;
//
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.event.MouseMotionListener;
//import java.awt.geom.Point2D;
//
//import static game.controller.Game.ELAPSED_TIME;
//
//
//public class MouseController implements MouseListener,MouseMotionListener {
//    private EpsilonModel epsilon;
//    public static Point2D mousePosition = null;
//
//    public MouseController(){
//        this.epsilon = EpsilonModel.getINSTANCE();
//        mousePosition = null;
//
//    }
//
//    @Override
//    public void mouseClicked(MouseEvent e) {
//
//
//
//        double startX = epsilon.getAnchor().getX();
//        double startY = epsilon.getAnchor().getY();
//        double mouseX = e.getX();
//        double mouseY = e.getY();
//
//        double deltaX = mouseX - startX;
//        double deltaY = mouseY - startY;
//        double pot = Math.hypot(deltaX, deltaY);
//
//        double velX = deltaX * (Constants.BULLET_VELOCITY / pot);
//        double velY = deltaY * (Constants.BULLET_VELOCITY / pot);
//        Point2D direction = new Point2D.Double(velX, velY);
//        mousePosition = new Point2D.Double(e.getX(), e.getY());
//
//        double now = ELAPSED_TIME;
//        double empowerInitTime = Profile.getCurrent().empowerInitiationTime;
//
//        new BulletModel(epsilon.getAnchor(), new Direction(direction));
//
//        if (now - empowerInitTime < 10){
//            double angle = 10;
//            Point2D right = Utils.rotateVector(direction, Math.toRadians(angle));
//            new BulletModel(epsilon.getAnchor(), new Direction(right));
//            Point2D left = Utils.rotateVector(direction, -Math.toRadians(angle));
//            new BulletModel(epsilon.getAnchor(), new Direction(left));
//        }
//
//    }
//
//    @Override
//    public void mousePressed(MouseEvent e) {
//
//
//
//    }
//
//    @Override
//    public void mouseReleased(MouseEvent e) {
////        playSound();
//
//    }
//
//    @Override
//    public void mouseEntered(MouseEvent e) {
//
//    }
//
//    @Override
//    public void mouseExited(MouseEvent e) {
//
//    }
//
//
//    @Override
//    public void mouseDragged(MouseEvent e) {}
//
//    @Override
//    public void mouseMoved(MouseEvent e) {
////        EpsilonModel epsilon = EpsilonModel.getINSTANCE();
////        Point2D vectorFromEpsilon = Utils.relativeLocation(e.getPoint(), epsilon.getAnchor());
////        double x = vectorFromEpsilon.getX();
////        double y = vectorFromEpsilon.getY();
////        double tangentOfAlpha = vectorFromEpsilon.getY() / vectorFromEpsilon.getX();
////        double alpha=0;
////        if (0<=x && 0<=y) alpha =  Math.atan(tangentOfAlpha);
////        if (x<0 && 0<y)alpha =  Math.atan(tangentOfAlpha)+ Constants.PI;
////        if (x<0 && y<0)alpha =  Math.atan(tangentOfAlpha)+ Constants.PI;
////        if (0<x && y<0) alpha =  Math.atan(tangentOfAlpha)+2* Constants.PI;
////        epsilon.setAngle(alpha);
////        epsilon.updateVertices();
//    }
//
//}
//
