package view.junks;

import controller.Game;
import model.entities.Profile;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndGamePanel extends JPanel {
    private static EndGamePanel INSTANCE;

    private EndGamePanel(String title) {
        INSTANCE = this;
        MainFrame frame = MainFrame.getINSTANCE();
        Dimension dimension = new Dimension(600, 600);
        setSize(dimension);
        setVisible(true);
        setLocationToCenter(frame);
        setLayout(null);


        // Adding "Game Over" label
        JLabel gameOver = new JLabel(title);
        gameOver.setBounds(150, 100, 300, 100);
        gameOver.setForeground(Color.red);
        gameOver.setHorizontalAlignment(JLabel.CENTER);
        gameOver.setFont(new Font("Serif", Font.PLAIN, 50));
        add(gameOver);

        // Adding XP label
        JLabel xp = new JLabel("XP: " + Profile.getCurrent().inGameXP);
        xp.setBounds(150, 220, 300, 100);
        xp.setForeground(Color.red);
        xp.setHorizontalAlignment(JLabel.CENTER);
        xp.setFont(new Font("Serif", Font.PLAIN, 20));
        add(xp);

        // Adding fired bullets label
        JLabel firedBullets = new JLabel("Fired Bullets: " + Profile.getCurrent().totalBullets); // Assuming you have a Game class with this info
        firedBullets.setBounds(150, 270, 300, 100);
        firedBullets.setForeground(Color.red);
        firedBullets.setHorizontalAlignment(JLabel.CENTER);
        firedBullets.setFont(new Font("Serif", Font.PLAIN, 20));
        add(firedBullets);

        // Adding successful bullets label
        JLabel successfulBullets = new JLabel("Successful Bullets: " + "dasdasdas"); // Assuming you have this info
        successfulBullets.setBounds(150, 320, 300, 100);
        successfulBullets.setForeground(Color.red);
        successfulBullets.setHorizontalAlignment(JLabel.CENTER);
        successfulBullets.setFont(new Font("Serif", Font.PLAIN, 20));
        add(successfulBullets);

        // Adding elapsed time label
        JLabel elapsedTime = new JLabel("Elapsed Time: " + Game.elapsedTime); // Assuming you have this info
        elapsedTime.setBounds(150, 370, 300, 100);
        elapsedTime.setForeground(Color.red);
        elapsedTime.setHorizontalAlignment(JLabel.CENTER);
        elapsedTime.setFont(new Font("Serif", Font.PLAIN, 20));
        add(elapsedTime);

        // Adding back button
        JButton backButton = new JButton("Back");
        backButton.setBounds(260, 500, 80, 30);
        backButton.setBackground(Color.gray);
        backButton.setForeground(Color.white);
        backButton.setFocusPainted(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action to perform when the back button is clicked
                MainFrame.getINSTANCE().remove(EndGamePanel.this);
                MainFrame.getINSTANCE().revalidate();
                MainFrame.getINSTANCE().repaint();
                new Menu();
                // Additional actions to reset game or navigate back to the main menu
            }
        });
        add(backButton);

        setFocusable(true);
        requestFocus();
        setBackground(Color.black);

        frame.add(this);
        frame.revalidate();
        frame.repaint();
    }

    private void setLocationToCenter(MainFrame frame) {
        setLocation(frame.getWidth() / 2 - getWidth() / 2, frame.getHeight() / 2 - getHeight() / 2);
    }

    public static EndGamePanel getINSTANCE(String title) {
        INSTANCE = new EndGamePanel(title);
        return null;
    }
}
