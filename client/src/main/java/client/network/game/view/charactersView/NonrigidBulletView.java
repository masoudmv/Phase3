package client.network.game.view.charactersView;

import javax.swing.*;
import java.awt.*;

import static shared.model.imagetools.ToolBox.getBufferedImage;
import static shared.model.imagetools.ToolBox.rotateImage;

public class NonrigidBulletView extends GeoShapeView{
    private static Image img;

    public NonrigidBulletView(String id) {
        super(id, img);
    }


    public static void loadImage(){
        Image img = new ImageIcon("./client/src/bullet.png").getImage();
        NonrigidBulletView.img = getBufferedImage(img);
    }

    @Override
    public void draw(Graphics g, String panelID){
        Graphics2D g2d = (Graphics2D) g;
        if (locations.get(panelID) == null || image == null) return;

        g2d.setColor(Color.white);

        int x = (int) locations.get(panelID).getX();
        int y = (int) locations.get(panelID).getY();

        g2d.drawImage(rotateImage(image, Math.toDegrees(-angle)), (int) (x - imageWidth/2), (int) (y - imageWidth/2), null);

    }
}
