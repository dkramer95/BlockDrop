package shapes.Tetrominoes;

import java.awt.*;

/**
 * S-Shaped implementation of the Tetromino class.
 * Created by David Kramer on 2/7/2016.
 */
public final class S extends Tetromino {

    public S() {
        super(Color.decode("#E74C3C"));  // red
    }

    public void createTileMaps() {
        northTiles = TileMap.fromArray(new int[][] {
                {1, 0, 0, 0,},
                {1, 2, 0, 0,},
                {0, 1, 0, 0,},
                {0, 0, 0, 0,},
        });
        eastTiles = TileMap.fromArray(new int[][] {
                {0, 0, 0, 0,},
                {0, 2, 1, 0,},
                {1, 1, 0, 0,},
                {0, 0, 0, 0,},
        });

        southTiles = northTiles;
        westTiles = eastTiles;
    }
}
