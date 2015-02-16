package com.ss12.csun_mmg.peripheralmaze;

/**
 * Created by cmcateer on 2/14/15.
 */
public class GameMode {

    public enum MODE {
        AUDIO_MODE,
        VISUAL_MODE
    }

    private static MODE gameMode;

    private GameMode(){}

    public static void setMode(MODE newMode) {
        gameMode = newMode;
    }
}
