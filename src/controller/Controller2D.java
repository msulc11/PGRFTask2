package controller;

import fill.Filler;
import fill.SeedFill;
import model.Edge;
import model.Point2D;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.LineRasterizerTrivial;
import rasterize.PolygonRasterizer;
import view.Panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Controller2D {
    private final Panel panel;
    private int x, y;

    // Rasterizers
    private LineRasterizer lineRasterizer;
    private PolygonRasterizer polygonRasterizer;

    // Fillers
    private SeedFill seedFiller;

    private final Polygon polygon;
    private Point2D lineStart, lineEnd;

    private Edge edge = new Edge(300, 400, 500, 200);

    public Controller2D(Panel panel) {
        this.panel = panel;

        // Rasterozers
        lineRasterizer = new LineRasterizerGraphics(panel.getRaster());
        //lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);

        // Fillers
        seedFiller = new SeedFill(panel.getRaster());

        polygon = new Polygon();

        initListeners();

        drawScene();
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int startX = panel.getWidth() / 2;
                    int startY = panel.getHeight() / 2;
                    int endX = e.getX();
                    int endY = e.getY();
                    lineStart = new Point2D(startX, startY);
                    lineEnd = new Point2D(endX, endY);

                    drawScene();
                }

                if (e.getButton() == MouseEvent.BUTTON2) {
                    seedFiller.setSeed(new Point2D(e.getX(), e.getY()));
                    drawScene();
                }

                if (e.getButton() == MouseEvent.BUTTON3) {
//                    Point2D point = new Point2D(e.getX(), e.getY());
//                    polygon.addPoint(point);
//                    drawScene();

                    // 1. spočítáme tečný vektor
                    int tx = edge.getX2() - edge.getX1();
                    int ty = edge.getY2() - edge.getY1();

                    // 2. spočítáme normálu
                    double nx = -ty;
                    double ny = tx;
                    double nL = Math.sqrt(nx * nx + ny * ny);
                    nx = nx / nL;
                    ny = ny / nL;

                    // 3. Spočítáme vektor k bodu P (kam jsem kliknul)
                    double vx = e.getX() - edge.getX1();
                    double vy = e.getY() - edge.getY1();
                    double vL = Math.sqrt(vx * vx + vy * vy);
                    vx = vx / vL;
                    vy = vy / vL;

                    // 4. Skalární součin
                    double dot = nx * vx + ny * vy;
                    System.out.println("dot: " + dot);

                    // 5. Porovnat znamenáko výsledku skalárního součinu (úhel)
                    // rozhodnout, jestli je vlevo nebo vpravo
                    if(dot < 0)
                        System.out.println("Vpravo");
                    else
                        System.out.println("Vlevo");
                }
            }
        });
    }

    private void drawScene() {
        panel.getRaster().clear();

        lineRasterizer.rasterize(edge.getX1(), edge.getY1(), edge.getX2(), edge.getY2());

        if (lineStart != null && lineEnd != null)
            lineRasterizer.rasterize(lineStart.getX(), lineStart.getY(), lineEnd.getX(), lineEnd.getY());

        polygonRasterizer.rasterize(polygon);

        seedFiller.fill();

        panel.repaint();
    }
}
