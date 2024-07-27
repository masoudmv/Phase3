package client.network.toolBox;

import javax.swing.*;
import java.awt.*;

public final class MainFrame extends JFrame {
    private static MainFrame INSTANCE;

    private MainFrame() {
        INSTANCE = this;
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(new GridBagLayout());
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
}
