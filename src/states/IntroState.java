package states;

import main.App;
import managers.StateManager;
import utils.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * This is the first state of the application that allows the user to
 * choose what level of difficulty they would like to play the game
 * at.
 * Created by David Kramer on 2/9/2016.
 */
public class IntroState extends State implements Animation {

    private static BufferedImage logo;
    /**
     * These properties will be animated, after the user has decided on their
     * level difficulty.
     */
    private Font font = new Font("Courier New", Font.PLAIN, 25);    // all other choices
    private Font choiceFont = new Font("Courier New", Font.PLAIN, 30);  // active choice
    private Dimension size;
    private int choiceAlpha;       // alpha value for the current level choice
    private int alpha;             // alpha value for all other level choices
    private int newAlpha;          // alpha value after, opening is finished!
    private int logoY;
    private int choiceFontSize;
    private boolean isOpening;     // is this intro state currently opening
    private boolean keyboardEnabled;    // can we use the keyboard yet?
    private Color curChoiceColor;   // color for the active choice selection
    private Color choiceColor;      // color for all other choices

    private String[] levelChoices = new String[Speeds.LEVELS.length]; // holds various level difficulties
    private int curLevelChoice; // current index in levelChoices




    public IntroState() {
        init();
    }

    /**
     * @return the game logo buffered image
     */
    public static BufferedImage getLogo() {
        return logo;
    }

    /**
     * Initializes all the components and properties that will be animated
     * to their initial starting values.
     */
    private void init() {
        logoY = -235;
        newAlpha = 0;
        choiceAlpha = 255;
        alpha = 255;
        choiceFontSize = 25;
        keyboardEnabled = false;
        size = new Dimension(App.SIZE.width, App.SIZE.height);
        curChoiceColor = new Color(0, 255, 255, newAlpha);
        choiceColor = new Color(0, 255, 255, newAlpha);

        createLevelsArray();
        createLogoImage();
        animateIn();
    }

    /**
     * Animates the beginning part of this state when it is first created.
     */
    public void animateIn() {
        isOpening = true;
        // fade out
        TimerUtil.setAction(5, true, e -> {
            alpha--;
            logoY++;

            if (logoY >= 20) {
                logoY = 20;
            }

            if (alpha <= 0) {
                TimerUtil.stop();
                isOpening = false;
                // fade back in
                TimerUtil.setAction(5, true, e2 -> {
                    newAlpha += 2;
                    if (newAlpha >= 255) {
                        newAlpha = 255;
                        keyboardEnabled = true; // user can now control the app
                        TimerUtil.stop();
                    }
                    curChoiceColor = new Color(0, 255, 255, newAlpha);
                });
            }
        });
    }

    /**
     * Creates all of the available level choices.
     */
    private void createLevelsArray() {
        for (int i = 0, length = levelChoices.length; i < length; i++) {
            levelChoices[i] = "Level [" + i + "]";
        }
        curLevelChoice = 0;
    }

    /**
     * Loads in the image logo file so that it can be drawn
     * in this state.
     */
    private void createLogoImage() {
        logo = null;
        try {
            logo = ImageIO.read(new File("res/blockDropLogo.png"));
        } catch (IOException e) {
            Logger.log(MessageLevel.ERROR, "Logo failed to load! " +  e.getMessage());
        }
    }

    public void update() {
        //TODO implement this
    }

