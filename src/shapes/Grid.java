package shapes;

import shapes.Tetrominoes.Tetromino;
import shapes.Tetrominoes.TileMap;
import utils.GameDisplay;
import utils.Logger;
import utils.MessageLevel;
import utils.TimerUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * This class models a Grid playing field for various Tetrominoes. A tetromino
 * can be added to this grid and moved around. A grid is made up of an array
 * of GridTiles organized intro columns and rows. Tetrominoes can move and rotate
 * to any col/row such that they do not exceed the boundaries of the grid or overlap
 * any other existing Tetrominoes. There is only ever one active falling Tetromino
 * that can be controlled by the user. After a Tetromino has been "dropped", this
 * class can check to see a line clear can occur, which is only possible if every
 * consecutive column in a row is filled with part of a Tetromino. If this condition
 * is met, those tiles are cleared, and other Tetrominoes are shifted down.
 * Created by David Kramer on 2/6/2016.
 */
public class Grid {
    public static final int COL_COUNT = 10;
    public static final int ROW_COUNT = 16;

    private GridTile[] gridTiles;
    private GridTile[] highlightedTiles;    // tiles that are currently highlighted
    private Tile[] shiftTiles;              // tiles that need to be shifted down
    private Tetromino fallingTetromino;     // tetromino that is currently dropping
    private int filledTileCount = 0;            // how many of the grid tiles are filled



    /**
     * Constructs a new create and initializes it.
     */
    public Grid() {
        init();
    }

    /**
     * @return the width of the grid, taking into account the size of
     * all tiles.
     */
    public static int getWidth() {
        return (Tile.WIDTH + Tile.X_OFFSET) * Grid.COL_COUNT;
    }

    /**
     * @return the height of the grid, taking into account the size of
     * all tiles.
     */
    public static int getHeight() {
        return (Tile.HEIGHT + Tile.Y_OFFSET) * Grid.ROW_COUNT;
    }

    /**
     * Initializes the grid.
     */
    private void init() {
        createGrid();
    }

