package shapes.Tetrominoes;

import shapes.Point;
import shapes.Tile;
import utils.MapValidator;

/**
 * Created by David Kramer on 2/7/2016.
 */
public abstract class TileMap {
    public static final int TILE_COUNT = 4; // no Tetromino can have more than this

    protected int[][] tileMap;
    protected Point pivotPt;




    private TileMap() {     // don't instantiate directly, instead use fromArray method
        tileMap = createTileMap();
        setPivotPt(findRelPivot(tileMap));
    }

    protected abstract int[][] createTileMap();

    /**
     * Utility method to allow for this class to be created cleanly, by simply
     * a 2D int array, layed out with the tile map positions. This will
     * define an instance of this TileMap class and figure out the pivot pt,
     * in relation to the 2D int array.
     * @param arr
     * @return
     */
    public static TileMap fromArray(int[][] arr) {
        TileMap map = new TileMap() {
            protected int[][] createTileMap() {
                if (MapValidator.validate(arr)) {
                    this.tileMap = arr;
                    return tileMap;
                }
                return null;
            }
        };
        return map;
    }

    /**
     * Utility method for finding where the pivot tile is in an
     * int[][] array.
     * @param arr - tile map array
     * @return point containing row and col of pivot pt
     */
    private static Point findRelPivot(int[][] arr) {
        for (int col = 0; col < TILE_COUNT; col++) {
            for (int row = 0; row < TILE_COUNT; row++) {
                if (arr[col][row] == Tile.PIVOT) {
                    return new Point(col, row);
                }
            }
        }
        return null;
    }

    /**
     * Sets the pivot pt location in the tile map
     * @param pivotPt
     */
    protected void setPivotPt(Point pivotPt) {
        this.pivotPt = pivotPt;
    }

    /**
     * @return the pivot pt location in the map
     */
    public Point getPivotPt() {
        return pivotPt;
    }

    /**
     * The integer TileType, in the backing array.
     * @param col - column value
     * @param row - row value
     * @return an integer value that should be one of the
     * defined constants in the Tiles class.
     */
    public int get(int col, int row) {
        if (check(col, row)) {
            return tileMap[col][row];
        }
        return -1;
    }

    /**
     * Checks to see if row and col are within the limits
     * @param col - col to check
     * @param row - row to check
     * @return true if both col and row are within the limits
     */
    private boolean check(int col, int row) {
        return (col >= 0 && col < TILE_COUNT) && (row >= 0 && row < TILE_COUNT);
    }
}

