package fill;

import model.Point2D;
import raster.Raster;

import java.util.ArrayList;
import java.util.List;

public class SeedFill implements Filler {
    private Raster raster;
    private final List<Point2D> seeds = new ArrayList<>();

    private int backgroundColor, fillColor, borderColor = 0xffffff;
    private boolean hranice = false;

    public SeedFill(Raster raster) {
        this.raster = raster;
        this.fillColor = 0x00ff00;
    }

    @Override
    public void fill() {
        for (Point2D seed : seeds) {
            backgroundColor = raster.getPixel(seed.getX(), seed.getY());
            if ((backgroundColor & 0xffffff) == (fillColor & 0xffffff))
                continue;
            fill(seed.getX(), seed.getY());
        }
    }

    public void seedClear(){
        seeds.clear();
    }

    // rekurze
    private void fill(int x, int y) {
        // 1. načtu barvu pixelu na x, y
        int colorPixel = raster.getPixel(x, y);
        if(hranice){
            if (x < 0 || x >= raster.getWidth() || y < 0 || y >= raster.getHeight())
                return;
            int rgb = colorPixel & 0xffffff;
            if (rgb == (borderColor & 0xffffff) || rgb == (fillColor & 0xffffff))
                return;
        }else{
            if (colorPixel != backgroundColor){
                return;
            }
        }

        raster.setPixel(x, y, fillColor);
        // vše opakuji pro sousedy (rekurze)
        fill(x + 1, y);
        fill(x - 1, y);
        fill(x, y + 1);
        fill(x, y - 1);
    }

    public void addSeed(Point2D seed) {
        seeds.add(seed);
    }

    @Override
    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public void setHranice(boolean hranice) {
        this.hranice = hranice;
    }

    public boolean isHranice() {
        return hranice;
    }
}

