package client.network.game.view.charactersView;



import javax.swing.*;
import java.awt.*;

import static shared.model.imagetools.ToolBox.getBufferedImage;

public class EpsilonView extends GeoShapeView {
    private double radius;
    private static Image img;
    private int index;
    private static int createdNum = 0;

    public EpsilonView(String id) {
        super(id, img);
        this.radius = 20;
        createdNum ++;
        index = createdNum;

    }

    public static void loadImage(){
        Image img = new ImageIcon("./client/src/epsilon.png").getImage();
        EpsilonView.img = getBufferedImage(img);
    }



    //
    @Override
    public void draw(Graphics g, String panelID){

        Graphics2D g2d = (Graphics2D) g;

        switch (index){
            case 1: g2d.setColor(Color.green); break;
            case 2: g2d.setColor(Color.yellow); break;
            case 3: g2d.setColor(Color.red); break;
        }


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