package com.ss12.csun_mmg.peripheralmaze;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class Boards extends ActionBarActivity {
    Maze [] boards;
    int numBoards = -1;
    int numRows = -1;
    int numCols = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {// like main
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_test);

        Log.v("MazeParseDebug", "Boards onCreate invoked.");


        // debug example:
        // Log.v("MazeParseDebug", "dump stuff");

        // Pull resources - needs to be in an activity.
        Resources res = getResources();

        TypedArray boardInfo = res.obtainTypedArray(R.array.boardInfo);
        String boardInfoStrings[];
        for (int i = 0; i < 3; i++) {
            boardInfoStrings = boardInfo.getString(i).split("=");
            if (boardInfoStrings[0].equals("numRows")) {
                numRows = Integer.valueOf(boardInfoStrings[1]);
            } else if (boardInfoStrings[0].equals("numCols")) {
                numCols = Integer.valueOf(boardInfoStrings[1]);
            } else { // numBoards
                numBoards = Integer.valueOf(boardInfoStrings[1]);
            }
        }

        Log.v("MazeParseDebug", "numBoards = " + String.valueOf(numBoards));
        Log.v("MazeParseDebug", "numRows = " + String.valueOf(numRows));
        Log.v("MazeParseDebug", "numCols = " + String.valueOf(numCols));

        boards = new Maze [numBoards];
        TypedArray boardData = res.obtainTypedArray(R.array.boardData);
        String [] boardItemStrings;

        for (int m = 0; m < 1; m++) {

            // Make new board
            boards[m] = new Maze(numRows,numCols);

            boardItemStrings = boardData.getString(m).split(",");
            for (int i=0; i < boardItemStrings.length; i++) {
                boardItemStrings[i] = boardItemStrings[i].replaceAll("[{}\\Q[]\\E]", "");
                Log.v("MazeParseDebug", "item [" + String.valueOf(i) + "] = " + boardItemStrings[i]);

                String[] itemData;

                // first item is of the form "ID:11"; all subsequent ones are of the form
                // "00:1 1 1 1 1 0"
                /*if (i == 0) {
                    itemData = boardItemStrings[i].split(":");
                    Maze[]
                } else {

                }*/
            }

            /*
            board = new Tile[boardRows][boardCols];

            // Iterate over XML data
            for (int i = 0; i < boardRows; i++) {


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
                        walls[k] = Character.getNumericValue(mazeTile[1].charAt(2 * k));
                    }
                    boolean isStart = (Character.getNumericValue(mazeTile[1].charAt(8)) == 1);
                    boolean isEnd = (Character.getNumericValue(mazeTile[1].charAt(10)) == 1);

                    maze[row][col] = new Tile(walls, isStart, isEnd);

                    //Log.v("MazeParseDebug", "Tile walls: [" + Integer.toString(walls[0])+ " " + Integer.toString(walls[1]) + " " + Integer.toString(walls[2]) + " " + Integer.toString(walls[3]) + "]");
                    //Log.v("MazeParseDebug", "isStart: " + String.valueOf(isStart));
                    //Log.v("MazeParseDebug", "isEnd: " + String.valueOf(isEnd));

                }
            }
            */
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}