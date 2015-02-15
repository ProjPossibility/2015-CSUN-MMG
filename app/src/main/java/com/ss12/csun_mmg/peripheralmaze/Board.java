package com.ss12.csun_mmg.peripheralmaze;

/*import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;*/

import android.content.res.TypedArray;

/**
 * Created by rosy on 2/14/15.
 */
public class Board {
    /* Pull the board specified by mapID from the maps directory in app/src/main/res/maps and
    parse it into a 2D array of board tiles. */
    private Tile[][] board;
    private int rows;
    private int cols;

    public Board() {
        rows = 8;
        cols = 8;
        board = new Tile [rows][cols];
    }

    public Board(Tile [][] board,int rows,int cols) {
        this.board = board;
        this.rows = rows;
        this.cols = cols;
    }
}