package shapes.Tetrominoes;


/**
 * This class encapsulates a collection of constant field values
 * for the various possible types of tetromino shapes.
 * Created by David Kramer on 1/28/2016.
 */
public final class Tetrominoes {
    public static final int MAX_COUNT = 7; // possible tetrominoes

    /**
     * Tetromino shape type constants.
     */
    public static final int J      = 1;
    public static final int L      = 2;
    public static final int T      = 3;
    public static final int Z      = 4;
    public static final int S      = 5;
    public static final int LINE   = 6;
    public static final int SQUARE = 7;

    private Tetrominoes() {}  // prevent instantiation
}
