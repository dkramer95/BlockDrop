package shapes;

/**
 * Basic Point class that uses (col, row) instead of (x, y). This makes
 * getting the locations of activeTiles and shapes, easier and more concise,
 * when we want to retrieve both the horizontal and vertical location
 * data.
 * Created by David Kramer on 1/26/2016.
 */
public class Point implements Comparable<Point>, Cloneable {
    private int col;
    private int row;




    public Point() {} // default empty constructor

    public Point(int col, int row) {
        this.col = col;
        this.row = row;
    }

    /**
     * Determines if 2 points are equals to each other
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point pt = (Point)obj;
            return col == pt.getCol() && row == pt.getRow();
        }
        return super.equals(obj);
    }

    /**
     * Compares this point to another point.
     * @param p - Point to compare to
     * @return
     */
    public int compareTo(Point p) {
        if (row < p.getRow()) {
            return -1;
        }
        if (row > p.getRow()) {
            return 1;
        }
        if (col < p.getCol()) {
            return -1;
        }
        if (col > p.getCol()) {
            return 1;
        }
        return 0;
    }

    public Point getLocation() {
        return new Point(col, row);
    }

    public void setLocation(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String toString() {
        return "Point [" + col + ", " + row + "]";
    }

    public Point clone() {
        Point ptClone = new Point();
        ptClone.setCol(getCol());
        ptClone.setRow(getRow());
        return ptClone;
    }
}
