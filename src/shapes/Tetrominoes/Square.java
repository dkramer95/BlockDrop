package shapes.Tetrominoes;

import java.awt.*;

/**
 * Square-Shaped implementation of the Tetromino class.
 * Created by David Kramer on 2/7/2016.
 */
public final class Square extends Tetromino {

    public Square() {
        super(Color.decode("#F1C40F")); // yellow
    }

    public void createTileMaps() {
        northTiles = TileMap.fromArray(new int[][] {
                {0, 1, 2, 0,},
                {0, 1, 1, 0,},
                {0, 0, 0, 0,},
                {0, 0, 0, 0,},
        });

        // square doesn't rotate
        eastTiles = northTiles;
        southTiles = northTiles;
        westTiles = northTiles;
    }
}
