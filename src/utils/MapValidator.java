package utils;

import shapes.Point;
import shapes.Tetrominoes.TileMap;
import shapes.Tile;

/**
 * Utility class that validates the structural integrity of tile map
 * arrays that define the shape makeup of a Tetromino. Tile maps for
 * tetrominoes have rules to follow, and class provides enforcement.
 * Created by David Kramer on 2/7/2016.
 */
public class MapValidator {

    /**
     * There can only be ONE pivot tile, and a maximum of
     * THREE regular tiles to make a total of FOUR pivot tiles. If any
     * of these rules are violated, this method will display information
     * so that it can be corrected.
     * @param arr - array to check
     * @return true if valid, false otherwise
     */
    public static boolean validate(int[][] arr) {
        int regCount = 0;   // number of regular tiles
        int pivCount = 0;   // number of pivot tiles
        int badCount = 0;   // number of bad tiles
        boolean success = true;

        // check each tile in the array, and make sure the rules are followed
        // if rules are violated, a "BadTiles" is added!
        for (int col = 0; col < TileMap.TILE_COUNT; col++) {
            for (int row = 0; row < TileMap.TILE_COUNT; row++) {
                if (arr[col][row] == Tile.REGULAR) {
                    regCount++;
                    if (regCount > BadTiles.MAX_REG) {
                        BadTiles.add(badCount++, BadTiles.TYPE_EXCESS_TILE, col, row);
                    }
                } else if (arr[col][row] == Tile.PIVOT) {
                    pivCount++;
                    if (pivCount > BadTiles.MAX_PIV) {
                        BadTiles.add(badCount++, BadTiles.TYPE_MULTI_PIVOT, col, row);
                    }
                } else if (arr[col][row] != Tile.EMPTY) {
                    BadTiles.add(badCount++, BadTiles.TYPE_INVALID_TYPE, col, row);
                }
            }
        }

        if (badCount > 0) {
            BadTiles.displayInfo();
            success = false;
        }
        return success;
    }
}

/**
 * Utility class that contains helpful methods for storing information
 * about invalid tiles, from an int[][] map array.
 */
class BadTiles {
    static final int MAX_REG = 3;
    static final int MAX_PIV = 1;

    static final String TYPE_MULTI_PIVOT    = "Multiple Pivot Tile";
    static final String TYPE_EXCESS_TILE    = "Excess Tile";
    static final String TYPE_INVALID_TYPE   = "Invalid Tile Type";

    static BadTiles[] tiles = new BadTiles[TileMap.TILE_COUNT * TileMap.TILE_COUNT];

    String type;
    Point ptIndex;

    static void add(int index, String type, int col, int row) {
        tiles[index] = new BadTiles(type, col, row);
    }

    // private constructors --> use add method instead
    private BadTiles(String type, Point ptIndex) {
        this.type = type;
        this.ptIndex = ptIndex;
    }

    private BadTiles(String type, int col, int row) {
        this.type = type;
        this.ptIndex = new Point(col, row);
    }

    /**
     * Displays all information stored in the BadTiles array.
     */
    static void displayInfo() {
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] != null) {
                System.out.println(getInfo(i));
            } else {
                break;
            }
        }
    }

    /**
     * Displays info about a tile in the BadTiles[] array at the specified index.
     * @param index - index location
     * @return String containing information about the pt.
     */
    public static String getInfo(int index) {
        String type = tiles[index].getType();
        Point pt = tiles[index].getPtIndex();
        String info = String.format("Bad Tile #%d --> Type: %s at [%d, %d] in map array",
                index, type, pt.getCol(), pt.getRow());
        return info;
    }

    String getType() {
        return type;
    }

    Point getPtIndex() {
        return ptIndex;
    }
}
