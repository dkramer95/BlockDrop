package utils;

/**
 * Collection of predefined constants for various types of speeds.
 * These speeds are used by components that interact with the
 * PlayState of the application.
 * Created by David Kramer on 2/7/2016.
 */
public final class Speeds {

    /**
     * Drop-speed constants. These speeds are delays in milliseconds and
     * useful for the Spawner. Such that, depending on the speed set,
     * the falling Tetromino will drop ONE level.
     */
    private static final int LEVEL_0     = 1500;
    private static final int LEVEL_1     = 1250;
    private static final int LEVEL_2     = 950;
    private static final int LEVEL_3     = 750;
    private static final int LEVEL_4     = 670;
    private static final int LEVEL_5     = 500;
    private static final int LEVEL_6     = 450;
    private static final int LEVEL_7     = 300;
    private static final int LEVEL_8     = 275;
    private static final int LEVEL_9     = 200;

    // access speeds through the array via an index. This makes it easier
    // to setup in the IntroState, as all the level choices follow the
    // same array design.. [0 - 9]
    public static final int[] LEVELS = {
            LEVEL_0, LEVEL_1, LEVEL_2, LEVEL_3,
            LEVEL_4, LEVEL_5, LEVEL_6, LEVEL_7,
            LEVEL_8, LEVEL_9,
    };

    /**
     * Animator and Spawner speed constants
     */
    public static final int CLEAR       = 250;
    public static final int SPAWN_WAIT  = 250;
    public static final int FINAL_MOVE  = 750;
    public static final int DROP        = 40;


    private Speeds() {} // prevent instantiation

}
