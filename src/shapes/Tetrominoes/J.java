package shapes.Tetrominoes;

import java.awt.*;

/**
 * J-Shaped implementation of the Tetromino class.
 * Created by David Kramer on 2/7/2016.
 */
public final class J extends Tetromino {

    public J() {
        super(Color.decode("#AF7AC4")); // purple / magenta
    }

    public void createTileMaps() {
        northTiles = TileMap.fromArray(new int[][] {
                {0, 1, 0, 0,},
                {0, 1, 0, 0,},
                {1, 2, 0, 0,},
                {0, 0, 0, 0,},
        });
        eastTiles = TileMap.fromArray(new int[][] {
                {0, 0, 0, 0,},
                {1, 1, 2, 0,},
                {0, 0, 1, 0,},
                {0, 0, 0, 0,},
        });
        southTiles = TileMap.fromArray(new int[][] {
                {0, 2, 1, 0,},
                {0, 1, 0, 0,},
                {0, 1, 0, 0,},
                {0, 0, 0, 0,},
        });
        westTiles = TileMap.fromArray(new int[][] {
                {1, 0, 0, 0,},
                {2, 1, 1, 0,},
                {0, 0, 0, 0,},
                {0, 0, 0, 0,},
        });
    }
}