    /**
     * Creates the grid by adding each individual grid tile to the array.
     */
    private void createGrid() {
        gridTiles = new GridTile[COL_COUNT * ROW_COUNT];
        highlightedTiles = new GridTile[TileMap.TILE_COUNT];
        int index = 0;
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                GridTile t = new GridTile(col, row);
                gridTiles[index++] = t;
            }
        }
        Logger.log(MessageLevel.INFO, "(%d x %d) Grid Created", COL_COUNT, ROW_COUNT);
    }

    /**
     * Clears out the grid from any previous tetrominoes, typically
     * after a GameOver!
     */
    public void clear() {
        clearHighlights();
        for (GridTile t : gridTiles) {
            t.setPartialShapeTile(null);
        }
        filledTileCount = 0;
    }

    /**
     * Utility method that prints a text-based grid that shows which cols
     * and rows are filled, denoted by an "F".
     */
    public void printGrid() {
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                String gridTile = " ";

                if (get(col, row).isFilled()) {
                    gridTile = "F";
                }
                System.out.print("[" + gridTile + "]");
            }
            System.out.println();
        }
    }

    /**
     *
     * @param pt - (col, row) pt in grid
     * @return grid tile at specified pt
     */
    public GridTile get(Point pt) {
        return get(pt.getCol(), pt.getRow());
    }

    /**
     *
     * @param col - col of grid tile
     * @param row - row of grid tile
     * @return grid tile at specified row and col
     */
    public GridTile get(int col, int row) {
        return gridTiles[getIndex(col, row)];
    }

    /**
     *
     * @param col - column to access
     * @param row - row to access
     * @return the correct index for the specified row and column
     */
    private int getIndex(int col, int row) {
        if (check(col, row)) {
            return (COL_COUNT * row) + col;
        }
        return -1;  // shouldn't ever actually reach this
    }

    /**
     * Checks to make sure that the col and row is valid and contained
     * within the GridTiles array
     * @param col - col to check
     * @param row - row to check
     * @return true if row and col are okay, false otherwise
     */
    private boolean check(int col, int row) {
        boolean success = false;
        String error = null;

        if (col < 0 || row < 0) {
            error = "Invalid GridTile: Col < 0 || Row < 0!";
        } else if (col >= COL_COUNT || row >= ROW_COUNT) {
            error = "Invalid GridTile: Col >= " + COL_COUNT + " || Row >= " + ROW_COUNT;
        } else {
            success = true;
        }

        if (!success) {
////            Logger.log(MessageLevel.WARNING, error);
            throw new ArrayIndexOutOfBoundsException(error);
        }
        return success;
    }

    /**
     * Checks to see if the specified pt is contained in this grid
     * @param pt - Point to check
     * @return true if contained within grid, false otherwise
     */
    private boolean check(Point pt) {
        return check(pt.getCol(), pt.getRow());
    }

    /**
     * Checks to see if we are able to clear. If we are, all the rows nums
     * are stored, so that they can be processed later, for actually clearing.
     */
    public void checkClear() {
        int[] rowsToClear = new int[ROW_COUNT]; // store row nums, so we know which ones to clear!
        int clearCount = 0;

        for (int row = 0; row < ROW_COUNT; row++) {
            boolean canClear = true;
            for (int col = 0; col < COL_COUNT; col++) {
                if (!get(col, row).isFilled()) {
                    canClear = false;
                    break;
                }
            }
            if (canClear) {
                rowsToClear[clearCount] = row;
                clearCount++;
            }
        }
        if (clearCount > 0) {
            clearRows(rowsToClear, clearCount);
        }
    }

    /**
     * Clears all the specified rows defined in the array, and limited
     * by the limit.
     * @param rowsToClear
     * @param limit - length limit in array, rowsToClear may contain empty
     *                numerical values, when it was created
     */
    private void clearRows(int[] rowsToClear, int limit) {
        GridTile[] tilesToClear = new GridTile[limit * COL_COUNT];
        int index = 0;
        for (int i = 0; i < limit; i++) {   // each row
            int row = rowsToClear[i];
            for (int col = 0; col < COL_COUNT; col++) {
                tilesToClear[index] = get(col, row);
                index++;
            }
        }
        TimerUtil.animateClear(tilesToClear, this);
        GameDisplay.increaseLineCount(limit);
    }

    public void updateShiftTiles() {
        TreeMap<Integer, Tile[]> tileRowMap = new TreeMap<>();  // hold all tiles we need to shift down
        for (int row = ROW_COUNT - 1; row >= 0; row--) {
            tileRowMap.put(row, getTilesForRow(row));
        }
        clearTiles();

        for (int row = ROW_COUNT - 1; row >= 0; row--) {
            moveRowDown(row, tileRowMap.get(row));
        }
    }

    private Tile[] getTilesForRow(int row) {
        Tile[] rowTiles = new Tile[COL_COUNT];
        for (int col = 0; col < COL_COUNT; col++) {
            rowTiles[col] = get(col, row).getPartialShapeTile();
        }
        return rowTiles;
    }

    private void moveRowDown(int row, Tile[] rowToMove) {
        Logger.log(MessageLevel.INFO, "STARTING ROW: %d", row);
        while (row + 1 < ROW_COUNT && isRowClear(row + 1)) {
            row++;
        }
        Logger.log(MessageLevel.INFO, "ENDING ROW: %d", row);

        for (Tile t : rowToMove) {
            if (t != null) {
                /*
                I have no idea why, but without cloning a tile, the actual
                grid tile itself, gets moved and causes issues. I have no idea
                where that bug is hiding, as I am passing around arrays of regular
                tiles, NOT grid tiles. But cloning the tile, appears to work around
                that issue just fine.
                */
                Tile newTile = t.clone();
                newTile.setRow(row);
                get(newTile.getGridLocation()).setPartialShapeTile(newTile);
            }
        }
    }

    /**
     * Checks to see if the specified row is clear.
     * @param row
     * @return
     */
    private boolean isRowClear(int row) {
        boolean isClear = true;
        for (int col = 0; col < COL_COUNT; col++) {
            if (get(col, row).isFilled()) {
                isClear = false;
                break;
            }
        }
        return isClear;
    }

    private Tile[] shiftTilesDown(Tile[] tilesToMove) {
        int length = tilesToMove.length;
        for (int i = length -1; i >= 0; i--) {
            Tile t = tilesToMove[i];
            int col = t.getCol();
            int row = t.getRow();

            while (!get(col, row).isFilled() && row < Grid.ROW_COUNT - 1) {
                row++;
            }

            if (get(col, row).isFilled()) {
                row -= 1;
            }
            get(col, row).setPartialShapeTile(t);
        }
        return tilesToMove;
    }

    //TODO remove this later!
    public void testFillGrid() {
        for (int row = ROW_COUNT - 1; row >= 10; row--) {
            for (int col = 0; col < COL_COUNT - 1; col++) {
                get(col,row).setPartialShapeTile(new Tile(Color.LIGHT_GRAY, false));
            }
        }
    }

    /**
     * Clears out all tiles in the grid by removing their partial shape tiles.
     */
    private void clearTiles() {
        for (GridTile t : gridTiles) {
            t.clear();
        }
    }

    /**
     * @return number of GridTiles that are currently filled
     */
    private int getFilledCount() {
        int count = 0;
        for (GridTile t : gridTiles) {
            if (t.getPartialShapeTile() != null) {
                count++;
            }
        }
        return count;
    }

    private boolean checkTileFitDown(Tile tileToCheck) {
        boolean doesFit = true;

        GridTile t = get(tileToCheck.getGridLocation());

        if (t.isFilled()) {
            doesFit = false;
        }
        if (tileToCheck.getRow() + 1 >= Grid.ROW_COUNT) {
            doesFit = false;
        }
        return doesFit;
    }

    /**
     * Highlights the lowest available GridTiles that the actively
     * falling Tetromino can occupy.
     */
    public void highlightTiles() {
        clearHighlights();
        // create temp tiles array for testing fitting within the grid
        Tile[] tiles = Tetromino.cloneTiles(fallingTetromino.getActiveTiles());

        while (Tetromino.checkFitDown(tiles)) {
            Tetromino.moveTilesDown(tiles);
        }
        // set grid tile highlights, based on where the tiles ended up
        for (int i = 0; i < tiles.length; i++) {
            Point pt = tiles[i].getGridLocation();
            GridTile t = get(pt);
            t.setHighlighted(true);
            highlightedTiles[i] = t;
        }
    }

    /**
     * Resets all previously highlighted gridTiles, to a non-highlighted state.
     */
    private void clearHighlights() {
        for (GridTile t : highlightedTiles) {
            if (t != null) {
                t.setHighlighted(false);
            }
        }
    }

    /**
     * Renders everything to the screen.
     * @param g2d
     */
    public void render(Graphics2D g2d) {
        drawGridTiles(g2d);
        if (fallingTetromino != null) {
            fallingTetromino.render(g2d);
        }
    }

    /**
     * Draws all the grid tiles to the screen.
     * @param g2d
     */
    private void drawGridTiles(Graphics2D g2d) {
        for (GridTile t : gridTiles) {
            t.render(g2d);
        }
    }

    /**
     * @return the currently falling tetromino
     */
    public Tetromino getFallingTetromino() {
        return fallingTetromino;
    }

    /**
     * Sets the current falling tetromino to the new one and
     * centers it within the grid.
     * @param t - New Tetromino that is falling
     */
    public void setFallingTetromino(Tetromino t) {
        this.fallingTetromino = t;
        centerTetromino(t);
        t.updateTiles();
    }

    /**
     * Centers the tetromino in the top middle of the grid
     * @param t - Tetromino to center
     */
    private void centerTetromino(Tetromino t) {
        t.setPivotGridLocation(new Point(5, -1));
    }

    /**
     * Checks to see if the tile grid pt fits
     * @param pt - Location to check
     * @return true if it fits, false otherwise
     */
    public boolean checkTileFit(Point pt) {
        boolean doesFit = true;
        try {
            if (check(pt)) {
                doesFit = !(get(pt).isFilled());
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            doesFit = false;
        }
        return doesFit;
    }

    /**
     * Checks to see if the specified tile fits in the grid
     * @param tile - Tile to check
     * @return true if it fits, false otherwise
     */
    public boolean checkTileFit(Tile tile) {
        return checkTileFit(tile.getGridLocation());
    }

    /**
     * Checks to see if the game is over. This occurs if the
     * falling tetromino can't move down and it's pivot tile is
     * at the top of the screen.
     * @return true if gameover, false otherwise
     */
    public boolean checkGameOver() {
        boolean gameOver = false;
        if (!fallingTetromino.checkFitDown()) {
            if (fallingTetromino.getPivotTile().getRow() <= 0) {
                gameOver = true;
            }
        }
        return gameOver;
    }

    /**
     * Drops the currently falling tetromino.
     */
    public void dropFalling() {
        clearHighlights();
        updateGrid();
        checkClear();
    }

    /**
     * @return all the GridTiles stored within this grid.
     */
    public GridTile[] getGridTiles() {
        return gridTiles;
    }

    /**
     * Updates the GridTiles in the grid with the last falling
     * Tetromino shape, so that wherever the Tetromino is, the
     * GridTiles are set to be filled, if they contain part of
     * the Tetromino.
     */
    private void updateGrid(Tile[] tiles) {
        for (int i = 0; i < tiles.length; i++) {
            Tile t = tiles[i];
            if (t != null) {
                get(t.getGridLocation()).setPartialShapeTile(t);
            }
        }
    }

    /**
     * Updates the grid with the current tiles from the falling shape.
     */
    private void updateGrid() {
        updateGrid(fallingTetromino.getActiveTiles());
    }
}
