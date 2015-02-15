package com.ss12.csun_mmg.peripheralmaze;

/**
 * Created by Rosy Davis on 2015.02.14.
 */
public class Tile {
    public int[] walls;
    private boolean isStart;
    private boolean isEnd;

    public Tile() {
        this(new int[4], false, false);
    }


    public Tile(int [] walls, boolean isStart, boolean isEnd) {
        System.arraycopy(walls,0, this.walls,0, this.walls.length);
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
                    case 0: spriteIds[numSprites--] = R.drawable.border_top;
                        break;
                    case 1: spriteIds[numSprites--] = R.drawable.border_bottom;
                        break;
                    case 2: spriteIds[numSprites--] = R.drawable.border_right;
                        break;
                    case 3: spriteIds[numSprites--] = R.drawable.border_left;
                        break;
                }
            }
        }

        return spriteIds;
    }
}