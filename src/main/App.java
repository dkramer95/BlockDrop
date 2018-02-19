package main;

import managers.StateManager;
import panels.GamePanel;
import utils.Logger;
import utils.MessageLevel;
import utils.ResizeUtility;

import javax.swing.*;
import java.awt.*;

/**
 * This is the main application class that contains all of the necessary
 * components to play the Block Drop game, and ensures that everything
 * is properly initialized.
 * Created by David Kramer on 2/6/2016.
 */
public class App extends JFrame {
    public static final Dimension SIZE = new Dimension(375, 600);
    public static final Dimension MIN_SIZE = new Dimension(300, 450);
    public static final String TITLE = "BlockDrop 2.0 by David Kramer";

    private GamePanel gamePanel;    // main game content container



    public App() {
        init();
    }

    /**
     * Initializes the application with all of the required components.
     * @return true if ALL components were successful, false otherwise
     */
    private boolean init() {
        boolean success = false;

        if (Logger.init("BlockDrop.log")) {
            if (StateManager.init(this)) {
                if (ResizeUtility.init(this)) {
                    gamePanel = new GamePanel();
                    if (gamePanel.init()) {
                        add(gamePanel);
                        createAndShowGUI();
                        gamePanel.start();
                        updateStateSize();
                        success = true;
                    } else {
                        Logger.log(MessageLevel.FATAL_ERROR, "GamePanel failed to initialize!");
                    }
                } else {
                    Logger.log(MessageLevel.FATAL_ERROR, "ResizeUtility failed to initialize!");
                }
            } else {
                Logger.log(MessageLevel.FATAL_ERROR, "StateManager failed to initialize!");
            }
        } else {
            Logger.log(MessageLevel.FATAL_ERROR, "Logger failed to initialize!");
        }
        return success;
    }

    /**
     * Initially when an application is constructed, the gamePanel size
     * is at (0, 0). This method corrects that so that it is correctly
     * sized.
     */
    private void updateStateSize() {
        StateManager.updateSize(gamePanel.getSize());
    }

    /**
     * Displays the GUI window to the screen.
     */
    private void createAndShowGUI() {
        setSize(SIZE);
        setMinimumSize(MIN_SIZE);
        setLocationRelativeTo(null);
        setTitle(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        Logger.log(MessageLevel.INFO, "App Window %s created!", getSize());
    }

    /**
     * Main method of the application that creates a new instance of
     * the BlockDrop application.
     * @param args
     */
    public static void main(String[] args) {
        App app = new App();
    }
}
