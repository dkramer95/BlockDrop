package states;

import managers.StateManager;
import shapes.Grid;
import shapes.GridTile;
import shapes.Tile;
import utils.GameUtils;
import utils.Logger;
import utils.MessageLevel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * This is an experimental, just for fun Easter Egg state, that renders
 * the developer credits and also has a cool animation of spinning
 * rectangles and ovals.
 * Created by David Kramer on 2/9/2016.
 */
public class EasterEggState extends State {
    private BufferedImage logoImg;
    private Random rng;
    private String titleString;
    private Font font;

    // animation properties
    private int velX;
    private int velY;
    private int nameY;
    private int curX;
    private int curY;
    private int alpha;
    private int alphaVel;
    private double curScale;
    private double scaleVel;
    private double curRotate;
    private double rotateVel;
    private GridTile[] animatedTiles;

    private boolean part1Finished = false;  //  the first part rendering of the name is finished




    public EasterEggState() {
        init();
    }

    /**
     * Initializes this EasterEgg state.
     */
    private void init() {
        createInitialValues();
        createGFX();
    }

    /**
     * Creates graphical elements.
     */
    private void createGFX() {
        logoImg = IntroState.getLogo();
        titleString = "Developed by David Kramer";
        font = new Font("Courier New", Font.PLAIN, 22);
        createTiles();
    }

    /**
     * Creates starting values for the animation properties.
     */
    private void createInitialValues() {
        rng = new Random();
        velX = rng.nextInt(6) + 1;
        velY = 2;
        curX = 0;
        alpha = 0;
        alphaVel = -1;
        curScale = 0.2;
        scaleVel = 0.01;
        curRotate = 0.0;
        rotateVel = 0.01;
        Logger.log(MessageLevel.INFO, "Easter Egg: VelX: %d", velX);
    }

    /**
     * Updates all the animation properties
     */
    public void update() {
        updatePosition();
        updateAlpha();
        updateScale();
        updateRotation();
        if (!part1Finished) {
            nameY += 0;
        }
    }

    /**
     * Updates x, y positions based off current velocities
     */
    private void updatePosition() {
        if ((curY >= getHeight()- 250) || (curY < -50)) {
            velY *= -1;
            updateColors();
            part1Finished = true;
        }
        if ((curX >= getWidth() - logoImg.getWidth()) || (curX < -50)) {
            velX *= -1;

        }
        curX += velX;
        curY += velY;
    }

    /**
     * Fades the alpha value in and out from 0 -> 255
     */
    private void updateAlpha() {
        if (alpha >= 100) {
            alpha = 100;
            alphaVel = -1;
        } else if (alpha <= 0) {
            alpha = 0;
            alphaVel = 1;
        }
        alpha += alphaVel;
    }

    /**
     * Updates the scale value, which is also used for rotations as well.
     */
    private void updateScale() {
        if (curScale >= 1.15 || curScale <= 0.1) {
            scaleVel *= -1;
        }
        curScale += scaleVel;
    }

    private void updateRotation() {
        if (curRotate >= 3.60) {
//            rotateVel *= -1;
            curRotate = 0.0;
        }
        curRotate += rotateVel;
    }

    /**
     * Creates random GridTiles which will be used for the animation
     * later on.
     */
    private void createTiles() {
        int length = rng.nextInt(10) + 5;
        animatedTiles = new GridTile[length];

        for (int i = 0; i < length; i++) {
            int randCol = rng.nextInt(Grid.COL_COUNT);
            int randRow = rng.nextInt(Grid.ROW_COUNT);

            GridTile t = new GridTile(randCol, randRow);
            t.setColor(GameUtils.getRandomColor(true));
            animatedTiles[i] = t;
        }
    }

    /**
     * Update all animation tiles with a new random color.
     */
    private void updateColors() {
        for (GridTile t : animatedTiles) {
            t.setColor(GameUtils.getRandomColor(true));
        }
    }

    /**
     * Renders all the colorized tiles.
     * @param g2d
     */
    private void renderTiles(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(rng.nextFloat() * 2));
        g2d.translate(curX + 300, 0);
        g2d.scale(curScale, curScale);
//        g2d.rotate(curScale);   // based off curScale as well
//        g2d.rotate(Math.sin(curRotate));
        g2d.rotate(curRotate);
        for (int i = 0, length = animatedTiles.length; i < length; i++) {
            Tile t = animatedTiles[i];
            float rotate = (float) i / 0.001f;  // add a subtle rotational factor
//            g2d.rotate(curScale + rotate);
            g2d.rotate(curRotate + rotate);
            // create an oval, based off positioning of tile
            g2d.setColor(t.getColor().darker());
            g2d.fillOval((int)t.getX(), (int)t.getY(), 10, 10);
            t.render(g2d);
        }
    }

    /**
     * Renders everything to the screen
     * @param g2d - graphics context to write to
     */
    public void render(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.translate(-curX, curY);
        g2d.setFont(font);
        drawFader(g2d);

        if (part1Finished) {
            renderTiles(g2d);
        } else {
            drawInfo(g2d);
        }
    }

    /**
     * Draws the "fader" which is a partially transparent rectangle,
     * that clears the screen and gives the illusion of fading out certain
     * leftover graphical artifacts that have been drawn to the screen.
     * @param g2d
     */
    private void drawFader(Graphics2D g2d) {
        g2d.translate(curX, curY);
        g2d.setColor(new Color(0, 0, 0, alpha));
        g2d.fillRect(0, 0, getWidth() + 500, getHeight() + 300);
    }

    /**
     * Draws the logo and title information
     * @param g2d
     */
    private void drawInfo(Graphics2D g2d) {
        g2d.setColor(Color.CYAN);
        g2d.translate(-curX, curY);
        // center text
        int width = g2d.getFontMetrics().stringWidth(titleString);
        int x = curX + (getWidth() - width) / 2;
//        g2d.drawString(titleString, curX + 15, nameY + 20);
        g2d.drawString(titleString, x, nameY + 20);
    }

    /**
     * Handles key events in this state.
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            StateManager.setActiveState(new IntroState());
        }
    }

    // unused
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
}
