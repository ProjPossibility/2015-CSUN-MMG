package com.ss12.csun_mmg.peripheralmaze;

/**
 * Created by Rosy Davis on 2015.02.14.
 */
public class Tile {
    public final static int NORTH = 0;
    public final static int EAST = 1;
    public final static int SOUTH = 2;
    public final static int WEST = 3;

    public String mPosition;
    public int[] walls;
    private boolean isStart;
    private boolean isEnd;

    public Tile(String position) {
        this(position, new int[4], false, false);
    }


    public Tile(String position, int [] walls, boolean isStart, boolean isEnd) {
        mPosition = position;
        this.walls = new int[4];
        if (walls != null && walls.length >= this.walls.length) {
            System.arraycopy(walls, 0, this.walls, 0, this.walls.length);
        }
        this.isStart = isStart;
        this.isEnd = isEnd;
    }


    public boolean isEndTile () { return isEnd; }

    public boolean isStartTile () { return isStart; }

    public int[] getSpriteIds() {
        // TODO based on current values, return ID of corresponding sprite file
        int numSprites=0;
        for (int i=0; i<walls.length; i++) {
            if (walls[i]!=0) {
                numSprites++;
            }
        }
        int[] spriteIds = new int[numSprites];
        for (int i=0; i<walls.length; i++) {
            if (walls[i]!=0) {
                switch(i) {
                    case NORTH: spriteIds[--numSprites] = R.drawable.border_top;
                        break;
                    case EAST: spriteIds[--numSprites] = R.drawable.border_right;
                        break;
                    case SOUTH: spriteIds[--numSprites] = R.drawable.border_bottom;
                        break;
                    case WEST: spriteIds[--numSprites] = R.drawable.border_left;
                        break;
                }
            }
        }

        return spriteIds;
    }
}