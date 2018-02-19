package utils;

import managers.StateManager;
import states.IntroState;
import shapes.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Utility class to display stats about the game, such as the current level,
 * how many blocks have been dropped, and the total lines cleared, to the
 * left side of the screen.
 * Created by David Kramer on 2/9/2016.
 */
public class GameDisplay implements Animation {
    private static Font font = new Font("Courier New", Font.PLAIN, 20);
    private static String curLevel;
    private static String blocksDropped;
    private static String linesCleared;

    private static BufferedImage logoImg;
    private static int levelUpCount;    // keep track of when we can advance to the next level
    private static int levelNum;
    private static int dropCount;
    private static int clearCount;
    private static int xOffset; // offset for text and gfx




    /**
     * Renders everything to the screen.
     * @param g2d
     */
    public static void render(Graphics2D g2d) {
        drawLogo(g2d);
        drawInfo(g2d);
        drawDebugStatus(g2d);
    }

    /**
     * Draws the logo to the screen.
     * @param g2d
     */
    private static void drawLogo(Graphics2D g2d) {
        g2d.drawImage(logoImg, xOffset, 50, null);
    }

    /**
     * Draws information about the current game such as the level,
     * amount of blocks dropped, and the total lines cleared.
     * @param g2d
     */
    private static void drawInfo(Graphics2D g2d) {
        g2d.setColor(Color.CYAN);
        g2d.setFont(font);
        g2d.drawString(curLevel, xOffset, 300);
        g2d.drawString(blocksDropped, xOffset, 350);
        g2d.drawString(linesCleared, xOffset, 400);
    }

    /**
     * If debug is turned on, this will draw an indicator.
     * @param g2d
     */
    private static void drawDebugStatus(Graphics2D g2d) {
        if (Tile.isShowDebug()) {
            g2d.setFont(new Font("Courier New", Font.PLAIN, 12));
            String text = "SHOW_DEBUG: true  Toggle: F4";
            Dimension size = StateManager.getActiveState().getSize();
            int x = size.width - g2d.getFontMetrics().stringWidth(text) - 40;
            int y = 20;
            g2d.drawString(text, x, y);
        }
    }

    /**
     * Resets all the current game information back to the default values
     */
    public static void reset() {
        xOffset = -125;
        levelNum = 0;
        dropCount = 0;
        clearCount = 0;
        levelUpCount = 0;
        curLevel = "Level: " + levelNum;
        blocksDropped = "Dropped: " + dropCount;
        linesCleared = "Cleared: " + clearCount;
        logoImg = IntroState.getLogo();
        animateIn();
    }

    /**
     * Properties to be adjusted via animation in.
     */
    public static void animateIn() {
        xOffset++;
        if (xOffset >= 30) {
            xOffset = 30;
        }
    }

    /**
     * Properties to be adjusted via animation out.
     */
    public static void animateOut() {
        xOffset -= 2;
    }

    /**
     * Increases the level display indicator by one.
     */
    public static void increaseLevel() {
        if (levelNum + 1 < Speeds.LEVELS.length) {
            levelNum++;
            curLevel = "Level: " + levelNum;
            levelUpCount = 0;
        }
    }

    /**
     * Sets the level display indicator to the specified level.
     * @param level - Level num to display
     */
    public static void setLevel(int level) {
        levelNum = level;
        curLevel = "Level: " + levelNum;
        levelUpCount = 0;
    }

    /**
     * @return the current level num
     */
    public static int getLevelNum() {
        return levelNum;
    }

    /**
     * Increases the total line clear amount by the specified value
     * @param linesClears - amount of line clears to add
     */
    public static void increaseLineCount(int linesClears) {
        clearCount += linesClears;
        levelUpCount += linesClears;
        linesCleared = "Cleared: " + clearCount;
        System.out.println("LEVEL UP COUNT: " + levelUpCount);
    }

    /**
     * @return the total line clear count
     */
    public static int getClearCount() {
        return clearCount;
    }

    /**
     * Increases the block drop display indicator by one.
     */
    public static void increaseBlockDrop() {
        dropCount++;
        blocksDropped = "Dropped: " + dropCount;
    }

    /**
     * @return the total block drop count
     */
    public static int getDropCount() {
        return dropCount;
    }

    public static int getLevelUpCount() {
        return levelUpCount;
    }
}
