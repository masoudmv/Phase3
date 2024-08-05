package client.network.game.view.charactersView;

import javax.swing.*;
import java.awt.*;

import static shared.Model.imagetools.ToolBox.getBufferedImage;

public class BabyArchmireView extends ArchmireView{
    private static Image img;

    public BabyArchmireView(String id) {
        super(id, img);
    }

    public static void loadImage(){
        Image img = new ImageIcon("./client/src/babyArchmire.png").getImage();
        BabyArchmireView.img = getBufferedImage(img);
    }
}
