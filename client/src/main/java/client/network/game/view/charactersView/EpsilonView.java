package client.network.game.view.charactersView;



import javax.swing.*;
import java.awt.*;

import static shared.Model.imagetools.ToolBox.getBufferedImage;

public class EpsilonView extends GeoShapeView {
    private double radius;
    private static Image img;

    public EpsilonView(String id) {
        super(id, img);
        this.radius = 20;

    }

    public static void loadImage(){
        Image img = new ImageIcon("./client/src/epsilon.png").getImage();
        EpsilonView.img = getBufferedImage(img);
    }



    //
    @Override
    public void draw(Graphics g, String panelID){

//        this.radius = Profile.getCurrent().EPSILON_RADIUS;


        // tofo epsilon polymorpph ability
        Graphics2D g2d = (Graphics2D) g;


        if (locations.get(panelID) == null) return;
        int x = (int) locations.get(panelID).getX();
        int y = (int) locations.get(panelID).getY();


//        g2d.drawImage(image, (int) (x - imageWidth/2), (int) (y - imageWidth/2), null);
        float thickness = 5.0f; // Thickness of the edges
        g2d.setStroke(new BasicStroke(thickness));
//        g2d.setColor(Color.red);
        g2d.drawOval( (int) (x - imageWidth/2), (int) (y - imageWidth/2), 40, 40);

    }


}