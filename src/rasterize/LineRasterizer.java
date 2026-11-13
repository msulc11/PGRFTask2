package rasterize;

import raster.Raster;

public abstract class LineRasterizer {
    protected Raster raster;
    protected int color = 0xffffff ;

    public LineRasterizer(Raster raster) {
        this.raster = raster;
    }

    public void rasterize(int x1, int y1, int x2, int y2) {
        System.out.println("rasterize předek");
    }
    public void setColor(int color) {
        this.color = color;
    }
}
