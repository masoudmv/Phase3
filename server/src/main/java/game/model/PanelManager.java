package game.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PanelManager {

    // Assume that no two rigid panels may overlap!
    // Pay Attention. right and left overlap are reversed in Rigid case handling!

    public static void handlePanelPanelCollision(FinalPanelModel frame1, FinalPanelModel frame2) {
        if (!isOverlapping(frame1, frame2)) return;

        if (frame1.isRigid() || frame2.isRigid()) findOverlapExtent(frame1, frame2);
        if (!frame1.isRigid() && !frame2.isRigid()) frame1.adjustEdgesOnOverlap(frame2);

    }

    public static boolean isOverlapping(FinalPanelModel f1, FinalPanelModel f2) {
        Rectangle rect1 = new Rectangle(f1.getArrayListVertices());
        Rectangle rect2 = new Rectangle(f2.getArrayListVertices());

        return !(rect1.right <= rect2.left || rect1.left >= rect2.right ||
                rect1.top <= rect2.bottom || rect1.bottom >= rect2.top);
    }

    public static void findOverlapExtent(FinalPanelModel th, FinalPanelModel other) {
        Rectangle rect1 = new Rectangle(th.getArrayListVertices());
        Rectangle rect2 = new Rectangle(other.getArrayListVertices());

        // Calculate the extent of overlap on each side
        double leftOverlap = rect1.right - rect2.left;
        double rightOverlap = rect2.right - rect1.left;
        double bottomOverlap = rect1.top - rect2.bottom;
        double topOverlap = rect2.top - rect1.bottom;

        double overlap = Math.min(Math.min(leftOverlap, rightOverlap), Math.min(bottomOverlap, topOverlap));

        if (leftOverlap == overlap) {
            trimPanel(th, other, "right", leftOverlap);
        } else if (rightOverlap == overlap) {
            trimPanel(th, other, "left", rightOverlap);
        } else if (bottomOverlap == overlap) {
            trimPanel(th, other, "bottom", bottomOverlap);
        } else if (topOverlap == overlap) {
            trimPanel(th, other, "top", topOverlap);
        }
    }

    public static void trimPanel(FinalPanelModel th, FinalPanelModel other, String side, double overlap) {
        double offset = 1.5;

        if (side.equals("left")) {
            if (th.isRigid()) other.trimRight(overlap + offset);
            else th.trimLeft(overlap + offset);
        } else if (side.equals("right")) {
            if (th.isRigid()) other.trimLeft(overlap + offset);
            else th.trimRight(overlap + offset);
        } else if (side.equals("bottom")) {
            if (th.isRigid()) other.trimTop(overlap + offset);
            else th.trimBottom(overlap + offset);
        } else if (side.equals("top")) {
            if (th.isRigid()) other.trimBottom(overlap + offset);
            else th.trimTop(overlap + offset);
        }
    }

    public static void rigid_rigid(FinalPanelModel frame1, FinalPanelModel frame2) {
        // won't happen!
    }

    public static void rigid_nonRigid(FinalPanelModel frame1, FinalPanelModel frame2) {
        // do nothing with the rigid one!
    }

    public static void nonRigid_rigid(FinalPanelModel frame1, FinalPanelModel frame2) {
        // Implement logic if needed
    }

    public static void nonRigid_nonRigid(FinalPanelModel frame1, FinalPanelModel frame2) {
        // Implement logic if needed
    }
}

class Rectangle {
    double left, right, top, bottom;

    public Rectangle(ArrayList<Point2D> vertices) {
        this.left = Math.min(vertices.get(0).getX(), vertices.get(1).getX());
        this.right = Math.max(vertices.get(0).getX(), vertices.get(1).getX());
        this.bottom = Math.min(vertices.get(0).getY(), vertices.get(2).getY());
        this.top = Math.max(vertices.get(0).getY(), vertices.get(2).getY());
    }
}
