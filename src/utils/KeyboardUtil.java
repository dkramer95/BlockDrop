package utils;

import managers.StateManager;
import panels.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by David Kramer on 2/9/2016.
 */
public class KeyboardUtil implements KeyListener {
    private static boolean initialized = false;
    private static GamePanel gamePanel;
    private static KeyboardUtil instance;

    private KeyboardUtil() {}

    public static boolean init(GamePanel _gamePanel) {
        if (_gamePanel != null) {
            gamePanel = _gamePanel;
            instance = new KeyboardUtil();
            gamePanel.addKeyListener(instance);
            gamePanel.setFocusable(true);
            gamePanel.requestFocus();
            initialized = true;
        }
        return initialized;
    }

    public void keyPressed(KeyEvent e) {
        StateManager.getActiveState().keyPressed(e);
    }

    // unused methods
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
}
