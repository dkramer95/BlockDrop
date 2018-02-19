package states;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;

/**
 * Base class for all the states that the BlockDrop game can have. This enforces
 * basic appearance behavior as to what should currently be executing as the
 * current thread is running.
 * Created by David Kramer on 1/19/2016.
 */
public abstract class State extends JPanel implements KeyListener {

    public State() {}   // default constructor

    /**
     * Abstract method for update behavior of a state.
     */
    public abstract void update();

    /**
     * Abstract method for drawing to screen of a state.
     * @param g2d - graphics context to write to
     */
    public abstract void render(Graphics2D g2d);

}
