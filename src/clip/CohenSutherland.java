package clip;

public class CohenSutherland {
    private static final int INSIDE = 0;
    private static final int LEFT = 8;
    private static final int RIGHT = 4;
    private static final int BOTTOM = 2;
    private static final int TOP = 1;

    private final double xMin, yMin, xMax, yMax;

    public CohenSutherland(double xMin, double yMin, double xMax, double yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    private int computeCode(double x, double y) {
        int code = INSIDE;
        if (x < xMin) code |= LEFT;
        if (x > xMax) code |= RIGHT;
        if (y < yMin) code |= TOP;
        if (y > yMax) code |= BOTTOM;
        return code;
    }

    public double[] clip(double x1, double y1, double x2, double y2) {
        while (true) {
            int code1 = computeCode(x1, y1);
            int code2 = computeCode(x2, y2);

            if ((code1 | code2) == 0) {
                return new double[]{x1, y1, x2, y2};
            }
            if ((code1 & code2) != 0) {
                return null;
            }

            int codeOut = (code1 != INSIDE) ? code1 : code2;
            double x, y;

            if ((codeOut & LEFT) != 0) {
                x = xMin;
                y = y1 + (y2 - y1) * (xMin - x1) / (x2 - x1);
            } else if ((codeOut & RIGHT) != 0) {
                x = xMax;
                y = y1 + (y2 - y1) * (xMax - x1) / (x2 - x1);
            } else if ((codeOut & TOP) != 0) {
                x = x1 + (x2 - x1) * (yMin - y1) / (y2 - y1);
                y = yMin;
            } else {
                x = x1 + (x2 - x1) * (yMax - y1) / (y2 - y1);
                y = yMax;
            }

            if (codeOut == code1) {
                x1 = x;
                y1 = y;
            } else {
                x2 = x;
                y2 = y;
            }
        }
    }
}
