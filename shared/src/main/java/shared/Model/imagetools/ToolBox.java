package shared.Model.imagetools;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ToolBox {

    public static BufferedImage getBufferedImage(Image image) {
        BufferedImage out = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = out.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose(); // Dispose the graphics context
        return out;
    }

    public static BufferedImage rotateImage(BufferedImage originalImage, double angle) {
        double radians = Math.toRadians(angle);  // Convert angle to radians
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Calculate the dimensions of the rotated image
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int newWidth = (int) Math.floor(originalWidth * cos + originalHeight * sin);
        int newHeight = (int) Math.floor(originalHeight * cos + originalWidth * sin);

        // Create a new BufferedImage with transparency support
        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotatedImage.createGraphics();

        // Set rendering hints for better image quality
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Apply the rotation with translation to keep the image centered
        AffineTransform transform = new AffineTransform();
        // Translate to the center of the new image
        transform.translate((newWidth - originalWidth) / 2, (newHeight - originalHeight) / 2);
        // Rotate around the center of the original image
        transform.rotate(radians, originalWidth / 2, originalHeight / 2);

        // Apply the transformation and draw the original image
        g2d.setTransform(transform);
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose(); // Dispose the graphics context

        return rotatedImage;
    }
}
