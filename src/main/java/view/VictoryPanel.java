package view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static controller.Game.*;
import static controller.Sound.stopVictorySound;
import static java.awt.Color.*;
import static model.BulletModel.bulletModels;
import static model.CollectibleModel.collectibleModels;
import static model.charactersModel.EpsilonModel.nullifyEpsilon;
import static model.charactersModel.SquarantineModel.squarantineModels;
import static model.charactersModel.TrigorathModel.trigorathModels;
import static model.collision.Collidable.collidables;
import static model.collision.Impactable.impactables;
import static model.movement.Movable.movables;
import static org.example.Main.totalXP;
import static view.BulletView.bulletViews;
import static view.CollectibleView.collectibleViews;
import static view.MainPanel.nullifyMainPanel;
import static view.Menu.addMenu;
import static view.charactersView.Drawable.drawables;
import static view.charactersView.SquarantineView.squarantineViews;
import static view.charactersView.TrigorathView.trigorathViews;

public class VictoryPanel extends JPanel implements MouseListener {
    private static VictoryPanel INSTANCE;

    public VictoryPanel() {
        INSTANCE = this;
        MainFrame frame = MainFrame.getINSTANCE();
        Dimension dimension = new Dimension(600, 600);
        setSize(dimension);
        setVisible(true);
        setLocationToCenter(frame);
        setLayout(null);

        // Adding labels

        JLabel gameOver = new JLabel("Victory!");
        gameOver.setBounds( 150,100, 300, 100);
        gameOver.setForeground(red);
        gameOver.setLayout(new GridBagLayout());
        gameOver.setHorizontalAlignment(JLabel.CENTER);
        gameOver.setFont(new Font("Serif", Font.PLAIN, 50));
        add(gameOver);



        JLabel xp = new JLabel("XP: " + inGameXP);
        xp.setBounds( 200,220, 200, 100);
        xp.setForeground(red);
        xp.setHorizontalAlignment(JLabel.CENTER);
        xp.setFont(new Font("Serif", Font.PLAIN, 35));
        xp.setLayout(new GridBagLayout());
        add(xp);




        JLabel backLabel = new JLabel("back");
        backLabel.setBounds( 260,370, 70, 30);
        backLabel.setBackground(gray);
        backLabel.setOpaque(true);
        backLabel.setHorizontalAlignment(JLabel.CENTER);
        backLabel.addMouseListener(this);
        add(backLabel);



        setFocusable(true);
        requestFocus();
        setBackground(black);
        frame.add(this);
    }


    private void setLocationToCenter(MainFrame frame) {
        setLocation(frame.getWidth() / 2 - getWidth() / 2, frame.getHeight() / 2 - getHeight() / 2);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        MainFrame.getINSTANCE().remove(this);
        MainFrame.getINSTANCE().repaint();
        collidables.clear();
        impactables.clear();
        movables.clear();
        trigorathModels.clear();
        trigorathViews.clear();
        squarantineModels.clear();
        squarantineViews.clear();
        drawables.clear();
        bulletModels.clear();
        bulletViews.clear();
        collectibleModels.clear();
        collectibleViews.clear();
        totalXP += inGameXP;
        nullifyEpsilon();
        nullifyGameInstance();
        stopVictorySound();


        MainFrame.getINSTANCE().removeMouseListener(MainPanel.getINSTANCE().getMouseController());
        MainFrame.getINSTANCE().removeMouseMotionListener(MainPanel.getINSTANCE().getMouseController());
//        MainFrame.getINSTANCE().removeKeyListener();
        nullifyMainPanel();
        addMenu();
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    public static VictoryPanel getINSTANCE(){
        if (INSTANCE == null) INSTANCE = new VictoryPanel();
        return INSTANCE;
    }

}
