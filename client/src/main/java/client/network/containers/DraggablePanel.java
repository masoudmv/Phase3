package client.network.containers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DraggablePanel extends JPanel {
    private Point initialClick;

    public DraggablePanel() {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // get location of the window
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(DraggablePanel.this);
                int thisX = frame.getLocation().x;
                int thisY = frame.getLocation().y;

                // Determine how much the mouse moved since the initial click
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                // Move window to this position
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                frame.setLocation(X, Y);
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }
}
