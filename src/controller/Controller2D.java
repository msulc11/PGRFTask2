package controller;

import clip.CohenSutherland;
import fill.ScanLineFill;
import fill.SeedFill;
import model.Point2D;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.PolygonRasterizer;
import view.Panel;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Controller2D {
    private final Panel panel;

    // Rasterizers
    private LineRasterizer lineRasterizer;
    private PolygonRasterizer polygonRasterizer;

    // Fillers
    private SeedFill seedFiller;
    private ScanLineFill scanLineFiller;

    // Clipping
    private CohenSutherland cohenSutherland;
    private Polygon clipWindow;

    private final Polygon polygon;
    private Point2D lineStart, lineEnd;

    public Controller2D(Panel panel) {
        this.panel = panel;

        // Rasterozers
        lineRasterizer = new LineRasterizerGraphics(panel.getRaster());
        //lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);

        // Fillers
        seedFiller = new SeedFill(panel.getRaster());

        polygon = new Polygon();

        scanLineFiller = new ScanLineFill(panel.getRaster(), polygonRasterizer);
        scanLineFiller.setPolygon(polygon);

        cohenSutherland = new CohenSutherland(200, 150, 600, 450);
        clipWindow = new Polygon();
        clipWindow.addPoint(new Point2D(200, 150));
        clipWindow.addPoint(new Point2D(600, 150));
        clipWindow.addPoint(new Point2D(600, 450));
        clipWindow.addPoint(new Point2D(200, 450));

        initListeners();

        drawScene();
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.isControlDown()) {
                    polygon.addPoint(new Point2D(e.getX(), e.getY()));
                    drawScene();
                    return;
                }

                if (e.getButton() == MouseEvent.BUTTON1) {
                    int startX = e.getX();
                    int startY = e.getY();
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
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // při tažení se mění jen konec úsečky; s Ctrl se kreslí polygon, ne úsečka
                if (!SwingUtilities.isLeftMouseButton(e) || e.isControlDown() || lineStart == null)
                    return;

                lineEnd = new Point2D(e.getX(), e.getY());
                drawScene();
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    lineStart = null;
                    lineEnd = null;
                    polygon.pointsClear();
                    seedFiller.seedClear();
                    drawScene();
                }
            }
        });
    }

    private void drawScene() {
        panel.getRaster().clear();

        polygonRasterizer.rasterize(clipWindow);

        if (lineStart != null && lineEnd != null) {
            double[] line = cohenSutherland.clip(lineStart.getX(), lineStart.getY(), lineEnd.getX(), lineEnd.getY());
            if (line != null)
                lineRasterizer.rasterize((int) line[0], (int) line[1], (int) line[2], (int) line[3]);
        }

        scanLineFiller.fill();

        seedFiller.fill();

        panel.repaint();
    }
}
