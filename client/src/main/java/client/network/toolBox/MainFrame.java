package client.network.toolBox;


import javax.swing.*;
import java.awt.*;



// it does not have paintComponent method!
public final class MainFrame extends JFrame {
    private static MainFrame INSTANCE;
    public static JLabel label;
    private MainFrame() {
        INSTANCE = this;
        setUndecorated(true);
        setBackground(new Color(0,0,0,0));
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(null);
//        label = new JLabel("<html>Wave: "+ Game.wave + "<br>Elapsed Time: "+ Game.ELAPSED_TIME
//                + "<br> XP: "+Game.inGameXP +"<br>HP: "+ 100);
//        label.setForeground(Color.red);
//        label.setBounds(0,0,120,100);
//        label.setBackground(Color.black);
//        label.setOpaque(true);

    }

    public static MainFrame getINSTANCE() {
        if (INSTANCE==null) INSTANCE=new MainFrame();
        return INSTANCE;
    }


    public void switchToPanel(JPanel panel) {
        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }






}