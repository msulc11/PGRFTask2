package rasterize;

import model.Point2D;
import model.Polygon;

public class PolygonRasterizer {
    private LineRasterizer lineRasterizer;


    public PolygonRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void setLineRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void rasterize(Polygon polygon) {
        if(polygon.getSize() < 2) return;

        for (int i = 0; i < polygon.getSize(); i++) {
            Point2D pointA = polygon.getPoint(i);
            Point2D pointB = polygon.getPoint((i + 1) % polygon.getSize());
            lineRasterizer.rasterize(pointA.getX(),  pointA.getY(), pointB.getX(), pointB.getY());
        }
    }
}
