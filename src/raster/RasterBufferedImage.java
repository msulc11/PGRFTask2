package raster;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RasterBufferedImage implements Raster {

    private final BufferedImage image;

    public RasterBufferedImage(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void setPixel(int x, int y, int color) {
        if (!isInside(x, y)) return;

        image.setRGB(x, y, color);
    }

    @Override
    public int getPixel(int x, int y) {
        if (!isInside(x, y)) return 0;
        return image.getRGB(x, y);
    }
    
    private boolean isInside(int x, int y) {
        return x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight();
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void clear() {
        image.getGraphics().clearRect(
                0,
                0,
                image.getWidth(),
                image.getHeight()
        );
    }
}
