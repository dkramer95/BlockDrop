package states;

import managers.StateManager;
import shapes.Grid;
import shapes.GridTile;
import shapes.Tetrominoes.Tetromino;
import shapes.Tile;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * This PlayState is the main state of the application that contains the
 * actual game components, such as the grid, and interaction with falling
 * Tetrominoes, by the user.
 * Created by David Kramer on 2/9/2016.
 */
public class PlayState extends State implements Animation {
    private Grid grid;

    // animation stuff
    private int nextRow;
    private int nextCol;
    private int alpha = 0;
    private boolean isExiting;  // are we in the middle of exiting this state?




    public PlayState() {
        init();
    }

    /**
     * Initializes this PlayState.
     * @return
     */
    private boolean init() {
        boolean success = false;
        grid = new Grid();
        if (Tetromino.init(grid)) {
            if (Spawner.init(this, grid)) {
                success = true;
                nextCol = -1;
                animateIn();
                GameDisplay.reset();
            } else {
                Logger.log(MessageLevel.FATAL_ERROR, "Spawner failed to initialize!");
            }
        } else {
            Logger.log(MessageLevel.FATAL_ERROR, "Tetromino failed to initialize with grid!");
        }
        return success;
    }

    /**
     * Animates the grid, at the start of this state.
     */
    public void animateIn() {
        nextCol = -1;
        TimerUtil.setAction(6, true, e -> {
            GameDisplay.animateIn();
            nextCol++;
            if (nextCol >= Grid.COL_COUNT) {
                nextRow++;
                nextCol = 0;
            }
            if (nextRow >= Grid.ROW_COUNT) {
                TimerUtil.stop();
                Spawner.start();
            }
            GridTile t = grid.get(nextCol, nextRow);
            t.setColor(Color.DARK_GRAY);
        });
    }

    public void update() {}

    /**
     * Renders everything in this PlayState to the screen.
     * @param g2d - graphics context to write to
     */
    public void render(Graphics2D g2d) {
        drawBG(g2d);
        grid.render(g2d);
        GameDisplay.render(g2d);

        if (isExiting) {
            // draw transparent rect to give appearance of fading out
            drawCover(g2d);
        }
    }

    /**
     * Draws the background
     * @param g2d
     */
    private void drawBG(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Draws the "alpha cover", which gives the appearance of fading
     * out, when we are exiting away from this state.
     * @param g2d
     */
    private void drawCover(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, alpha));
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Handles key presses in this state
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        Tetromino t = grid.getFallingTetromino();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                t.moveRight();
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                t.moveLeft();
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                t.moveDown();
                break;
//            case KeyEvent.VK_UP:
//            case KeyEvent.VK_W:
//                t.moveUp();
//                break;
            case KeyEvent.VK_R:
                t.rotate();
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_ENTER:
                Spawner.animateDrop();
                break;
            case KeyEvent.VK_ESCAPE:
                showQuitDialog();
                break;

            /////////////////////////
            //// DEBUG HOT KEYS ////
            ///////////////////////
            case KeyEvent.VK_F2:
                grid.testFillGrid();
                break;
            case KeyEvent.VK_F3:
                grid.clear();
                break;
            case KeyEvent.VK_F4:
                Tile.toggleDebug();
                break;
            case KeyEvent.VK_F5:
                grid.printGrid();
                break;
            case KeyEvent.VK_F7:
                StateManager.setActiveState(new GameOverState());
                break;
        }
    }

    /**
     * Shows confirmation quit dialog, if the user has hit the ESCAPE key.
     * If they exit, this state animates out.
     */
    private void showQuitDialog() {
        Spawner.stopTimer();
        int result = JOptionPane.showConfirmDialog(this, "Do you really want to leave the game?",
                                                    "Confirm Quit", JOptionPane.OK_CANCEL_OPTION);
        if (result == 0) {  // quit
            animateOut();
        } else {    // continue game
            Spawner.startTimer();
        }
    }

    /**
     * The animation for when we are leaving this state.
     */
    public void animateOut() {
        TimerUtil.setAction(5, true, e-> {
            isExiting = true;
            GameDisplay.animateOut();
            Dimension size = StateManager.getAppSize();
            size.width -= 2;
            StateManager.setAppSize(size);

            if (size.width < 350) {
                size.width = 350;
                TimerUtil.stop();
                StateManager.setActiveState(new IntroState());
            }

            alpha += 2;

            if (alpha >= 255) {
                alpha = 255;
            }
        });
    }

    // unused methods
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    /**
     * @return the grid in this PlayState
     */
    public Grid getGrid() {
        return grid;
    }
}