    /**
     * Renders everything to the screen
     * @param g2d - graphics context to write to
     */
    public void render(Graphics2D g2d) {
        drawBG(g2d);
        drawChoices(g2d);
        drawTitle(g2d);
        if (isOpening) {
            g2d.setColor(new Color(0, alpha, alpha, alpha));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * Draws the background to the screen.
     * @param g2d
     */
    private void drawBG(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Draws all the level choices to the screen.
     * @param g2d
     */
    private void drawChoices(Graphics2D g2d) {
        for (int i = 0, length = levelChoices.length; i < length; i++) {
            String choice = levelChoices[i];

            if (i != curLevelChoice) {
                g2d.setFont(font);
                g2d.setColor(new Color(120, 120, 120, newAlpha));
            } else {
                g2d.setFont(choiceFont);
                g2d.setColor(curChoiceColor);
            }
            int w = g2d.getFontMetrics().stringWidth(choice);
            int x = (getWidth() - w) / 2;
            int y = 200 + (i * 35);

            if (i == curLevelChoice) {
                g2d.setColor(new Color(0, 255, 255, newAlpha));
                g2d.drawLine(x - 5, y - 28, x + w + 5, y - 28);
                g2d.drawLine(x - 5, y + 10, x + w + 5, y + 10);   // active choice underline
            }
            g2d.drawString(choice, x, y);
        }
    }

    /**
     * Draws the title to the screen.
     * @param g2d
     */
    private void drawTitle(Graphics2D g2d) {
        int x = getCenterX(logo.getWidth());
        int y = 20;
        g2d.drawImage(logo, x, logoY, null);
    }

    /**
     *
     * @param w - width to account for center
     * @return center x position
     */
    private int getCenterX(int w) {
        return (getWidth() - w) / 2;
    }

    /**
     * Handles key pressed input.
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        if (keyboardEnabled) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    moveChoiceUp();
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    moveChoiceDown();
                    break;
                case KeyEvent.VK_ENTER:
                case KeyEvent.VK_SPACE:
                    animateOut();
                    break;
                case KeyEvent.VK_F12:
                    StateManager.setActiveState(new EasterEggState());
                    break;
                case KeyEvent.VK_ESCAPE:
                    exit();
                    break;
            }
        }
    }

    // unused methods
    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    /**
     * Moves the current level choice selection up, and if it goes past
     * the beginning, it is placed at the last selection.
     */
    private void moveChoiceUp() {
        curLevelChoice--;
        if (curLevelChoice < 0) {
            curLevelChoice = levelChoices.length - 1;
        }
        GameUtils.playSound("res/doot1.wav");
    }

    /**
     * Moves the current level choice selection down, and if it goes past
     * the ending, it is placed at the start selection.
     */
    private void moveChoiceDown() {
        curLevelChoice++;
        if (curLevelChoice >= levelChoices.length) {
            curLevelChoice = 0;
        }
        GameUtils.playSound("res/doot1.wav");
    }

    /**
     * Updates the state to the PlayState so that we can begin playing the game!
     */
    private void launchGame() {
        GameUtils.playSound("res/doot2.wav");
        StateManager.setActiveState(new PlayState());
        Spawner.setCurSpeed(curLevelChoice);
        Logger.log(MessageLevel.INFO, "Launching game with level: " + curLevelChoice);
    }

    /**
     * Prompts user with dialog, confirming they want to exit. If they hit okay,
     * the application terminates.
     */
    private void exit() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?",
                "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);

        if (choice == 0) {
            Logger.log(MessageLevel.INFO, "Application closed!");
            System.exit(0);
        }
    }

    /**
     * Dissolves this IntroState out to transition into the
     * next state.
     */
    public void animateOut() {
        TimerUtil.setAction(5, true, e -> {
            choiceAlpha -= 2;
            alpha -= 3;
            newAlpha -= 3;
            logoY += 8;
            choiceFontSize++;

            if (choiceFontSize > 140) {
                choiceFontSize = 140;
            }
            choiceFont = new Font("Courier New", Font.PLAIN, choiceFontSize);
            size.width += 2;
            if (size.width > 650) {
                size.width = 650;
            }
            StateManager.setAppSize(size);
            if (newAlpha <= 0) {
                newAlpha = 0;
                if (choiceAlpha <= 0) {
                    choiceAlpha = 0;
                    TimerUtil.stop();
                    launchGame();
                }
            }
        });
    }
}
