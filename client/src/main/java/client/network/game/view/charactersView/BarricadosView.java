package client.network.game.view.charactersView;

import javax.swing.*;
import java.awt.*;

import static shared.model.imagetools.ToolBox.getBufferedImage;

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
