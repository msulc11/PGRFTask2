package rasterize;

import raster.Raster;

public class LineRasterizerTrivial extends LineRasterizer {
    public LineRasterizerTrivial(Raster raster) {
        super(raster);
    }
    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {
 //nemohl jsem prijit jak udelat aby nebyly čáry tečkované -> AI poradila použít Math.abs
        if (Math.abs(y2 - y1) <= Math.abs(x2 - x1)) {
            if (x1 > x2) {
                int temp = x1; x1 = x2; x2 = temp;
                temp = y1; y1 = y2; y2 = temp;
            }
            // 1. spočítat k a q
            float k = (y2 - y1) / (float) (x2 - x1);
            float q = y1 - k * x1;

            // 2. napsat for cyklus od x1 po x2
            for(int x = x1; x <= x2; x++) {
                int y = Math.round(k * x + q);
                // 3. obarvit pixel
                raster.setPixel(x, y, color);
            }
        }
        else {
            if (y1 > y2) {
                int temp = x1; x1 = x2; x2 = temp;
                temp = y1; y1 = y2; y2 = temp;
            }
            // 1. spočítat k
            float k = (x2 - x1) / (float) (y2 - y1);

            // 2. napsat for cyklus od y1 po y2
            for(int y = y1; y <= y2; y++) {
                int x = Math.round(x1 + k * (y - y1));
                // 3. obarvit pixel
                raster.setPixel(x, y, color);
            }
        }

    }
}
