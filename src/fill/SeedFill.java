package fill;

import model.Point2D;
import raster.Raster;

public class SeedFill implements Filler {
    private Raster raster;
    private Point2D seed;

    private int backgroundColor, fillColor;

    public SeedFill(Raster raster) {
        this.raster = raster;
        this.fillColor = 0x00ff00;
    }

    @Override
    public void fill() {
        if(seed == null) return;

        fill(seed.getX(), seed.getY());
    }
    public void seedClear(){
        seed = null;
    }

    // rekurze
    private void fill(int x, int y) {
        // 1. načtu barvu pixelu na x, y
        int colorPixel = raster.getPixel(x, y);

        // 2. dostal jsem barvu a musím rozhodnout, jestli chci obarvit pixel
        // rovná se barva barvě pozadí? pokud ano, obarvím, jinak nic nedělám
        if(colorPixel != backgroundColor)
            return;

        // 3. pokud mám obarvit
        // obarvím
        raster.setPixel(x, y, fillColor);
        // vše opakuji pro sousedy (rekurze)
        fill(x + 1, y);
        fill(x - 1, y);
        fill(x, y + 1);
        fill(x, y - 1);
    }

    public void setSeed(Point2D seed) {
        this.seed = seed;
        setBackgroundColor(raster.getPixel(seed.getX(), seed.getY()));
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }
}
