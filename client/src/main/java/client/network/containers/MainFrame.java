package client.network.containers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class MainFrame extends JFrame {
    private static MainFrame INSTANCE;
    private Point initialClick;

    private MainFrame() {
        INSTANCE = this;
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(new GridBagLayout());
        makeFrameDraggable();
    }

    public static MainFrame getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new MainFrame();
        return INSTANCE;
    }

    public void switchToPanel(JPanel panel) {
        getContentPane().removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        getContentPane().add(panel, gbc);
        revalidate();
        repaint();
    }

    private void makeFrameDraggable() {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // get location of the window
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                // Determine how much the mouse moved since the initial click
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                // Move window to this position
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                setLocation(X, Y);
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }
}
