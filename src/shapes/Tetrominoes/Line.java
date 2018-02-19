package shapes.Tetrominoes;

import java.awt.*;

/**
 * Line-Shaped implementation of the Tetromino class.
 * Created by David Kramer on 2/6/2016.
 */
public final class Line extends Tetromino {

    public Line() {
        super(Color.decode("#2AB1B5")); // cyan
    }

    public void createTileMaps() {
        northTiles = TileMap.fromArray(new int[][] {
                {0, 0, 1, 0,},
                {0, 0, 2, 0,},
                {0, 0, 1, 0,},
                {0, 0, 1, 0,},
        });
        eastTiles = TileMap.fromArray(new int[][] {
                {0, 0, 0, 0,},
                {1, 1, 2, 1,},
                {0, 0, 0, 0,},
                {0, 0, 0, 0,},
        });
        southTiles = TileMap.fromArray(new int[][] {
                {0, 0, 1, 0,},
                {0, 0, 1, 0,},
                {0, 0, 2, 0,},
                {0, 0, 1, 0,},
        });
        westTiles = TileMap.fromArray(new int[][] {
                {0, 0, 0, 0,},
                {1, 2, 1, 1,},
                {0, 0, 0, 0,},
                {0, 0, 0, 0,},
        });
    }
}
