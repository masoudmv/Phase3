//package model;
//
//import model.charactersModel.BulletModel;
//import model.charactersModel.GeoShapeModel;
//import model.collision.Collidable;
//import model.movement.Direction;
//import javax.swing.*;
//import java.util.concurrent.atomic.AtomicInteger;
//import static controller.constants.EntityConstants.EPSILON_RAPID_SHOOTING_DELAY;
//
//
//public interface Long_Ranged extends Collidable {
//    String getMotionPanelId();
//
//    int getShootingRapidity();
//
//    void setShootingRapidity(int shootingRapidity);
//
//    default void shoot(GeoShapeModel shooter, Direction direction, int damage) {
//        AtomicInteger cnt = new AtomicInteger();
//        Timer rapidFire = new Timer(EPSILON_RAPID_SHOOTING_DELAY.getValue(), null) {{
//            setCoalesce(true);
//        }};
//        rapidFire.addActionListener(e -> {
//            new BulletModel(roundPoint(getAnchor()), getMotionPanelId(), damage) {
//                @Override
//                public boolean collide(Collidable collidable) {
//                    return !(collidable instanceof BulletModel) && collidable != shooter;
//                }
//            }.getMovement().setDirection(direction);
//            cnt.getAndIncrement();
//            if (cnt.get() == getShootingRapidity()) rapidFire.stop();
//        });
//        rapidFire.start();
//    }
//}
