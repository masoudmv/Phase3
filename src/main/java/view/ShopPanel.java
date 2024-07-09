package view;

import controller.Game;
import controller.Update;
import model.charactersModel.EpsilonModel;
import org.example.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//import static controller.Game.ShopAbility.*;
import static java.awt.Color.white;

public class ShopPanel extends JPanel implements MouseListener {

    static Rectangle banish = new Rectangle(50,50,200,200);
    static Rectangle empower= new Rectangle(300,50,200,200);
    static Rectangle heal= new Rectangle(550,50,200,200);


    public ShopPanel() {
        MainFrame frame = MainFrame.getINSTANCE();
        Dimension dimension = new Dimension(800, 300);
        setSize(dimension);
        setLocationToCenter(frame);
        setBackground(white);


//        Label heal = new Label("O' Apollo Heal");
//        heal.setVisible(true);
//        heal.setForeground(blue);
//        heal.setBounds(500,500,1000,1000);
//        add(heal);
//        frame.add(heal);

        setVisible(true);
        frame.add(this);
    }





    public void setLocationToCenter(MainFrame frame){
        setLocation(frame.getWidth()/2-getWidth()/2,frame.getHeight()/2-getHeight()/2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.black);
        g.drawImage(Main.heal, 50, 50, this);
        g.drawImage(Main.empower, 300, 50, this);
        g.drawImage(Main.banish, 550, 50, this);

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if ( e.getY()>334 && e.getY()<534 && 418<e.getX() && e.getX()<618 && Game.getINSTANCE().getInGameXp() >= 50 && Update.shopAbility==null) {
            Update.shopAbility = Update.ShopAbility.heal;
            Game.getINSTANCE().sumInGameXpWith(-50);
            MainFrame.label.setText("<html>Wave: "+ Game.wave + "<br>Elapsed Time: "+ (int) Game.elapsedTime
                    + "<br> XP: "+Game.inGameXP +"<br>HP: "+ EpsilonModel.getINSTANCE().getHp());
        }
        if ( e.getY()>334 && e.getY()<534 && 668<e.getX() && e.getX()<868 && Game.getINSTANCE().getInGameXp() >= 75&& Update.shopAbility==null) {
            Update.shopAbility = Update.ShopAbility.empower;
            Game.getINSTANCE().sumInGameXpWith(-75);
            MainFrame.label.setText("<html>Wave: "+ Game.wave + "<br>Elapsed Time: "+ (int) Game.elapsedTime
                    + "<br> XP: "+Game.inGameXP +"<br>HP: "+ EpsilonModel.getINSTANCE().getHp());        }
        if ( e.getY()>334 && e.getY()<534 && 918<e.getX() && e.getX()<1118 && Game.getINSTANCE().getInGameXp() >= 100&& Update.shopAbility==null) {
            Update.shopAbility = Update.ShopAbility.banish;
            Game.getINSTANCE().sumInGameXpWith(-100);
            MainFrame.label.setText("<html>Wave: "+ Game.wave + "<br>Elapsed Time: "+ (int) Game.elapsedTime
                    + "<br> XP: "+Game.inGameXP +"<br>HP: "+ EpsilonModel.getINSTANCE().getHp());

        }

    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

}
