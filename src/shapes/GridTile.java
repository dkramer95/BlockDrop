package shapes;

import java.awt.*;

/**
 * This class represents a single tile that makes up a grid.
 * A Grid tile can be occupied partially by another Tetromino.
 * GridTiles can also be highlighted partially, to show a preview
 * of where a Tetromino's tile can occupy.
 * Created by David Kramer on 2/6/2016.
 */
public class GridTile extends Tile {
    private static final Color HIGHLIGHT_COLOR = Color.decode("#212121");

    private Tile partialShapeTile;  // tile that belongs to part of a tetromino
    private boolean isFilled;       // does this grid tile contain the partial tile?
    private boolean isHighlighted;  // should this tile, be drawn with a highlight?


    /**
     * Constructs a new GridTile at the specified column and row location
     * @param col - GridLocation on the columns
     * @param row - GridLocation on the rows
     */
    public GridTile(int col, int row) {
        setBounds(col * WIDTH, row * HEIGHT, WIDTH, HEIGHT);
        location = new Point(col, row);
        isFilled = false;
        color = Color.BLACK;    // this is intentional, as it will be changed later in PlayState animateGrid()
    }

    /**
     * Renders this individual tile to the screen.
     * @param g2d - Graphics context to draw to
     */
    public void render(Graphics2D g2d) {
        if (isHighlighted && !isFilled()) {
            drawHighlighted(g2d);
        }
        drawOutlined(g2d);
        if (isFilled) {
            drawPartial(g2d);
        }
        if (SHOW_DEBUG) {
            drawDebug(g2d);
        }
    }

    /**
     * Draws useful debug information about this grid tile, inside of
     * the tile itself, such as the numerical value and whether or
     * not it is filled, denoted by an asterisk.
     * @param g2d
     */
    protected void drawDebug(Graphics2D g2d) {
        String s = "" + getNum();
        if (isFilled) {
            s += "*";
        }
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString(s, getDrawX() + 5, getDrawY() + 20);
    }

    /**
     * @return the grid tile number based on its col and row.
     */
    protected int getNum() {
        return (Grid.COL_COUNT * getRow()) + getCol();
    }

    /**
     * Draws this GridTile with the line outline
     * @param g2d
     */
    protected void drawOutlined(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.drawRect(getDrawX(), getDrawY(), WIDTH, HEIGHT);
    }

    /**
     * Draws the partial tetromino tile, if it exists!
     * @param g2d
     */
    protected void drawPartial(Graphics2D g2d) {
        if (partialShapeTile != null && isFilled) {
            partialShapeTile.render(g2d);
        }
    }

    /**
     * Draws the highlight of this grid tile.
     * @param g2d
     */
    protected void drawHighlighted(Graphics2D g2d) {
        g2d.setColor(HIGHLIGHT_COLOR);
        g2d.fillRect(getDrawX(), getDrawY(), WIDTH, HEIGHT);
    }

    /**
     * Clears out the partial shape tile of this GridTile.
     */
    public void clear() {
        partialShapeTile = null;
        isFilled = false;
    }

    /**
     * @return the partial tetromino tile that is contained
     * within this GridTile
     */
    public Tile getPartialShapeTile() {
        return partialShapeTile;
    }

    /**
     * Sets the partial Tile that belongs to a Tetromino, that
     * occupies this GridTile.
     * @param t - The partial tile of a Tetromino that will
     *          occupy this GridTile
     */
    public void setPartialShapeTile(Tile t) {
        if (t != null) {
            this.partialShapeTile = t;
            t.setGridLocation(getGridLocation());
            isFilled = true;
        } else {
            isFilled = false;
        }
    }

    /**
     * @return true if this GridTile is filled, false otherwise
     */
    public boolean isFilled() {
        return isFilled;
    }

    /**
     * @return the highlight flag of this GridTile
     */
    public boolean isHighlighted() {
        return isHighlighted;
    }

    /**
     * Updates the highlight status of this GridTile
     * @param isHighlighted - should this gridTile be highlighted
     */
    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }
}
