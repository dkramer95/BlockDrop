package utils;

import managers.StateManager;
import shapes.Grid;
import shapes.Tetrominoes.*;
import states.GameOverState;
import states.IntroState;
import states.PlayState;

import javax.swing.*;
import java.security.SecureRandom;

/**
 * This class is a helper class for spawning Tetrominoes in the grid.
 * Created by David Kramer on 2/7/2016.
 */
public class Spawner extends GameUtility {
    public static final int LEVEL_UP_LIMIT = 10;   // clear threshold so we can level up

    private static int curSpeed = Speeds.LEVELS[0];   // default starting value

    private static SecureRandom rng;

    /**
     * Holds ID's of previously spawned shapes, to prevent multiple duplicate shapes from being spawned
     */
    private static int[] lastSpawned;
    private static int lastIndex;



    private Spawner() {}    // prevent instantiation

    /**
     * Initializes the Spawner with the active PlayState.
     * @param _state - Playing State
     * @param _grid - The grid
     * @return true if successful, false otherwise
     */
    public static boolean init(PlayState _state, Grid _grid) {
        if (_grid != null) {
            grid = _grid;
            rng = new SecureRandom();
            lastSpawned = new int[Tetrominoes.MAX_COUNT];
            lastIndex = 0;
        }
        return GameUtility.init(_state);
    }

    /**
     * Starts to kick off the spawning sequence.
     */
    public static void start() {
        spawn();
    }

    /**
     * Animates the drop of a tetromino.
     */
    public static void animateDrop() {
        Tetromino falling = grid.getFallingTetromino();
        resetTimer(Speeds.DROP, e -> {
            if (!falling.checkFitDown()) {
                timer.stop();
                updateFinalMove();
            } else {
                falling.moveDown();
            }
        });
    }

    private static void spawn() {
        checkReset();
        checkLevelUp();
        updateGrid(getValidChoice());
    }

    /**
     * Checks to see if the last index in the lastSpawned array
     * needs to reset and if it doesn't, the last index is set
     * to 0.
     */
    private static void checkReset() {
        if (lastIndex > lastSpawned.length - 1) {
            lastIndex = 0;
            Logger.log(MessageLevel.INFO, "Spawner index reset");
        }
    }

    /**
     * Checks to see if we should go to the next level, based on the number of
     * line clears.
     */
    private static void checkLevelUp() {
        if (GameDisplay.getLevelUpCount() >= LEVEL_UP_LIMIT) { // add 1 to work around initial zero clears
            GameDisplay.increaseLevel();
            setCurSpeed(GameDisplay.getLevelNum());
        }
    }

    /**
     * This method checks to make sure that the next type of Tetromino to
     * be spawned is not a recent duplicate choice, and therefore is valid.
     * This prevents frequently spawning the same Tetromino consecutively.
     * @return - an int value that is a type, defined in Tetrominoes class
     */
    private static int getValidChoice() {
        int choice = -1;
        boolean isDuplicate = true;

        do {
            choice = rng.nextInt(Tetrominoes.MAX_COUNT) + 1;
            isDuplicate = false;
            for (int i = 0; i < lastIndex; i++) {
                if (lastSpawned[i] == choice) {
                    isDuplicate = true;
                }
            }
        } while (isDuplicate);
        return choice;
    }

    /**
     * Updates the grid with a new recently spawned Tetromino as well
     * sa the GameDisplay drop count.
     * @param type
     */
    private static void updateGrid(int type) {
        Tetromino t = getFromInt(type);
        grid.setFallingTetromino(t);
        lastSpawned[lastIndex] = type;
        lastIndex++;
        updateFalling();
        GameDisplay.increaseBlockDrop();
    }

    /**
     * Drops the current Tetromino at the specified interval defined
     * by curSpeed;
     */
    private static void updateFalling() {
        resetTimer(curSpeed, e -> {
            boolean gameOver = grid.checkGameOver();
            if (gameOver) {
                endGame();
            } else {
                boolean canDrop = grid.getFallingTetromino().moveDown();
                if (!canDrop) {
                    updateFinalMove();
                }
            }
        });
    }

    /**
     * Shows a dialog to the user letting them know they have lost
     * the game. The app then transitions back to the starting IntroState.
     */
    private static void endGame() {
        timer.stop();
//        JOptionPane.showMessageDialog(state, "Game Over");
        StateManager.setActiveState(new GameOverState());
        Logger.log(MessageLevel.INFO, "Game Over!");
//        StateManager.setActiveState(new IntroState());
        grid.clear();
    }

    /**
     * Gives the user a brief window of time, to make any final adjustments
     * to their move on the current Tetromino, before respawning a new
     * Tetromino.
     */
    private static void updateFinalMove() {
        resetTimer(Speeds.FINAL_MOVE, e -> {
            grid.dropFalling();
            removeTimerActions();
            timer.setInitialDelay(Speeds.SPAWN_WAIT);
            timer.addActionListener(ex -> {
                spawn();
            });
            timer.start();
        });
        timer.start();
    }

    /**
     * Creates a new tetromino from a specified int value type that
     * is one of the predefined field constants in the Tetrominoes class.
     * @param type - type of tetromino
     * @return tetromino from specified numerical type
     * @throws - IllegalArgumentException
     */
    private static Tetromino getFromInt(int type) {
        switch (type) {
            case Tetrominoes.J:
                return new J();
            case Tetrominoes.L:
                return new L();
            case Tetrominoes.T:
                return new T();
            case Tetrominoes.Z:
                return new Z();
            case Tetrominoes.S:
                return new S();
            case Tetrominoes.LINE:
                return new Line();
            case Tetrominoes.SQUARE:
                return new Square();
            default:
                String error = "Invalid numerical value for generating a Tetromino! " +
                                "You must be in range of the defined constants in the Tetrominoes class";
                Logger.log(MessageLevel.FATAL_ERROR, error);
                throw new IllegalArgumentException(error);
        }
    }

    /**
     * Sets the spawn speed. *NOTE speedIndex is the index defined in the
     * Speeds.LEVELS array. This method accesses the actual speed value
     * via the specified index.
     * @param speedIndex
     */
    public static void setCurSpeed(int speedIndex) {
        if (speedIndex < Speeds.LEVELS.length && speedIndex >= 0) {
            int realSpeed = Speeds.LEVELS[speedIndex];  // the actual speed value, defined in the Speeds.LEVELS array
            curSpeed = realSpeed;
            GameDisplay.setLevel(speedIndex);
        } else {
            throw new IllegalArgumentException("Invalid Speed Index! Must be in range of Speeds.LEVELS array!");
        }
    }

    /**
     * Stops the timer if it is running.
     */
    public static void stopTimer() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }

    /**
     * Starts the timer if it has a set action.
     */
    public static void startTimer() {
        if (!timer.isRunning() && timer.getActionListeners() != null) {
            timer.start();
        }
    }
}
