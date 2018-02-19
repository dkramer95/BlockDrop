package managers;

import main.App;
import states.IntroState;
import states.State;
import utils.KeyboardUtil;
import utils.Logger;
import utils.MessageLevel;

import java.awt.*;
import java.util.Stack;

/**
 * This class manages all the various states that this application can have.
 * When transitioning between states, a new State is pushed onto the stack,
 * which becomes the active state, for handling updates and rendering to
 * the screen.
 * Created by David Kramer on 2/8/2016.
 */
public class StateManager {
    private static boolean initialized = false;
    private static int renderSpeed = 30;    // how fast should states be rendered?

    private static Stack<State> states;
    private static App app;



    private StateManager() {}   // don't instantiate directly, use init()

    /**
     * Initializes StateManager with a link to the main application
     * @param _app - Main application
     * @return true if successful, false otherwise
     */
    public static boolean init(App _app) {
        if (_app != null) {
            app = _app;
            states = new Stack<>();
            states.push(new IntroState());
            initialized = true;
        }
        return initialized;
    }

    /**
     * Calls update method on the active state.
     */
    public static void update() {
        states.peek().update();
    }

    /**
     * Calls the render method on the active state.
     * @param g2d
     */
    public static void render(Graphics2D g2d) {
        states.peek().render(g2d);
    }

    /**
     * Sets the current state. If the state is an IntroState, all other
     * states are cleared, to save on memory resources.
     * @param state
     */
    public static void setActiveState(State state) {
        // ensure size is proper
        Dimension size = getActiveState().getSize();

        if (state instanceof IntroState && states.size() > 1) {
            states.clear(); // clear out old states to free up memory
            Logger.log(MessageLevel.INFO, "State stack cleared!");
        }
        state.setSize(size);
        states.push(state);

        Logger.log(MessageLevel.INFO, "State pushed %s", state.getClass().getName());
    }

    /**
     *
     * @return the active state
     */
    public static State getActiveState() {
        return states.peek();
    }

    /**
     * Updates the size of the active state
     * @param size
     */
    public static void updateSize(Dimension size) {
        getActiveState().setSize(size);
    }

    /**
     * Sets the render speed that everything should be drawn at.
     * @param speed
     */
    public static void setRenderSpeed(int speed) {
        renderSpeed = speed;
    }

    /**
     *
     * @return the current render speed
     */
    public static int getRenderSpeed() {
        return renderSpeed;
    }

    /**
     * Sets the window size of the application
     * @param size
     */
    public static void setAppSize(Dimension size) {
        app.setSize(size);
        updateSize(size);
    }

    public static App getApp() {
        return app;
    }

    /**
     *
     * @return window size of the application
     */
    public static Dimension getAppSize() {
        return app.getSize();
    }
}
