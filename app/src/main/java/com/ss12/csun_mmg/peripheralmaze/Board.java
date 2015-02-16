package com.ss12.csun_mmg.peripheralmaze;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

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
    int numBoards = -1;
    int numRows = -1;
    int numCols = -1;

    MazeGame mMazeGame;

    ImageView mMapSprite;

    Resources mResources;
    GestureDetectorCompat mDetector;
    MediaPlayer ambientSound, activeSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResources = getResources();

        setContentView(R.layout.activity_board);

        final View contentView = findViewById(R.id.maze_game_layout);

        mMapSprite = (ImageView)findViewById(R.id.map_sprite);

        mDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float dx = e2.getX() - e1.getX();
                float dy = e2.getY() - e1.getY();
                dx = dx==0 ? 1 : dx;
                dy = dy==0 ? 1 : dy;

                if (Math.abs(dy) >= Math.abs(dx)) {
                    // up/down
                    if (dy <= 0) {
                        // up
                        mMazeGame.movePlayer(Tile.NORTH);
                        Log.v("Board", "Playing moving NORTH");
                    } else {
                        // down
                        mMazeGame.movePlayer(Tile.SOUTH);
                        Log.v("Board", "Playing moving SOUTH");
                    }
                } else {
                    // left/right
                    if (dx <= 0) {
                        // left
                        mMazeGame.movePlayer(Tile.WEST);
                        Log.v("Board", "Playing moving WEST");
                    } else {
                        // right
                        mMazeGame.movePlayer(Tile.EAST);
                        Log.v("Board", "Playing moving EAST");
                    }
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        numBoards = mResources.getInteger(R.integer.numBoards);
        numRows = mResources.getInteger(R.integer.numRows);
        numCols = mResources.getInteger(R.integer.numCols);

        Log.v("Board", "numBoards: "+numBoards);
        Log.v("Board", "numRows: "+numRows);
        Log.v("Board", "numCols: "+numCols);

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
                JSONArray tiles = boardObj.getJSONArray("tiles");

                // iterate over each row x col index combo and get that property value
                for (int row=0; row<numRows; row++) {
                    JSONArray rowData = tiles.getJSONArray(row);
                    for (int col=0; col<numCols; col++) {
                        JSONArray colData = rowData.getJSONArray(col);
                        String position = String.format("%d%d", row, col);
                        if (colData != null && colData.length() == 6) {
                            newMaze.setTile(
                                    position,
                                    new Tile(
                                            position,
                                            new int[]{
                                                    colData.getInt(0),colData.getInt(1),
                                                    colData.getInt(2),colData.getInt(3)
                                            },
                                            colData.getInt(4) == 1,
                                            colData.getInt(5) == 1
                                    )
                            );
                            numGoodTiles++;
                        } else {
                            if (colData== null) {

                            }
                        }
                    }
                }

                // if all tiles were clean, add this maze
                if (numGoodTiles == numRows * numCols) {
                    boards[i] = newMaze;
                    numGoodBoards++;
                } else {
                    Log.e("Board", ((numRows*numCols)-numGoodTiles)+"/"+(numRows*numCols)+" tiles did not get initialized");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } // end for each of the boards

        // TODO what happens if a maze was not set?
        if (numGoodBoards != boards.length) {
            Log.e("Board", (boards.length-numGoodBoards)+"/"+boards.length+" boards did not initialize");
        }

        mMazeGame = new MazeGame(boards[0]);
        mMazeGame.setEventCallback(new GameCallback() {
            @Override
            public void run(GameEvent event) {
                Board.this.onGameEvent(event);
            }
        });

        mMazeGame.start();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void updateUI() {
        Tile tile = mMazeGame.getPlayerTile();
        if (tile == null) {
            return;
        }

        int[] spriteIds = tile.getSpriteIds();
        Log.v("Board.Sprites", "Should only draw "+spriteIds.length+" sprites");
        Drawable[] drawables = new Drawable[spriteIds.length];

        for (int i=0; i<drawables.length; i++) {
            drawables[i] = mResources.getDrawable(spriteIds[i]);
        }
        LayerDrawable layerDrawable = new LayerDrawable(drawables);
        mMapSprite.setImageDrawable(layerDrawable);

        mMapSprite.invalidate();
    }

    private void onGameEvent(GameEvent event) {
        Log.v("Board", "Received game event: " + event.getEventType().name());
        switch (event.getEventType()) {
            case GAME_START:
                // TODO update UI
                break;
            case PLAYER_WIN:
            case PLAYER_LOSE:
                // TODO post end game status to ReviewMenu activity
                break;
            case PLAYER_LOOK:
                // TODO update sprite (orientation)
                // TODO update ambient audio
                break;
            case PLAYER_MOVE:
                // TODO play instant audio
                playSound(this, R.raw.blip_no_wall);
                // TODO update map buffer
                // TODO update sprite
                // TODO update ambient audio
                break;
            case PLAYER_COLLIDE:
                // TODO play instant audio (grunt or painful sound)
                playSound(this, R.raw.blip_wall);
                // TODO provide haptic feedback
                break;
        }
        updateUI();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.mDetector.onTouchEvent(event);
    }

    private void playSound(Context context, int rid) {
        final MediaPlayer audio = MediaPlayer.create(context, rid);
        audio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                audio.release();
            }
        });
        audio.start();
    }
}
