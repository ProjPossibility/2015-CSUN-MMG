package com.ss12.csun_mmg.peripheralmaze;

/**
 * Created by Rosy Davis on 2015.02.14.
 */
public class Tile {
    public int[] walls;
    public boolean isStart;
    public boolean isEnd;

    public Tile() {
        walls  = new int[4];
        isStart = false;
        isEnd = false;
    }


    public Tile(int [] walls, boolean isStart, boolean isEnd) {
        this.walls  = walls;
        this.isStart = isStart;
        this.isEnd = isEnd;
    }


    public boolean isEndTile () { return isEnd; }

    public boolean isStartTile () { return isStart; }

    public void drawTile () {
        /* needs to be filled in */
        /* draw wall for each wall [N E S W] that is 0 in the walls array */
    }
}