package shapes.Tetrominoes;

import java.awt.*;

/**
 * L-Shaped implementation of the Tetromino class.
 * Created by David Kramer on 2/7/2016.
 */
public final class L extends Tetromino {

    public L() {
        super(Color.decode("#E66B64")); // salmon pink
    }

    public void createTileMaps() {
        northTiles = TileMap.fromArray(new int[][] {
                {1, 2, 0, 0,},
                {0, 1, 0, 0,},
                {0, 1, 0, 0,},
                {0, 0, 0, 0,},
        });
        eastTiles = TileMap.fromArray(new int[][] {
                {0, 0, 0, 0,},
                {2, 1, 1, 0,},
                {1, 0, 0, 0,},
                {0, 0, 0, 0,},
        });
        southTiles = TileMap.fromArray(new int[][] {
                {0, 1, 0, 0,},
                {0, 1, 0, 0,},
                {0, 2, 1, 0,},
                {0, 0, 0, 0,},
        });
        westTiles = TileMap.fromArray(new int[][] {
                {0, 0, 1, 0,},
                {1, 1, 2, 0,},
                {0, 0, 0, 0,},
                {0, 0, 0, 0,},
        });
    }
}
