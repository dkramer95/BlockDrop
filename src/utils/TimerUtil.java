package utils;

import java.awt.event.ActionListener;
import javax.swing.Timer;

import shapes.Grid;
import shapes.GridTile;

/**
 * This utility class provides an easy access SwingTimer
 * which can be useful for times when animation is needed.
 * Created by David Kramer on 2/10/2016.
 */
public class TimerUtil {
    private static Timer timer;
    private static GridTile[] tilesToAnimate; //TODO MOVE THIS ELSEWHERE
    private static int index;


    public static void setAction(int delay, boolean startNow, ActionListener action) {
        clearTimer();
        timer = new Timer(delay, action);

        if (startNow) {
            timer.start();
        }
    }

    /**
     * Starts the timer
     */
    public static void start() {
        if (timer != null) {
            timer.start();
        }
    }

    /**
     * Stops the timer
     */
    public static void stop() {
        if (timer != null) {
            timer.stop();
        }
    }

    /**
     * Clears the timer from any previous actions.
     */
    private static void clearTimer() {
        if (timer != null) {
            timer.stop();
            for (ActionListener action: timer.getActionListeners()) {
                timer.removeActionListener(action);
            }
        }
    }

    /**
     * @return true if this timer is running
     */
    public static boolean isRunning() {
        boolean isRunning = false;
        if (timer != null) {
            isRunning = timer.isRunning();
        }
        return isRunning;
    }

    public static void animateClear(GridTile[] _gridTiles, Grid grid) {
        tilesToAnimate = _gridTiles;
        index = 0;

        setAction(20, true, e -> {
            if (index >= tilesToAnimate.length) {
                stop();
                grid.updateShiftTiles();
            } else {
                GridTile t = tilesToAnimate[index];
                t.clear();
                index++;
            }
        });
    }

}
