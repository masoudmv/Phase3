package model.imagetools;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ToolBox {

    public static BufferedImage getBufferedImage(Image image){
        BufferedImage out = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = out.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.drawImage(image, 0, 0, null);
        return out;

    }

    public static BufferedImage rotateImage(BufferedImage originalImage, double angle) {
        // Calculate the dimensions of the new image
        double radians = Math.toRadians(angle);
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Calculate the dimensions of the rotated image
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int newWidth = (int) Math.floor(originalWidth * cos + originalHeight * sin);
        int newHeight = (int) Math.floor(originalHeight * cos + originalWidth * sin);

        // Create a new BufferedImage
        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g2d = rotatedImage.createGraphics();

        // Apply the rotation
        AffineTransform transform = new AffineTransform();
        // Translate to the center of the new image
        transform.translate(originalWidth / 2, originalHeight / 2);
        // Rotate around the center of the original image
        transform.rotate(radians);
        // Translate back to the top-left corner of the original image
        transform.translate(-originalWidth / 2, -originalHeight / 2);

        g2d.setTransform(transform);
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();

        return rotatedImage;
    }
}
