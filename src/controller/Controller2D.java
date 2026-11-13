package controller;

import clip.CohenSutherland;
import fill.ScanLineFill;
import fill.SeedFill;
import model.Line;
import model.Point2D;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import rasterize.PolygonRasterizer;
import view.Panel;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

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
    private final List<Line> lines = new ArrayList<>();
    private Point2D lineStart, lineEnd;
    private Point2D polygonPreview; // nový vrchol polygonu během tažení, potvrdí se uvolněním
    private boolean polygonMode;

    public Controller2D(Panel panel) {
        this.panel = panel;

        // Rasterozers
        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);

        // Fillers
        seedFiller = new SeedFill(panel.getRaster());

        polygon = new Polygon();

        scanLineFiller = new ScanLineFill(panel.getRaster(), polygonRasterizer);
        scanLineFiller.setPolygon(polygon);
        scanLineFiller.setFillColor(0xffff00);

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
                if (e.getButton() == MouseEvent.BUTTON1 && polygonMode) {
                    polygonPreview = new Point2D(e.getX(), e.getY());
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
                    seedFiller.addSeed(new Point2D(e.getX(), e.getY()));
                    drawScene();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1)
                    return;

                if (polygonMode && polygonPreview != null) {
                    polygon.addPoint(polygonPreview);
                    polygonPreview = null;
                    drawScene();
                    return;
                }

                if (polygonMode || lineStart == null)
                    return;

                lines.add(new Line(lineStart, lineEnd));
                lineStart = null;
                lineEnd = null;
                drawScene();
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e))
                    return;


                if (polygonMode) {
                    if (polygonPreview == null)
                        return;
                    polygonPreview = new Point2D(e.getX(), e.getY());
                    drawScene();
                    return;
                }


                if (lineStart == null)
                    return;

                lineEnd = new Point2D(e.getX(), e.getY());
                drawScene();
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    polygonMode = !polygonMode;
                    System.out.println("Režim polygonů: " + polygonMode);
                }
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    lines.clear();
                    lineStart = null;
                    lineEnd = null;
                    polygonPreview = null;
                    polygon.pointsClear();
                    seedFiller.seedClear();
                    seedFiller.setHranice(false);
                    drawScene();
                }
                if (e.getKeyCode() == KeyEvent.VK_B) {
                seedFiller.setHranice((!seedFiller.isHranice()));
                System.out.println("Seed fill omezení hranicí: " + seedFiller.isHranice());
                drawScene();
                }

            }
        });
    }

    private void drawScene() {
        panel.getRaster().clear();

        polygonRasterizer.rasterize(clipWindow);

        for (Line line : lines)
            drawClippedLine(line.getStart(), line.getEnd());

        if (lineStart != null && lineEnd != null)
            drawClippedLine(lineStart, lineEnd);

        // polygon jinou barvou než hranice (bílá), aby byl vidět rozdíl mezi omezením barvou hranice a pozadí
        lineRasterizer.setColor(0xff8800);
        scanLineFiller.fill();
        polygonRasterizer.rasterize(polygon);
        if (polygonPreview != null && polygon.getSize() > 0) {
            Point2D first = polygon.getPoint(0);
            Point2D last = polygon.getPoint(polygon.getSize() - 1);
            lineRasterizer.rasterize(first.getX(), first.getY(), polygonPreview.getX(), polygonPreview.getY());
            lineRasterizer.rasterize(last.getX(), last.getY(), polygonPreview.getX(), polygonPreview.getY());
        }
        lineRasterizer.setColor(0xffffff);

        seedFiller.fill();

        panel.repaint();
    }

    private void drawClippedLine(Point2D start, Point2D end) {
        double[] line = cohenSutherland.clip(start.getX(), start.getY(), end.getX(), end.getY());
        if (line != null)
            lineRasterizer.rasterize((int) line[0], (int) line[1], (int) line[2], (int) line[3]);
    }
}
