package shapes.Tetrominoes;

import shapes.Grid;
import shapes.Point;
import shapes.Tile;
import utils.Logger;
import utils.MessageLevel;

import java.awt.*;


/**
 * Base class for all Tetrominoes. This class provides all the logic for
 * navigating Tetrominoes around inside of a grid. Before any objects can
 * be created, this class must be instantiated with a Grid object. ALl
 * subclasses need only to overwrite the createTileMaps() method which
 * defines the positioning of their tiles. They also need to supply their
 * own color for the constructor, so that this Tetromino can initialize
 * everything with the correct color appearance.
 * Created by David Kramer on 2/6/2016.
 */
public abstract class Tetromino {

    /**
     * Orientation constants for accessing the different tile maps
     * array. These will be used for when rotations occur. There
     * are 4 possible choices: North, East, South, and West.
     */
    public static final int ORIENTATION_NORTH   = 0;
    public static final int ORIENTATION_EAST    = 1;
    public static final int ORIENTATION_SOUTH   = 2;
    public static final int ORIENTATION_WEST    = 3;

    protected static boolean initialized = false;
    protected static Grid grid;

    protected Color color;
    protected int curOrientation;

    /**
     * To be defined by all subclasses, that define the makeup of Tetromino tiles.
     */
    protected TileMap northTiles;
    protected TileMap eastTiles;
    protected TileMap southTiles;
    protected TileMap westTiles;

    protected Tile pivotTile;       // for determining positioning of all other activeTiles, in relation to grid
    protected Tile[] activeTiles;   // currently active tiles that the tetromino is made up of
    protected Tile[] tempTiles;     // placeholder activeTiles for checking movement / rotations



    public Tetromino() {}   // default constructor

    public Tetromino(Color color) {
        init(color);
    }

    /**
     * Method to be implemented by all subclasses that creates all the
     * TileMaps that make up a Tetromino. At most, a Tetromino can have
     * 4 different orientations, and therefore each TileMap will occupy
     * different cells differently. Subclasses need to define the structure
     * of their respective Tetromino.
     */
    public abstract void createTileMaps();

    /**
     * Initializes tetrominoes with access to the grid so that various movement
     * operations can take place.
     * @param _grid - the grid that all tetrominoes will belong too
     * @return true if initialized successfully, false otherwise
     */
    public static boolean init(Grid _grid) {
        if (_grid != null) {
            grid = _grid;
            initialized = true;
        }
        return initialized;
    }

    /**
     * Utility method for cloning all tiles into a new tiles array
     * @param tilesToClone - array containing tiles to clone
     * @return new array, with cloned tiles
     */
    public static Tile[] cloneTiles(Tile[] tilesToClone) {
        Tile[] clonedTiles = new Tile[tilesToClone.length];
        for (int i = 0; i < tilesToClone.length; i++) {
            clonedTiles[i] = tilesToClone[i].clone();
        }
        return clonedTiles;
    }

    /**
     * Utility method for checking all tiles in array, down 1 row.
     * @param tilesToCheck - array of tiles to check
     * @return true if tilesToCheck fits, down 1 row
     */
    public static boolean checkFitDown(Tile[] tilesToCheck) {
        return checkFit(tilesToCheck, 0, 1);
    }

    /**
     * Utility method for moving all tiles in array, down 1 row.
     * @param tilesToMove - array of tiles to move
     */
    public static Tile[] moveTilesDown(Tile[] tilesToMove) {
        for (Tile t : tilesToMove) {
            if (t != null) {
                t.setRow(t.getRow() + 1);
                if (t.getRow() >= Grid.ROW_COUNT) {
                    t.setRow(Grid.ROW_COUNT);
                    t.setVisible(false);
                }
            } else {
                break;
            }
        }
        return tilesToMove;
    }

    /**
     * Initializes this tetromino shape with a specified color.
     * @param color - Color to assign to this Tetromino for drawing
     */
    private void init(Color color) {
        if (initialized) {
            this.color = color;
            createTileMaps();
            createTileArrays();
            curOrientation = ORIENTATION_NORTH;
        } else {
            Logger.log(MessageLevel.FATAL_ERROR, "Call Tetromino.init() first before constructing any instances!");
        }
    }

