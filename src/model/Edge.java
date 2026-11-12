package model;

public class Edge {
    private int x1, y1, x2, y2;

    public Edge(Point2D a, Point2D b) {
        this.x1 = a.getX();
        this.y1 = a.getY();
        this.x2 = b.getX();
        this.y2 = b.getY();
    }

    public boolean isHorizontal() {
        return y1 == y2;
    }

    public void orientate() {
        if (y1 > y2) {
            int tempX = x1;
            x1 = x2;
            x2 = tempX;

            int tempY = y1;
            y1 = y2;
            y2 = tempY;
        }
    }

    public boolean isIntersection(int y) {
        if (y >= y1 && y <= y2 - 1) {
            return true;
        }
        else return false;
    }

    public int getIntersection(int y) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        float k = dx / (float) dy;
        int rows = y - y1;
        float x = x1 + k * rows;
        return Math.round(x);
    }

    // Poloha bodu vůči hraně: záporné = vpravo, jinak vlevo
    public double sideDecide(Point2D point) {
        // 1. spočítáme tečný vektor
        int tx = x2 - x1;
        int ty = y2 - y1;

        // 2. spočítáme normálu
        double nx = -ty;
        double ny = tx;
        double nL = Math.sqrt(nx * nx + ny * ny);
        nx = nx / nL;
        ny = ny / nL;

        // 3. Spočítáme vektor k bodu P
        double vx = point.getX() - x1;
        double vy = point.getY() - y1;
        double vL = Math.sqrt(vx * vx + vy * vy);
        vx = vx / vL;
        vy = vy / vL;

        // 4. Skalární součin
        return nx * vx + ny * vy;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }
}
