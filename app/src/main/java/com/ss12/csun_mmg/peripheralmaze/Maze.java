package com.ss12.csun_mmg.peripheralmaze;

/*import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;*/

import android.content.res.TypedArray;

/**
 * Created by rosy on 2/14/15.
 */
public class Maze {
    Tile [][] mazeTiles;
    int numRows; // not currently used - set globally for all boards
    int numCols; // not currently used - set globally for all boards

    public Maze() {
        numRows = 8;
        numCols = 8;
        mazeTiles = new Tile[numRows][numCols];
    }

    public Maze(int numRows, int numCols) {
        this.numRows = numRows;
        this.numRows = numCols;
        this.mazeTiles = new Tile[numRows][numCols];
    }

    public Maze(Tile [][] mazeTiles) {
        this.mazeTiles = mazeTiles;
        this.numRows = mazeTiles.length;
        this.numRows = mazeTiles[0].length;
    }

    // TODO: draw full maze capability? used at end? => how would we duplicate it in sound?
}