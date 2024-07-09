package model.imagetools;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ToolBox {

    public static BufferedImage getBufferedImage(Image image){
        BufferedImage out = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = out.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.drawImage(image, 0, 0, null);
        return out;

    }
}
