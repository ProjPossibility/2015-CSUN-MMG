package com.ss12.csun_mmg.peripheralmaze;

/*import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;*/

/**
 * Created by rosy on 2/14/15.
 */
public class Maze {
    Tile [][] mazeTiles;
    int numRows; // not currently used - set globally for all boards
    int numCols; // not currently used - set globally for all boards

    public Maze() {
        this(8,8);
    }

    public Maze(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.mazeTiles = new Tile[numRows][numCols];
    }

    public Maze(Tile [][] mazeTiles) {
        this.mazeTiles = mazeTiles;
        this.numRows = mazeTiles.length;
        this.numCols = mazeTiles[0].length;
    }

    public void setTile(int row, int col, Tile tile) {
        mazeTiles[row][col] = tile;
    }

    public Tile getTile(int row, int col) {
        return null;
    }

    public Tile getStartTile() {
        if (mazeTiles != null) {
            for (int row=0; row<numRows; row++) {
                for (int col=0; col<numCols; col++) {
                    if (mazeTiles[row][col] != null && mazeTiles[row][col].isStartTile()) {
                        return mazeTiles[row][col];
                    }
                }
            }
        }
        return null;
    }

    public Tile getEndTile() {
        if (mazeTiles != null) {
            for (int row=0; row<numRows; row++) {
                for (int col=0; col<numCols; col++) {
                    if (mazeTiles[row][col] != null && mazeTiles[row][col].isEndTile()) {
                        return mazeTiles[row][col];
                    }
                }
            }
        }
        return null;
    }

    // TODO: draw full maze capability? used at end? => how would we duplicate it in sound?
}