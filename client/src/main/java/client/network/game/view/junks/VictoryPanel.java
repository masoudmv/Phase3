//package client.network.game.view.junks;
//
//
//import client.network.game.view.MainFrame;
//import game.controller.Game;
//import game.controller.Sound;
//import game.example.Main;
//import game.model.charactersModel.BulletModel;
//import game.model.charactersModel.CollectibleModel;
//import game.model.charactersModel.EpsilonModel;
//import game.model.collision.Collidable;
//import game.model.collision.Impactable;
//import game.model.movement.Movable;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//
//import static game.model.charactersModel.SquarantineModel.squarantineModels;
//import static game.model.charactersModel.TrigorathModel.trigorathModels;
//import static game.view.charactersView.BulletView.bulletViews;
//import static game.view.junks.Menu.addMenu;
//import static java.awt.Color.*;
//
//
//public class VictoryPanel extends JPanel implements MouseListener {
//    private static VictoryPanel INSTANCE;
//
//    public VictoryPanel() {
//        INSTANCE = this;
//        MainFrame frame = MainFrame.getINSTANCE();
//        Dimension dimension = new Dimension(600, 600);
//        setSize(dimension);
//        setVisible(true);
//        setLocationToCenter(frame);
//        setLayout(null);
//
//        // Adding labels
//
//        JLabel gameOver = new JLabel("Victory!");
//        gameOver.setBounds( 150,100, 300, 100);
//        gameOver.setForeground(red);
//        gameOver.setLayout(new GridBagLayout());
//        gameOver.setHorizontalAlignment(JLabel.CENTER);
//        gameOver.setFont(new Font("Serif", Font.PLAIN, 50));
//        add(gameOver);
//
//
//
//        JLabel xp = new JLabel("XP: " + Game.inGameXP);
//        xp.setBounds( 200,220, 200, 100);
//        xp.setForeground(red);
//        xp.setHorizontalAlignment(JLabel.CENTER);
//        xp.setFont(new Font("Serif", Font.PLAIN, 35));
//        xp.setLayout(new GridBagLayout());
//        add(xp);
//
//
//
//
//        JLabel backLabel = new JLabel("back");
//        backLabel.setBounds( 260,370, 70, 30);
//        backLabel.setBackground(gray);
//        backLabel.setOpaque(true);
//        backLabel.setHorizontalAlignment(JLabel.CENTER);
//        backLabel.addMouseListener(this);
//        add(backLabel);
//
//
//
//        setFocusable(true);
//        requestFocus();
//        setBackground(black);
//        frame.add(this);
//    }
//
//
//    private void setLocationToCenter(MainFrame frame) {
//        setLocation(frame.getWidth() / 2 - getWidth() / 2, frame.getHeight() / 2 - getHeight() / 2);
//    }
//
//    @Override
//    public void mouseClicked(MouseEvent e) {
//        MainFrame.getINSTANCE().remove(this);
//        MainFrame.getINSTANCE().repaint();
//        Collidable.collidables.clear();
//        Impactable.impactables.clear();
//        Movable.movables.clear();
//        trigorathModels.clear();
////        trigorathViews.clear();
//        squarantineModels.clear();
////        squarantineViews.clear();
//        BulletModel.bulletModels.clear();
//        bulletViews.clear();
//        CollectibleModel.collectibleModels.clear();
////        collectibleViews.clear();
//        Main.totalXP += Game.inGameXP;
//        EpsilonModel.nullifyEpsilon();
//        Game.nullifyGameInstance();
//        Sound.stopVictorySound();
//
//
////        MainFrame.getINSTANCE().removeMouseListener(MainPanel.getINSTANCE().getMouseController());
////        MainFrame.getINSTANCE().removeMouseMotionListener(MainPanel.getINSTANCE().getMouseController());
////        MainFrame.getINSTANCE().removeKeyListener();
////        nullifyMainPanel();
//        addMenu();
//    }
//
//    @Override
//    public void mousePressed(MouseEvent e) {}
//
//    @Override
//    public void mouseReleased(MouseEvent e) {}
//
//    @Override
//    public void mouseEntered(MouseEvent e) {}
//
//    @Override
//    public void mouseExited(MouseEvent e) {}
//    public static VictoryPanel getINSTANCE(){
//        if (INSTANCE == null) INSTANCE = new VictoryPanel();
//        return INSTANCE;
//    }
//
//}
