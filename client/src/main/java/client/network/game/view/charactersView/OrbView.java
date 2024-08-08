package client.network.game.view.charactersView;

import javax.swing.*;
import java.awt.*;

import static shared.model.imagetools.ToolBox.getBufferedImage;

public class OrbView extends GeoShapeView{
    private double radius;
    private static Image img;

    public OrbView(String id) {
        super(id, img);
//        this.radius = 20;

    }

    public static void loadImage(){
        Image img = new ImageIcon("./client/src/orb.png").getImage();
        OrbView.img = getBufferedImage(img);
    }


    @Override
    public void draw(Graphics g, String panelID){
        Graphics2D g2d = (Graphics2D) g;

        if (locations.get(panelID) == null) return;
        int x = (int) locations.get(panelID).getX();
        int y = (int) locations.get(panelID).getY();


        g2d.drawImage(image, (int) (x - imageWidth/2), (int) (y - imageWidth/2), null);

    }

}
