package com.ss12.csun_mmg.peripheralmaze.util;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.ss12.csun_mmg.peripheralmaze.R;


public class BoardTest extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) { // like main
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_test);

        // debug example:
        // Log.v("MazeParseDebug", "dump stuff");

        // TODO: relocate this code into a location that makes sense for maze parsing
        /*Resources res = getResources();
        TypedArray boardData = res.obtainTypedArray(R.array.board1);
        int length = boardData.length();

        Tile[][] maze = new Tile [length][length];
        for (int i = 0; i < length; i++) {
            String mazeRow = boardData.getString(i).replaceAll("[{}\\Q[]\\E]","");

            // split into "30:1 1 1 1 1 0" - (row, col) and then tile code
            String[] mazeTilePairs = mazeRow.split(",");
            //Log.v("MazeParseDebug", mazeTilePairs[0]);
            for (int j = 0; j < mazeTilePairs.length; j++) {
                // split into "01" and "0 1 0 0 1 0" - (row, col) and then tile code
                String[] mazeTile = mazeTilePairs[j].split(":");

                // assign row and column
                int row = Character.getNumericValue(mazeTile[0].charAt(0));
                int col = Character.getNumericValue(mazeTile[0].charAt(1));
                //Log.v("MazeParseDebug:", Integer.toString(row));
                //Log.v("MazeParseDebug:", Integer.toString(col));
                // TODO: since not using it, it would be OK to remove the ID number of the tile from data file generator script

                //Log.v("MazeParseDebug:", mazeTile[1]);
                int[] walls = new int[4];
                for (int k = 0; k < 4; k++) {
                    walls[k] = Character.getNumericValue(mazeTile[1].charAt(2*k));
                }
                boolean isStart = (Character.getNumericValue(mazeTile[1].charAt(8)) == 1);
                boolean isEnd = (Character.getNumericValue(mazeTile[1].charAt(10)) == 1);

                maze[row][col] = new Tile(walls, isStart, isEnd);

                //Log.v("MazeParseDebug", "Tile walls: [" + Integer.toString(walls[0])+ " " + Integer.toString(walls[1]) + " " + Integer.toString(walls[2]) + " " + Integer.toString(walls[3]) + "]");
                //Log.v("MazeParseDebug", "isStart: " + String.valueOf(isStart));
                //Log.v("MazeParseDebug", "isEnd: " + String.valueOf(isEnd));

            }

        }
        Board testMaze = new Board(maze,length,length);
        //TODO: the final maze structure test is incomplete (Board/Tile untested)
        //Log.v("MazeParseDebug","Length is " + Integer.toString(length));
*/

        //Log.v("MazeParseDebug", "BoardTest invoked.");

        //Board gameBoard = new Board();
    }
}
