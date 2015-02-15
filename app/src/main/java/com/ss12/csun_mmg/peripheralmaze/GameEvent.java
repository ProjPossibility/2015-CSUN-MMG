package com.ss12.csun_mmg.peripheralmaze;

/**
 * Created by cmcateer on 2/15/15.
 */
public class GameEvent {
    public static enum EVENT_TYPE {
        PLAYER_MOVE,
        PLAYER_LOOK,
        PLAYER_COLLIDE,
        PLAYER_LOSE,
        PLAYER_WIN,
    }

    private EVENT_TYPE mEventType;
    private Player mPlayer;

    public GameEvent(EVENT_TYPE eventType, Player player) {
        mEventType = eventType;
        mPlayer = player;
    }

    public EVENT_TYPE getEventType() {
        return mEventType;
    }

    public Player getPlayer() {
        return mPlayer;
    }
}
