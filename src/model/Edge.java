package model;

public class Edge {
    private int x1, y1, x2, y2;

    public Edge(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

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
        if (y >= y1 && y<=y2 - 1){
            return true;
        }
        else return false;
    }

    public int getIntersection(int y) {
        // TODO: spočítáme průsečík
        return 0;
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