    /**
     * Creates the active and temp tile arrays to hold the activeTiles
     * that are actively seen.
     */
    private void createTileArrays() {
        activeTiles = new Tile[TileMap.TILE_COUNT];
        tempTiles = new Tile[TileMap.TILE_COUNT];

        for (int i = 0; i < TileMap.TILE_COUNT - 1; i++) {
            Tile activeTile = new Tile(getColor(), false);
            addToArrays(i, activeTile, activeTile.clone());
        }
        pivotTile = new Tile(getColor(), true);
        addToArrays(TileMap.TILE_COUNT - 1, pivotTile, pivotTile.clone());
        Logger.log(MessageLevel.INFO, "%s tile map arrays created!", getClass().getName());
    }

    /**
     * Convenience method for adding an active tile to the active tiles
     * array, and a temp tile to the temp tiles array
     * @param index - index to add tiles to
     * @param activeTile - active tile to add to active tiles array
     * @param tempTile - temp tile to add to temp tiles array
     */
    private void addToArrays(int index, Tile activeTile, Tile tempTile) {
        if (index < activeTiles.length) {
            activeTiles[index] = activeTile;
            tempTiles[index] = tempTile;
        }
    }

    /**
     * Updates the active tiles with the current orientation
     * @return tile array, with correctly positioned activeTiles in
     * relation to the grid.
     */
    public Tile[] updateTiles() {
        return updateTiles(activeTiles, curOrientation);
    }

    /**
     * Updates activeTiles array based on the specified orientation tile array.
     * @param tiles - Tiles array to update
     * @param orientation - Orientation to update to
     * @return tile array, with correctly positioned activeTiles in relation
     * to the grid.
     */
    private Tile[] updateTiles(Tile[] tiles, int orientation) {
        TileMap tileMap = getTileMap(orientation);
        Point relPivotPt = tileMap.getPivotPt();
        int index = 0;

        for (int col = 0; col < TileMap.TILE_COUNT; col++) {
            for (int row = 0; row < TileMap.TILE_COUNT; row++) {
                if (tileMap.get(col, row) == Tile.REGULAR) {
                    if (index < tiles.length) {
                        Tile t = tiles[index];
                        positionTileInGrid(t, relPivotPt, pivotTile.getGridLocation(), col, row);
                        index++;
                    } else {
                        break;
                    }
                }
            }
        }
        Logger.log(MessageLevel.INFO, "Tiles updated in %s", getClass().getName());
        return tiles;
    }

    /**
     *
     * @return the tile map array that is based on the
     * current orientation.
     */
    public TileMap getTileMap(int orientation) {
        switch (orientation) {
            case ORIENTATION_NORTH:
                return northTiles;
            case ORIENTATION_EAST:
                return eastTiles;
            case ORIENTATION_SOUTH:
                return southTiles;
            case ORIENTATION_WEST:
                return westTiles;
            default:
                String error = "Invalid orientation value. Must be one of the " +
                                "defined constants in Tetromino!";
                Logger.log(MessageLevel.FATAL_ERROR, error);
                throw new IllegalArgumentException(error);
        }
    }

    /**
     * Calculates a tile's positioning in the grid, based on its relation to other
     * activeTiles in this Tetromino, that make up the shape
     * @param tile - Individual tile to update
     * @param relPivotPt - Tetromino's shape array pivot pt
     * @param gridPivotPt - Tetromino's actual active pivot pt in grid
     * @param col - relative col
     * @param row - relative row
     */
    private void positionTileInGrid(Tile tile, Point relPivotPt, Point gridPivotPt, int col, int row) {
        int colDX = relPivotPt.getCol() - col;
        int rowDX = relPivotPt.getRow() - row;
        tile.setGridLocation((gridPivotPt.getCol() + colDX),
                             (gridPivotPt.getRow() + rowDX));
    }

    /**
     * Moves tetromino left.
     * @return true if move was successful
     */
    public boolean moveLeft() {
        return move(-1, 0);
    }

    /**
     * Moves tetromino right.
     * @return true if move was successful
     */
    public boolean moveRight() {
        return move(1, 0);
    }

