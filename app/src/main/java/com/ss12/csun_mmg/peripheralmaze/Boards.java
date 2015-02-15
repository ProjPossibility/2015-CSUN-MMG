package com.ss12.csun_mmg.peripheralmaze;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;


public class Boards extends ActionBarActivity {
    Maze [] boards;
    int numBoards = R.integer.numBoards;
    int numRows = R.integer.numRows;
    int numCols = R.integer.numCols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {// like main
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_test);

        Log.v("MazeParseDebug", "Boards onCreate invoked.");


        // debug example:
        // Log.v("MazeParseDebug", "dump stuff");

        // Pull resources - needs to be in an activity.
        Resources res = getResources();

        Log.v("MazeParseDebug", "numBoards = " + String.valueOf(numBoards));
        Log.v("MazeParseDebug", "numRows = " + String.valueOf(numRows));
        Log.v("MazeParseDebug", "numCols = " + String.valueOf(numCols));

        boards = new Maze [numBoards];
        int numGoodBoards=0;
        TypedArray boardData = res.obtainTypedArray(R.array.boardData);

        for (int i=0; i<boardData.length(); i++) {
            try {
                JSONObject boardObj = new JSONObject(boardData.getString(i));
                Maze newMaze = new Maze(numRows,numCols);
                int numGoodTiles=0;

                // iterate over each row x col index combo and get that property value
                for (int row=0; row<numRows; row++) {
                    for (int col=0; col<numCols; col++) {
                        JSONArray data = boardObj.getJSONArray(String.format("%d%d",row,col));
                        if (data != null && data.length() == numCols) {
                            newMaze.setTile(
                                    row, col,
                                    new Tile(
                                        new int[]{data.getInt(0)},
                                        data.getInt(6) == 1,
                                        data.getInt(7) == 1
                                    )
                            );
                            numGoodTiles++;
                        }
                    }
                }

                // if all tiles were clean, add this maze
                if (numGoodTiles == numRows * numCols) {
                    boards[i] = newMaze;
                    numGoodBoards++;
                }
            } catch (Exception e) {}
        } // end for each of the boards

        // TODO what happens if a maze was not set?
        // if (numGoodBoards != boards.length) {}
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
