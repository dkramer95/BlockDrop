package states;

import managers.StateManager;
import utils.Logger;
import utils.TimerUtil;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Ending GameOver state that displays a brief msg and then
 * transitions back to the starting IntroState.
 * Created by David Kramer on 2/16/2016.
 */
public class GameOverState extends State {
    private static String msg = "Game Over!";
    private int fontSize = 0;
    private int alpha = 0;  // animation property




    public GameOverState() {
        init();
    }

    private void init() {
        TimerUtil.setAction(4500, true, e -> {
            StateManager.setActiveState(new IntroState());
        });
    }

    /**
     * Updates the animation properties.
     */
    public void update() {
        alpha += 1;
        fontSize += 1;
        if (alpha >= 255) {
            alpha = 255;
        }
        if (fontSize >= 100) {
            fontSize = 100;
        }
    }

    /**
     * Renders the text to the screen and fades out the background.
     * @param g2d - graphics context to write to
     */
    public void render(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(0, 0, 0, alpha));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(new Color(0, 255, 255, alpha));
        g2d.setFont(new Font("Courier New", Font.PLAIN, fontSize));

        // center text horiz and vert
        int w = g2d.getFontMetrics().stringWidth(msg);
        int x = (getWidth() - w) / 2;
        int y = (getHeight() - 50) / 2;
        g2d.drawString(msg, x, y);
    }

    public void keyTyped(KeyEvent e) {
        StateManager.setActiveState(new IntroState());
    }

    // unused
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
}
