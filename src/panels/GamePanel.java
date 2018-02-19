package panels;

import managers.StateManager;
import states.State;
import utils.*;

import javax.swing.*;
import java.awt.*;

/**
 * This class is responsible for containing the StateManager of the game
 * and displaying the current state to the screen. This class contains the
 * main game loop for updating and redrawing to the screen.
 * Created by David Kramer on 2/8/2016.
 */
public class GamePanel extends JPanel implements Runnable {
    private boolean initialized = false;
    private Thread thread;
    private boolean isRunning;




    public GamePanel() {}

    /**
     * Initializes the GamePanel and sets up the StateManager and
     * establishes input connections to the active state.
     * @return true if successful, false otherwise
     */
    public boolean init() {
        if (KeyboardUtil.init(this)) {
            State state = StateManager.getActiveState();
            state.setSize(getSize());
            initialized = true;
        }
        return initialized;
    }

    /**
     * Starts the game loop.
     */
    public void start() {
        if (initialized) {
            isRunning = true;
            thread = new Thread(this);
            thread.start();
        } else {
            Logger.log(MessageLevel.FATAL_ERROR, "Unable to start() GamePanel. It is not initialized!");
        }
    }

    /**
     * Main game loop of the application. Actively renders and updates
     * the current state in StateManager.
     */
    public void run() {
        while (isRunning) {
            StateManager.update();
            repaint();

            // limit refresh rate
            try {
                Thread.sleep(StateManager.getRenderSpeed());
            } catch (InterruptedException e) {}
        }
    }

    /**
     * Draws everything in the StateManager.
     * @param g
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2d = null;
        try {
            g2d = (Graphics2D)g.create();
            StateManager.render(g2d);
        } finally {
            g2d.dispose();
        }
    }
}
