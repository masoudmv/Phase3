package client.network.game.controller;

import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import static client.network.RequestFactory.createClickedRequest;

public class MouseController implements MouseListener, MouseMotionListener {
    public static Point2D mousePosition = null;
    private static final int DEBOUNCE_DELAY = 100; // Delay in milliseconds
    private Timer debounceTimer;

    public MouseController() {
        mousePosition = null;
        debounceTimer = new Timer(DEBOUNCE_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                debounceTimer.stop();
            }
        });
        debounceTimer.setRepeats(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {


    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!debounceTimer.isRunning()) {
            debounceTimer.start();
            int mouseX = e.getX();
            int mouseY = e.getY();
            Point pos = new Point(mouseX, mouseY);
            createClickedRequest(pos);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}
}
