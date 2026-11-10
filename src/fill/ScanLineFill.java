package fill;

import model.Edge;
import model.Point2D;
import model.Polygon;
import raster.Raster;
import rasterize.PolygonRasterizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanLineFill implements Filler {
    private final Raster raster;
    private final PolygonRasterizer polygonRasterizer;

    private int fillColor;
    private Polygon polygon;

    public ScanLineFill(Raster raster, PolygonRasterizer polygonRasterizer) {
        this.raster = raster;
        this.polygonRasterizer = polygonRasterizer;
        setFillColor(0x00ff00);
    }

    @Override
    public void fill() {
        if (polygon.getSize() < 3) return;

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < polygon.getSize(); i++) {
            Point2D pointA = polygon.getPoint(i);
            Point2D pointB = polygon.getPoint((i + 1) % polygon.getSize());

            Edge edge = new Edge(pointA, pointB);
            if (!edge.isHorizontal()) {
                edge.orientate();
                edges.add(edge);
            }
        }

        int yMin = polygon.getPoint(0).getY();
        int yMax = yMin;
        for (int i = 1; i < polygon.getSize(); i++) {
            int pointY = polygon.getPoint(i).getY();
            if (pointY<yMin){
                yMin = pointY;
            } else if (pointY>yMax) {
                yMax = pointY;
            }
        }

        for (int y = yMin; y <= yMax; y++) {
            List<Integer> intersections = new ArrayList<>();
            for (Edge edge : edges) {
                if (!edge.isIntersection(y)) continue;
                var x = edge.getIntersection(y);
                 intersections.add(x);
            }
            Collections.sort(intersections);
            for (int i = 0; i + 1 < intersections.size(); i += 2) {
                int xStart = intersections.get(i);
                int xEnd = intersections.get(i + 1);
                for (int j = xStart; j<= xEnd; j++ ){
                    raster.setPixel(j,y,fillColor);
                }
            }
        }

        polygonRasterizer.rasterize(polygon);
    }

    @Override
    public void setFillColor(int color) {
        this.fillColor = color;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }
}
