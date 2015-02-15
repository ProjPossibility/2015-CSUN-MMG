package com.ss12.csun_mmg.peripheralmaze;

/**
 * Created by cmcateer on 2/15/15.
 */
public class MazeGame {

    Player mPlayer;
    Maze mMaze;
    Tile mStartTile, mEndTile;
    GameCallback mEventCallback;

    public MazeGame(Maze maze) {
        mMaze = maze;
        mStartTile = mMaze.getStartTile();
        mEndTile = mMaze.getEndTile();
        mPlayer = new Player(mStartTile.mPosition, 0);

        // TODO randomize player direction
    }

    public void movePlayer(int direction) {
        mPlayer.move(direction);
        signal(GameEvent.EVENT_TYPE.PLAYER_MOVE, mPlayer);
    }

    public void lookPlayer(int direction) {
        if (mPlayer.look(direction)) {
            signal(GameEvent.EVENT_TYPE.PLAYER_LOOK, mPlayer);
        } else {
            signal(GameEvent.EVENT_TYPE.PLAYER_COLLIDE, mPlayer);
        }
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
