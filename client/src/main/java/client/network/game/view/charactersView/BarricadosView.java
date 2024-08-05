package client.network.game.view.charactersView;

import shared.Model.MyPolygon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

import static shared.Model.imagetools.ToolBox.getBufferedImage;

public class BarricadosView extends GeoShapeView{
    private static Image img;

    public BarricadosView(String id) {
        super(id, img);
    }


    public static void loadImage(){
        Image img = new ImageIcon("./client/src/barricados.png").getImage();
        BarricadosView.img = getBufferedImage(img);
    }

}
