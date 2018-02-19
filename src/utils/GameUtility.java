package utils;

import panels.GamePanel;
import shapes.Grid;
import states.State;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Abstract class for GameUtilities that provides subclasses with
 * access to the Grid and GamePanel, through the init method.
 * Created by David Kramer on 2/7/2016.
 */
public abstract class GameUtility {
    protected static boolean initialized = false;
    protected static Grid grid;
    protected static Timer timer;

    protected static State state;
    protected static GamePanel gamePanel;




    protected GameUtility() {}

    public static boolean init(State _state) {
        if (_state != null) {
            state = _state;
            createTimer();
            initialized = true;
        }
        return initialized;
    }

    public static boolean init(GamePanel _gamePanel) {
        if (_gamePanel != null) {
            gamePanel = _gamePanel;
            createTimer();
            initialized = true;
        }
        return initialized;
    }

    /**
     * Creates the timer with a null action and delay of 0.
     */
    protected static void createTimer() {
        timer = new Timer(0, null);
    }

    /**
     * Resets the timer to a new set of execution actions.
     * @param delay - delay for the new execution
     * @param action - the action to run
     */
    protected static void resetTimer(int delay, ActionListener action) {
        timer.stop();
        removeTimerActions();
        timer.setInitialDelay(0);
        timer.setDelay(delay);
        timer.addActionListener(action);
        timer.start();
    }

    /**
     * Removes any action listeners from the timer.
     */
    protected static void removeTimerActions() {
        for (ActionListener a : timer.getActionListeners()) {
            timer.removeActionListener(a);
        }
    }
}
