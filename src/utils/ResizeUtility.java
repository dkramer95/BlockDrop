package utils;

import main.App;
import managers.StateManager;
import shapes.Grid;
import shapes.Tile;
import states.PlayState;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * This class handles window resizing events and updates any necessary components,
 * that would be affected by the event, such as the GameTiles. When a window is
 * resized, the tiles need to be sized up as well to occupy the newly available
 * space in the application.
 * Created by David Kramer on 2/8/2016.
 */
public class ResizeUtility extends GameUtility implements ComponentListener {
    private static App app;
    private static PlayState playState;    // main playing state of the app


    private ResizeUtility() {}   // don't instantiate directly, use init()


    public static boolean init(App _app) {
        boolean success = false;
        if (_app != null) {
            ResizeUtility resizeUtil = new ResizeUtility();
            app = _app;
            app.addComponentListener(resizeUtil);
            success = true;
        }
        return success;
    }

    /**
     * Updates the tile x and y offsets, which are their positions relative
     * in the grid.
     */
    public static void updateOffsets() {
        Tile.setGlobalScale(getScaleFactor());
        Tile.setGlobalOffsets(getXOffset() + 75, getYOffset());
    }

    /**
     *
     * @return the correct xOffset for the grid
     */
    private static int getXOffset() {
        int gridWidth = Grid.COL_COUNT * Tile.WIDTH;
        int xOffset = (StateManager.getActiveState().getWidth() - gridWidth) / 2;
        return xOffset;
    }

    /**
     *
     * @return the correct yOffset for the grid
     */
    private static int getYOffset() {
        int gridHeight = Grid.ROW_COUNT * Tile.HEIGHT;
        int yOffset = (StateManager.getActiveState().getHeight() - gridHeight) / 2;
        return yOffset;
    }

    /**
     *
     * @return the correct scale factor for tiles in the grid
     */
    private static float getScaleFactor() {
        float heightDX = (float)(StateManager.getActiveState().getHeight() / App.SIZE.getHeight());
        return heightDX;
    }

    public void componentResized(ComponentEvent e) {
        StateManager.updateSize(app.getSize());
        updateOffsets();
    }

    // unused methods
    public void componentMoved(ComponentEvent e) {}
    public void componentShown(ComponentEvent e) {}
    public void componentHidden(ComponentEvent e) {}
}
