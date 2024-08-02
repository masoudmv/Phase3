package model.movement;

import java.awt.geom.Point2D;
import java.util.concurrent.CopyOnWriteArrayList;

public interface Movable {
    CopyOnWriteArrayList<Movable> movables = new CopyOnWriteArrayList<>();
    boolean isCircular();
    void setDirection(Direction direction);
    Direction getDirection();
    void update(Direction direction);
    void update();
    void friction();
    Point2D getAnchor();

    }