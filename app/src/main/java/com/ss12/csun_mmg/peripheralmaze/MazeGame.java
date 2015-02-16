package com.ss12.csun_mmg.peripheralmaze;

/**
 * Created by cmcateer on 2/15/15.
 */
public class MazeGame {

    Player mPlayer;
    Maze mMaze;
    Tile mStartTile, mEndTile;
    GameCallback mEventCallback;

    private MazeGame(){}

    public MazeGame(Maze maze) {
        mMaze = maze;
        mStartTile = mMaze.getStartTile();
        mEndTile = mMaze.getEndTile();
        mPlayer = new Player(this, mStartTile.mPosition, 0);

        // TODO randomize player direction
    }

    public void start() {
        signal(GameEvent.EVENT_TYPE.GAME_START, mPlayer);
    }

    public void movePlayer(int direction) {
        if (mPlayer.move(direction)) {
            signal(GameEvent.EVENT_TYPE.PLAYER_MOVE, mPlayer);
            if (getPlayerTile().isEndTile()) {
                signal(GameEvent.EVENT_TYPE.PLAYER_WIN, mPlayer);
            }
        } else {
            signal(GameEvent.EVENT_TYPE.PLAYER_COLLIDE, mPlayer);
        }
    }

    public void lookPlayer(int direction) {
        signal(GameEvent.EVENT_TYPE.PLAYER_LOOK, mPlayer);
    }

    public Tile getPlayerTile() {
        return mPlayer.currentTile;
    }

    public Maze getMaze() {
        return mMaze;
    }

    public void setEventCallback(GameCallback callback) {
        mEventCallback = callback;
    }

    private void signal(GameEvent.EVENT_TYPE eventType, Player player) {
        if (mEventCallback != null) {
            mEventCallback.run(new GameEvent(eventType, player));
        }
    }
}
