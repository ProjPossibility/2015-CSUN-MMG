package com.ss12.csun_mmg.peripheralmaze;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ss12.csun_mmg.peripheralmaze.util.SystemUiHider;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class Board extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    Maze [] boards;
    int numBoards = R.integer.numBoards;
    int numRows = R.integer.numRows;
    int numCols = R.integer.numCols;

    MazeGame mMazeGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_board);

        final View contentView = findViewById(R.id.maze_game_layout);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider.hide();

        boards = new Maze [numBoards];
        int numGoodBoards=0;
        TypedArray boardData = getResources().obtainTypedArray(R.array.boardData);

        for (int i=0; i<boardData.length(); i++) {
            try {
                JSONObject boardObj = new JSONObject(boardData.getString(i));
                Maze newMaze = new Maze(numRows,numCols);
                int numGoodTiles=0;

                // iterate over each row x col index combo and get that property value
                for (int row=0; row<numRows; row++) {
                    for (int col=0; col<numCols; col++) {
                        String position = String.format("%d%d",row,col);
                        JSONArray data = boardObj.getJSONArray(position);
                        if (data != null && data.length() == numCols) {
                            newMaze.setTile(
                                    row, col,
                                    new Tile(
                                            position,
                                            new int[]{
                                                    data.getInt(0),data.getInt(1),
                                                    data.getInt(2),data.getInt(3)
                                            },
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

        mMazeGame = new MazeGame(boards[0]);
        mMazeGame.setEventCallback(new GameCallback() {
            @Override
            public void run(GameEvent event) {
                Board.this.onGameEvent(event);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO temporarily going straight to the review page
        Intent intent = new Intent(this, ReviewMenu.class);
        startActivity(intent);
    }

    private void onGameEvent(GameEvent event) {
        Log.v("Board", "Received game event: "+event.toString());
        switch (event.getEventType()) {
            case PLAYER_WIN:
            case PLAYER_LOSE:
                // TODO post end game status to ReviewMenu activity
                break;
            case PLAYER_LOOK:
                // TODO update sprite (orientation)
                // TODO update ambient audio
                break;
            case PLAYER_MOVE:
                // TODO update map buffer
                // TODO update sprite
                // TODO update ambient audio
                break;
            case PLAYER_COLLIDE:
                // TODO play instant audio (grunt or painful sound)
                // TODO provide haptic feedback
                break;
        }
    }
}