    /**
     * Moves tetromino down
     * @return true if move was successful
     */
    public boolean moveDown() {
        return move(0, 1);
    }

    /**
     * Moves tetromino up
     * @return true if move was successful
     */
    public boolean moveUp() {
        return move(0, -1);
    }

    /**
     * Moves this Tetromino specified delta values on cols and rows
     * @param colDX - cols to move, relative to current pos
     * @param rowDX - rows to move, relative to current pos
     * @return
     */
    private boolean move(int colDX, int rowDX) {
        boolean success = false;
        if (checkFit(colDX, rowDX)) {
            for (Tile t : activeTiles) {
                t.setGridLocation(t.getCol() + colDX, t.getRow() + rowDX);
            }
            grid.highlightTiles();
            success = true;
        }
        return success;
    }

    /**
     * Checks to see if this Tetromino will fit with specified delta
     * movement values
     * @param colDX - cols to move, relative to current pos
     * @param rowDX - rows to move, relative to current pos
     * @return
     */
    public boolean checkFit(int colDX, int rowDX) {
        return checkFit(activeTiles, colDX, rowDX);
    }

    /**
     * Checks the fit of this Tetromino, moving down 1
     * @return - true if it fits, false otherwise
     */
    public boolean checkFitDown() {
        return checkFit(0, 1);
    }

    /**
     * Rotates this tetromino. If the orientation is exceeded, it is reset
     * back to the starting orientation,
     * @return true if rotation was successful, false otherwise
     */
    public boolean rotate() {
        if (++curOrientation > ORIENTATION_WEST) {
            curOrientation = ORIENTATION_NORTH;  // we've rotated all directions, need to reset
        }
        return rotate(curOrientation);
    }

    /**
     * Rotates to the specified orientation
     * @param orientation - orientation to rotate too
     * @return true if rotation was successful, false otherwise
     */
    private boolean rotate(int orientation) {
        boolean success = false;
        curOrientation = orientation;
        if (checkRotation(orientation)) {
            grid.highlightTiles();
            success = true;
        }
        return success;
    }

    /**
     * Checks the rotation at the specified orientation
     * @param orientation - orientation to potentially rotate to
     * @return true if it was successful, false otherwise
     */
    private boolean checkRotation(int orientation) {
        updateTiles(tempTiles, orientation);
        if (checkFit(tempTiles)) {
            updateTiles();
        }
        return true;
    }

    /**
     * Checks all the activeTiles to see if they fit within the grid, based on
     * the specified colDX and rowDX movements
     * @param tiles activeTiles array to check
     * @param colDX movement diff on cols
     * @param rowDX movement diff on rows
     * @return true if ALL activeTiles fit, false otherwise
     */
    public static boolean checkFit(Tile[] tiles, int colDX, int rowDX) {
        boolean doesFit = true;
        for (Tile t : tiles) {
            // create new temporary pt, with movement DX applied, to see if it fits within grid
            Point checkPt = new Point(t.getCol() + colDX, t.getRow() + rowDX);
            if (!grid.checkTileFit(checkPt)) {
                doesFit = false;
                break;
            }
        }
        return doesFit;
    }

    /**
     * Checks fit of specified tiles
     * @param tiles - tiles array to check
     * @return - true if tiles fit, false otherwise
     */
    private boolean checkFit(Tile[] tiles) {
        return checkFit(tiles, 0, 0);
    }

    /**
     * Draws this tetromino to the screen.
     * @param g2d - graphics context to draw to
     */
    public void render(Graphics2D g2d) {
        for (Tile t : activeTiles) {
            t.render(g2d);
        }
        pivotTile.render(g2d);
    }

    /**
     * Sets the pivot grid location of the PivotTile that makes up
     * this Tetromino
     * @param pt - grid location for the pivot pt
     */
    public void setPivotGridLocation(Point pt) {
        pivotTile.setGridLocation(pt);
    }

    /**
     * @return the color of this Tetromino
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the active tiles of this Tetromino
     */
    public Tile[] getActiveTiles() {
        return activeTiles;
    }

    /**
     * @return the pivot tile of this Tetromino
     */
    public Tile getPivotTile() {
        return pivotTile;
    }

}
