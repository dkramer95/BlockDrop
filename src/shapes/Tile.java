package shapes;

import java.awt.*;

/**
 * This class is an individual square tile that makes up a Tetromino shape.
 * Created by David Kramer on 1/19/2016.
 */
public class Tile extends Rectangle implements Cloneable {
    protected static boolean SHOW_DEBUG = false;    // global flag for showing debug info
    /**
     * Tile types
     */
    public static final int EMPTY   = 0;    // for tile maps that don't have any REGULAR or PIVOT tile occupation
    public static final int REGULAR = 1;    // for regular activeTiles (that aren't pivot activeTiles)
    public static final int PIVOT   = 2;    // for activeTiles that are pivot activeTiles
    /**
     * Default positioning and sizing values
     */
    private static final int DEFAULT_WIDTH      = 30;
    private static final int DEFAULT_HEIGHT     = 30;
    private static final int DEFAULT_X_OFFSET   = 40;
    private static final int DEFAULT_Y_OFFSET   = 40;
    /**
     * Scaled values shared across ALL Tiles.
     */
    public static int WIDTH     = DEFAULT_WIDTH;
    public static int HEIGHT    = DEFAULT_HEIGHT;
    public static int X_OFFSET  = DEFAULT_X_OFFSET;
    public static int Y_OFFSET  = DEFAULT_Y_OFFSET;

    protected Point location;   // col, row position of this tile
    protected Color color;
    protected boolean isVisible;
    private boolean isPivot;    // is this tile the pivot tile for the overall shape?



    public Tile() {}    // default constructor

    public Tile(Color color, boolean pivot) {
        init(color, pivot);
    }

    /**
     * Sets the global scale for all tiles, so that they are sized correctly
     * @param scaleFactor - ScaleFactor to apply
     */
    public static void setGlobalScale(float scaleFactor) {
        if (scaleFactor > 0.5f) {
            WIDTH = (int)(DEFAULT_WIDTH * scaleFactor);
            HEIGHT = (int)(DEFAULT_HEIGHT * scaleFactor);
        }
    }

    /**
     * Sets the global offsets for all tiles, so that they are positioned correctly
     * @param xOffset - The XOffset
     * @param yOffset - The YOffset
     */
    public static void setGlobalOffsets(int xOffset, int yOffset) {
        X_OFFSET = xOffset;
        Y_OFFSET = yOffset;
    }

    /**
     * Toggles the debug flag for all Tiles, to show useful information
     * about the tile.
     */
    public static void toggleDebug() {
        if (SHOW_DEBUG) {
            SHOW_DEBUG = false;
        } else {
            SHOW_DEBUG = true;
        }
    }

    /**
     * @return status of the SHOW_DEBUG flag
     */
    public static boolean isShowDebug() {
        return SHOW_DEBUG;
    }

    /**
     * Initializes this Tile
     * @param color - Color
     * @param pivot - Pivot location
     */
    private void init(Color color, boolean pivot) {
        this.color = color;
        this.isPivot = pivot;
        location = new Point();
        isVisible = true;
    }

    /**
     * Renders this tile. If SHOW_DEBUG is true, the debug info is drawn.
     * @param g2d
     */
    public void render(Graphics2D g2d) {
        if (isVisible) {
            drawFilled(g2d);
            drawOutlined(g2d);
        }
        if (SHOW_DEBUG) {
            drawDebug(g2d);
        }
    }

    /**
     * Draws debug info about this tile
     * @param g2d
     */
    protected void drawDebug(Graphics2D g2d) {
        if (isPivot) {
            g2d.drawString("P", getDrawX() + 10, getDrawY() + 18);
        }
    }

    /**
     * Draws the tile filled with its color
     * @param g2d
     */
    protected void drawFilled(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fill3DRect(getDrawX(), getDrawY(), WIDTH, HEIGHT, true);
    }

    /**
     * Draws this Tile with the line outline
     * @param g2d
     */
    protected void drawOutlined(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.drawRect(getDrawX(), getDrawY(), WIDTH, HEIGHT);
    }

    /**
     * @return the x location for drawing the tile correctly
     */
    protected int getDrawX() {
        return getCol() * WIDTH + X_OFFSET;
    }

    /**
     * @return the y location for drawing the tile correctly
     */
    protected int getDrawY() {
        return getRow() * HEIGHT + Y_OFFSET;
    }

    public int getCol() {
        return location.getCol();
    }

    public void setCol(int col) {
        location.setCol(col);
    }

    public int getRow() {
        return location.getRow();
    }

    public void setRow(int row) {
        location.setRow(row);
    }

    public void setGridLocation(int col, int row) {
        location.setCol(col);
        location.setRow(row);
    }

    public Point getGridLocation() {
        return location;
    }

    public void setGridLocation(Point p) {
        this.location = p;
    }

    public boolean isPivot() {
        return isPivot;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Tile clone() {
        Tile tileClone = new Tile(getColor(), isPivot());
        tileClone.setGridLocation(new Point(getCol(), getRow()));
        return tileClone;
    }

    public String toString() {
        return "Tile -> isPivot: " + isPivot + ", " + location;
    }

}
